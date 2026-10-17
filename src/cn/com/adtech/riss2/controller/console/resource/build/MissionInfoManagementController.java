package cn.com.adtech.riss2.controller.console.resource.build;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.IMissionDeliverService;
import cn.com.adtech.riss2.bso.resource.IMissionInfoManagementService;
import cn.com.adtech.riss2.bso.resource.IMissionLanguageTempService;
import cn.com.adtech.riss2.bso.sc.IResFilesystemService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.ResFilesystem;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResMissDevolve;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.upload.IUploadService;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class MissionInfoManagementController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(MissionInfoManagementController.class);

	private IMissionInfoManagementService missionInfoManagementService;
	private IMissionDeliverService deliverService = null;
	private IMissionLanguageTempService missionLanguageTempService = null;
	private IUploadService uploadService = null;
	private IUploadService localUploadService = null;
	private IUploadService remoteUploadService = null;
	private IResFilesystemService fileSystemService = null;

	public void setLocalUploadService(IUploadService localUploadService) {
		this.localUploadService = localUploadService;
	}

	public void setRemoteUploadService(IUploadService remoteUploadService) {
		this.remoteUploadService = remoteUploadService;
	}

	public void setDeliverService(IMissionDeliverService deliverService) {
		this.deliverService = deliverService;
	}

	public IMissionInfoManagementService getMissionInfoManagementService() {
		return missionInfoManagementService;
	}

	public void setMissionInfoManagementService(IMissionInfoManagementService missionInfoManagementService) {
		this.missionInfoManagementService = missionInfoManagementService;
	}

	/**
	 * 初始化作业管理页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-8
	 */
	public ModelAndView initMissionInfoManagementView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/build/missionInfoManagement");
	}

	/**
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
		int currentPage = start / limit;
		// 添加分页器
		Pagination pagination = new Pagination();
		// 设置当前页
		pagination.setCurrentPage(currentPage);
		// 设置每页记录数
		pagination.setPageResults(limit);
		Map<String, Object> conditions = new HashMap<String, Object>();
		// 查询条件一：特色库
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId_combox", 0);
		conditions.put("rmstId", rmstId);
		// 查询条件二：资源类型
		Long rltyId = ServletRequestUtils.getLongParameter(request, "rlty_Combox", 0);
		conditions.put("rltyId", rltyId);
		// 查询条件三：作业状态
		Long rminStatus_combox = ServletRequestUtils.getLongParameter(request, "rminStatus_combox", -1);
		conditions.put("rminStatus", rminStatus_combox);
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rminBuildDate");
		conditions.put("sort", sort);
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		conditions.put("dir", dir);
		conditions.put("smanId", sysManager.getSmanId());
		conditions.put("rmpeType", new Long(2));
		List list = this.missionInfoManagementService.getResMissionInfoList(pagination, conditions);
		String result = CommonUtil.createJsonTable(list, new String[] {"rminId", "rmstId", "rminMissName", "rmstName", "rltyName",
				"smanUseName", "smanCreateName", "rminBuildDate", "rminEndDate", "rminStatus", "rminRemark", "rminNum", "rminNameremark"}, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}

	/**
	 * 操作作业数据
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-9
	 */
	public void saveOrUpdateResMissionInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null) {
				response.sendRedirect(request.getContextPath() + "/console/index.htm");
				return;
			}
			Long rltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0); // 资源类型
			Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0);// 所属特色库
			Long languageId = ServletRequestUtils.getLongParameter(request, "languageId", 0);// 第一语种ID
			String rminMissName = ServletRequestUtils.getStringParameter(request, "rminMissName", "").trim(); // 作业名称
			String rminNameremark = ServletRequestUtils.getStringParameter(request, "RminNameremark",""); //作业名称备注
			String rminRemark = ServletRequestUtils.getStringParameter(request, "rminRemark", "").trim(); // 作业描述
			String isEditing = ServletRequestUtils.getStringParameter(request, "isEditing", "false"); // submit 状态:false=新建；true=编辑;
			if(rmstId <= 0)
				throw new ErrorMessageException("特色库ID不能为空！");
			ResMetaStore rms = (ResMetaStore)this.missionInfoManagementService.get(ResMetaStore.class, rmstId);
			if(rms == null)
				throw new ErrorMessageException("特色库不存在！");
			if(languageId == null)
				throw new ErrorMessageException("请选择第一语种！");
			if(!CommonUtil.isNotNull(rminMissName))
				throw new ErrorMessageException("作业名称不能为空！");
			if(CommonUtil.isNotNull(rminRemark) && rminRemark.length() > 350)
				throw new ErrorMessageException("作业描述不能超过350个字符！");
			if("false".equals(isEditing)) {
				// 新建作业
				ResMissionInfo rmi = this.missionInfoManagementService.getResMissionInfo(rmstId, rltyId, rminMissName);
				if(rmi != null)
					throw new ErrorMessageException("该作业名称在\"" + rmi.getResMetaStore().getRmstName() + "\"特色库下，\""
							+ rmi.getRminResLiterType().getRltyName() + "\"资源类型中已经存在！");
				rmi = new ResMissionInfo();
				rmi.setRminRmstId(rmstId);
				rmi.setRminAuthor(sysManager.getSmanId());
				rmi.setRminUser(sysManager.getSmanId());
				rmi.setRminMissname(rminMissName);
				rmi.setRminNameremark(rminNameremark);
				rmi.setRminRemark(rminRemark);
				rmi.setRminBuilddate(new Date());
				rmi.setRminStatus(ResMissionInfo.RMINSTATUS_NOTCOMMIT);
				rmi.setRminRltyId(rltyId);
				rmi.setRminLanugage(languageId);
				rmi.setRminResCount(0);
				rmi = (ResMissionInfo) this.missionInfoManagementService.saveMissionInfo(rmi);
				
				rmi = this.missionInfoManagementService.getResMissionInfoById(rmi.getRminId());
				localUploadService.createDir(SysConfigInfo.RES_DIR + rmi.getResMetaStore().getRmstFolder() + "/" + rmi.getRminMissname());
				
				out.write("{success:true,msg:'新增作业成功！'}");
			}else {
				// 编辑作业
				Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0);
				if(rminId <= 0)
					throw new ErrorMessageException("作业ID不能为空！");
				ResMissionInfo rmi = (ResMissionInfo)this.missionInfoManagementService.get(ResMissionInfo.class, rminId);
				long preLanugage = rmi.getRminLanugage();
				if(rmi == null)
					throw new ErrorMessageException("该作业不存在！");
				ResMissionInfo tempRmi = this.missionInfoManagementService.getResMissionInfo(rmstId, rltyId > 0 ? rltyId : rmi
						.getRminRltyId(), rminMissName);
				if(tempRmi != null && !tempRmi.getRminId().equals(rmi.getRminId()))
					throw new ErrorMessageException("该作业名称在\"" + rmi.getResMetaStore().getRmstName() + "\"特色库下，\""
							+ rmi.getRminResLiterType().getRltyName() + "\"资源类型中已经存在！");
				if(rltyId > 0)
					rmi.setRminRltyId(rltyId);
				rmi.setRminMissname(rminMissName);
				rmi.setRminRmstId(rmstId);
				rmi.setRminRemark(rminRemark);
				rmi.setRminLanugage(languageId);
				rmi.setRminNameremark(rminNameremark);
				this.missionInfoManagementService.save(rmi);
				missionLanguageTempService.updateSingleColumn(rminId, preLanugage, "resLanguageId", languageId);
				out.write("{success:true,msg:'更新作业成功！'}");
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
	 * 获取作业详细信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-9
	 */
	public void getResMissionInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0); // 作业id
			if(rminId <= 0)
				throw new ErrorMessageException("作业ID不能为空！");
			ResMissionInfo rmi = (ResMissionInfo)this.missionInfoManagementService.get(ResMissionInfo.class, rminId);
			if(rmi == null)
				throw new ErrorMessageException("该作业不存在！");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("rminMissName", rmi.getRminMissname());
			result.put("rminRmstId", rmi.getRminRmstId());
			result.put("rminRltyId", rmi.getRminRltyId());
			result.put("languageId", rmi.getRminLanugage());
			result.put("rminRemark", rmi.getRminRemark());
			result.put("rminRltyName", rmi.getRminResLiterType().getRltyName());
			result.put("rminUserName", rmi.getRminUserSysManager().getSmanName());
			result.put("rminNameremark", rmi.getRminNameremark());
			result.put("success", true);
			String json = JSONObject.fromObject(result).toString();
			out.write(json);
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			out.write("{success:false,msg:'系统忙，请稍后访问！'}");
		}
		out.flush();
	}

	/**
	 * 删除作业详细信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-9
	 */
	public void delResMissionInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0); // 作业id
			if(rminId <= 0)
				throw new ErrorMessageException("作业ID不能为空！");
			ResMissionInfo rmi = (ResMissionInfo)this.missionInfoManagementService.get(ResMissionInfo.class, rminId);
			if(rmi == null)
				throw new ErrorMessageException("该作业不存在！");
			if(rmi.getRminStatus() == 2)
				throw new ErrorMessageException("该作业还未被审核，不允许删除！");
