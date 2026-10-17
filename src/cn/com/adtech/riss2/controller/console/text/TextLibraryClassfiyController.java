package cn.com.adtech.riss2.controller.console.text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import cn.com.adtech.riss2.bso.text.ITextLibClassfiyService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.text.ResLibraryClassify;
import cn.com.adtech.riss2.domain.text.ResTextLibrary;

/**
 * 资源库分类设置控制器
 *   
 * TextLibraryClassfiyController  
 *   
 * 2017-10-17 下午05:17:37  
 *   
 * @athor lichangkun
 * @version 1.0.0  
 *
 */
public class TextLibraryClassfiyController extends MultiActionController {
    private ITextLibClassfiyService textLibClassfiyService;
    public void setTextLibClassfiyService(ITextLibClassfiyService textLibClassfiyService) {
        this.textLibClassfiyService = textLibClassfiyService;
    }

    @Override
    protected ModelAndView handleNoSuchRequestHandlingMethod(
            NoSuchRequestHandlingMethodException ex,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        Long rtliId =ServletRequestUtils.getLongParameter(request, "rtliId",0L);
        map.put("rtliId", rtliId);
        return new ModelAndView("/text/resLibClassfiyFrame",map);
    }
    
    /**
     * 资源库类型分类树
     */
    public ModelAndView initGroupTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long rtliId =ServletRequestUtils.getLongParameter(request, "rtliId",0L);
        List<ResLibraryClassify> list = this.textLibClassfiyService.getLibClassfiyList(rtliId);
        request.setAttribute("resLibClassfiyList", list);
        request.setAttribute("rtliId", rtliId);
        if(rtliId > 0){
            ResTextLibrary rtli = (ResTextLibrary)this.textLibClassfiyService.get(ResTextLibrary.class, rtliId);
            request.setAttribute("rtliName", rtli.getRtliName());
        }
        return new ModelAndView("/text/resLibClassfiyTree");
    }
    
    /**
     * 资源库类型详细信息
     */
    public ModelAndView groupInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long rtliId =ServletRequestUtils.getLongParameter(request, "rtliId",0L);//资源类型Id
        Long rlclId = ServletRequestUtils.getLongParameter(request, "rlclId",0L);
        String options = "";
        ResLibraryClassify subject = null;
        if(rlclId == null || rlclId < 1){
            options = this.textLibClassfiyService.getLibClassfiyOptions(rtliId, 0L);
        }else{
            subject = (ResLibraryClassify)this.textLibClassfiyService.get(ResLibraryClassify.class, rlclId);
            options =  this.textLibClassfiyService.getLibClassfiyOptions(rtliId, subject.getRlclParentId() == null ? 0 : subject.getRlclParentId());
        }
        
        if(CommonUtil.isNotNull(options)){
            options = options.replace("请选择...", "无");
        }else{
            options = "<option value='0'>无</option>";
        }
        
        request.setAttribute("options", options);
        request.setAttribute("subject", subject);
        request.setAttribute("rtliId", rtliId);
        request.setAttribute("rlclId", rlclId);
        return new ModelAndView("/text/resLibClassfiyInfo");
    }
}
