package cn.com.adtech.riss2.controller.console.text;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import cn.com.adtech.riss2.bso.text.IResourceInputService;
import cn.com.adtech.riss2.bso.text.ITextLibClassfiyService;
import cn.com.adtech.riss2.bso.text.ITextResourceService;
import cn.com.adtech.riss2.controller.FileUploadController;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ImgHandleUtil;
import cn.com.adtech.riss2.domain.text.ResTextLibrary;
import cn.com.adtech.riss2.domain.text.ResTextResource;
import cn.com.unionlib.lucene.bean.ResMainBean;
import cn.com.unionlib.lucene.dao.ILuceneDaoService;
import cn.com.unionlib.lucene.dao.LuceneDaoServiceImp;

import com.jbetter.framework.session.Pagination;
/**
 * 资源录入
 *   
 * TestResourceController  
 *   
 * 2017-10-17 下午01:02:16  
 *   
 * @athor lichangkun
 * @version 1.0.0  
 *
 */
public class TextResourceController extends FileUploadController {
    private ITextResourceService textResourceService;
    private IResourceInputService resourceInputService;
    private ITextLibClassfiyService textLibClassfiyService;
    
    public void setTextLibClassfiyService(ITextLibClassfiyService textLibClassfiyService) {
        this.textLibClassfiyService = textLibClassfiyService;
    }
    public void setResourceInputService(IResourceInputService resourceInputService) {
        this.resourceInputService = resourceInputService;
    }
    public void setTextResourceService(ITextResourceService textResourceService) {
        this.textResourceService = textResourceService;
    }
    
    private ResourceFileUploadUtil upload = new ResourceFileUploadUtil();
    
