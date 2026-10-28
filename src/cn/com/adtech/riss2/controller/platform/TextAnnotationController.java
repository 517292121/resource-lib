package cn.com.adtech.riss2.controller.platform;

import cn.com.adtech.riss2.annotation.TextAnchor;
import cn.com.adtech.riss2.annotation.TextAnnotationRepository;
import cn.com.adtech.riss2.bso.platform.IResourceSearchService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.CurrentUserSession;
import cn.com.adtech.riss2.core.IpUtil;
import cn.com.adtech.riss2.core.file.ResourceDirectoryFiles;
import cn.com.adtech.riss2.core.file.TextFilePreview;
import cn.com.adtech.riss2.domain.SysUserType;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/** Public reading, researcher-owned annotations, and authenticated console moderation. */
@Controller
public class TextAnnotationController {
    private static final Log LOG = LogFactory.getLog(TextAnnotationController.class);
    private static final String CSRF = TextAnnotationController.class.getName() + ".csrf";
    @Resource(name="textAnnotationRepository") protected TextAnnotationRepository repository;
    @Resource(name="resourceSearchService") protected IResourceSearchService resources;

    protected SysUserinfo member(HttpServletRequest request) {
        Object sessionUser = request.getSession().getAttribute("sysUserinfo");
        if (!(sessionUser instanceof SysUserinfo)) return null;
        Long id = ((SysUserinfo)sessionUser).getSuinId();
        if (id == null || id < 1) return null;
        // Re-read the member so changing/suspending a membership also changes annotation permissions.
        SysUserinfo user = (SysUserinfo)resources.get(SysUserinfo.class, id);
        return user == null || Boolean.TRUE.equals(user.getSuinIsStop()) ? null : user;
    }

    private boolean researcher(SysUserinfo user) {
        return user != null && user.getSysUserType() != null
                && "研究人员".equals(trim(user.getSysUserType().getSutyName()));
    }

    protected boolean administrator(HttpServletRequest request) {
        Object value = request.getSession().getAttribute("userSession");
        if (!(value instanceof CurrentUserSession)) return false;
        CurrentUserSession session = (CurrentUserSession)value;
        return session.getCurrentUser() != null && session.getCurrentUser().getSmanId() != null
                && session.getCurrentUser().getSmanId() > 0 && session.isManager();
    }

    protected ResSourceData readable(String code, HttpServletRequest request) {
        Long id;
        try { id = Long.valueOf(code); }
        catch (NumberFormatException e) { try { id = CommonUtil.strToId(code); } catch (Exception invalid) { id = null; } }
        if (id == null || id < 1) throw new Problem(404,"资源不存在或已下架。");
        ResSourceData resource = (ResSourceData)resources.get(ResSourceData.class,id);
        if (resource == null || !Long.valueOf(3).equals(resource.getRsdaStatus())
                || !ResSourceData.ISUSE_USEABLENESS.equals(resource.getRsdaIsuse())
                || resource.getResMetaStore() == null || !Long.valueOf(1).equals(resource.getResMetaStore().getRmstStatus())) {
            throw new Problem(404,"资源不存在或已下架。");
        }
        SysUserinfo user = member(request);
        SysUserType type = user == null ? (SysUserType)resources.get(SysUserType.class,SysUserType.USER_TYPE_VISITOR) : user.getSysUserType();
        if (type == null || type.getSutyPurview() == null || type.getSutyPurview() < SysUserType.PURIVEW_BROWSE_VIEW) {
            throw new Problem(403,"您没有该资源的阅读权限。");
        }
        List<String> ips = resources.getStoreIpList(resource.getRsdaRmstId());
        if (user == null && ips != null && !ips.isEmpty() && !IpUtil.isInMyIp(request.getRemoteAddr(),ips)) {
            throw new Problem(403,"请登录后查看该资源的批注。");
        }
        if (TextFilePreview.supports(resource)) {
            if(type.getSutyPurview()<SysUserType.PURIVEW_BROWSE_SOURCE)throw new Problem(403,"您没有该资源的阅读权限。");
        } else if (resource.getRsdaSourceType() == null || resource.getRsdaSourceType() != 1 || trim(resource.getRsdaFileUrl()).isEmpty()) {
            throw new Problem(400,"该资源不是目录文本资源。");
        }
        return resource;
    }

