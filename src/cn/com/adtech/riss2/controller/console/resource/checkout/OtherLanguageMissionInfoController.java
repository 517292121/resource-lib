package cn.com.adtech.riss2.controller.console.resource.checkout;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.IMissionInfoManagementService;
import cn.com.adtech.riss2.bso.resource.IResMissionInfoCheckService;
import cn.com.adtech.riss2.bso.resource.ISourceLanguageService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.resource.ResLanguageType;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.domain.resource.ResMissionLanguage;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceLanguage;
import cn.com.adtech.riss2.exception.ErrorMessageException;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class OtherLanguageMissionInfoController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(OtherLanguageMissionInfoController.class);

	private IResMissionInfoCheckService resMissionInfoCheckService;

	private IMissionInfoManagementService missionInfoManagementService;

	private ISourceLanguageService sldao = null;

	public ISourceLanguageService getSldao() {
		return sldao;
	}

	public void setSldao(ISourceLanguageService sldao) {
		this.sldao = sldao;
	}

	public IMissionInfoManagementService getMissionInfoManagementService() {
		return missionInfoManagementService;
	}

	public void setMissionInfoManagementService(IMissionInfoManagementService missionInfoManagementService) {
		this.missionInfoManagementService = missionInfoManagementService;
	}

	public IResMissionInfoCheckService getResMissionInfoCheckService() {
		return resMissionInfoCheckService;
	}

	public void setResMissionInfoCheckService(IResMissionInfoCheckService resMissionInfoCheckService) {
		this.resMissionInfoCheckService = resMissionInfoCheckService;
	}

	/**
	 * 初始化作业页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-13
	 */
	public ModelAndView initOtherLanguageMissionInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/checkout/otherLanguageMissionInfo");
	}

	/**
	 * 初始化其它语审核资源列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-14
	 */
	public ModelAndView initOtherLanguageResSourceData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0);
		Long userId = ServletRequestUtils.getLongParameter(request, "userid", 0);
		if(userId < 1) {
			userId = resMissionInfoCheckService.getUserId(rminId, "rminOthercheck");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rminId", rminId);
		map.put("userId", userId);
		return new ModelAndView("resource/checkout/otherLanguageResSourceData", map);
	}

	/**
	 * 初始化其它语审核资源列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-28
	 */
	public ModelAndView initOtherLanguageRmsMissionInfoDataCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Long rmlaId = ServletRequestUtils.getLongParameter(request, "rmlaId", 0);
		map.put("rmlaId", rmlaId);
		ResMissionLanguage rmla = (ResMissionLanguage)this.missionInfoManagementService.get(ResMissionLanguage.class, rmlaId);
		if(rmla != null)
			map.put("rmla", rmla);
		return new ModelAndView("resource/checkout/otherLanguageRmsMissionInfoDataCheck", map);
	}

	/**
	 * 进入资源语种详情页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-14
	 */
	public ModelAndView initResSourceDataOtherLanguageDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		Long rsdaId = ServletRequestUtils.getLongParameter(request, "rsdaId", 0);
		Long rltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0);
		Long rmlaId = ServletRequestUtils.getLongParameter(request, "rmlaId", 0);
		ResSourceData rsd = (ResSourceData)this.resMissionInfoCheckService.get(ResSourceData.class, rsdaId);
		if(rsd != null) {
			List list = this.resMissionInfoCheckService.getRSDFirstLanguage(rsdaId, true, "1", null);
			if(list.size() > 0) {
				Long languageId = (Long)list.get(0);
				List rsdList = this.resMissionInfoCheckService.getResSourceDataDetailInfo(rsdaId, null, languageId, "1", rltyId);
				result.put("rsdList", rsdList);
				result.put("rsd", rsd);
				result.put("rmlaId", rmlaId);
				result.put("rltyId", rltyId);
				List tempList = this.resMissionInfoCheckService.getRSDFirstLanguage(rsdaId, null, null, rltyId);
				if(tempList.size() > 0) {
					ResSourceLanguage rsl = (ResSourceLanguage)tempList.get(0);
					result.put("rsl", rsl);
				}
				// 初始化语种信息
				ResMissionLanguage rml = (ResMissionLanguage)this.missionInfoManagementService.get(ResMissionLanguage.class, rmlaId);
				ResLanguageType language = rml.getResLanguageType();
				String passLanguageOption = sldao.getSourceLanguageOption(rsdaId, 4, languageId); // 已审核通过的语种
				result.put("lname", passLanguageOption);
				result.put("language", language);
			}
		}
		return new ModelAndView("resource/checkout/resSourceDataOtherLanguage", result);
	}

	/**
	 * 初始化资源语种页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-14
	 */
	public ModelAndView initRsdOtherLanguage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long rsdaId = ServletRequestUtils.getLongParameter(request, "rsdaId", 0);
		Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rsdaId", rsdaId);
		map.put("rminId", rminId);
		return new ModelAndView("resource/checkout/rsdOtherLanguage", map);
	}

	/**
	 * 获取作业列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-13
	 */
	public void getResMissionInfoList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
		if(sysManager == null) {
			response.sendRedirect(request.getContextPath() + "/console/index.htm");
			return;
		}
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		// 查询条件一：特色库
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId_combox", 0);
		// 查询条件二：资源类型
		Long rltyId = ServletRequestUtils.getLongParameter(request, "rlty_Combox", 0);
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "RMIN_ENDDATE");
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		
		String jsondata = missionInfoManagementService.getOtherLanguageCheckedMissionListByJson(rmstId, rltyId, sysManager.getSmanId(),
				start, limit,sort, dir);
		out.write(jsondata);
		out.flush();
	}

	/**
	 * 更新作业状态
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-14
	 */
	public void saveResMissionInfoStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0);
			Long rminStatus = ServletRequestUtils.getLongParameter(request, "rminStatus", 0);
			if(rminId == 0)
				throw new ErrorMessageException("作业Id不能为空！");
			ResMissionInfo rmi = (ResMissionInfo)this.missionInfoManagementService.get(ResMissionInfo.class, rminId);
			if(rmi == null)
				throw new ErrorMessageException("该作业不存在！");
			if(rminStatus == 0)
				throw new ErrorMessageException("校验结果不正确！");
			// 增加权限校验：只有所有其它语资源通过审核才能通过作业
			if(rminStatus == 3) {
				List list = this.missionInfoManagementService.getOtherLanguageResSourceDataList(rminId);
				if(list.size() > 0)
					throw new ErrorMessageException("该作业下有未审核或者未通过的其它语种资源，不允许通过该作业！");
			}
			rmi.setRminStatus(rminStatus);
			this.missionInfoManagementService.save(rmi);
			out.write("{success:true,msg:'操作成功！'}");
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}catch(Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
			out.write("{success:false,msg:'系统忙，请稍后访问！'}");
		}
		out.flush();
	}

	public void getOtherLanguageResSourceDataList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		// 添加分页器
		Pagination pagination = new Pagination();
		// 设置当前页
		pagination.setCurrentPage(currentPage);
		// 设置每页记录数
		pagination.setPageResults(limit);
		Map<String, Object> conditions = new HashMap<String, Object>();
		// 查询条件一：状态
		Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0);
		conditions.put("rminId", rminId);
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rsdaBuilddate");
		conditions.put("sort", sort);
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		conditions.put("dir", dir);
		conditions.put("otherLanguageRSD", "1");
		List list = this.resMissionInfoCheckService.getResSourceDataList(pagination, conditions);
		String result = CommonUtil.createJsonTable(list, new String[] {"rsdaId", "rsdaTitle", "rltyName", "smanName",
				"rsdaBuilddate", "rsdaStatus", "rltyId", "rsLaNum"}, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}

	/**
	 * 其它语审核：作业 - 语种 - 资源列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-28
	 */
	public void getOtherLanguageResMissionLanguageDataList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		// 添加分页器
		Pagination pagination = new Pagination();
		// 设置当前页
		pagination.setCurrentPage(currentPage);
		// 设置每页记录数
		pagination.setPageResults(limit);
		// 查询条件一：状态
		Long rsdaStatus = ServletRequestUtils.getLongParameter(request, "rsdaStatus_combox", 0);
		// 查询条件二，题名
		String title = ServletRequestUtils.getStringParameter(request, "title", "").trim();
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rslaDate");
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		Long rmlaId = ServletRequestUtils.getLongParameter(request, "rmlaId", 0);
		ResMissionLanguage rmla = (ResMissionLanguage)this.missionInfoManagementService.get(ResMissionLanguage.class, rmlaId);
		if(rmla != null) {
			List list = this.resMissionInfoCheckService.getResSourceDataList(pagination, sort, dir, rmla.getResMissionId(), rmla
					.getResLanguageId(), rsdaStatus, title);
			String result = CommonUtil.createJsonTable(list, new String[] {"rsdaId", "rsdaTitle", "rltyName", "smanName",
					"rslaDate", "rslaStatus", "rltyId","rsdaCreator","rsdaPublisher"}, pagination.getTotalResults() + "");
			out.write(result);
			out.flush();
		}
	}

	/**
	 * 更新资源语种状态
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-15
	 */
	public ModelAndView saveRsdaOtherLanguageStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null) {
				response.sendRedirect(request.getContextPath() + "/console/index.htm");
				return null;
			}
			Long rslaId = ServletRequestUtils.getLongParameter(request, "rslaId", 0);
			Long rslaStatus = ServletRequestUtils.getLongParameter(request, "rslaStatus", 0);
			Long rmlaId = ServletRequestUtils.getLongParameter(request, "rmlaId", 0);
			String rslaAuditing = ServletRequestUtils.getStringParameter(request, "rslaAuditing", "");
			if(rslaStatus == 0)
				throw new ErrorMessageException("校验结果不正确！");
			ResSourceLanguage rsl = (ResSourceLanguage)this.missionInfoManagementService.get(ResSourceLanguage.class, rslaId);
			ResMissionLanguage rmla = (ResMissionLanguage)this.missionInfoManagementService.get(ResMissionLanguage.class, rmlaId);
			if(rsl != null) {
				rsl.setRslaStatus(rslaStatus);
				rsl.setRslaAuditing(rslaAuditing);
				//添加审核人登录名和姓名，该字段为统计工作量而设计
				rsl.setRslCheckLoginname(sysManager.getSmanLoginName());
				rsl.setRslCheckSuinname(sysManager.getSmanName());
				
				this.resMissionInfoCheckService.save(rsl);
				// 返回提示信息
				request.setAttribute("result", "<script>showSuccessMessage('" + request.getContextPath() + "/','2','保存成功');</script>");
				// 通过并审核下一条资源
				Long type = ServletRequestUtils.getLongParameter(request, "type", 0);
				if(type == 3 && rmla != null) {
					// 获取下一条资源:存在则跳转到详情页面，不存在则回到审核页面;
					List nextRsd = this.resMissionInfoCheckService.getNextOtherLanguageRSD(rslaId, rmla.getResMissionId(), rmla
							.getResLanguageId());
					if(nextRsd.size() > 0) {
						Long nextRsdaId = (Long)nextRsd.get(0);
						request.getRequestDispatcher(
								"/console/data/otherLanguageMissionInfo.htm?action=initResSourceDataOtherLanguageDetail&rsdaId="
										+ nextRsdaId + "&rltyId=" + rmla.getResLanguageId() + "&rmlaId=" + rmlaId).forward(request,
								response);
						return null;
					}
				}
				request.getRequestDispatcher(
						"/console/data/otherLanguageMissionInfo.htm?action=initOtherLanguageRmsMissionInfoDataCheck&rmlaId=" + rmlaId)
						.forward(request, response);
				return null;
			}
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			result.put("msg", e.getMessage());
			return new ModelAndView("resource/checkout/resSourceDataOtherLanguage", result);
		}catch(Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
		return new ModelAndView("resource/checkout/resSourceDataOtherLanguage", result);
	}
}
