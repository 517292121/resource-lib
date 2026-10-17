package cn.com.adtech.riss2.controller.platform.wap;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.adtech.riss2.bso.ids.IIdsService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.IdsInformation;

/**
 * 
 * wap信息咨询管理
 *
 * @author MaChao
 *
 * @date：Apr 2, 2018
 */
@Controller
public class WapInformationController {
	
	@Resource(name="idsServices")
	private IIdsService infoService;

	//进入信息详细页面
	@RequestMapping(value="/wap/info/detail/{infoId}")
	public String infoDetail(@PathVariable String infoId, Model model, HttpServletRequest request) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Long numId = null;
		try {
			numId = Long.parseLong(infoId);
		} catch (Exception e) {
			numId = CommonUtil.strToId(infoId);
		}
		try {
			IdsInformation info = this.infoService.getIdsInfomarion(numId);
			
			//更新点击量
			long viewNum = info.getIdsiHitCount() == null ? 1 : info.getIdsiHitCount() + 1;
			String hql = "update IdsInformation info set info.idsiHitCount="+viewNum+" where info.idsiId=" + numId;
			this.infoService.getServiceHibernateTemplate().bulkUpdate(hql);
			
			Object[] infoPre = this.infoService.getInfoPre(numId);
			Object[] infoNext = this.infoService.getInfoNext(numId);
			
			map.put("title", info.getIdsiTitle());
			map.put("info", info);
			map.put("infoPre", infoPre);
			map.put("infoNext", infoNext);
			model.addAllAttributes(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "platform/wap/infoDetail";
	}
}
