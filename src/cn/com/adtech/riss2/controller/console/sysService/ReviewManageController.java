package cn.com.adtech.riss2.controller.console.sysService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.ISysServiceService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.domain.SysSensitiveWord;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.resource.ResSourceComt;

/**
 * 资源评论管理
 * 
 * @author MaChao
 * @date 2010-10-30
 */
public class ReviewManageController extends MultiActionController {
	/**
	 * 初始化页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView initView(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("sysService/reviewManage");
	}

	/**
	 * 获得资源评论列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getResSourceComtList(HttpServletRequest request, HttpServletResponse response, PageExtNative page) {
		response.setContentType("text/html; charset=UTF-8");
		String name = ServletRequestUtils.getStringParameter(request, "condition", "");
		String checkState = ServletRequestUtils.getStringParameter(request, "checkState", "2");
		//String beginDate = ServletRequestUtils.getStringParameter(request, "beginDate", "");
		//String endDate = ServletRequestUtils.getStringParameter(request, "endDate", "");
		try {
			Wrapper wrapper = null;
			if(checkState.equals("2")){
				wrapper = sysService.getResSourceComtList(name, null, null, null, page);
			}else{
				wrapper = sysService.getResSourceComtList(name, null, null, checkState, page);
			}
			List list = (List)wrapper.getResult();  
			
			/*Add by YangJunping 处理换行符  begin */
			List newList = new ArrayList();
			for(int i = 0; i < list.size(); i++) {
				Object[] comt = (Object[])list.get(i);
				String content = comt[1].toString();
				if(CommonUtil.isNotNull(content)){
					content = content.replaceAll("<br/>", " ");
				}
				
				Object[] newObj = new Object[comt.length+1];
				System.arraycopy(comt, 0, newObj, 0, comt.length);
				if(CommonUtil.isNotNull(content) && content.trim().length() > 60){
					newObj[comt.length] = content.substring(0, 60);
				}else{
					newObj[comt.length] = content;
				}
				newList.add(newObj);
			}
			/*Add by YangJunping 处理换行符  end */
			
			String result = CommonUtil.createJsonTable(newList, new String[] {"rscoId", "rscoContent", "rscoDate", "suinLoginName",
					"suinName", "rscoStatus","tempContent"}, wrapper.getMessage());
			//String sensitiveWord = ConstantsObj.getSysProperty(MmSettingConstant.getsensitiveWords());
			List sswList = sysService.getSSWList();
			if(sswList!=null && sswList.size() > 0) {
				for(int i=0;i<sswList.size();i++) {
					result = result.replaceAll(((SysSensitiveWord)sswList.get(i)).getSswName(), "<font style=color:red>" + ((SysSensitiveWord)sswList.get(i)).getSswName() + "</font>");
				}
			}
			response.getWriter().write(result);
			response.getWriter().flush();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 审核资源评论
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView checkResSourceComt(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		response.setContentType("text/html; charset=UTF-8");
		Long rscoId = ServletRequestUtils.getLongParameter(request, "rscoId", 0l);
		String rscoStatus = ServletRequestUtils.getStringParameter(request, "rscoStatus", ResSourceComt.STATUS_PAST + "");
		String result = "{success:false,msg:'审核失败！'}";
		try {
			out = response.getWriter();
			if(rscoId > 0) {
				sysService.updateResSourceComt(rscoId, new Short(rscoStatus));
				if(new Short(rscoStatus)==ResSourceComt.STATUS_PAST){
					ResSourceComt resSourceComt=(ResSourceComt)sysService.get(ResSourceComt.class, rscoId);
					if(resSourceComt!=null&&resSourceComt.getRscoSuinId()!=null){
						sysService.updateUsersPoint(resSourceComt.getRscoSuinId(),MmSettingConstant.getAsspoint_machining_message());
					}
				}
				result = "{success:true,msg:'审核成功！'}";
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		out.write(result);
		out.flush();
		return null;
	}

	/**
	 * 删除资源评论
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView deleteResSourceComt(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		response.setContentType("text/html; charset=UTF-8");
		String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
		String result = "{success:false,msg:'删除失败！'}";
		try {
			out = response.getWriter();
			if(ServiceUtil.isNotNull(ids)) {
				sysService.deleteResSourceComt(ids);
				result = "{success:true,msg:'删除成功！'}";
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		out.write(result);
		out.flush();
		return null;
	}
	
	/**
	 * 批量审核
	 * @author MaChao
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView checkReviewBatch(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		response.setContentType("text/html; charset=UTF-8");
		String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
		String check = ServletRequestUtils.getStringParameter(request, "check", ResSourceComt.STATUS_PAST + "");
		String result = "{success:false,msg:'审核失败！'}";
		try {
			out = response.getWriter();
			String[] id = ids.split(",");
			for(int i=0;i<id.length;i++){
				if(id[i].length() > 0) {
					sysService.updateResSourceComt(Long.parseLong(id[i]), new Short(check));
					if(new Short(check).equals(ResSourceComt.STATUS_PAST)){
						ResSourceComt resSourceComt=(ResSourceComt)sysService.get(ResSourceComt.class, Long.parseLong(id[i]));
						if(resSourceComt!=null&&resSourceComt.getRscoSuinId()!=null){
							sysService.updateUsersPoint(resSourceComt.getRscoSuinId(),MmSettingConstant.getAsspoint_machining_message());
						}
					}
				}
			}
			result = "{success:true,msg:'审核成功！'}";
		}catch(Exception e) {
			e.printStackTrace();
		}
		out.write(result);
		out.flush();
		return null;
	}

	private ISysServiceService sysService;

	public ISysServiceService getSysService() {
		return sysService;
	}

	public void setSysService(ISysServiceService sysService) {
		this.sysService = sysService;
	}
}
