package cn.com.adtech.riss2.controller.console.resource.metadata;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.IMetadataManagementService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.resource.ResLanguageType;
import cn.com.adtech.riss2.exception.ErrorMessageException;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class ResLanguageTypeController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(ResLanguageTypeController.class);

	private IMetadataManagementService metadataManagementService;

	public IMetadataManagementService getMetadataManagementService() {
		return metadataManagementService;
	}

	public void setMetadataManagementService(IMetadataManagementService metadataManagementService) {
		this.metadataManagementService = metadataManagementService;
	}

	/**
	 * 初始化语言分类页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-21
	 */
	public ModelAndView initResLanguageTypeView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/metadata/resLanguageType");
	}

	/**
	 * 获取语言分类列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-21
	 */
	public void getResLanguageTypeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		// SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
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
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rltyName");
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		List list = this.metadataManagementService.getResLanguageTypeList(null, sort, dir);
		String result = CommonUtil.createJsonTable(list, new String[] {"rltyId", "rltyName"}, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}

	/**
	 * 操作语言数据
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-21
	 */
	public void saveOrUpdateResLanguageType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null)
				return;
			String rltyName = ServletRequestUtils.getStringParameter(request, "rltyName", "").trim();// 语言名称
			String isEditing = ServletRequestUtils.getStringParameter(request, "isEditing", "false"); // submit 状态:false=新建；true=编辑;
			if(!CommonUtil.isNotNull(rltyName))
				throw new ErrorMessageException("语言名称不能为空！");
			if(rltyName.length() > 20)
				throw new ErrorMessageException("语言名称不能超过20个字符！");
			if("false".equals(isEditing)) {
				// 新建语种
				List list = this.metadataManagementService.getResLanguageTypeList(rltyName, null, null);
				if(list.size() > 0)
					throw new ErrorMessageException("该语种已经存在！");
				ResLanguageType rlt = new ResLanguageType();
				rlt.setRltyName(rltyName);
				this.metadataManagementService.save(rlt);
				out.write("{success:true,msg:'新增语种成功！'}");
			}else {
				// 编辑语种
				Long rltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0); // 语言类型
				if(rltyId == 0)
					throw new ErrorMessageException("语种ID不能为空！");
				ResLanguageType rlt = (ResLanguageType)this.metadataManagementService.get(ResLanguageType.class, rltyId);
				if(rlt == null)
					throw new ErrorMessageException("该语种不存在！");
				List list = this.metadataManagementService.getResLanguageTypeList(rltyName, null, null);
				if(list.size() > 0) {
					Object[] obj = (Object[])list.get(0);
					if(!obj[0].equals(rltyId))
						throw new ErrorMessageException("该语种已经存在！");
				}
				rlt.setRltyName(rltyName);
				this.metadataManagementService.save(rlt);
				out.write("{success:true,msg:'更新语种名称成功！'}");
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
	 * 删除语种
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void delResLanguageType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long rltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0); // 语言类型
			if(rltyId == 0)
				throw new ErrorMessageException("语种ID不能为空！");
			ResLanguageType rlt = (ResLanguageType)this.metadataManagementService.get(ResLanguageType.class, rltyId);
			if(rlt == null)
				throw new ErrorMessageException("该语种不存在！");
			this.metadataManagementService.delete(rlt);
			out.write("{success:true,msg:'删除语种成功！'}");
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			out.write("{success:false,msg:'该语种下有子数据项，不允许删除！'}");
		}
		out.flush();
	}
}