//			if(rmi.getRminStatus() == 3)
//				throw new ErrorMessageException("该作业已经审核通过，不允许删除！");
			
			//验证作业中是否存在资源
			String hql = "select count(rsdaId) from ResSourceData where rsdaRminId=" + rminId;
			String count = this.missionInfoManagementService.getSingleData(hql);
			if(CommonUtil.isNotNull(count)){
				int num = Integer.parseInt(count);
				if(num > 0){
					throw new ErrorMessageException("该作业中存在资源，不允许删除！");
				}
			}
			
			this.missionLanguageTempService.deleteByMissionId(rminId);
			this.missionInfoManagementService.deleteSubjectByRminId(rminId);
			this.missionInfoManagementService.delete(rmi);
			Long fileServiceId = rmi.getResMetaStore().getRmstRfilId();
			if(fileServiceId == null || fileServiceId.equals(0L)){
				uploadService = localUploadService;
			}else{
				uploadService = remoteUploadService;
				ResFilesystem fileservice = fileSystemService.getFilesystem(fileServiceId);
				uploadService.setFileServiceBean(fileservice);
			}
			uploadService.deleteFileOrDir(rmi.getResMetaStore().getRmstFolder() + "/" + rmi.getRminMissname());
			out.write("{success:true,msg:'删除作业成功！'}");
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			out.write("{success:false,msg:'该作业下有子数据项，不允许删除！'}");
		}
		out.flush();
	}
	
	/**
	 * 保存任务转交 第一语转交
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-29
	 */
	public void saveRminChange(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null) 
				return;
			Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0);
			Long smanId = ServletRequestUtils.getLongParameter(request, "smanId", 0);
			String remark = ServletRequestUtils.getStringParameter(request, "remark","");
			short missiontype = Short.parseShort(ServletRequestUtils.getStringParameter(request, "missiontype",ResMissDevolve.MISSION_JIAGONG+"")); //任务类型，默认为作业加工
			SysManager sm = (SysManager)this.missionInfoManagementService.get(SysManager.class, smanId);
			if(sm==null)throw new ErrorMessageException("该操作者不存在！");
			ResMissionInfo rmi = (ResMissionInfo)this.missionInfoManagementService.get(ResMissionInfo.class, rminId);
			if(rmi!=null){
				// 新建任务转交
				ResMissDevolve rmd = new ResMissDevolve();
				rmd.setRmdeRminId(missionLanguageTempService.getMissionFirstId(rminId));
				//rmd.setRmdePreuid(rmi.getRminUser());
				rmd.setRmdeAftuid(smanId);
				rmd.setRmdeStatus(ResMissDevolve.DELIVER_WAITING);
				rmd.setRmdeMissiontype(missiontype);
				rmd.setRmdeRemark(remark);
				rmd.setRmdeMissdate(rmi.getRminBuilddate());
				rmd.setRmdeDevodate(new Date());
				// 更新作业信息
				if(missiontype == ResMissDevolve.MISSION_JIAGONG){ //第一语加工
					rmd.setRmdePreuid(rmi.getRminUser());
					//rmi.setRminUser(null);  //作业转交后双方都不能再操作该作业，直接对方接受或拒绝为止。因为将操作人设为0;
					//missionLanguageTempService.updateSingleColumn(rminId, rmi.getRminLanugage(), IMissionLanguageTempService.mlaProcessor, null);
				}else if(missiontype == ResMissDevolve.MISSION_XIAOYAN){ //其它语加工
					rmd.setRmdePreuid(rmi.getRminCheckuser());
					//rmi.setRminInteruser(-1L);
					//missionLanguageTempService.updateSingleColumn(rminId, rmi.getRminLanugage(), IMissionLanguageTempService.rmlaAssessor, null);
				}
				missionLanguageTempService.updateSingleColumn(rminId, rmi.getRminLanugage(), IMissionLanguageTempService.rmlaStatus, ResMissionInfo.RMINSTATUS_DELIVERING);
				rmi.setRminStatus(ResMissionInfo.RMINSTATUS_DELIVERING);
				this.missionInfoManagementService.save(rmd);
				this.missionInfoManagementService.save(rmi);
				out.write("{success:true,msg:'作业已成功转交,请等待对方应答！'}");
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
	 * 保存任务转交
	 * 
	 */
	public void saveMissionLanguageChange(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long missLanid = ServletRequestUtils.getLongParameter(request, "missId");
		long preUserId = ServletRequestUtils.getLongParameter(request, "preUid");
		long nextUserId = ServletRequestUtils.getLongParameter(request,	"afterUid");
		long missionId = ServletRequestUtils.getLongParameter(request, "missionId", 0L);
		short missiontype = Short.parseShort(ServletRequestUtils.getStringParameter(request, "missiontype",	ResMissDevolve.MISSION_JIAGONG + "")); // 任务类型，默认为作业加工
		
		String remark = ServletRequestUtils.getStringParameter(request,	"remark", "");
		String isfirstStr = ServletRequestUtils.getStringParameter(request, "isfirst", "false");
		
		boolean isfirst ;
		if(isfirstStr.equals("true")){
			isfirst = true;
		}else{
			isfirst = false;
		}
		
		ResMissDevolve rmd = new ResMissDevolve();
		rmd.setRmdeRminId(missLanid); // 作业语种ID
		rmd.setRmdePreuid(preUserId);
		rmd.setRmdeAftuid(nextUserId);
		rmd.setRmdeMissiontype(missiontype);
		rmd.setRmdeDevodate(new Date());
		rmd.setRmdeRemark(remark);
		rmd.setRmdeStatus(ResMissDevolve.DELIVER_WAITING);
		rmd.setMissionId(missionId);
		
		//rmd = (ResMissDevolve) this.missionInfoManagementService.save(rmd); //将之前的单独保存改到下面这个保存中统一保存
		boolean b = missionLanguageTempService.missionDeliver(rmd, isfirst);
		String jsondata = "";
		if (b) {
			jsondata = "{success:true,msg:'转交成功！'}";
		} else {
			jsondata = "{success:false,msg:'转交失败！'}";
		}
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}
	
	
	/**
	 * 获取管理员列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-15
	 */
	public void getSysManagerList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0);
		if(rminId < 1){
			out.write("[]");
			return;
		}
		int missiontype = ServletRequestUtils.getIntParameter(request, "missiontype",ResMissDevolve.MISSION_JIAGONG);
		long userid = ServletRequestUtils.getLongParameter(request, "userid", 0);
		long storePerm = 2L;
		
		ResMissionInfo rmi = (ResMissionInfo)this.missionInfoManagementService.get(ResMissionInfo.class, rminId);
		
		
		if(missiontype == ResMissDevolve.MISSION_JIAGONG){
			if(userid < 1){
				userid = rmi.getRminUser();
			}
			storePerm = 2L;
		}else if (missiontype == ResMissDevolve.MISSION_XIAOYAN) {
			if(userid < 1){
				userid = rmi.getRminCheckuser();
			}
			storePerm = 3L;
		}
		if(rmi != null) {
			List list = this.missionInfoManagementService.getSysManagerList(storePerm, rmi.getRminRmstId(), userid);
			String jsondata = createJsonFromList(list);
			out.write(jsondata);
		}
		out.flush();
	}

	private String createJsonFromList(List list) {
		if(list.size()==0)return "[]";
		StringBuilder result = new StringBuilder();
		result.append("[");
		for(int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[])list.get(i);
			result.append("['").append(obj[0]).append("','").append(obj[1]).append("']");
			if(i != list.size() - 1)
				result.append(",");
		}
		result.append("]");
		return result.toString();
	}

	/**
	 * 第一语提交审核 author 王林
	 */
	public void setMissionStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		long mission_id = MyRequestUtil.getLongParameter(request, "missionid");
		long status = MyRequestUtil.getLongParameter(request, "statu");
		boolean b = missionInfoManagementService.updateMissionStatus(mission_id, status);
		if(b) {
			//missionLanguageTempService.updateMissionFirstStatu(mission_id, MyRequestUtil.getIntParameter(request, "statu"));
			out.print("{success:true,msg:'作业提交成功,请注意关注资源审核结果！'}");
		}else {
			out.print("{success:false}");
		}
		out.flush();
	}
	
	/**
	 * 其它语录入提交审核 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void setMissionLanguageStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		long missLanId = MyRequestUtil.getLongParameter(request, "missId");
		int status = MyRequestUtil.getIntParameter(request, "statu");
		boolean b = missionLanguageTempService.updateMissionLanguageStatu(missLanId, status);
		if(b) {
			out.print("{success:true,msg:'作业提交成功,请注意关注资源审核结果！'}");
		}else {
			out.print("{success:false}");
		}
		out.flush();
	}
	
	
	/**
	 * 进入加工任务转交列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView missionDeliver(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/build/missionInt");
	}
	/**
	 * 进入加校验务转交列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView missionCheckedDeliver(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/checkout/deliverList");
	}
	
	
	public void deliverList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		response.setContentType("txt/plain;charset=utf-8");
		long userid = MyRequestUtil.getLoginUserId(request);
		int type = ServletRequestUtils.getIntParameter(request, "type", 0);
		int missionType = ServletRequestUtils.getIntParameter(request, "missType", 0);
		String missionTypes = ServletRequestUtils.getStringParameter(request, "mtypes","0");

		//查询总记录
		int count = deliverService.getDevolveListCount(userid, type,missionType, missionTypes);
		
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		// 添加分页器
		Pagination pagination = new Pagination();
		// 设置当前页
		pagination.setCurrentPage(currentPage);
		// 设置每页记录数
		pagination.setPageResults(limit);
		
		List<ResMissDevolve> list = deliverService.getDevolveList(userid, type,missionType, pagination,missionTypes);
		
		String[] keys = new String[]{"rmdeId","rminMissname","rmdeRminId","preSmanName","rmdePreuid","afterSmanName","rmdeAftuid","rmdeMissdate","rmdeDevodate","rmdeRemark","rmdeStatus","rmdeMind","rmdeAudidate","rmdeMissiontype"};
		String jsondata = CommonUtil.createJsonTable(list, keys , count+"");
		
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}
	
	/**
	 * 任务转交处理
	 */
	public void revertDeliver(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		long deliverId = ServletRequestUtils.getLongParameter(request, "deliverId", 0);
		int statu = ServletRequestUtils.getIntParameter(request, "statu", 0); 
		String remark = ServletRequestUtils.getStringParameter(request, "remark","");
		boolean b = deliverService.revertDeliver(deliverId, statu, remark);
		
		String jsondata = "";
		if (b) {
			jsondata = "1";
		} else {
			jsondata = "0";
		}
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}
	
	/**
	 * 撤销任务转交
	 */
	public void chexiaoDeliver(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		long deliverId = ServletRequestUtils.getLongParameter(request, "deliverId", 0);
		boolean b = deliverService.chexiao(deliverId);
		
		String jsondata = "";
		if (b) {
			jsondata = "1";
		} else {
			jsondata = "0";
		}
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}
	
	/**
	 * 删除转交记录
	 */
	public void deleteMissionDeliver(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		long deliverId = ServletRequestUtils.getLongParameter(request, "deliverId", 0);
		boolean b = deliverService.deleteDevolveById(deliverId);
		String jsondata = "";
		if (b) {
			jsondata = "1";
		} else {
			jsondata = "0";
		}
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}
	
	/**
	 * 设置作业的操作者
	 */
	public void setMissionUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long missionId = ServletRequestUtils.getLongParameter(request, "missionid", 0);
		int userType = ServletRequestUtils.getIntParameter(request, "userType", 0);
		long userid = ServletRequestUtils.getLongParameter(request, "userid", 0);
		long languageId = ServletRequestUtils.getLongParameter(request, "languageId", 0);
		boolean b = missionInfoManagementService.setMissionUser(userType, userid, missionId, languageId);
		String jsondata = "";
		if (b) {
			jsondata = "1";
		} else {
			jsondata = "0";
		}
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}

	public void setMissionLanguageTemp(IMissionLanguageTempService missionLanguageTempService) {
		this.missionLanguageTempService = missionLanguageTempService;
	}
	
	/**获取作业默认名称*
	 * @throws IOException */
	public void getMissionName(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String missionName = this.missionInfoManagementService.getMissionDefaultName();
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(missionName);
		out.flush();
	}

	public void setFileSystemService(IResFilesystemService fileSystemService) {
		this.fileSystemService = fileSystemService;
	}
}
