package cn.com.adtech.riss2.controller.console.sc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.sc.IScService;
import cn.com.adtech.riss2.controller.common.AbsFormController;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.sc.ScIssue;
import cn.com.adtech.riss2.domain.sc.ScIssueReply;
import cn.com.adtech.riss2.domain.sc.ScIssueType;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.upload.IUploadService;

/**
 * 文献互助_发布文献求助信息
 * 
 * @author MaChao
 * @date 2010-11-2
 */
public class CreateScIssueReplyController extends AbsFormController {
	private IUploadService uploadService;

	public IUploadService getUploadService() {
		return uploadService;
	}

	public void setUploadService(IUploadService uploadService) {
		this.uploadService = uploadService;
	}

	@SuppressWarnings("unchecked")
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		// 获取登录用户实时数据
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo != null) {
			request.setAttribute("userinfo", userinfo);
		}
		// 保存求助信息
		Long scisId = ServletRequestUtils.getLongParameter(request, "scisId", 0);
		ScIssue scIssue = (ScIssue)((IScService)getCommomService()).get(ScIssue.class, scisId);
		request.setAttribute("scIssue", scIssue);
		// 保存求助回复信息
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(currentPage);
		int WEB_DEFAULT_PAGE_SIZE = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		pagination.setPageResults(WEB_DEFAULT_PAGE_SIZE);
		List scIssueReplyList = ((IScService)getCommomService())
				.getScIssueReplyList(pagination, scisId, scIssue.getScisCloseScirId(), null);
		request.setAttribute("scIssueReplyList", scIssueReplyList);
		request.setAttribute("pagination", pagination);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("pageUrl", "/platform/scIssue.htm?action=initScIssueDetailView&scisId=" + scisId);
		// 添加条件限制
		if(userinfo.getSuinId().equals(scIssue.getScisSuinId())) {
			request.setAttribute("isMe", true);
		}else {
			request.setAttribute("isMe", false);
		}
		ScIssueReply scIssueReply = new ScIssueReply();
		
		String scirContent = ServletRequestUtils.getStringParameter(request, "scirContent","");
		scIssueReply.setScirContent(scirContent);
		
		return scIssueReply;
	}

	@Override
	protected void validate(HttpServletRequest request, Object obj, Errors errors) {
		ScIssueReply scIssueReply = (ScIssueReply)obj;
		/**
		 * 实现国际化验证,Spring会根据session中的locale对象，自动调用属性文件
		 */
		Object val = request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		Locale locale = null;
		if(val != null) {
			locale = (Locale)val;
		}else {
			locale = new Locale("zh", "CN");
		}
		ResourceBundle rb = ResourceBundle.getBundle("messages", locale);
		// 验证回复内容
		String scirContent = scIssueReply.getScirContent();
		if(!CommonUtil.isNotNull(scirContent)) {
			request.setAttribute("scirContent", rb.getString("usercenter.issue.detail.info"));
			errors.rejectValue("scirContent", null, rb.getString("usercenter.issue.detail.info"));
			return;
		}else if(scirContent.length() > 8000) {
			request.setAttribute("scirContent", rb.getString("usercenter.issue.detail.info_1"));
			errors.rejectValue("scirContent", null, rb.getString("usercenter.issue.detail.info_1"));
			return;
		}
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		MultipartFile file = multiRequest.getFile("scirAttachmentName2");
		String scirAttachmentName = file.getOriginalFilename();
		// 验证附件
		if(scirAttachmentName != null && scirAttachmentName.trim().length() > 1) {
			if(scirAttachmentName.indexOf(".") == -1) {
				request.setAttribute("scirAttachmentName", rb.getString("usercenter.issue.reply.error.att"));
				errors.rejectValue("scirAttachmentName", null,  rb.getString("usercenter.issue.reply.error.att"));
				return;
			}
			if(file.getSize() == 0) {
				request.setAttribute("scirAttachmentName", rb.getString("usercenter.issue.reply.error.att.null"));
				errors.rejectValue("scirAttachmentName", null,   rb.getString("usercenter.issue.reply.error.att.null"));
				return;
			}
			int tempSize = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultIssueAttachementSize()));
			Long ISSUE_SIZE = Long.parseLong(1024 * 1024 * tempSize + "");
			if(file.getSize() > ISSUE_SIZE) {
				request.setAttribute("scirAttachmentName", rb.getString("usercenter.issue.reply.error.att.large") + tempSize + "M！");
				errors.rejectValue("scirAttachmentName", null, rb.getString("usercenter.issue.reply.error.att.large") + tempSize + "M！");
				return;
			}
		}
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj, BindException errors)
			throws Exception {
		// 获取登录用户ID
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Long scisId = ServletRequestUtils.getLongParameter(request, "scisId", 0);
		ScIssue sis = (ScIssue)((IScService)getCommomService()).get(ScIssue.class, scisId);
		if(sis != null) {
			ScIssueReply scIssueReply = (ScIssueReply)obj;
			Date date = new Date();
			scIssueReply.setScirScisId(scisId);
			scIssueReply.setScirReplyTime(date);
			scIssueReply.setScirSuinId(userinfo.getSuinId());
			scIssueReply.setScirIsuse(ScIssueReply.CAN_USE);
			scIssueReply.setScirGrade(Short.parseShort("0"));
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
			MultipartFile file = multiRequest.getFile("scirAttachmentName2");
			// 保存附件
			String scirAttachmentName = file.getOriginalFilename();
			if(scirAttachmentName != null && scirAttachmentName.trim().length() > 1) {
				byte[] temp = file.getBytes();
				String extName = getExtName(scirAttachmentName);
				String preName = getPreName(scirAttachmentName);
				if(preName.length() > 70)
					scirAttachmentName = preName.subSequence(0, 70) + extName;
				scIssueReply.setScirAttachmentName(scirAttachmentName);
				// 生成实际保存的文件名
				Long pre = System.currentTimeMillis();
				// 原图物理文件名
				String saveFileName = pre + "." + extName;
				String path = ConstantsObj.ISSUE_ATTACHEMENT + "/" + sis.getScisId();
				String url = "/FileDownload?downloadPath=" + uploadService.encode(path + "/" + saveFileName) + "&fileName="
						+ uploadService.encode(scirAttachmentName);
				// 上传文件
				this.uploadService.saveFile(path, saveFileName, temp);
				scIssueReply.setScirAttachmentFileName(saveFileName);
				scIssueReply.setScirAttachmentUrl(url);
			}
			((IScService)getCommomService()).save(scIssueReply);
			// 更新回复数
			sis.setScisReplyNumber(sis.getScisReplyNumber() + 1);
			sis.setScisLastReplyTime(date);
			sis.setScisLastReplyManId(userinfo.getSuinId());
			((IScService)getCommomService()).save(sis);
		}
		response.sendRedirect(request.getContextPath() + "/platform/scIssue.htm?action=initScIssueDetailView&scisId=" + scisId);
		return null;
	}

	@Override
	protected void supportData(HttpServletRequest request) {}

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
}
