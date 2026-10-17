package cn.com.adtech.riss2.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.ILanguageTypeService;
import cn.com.adtech.riss2.bso.resource.IMissionInfoManagementService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.resource.ResDynamicType;
import cn.com.adtech.riss2.domain.text.ResLibraryClassify;

public class ExtdataUtil extends MultiActionController {
	
	private ILanguageTypeService lanService = null;
	private IMissionInfoManagementService missionService = null;
	

	public void setMissionService(IMissionInfoManagementService missionService) {
		this.missionService = missionService;
	}

	public void setLanService(ILanguageTypeService lanService) {
		this.lanService = lanService;
	}

	/**
	 * 获取学科的Ext下拉选项json数据
	 */
	public ModelAndView xueke(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String jsondata = "";
		jsondata = "[[1,'计算机学'],[2,'文学']]";
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		return null;
	}

	/**
	 * 获取元数据模板的下拉选项json数据
	 */
	public ModelAndView templet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String jsondata = "";
		jsondata = "[[1,'中国图书模板'],[2,'中国文学类模板']]";
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		return null;
	}

	/**
	 * 获取语种的下拉选项json数据
	 */
	public ModelAndView language(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String jsondata = "";
		jsondata = lanService.getLanguageSelectOptionByExt();
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		return null;
	}

	/**
	 * 获取自定义选项json数据
	 */
	public ModelAndView privatetype(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String jsondata = "";
		jsondata = "[[1,'aaaaa'],[2,'bbbbb']]";
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		return null;
	}

	/**
	 * 获取特色库的下拉选项json数据
	 */
	public ModelAndView store_select(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		int purview_type = MyRequestUtil.getIntParameter(request, "ptype");
		SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
		if(sysManager == null) { // 如果用户为空
			response.sendRedirect(request.getContextPath() + "/console/index.htm");
			return null;
		}
		String sql = "select b.rmst_id,b.rmst_name from sys_manager a, res_meta_store b, RES_META_PERM t where t.rmst_id=b.rmst_id and t.rmpe_adminid=a.sman_id and b.RMST_STATUS = 1 ";
		if(sysManager.getSmanId()> 0) {
			sql += " and a.sman_id = " + sysManager.getSmanId();
		}
		if(purview_type > 0) {
			sql += " and t.rmpe_type = " + purview_type;
		}
		String jsondata = lanService.getJsonByOption(sql);
		String hasNull = request.getParameter("hasNull");
		if("hasNull".equals(hasNull))
			jsondata = CommonUtil.createStoreSelectHasNull(jsondata, "0", "不限");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		return null;
	}
	
	public ModelAndView all_store_select(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		int purview_type = MyRequestUtil.getIntParameter(request, "ptype");
		SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
		long userId = 0;
		if(sysManager != null) { // 如果用户为空
			userId = sysManager.getSmanId();
		}
		String sql = "select b.rmst_id,b.rmst_name from sys_manager a, res_meta_store b, RES_META_PERM t where t.rmst_id=b.rmst_id and t.rmpe_adminid=a.sman_id and b.RMST_STATUS = 1 ";
		if(userId> 0) {
			sql += " and a.sman_id = " + userId;
		}
		if(purview_type > 0) {
			sql += " and t.rmpe_type = " + purview_type;
		}
		String jsondata = lanService.getJsonByOption(sql);
		
		String hasNull = request.getParameter("hasNull");
		if("hasNull".equals(hasNull))
			jsondata = CommonUtil.createStoreSelectHasNull(jsondata, "0", "不限");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		return null;
	}
	
	public ModelAndView all_store(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		String sql = "select b.rmst_id,b.rmst_name from res_meta_store b where  b.RMST_STATUS = 1 ";
		String jsondata = lanService.getJsonByOption(sql);
		String hasNull = request.getParameter("hasNull");
		if("hasNull".equals(hasNull))
			jsondata = CommonUtil.createStoreSelectHasNull(jsondata, "0", "不限");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		return null;
	}
	
	public ModelAndView AllStoreJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		
		String sql = "select RMST_ID, RMST_NAME FROM RES_META_STORE where rmst_status=1";
		
		String jsondata = lanService.getJsonByOption(sql);
		String hasNull = request.getParameter("hasNull");
		if("hasNull".equals(hasNull))
			jsondata = CommonUtil.createStoreSelectHasNull(jsondata, "0", "不限");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		return null;
	}


	/**
	 * 资源类型的下拉选项json数据
	 */
	public ModelAndView resource_type_select(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String jsondata = "";
		jsondata = "[[1,'图书'],[2,'期刊']]";
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		return null;
	}

	/**
	 * 获取其它语录入的作业列表的json数据
	 */
	public ModelAndView interpretMissionList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String jsondata = "";
		long store = MyRequestUtil.getLongParameter(request, "store");
		long sourceType = MyRequestUtil.getLongParameter(request, "sourceType");
		int statu = MyRequestUtil.getIntParameter(request, "statu");
		int begin = MyRequestUtil.getIntParameter(request, "start");
		int pageNum = MyRequestUtil.getIntParameter(request, "limit");
		SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
		if(sysManager == null) {
			return null;
		}
		long userid = sysManager.getSmanId();
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "RMIN_ENDDATE");
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		
		jsondata = missionService.getInterpretMissionListByJson(store, sourceType, statu, userid, begin, pageNum, sort, dir);
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		return null;
	}

	public ModelAndView AllLibJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html; charset=UTF-8");
        
        String sql = "select rtli.RTLI_ID,rtli.RTLI_NAME from RES_TEXT_LIBRARY rtli order by rtli.RTLI_INDEX ASC";
        
        String jsondata = lanService.getJsonByOption(sql);
        String hasNull = request.getParameter("hasNull");
        if("hasNull".equals(hasNull))
            jsondata = CommonUtil.createStoreSelectHasNull(jsondata, "0", "不限");
        PrintWriter out = response.getWriter();
        out.print(jsondata);
        return null;
    }
}
