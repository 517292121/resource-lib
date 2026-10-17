package cn.com.adtech.riss2.controller.console.resource.setting;

/**
 * 系统馆藏位置设置
 * @author MaChao
 * @date 2011-04-21
 */

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import com.jbetter.framework.session.Pagination;
import cn.com.adtech.riss2.bso.ICommonServiceCommonlyService;
import cn.com.adtech.riss2.controller.console.ids.IdsInformationController;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.SysCollectionPosition;
import cn.com.adtech.riss2.exception.ErrorMessageException;

public class SysCollectionPositionSetController extends MultiActionController {

	private static final Logger logger = Logger.getLogger(IdsInformationController.class);
	
	private ICommonServiceCommonlyService commonlyService;

	public void setCommonlyService(ICommonServiceCommonlyService commonlyService) {
		this.commonlyService = commonlyService;
	}
	
	/**
	 * 初始化馆藏位置设置页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-04-21
	 */
	public ModelAndView initView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("setting/collectionPositionSet");
	}
	
	/**
	 * 获取馆藏位置列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-04-21
	 */
	public void getSCPList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		// 添加分页器
		Pagination pagination = new Pagination();
		// 设置当前页q
		pagination.setCurrentPage(currentPage);
		// 设置每页记录数
		pagination.setPageResults(limit);
		Map<String, String> conditions = new HashMap<String, String>();
		// 查询条件
		String scpName = ServletRequestUtils.getStringParameter(request, "scpName", "").trim();
		if(scpName != null && scpName.length() > 0) {
			conditions.put("scpNameCondition", scpName);
		}
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "scpIndex");
		conditions.put("sort", sort);
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "ASC");
		conditions.put("dir", dir);
		List list = this.commonlyService.getSCPList(pagination, conditions);
		String result = CommonUtil.createJsonTable(list, new String[] {"scpId", "scpName", "scpIndex"}, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}
	
	/**
	 * 保存馆藏位置
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-04-21
	 */
	public void saveScp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			String scpName = ServletRequestUtils.getStringParameter(request, "scpName_Add", "").trim();//馆藏位置
			String scpIndex = ServletRequestUtils.getStringParameter(request, "scpIndex_Add", "0"); // 序号
			if(!CommonUtil.isNotNull(scpName))
				throw new ErrorMessageException("馆藏位置不能为空！");
			if(scpName.length() > 256)
				throw new ErrorMessageException("馆藏位置不能超过256个字符！");
			if(!CommonUtil.isNotNull(scpIndex))
				throw new ErrorMessageException("序号不能为空！");
			
			SysCollectionPosition scp = new SysCollectionPosition();
			// 新增信息
			scp.setScpName(scpName);
			scp.setScpIndex(Integer.parseInt(scpIndex));
			this.commonlyService.save(scp);
			// 更新分类中信息数量
			out.write("{success:true,msg:'馆藏位置添加成功！'}");
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
	 * 更新馆藏位置
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-04-21
	 */
	public void updateScp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			String scpId = ServletRequestUtils.getStringParameter(request, "scpId_Edit", ""); // ID
			String scpName = ServletRequestUtils.getStringParameter(request, "scpName_Edit", "").trim();// 馆藏位置
			String scpIndex = ServletRequestUtils.getStringParameter(request, "scpIndex_Edit", "0");	// 序号
			if(!CommonUtil.isNotNull(scpName))
				throw new ErrorMessageException("馆藏位置不能为空！");
			if(!CommonUtil.isNotNull(scpId))
				throw new ErrorMessageException("请选中要编辑的馆藏位置！");
			if(scpName.length() > 256)
				throw new ErrorMessageException("馆藏位置不能超过256个字符！");
			if(!CommonUtil.isNotNull(scpIndex))
				throw new ErrorMessageException("序号不能为空！");
			
			SysCollectionPosition scp = (SysCollectionPosition)this.commonlyService.get(SysCollectionPosition.class, Long.parseLong(scpId));
			if(scp == null)
				throw new ErrorMessageException("馆藏位置不存在！");
			
			scp.setScpName(scpName);
			scp.setScpIndex(Integer.parseInt(scpIndex));
			this.commonlyService.save(scp);
			out.write("{success:true,msg:'馆藏位置修改成功！'}");
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
	 * 删除馆藏位置
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-26
	 */
	public void delScp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long scpId = ServletRequestUtils.getLongParameter(request, "scpId", 0);
			if(scpId == 0)
				throw new ErrorMessageException("请选择要删除的馆藏位置！");
			SysCollectionPosition scp = (SysCollectionPosition)this.commonlyService.get(SysCollectionPosition.class, scpId);
			if(scp == null)
				throw new ErrorMessageException("该馆藏位置不存在！");
			this.commonlyService.delete(scp);
			out.write("{success:true,msg:'馆藏位置删除成功！'}");
			out.flush();
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}
	}
	
}