    protected String chapterText(ResSourceData resource, String chapter) throws Exception {
        if (chapter.length() > 1024) throw new Problem(400,"章节名称过长。");
        if (TextFilePreview.supports(resource)) {
            if(!TextFilePreview.CHAPTER.equals(chapter))throw new Problem(404,"文本章节不存在。");
            return TextAnchor.normalize(TextFilePreview.read(resource));
        }
        File root = new File(resource.getRsdaFileUrl());
        for (File file : ResourceDirectoryFiles.textFiles(root.getPath())) {
            if (ResourceDirectoryFiles.relativeTitle(root,file).equals(chapter)) {
                return TextAnchor.normalize(ResourceDirectoryFiles.readText(file));
            }
        }
        throw new Problem(404,"文本章节不存在。");
    }

    @RequestMapping(value="/res/{code}/annotations",method=RequestMethod.GET)
    public void list(@PathVariable String code, HttpServletRequest request, HttpServletResponse response) throws Exception {
        reply(response, () -> {
            ResSourceData resource = readable(code,request);
            String chapter = TextAnchor.chapterParameter(request,"chapter");
            SysUserinfo user = member(request);
            Map<String,Object> result = permissions(request,user);
            List<Map<String,Object>> rows;
            Map<String,String> hashes = new LinkedHashMap<String,String>();
            if (!chapter.isEmpty()) {
                // Retain compatibility for existing callers requesting one specific chapter.
                hashes.put(chapter,TextAnchor.hash(chapterText(resource,chapter)));
                rows = repository.chapter(resource.getRsdaId(),chapter);
                result.put("textHash",hashes.get(chapter));
            } else {
                rows = repository.resource(resource.getRsdaId());
                java.util.Set<String> annotated = new java.util.HashSet<String>();
                for (Map<String,Object> row : rows) annotated.add((String)row.get("chapter"));
                if (TextFilePreview.supports(resource)) {
                    if(annotated.contains(TextFilePreview.CHAPTER))hashes.put(TextFilePreview.CHAPTER,TextAnchor.hash(TextFilePreview.read(resource)));
                } else {
                    File root = new File(resource.getRsdaFileUrl());
                    for (File file : ResourceDirectoryFiles.textFiles(root.getPath())) {
                        String path = ResourceDirectoryFiles.relativeTitle(root,file);
                        if (annotated.contains(path)) hashes.put(path,TextAnchor.hash(ResourceDirectoryFiles.readText(file)));
                    }
                }
            }
            int number = 0;
            for (Map<String,Object> row : rows) {
                row.put("number",++number);
                row.put("editable",researcher(user) && String.valueOf(user.getSuinId()).equals(row.get("userId")));
                String hash = hashes.get(row.get("chapter"));
                row.put("stale", hash == null || !hash.equals(row.get("textHash")));
                row.remove("userId");
                row.remove("auditRemark");row.remove("auditedAt");
            }
            result.put("annotations",rows);
            return result;
        });
    }

    @RequestMapping(value="/res/{code}/annotations",method=RequestMethod.POST)
    public void create(@PathVariable String code, @RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        reply(response, () -> {
            SysUserinfo user = writer(request); csrf(request);
            ResSourceData resource = readable(code,request);
            JSONObject data = json(body); String content = content(data);
            String chapter = field(data,"chapter"), quote = field(data,"quote"), hash = field(data,"textHash");
            int start = integer(data,"start"), end = integer(data,"end"); String scope = field(data,"scope");
            if (!"selection".equals(scope) && !"page".equals(scope)) throw new Problem(400,"请选择选文批注或整页批注。");
            String text = chapterText(resource,chapter);
            if (!TextAnchor.matches(text,hash,start,end,quote) || quote.trim().isEmpty() || quote.length() > 100000) {
                throw new Problem(409,"原文位置已改变，请刷新页面后重新选择文字。");
            }
            String id = repository.create(resource.getRsdaId(),user.getSuinId(),chapter,hash,start,end,scope,quote,content);
            Map<String,Object> result = ok(); result.put("id",id); result.put("auditStatus",TextAnnotationRepository.PENDING);
            result.put("message","批注已提交，等待管理员审核。可在个人中心查看审核状态。"); return result;
        });
    }

    @RequestMapping(value="/res/{code}/annotations/{id}",method=RequestMethod.POST)
    public void edit(@PathVariable String code, @PathVariable String id, @RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        mutate(code,id,body,false,false,request,response);
    }
    @RequestMapping(value="/res/{code}/annotations/{id}/delete",method=RequestMethod.POST)
    public void delete(@PathVariable String code, @PathVariable String id, @RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        mutate(code,id,body,true,false,request,response);
    }