    //索引管理
    private ILuceneDaoService luceneDaoService = new LuceneDaoServiceImp();
    /**
     * 进入资源录入第一个页面
     */
    @Override
    protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return new ModelAndView("text/initTextResource");
    }
    
    /**
     * 录入资源（单条）
     * 
     * @athor lichangkun
     * 2017-10-18
     */
    public ModelAndView inputResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        //资源库ID
        Long rtliId = ServletRequestUtils.getLongParameter(request, "rtliId", 0L);
        ResTextLibrary rtli = (ResTextLibrary) this.resourceInputService.get(ResTextLibrary.class, rtliId); 
        
        //录入页面请求来源
        String source = ServletRequestUtils.getStringParameter(request, "source", "");
        //资源库分类下拉选项
        String libClassfiyOptions = this.textLibClassfiyService.getLibClassfiyOptions(rtliId, null);
        
        map.put("rtli", rtli);
        map.put("rtliId", rtliId);
        map.put("source", source);
        map.put("libClassfiyOptions", libClassfiyOptions);
        return new ModelAndView("text/inputResource", map);
    }
    
    /**
     * 修改资源
     *
     * @author MaChao
     * @date Nov 18, 2015
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ModelAndView updateResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Long rtreId = ServletRequestUtils.getLongParameter(request, "rtreId", 0L);
        
        ResTextResource resource = (ResTextResource) this.resourceInputService.get(ResTextResource.class, rtreId);
        
        //资源库ID
        Long rtliId = resource.getRtreRtliId();
        ResTextLibrary rtli = null;
        if(rtliId != null && rtliId > 0L){
            rtli = (ResTextLibrary) this.resourceInputService.get(ResTextLibrary.class, resource.getRtreRtliId()); 
        }
        
        //录入页面请求来源
        String source = ServletRequestUtils.getStringParameter(request, "source", "");
        //资源库分类下拉选项
        String libClassfiyOptions = this.textLibClassfiyService.getLibClassfiyOptions(rtliId, resource.getRtreRlclId());
        
        map.put("rtli", rtli);
        map.put("rtliId", rtliId);
        map.put("source", source);
        map.put("resource", resource);
        map.put("libClassfiyOptions", libClassfiyOptions);
        return new ModelAndView("text/inputResource", map);
    }
    
    /**
     * 进入资源列表
     * 
     * @athor lichangkun
     * 2017-10-18
     */
    public ModelAndView resourceList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        //资源库ID
        Long rtliId = ServletRequestUtils.getLongParameter(request, "rtliId", 0L);
        if(rtliId != null && rtliId > 0L){
            ResTextLibrary rtli = (ResTextLibrary) this.resourceInputService.get(ResTextLibrary.class, rtliId);
            map.put("rtli", rtli);
        }
        
        map.put("rtliId", rtliId);
        return new ModelAndView("text/resourceListByLib", map);
    }
    
    /**
     * 获取资源列表JSON
     * 
     * @athor lichangkun
     * 2017-10-19
     */
    public void getResourceListJson(HttpServletRequest request,HttpServletResponse response) throws IOException{
        //排序方式
        String sort = ServletRequestUtils.getStringParameter(request, "sort", "rtreDate");// 排序列
        String dir = ServletRequestUtils.getStringParameter(request, "dir", "ASC");// 排序方式
        int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
        int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
        String key = ServletRequestUtils.getStringParameter(request, "eleCode1", ""); //查询字段
        String val = ServletRequestUtils.getStringParameter(request, "indexKey1", ""); //查询关键字
        
        int currentPage = start / limit;
        // 添加分页器
        Pagination pagination = new Pagination();
        // 设置当前页
        pagination.setCurrentPage(currentPage);
        // 设置每页记录数
        pagination.setPageResults(limit);
        if(sort.equals("rlclName")){
            sort = "rlcl.rlclName";
        }else{
            sort = "rtre." + sort;
        }
        Long rtliId = ServletRequestUtils.getLongParameter(request, "rtliId", 0L);
        String keyStr = "";
        if(rtliId > 0L){
            ResTextLibrary rtli = (ResTextLibrary)this.resourceInputService.get(ResTextLibrary.class, rtliId);
            
            //获取字段
            if(rtli.getRtliCol1Label() != null && CommonUtil.isNotNull(rtli.getRtliCol1Label())){
                keyStr += "rtre.rtreCol1,";
            }
            if(rtli.getRtliCol2Label() != null && CommonUtil.isNotNull(rtli.getRtliCol2Label())){
                keyStr += "rtre.rtreCol2,";
            }
            if(rtli.getRtliCol3Label() != null && CommonUtil.isNotNull(rtli.getRtliCol3Label())){
                keyStr += "rtre.rtreCol3,";
            }
            if(rtli.getRtliCol4Label() != null && CommonUtil.isNotNull(rtli.getRtliCol4Label())){
                keyStr += "rtre.rtreCol4,";
            }
            if(rtli.getRtliCol5Label() != null && CommonUtil.isNotNull(rtli.getRtliCol5Label())){
                keyStr += "rtre.rtreCol5,";
            }
            if(rtli.getRtliCol6Label() != null && CommonUtil.isNotNull(rtli.getRtliCol6Label())){
                keyStr += "rtre.rtreCol6,";
            }
            if(rtli.getRtliCol7Label() != null && CommonUtil.isNotNull(rtli.getRtliCol7Label())){
                keyStr += "rtre.rtreCol7,";
            }
            if(rtli.getRtliCol8Label() != null && CommonUtil.isNotNull(rtli.getRtliCol8Label())){
                keyStr += "rtre.rtreCol8,";
            }
            if(rtli.getRtliCol9Label() != null && CommonUtil.isNotNull(rtli.getRtliCol9Label())){
                keyStr += "rtre.rtreCol9,";
            }
            if(rtli.getRtliCol10Label() != null && CommonUtil.isNotNull(rtli.getRtliCol10Label())){
                keyStr += "rtre.rtreCol10,";
            }
            if(rtli.getRtliCol11Label() != null && CommonUtil.isNotNull(rtli.getRtliCol11Label())){
                keyStr += "rtre.rtreCol11,";
            }
            if(rtli.getRtliCol12Label() != null && CommonUtil.isNotNull(rtli.getRtliCol12Label())){
                keyStr += "rtre.rtreCol12,";
            }
            if(rtli.getRtliCol13Label() != null && CommonUtil.isNotNull(rtli.getRtliCol13Label())){
                keyStr += "rtre.rtreCol13,";
            }
            if(rtli.getRtliCol14Label() != null && CommonUtil.isNotNull(rtli.getRtliCol14Label())){
                keyStr += "rtre.rtreCol14,";
            }
            if(rtli.getRtliCol15Label() != null && CommonUtil.isNotNull(rtli.getRtliCol15Label())){
                keyStr += "rtre.rtreCol15,";
            }
            
        }
        //获取列表内容
        List<Object[]> resourceList = this.textResourceService.getResourceList(pagination, rtliId, 0L, keyStr, key, val, sort, dir);
        
        //处理参数
        keyStr = keyStr.replace("rtre.", "");
        String[] temple = null;
        if(keyStr.length() > 0){
            keyStr = keyStr.substring(0, keyStr.length()-1);
            temple = keyStr.split(",");
        }
        
        //添加参数
        int len = temple==null?6:temple.length + 6;
        String[] keys = new String[len];
        keys[0] = "rtreId";
        keys[1] = "rtreTitle";
        if(temple != null && temple.length > 0){
            for (int i = 1; i <= temple.length; i++) {
                keys[i+1] = temple[i-1];
            }
        }
        keys[len-4] = "rlclName";
        keys[len-3] = "rtreUsed";
        keys[len-2] = "rtreRecommend";
        keys[len-1] = "rtreDate";
        
        //获取json
        String jsondata = CommonUtil.createJsonTable(resourceList, keys, pagination.getTotalResults() + "");
        
        response.setContentType("txt/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsondata);
        out.flush();
    }
    
    /**
     * 保存或修改资源
     * 
     * @athor lichangkun
     * 2017-10-18
     */
    public ModelAndView saveResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Long rtreRlclId = ServletRequestUtils.getLongParameter(request, "rtreRlclId", 0L);
        Long rtliId = ServletRequestUtils.getLongParameter(request, "rtliId", 0L);
        Long rtreId = ServletRequestUtils.getLongParameter(request, "rtreId", 0L);
        int coverChange = ServletRequestUtils.getIntParameter(request, "coverChange", 0);
        String rtreCover = ServletRequestUtils.getStringParameter(request, "rtreCover", "");
        
        ResTextResource resource = null;
        if(rtreId == null || rtreId < 1){
            resource = new ResTextResource();
            //录入时间
            resource.setRtreDate(new Date());
            //是否启用（默认为1启用，0不启用）
            resource.setRtreUsed(1L);
            //是否推荐（默认为0不启用，1启用）
            resource.setRtreRecommend(0L);
            //访问量默认0
            resource.setRtreAccess(0L);
            //如果是新增，则直接上传封面处理
            if(coverChange > 0){
                String[] arr = upload.uploadfile(request, "", "rtreCover");
                ImgHandleUtil.pictureHandle(arr[0], 180, 250, false);
                resource.setRtreCover(arr[1]);
            }
        }else{
            resource = (ResTextResource)this.resourceInputService.get(ResTextResource.class, rtreId);
            
            //如果封面发生了变化
            if(coverChange == 1){
                if(CommonUtil.isNotNull(resource.getRtreCover())){
                    deleteFile(request, resource.getRtreCover());
                    resource.setRtreCover(null);
                }
                String[] arr = upload.uploadfile(request, "", "rtreCover");
                if(null != arr){
                    resource.setRtreCover(arr[1]);
                    ImgHandleUtil.pictureHandle(arr[0], 180, 250, false);
                }
            }
        }
        
        String rtreTitle = ServletRequestUtils.getStringParameter(request, "rtreTitle", "").trim();
        String rtreCol1 = ServletRequestUtils.getStringParameter(request, "rtreCol1", "").trim();
        String rtreCol2 = ServletRequestUtils.getStringParameter(request, "rtreCol2", "").trim();
        String rtreCol3 = ServletRequestUtils.getStringParameter(request, "rtreCol3", "").trim();
        String rtreCol4 = ServletRequestUtils.getStringParameter(request, "rtreCol4", "").trim();
        String rtreCol5 = ServletRequestUtils.getStringParameter(request, "rtreCol5", "").trim();
        String rtreCol6 = ServletRequestUtils.getStringParameter(request, "rtreCol6", "").trim();
        String rtreCol7 = ServletRequestUtils.getStringParameter(request, "rtreCol7", "").trim();
        String rtreCol8 = ServletRequestUtils.getStringParameter(request, "rtreCol8", "").trim();
        String rtreCol9 = ServletRequestUtils.getStringParameter(request, "rtreCol9", "").trim();
        String rtreCol10 = ServletRequestUtils.getStringParameter(request, "rtreCol10", "").trim();
        String rtreCol11 = ServletRequestUtils.getStringParameter(request, "rtreCol11", "").trim();
        String rtreCol12 = ServletRequestUtils.getStringParameter(request, "rtreCol12", "").trim();
        String rtreCol13 = ServletRequestUtils.getStringParameter(request, "rtreCol13", "");
        String rtreCol14 = ServletRequestUtils.getStringParameter(request, "rtreCol14", "");
        String rtreCol15 = ServletRequestUtils.getStringParameter(request, "rtreCol15", "");
        
        resource.setRtreTitle(rtreTitle);
        resource.setRtreRtliId(rtliId);
        resource.setRtreRlclId(rtreRlclId < 1L ? null : rtreRlclId);
        resource.setRtreCol1(rtreCol1);
        resource.setRtreCol2(rtreCol2);
        resource.setRtreCol3(rtreCol3);
        resource.setRtreCol4(rtreCol4);
        resource.setRtreCol5(rtreCol5);
        resource.setRtreCol6(rtreCol6);
        resource.setRtreCol7(rtreCol7);
        resource.setRtreCol8(rtreCol8);
        resource.setRtreCol9(rtreCol9);
        resource.setRtreCol10(rtreCol10);
        resource.setRtreCol11(rtreCol11);
        resource.setRtreCol12(rtreCol12);
        resource.setRtreCol13(rtreCol13);
        resource.setRtreCol14(rtreCol14);
        resource.setRtreCol15(rtreCol15);
        
        map.put("resource", resource);
        if(rtreTitle == null || rtreTitle.length() == 0){
            getElement(map, rtliId);
            map.put("result", "<script>Msg.success('请输入题名标题！');</script>");
            return new ModelAndView("text/inputResource", map);
        }
        
//        if(rtreRlclId == 0L){
//            getElement(map, rtliId);
//            map.put("result", "<script>Msg.success('请选择分类！');</script>");
//            return new ModelAndView("text/inputResource", map);
//        }
        
        //资源库语种
        String libLan = this.textResourceService.getLibLanguage(rtliId);
        resource.setRtreLanguage(libLan);
        
        
        //保存请求来源类型
        String source = ServletRequestUtils.getStringParameter(request, "source", "");
        request.setAttribute("source", source);
        try {
            this.resourceInputService.save(resource);
            
            //加入索引
            ResMainBean data = this.resourceInputService.textResToDatabean(resource);
            if(rtreId == null || rtreId < 1L){
            	luceneDaoService.addDatabean(data);
            }else{
            	//修改索引
            	luceneDaoService.updateDatabean(data);
            }
            request.setAttribute("result", "<script>Msg.success('保存成功！');</script>");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("result", "<script>Msg.fail('保存失败！');</script>");
        }
        
        request.setAttribute("resource", resource);
        if(source.equals("manager")){
            return new ModelAndView("text/textResManager");
        }else{
            return resourceList(request, response);
        }
    }
    
    //查询必要条件
    public void getElement(Map<String, Object> map, Long rtliId){
        //资源库ID
        ResTextLibrary rtli = (ResTextLibrary) this.resourceInputService.get(ResTextLibrary.class, rtliId); 
        //资源库分类下拉选项
        String libClassfiyOptions = this.textLibClassfiyService.getLibClassfiyOptions(rtliId, null);
        
        map.put("rtli", rtli);
        map.put("rtliId", rtliId);
        map.put("libClassfiyOptions", libClassfiyOptions);
    }
    
    /**
     * 删除资源
     *
     * @author MaChao
     * @date Nov 18, 2015
     *
     * @param request
     * @param rtreId2
     * @throws Exception
     */
    public void deleteResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        String[] idArr = ids.split(",");
        
        String jsondata ="";
        try {
            if(idArr.length > 0){
                for(int i=0; i<idArr.length; i++){
                    try {
                        Long rtreId = Long.parseLong(idArr[i]);
                        deleteResource(request, rtreId);
                        
                        //删除索引
                        this.luceneDaoService.deleteDatabean(idArr[i]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            jsondata="1";
        } catch (Exception e) {
            jsondata="0";
            e.printStackTrace();
        }
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsondata);
        out.flush();
    }
    
    private void deleteResource(HttpServletRequest request, Long rtreId){
        ResTextResource resource = (ResTextResource) this.resourceInputService.get(ResTextResource.class, rtreId);
        try {
            //判断删除资源的相关文件
            if(CommonUtil.isNotNull(resource.getRtreCover())){
                this.deleteFile(request, resource.getRtreCover());
            }
            
            //删除资源
            this.resourceInputService.delete(resource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
   
    
    
}




