package cn.com.adtech.riss2.controller.console.sc;

import java.io.PrintWriter;
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
import cn.com.adtech.riss2.domain.sc.ScIssue;
import cn.com.adtech.riss2.domain.sc.ScIssueReply;
import cn.com.adtech.riss2.domain.sc.ScIssueType;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.upload.IUploadService;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class ScIssueReplyController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(ScIssueReplyController.class);

	private IScService scServices;

	public IScService getScServices() {
		return scServices;
	}

	public void setScServices(IScService scServices) {
		this.scServices = scServices;
	}
	
	private IUploadService uploadService;

	public IUploadService getUploadService() {
		return uploadService;
	}

	public void setUploadService(IUploadService uploadService) {
		this.uploadService = uploadService;
	}

	/**
	 * 初始化后台求助回复信息管理
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-3
	 */
	public ModelAndView initScIssueReplyView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("sc/scIssueReply");
	}

	/**
	 * 求助回复列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-3
	 */
	public void getScIssueReplyList(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "scirReplyTime");
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		// 查询条件
		//  
		Long scitId = ServletRequestUtils.getLongParameter(request, "scitId_Combox", -1); // 类型
		Long temp_scirGrade = ServletRequestUtils.getLongParameter(request, "scirGrade_Combox", 0); // 满意度
		String scisTitle = ServletRequestUtils.getStringParameter(request, "scisTitle_Condition", "").trim(); // 标题
		Short scirGrade = Short.parseShort(temp_scirGrade.toString());
		List list = this.scServices.getScIssueReplyList(pagination, scitId, scisTitle, scirGrade, sort, dir);
		String result = CommonUtil.createJsonTable(list, new String[] {"scirId", "scitName", "scisTitle", "suinName", "scirReplyTime",
				"scirGrade", "scirIsuse", "scirContent"}, pagination.getTotalResults() + "");
		out.write(result);
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
	public void openScIssueReply(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long scirId = ServletRequestUtils.getLongParameter(request, "scirId", 0);
			Boolean scirIsuse = ServletRequestUtils.getBooleanParameter(request, "scirIsuse", true);
			ScIssueReply scir = (ScIssueReply)this.scServices.get(ScIssueReply.class, scirId);
			if(scir == null)
				throw new ErrorMessageException("该回复不存在！");
			if(ScIssueReply.CAN_NOT_USE.equals(scirIsuse)) {
				scir.setScirIsuse(ScIssueReply.CAN_USE);
				this.scServices.save(scir);
				out.write("{success:true,msg:'启用回复成功！'}");
			}else {
				scir.setScirIsuse(ScIssueReply.CAN_NOT_USE);
				this.scServices.save(scir);
				out.write("{success:true,msg:'禁用回复成功！'}");
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
	 * 删除回复
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-3
	 */
	public void deleteIssueReply(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			// 删除回复记录
			Long scirId = ServletRequestUtils.getLongParameter(request, "scirId", 0);
			ScIssueReply scir = (ScIssueReply)this.scServices.get(ScIssueReply.class, scirId);
			if(scir == null)
				throw new ErrorMessageException("该回复不存在！");
			// 查看是否该回复被采纳为最佳回复
			ScIssue tempScIssue = this.scServices.getScIssue(scirId);
			this.scServices.delete(scir);
			
			// 删除回复对应的附件
			String path = ConstantsObj.ISSUE_ATTACHEMENT + "/" + scir.getScirScisId();
			String tempFileName = scir.getScirAttachmentFileName();
			if(tempFileName != null && tempFileName.length() > 1)
				this.uploadService.deleteFile(path, tempFileName);
			// 更新回复数量
			ScIssue tempSis = scir.getScIssue();
			Long tempNum = tempSis.getScisReplyNumber();
			if(tempNum > 0)
				tempSis.setScisReplyNumber(tempNum - 1);
			else
				tempSis.setScisReplyNumber(0L);
			
			if(tempScIssue != null){
				tempSis.setScisCloseScirId(null);
				tempSis.setScisState(ScIssue.CLOSE);
				tempSis.setScisCloseComment(null);
			}
			this.scServices.save(tempSis);
			out.write("{success:true,msg:'删除回复成功！'}");
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
	 * 获取回复详细信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-3
	 */
	public void initScIssueReplyDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		Long scirId = ServletRequestUtils.getLongParameter(request, "scirId", 0);
		ScIssueReply scir = (ScIssueReply)this.scServices.get(ScIssueReply.class, scirId);
		if(scir == null) {
			out.write("{success:false,msg:'该求助回复不存在!'}");
			return;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		ScIssue scis = scir.getScIssue();
		if(scis == null) {
			out.write("{success:false,msg:'求助不存在!'}");
			return;
		}
		ScIssueType scit = scis.getScIssueType();
		if(scit == null) {
			out.write("{success:false,msg:'求助分类不存在!'}");
			return;
		}
		result.put("scisTitle", scis.getScisTitle());
		result.put("scitName", scit.getScitName());
		result.put("scirReplyTime", ConstantsObj.FORMAT_DATE_TIME.format(scir.getScirReplyTime()));
		result.put("suinName", scir.getSysUserinfo().getSuinName());
		result.put("scirGrade", scir.getScirGrade());
		Boolean scirIsuse = scir.getScirIsuse();
		if(ScIssueReply.CAN_NOT_USE.equals(scirIsuse))
			result.put("scirIsuse", "禁用");
		else
			result.put("scirIsuse", "可用");
		result.put("scirContent", scir.getScirContent());
		result.put("scirAttachmentName", scir.getScirAttachmentName());
		result.put("scirAttachmentUrl", scir.getScirAttachmentUrl());
		result.put("success", true);
		JSONObject jsonObject = JSONObject.fromObject(result);
		out.write(jsonObject.toString());
		out.flush();
	}
}
