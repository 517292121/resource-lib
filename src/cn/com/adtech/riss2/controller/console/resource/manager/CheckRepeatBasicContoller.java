package cn.com.adtech.riss2.controller.console.resource.manager;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bean.ResourceRepeatBean;
import cn.com.adtech.riss2.core.ServiceUtil;

public class CheckRepeatBasicContoller extends MultiActionController{

	protected String parseColumn(int code){
		if(code == 1){
			return "rsdaTitle";
		}
		if(code == 2){
			return "rsdaPublisher";
		}
		if(code == 3){
			return "rsdaFileSize";
		}
		if(code == 4){
			return "rsdaFileType";
		}
		
		if(code == 5){
			return "rsdaSubject";
		}
		if(code == 6){
			return "rsdaDescription";
		}
		if(code == 7){
			return "rsdaSource";
		}
		if(code == 8){
			return "rsdaLanguage";
		}
		if(code == 9){
			return "rsdaRelation";
		}
		if(code == 10){
			return "rsdaCoverage";
		}
		if(code == 11){
			return "rsdaCreator";
		}
		if(code == 12){
			return "rsdaContributor";
		}
		if(code == 13){
			return "rsdaRights";
		}
		if(code == 14){
			return "rsdaDate";
		}
		if(code == 15){
			return "rsdaType";
		}
		if(code == 16){
			return "rsdaFormat";
		}
		if(code == 17){
			return "rsdaIdentifier";
		}
		return "";
	}
	
	
	/**
	 * 封面查重条件和查重范围
	 * @param request
	 * @param repeatBean
	 * @return
	 */
	protected ResourceRepeatBean parseRepeatBean(HttpServletRequest request, ResourceRepeatBean repeatBean){
		//查重条件(1 题名 、 2 出版者、 3 资源大小、 4 资源类型) 若为多个 用','隔开
		String cctj = ServletRequestUtils.getStringParameter(request, "cctj","");
		Long[] tjArr = parseRsdaIdToArrary(cctj);
		Long tj;
		for(int i=0; i<tjArr.length; i++){
			tj = tjArr[i];
			if(tj == null || tj < 1){
				continue;
			}
			if(tj == 1){
				repeatBean.setCheckTitle(true);
				continue;
			}
			if(tj == 2){
				repeatBean.setCheckPublisher(true);
				continue;
			}
			if(tj == 3){
				repeatBean.setCheckFilesize(true);
				continue;
			}
			if(tj == 4){
				repeatBean.setCheckFiletype(true);
				continue;
			}
			if(tj == 5){
				repeatBean.setCheckSubject(true);
				continue;
			}
			
			if(tj == 5){
				repeatBean.setCheckSubject(true);
				continue;
			}
			if(tj == 6){
				repeatBean.setCheckDescription(true);
				continue;
			}
			if(tj == 7){
				repeatBean.setCheckSource(true);
				continue;
			}
			if(tj == 8){
				repeatBean.setCheckLanguage(true);
				continue;
			}
			if(tj == 9){
				repeatBean.setCheckRelation(true);
				continue;
			}
			if(tj == 10){
				repeatBean.setCheckCoverage(true);
				continue;
			}
			if(tj == 11){
				repeatBean.setCheckCreator(true);
				continue;
			}
			if(tj == 12){
				repeatBean.setCheckContributor(true);
				continue;
			}
			if(tj == 13){
				repeatBean.setCheckRights(true);
				continue;
			}
			if(tj == 14){
				repeatBean.setCheckDate(true);
				continue;
			}
			if(tj == 15){
				repeatBean.setCheckType(true);
				continue;
			}
			if(tj == 16){
				repeatBean.setCheckFormat(true);
				continue;
			}
			if(tj == 17){
				repeatBean.setCheckIdentifier(true);
				continue;
			}
		}
		
		//查重范围，即资源状态。默认为0(所有资源)
//		Long rsdaStatus = ServletRequestUtils.getLongParameter(request, "rsdaStatus",0L);
//		repeatBean.setRsdaStatus(rsdaStatus);
//		request.setAttribute("rsdaStatus", rsdaStatus);
		boolean showall = ServletRequestUtils.getBooleanParameter(request, "showall", false);
		repeatBean.setRepeatAll(showall);
		request.setAttribute("showall", showall);
		return repeatBean;
	}
	
	/**
	 * 将查重的ID字段串解析为ID数组
	 * @param ids
	 * @return
	 */
	protected Long[] parseRsdaIdToArrary(String ids){
		if(ServiceUtil.isNotNull(ids) == false){
			return null;
		}
		String[] idStrArr = ids.split(",");
		Long[] idArr = new Long[idStrArr.length];
		for(int i=0; i<idStrArr.length; i++){
			try {
				idArr[i] = Long.parseLong(idStrArr[i].trim());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		idStrArr = null;		
		return idArr;
	}
}
