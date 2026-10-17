package cn.com.adtech.riss2.controller.console.sc.docu;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import cn.com.adtech.riss2.bso.sc.docu.IDocuDeliveryService;
import cn.com.adtech.riss2.controller.console.resource.build.ResourceFileLoad;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.sc.ScConsultation;
import cn.com.adtech.riss2.domain.sc.ScConsultationType;
import cn.com.adtech.riss2.domain.sc.docu.ScDocuDelivery;
import cn.com.adtech.riss2.exception.ErrorMessageException;

import com.jbetter.framework.session.Pagination;
/**
 * 文献传递 后台管理
 * @author MaChao
 *
 * @date Aug 28, 2015 - 9:58:47 AM
 */
@SuppressWarnings("unchecked")
public class DocuDeliveryConsoleController extends ResourceFileLoad {
	private IDocuDeliveryService docuServices;
	public IDocuDeliveryService getDocuServices() {
		return docuServices;
	}
	public void setDocuServices(IDocuDeliveryService docuServices) {
		this.docuServices = docuServices;
	}

	/**
	 * 初始化 文献传递 后台页面
	 */
	public ModelAndView initDocuView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("sc/docu/scDocu");
	}

	/**
	 * 获取 文献传递 列表
	 */
	public void getDocuList(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "sddeReplyTime");
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		
		Long smanId = sysManager.getSmanId();
		Long sddeStatus = ServletRequestUtils.getLongParameter(request, "sddeStatus", 0); // 状态
		String loginName = ServletRequestUtils.getStringParameter(request, "loginName", "").trim();
		String sddeTitle = ServletRequestUtils.getStringParameter(request, "sddeTitle", "").trim();
	
		Map<String, Object> map = new HashMap<String, Object>();
		if(CommonUtil.isNotNull(sddeTitle) && sddeTitle.length() > 0) {
			map.put("sddeTitle", sddeTitle);
		}
		if(CommonUtil.isNotNull(loginName) && loginName.length() > 0) {
			map.put("loginName", loginName);
		}
		if(sddeStatus != null && sddeStatus > 0) {
			map.put("sddeStatus", sddeStatus);
		}
		if(smanId != null && smanId > 0) {
			map.put("smanId", smanId);
		}
		map.put("dir", dir);
		map.put("sort", sort);

		List list = this.docuServices.getDocuList(pagination, map);
		String result = CommonUtil.createJsonTable(list, new String[] {"sddeId", "sddeDocuTitle", "suinName", "suinLoginName",
				"sddeCreationTime", "sddeStatus"}, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}


	/**
	 * 删除文献传递
	 */
	public void deleteDocu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long sddeId = ServletRequestUtils.getLongParameter(request, "sddeId", 0);
			ScDocuDelivery docu = (ScDocuDelivery)this.docuServices.get(ScDocuDelivery.class, sddeId);
			if(docu == null)
				throw new ErrorMessageException("该服务不存在！");
			this.docuServices.delete(docu);
			out.write("{success:true,msg:'删除成功！'}");
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
	 * 查看详情或者回复 文献传递
	 */
	public ModelAndView addReply(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		Long sddeId = ServletRequestUtils.getLongParameter(request, "sddeId", 0);
		ScDocuDelivery sdde = (ScDocuDelivery)this.docuServices.get(ScDocuDelivery.class, sddeId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sdde", sdde);
		return new ModelAndView("sc/docu/scDocuDetail",result); 
	}
	
	/**
	 * 保存回复-文献传递
	 */
	public ModelAndView saveReply(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager  == null) {
				response.sendRedirect(request.getContextPath() + "/console/index.htm");
				return null;
			}
			String sddeReplyContent = ServletRequestUtils.getStringParameter(request, "sddeReplyContent", "");
			//上传文件
			String sddeDocuUrl = uploadFile(request, "sddeDocuUrl");
			
			Long sddeId = ServletRequestUtils.getLongParameter(request, "sddeId", 0);
			ScDocuDelivery sdde =  (ScDocuDelivery)this.docuServices.get(ScDocuDelivery.class, sddeId);
			
			if(sdde == null)
				throw new ErrorMessageException("服务不存在！");
			if(!CommonUtil.isNotNull(sddeReplyContent))
				throw new ErrorMessageException("回复内容不能为空！");
			
			sdde.setSddeDocuUrl(sddeDocuUrl);
			sdde.setSddeSmanId(sysManager.getSmanId());
			sdde.setSddeReplyContent(sddeReplyContent);
			sdde.setSddeReplyTime(new Date());
			sdde.setSddeStatus(ScConsultation.RESPONSED);
			this.docuServices.save(sdde);
			
			request.setAttribute("result", "<script>Msg.success('保存成功！');</script>");
			return initDocuView(request, response);
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			request.setAttribute("result", "<script>Msg.success('"+ e.getMessage()+"');</script>");
			return null;
		}catch(Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
			request.setAttribute("result", "<script>Msg.success('保存失败！');</script>");
			return null;
		}
	}
}
