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
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.sc.ScFaqLibrary;
import cn.com.adtech.riss2.domain.sc.ScFaqType;
import cn.com.adtech.riss2.domain.sc.ScIssue;
import cn.com.adtech.riss2.domain.sc.ScIssueReply;
import cn.com.adtech.riss2.domain.sc.ScIssueType;
import cn.com.adtech.riss2.exception.ErrorMessageException;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class ScIssueController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(ScIssueController.class);

	private IScService scServices;

	public IScService getScServices() {
		return scServices;
	}

	public void setScServices(IScService scServices) {
		this.scServices = scServices;
	}

	/**
	 * 初始化求助后台管理页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-2
	 */
	public ModelAndView initScIssueView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("sc/scIssue");
	}

	/**
	 * 获取求助信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-2
	 */
	public void getScIssueList(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "scisLastReplyTime");
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		// 查询条件
		Long scitId = ServletRequestUtils.getLongParameter(request, "scitId_Combox", -1); // 类型
		Long scisState = ServletRequestUtils.getLongParameter(request, "scisState_Combox", 0); // 状态
		String scisTitle = ServletRequestUtils.getStringParameter(request, "scisTitle_Condition", "").trim(); // 标题
		List list = this.scServices.getScIssueList(pagination, scitId, scisTitle, scisState, sort, dir);
		String result = CommonUtil.createJsonTable(list, new String[] {"scisId", "scisTitle", "scitName", "suinName", "scisCreationTime",
				"scisState", "scisReplyNumber", "scisIsuse", "scisLastReplyTime"}, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}

	/**
	 * 获取求助分类下拉框
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-2
	 */
	public void getScIssueTypeCombox(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		List list = this.scServices.getScIssueTypeList(null, "scitIndex", "DESC");
		list.add(0, new Object[] {"-1", "不限"});
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
			Object[] obj = (Object[])list.get(i);
			result.append("['").append(obj[0]).append("','").append(obj[1]).append("']");
			if(i != list.size() - 1)
				result.append(",");
		}
		result.append("]");
		return result.toString();
	}

	/**
	 * 获取求助详细信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-2
	 */
	public void getScIssueDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		Long scisId = ServletRequestUtils.getLongParameter(request, "scisId", 0);
		ScIssue scIssue = (ScIssue)this.scServices.get(ScIssue.class, scisId);
		if(scIssue == null) {
			out.write("{success:false,msg:'该求助不存在!'}");
			return;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		ScIssueType scit = scIssue.getScIssueType();
		if(scit == null) {
			out.write("{success:false,msg:'求助分类不存在!'}");
			return;
		}
		result.put("scisTitle", scIssue.getScisTitle());
		result.put("scitName", scit.getScitName());
		result.put("suinName", scIssue.getScisSuin().getSuinName());
		result.put("creationTime", ConstantsObj.FORMAT_DATE_TIME.format(scIssue.getScisCreationTime()));
		Long scisState = scIssue.getScisState();
		if(ScIssue.UN_SOLVE.equals(scisState))
			result.put("scisState", "待解决");
		else if(ScIssue.SOLVE.equals(scisState))
			result.put("scisState", "已解决");
		else
			result.put("scisState", "关闭");
		if(ScIssue.CAN_NOT_USE == scIssue.getScisIsuse())
			result.put("scisIsuse", "禁用");
		else
			result.put("scisIsuse", "可用");
		result.put("scisContent", scIssue.getScisContent());
		ScIssueReply temp = scIssue.getCloseScIssueReply();
		if(temp == null)
			result.put("scirContent", "");
		else
			result.put("scirContent", temp.getScirContent());
		result.put("success", true);
		JSONObject jsonObject = JSONObject.fromObject(result);
		out.write(jsonObject.toString());
		out.flush();
	}

	/**
	 * 删除求助
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-3
	 */
	public void deleteScIssue(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long scisId = ServletRequestUtils.getLongParameter(request, "scisId", 0);
			if(scisId == 0)
				throw new ErrorMessageException("求助ID不能为空！");
			ScIssue scis = (ScIssue)this.scServices.get(ScIssue.class, scisId);
			if(scis == null)
				throw new ErrorMessageException("该求助不存在！");
			this.scServices.delete(scis);
			out.write("{success:true,msg:'删除求助成功！'}");
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			out.write("{success:false,msg:'该求助下已有回复，不允许删除！'}");
		}
		out.flush();
	}

	/**
	 * 启用开关
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-3
	 */
	public void openScIssue(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long scisId = ServletRequestUtils.getLongParameter(request, "scisId", 0);
			Boolean scisIsuse = ServletRequestUtils.getBooleanParameter(request, "scisIsuse", true);
			if(scisId == 0)
				throw new ErrorMessageException("求助ID不能为空！");
			ScIssue scis = (ScIssue)this.scServices.get(ScIssue.class, scisId);
			if(scis == null)
				throw new ErrorMessageException("该求助不存在！");
			if(ScIssue.CAN_NOT_USE.equals(scisIsuse)) {
				scis.setScisIsuse(ScIssue.CAN_USE);
				this.scServices.save(scis);
				out.write("{success:true,msg:'启用求助成功！'}");
			}else {
				scis.setScisIsuse(ScIssue.CAN_NOT_USE);
				this.scServices.save(scis);
				out.write("{success:true,msg:'禁用求助成功！'}");
			}
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
			// Long scftId = ServletRequestUtils.getLongParameter(request, "scftId", 0);
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
				// 2：以默认分类保存:A.该默认分类存在；B.默认分类不存在：a.“文献求助”存在；b.“文献求助”不存在；
				// 查看FAQ库分类中是否已经存在该分类
				List list_2_A = this.scServices.getScFaqTypeList(null, null, temp_ScftId, null, null, null);
				if(list_2_A.size() > 0) {
					ScFaqType sft = (ScFaqType)list_2_A.get(0);
					scftId = sft.getScftId();
				}else {
					List list_2_B = this.scServices.getScFaqTypeList(null, null, "文献求助", null, null, null);
					if(list_2_B.size() > 0) {
						// 创建二级分类：
						ScFaqType sft = (ScFaqType)list_2_B.get(0);
						// 获取当前最大排序号
						Long maxIndex = 1L;
						List list_2_B_a = this.scServices.getScFaqTypeList(null, sft.getScftParentId(), null, null, "1", null);
						if(list_2_B_a.size() > 0) {
							ScFaqType tempSft_a = (ScFaqType)list_2_B_a.get(0);
							maxIndex = tempSft_a.getScftIndex()+1;
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
							maxIndex_1 = tempSft_a.getScftIndex()+1;
						}
						// 创建一级分类：文献求助
						ScFaqType tempSFT = new ScFaqType();
						tempSFT.setScftGrade(1L);
						tempSFT.setScftIndex(maxIndex_1);
						tempSFT.setScftName("文献求助");
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
			out.write("{success:true,msg:'添加求助到FAQ库成功！'}");
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