    private void mutate(String code, String id, String body, boolean deleting, boolean admin,
                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        reply(response, () -> {
            SysUserinfo user = null;
            if (admin) requireAdmin(request); else user = writer(request);
            csrf(request); JSONObject data = json(body);
            Map<String,Object> row = repository.find(id);
            if (row == null) throw new Problem(404,"批注已被删除。");
            long owner = user == null ? 0 : user.getSuinId();
            if (!admin) {
                if (!String.valueOf(owner).equals(row.get("userId"))) throw new Problem(403,"只能编辑或删除自己的批注。");
                // Personal records remain manageable even after a source is removed; no source text is exposed here.
                if (!row.get("resourceId").equals(String.valueOf(decode(code)))) throw new Problem(404,"批注与资源不对应。");
            }
            int version = integer(data,"version");
            boolean changed = deleting ? repository.delete(id,owner,admin,version) : repository.edit(id,owner,admin,version,content(data));
            if (!changed) throw new Problem(409,"批注已被其他页面修改，请刷新后重试。");
            Map<String,Object> result = ok(); result.put("message",deleting ? "批注已删除。" : "批注已更新，等待管理员重新审核。"); return result;
        });
    }

    @RequestMapping(value="/member/annotations",method=RequestMethod.GET)
    public String personal(HttpServletRequest request, HttpServletResponse response, Model model) {
        if (member(request) == null) return "redirect:/member/login";
        model.addAttribute("annotationAdmin",false); return "platform/userCenter/usercenterAnnotations";
    }

