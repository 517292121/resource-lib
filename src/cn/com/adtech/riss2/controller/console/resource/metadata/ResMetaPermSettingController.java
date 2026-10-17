package cn.com.adtech.riss2.controller.console.resource.metadata;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.IMetadataManagementService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.resource.ResMetaPerm;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.exception.ErrorMessageException;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class ResMetaPermSettingController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(ResMetaPermSettingController.class);

	private IMetadataManagementService metadataManagementService;

	public IMetadataManagementService getMetadataManagementService() {
		return metadataManagementService;
	}

	public void setMetadataManagementService(IMetadataManagementService metadataManagementService) {
		this.metadataManagementService = metadataManagementService;
	}

	/**
	 * 初始化特色库权限管理页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-19
	 */
	public ModelAndView initResMetaPermSettingView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/metadata/resMetaPermSetting");
	}

	/**
	 * 初始化分配权限页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-19
	 */
	public ModelAndView initPermSetting(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0);
		if(rmstId > 0) {
			ResMetaStore rms = (ResMetaStore)this.metadataManagementService.get(ResMetaStore.class, rmstId);
			if(rms != null)
				map.put("rmstId", rmstId);
		}
		return new ModelAndView("resource/metadata/permSetting", map);
	}

	/**
	 * 获取具有管理权限的特色库列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-19
	 */
	public void getResMetaStoreList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
		if(sysManager == null)
			return;
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
		// 查询条件
		String rmstName = ServletRequestUtils.getStringParameter(request, "rmstName", "");
		if(rmstName != null && rmstName.trim().length() > 0) {
			String[] searchCon1 = StringUtils.split(rmstName, " ,");
			List rmstNamesList = new ArrayList();
			addToCollection(rmstNamesList, searchCon1);
			conditions.put("rmstNamesList", rmstNamesList);
		}
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rmstBuilddate");
		conditions.put("sort", sort);
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		conditions.put("dir", dir);
		conditions.put("rmpeType", new Long(1));
		conditions.put("smanId", sysManager.getSmanId());
		List list = this.metadataManagementService.getResMetaStoreListByRmpeType(pagination, conditions);
		String result = CommonUtil.createJsonTable(list, new String[] {"rmstId", "smanName", "rmstName", "rmstCompany", "rmstBuilddate",
				"rmstStatus"}, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}

	/**
	 * 获取特色库权限列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-19
	 */
	public void getResMetaPermList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
		if(sysManager == null)
			return;
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
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rmpeBuilddate");
		conditions.put("sort", sort);
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		conditions.put("dir", dir);
		// 特色库
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0);
		// 权限类型
		Long rmpeType = ServletRequestUtils.getLongParameter(request, "rmpeType", 1);
		conditions.put("rmpeType", rmpeType);
		if(rmstId > 0) {
			ResMetaStore rms = (ResMetaStore)this.metadataManagementService.get(ResMetaStore.class, rmstId);
			if(rms != null) {
				conditions.put("rmstId", rmstId);
				List list = this.metadataManagementService.getResMetaPermList(pagination, conditions);
				String result = CommonUtil.createJsonTable(list, new String[] {"rmpeId", "rmpeType", "smanName", "rmpeBuilddate"},
						pagination.getTotalResults() + "");
				out.write(result);
				out.flush();
			}
		}
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
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0);
		Long rmpeType = ServletRequestUtils.getLongParameter(request, "rmpeType", 0);
		if(rmstId > 0 && rmpeType > 0) {
			List list = this.metadataManagementService.getSysManagerList(rmstId, rmpeType, null, "1");
			out.write(createJsonFromList(list));
		}else
			out.write("[]");
		out.flush();
	}

	/**
	 * 操作数据
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-20
	 */
	public void saveOrUpdateResMetaPerm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long rmpeType = ServletRequestUtils.getLongParameter(request, "rmpeType", 0); // 资源类型
			Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0);// 所属特色库
			Long smanId = ServletRequestUtils.getLongParameter(request, "smanId", 0);// 操作人
			String isEditing = ServletRequestUtils.getStringParameter(request, "isEditing", "false"); // submit 状态:false=新建；true=编辑;
			if(rmstId == 0)
				throw new ErrorMessageException("特色库ID不能为空！");
			ResMetaStore rms = (ResMetaStore)this.metadataManagementService.get(ResMetaStore.class, rmstId);
			if(rms == null)
				throw new ErrorMessageException("特色库不存在！");
			if("false".equals(isEditing)) {
				if(smanId == 0)
					throw new ErrorMessageException("操作人ID不能为空！");
				SysManager sman = (SysManager)this.metadataManagementService.get(SysManager.class, smanId);
				if(sman == null)
					throw new ErrorMessageException("该操作人不存在！");
				// 新增管理员
				List rmpList = this.metadataManagementService.getResMetaPermList(rmstId, rmpeType, smanId, new Long(3));
				if(rmpList.size() > 0)
					throw new ErrorMessageException("在特色库\"" + rms.getRmstName() + "\"下，" + sman.getSmanName() + "已经且有该权限了，不能重复添加相同的权限！");
				ResMetaPerm rmp = new ResMetaPerm();
				rmp.setRmstId(rmstId);
				rmp.setSmanId(smanId);
				rmp.setRmpeType(rmpeType);
				rmp.setRmpeBuilddate(new Date());
				this.metadataManagementService.save(rmp);
				out.write("{success:true,msg:'新增权限成功！'}");
			}else {
				// 编辑操作人
				Long rmpeId = ServletRequestUtils.getLongParameter(request, "rmpeId", 0);
				if(rmpeId <= 0)
					throw new ErrorMessageException("作业ID不能为空！");
				ResMetaPerm rmp = (ResMetaPerm)this.metadataManagementService.get(ResMetaPerm.class, rmpeId);
				if(rmp == null)
					throw new ErrorMessageException("该权限不存在！");
				List rmpList = this.metadataManagementService.getResMetaPermList(rmstId, rmpeType, rmp.getSmanId(), new Long(3));
				if(rmpList.size() > 0) {
					ResMetaPerm temp = (ResMetaPerm)rmpList.get(0);
					if(!temp.getRmpeId().equals(rmpeId))
						throw new ErrorMessageException("在特色库\"" + rms.getRmstName() + "\"下，" + rmp.getSysManager().getSmanName()
								+ "已经且有此权限了，不能重复添加相同的权限！");
				}
				if(smanId > 0)
					rmp.setSmanId(smanId);
				rmp.setRmpeType(rmpeType);
				this.metadataManagementService.save(rmp);
				out.write("{success:true,msg:'更新成功！'}");
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
	 * 删除权限
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-20
	 */
	public void delResMetaPerm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null) 
				return;
			Long rmpeId = ServletRequestUtils.getLongParameter(request, "rmpeId", 0);
			if(rmpeId <= 0)
				throw new ErrorMessageException("权限ID不能为空！");
			ResMetaPerm rmp = (ResMetaPerm)this.metadataManagementService.get(ResMetaPerm.class, rmpeId);
			if(rmp == null)
				throw new ErrorMessageException("该权限不存在！");
			this.metadataManagementService.delete(rmp);
			out.write("{success:true,msg:'删除成功！'}");
		}catch(ErrorMessageException e) {
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			out.write("{success:false,msg:'系统忙，请稍后访问！'}");
		}
		out.flush();
	}
	private String createJsonFromList(List list) {
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

	private void addToCollection(Collection collection, Object[] arr) {
		if(null != collection && null != arr) {
			CollectionUtils.addAll(collection, arr);
		}
	}
}
