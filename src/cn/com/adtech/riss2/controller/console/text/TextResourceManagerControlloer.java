package cn.com.adtech.riss2.controller.console.text;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import cn.com.adtech.riss2.bso.text.IResourceInputService;
import cn.com.adtech.riss2.bso.text.ITextLibClassfiyService;
import cn.com.adtech.riss2.bso.text.ITextResourceService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResDynamicType;
import cn.com.adtech.riss2.domain.text.ResLibraryClassify;
import cn.com.adtech.riss2.domain.text.ResTextLibrary;

import com.jbetter.framework.session.Pagination;

/**
 * 文档资源维护
 *   
 * TextResourceManagerControlloer  
 *   
 * 2017-10-17 下午01:03:48  
 *   
 * @athor lichangkun
 * @version 1.0.0  
 *
 */
public class TextResourceManagerControlloer extends MultiActionController {
    private ITextResourceService textResourceService;
    public void setTextResourceService(ITextResourceService textResourceService) {
        this.textResourceService = textResourceService;
    }
    
    /**
     * 进入资源维护列表
     */
    @Override
    protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        return new ModelAndView("text/textResManager");
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
        
        Long rtliId = ServletRequestUtils.getLongParameter(request, "rtliId", 0L);
        Long rlclId = ServletRequestUtils.getLongParameter(request, "rlclId", 0L);
        
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
        //获取列表内容
        List<Object[]> resourceList = this.textResourceService.getResourceList(pagination, rtliId, rlclId, "", key, val, sort, dir);
        
        //添加参数
        String[] keys = {"rtreId","rtreTitle","rtreRtliId","rtliName","rlclName","rtreUsed","rtreRecommend","rtreDate"};
        
        //获取json
        String jsondata = CommonUtil.createJsonTable(resourceList, keys, pagination.getTotalResults() + "");
        
        response.setContentType("txt/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsondata);
        out.flush();
    }
    
    /**
     * 设置资源的推荐状态
     *
     * @author MaChao
     * @date Nov 25, 2015
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void changRcmdStatu(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sourceIdlist = ServletRequestUtils.getStringParameter(request, "idlist", "");
        boolean b = this.textResourceService.updateRcmdStatus(sourceIdlist);
        String jsondata = "";
        if(b) {
            jsondata = "1";
        }else {
            jsondata = "0";
        }
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsondata);
        out.flush();
    }
    
    /**
     * 设置资源使用状态
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    public void changUseStatu(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sourceIdlist = ServletRequestUtils.getStringParameter(request, "idlist", "");
        boolean b = this.textResourceService.updateUseStatus(sourceIdlist);
        String jsondata = "";
        if(b) {
            jsondata = "1";
        }else {
            jsondata = "0";
        }
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsondata);
        out.flush();
    }
    
    public ModelAndView AllType(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html; charset=UTF-8");
        Long store = ServletRequestUtils.getLongParameter(request, "store", 0L);
        
        String jsondata = " ";
        if(store > 0L){
            jsondata = textResourceService.getJsonByType(store);
        }
        String hasNull = request.getParameter("hasNull");
        if("hasNull".equals(hasNull)){
            String result = "[['不限','0']";
            result += ",";
            result += jsondata.substring(0,jsondata.length()-1)+"]";
            jsondata = result;
        }
        PrintWriter out = response.getWriter();
        out.print(jsondata);
        return null;
    }
    
}
