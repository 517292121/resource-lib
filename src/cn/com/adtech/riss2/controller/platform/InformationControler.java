package cn.com.adtech.riss2.controller.platform;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.adtech.riss2.bso.ids.IIdsService;
import cn.com.adtech.riss2.cache.PlatformCacheFactory;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.domain.IdsInformation;
import cn.com.adtech.riss2.domain.IdsInformationType;

import com.jbetter.framework.session.Pagination;

/**
 * 
 * 前端信息资讯控制器
 *
 * @author MaChao
 *
 * @date：Jan 2, 2018
 */
@Controller
public class InformationControler {
	
	@Resource(name="idsServices")
	private IIdsService infoService;

	
	//进入信息列表页面
	@RequestMapping(value="/info/list")
	public String infoList(Model model, HttpServletRequest request) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		String cid = ServletRequestUtils.getStringParameter(request, "cid", "");
		Long classId = null;
		try {
			classId = Long.parseLong(cid);
		} catch (Exception e) {
			classId = CommonUtil.strToId(cid);
		}
		classId = classId == null ? 0L : classId;
		List<Object[]> classList = PlatformCacheFactory.instance().getInfoClassList();
		
		//分页设置
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
		Pagination pagination = new Pagination();
		int size = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		pagination.setPageResults(size);
		pagination.setCurrentPage(currentPage);
		
		try {
			List<Object[]> dataList = this.infoService.getInfoList(pagination, classId);
			
			
			map.put("classId", cid);
			if(classId != null && classId > 0L){
				IdsInformationType infoType = (IdsInformationType) this.infoService.get(IdsInformationType.class, classId);
				map.put("title", infoType.getIdstName());
			}
			
			map.put("classList", classList);
			map.put("dataList", dataList);
			map.put("pagination", pagination);
			map.put("pageUrl", "/info/list?cid="+cid);
			model.addAllAttributes(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if(classList == null || classList.size() < 8){
			//当信息分类个数较少时，采用分类在上面，信息拉通展示方式
			return "platform/pc/infoListA";
		}else{
			//当信息分类较多时，采用传统左右结构方式展示
			return "platform/pc/infoListB";
		}
	}
	
	//进入信息详细页面
	@RequestMapping(value="/info/detail/{infoId}")
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
		return "platform/pc/infoDetail";
	}
}
