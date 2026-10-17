package cn.com.adtech.riss2.controller.console.text;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import cn.com.adtech.riss2.bso.text.ITextLibraryService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.text.ResTextLibrary;

import com.jbetter.framework.session.Pagination;

/**
 * 资源库管理
 *   
 * TextLibraryController  
 *   
 * 2017-10-17 上午11:32:16  
 *   
 * @athor lichangkun
 * @version 1.0.0  
 *
 */
public class TextLibraryController extends MultiActionController {
    private ITextLibraryService textLibraryService;
    public void setTextLibraryService(ITextLibraryService textLibraryService) {
        this.textLibraryService = textLibraryService;
    }

    @Override
    protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return new ModelAndView("text/textLibrary");
    }

    /**
     * 获取文档资源库json数据
     * 
     * @athor lichangkun
     * 2017-10-17
     */
    public void getLibListJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 排序方式
        String sort = ServletRequestUtils.getStringParameter(request, "sort", "rtliIndex");
        String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");

        //翻页
        int start = ServletRequestUtils.getIntParameter(request, "start", 0);
        int limit = ServletRequestUtils.getIntParameter(request, "limit", 20);
        int currentPage = start / limit;
        // 添加分页器
        Pagination pagination = new Pagination();
        // 设置当前页
        pagination.setCurrentPage(currentPage);
        // 设置每页记录数
        pagination.setPageResults(limit);

        //获取分类
        List<Object[]> resTypeList = this.textLibraryService.getLibraryList(sort, dir, pagination);
        //将分类转换为JSON
        String[] keys = { "rtliId", "rtliName", "rtliIndex", "rtliLanguage"};
        String jsondata = CommonUtil.createJsonTable(resTypeList, keys, pagination.getTotalResults() + "");

        response.setContentType("txt/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsondata);
        out.flush();
    }
    
    /**
     * 新增、编辑资源库
     */
    public ModelAndView addLibrary(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        //资源库ID
        Long rtliId = ServletRequestUtils.getLongParameter(request, "rtliId", 0L);
        if(rtliId != null && rtliId > 0L  ){
            //进入编辑资源类型页面
            ResTextLibrary lib = (ResTextLibrary) this.textLibraryService.get(ResTextLibrary.class, rtliId);
            map.put("lib", lib);
            return new ModelAndView("text/addTextLibrary",map);
        }else{
            //进入新增资源类型页面
            return new ModelAndView("text/addTextLibrary");
        }
    }
    
    /**
     * 保存资源库
     */
    public ModelAndView saveLibrary(HttpServletRequest request, HttpServletResponse response, ResTextLibrary resTextLibrary){
        Map<String, Object> map = new HashMap<String, Object>();
        //判断是否有重复的Excel设置
        if(!getIsFine(resTextLibrary)){
            map.put("lib", resTextLibrary);
            map.put("result", "<script>Msg.fail('Excel模板设置不可以重复，请确认后保存！');</script>");
            return new ModelAndView("text/addTextLibrary",map);
        }
        
        try {
            this.textLibraryService.save(resTextLibrary); 
            this.textLibraryService.updateCache();
        } catch (Exception e) {
            map.put("lib", resTextLibrary);
            return new ModelAndView("text/addTextLibrary",map);
        }
        
        return new ModelAndView("text/textLibrary");
    }
    
    public boolean getIsFine(ResTextLibrary resTextLibrary){
        String Str = "";
        if(resTextLibrary == null){
            return false;
        }
        
        if(null != resTextLibrary.getRtliTitleImp() && resTextLibrary.getRtliTitleImp().length() > 0){
            Str += resTextLibrary.getRtliTitleImp() + ",";
        }
        if(null != resTextLibrary.getRtliCol1Imp() && resTextLibrary.getRtliCol1Imp().length() > 0){
            Str += resTextLibrary.getRtliCol1Imp() + ",";
        }
        if(null != resTextLibrary.getRtliCol2Imp() && resTextLibrary.getRtliCol2Imp().length() > 0){
            Str += resTextLibrary.getRtliCol2Imp() + ",";
        }
        if(null != resTextLibrary.getRtliCol3Imp() && resTextLibrary.getRtliCol3Imp().length() > 0){
            Str += resTextLibrary.getRtliCol3Imp() + ",";
        }
        if(null != resTextLibrary.getRtliCol4Imp() && resTextLibrary.getRtliCol4Imp().length() > 0){
            Str += resTextLibrary.getRtliCol4Imp() + ",";
        }
        if(null != resTextLibrary.getRtliCol5Imp() && resTextLibrary.getRtliCol5Imp().length() > 0){
            Str += resTextLibrary.getRtliCol5Imp() + ",";
        }
        if(null != resTextLibrary.getRtliCol6Imp() && resTextLibrary.getRtliCol6Imp().length() > 0){
            Str += resTextLibrary.getRtliCol6Imp() + ",";
        }
        if(null != resTextLibrary.getRtliCol7Imp() && resTextLibrary.getRtliCol7Imp().length() > 0){
            Str += resTextLibrary.getRtliCol7Imp() + ",";
        }
        if(null != resTextLibrary.getRtliCol8Imp() && resTextLibrary.getRtliCol8Imp().length() > 0){
            Str += resTextLibrary.getRtliCol8Imp() + ",";
        }
        if(null != resTextLibrary.getRtliCol9Imp() && resTextLibrary.getRtliCol9Imp().length() > 0){
            Str += resTextLibrary.getRtliCol9Imp() + ",";
        }
        if(null != resTextLibrary.getRtliCol10Imp() && resTextLibrary.getRtliCol10Imp().length() > 0){
            Str += resTextLibrary.getRtliCol10Imp() + ",";
        }
        if(null != resTextLibrary.getRtliCol11Imp() && resTextLibrary.getRtliCol11Imp().length() > 0){
            Str += resTextLibrary.getRtliCol11Imp() + ",";
        }
        if(null != resTextLibrary.getRtliCol12Imp() && resTextLibrary.getRtliCol12Imp().length() > 0){
            Str += resTextLibrary.getRtliCol12Imp() + ",";
        }
        
        String[] temp = null;
        if(Str.length() > 0){
            Str = Str.substring(0, Str.length()-1);
            temp = Str.split(",");
        }

        //检查
        boolean fine = true;
        if(null != temp && temp.length > 1){
            for (int i = 0; i < temp.length; i++) {
                for (int j = 0; j < temp.length; j++) {
                    if(i != j && temp[i].equals(temp[j])){
                        fine = false;
                        break;
                    }
                }
            }
        }
        
        return fine;
    }
    
    /**
     * 删除资源库
     * @throws Exception 
     */
    public ModelAndView deleteLibrary(HttpServletRequest request, HttpServletResponse response, ResTextLibrary resTextLibrary) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        //资源库ID
        Long rtliId = ServletRequestUtils.getLongParameter(request, "rtliId", 0L);
        ResTextLibrary lib = (ResTextLibrary) this.textLibraryService.get(ResTextLibrary.class, rtliId);

        try {
            this.textLibraryService.delete(lib);
            this.textLibraryService.updateCache();
            request.setAttribute("result", "<script>Msg.success('删除成功！');</script>");
        } catch (Exception e) {
            request.setAttribute("result", "<script>Msg.success('该资源库存在资源或分类,删除失败！');</script>");
        }
        return handleNoSuchRequestHandlingMethod(null, request, response);
    }
}
