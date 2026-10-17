package cn.com.adtech.riss2.controller.platform.wap;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.adtech.riss2.bso.platform.ITextResourceService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.text.ResTextLibrary;
import cn.com.adtech.riss2.domain.text.ResTextResource;

/**
 * 
 * wap版文本资源库处理
 *
 * @author MaChao
 *
 * @date：Apr 3, 2018
 */
@Controller
public class WapTextLibraryController {
	
	//资源收藏服务对象
	@Resource(name="textResService")
	private ITextResourceService textResourceService;

	//按特色库进入资源列表
	@RequestMapping(value="/wap/text/{rtreId}")
	public String resDetail(@PathVariable String rtreId, Model model, HttpServletRequest request) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		Long numId = null;
		try {
			numId = Long.parseLong(rtreId);
		} catch (Exception e) {
			numId = CommonUtil.strToId(rtreId);
		}
		//获取资源库核心对象
		ResTextResource resource = (ResTextResource) this.textResourceService.get(ResTextResource.class, numId);
		
		if(resource.getRtreUsed() == 0L){
			map.put("errormsg", "对不起，该资源已禁用！");
			model.addAllAttributes(map);
			return "platform/pc/error";
		}
		
		if(null != resource.getRtreRtliId() && resource.getRtreRtliId() > 0L){
			ResTextLibrary textLib = (ResTextLibrary)this.textResourceService.get(ResTextLibrary.class, resource.getRtreRtliId());
            map.put("textLib", textLib);
        }
		
		//更新点击量
		long viewNum = resource.getRtreAccess() == null ? 1 : resource.getRtreAccess() + 1;
		String hql = "update ResTextResource r set r.rtreAccess="+viewNum+" where r.rtreId=" + numId;
		this.textResourceService.getServiceHibernateTemplate().bulkUpdate(hql);
		
		//以下字段为textarea框输入，因此展示前需要转换一下格式
		resource.setRtreCol11(CommonUtil.hangleTextArea(resource.getRtreCol11()));
		resource.setRtreCol12(CommonUtil.hangleTextArea(resource.getRtreCol12()));
		
		map.put("title", resource.getRtreTitle());
		map.put("resource", resource);
		model.addAllAttributes(map);
		return "platform/wap/textResDetail";
	}
}
