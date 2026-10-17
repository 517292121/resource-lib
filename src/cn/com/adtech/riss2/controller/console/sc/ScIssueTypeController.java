package cn.com.adtech.riss2.controller.console.sc;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.sc.IScService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.IdsInformationType;
import cn.com.adtech.riss2.domain.sc.ScIssueType;
import cn.com.adtech.riss2.exception.ErrorMessageException;

@SuppressWarnings("unchecked")
public class ScIssueTypeController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(ScIssueTypeController.class);

	private IScService scServices;

	public IScService getScServices() {
		return scServices;
	}

	public void setScServices(IScService scServices) {
		this.scServices = scServices;
	}

	/**
	 * 求助分类
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-1
	 */
	public ModelAndView initScIssueTypeView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List list = this.scServices.getScIssueType(null, null, null);
		request.setAttribute("scIssueTypeList", list);
		return new ModelAndView("sc/scIssueType2");
	}

	/**
	 * 求助分类
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-1
	 */
	public void getScIssueTypeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "scitName");
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		List list = this.scServices.getScIssueTypeList(null, sort, dir);
		String result = CommonUtil.createJsonTable(list, new String[] {"scitId", "scitName", "scitIndex"}, pagination
				.getTotalResults()
				+ "");
		out.write(result);
		out.flush();
	}
	
	/**
	 * 操作求助分类
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-1
	 */
	public void saveOrUpdateScIssueType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			String scitName = ServletRequestUtils.getStringParameter(request, "scitName", "").trim();// 分类名称
			Long scitIndex = ServletRequestUtils.getLongParameter(request, "scitIndex", -1); // 分类排序
			String isEditing = ServletRequestUtils.getStringParameter(request, "isEditing", "false"); // submit 状态:false=新建；true=编辑;
			if(!CommonUtil.isNotNull(scitName))
				throw new ErrorMessageException("求助分类名称不能为空！");
			if(scitName.length() > 40)
				throw new ErrorMessageException("求助分类名称不能超过40个字符！");
			if(scitIndex == -1)
				throw new ErrorMessageException("求助分类排序号不正确！");
			if("false".equals(isEditing)) {
				// 新建求助分类
				List list = this.scServices.getScIssueTypeList(scitName, null, null);
				if(list.size()>0)
					throw new ErrorMessageException("该求助分类已经存在！");
				ScIssueType sity = new ScIssueType();
				sity.setScitName(scitName);
				sity.setScitIndex(scitIndex);
				sity.setScitNumber(0L);
				this.scServices.save(sity);
				out.write("{success:true,msg:'新增求助分类成功！'}");
			}else {
				// 编辑求助分类
				Long scitId = ServletRequestUtils.getLongParameter(request, "scitId", 0); // 求助分类ID
				if(scitId == 0)
					throw new ErrorMessageException("求助分类ID不能为空！");
				ScIssueType sity = (ScIssueType)this.scServices.get(ScIssueType.class, scitId);
				if(sity == null)
					throw new ErrorMessageException("该求助分类不存在！");
				List list = this.scServices.getScIssueTypeList(scitName, null, null);
				if(list.size() > 0){
					Object[] obj = (Object[])list.get(0);
					if(!obj[0].equals(scitId))
						throw new ErrorMessageException("该求助分类已经存在！");
				}
				sity.setScitName(scitName);
				sity.setScitIndex(scitIndex);
				this.scServices.save(sity);
				out.write("{success:true,msg:'更新求助分类成功！'}");
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
	 * 删除求助分类
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-1
	 */
	public void deleteScIssueType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long scitId = ServletRequestUtils.getLongParameter(request, "scitId", 0);
			if(scitId == 0)
				throw new ErrorMessageException("求助分类ID不能为空！");
			ScIssueType sity = (ScIssueType)this.scServices.get(ScIssueType.class, scitId);
			if(sity == null)
				throw new ErrorMessageException("该求助分类不存在！");
			this.scServices.delete(sity);
			out.write("{success:true,msg:'删除求助分类成功！'}");
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			out.write("{success:false,msg:'该求助分类下有子数据项，不允许删除！'}");
		}
		out.flush();
	}
}
