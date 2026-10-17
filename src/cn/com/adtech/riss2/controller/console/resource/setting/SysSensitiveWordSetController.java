package cn.com.adtech.riss2.controller.console.resource.setting;

/**
 * 系统敏感词设置
 * @author MaChao
 * @date 2011-04-22
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
import cn.com.adtech.riss2.domain.SysSensitiveWord;
import cn.com.adtech.riss2.exception.ErrorMessageException;

public class SysSensitiveWordSetController extends MultiActionController {

private static final Logger logger = Logger.getLogger(IdsInformationController.class);
	
	private ICommonServiceCommonlyService commonlyService;

	public void setCommonlyService(ICommonServiceCommonlyService commonlyService) {
		this.commonlyService = commonlyService;
	}
	
	/**
	 * 初始化敏感词设置页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-04-22
	 */
	public ModelAndView initView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("setting/sensitiveWordSet");
	}
	
	/**
	 * 获取敏感词列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-04-22
	 */
	public void getSSWList(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		String sswName = ServletRequestUtils.getStringParameter(request, "sswName", "").trim();
		if(sswName != null && sswName.length() > 0) {
			conditions.put("sswNameCondition", sswName);
		}
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "sswIndex");
		conditions.put("sort", sort);
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "ASC");
		conditions.put("dir", dir);
		List list = this.commonlyService.getSSWList(pagination, conditions);
		String result = CommonUtil.createJsonTable(list, new String[] {"sswId", "sswName", "sswIndex"}, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}
	
	/**
	 * 保存敏感词
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-04-22
	 */
	public void saveSsw(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			String sswName = ServletRequestUtils.getStringParameter(request, "sswName_Add", "").trim();//馆藏位置
			String sswIndex = ServletRequestUtils.getStringParameter(request, "sswIndex_Add", "0"); // 序号
			if(!CommonUtil.isNotNull(sswName))
				throw new ErrorMessageException("馆藏位置不能为空！");
			if(sswName.length() > 256)
				throw new ErrorMessageException("馆藏位置不能超过256个字符！");
			if(!CommonUtil.isNotNull(sswIndex))
				throw new ErrorMessageException("序号不能为空！");
			
			SysSensitiveWord ssw = new SysSensitiveWord();
			// 新增信息
			ssw.setSswName(sswName);
			ssw.setSswIndex(Integer.parseInt(sswIndex));
			this.commonlyService.save(ssw);
			// 更新分类中信息数量
			out.write("{success:true,msg:'敏感词添加成功！'}");
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
	 * 更新敏感词
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-04-22
	 */
	public void updateSsw(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			String sswId = ServletRequestUtils.getStringParameter(request, "sswId_Edit", ""); // ID
			String sswName = ServletRequestUtils.getStringParameter(request, "sswName_Edit", "").trim();// 馆藏位置
			String sswIndex = ServletRequestUtils.getStringParameter(request, "sswIndex_Edit", "0");	// 序号
			if(!CommonUtil.isNotNull(sswName))
				throw new ErrorMessageException("敏感词不能为空！");
			if(!CommonUtil.isNotNull(sswId))
				throw new ErrorMessageException("请选中要编辑的敏感词！");
			if(sswName.length() > 256)
				throw new ErrorMessageException("敏感词不能超过256个字符！");
			if(!CommonUtil.isNotNull(sswIndex))
				throw new ErrorMessageException("序号不能为空！");
			
			SysSensitiveWord ssw = (SysSensitiveWord)this.commonlyService.get(SysSensitiveWord.class, Long.parseLong(sswId));
			if(ssw == null)
				throw new ErrorMessageException("敏感词不存在！");
			
			ssw.setSswName(sswName);
			ssw.setSswIndex(Integer.parseInt(sswIndex));
			this.commonlyService.save(ssw);
			out.write("{success:true,msg:'敏感词修改成功！'}");
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
	 * 删除敏感词
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-04-22
	 */
	public void delSsw(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long sswId = ServletRequestUtils.getLongParameter(request, "sswId", 0);
			if(sswId == 0)
				throw new ErrorMessageException("请选择要删除的敏感词！");
			SysSensitiveWord ssw = (SysSensitiveWord)this.commonlyService.get(SysSensitiveWord.class, sswId);
			if(ssw == null)
				throw new ErrorMessageException("该敏感词不存在！");
			this.commonlyService.delete(ssw);
			out.write("{success:true,msg:'敏感词删除成功！'}");
			out.flush();
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}
	}
}
