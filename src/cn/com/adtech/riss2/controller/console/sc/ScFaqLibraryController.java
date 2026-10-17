package cn.com.adtech.riss2.controller.console.sc;

import java.io.PrintWriter;
import java.util.Date;
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
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.sc.ScFaqLibrary;
import cn.com.adtech.riss2.domain.sc.ScFaqType;
import cn.com.adtech.riss2.exception.ErrorMessageException;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class ScFaqLibraryController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(ScFaqLibraryController.class);

	private IScService scServices;

	public IScService getScServices() {
		return scServices;
	}

	public void setScServices(IScService scServices) {
		this.scServices = scServices;
	}

	/**
	 * 初始化FAQ管理页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-4
	 */
	public ModelAndView initScFaqLibraryView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("sc/scFaqLibrary");
	}

	/**
	 * 构造分类下拉框
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-4
	 */
	public void getScFaqTypeCombox(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		List<ScFaqType> list = this.scServices.getScFaqTypeList(null, null, null, null, null, null);
		String jsondata = createJsonFromList(list);
		out.write(jsondata);
		System.out.println(jsondata);
		out.flush();
	}

	/**
	 * 获取FAQ列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-2
	 */
	public void getScFaqLibraryList(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "scflTopTime");
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		// 查询条件
		List list = this.scServices.getScFaqLibraryList(pagination, null, sort, dir);
		String result = CommonUtil.createJsonTable(list, new String[] {"scflId", "scftName", "scflQuestion", "scflTopTime", "scflIstop"},
				pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}

	/**
	 * 操作FAQ数据
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-4
	 */
	public void saveOrUpdateFaqLibrary(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null)
				return;
			String scflQuestion = ServletRequestUtils.getStringParameter(request, "scflQuestion", "").trim();// 问题
			String scflAnswer = ServletRequestUtils.getStringParameter(request, "scflAnswer", "").trim();// 回答
			Long scftId = ServletRequestUtils.getLongParameter(request, "scftId", 0);
			String isEditing = ServletRequestUtils.getStringParameter(request, "isEditing", "false"); // submit 状态:false=新建；true=编辑;
			if(!CommonUtil.isNotNull(scflQuestion))
				throw new ErrorMessageException("问题不能为空！");
			if(scflQuestion.length() > 1024)
				throw new ErrorMessageException("问题不能超过1024个字符！");
			if(!CommonUtil.isNotNull(scflAnswer))
				throw new ErrorMessageException("回答不能为空！");
			if(scflAnswer.length() > 1024)
				throw new ErrorMessageException("回答不能超过1024个字符！");
			ScFaqType sft = (ScFaqType)this.scServices.get(ScFaqType.class, scftId);
			if(sft == null)
				throw new ErrorMessageException("分类不存在！");
			if("false".equals(isEditing)) {
				// 新建
				List list = this.scServices.getScFaqLibraryList(new Pagination(0), scflQuestion, "scflTopTime", "DESC");
				if(list.size() > 0)
					throw new ErrorMessageException("该问题已经存在！");
				ScFaqLibrary sfl = new ScFaqLibrary();
				sfl.setScflAnswer(scflAnswer);
				sfl.setScflQuestion(scflQuestion);
				sfl.setScflScftId(scftId);
				sfl.setScflIstop(false);
				this.scServices.save(sfl);
				out.write("{success:true,msg:'新增问题成功！'}");
			}else {
				// 编辑
				Long scflId = ServletRequestUtils.getLongParameter(request, "scflId", 0);
				ScFaqLibrary sfl = (ScFaqLibrary)this.scServices.get(ScFaqLibrary.class, scflId);
				if(sfl == null)
					throw new ErrorMessageException("该问题不存在！");
				List list = this.scServices.getScFaqLibraryList(new Pagination(0), scflQuestion, "scflTopTime", "DESC");
				if(list.size() > 0) {
					Object[] obj = (Object[])list.get(0);
					if(!obj[0].equals(scflId))
						throw new ErrorMessageException("该问题已经存在！");
				}
				sfl.setScflQuestion(scflQuestion);
				sfl.setScflAnswer(scflAnswer);
				sfl.setScflScftId(scftId);
				this.scServices.save(sfl);
				out.write("{success:true,msg:'更新问题成功！'}");
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
	 * 删除FAQ
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-4
	 */
	public void deleteFaqLibrary(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long scflId = ServletRequestUtils.getLongParameter(request, "scflId", 0);
			ScFaqLibrary sfl = (ScFaqLibrary)this.scServices.get(ScFaqLibrary.class, scflId);
			if(sfl == null)
				throw new ErrorMessageException("该问题不存在！");
			this.scServices.delete(sfl);
			out.write("{success:true,msg:'删除问题成功！'}");
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
	 * 问题置顶
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-4
	 */
	public void faqLibraryToTop(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long scflId = ServletRequestUtils.getLongParameter(request, "scflId", 0);
			String scflIstop = ServletRequestUtils.getStringParameter(request, "scflIstop", "false");
			ScFaqLibrary sfl = (ScFaqLibrary)this.scServices.get(ScFaqLibrary.class, scflId);
			if(sfl == null)
				throw new ErrorMessageException("该问题不存在！");
			if(scflIstop.equals("false")) {
				sfl.setScflTopTime(new Date());
				sfl.setScflIstop(true);
				this.scServices.save(sfl);
				out.write("{success:true,msg:'置顶成功！'}");
			}else {
				sfl.setScflTopTime(null);
				sfl.setScflIstop(false);
				this.scServices.save(sfl);
				out.write("{success:true,msg:'拆消置顶成功！'}");
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
	 * 获取详细信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-4
	 */
	public void getScFaqLibraryDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long scflId = ServletRequestUtils.getLongParameter(request, "scflId", 0);
			ScFaqLibrary sfl = (ScFaqLibrary)this.scServices.get(ScFaqLibrary.class, scflId);
			if(sfl == null)
				throw new ErrorMessageException("该问题不存在！");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("scflAnswer", sfl.getScflAnswer());
			result.put("scflQuestion", sfl.getScflQuestion());
			result.put("scftId", sfl.getScflScftId());
			result.put("success", true);
			String json = JSONObject.fromObject(result).toString();
			out.write(json);
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

	// [['651288861800584','├参考咨询'],['651288861800601','├─我的咨询'],['651288861800602','├─我参与的咨询'],['651288861800583','├文献求助'],['651288920129143','├─论文'],['651288861800598','├─我的求助'],['651288861800599','├─我的回复'],['651288920129146','├─EJB'],['651288925738825','├─法律服务hgj'],]
	private String createJsonFromList(List<ScFaqType> list) {
		if(list.size() == 0)
			return "[]";
		StringBuilder result = new StringBuilder();
		result.append("[");
		int len = list.size();
		int temp = 0;
		for(int i = 0; i < len; i++) {
			// 查询一级分类总记录数
			List level1 = this.scServices.getScFaqTypeList(null, null, null, 1L, null, null);
			int level1_len = level1.size();
			ScFaqType sft = list.get(i);
			// 一级分类
			if(sft.getScftGrade() == 1) {
				temp++;
				result.append("['").append(sft.getScftId()).append("','├").append(sft.getScftName()).append("']");
				if(temp <= level1_len)
					result.append(",");
				for(int j = 0; j < len; j++) {
					// 二级分类
					ScFaqType sft2 = list.get(j);
					if(sft.getScftId().equals(sft2.getScftParentId())) {
						result.append("['").append(sft2.getScftId()).append("','├─").append(sft2.getScftName()).append("']");
						if(temp <= level1_len)
							result.append(",");
					}
				}
			}
		}
		// 去掉最后一个“,”
		String tempResult = result.toString();
		int l = tempResult.length();
		if(l > 1)
			return tempResult.substring(0, l - 1) + "]";
		return "[]";
	}
}
