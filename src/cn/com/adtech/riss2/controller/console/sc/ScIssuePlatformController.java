package cn.com.adtech.riss2.controller.console.sc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.sc.IScService;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.sc.ScIssue;
import cn.com.adtech.riss2.domain.sc.ScIssueReply;
import cn.com.adtech.riss2.domain.sc.ScIssueType;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.upload.IUploadService;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class ScIssuePlatformController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(ScIssuePlatformController.class);
	
	private IScService scServices;

	private IUploadService uploadService;

	public IScService getScServices() {
		return scServices;
	}

	public void setScServices(IScService scServices) {
		this.scServices = scServices;
	}

	/**
	 * 初始化文献求助前台页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-9
	 */
	public ModelAndView initPlatformScIssueView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
		String search = ServletRequestUtils.getStringParameter(request, "search", "");
		Long scitId = ServletRequestUtils.getLongParameter(request, "scitId", 0);
		Long sctype = ServletRequestUtils.getLongParameter(request, "sctype", 0);
		Pagination pagination = new Pagination();
		int WEB_DEFAULT_PAGE_SIZE = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		pagination.setPageResults(WEB_DEFAULT_PAGE_SIZE);
		pagination.setCurrentPage(currentPage);
		List issueTypeList = this.scServices.getScIssueTypeList();
		map.put("issueTypeList", issueTypeList);