    @RequestMapping(value="/member/annotations/data",method=RequestMethod.GET)
    public void personalData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        reply(response, () -> {
            SysUserinfo user = member(request);
            if (user == null) throw new Problem(401,"请先登录。");
            return records(request,user,false);
        });
    }

    @RequestMapping(value="/console/annotations",method=RequestMethod.GET)
    public String admin(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        if (!administrator(request)) { response.sendError(403,"请使用后台管理员账户登录。"); return null; }
        model.addAttribute("annotationAdmin",true); return "platform/userCenter/usercenterAnnotations";
    }
    @RequestMapping(value="/console/annotations/data",method=RequestMethod.GET)
    public void adminData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        reply(response, () -> { requireAdmin(request); return records(request,null,true); });
    }
    @RequestMapping(value="/console/annotations/{id}",method=RequestMethod.POST)
    public void adminEdit(@PathVariable String id, @RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        mutate(null,id,body,false,true,request,response);
    }
    @RequestMapping(value="/console/annotations/{id}/delete",method=RequestMethod.POST)
    public void adminDelete(@PathVariable String id, @RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        mutate(null,id,body,true,true,request,response);
    }

    @RequestMapping(value="/console/annotations/{id}/review",method=RequestMethod.POST)
    public void review(@PathVariable String id,@RequestBody String body,HttpServletRequest request,HttpServletResponse response) throws Exception {
        reply(response,()->{
            requireAdmin(request);csrf(request);
            JSONObject data=json(body);String auditStatus=field(data,"auditStatus");
            if(!TextAnnotationRepository.APPROVED.equals(auditStatus)&&!TextAnnotationRepository.REJECTED.equals(auditStatus))throw new Problem(400,"请选择审核通过或不通过。");
            String remark=data.containsKey("remark")?field(data,"remark").trim():"";
            if(remark.length()>1000||remark.indexOf('\0')>=0)throw new Problem(400,"审核说明最多 1000 个字符。");
            if(repository.find(id)==null)throw new Problem(404,"批注已被删除。");
            CurrentUserSession session=(CurrentUserSession)request.getSession().getAttribute("userSession");
            if(!repository.review(id,session.getCurrentUser().getSmanId(),integer(data,"version"),auditStatus,remark))throw new Problem(409,"批注已被修改，请刷新后重新审核。");
            Map<String,Object> result=ok();result.put("message",TextAnnotationRepository.APPROVED.equals(auditStatus)?"审核通过，批注已在文献页显示。":"审核未通过，批注不会在文献页显示。");return result;
        });
    }

    private Map<String,Object> records(HttpServletRequest request, SysUserinfo user, boolean admin) {
        int page = 1; try { page = Integer.parseInt(request.getParameter("page")); } catch (Exception ignore) { }
        String search = trim(request.getParameter("q")); if (search.length()>100) search=search.substring(0,100);
        String auditStatus=request.getParameter("auditStatus");
        if(auditStatus==null)auditStatus=admin?TextAnnotationRepository.PENDING:"";
        if(!auditStatus.isEmpty()&&!TextAnnotationRepository.PENDING.equals(auditStatus)&&!TextAnnotationRepository.APPROVED.equals(auditStatus)&&!TextAnnotationRepository.REJECTED.equals(auditStatus))throw new Problem(400,"审核状态无效。");
        Map<String,Object> result = repository.records(admin ? null : user.getSuinId(),search,page,auditStatus);
        for (Map<String,Object> row : (List<Map<String,Object>>)result.get("records")) {
            row.put("resourceCode",CommonUtil.idToString(String.valueOf(row.get("resourceId"))));
            row.put("editable",admin || researcher(user)); row.remove("userId");
        }
        result.putAll(permissions(request,user)); result.put("admin",admin); return result;
    }

    private Map<String,Object> permissions(HttpServletRequest request, SysUserinfo user) {
        Map<String,Object> result=ok(); result.put("authenticated",user!=null); result.put("canWrite",researcher(user));
        if (user != null || administrator(request)) {
            synchronized (request.getSession()) {
                if (request.getSession().getAttribute(CSRF)==null) request.getSession().setAttribute(CSRF,UUID.randomUUID().toString());
                result.put("csrfToken",request.getSession().getAttribute(CSRF));
            }
        }
        return result;
    }
    private void csrf(HttpServletRequest request) {
        Object token=request.getSession().getAttribute(CSRF);
        String supplied=request.getHeader("X-Annotation-CSRF");
        if (token==null || supplied==null || !java.security.MessageDigest.isEqual(token.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8),supplied.getBytes(java.nio.charset.StandardCharsets.UTF_8))) {
            throw new Problem(403,"页面验证已失效，请刷新后重试。");
        }
    }
    private SysUserinfo writer(HttpServletRequest request) {
        SysUserinfo user=member(request);
        if(user==null) throw new Problem(401,"请先登录研究人员账户。");
        if(!researcher(user)) throw new Problem(403,"仅研究人员可新增、编辑或删除批注。");
        return user;
    }
    private void requireAdmin(HttpServletRequest request) { if(!administrator(request)) throw new Problem(403,"请使用后台管理员账户登录。"); }
    private static Long decode(String code) { try{return Long.valueOf(code);}catch(Exception e){try{return CommonUtil.strToId(code);}catch(Exception invalid){return null;}} }
    private static String trim(String text) { return text==null ? "" : text.trim(); }
    private static String field(JSONObject data,String key) { if(!data.containsKey(key)||data.get(key)==null||data.get(key)==net.sf.json.JSONNull.getInstance()) throw new Problem(400,"缺少批注参数。"); return data.getString(key); }
    private static int integer(JSONObject data,String key) { String value=field(data,key); if(!value.matches("[0-9]{1,9}")) throw new Problem(400,"批注位置或版本无效。"); return Integer.parseInt(value); }
    private static JSONObject json(String body) { if(body==null||body.length()>700000) throw new Problem(400,"批注数据过大。"); try{return JSONObject.fromObject(body);}catch(Exception e){throw new Problem(400,"批注数据格式不正确。");} }
    private static String content(JSONObject data) { String text=field(data,"content").trim(); if(text.isEmpty()||text.length()>2000||text.indexOf('\0')>=0) throw new Problem(400,"批注内容请填写 1–2000 个字符。"); return text; }
    private static Map<String,Object> ok() { Map<String,Object> result=new LinkedHashMap<String,Object>();result.put("success",true);return result; }
    private interface Work { Map<String,Object> execute() throws Exception; }
    private void reply(HttpServletResponse response, Work work) throws Exception {
        response.setContentType("application/json;charset=UTF-8"); response.setHeader("Cache-Control","no-store");
        Map<String,Object> result;
        try { result=work.execute(); }
        catch(Problem problem) { response.setStatus(problem.status);result=new LinkedHashMap<String,Object>();result.put("success",false);result.put("message",problem.getMessage()); }
        catch(Exception error) { LOG.error("Text annotation operation failed",error);response.setStatus(500);result=new LinkedHashMap<String,Object>();result.put("success",false);result.put("message","批注暂时无法处理，请稍后重试。"); }
        response.getWriter().write(JSONObject.fromObject(result).toString());
    }
    private static class Problem extends RuntimeException { final int status; Problem(int status,String message){super(message);this.status=status;} }
}
