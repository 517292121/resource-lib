package cn.com.adtech.riss2.controller.console.resource.metadata;

/**
 * 特色库IP配置
 * @author MaChao
 * @time 2011-03-24
 */

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.IMetadataManagementService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.IpUtil;
import cn.com.adtech.riss2.domain.resource.ResStoreIpDeploy;

import com.jbetter.framework.session.Pagination;

public class IpDeployController extends MultiActionController{

	private IMetadataManagementService metadataManagementService;

	public void setMetadataManagementService(
			IMetadataManagementService metadataManagementService) {
		this.metadataManagementService = metadataManagementService;
	}
	
	public ModelAndView initMetaStoreIpDeploy(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		String metaStoreId = ServletRequestUtils.getStringParameter(request, "metaStoreId", "");
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
		Map<String, Object> conditions = new HashMap<String, Object>();
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "ASC");
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rsidIpPart");
		String ipPart = ServletRequestUtils.getStringParameter(request, "rsidIpPart", "");
		conditions.put("sort", sort);
		conditions.put("search", ipPart);
		conditions.put("dir", dir);
		List list = this.metadataManagementService.getResStoreIpDeployList(pagination, conditions, Long.parseLong(metaStoreId));
		String result = CommonUtil.createJsonTable(list, new String[] {"rsidId", "rsidRmstId", "rsidIpPart"}, pagination.getTotalResults() + "");
		out.write(result.replace(IpUtil.IP_RANGE_SPLIT, "——"));
		out.flush();
		return null;
	}
	
	public ModelAndView addStoreIpPart(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setContentType("txt/plain;charset=utf-8");
		String metaStoreId = ServletRequestUtils.getStringParameter(request, "metaStoreId", ""); 
		String metaStoreIpStart = ServletRequestUtils.getStringParameter(request, "ipPartStart", "");
		String metaStoreIpEnd = ServletRequestUtils.getStringParameter(request, "ipPartEnd", "");
		
		ResStoreIpDeploy rsid = new ResStoreIpDeploy();
		rsid.setRsidRmstId(Long.parseLong(metaStoreId));
		rsid.setRsidIpPart(IpUtil.resetIpRange(metaStoreIpStart, metaStoreIpEnd));
		this.metadataManagementService.save(rsid);
		response.getWriter().write("{success:true,msg:'IP配置成功'}");
		response.getWriter().flush();
		return null;
	}
	
	public ModelAndView editStoreIpPart(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setContentType("txt/plain;charset=utf-8");
		String ipDeployId = ServletRequestUtils.getStringParameter(request, "ipDeployId", ""); 
		String ipPartStartEdit = ServletRequestUtils.getStringParameter(request, "ipPartStartEdit", "");
		String ipPartEndEdit = ServletRequestUtils.getStringParameter(request, "ipPartEndEdit", "");
		
		ResStoreIpDeploy rsid = (ResStoreIpDeploy)this.metadataManagementService.get(ResStoreIpDeploy.class,Long.parseLong(ipDeployId));
		rsid.setRsidIpPart(IpUtil.resetIpRange(ipPartStartEdit, ipPartEndEdit));
		this.metadataManagementService.save(rsid);
		response.getWriter().write("{success:true,msg:'IP配置成功'}");
		response.getWriter().flush();
		return null;
	}
	
	public ModelAndView deleteStoreIpPart(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

			response.setContentType("text/html; charset=UTF-8");
			String ids = ServletRequestUtils.getStringParameter(request, "ids",
					"");
			String[] str = ids.split(",");
			for (int i = 0; i < str.length; i++) {
				this.metadataManagementService.delete((ResStoreIpDeploy)this.metadataManagementService.get(ResStoreIpDeploy.class,
						Long.parseLong(str[i])));
			}
			response.getWriter().write("success:删除成功！");
			response.getWriter().flush();
			return null;
	}
}
