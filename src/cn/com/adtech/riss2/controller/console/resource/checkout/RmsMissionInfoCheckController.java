package cn.com.adtech.riss2.controller.console.resource.checkout;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

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
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.exception.ErrorMessageException;

@SuppressWarnings("unchecked")
public class RmsMissionInfoCheckController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(RmsMissionInfoCheckController.class);

	private IResMissionInfoCheckService resMissionInfoCheckService;

	private IMissionInfoManagementService missionInfoManagementService;
	
	private IMissionLanguageTempService missionLanguageService = null;
	
	private static IResourceLuceneService resourceLuceneService = null;

	public void setResourceLuceneService(
			IResourceLuceneService resourceLuceneService) {
		this.resourceLuceneService = resourceLuceneService;
	}

	public void setMissionLanguageService(
			IMissionLanguageTempService missionLanguageService) {
		this.missionLanguageService = missionLanguageService;
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
	 * 初始化作业校验页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-13
	 */
	public ModelAndView initRmsMissionInfoCheckView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/checkout/rmsMissionInfoCheck");
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
		
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rminBuildDate");
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		String jsondata = missionInfoManagementService.getCheckedMissionListByJson(rmstId, rltyId , sysManager.getSmanId(), start, limit, sort, dir);
		out.write(jsondata);
		
		out.flush();
	}

	/**
	 * 第一语作业审核
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-14
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
			if(b){
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
			}else{
				out.write("{success:false,msg:'操作失败！'}");
			}
//			this.missionInfoManagementService.save(rmi);
//			missionLanguageService.updateSingleColumn(rminId, rmi.getRminLanugage(), IMissionLanguageTempService.rmlaAssetime, new Date());
//			missionLanguageService.updateSingleColumn(rminId, rmi.getRminLanugage(), IMissionLanguageTempService.rmlaStatus, rminStatus);
//			boolean b = missionLanguageService.missionChecked(missLanId, rminId, isfirst, Short.parseShort(rminStatus + ""));
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
	 * 作业其它语校验审核
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-28
	 */
	public void checkedMissionLanguage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long missLanId = ServletRequestUtils.getLongParameter(request, "missLanId", 0);
			long rminStatus = ServletRequestUtils.getLongParameter(request, "rminStatus", 0);
			if(rminStatus == 3){
				int noCheck = missionLanguageService.getUnCheckNumber(missLanId);
				if(noCheck > 0){
					throw new ErrorMessageException("该作业下有未审核或者未通过的资源，不允许通过该作业！");
				}
			}
			
			missionLanguageService.updateSingleColumn(missLanId, IMissionLanguageTempService.rmlaStatus, rminStatus);
			missionLanguageService.updateSingleColumn(missLanId, IMissionLanguageTempService.rmlaAssetime, new Date());
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
}
