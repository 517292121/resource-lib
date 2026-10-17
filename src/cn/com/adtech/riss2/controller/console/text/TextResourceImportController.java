package cn.com.adtech.riss2.controller.console.text;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import cn.com.adtech.riss2.bso.text.IResourceInputService;
import cn.com.adtech.riss2.bso.text.ITextLibClassfiyService;
import cn.com.adtech.riss2.controller.FileUploadController;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.domain.text.ResTextLibrary;

public class TextResourceImportController extends FileUploadController{
    private IResourceInputService resourceInputService;
    private ITextLibClassfiyService textLibClassfiyService;
    public void setTextLibClassfiyService(ITextLibClassfiyService textLibClassfiyService) {
        this.textLibClassfiyService = textLibClassfiyService;
    }
    public void setResourceInputService(IResourceInputService resourceInputService) {
        this.resourceInputService = resourceInputService;
    }
    
    private ResourceFileUploadUtil upload = new ResourceFileUploadUtil();
    
    /**文件导入信息****/
    private String impMessage = "";
    
    /**
     * 资源导入默认页面
     */
    @Override
    protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        
        Long rtliId = ServletRequestUtils.getLongParameter(request, "rtliId", 0L);
        ResTextLibrary rtli = (ResTextLibrary) this.resourceInputService.get(ResTextLibrary.class, rtliId); 
        
        //录入页面请求来源
        String source = ServletRequestUtils.getStringParameter(request, "source", "");
        
        map.put("rtli", rtli);
        map.put("rtliId", rtliId);
        map.put("source", source);
        return new ModelAndView("/text/importResource", map);
    }
    
    /**
     * 导入入口
     * 
     * @athor lichangkun
     * 2017-10-19
     */
    public ModelAndView importResourceData(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //导入信息
        impMessage = "";
        boolean b = checkform(request);
        if(b == false){ //验证表单基本信息
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(getErrorMessage(null));
            out.flush();
            return null;
        }
        //导入其始行
        long beginTime = System.currentTimeMillis(); //开始执行时间
        
        //上传excel文件
        
        File file = uploadImportExcel(request);
        
        //验证表格
        String msg = this.resourceInputService.checkImportData(request, file);
        if(CommonUtil.isNotNull(msg)){
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(getErrorMessage(msg));
            out.flush();
            return null;
        }
        
        String impMsg = this.resourceInputService.saveImportData(request, file);
        long endTime = System.currentTimeMillis();
        
        impMsg = "<p>此次操作用时：<span style='color:red'>" + ((endTime - beginTime) / 1000.000) + "</span> 秒！" + impMsg;
        
        request.setAttribute("impMsg", impMsg);
        request.setAttribute("beginLine", ServletRequestUtils.getStringParameter(request, "beginLine", ""));
        request.setAttribute("fileFolder", ServletRequestUtils.getStringParameter(request, "fileFolder", ""));
        return handleNoSuchRequestHandlingMethod(null, request, response);
    }
    
    /**验证表单基本信息**/
    public boolean checkform(HttpServletRequest request){
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
        MultipartFile file = multiRequest.getFile("importExcel");
        boolean b = true;

        if(file == null || file.getSize() <= 0 || !CommonUtil.isNotNull(file.getOriginalFilename())) {
            impMessage += "<li>excel文件不能为空！</li>";
            b = false;
        }
        
        return b;       
    }
    
    /**上传EXCEL数据文件**/
    public File uploadImportExcel(HttpServletRequest request){
        try {
            //String fileFolder = ToolUnit.getProperties("downloadPath", ConstantsObj.CONFIG_PATH);
            String[] fileInfo = upload.uploadfile(request, "", "importExcel");
            
            File localfile = new File(fileInfo[0]);
            return localfile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
