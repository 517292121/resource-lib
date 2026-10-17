package cn.com.adtech.riss2.controller.console.sc;

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

import cn.com.adtech.riss2.bso.sc.IScService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.domain.sc.ScConsultation;
import cn.com.adtech.riss2.domain.sc.ScConsultationType;
import cn.com.adtech.riss2.domain.sc.ScFaqLibrary;
import cn.com.adtech.riss2.domain.sc.ScFaqType;
import cn.com.adtech.riss2.domain.sc.ScIssue;
import cn.com.adtech.riss2.domain.sc.ScIssueReply;
import cn.com.adtech.riss2.domain.sc.ScIssueType;
import cn.com.adtech.riss2.exception.ErrorMessageException;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class ScConsultationController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(ScConsultationController.class);

	private IScService scServices;

	public IScService getScServices() {
		return scServices;
	}

	public void setScServices(IScService scServices) {
		this.scServices = scServices;
	}

	/**
	 * 初始化咨询分类后台管理页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-15
	 */
	public ModelAndView initScConsultationTypeView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List list = this.scServices.getScConsultationType(null, null, null);
		request.setAttribute("scIssueTypeList", list);
		return new ModelAndView("sc/scConsultationType");
	}

	/**
	 * 初始化参考咨询后台页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-15
	 */
	public ModelAndView initScConsultationView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("sc/scConsultation");
	}

	/**
	 * 获取咨询列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-15
	 */
	public void getScConsultationList(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "sccoReplyTime");
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		// 查询条件
		Long sctyId = ServletRequestUtils.getLongParameter(request, "scit_Combox_Condition", -1); // 类型
		Long sccoState = ServletRequestUtils.getLongParameter(request, "scIssue_status_Combox_Condition", 0); // 状态
		String loginName = ServletRequestUtils.getStringParameter(request, "loginName_Condition", "").trim();
		String sccoTitle = ServletRequestUtils.getStringParameter(request, "sccoTitle_Condition", "").trim();
		List list = this.scServices.getScConsultationList(pagination, sctyId, sccoTitle, loginName, sccoState, sort, dir, null, null);
		String result = CommonUtil.createJsonTable(list, new String[] {"sccoId", "sccoTitle", "sctyName", "suinName", "suinLoginName",
				"sccoCreationTime", "sccoState"}, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}

	/**
	 * 保存咨询信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-16
	 */
	public void saveScConsultation(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null) {
				response.sendRedirect(request.getContextPath() + "/console/index.htm");
				return;
			}
			// sccoTitle sccoState sctyName suinName sccoCreationTime sccoContent sccoReplyContent \ sccoReplyTime smanName
			Long sccoId = ServletRequestUtils.getLongParameter(request, "sccoId", 0);
			String sccoTitle = ServletRequestUtils.getStringParameter(request, "sccoTitle", "").trim();
			Long sctyId = ServletRequestUtils.getLongParameter(request, "sctyName", 0);
			String sccoContent = ServletRequestUtils.getStringParameter(request, "sccoContent", "").trim();
			String sccoReplyContent = ServletRequestUtils.getStringParameter(request, "sccoReplyContent", "").trim();
			ScConsultation sc = (ScConsultation)this.scServices.get(ScConsultation.class, sccoId);
			if(sc == null)
				throw new ErrorMessageException("咨询不存在！");
			ScConsultationType sct = (ScConsultationType)this.scServices.get(ScConsultationType.class, sctyId);
			if(sct == null)
				throw new ErrorMessageException("咨询类型不存在！");
			if(!CommonUtil.isNotNull(sccoTitle))
				throw new ErrorMessageException("咨询标题不能为空！");
			if(CommonUtil.isNotNull(sccoTitle) && sccoTitle.length() > 300)
				throw new ErrorMessageException("咨询标题不能超过300个字符！");
			if(!CommonUtil.isNotNull(sccoReplyContent))
				throw new ErrorMessageException("咨询回复内容不能为空！");
			if(CommonUtil.isNotNull(sccoReplyContent) && sccoReplyContent.length() > 8000)
				throw new ErrorMessageException("咨询回复内容不能超过8000个字符！");
			sc.setSccoContent(sccoContent);
			sc.setSccoReplyContent(sccoReplyContent);
			sc.setSccoReplyTime(new Date());
			sc.setSmanId(sysManager.getSmanId());
			sc.setSctyId(sctyId);
			sc.setSccoTitle(sccoTitle);
			sc.setSccoState(ScConsultation.RESPONSED);
			this.scServices.save(sc);
			out.write("{success:true,msg:'更新咨询成功！'}");
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

	public void saveScConsultation1(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null) {
				response.sendRedirect(request.getContextPath() + "/console/index.htm");
				return;
			}
			Long sccoId = ServletRequestUtils.getLongParameter(request, "sccoId", 0);
			String sccoTitle_1 = ServletRequestUtils.getStringParameter(request, "sc_sccoTitle_1", "").trim();
			Long sctyId_1 = ServletRequestUtils.getLongParameter(request, "sctyId_1", 0);
			String sccoContent_1 = ServletRequestUtils.getStringParameter(request, "sc_sccoContent_1", "").trim();
			String sccoReplyContent_1 = ServletRequestUtils.getStringParameter(request, "sc_sccoReplyContent_1", "").trim();
			ScConsultation sc = (ScConsultation)this.scServices.get(ScConsultation.class, sccoId);
			if(sc == null)
				throw new ErrorMessageException("咨询不存在！");
			ScConsultationType sct_1 = (ScConsultationType)this.scServices.get(ScConsultationType.class, sctyId_1);
			if(sct_1 == null)
				throw new ErrorMessageException("咨询类型不存在！");
			if(!CommonUtil.isNotNull(sccoTitle_1))
				throw new ErrorMessageException("咨询标题不能为空！");
			if(CommonUtil.isNotNull(sccoTitle_1) && sccoTitle_1.length() > 300)
				throw new ErrorMessageException("咨询标题不能超过300个字符！");
			if(!CommonUtil.isNotNull(sccoReplyContent_1))
				throw new ErrorMessageException("咨询回复内容不能为空！");
			if(CommonUtil.isNotNull(sccoReplyContent_1) && sccoReplyContent_1.length() > 8000)
				throw new ErrorMessageException("咨询回复内容不能超过8000个字符！");
			sc.setSccoContent(sccoContent_1);
			sc.setSccoReplyContent(sccoReplyContent_1);
			sc.setSccoReplyTime(new Date());
			sc.setSmanId(sysManager.getSmanId());
			sc.setSctyId(sctyId_1);
			sc.setSccoTitle(sccoTitle_1);
			sc.setSccoState(ScConsultation.RESPONSED);
			this.scServices.save(sc);
			out.write("{success:true,msg:'回复咨询成功！'}");
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
	 * 咨询详情
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-16
	 */
	public void initConsultation(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		Long sscoId = ServletRequestUtils.getLongParameter(request, "sscoId", 0);
		ScConsultation sc = (ScConsultation)this.scServices.get(ScConsultation.class, sscoId);
		if(sc == null) {
			out.write("{success:false,msg:'该咨询不存在!'}");
			return;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		ScConsultationType sct = sc.getScConsultationType();
		if(sct == null) {
			out.write("{success:false,msg:'咨询分类不存在!'}");
			return;
		}
		// sccoTitle sccoState sctyName suinName sccoCreationTime sccoContent sccoReplyContent \ sccoReplyTime smanName
		result.put("sccoTitle", sc.getSccoTitle());
		result.put("sctyName", sc.getSctyId());
		result.put("sctyName2", sc.getScConsultationType().getSctyName());
		result.put("suinName", sc.getSysUserinfo().getSuinName());
		result.put("sccoCreationTime", ConstantsObj.FORMAT_DATE_TIME.format(sc.getSccoCreationTime()));
		result.put("sccoContent", sc.getSccoContent());
		Long sccoState = sc.getSccoState();
		if(ScConsultation.UN_RESPONSE.equals(sccoState)) {
			result.put("sccoReplyContent", "");
			result.put("smanName", "");
			result.put("sccoReplyTime", "");
			result.put("sccoState", "未回复");
		}else {
			result.put("sccoReplyTime", ConstantsObj.FORMAT_DATE_TIME.format(sc.getSccoReplyTime()));
			result.put("sccoReplyContent", sc.getSccoReplyContent());
			result.put("smanName", sc.getSysManager().getSmanName());
			result.put("sccoState", "已回复");
		}
		result.put("success", true);
		JSONObject jsonObject = JSONObject.fromObject(result);
		out.write(jsonObject.toString());
		out.flush();
	}

	/**
	 * 获取咨询下拉框
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-15
	 */
	public void getScConsultationTypeCombox(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		List list = this.scServices.getScConsultationType(null, null, null);
		Long type = ServletRequestUtils.getLongParameter(request, "type", 0);
		if(type == 1) {
			ScConsultationType sct = new ScConsultationType();
			sct.setSctyId(-1L);
			sct.setSctyName("不限");
			list.add(0, sct);
		}
		String jsondata = createJsonFromList(list);
		out.write(jsondata);
		out.flush();
	}

	private String createJsonFromList(List list) {
		if(list.size() == 0)
			return "[]";
		StringBuilder result = new StringBuilder();
		result.append("[");
		for(int i = 0; i < list.size(); i++) {
			ScConsultationType sct = (ScConsultationType)list.get(i);
			result.append("['").append(sct.getSctyId()).append("','").append(sct.getSctyName()).append("']");
			if(i != list.size() - 1)
				result.append(",");
		}
		result.append("]");
		return result.toString();
	}

	/**
	 * 删除咨询列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-15
	 */
	public void deleteScCosultation(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long sccoId = ServletRequestUtils.getLongParameter(request, "sccoId", 0);
			ScConsultation sc = (ScConsultation)this.scServices.get(ScConsultation.class, sccoId);
			if(sc == null)
				throw new ErrorMessageException("该咨询不存在！");
			this.scServices.delete(sc);
			out.write("{success:true,msg:'删除咨询成功！'}");
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
	 * 添加到FAQ
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-4
	 */
	public void addToFaqLibrary(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null)
				return;
			String scflQuestion = ServletRequestUtils.getStringParameter(request, "scflQuestion", "").trim();// 问题
			String scflAnswer = ServletRequestUtils.getStringParameter(request, "scflAnswer", "").trim();// 回答
			String temp_ScftId = ServletRequestUtils.getStringParameter(request, "scftId", "").trim();
			if(!CommonUtil.isNotNull(scflQuestion))
				throw new ErrorMessageException("问题不能为空！");
			if(scflQuestion.length() > 1024)
				throw new ErrorMessageException("问题不能超过1024个字符！");
			if(!CommonUtil.isNotNull(scflAnswer))
				throw new ErrorMessageException("回答不能为空！");
			if(scflAnswer.length() > 1024)
				throw new ErrorMessageException("回答不能超过1024个字符！");
			if(!CommonUtil.isNotNull(temp_ScftId))
				throw new ErrorMessageException("分类不能为空！");
			List list = this.scServices.getScFaqLibraryList(new Pagination(0), scflQuestion, "scflTopTime", "DESC");
			if(list.size() > 0)
				throw new ErrorMessageException("该问题已经存在！");
			// 处理分类:1选中已有分类保存；2以默认分类保存；
			Long scftId = 0L;
			try {
				// 1：选中已有分类保存
				scftId = Long.parseLong(temp_ScftId);
			}catch(Exception e) {
				// 2：以默认分类保存:A.该默认分类存在；B.默认分类不存在：a.“参考咨询”存在；b.“参考咨询”不存在；
				// 查看FAQ库分类中是否已经存在该分类
				List list_2_A = this.scServices.getScFaqTypeList(null, null, temp_ScftId, null, null, null);
				if(list_2_A.size() > 0) {
					ScFaqType sft = (ScFaqType)list_2_A.get(0);
					scftId = sft.getScftId();
				}else {
					List list_2_B = this.scServices.getScFaqTypeList(null, null, "参考咨询", null, null, null);
					if(list_2_B.size() > 0) {
						// 创建二级分类：
						ScFaqType sft = (ScFaqType)list_2_B.get(0);
						// 获取当前最大排序号
						Long maxIndex = 1L;
						List list_2_B_a = this.scServices.getScFaqTypeList(null, sft.getScftParentId(), null, null, "1", null);
						if(list_2_B_a.size() > 0) {
							ScFaqType tempSft_a = (ScFaqType)list_2_B_a.get(0);
							maxIndex = tempSft_a.getScftIndex() + 1;
						}
						ScFaqType tempSFT = new ScFaqType();
						tempSFT.setScftGrade(2L);
						tempSFT.setScftIndex(maxIndex);
						tempSFT.setScftName(temp_ScftId);
						tempSFT.setScftParentId(sft.getScftId());
						this.scServices.save(tempSFT);
						scftId = tempSFT.getScftId();
					}else {
						// 获取当前最大排序号
						Long maxIndex_1 = 1L;
						List list_2_B_b = this.scServices.getScFaqTypeList(null, null, null, 1L, "1", null);
						if(list_2_B_b.size() > 0) {
							ScFaqType tempSft_a = (ScFaqType)list_2_B_b.get(0);
							maxIndex_1 = tempSft_a.getScftIndex() + 1;
						}
						// 创建一级分类：文献求助
						ScFaqType tempSFT = new ScFaqType();
						tempSFT.setScftGrade(1L);
						tempSFT.setScftIndex(maxIndex_1);
						tempSFT.setScftName("参考咨询");
						tempSFT.setScftParentId(null);
						this.scServices.save(tempSFT);
						// 创建二级分类：
						ScFaqType tempSFT2 = new ScFaqType();
						tempSFT2.setScftGrade(2L);
						tempSFT2.setScftIndex(1L);
						tempSFT2.setScftName(temp_ScftId);
						tempSFT2.setScftParentId(tempSFT.getScftId());
						this.scServices.save(tempSFT2);
						scftId = tempSFT2.getScftId();
					}
				}
			}
			ScFaqLibrary sfl = new ScFaqLibrary();
			sfl.setScflAnswer(scflAnswer);
			sfl.setScflQuestion(scflQuestion);
			sfl.setScflScftId(scftId);
			sfl.setScflIstop(false);
			this.scServices.save(sfl);
			out.write("{success:true,msg:'添加咨询到FAQ库成功！'}");
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