//		List noResoleList = this.scServices.getScIssueList(pagination, scitId, null, search, ScIssue.UN_SOLVE, null);
		//默认取出所有求助问题,传参后根据状态去取
		List noResoleList = null;
		if(sctype > 0L){
			noResoleList = this.scServices.getScIssueList(pagination, scitId, null, search, ScIssue.UN_SOLVE, null);  
		}else{
			noResoleList = this.scServices.getScIssueList(pagination, scitId, null, search, null, null);  
		}
		map.put("noResoleList", noResoleList);
		map.put("pagination", pagination);
		map.put("currentPage", currentPage);
		map.put("pageUrl", "/platform/scIssue.htm?action=initPlatformScIssueView");
		map.put("search", search);
		return new ModelAndView("platform/userCenter/usercenterIssue", map);
	}

	/**
	 * 求助分类
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-26
	 */
	public ModelAndView initPlatformScIssueTypeView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
		String search = ServletRequestUtils.getStringParameter(request, "search", "");
		Long scitId = ServletRequestUtils.getLongParameter(request, "scitId", 0);
		Pagination pagination = new Pagination();
		int WEB_DEFAULT_PAGE_SIZE = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		pagination.setPageResults(WEB_DEFAULT_PAGE_SIZE);
		pagination.setCurrentPage(currentPage);
		List issueTypeList = this.scServices.getScIssueTypeList();
		map.put("issueTypeList", issueTypeList);
		List list = this.scServices.getScIssueList(pagination, scitId, null, search, null, null);
		map.put("list", list);
		map.put("pagination", pagination);
		map.put("currentPage", currentPage);
		map.put("pageUrl", "/platform/scIssue.htm?action=initPlatformScIssueTypeView");
		map.put("search", search);
		return new ModelAndView("platform/userCenter/usercenterIssueType", map);
	}

	/**
	 * 最新解决
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-11
	 */
	public ModelAndView initPlatformLatestScIssueView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
		String search = ServletRequestUtils.getStringParameter(request, "search", "");
		Pagination pagination = new Pagination();
		int WEB_DEFAULT_PAGE_SIZE = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		pagination.setPageResults(WEB_DEFAULT_PAGE_SIZE);
		pagination.setCurrentPage(currentPage);
		List issueTypeList = this.scServices.getScIssueTypeList();
		map.put("issueTypeList", issueTypeList);
		List LatestResoleList = this.scServices.getScIssueList(pagination, null, null, search, ScIssue.SOLVE, null);
		map.put("LatestResoleList", LatestResoleList);
		map.put("pagination", pagination);
		map.put("currentPage", currentPage);
		map.put("search", search);
		map.put("pageUrl", "/platform/scIssue.htm?action=initPlatformLatestScIssueView");
		return new ModelAndView("platform/userCenter/usercenterIssue2", map);
	}

	/**
	 * 无满意答案
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-11
	 */
	public ModelAndView issueClose(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		ScIssue sis = null;
		Long scisId = null;
		try {
			SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
			if(userinfo == null) {
				response.sendRedirect(request.getContextPath() + "/index.htm");
				return null;
			}
			scisId = ServletRequestUtils.getLongParameter(request, "scisId", 0);
			Long scirId = ServletRequestUtils.getLongParameter(request, "scirId", 0);
			String scisCloseComment = ServletRequestUtils.getStringParameter(request, "scirContent", "");
			String grade = ServletRequestUtils.getStringParameter(request, "scirGrade","3");
			if(scisCloseComment.trim().length() > 300)
				throw new ErrorMessageException("评论不能超过300个字符！");
			sis = (ScIssue)this.scServices.get(ScIssue.class, scisId);
			if(sis == null)
				throw new ErrorMessageException("求助不存在！");
			
			Short scirGrade = 1;
			try {
				scirGrade = Short.parseShort(grade);
			}catch(Exception e) {
				scirGrade = 1;
			}
			
			
			ScIssueReply sir = (ScIssueReply)this.scServices.get(ScIssueReply.class, scirId);
			sis.setScisCloseTime(new Date());
			if(sir != null) {
				sis.setScisCloseComment(scisCloseComment);
				sis.setScisCloseScirId(scirId);
				sis.setScisState(ScIssue.SOLVE);
				sir.setScirGrade(scirGrade);
				this.scServices.save(sir);
			}else {
				sis.setScisState(ScIssue.CLOSE);
			}
			this.scServices.save(sis);
			response.sendRedirect(request.getContextPath() + "/platform/scIssue.htm?action=initScIssueDetailView&scisId=" + scisId);
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/platform/scIssue.htm?action=initScIssueDetailView&scisId=" + scisId);
		}
		return null;
	}

	/**
	 * 初始化我的求助前台页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-9
	 */
	public ModelAndView initPlatformMyIssueView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
		Pagination pagination = new Pagination();
		int WEB_DEFAULT_PAGE_SIZE = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		pagination.setPageResults(WEB_DEFAULT_PAGE_SIZE);
		pagination.setCurrentPage(currentPage);
		String search = ServletRequestUtils.getStringParameter(request, "search", "");
		List myIssueList = this.scServices.getScIssueList(pagination, null, userinfo.getSuinId(), search, null, null);
		map.put("myIssueList", myIssueList);
		map.put("pagination", pagination);
		map.put("currentPage", currentPage);
		map.put("pageUrl", "/platform/scIssue.htm?action=initPlatformMyIssueView");
		map.put("search", search);
		return new ModelAndView("platform/userCenter/usercenterMyIssue", map);
	}

	/**
	 * 初始化我参与的求助前台页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-9
	 */
	public ModelAndView initPlatformMyJoinIssueView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
		Pagination pagination = new Pagination();
		int WEB_DEFAULT_PAGE_SIZE = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		pagination.setPageResults(WEB_DEFAULT_PAGE_SIZE);
		pagination.setCurrentPage(currentPage);
		String search = ServletRequestUtils.getStringParameter(request, "search", "");
		List myJoinIssueList = this.scServices.getScIssueList(pagination, null, userinfo.getSuinId(), search, null, "1");
		map.put("myJoinIssueList", myJoinIssueList);
		map.put("pagination", pagination);
		map.put("currentPage", currentPage);
		map.put("pageUrl", "/platform/scIssue.htm?action=initPlatformMyJoinIssueView");
		map.put("search", search);
		return new ModelAndView("platform/userCenter/usercenterMyJoinIssue", map);
	}

	/**
	 * 初始化前台文献求助页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-9
	 */
	public ModelAndView initScIssueDetailView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		// 保存求助信息
		Long scisId = ServletRequestUtils.getLongParameter(request, "scisId", 0);
		ScIssue scIssue = (ScIssue)this.scServices.get(ScIssue.class, scisId);
		if(scIssue != null)
			map.put("scIssue", scIssue);
		// 保存求助回复信息
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(currentPage);
		int WEB_DEFAULT_PAGE_SIZE = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		pagination.setPageResults(WEB_DEFAULT_PAGE_SIZE);
		List scIssueReplyList = this.scServices.getScIssueReplyList(pagination, scisId, scIssue.getScisCloseScirId(), null);
		map.put("scIssueReplyList", scIssueReplyList);
		map.put("pagination", pagination);
		map.put("currentPage", currentPage);
		map.put("pageUrl", "/platform/scIssue.htm?action=initScIssueDetailView&scisId=" + scisId);
		// 添加条件限制
		if(userinfo.getSuinId().equals(scIssue.getScisSuinId()))
			map.put("isMe", true);
		else
			map.put("isMe", false);
		map.put("scIssueReply", null);
		return new ModelAndView("platform/userCenter/usercenterIssueDetail", map);
	}

	/**
	 * 删除回复
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-11
	 */
	public ModelAndView delScIssueReply(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
			if(userinfo == null) {
				response.sendRedirect(request.getContextPath() + "/index.htm");
				return null;
			}
			Long scirId = ServletRequestUtils.getLongParameter(request, "scirId", 0);
			ScIssueReply sir = (ScIssueReply)this.scServices.get(ScIssueReply.class, scirId);
			if(sir == null)
				throw new ErrorMessageException("回复不存在！");
			String path = ConstantsObj.ISSUE_ATTACHEMENT + "/" + sir.getScirScisId();
			this.scServices.delete(sir);
			String tempFileName = sir.getScirAttachmentFileName();
			if(tempFileName != null && tempFileName.length() > 1)
				this.uploadService.deleteFile(path, tempFileName);
			map.put("msg", "删除回复成功！");
			// 更新回复数量
			ScIssue tempSis = sir.getScIssue();
			Long tempNum = tempSis.getScisReplyNumber();
			if(tempNum > 0)
				tempSis.setScisReplyNumber(tempNum - 1);
			else
				tempSis.setScisReplyNumber(0L);
			this.scServices.save(tempSis);
			map.put("scIssue", sir.getScIssue());
			Pagination pagination = new Pagination();
			pagination.setCurrentPage(0);
			int WEB_DEFAULT_PAGE_SIZE = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
			pagination.setPageResults(WEB_DEFAULT_PAGE_SIZE);
			List scIssueReplyList = this.scServices.getScIssueReplyList(pagination, sir.getScirScisId(), null, null);
			map.put("scIssueReplyList", scIssueReplyList);
		}catch(ErrorMessageException e) {
			map.put("msg", e.getMessage());
			return new ModelAndView("platform/userCenter/usercenterIssueDetail", map);
		}
		return new ModelAndView("platform/sc/ajaxScIssueDetail", map);
	}

	/**
	 * 删除求助
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView delScIssue(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		ScIssue sis = null;
		Long scisId = null;
		try {
			SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
			if(userinfo == null) {
				response.sendRedirect(request.getContextPath() + "/index.htm");
				return null;
			}
			scisId = ServletRequestUtils.getLongParameter(request, "scisId", 0);
			sis = (ScIssue)this.scServices.get(ScIssue.class, scisId);
			if(sis == null)
				throw new ErrorMessageException("求助不存在！");
			if(sis.getScisReplyNumber() > 0)
				throw new ErrorMessageException("该求助下已经存在回复了，不允许删除！");
			if(!userinfo.getSuinId().equals(sis.getScisSuinId()))
				throw new ErrorMessageException("你没有权限删除该求助！");
			this.scServices.delete(sis);
			map.put("msg", "删除求助成功！");
			// 更新回复数量
			ScIssueType sit = sis.getScIssueType();
			Long tempNum = sit.getScitNumber();
			if(tempNum > 0)
				sit.setScitNumber(tempNum - 1);
			else
				sit.setScitNumber(0L);
			this.scServices.save(sit);
			response.sendRedirect(request.getContextPath() + "/platform/scIssue.htm?action=initPlatformMyIssueView");
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/platform/scIssue.htm?action=initPlatformMyIssueView");
		}
		return null;
	}

	/**
	 * 返回文件后缀名，以"."开始 。例 文件名"pic.jpg",返回的后缀名"jpg"。
	 * 
	 * @param fileName
	 * @return
	 */
	public String getExtName(String fileName) {
		String extName = "";
		if(fileName != null) {
			extName = fileName.substring(fileName.lastIndexOf('.') + 1);
		}
		return extName;
	}

	/**
	 * 返回文件前缀，如pic.jpg,返回pic
	 * 
	 * @param fileName
	 * @return
	 */
	public String getPreName(String fileName) {
		String preName = "";
		if(fileName != null && !"".equals(fileName)) {
			// preName = fileName.split("\\.")[0];
			preName = fileName.substring(0, fileName.lastIndexOf('.'));
		}
		return preName;
	}

	public IUploadService getUploadService() {
		return uploadService;
	}

	public void setUploadService(IUploadService uploadService) {
		this.uploadService = uploadService;
	}
}
