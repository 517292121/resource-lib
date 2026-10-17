package cn.com.adtech.riss2.controller.console.resource.checkout;

import java.io.PrintWriter;
import java.util.Date;
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
import cn.com.adtech.riss2.bso.resource.IMissionLanguageTempService;
import cn.com.adtech.riss2.bso.resource.IResMissionInfoCheckService;
import cn.com.adtech.riss2.bso.resource.IResourceLuceneService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.exception.ErrorMessageException;

import com.jbetter.framework.session.Pagination;

/**
 * 资源批量审核
 * 
 * @author MaChao
 * @date 2011-3-8
 */
public class ResSourceDataBatchCheckController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(RmsMissionInfoCheckController.class);

	private IResMissionInfoCheckService resMissionInfoCheckService;

	private IMissionInfoManagementService missionInfoManagementService;

	private IMissionLanguageTempService missionLanguageService;
	
	private static IResourceLuceneService resourceLuceneService = null;

	public void setResourceLuceneService(
			IResourceLuceneService resourceLuceneService) {
		this.resourceLuceneService = resourceLuceneService;
	}
	public IResMissionInfoCheckService getResMissionInfoCheckService() {
		return resMissionInfoCheckService;
	}

	public void setResMissionInfoCheckService(IResMissionInfoCheckService resMissionInfoCheckService) {
		this.resMissionInfoCheckService = resMissionInfoCheckService;
	}

	public IMissionInfoManagementService getMissionInfoManagementService() {
		return missionInfoManagementService;
	}

	public void setMissionInfoManagementService(IMissionInfoManagementService missionInfoManagementService) {
		this.missionInfoManagementService = missionInfoManagementService;
	}

	public IMissionLanguageTempService getMissionLanguageService() {
		return missionLanguageService;
	}

	public void setMissionLanguageService(IMissionLanguageTempService missionLanguageService) {
		this.missionLanguageService = missionLanguageService;
	}

	/**
	 * 初始化作业校验页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView initRmsMissionInfoView(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("resource/checkout/rmsMissionInfoBatchCheck");
	}

	/**
	 * 获取作业列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
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
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rminBuildDate");
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		String jsondata = missionInfoManagementService.getCheckedMissionListByJson(rmstId, rltyId, sysManager.getSmanId(), start, limit,
				sort, dir);
		out.write(jsondata);
		out.flush();
	}

	/**
	 * 作业审核
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void saveResMissionInfoStatus(final HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			final Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0);
			Long rminStatus = ServletRequestUtils.getLongParameter(request, "rminStatus", 0);
			if(rminId == 0)
				throw new ErrorMessageException("作业Id不能为空！");
			ResMissionInfo rmi = (ResMissionInfo)this.missionInfoManagementService.get(ResMissionInfo.class, rminId);
			if(rmi == null)
				throw new ErrorMessageException("该作业不存在！");
			if(rminStatus == 0)
				throw new ErrorMessageException("校验结果不正确！");
			// 增加权限校验：只有所有资源通过审核才能通过作业
			if(rminStatus == 3) {
				List list = this.missionInfoManagementService.getResSourceDataList(rminId, 2L);
				if(list.size() > 0)
					throw new ErrorMessageException("该作业下有未审核或者未通过的资源，不允许通过该作业！");
			}
			rmi.setRminStatus(rminStatus);
			rmi.setRminEnddate(new Date());
			boolean b = this.missionInfoManagementService.checkFirstLanguage(rmi, rminId);
			if(b) {
				//当作业通过审核就将该作业下边的资源追加到全文检索里边
				if(rminStatus==3){
					//由于批量读取源文件来创建索引可能耗费时间较长，因此开个线程后台执行该操作
					new Thread(){
						@Override
						public void run() {
							super.run();
							resourceLuceneService.addIndexByMission(request, rminId);
						
						}
					}.start();
				}
//				((IIndexCacheService) WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("indexCacheService")).updateAllMainCache();
				out.write("{success:true,msg:'操作成功！'}");
			}else {
				out.write("{success:false,msg:'操作失败！'}");
			}
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

	/**
	 * 初始化作业资源校验页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView initResSourceDataBatchCheckView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0);
		Long userId = ServletRequestUtils.getLongParameter(request, "userid", 0);
		String sql = "select RMIN_STATUS from RES_MISSION_INFO where RMIN_ID = " + rminId;
		String rminStatu = resMissionInfoCheckService.getFirstData(sql);
		if(userId == 0) {
			userId = resMissionInfoCheckService.getUserId(rminId, "rminCheckuser");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rminId", rminId);
		map.put("userId", userId);
		map.put("rminStatu", rminStatu);
		return new ModelAndView("resource/checkout/resSourceDataBatchCheck", map);
	}

	/**
	 * 获取资源列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getResMissionInfoDataList(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		Long rsdaStatus = ServletRequestUtils.getLongParameter(request, "rsdaStatus_combox", 0);
		conditions.put("rslaStatus", rsdaStatus);
		Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0);
		conditions.put("rminId", rminId);
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rslaDate");
		conditions.put("sort", sort);
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		conditions.put("dir", dir);
		List list = this.resMissionInfoCheckService.getResSourceDataList(pagination, conditions);
		String result = CommonUtil.createJsonTable(list, new String[] {"rsdaId", "rsdaTitle", "rltyName", "smanName",
				"rsdaBuilddate", "rsdaStatus", "rltyId", "rsdaCreator", "rsdaPublisher"}, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}

	/**
	 * 批量审核资源
	 * @param request
	 * @param response
	 */
	public void resSourceDataBatchCheck(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = null;
		String result = "{success:true,msg:'操作成功！'}";
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null) {
				response.sendRedirect(request.getContextPath() + "/console/index.htm");
				return;
			}
			String rsdaIds = ServletRequestUtils.getStringParameter(request, "rsdaIds", "");
			if(!ServiceUtil.isNotNull(rsdaIds)) {
				throw new ErrorMessageException("请至少选择一行数据然后才能够操作！");
			}
			Long rsdaStatus = ServletRequestUtils.getLongParameter(request, "rsdaStatus", 2);
			resMissionInfoCheckService.updateBatchCheckResSourceData(rsdaIds, rsdaStatus, sysManager);
			out = response.getWriter();
		}catch(ErrorMessageException e) {
			logger.error(e.getMessage());
			result = "{success:false,msg:'" + e.getMessage() + "'}";
		}catch(Exception e) {
			logger.error(e.getMessage());
			result = "{success:false,msg:'系统忙，请稍后访问！'}";
		}
		out.write(result);
		out.flush();
	}
	
	/**
	 * 批量审核作业中的所有资源
	 * @param request
	 * @param response
	 */
	public void checkAllByMission(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = null;
		String result = "{success:true,msg:'操作成功！'}";
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null) {
				response.sendRedirect(request.getContextPath() + "/console/index.htm");
				return;
			}
			long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0l);
			Long rsdaStatus = ServletRequestUtils.getLongParameter(request, "rsdaStatus", 2);
			resMissionInfoCheckService.updateCheckAllByMission(rminId, rsdaStatus, sysManager);
			out = response.getWriter();
		}catch(ErrorMessageException e) {
			logger.error(e.getMessage());
			result = "{success:false,msg:'" + e.getMessage() + "'}";
		}catch(Exception e) {
			logger.error(e.getMessage());
			result = "{success:false,msg:'系统忙，请稍后访问！'}";
		}
		out.write(result);
		out.flush();
	}
	
	/**
	 * 重建作业下所有资源到索引
	 * @param request
	 * @param response
	 */
	public void reMissionIndex(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter out = null;
		String result = "{success:true,msg:'操作成功！'}";
		try {
			out = response.getWriter();
			Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0L);
			
			System.out.println("删除现有索引中....");
			resourceLuceneService.deleteMissionIndex(rminId);			
			
			System.out.println("索引创建中...");
			resourceLuceneService.addIndexByMission(request, rminId);
		}catch(ErrorMessageException e) {
			logger.error(e.getMessage());
			result = "{success:false,msg:'" + e.getMessage() + "'}";
		}catch(Exception e) {
			logger.error(e.getMessage());
			result = "{success:false,msg:'系统忙，请稍后访问！'}";
		}
		out.write(result);
		out.flush();
		out.close();
	}
}
