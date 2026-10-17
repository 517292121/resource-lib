package cn.com.adtech.riss2.controller.console.resource.manager;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bean.ResourceRepeatBean;
import cn.com.adtech.riss2.bso.resource.ISystemCheckRepeatService;
import cn.com.adtech.riss2.core.CommonUtil;

/**
 * 系统级资源查重控制器
 * 
 * @author MaChao
 * 
 * @date 2011-08-25
 *
 */
public class SystemCheckRepeatController extends CheckRepeatBasicContoller{

	ISystemCheckRepeatService systemCheckRepeatService = null;
	public void setSystemCheckRepeatService(
			ISystemCheckRepeatService systemCheckRepeatService) {
		this.systemCheckRepeatService = systemCheckRepeatService;
	}
	
	/**
	 * 进入资源查重页面
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/manager/systemCheckRepeat");
	}
	
	/**
	 * 启动资源查重入口
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView repeatStart(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String params = ServletRequestUtils.getStringParameter(request, "cctj","");
		List<String> columnList = getColumnList(params);
		ResourceRepeatBean repeatBean = new ResourceRepeatBean();
		repeatBean = parseRepeatBean(request, repeatBean);
		
		request.setAttribute("repeatBean", repeatBean);
		systemCheckRepeatService.saveResourceRepeat(columnList);
		return result(request, response);
	}
	
	public ModelAndView result(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/manager/checkRepeatResult");
	}
	
	private List<String> getColumnList(String ids){
		String[] idArr = ids.split(",");
		if(idArr == null || idArr.length < 1){
			return null;
		}
		int id = 0;
		String column;
		List<String> columnList = new LinkedList<String>();
		for(int i=0; i<idArr.length; i++){
			id = Integer.parseInt(idArr[i]);
			column = parseColumn(id);
			columnList.add(column);
		}
		return columnList;
	}
	
	
	
	public void getRepeatResultJson(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
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
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rsyrRepeatnum");
		
		String pre = sort.substring(0,4);
		if(pre.equals("rsda")){
			sort = "sourceData." + sort;
		}
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		
		List list = this.systemCheckRepeatService.getRepeatResultList(pagination, sort, dir);
		
		String[] keys = { "rsyrId", "rsyrRsdaId", "rsyrRepeatnum", "rsdaTitle",
				"rsdaSubject", "rsdaDescription", "rsdaSource", "rsdaLanguage",
				"rsdaRelation", "rsdaCoverage", "rsdaCreator", "rsdaPublisher",
				"rsdaContributor", "rsdaRights", "rsdaDate", "rsdaType",
				"rsdaFormat", "rsdaIdentifier", "rsdaFileType", "rsdaFileSize" };
		
		String jsondata = CommonUtil.createJsonTable(list, keys, pagination.getTotalResults() + "");
		
		out.write(jsondata);
		out.flush();
	}
	
	/**
	 * 去除资源的重复记录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void deleteRepeat(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		String rsdaId = ServletRequestUtils.getStringParameter(request, "rsdaIds","");
		
		//如果不止一个查询条件，即全部重复的资源
		ResourceRepeatBean repeatBean = new ResourceRepeatBean();
		repeatBean = parseRepeatBean(request, repeatBean);
		
		String[] idArr = rsdaId.split(",");
		Long id ;
		boolean b = true;
		for(int i=0; i<idArr.length; i++){
			id = Long.parseLong(idArr[i]);
			if(this.systemCheckRepeatService.deleteRepeatByRsdaId(id, repeatBean) == false){
				b = false;
			}
		}
		
		String jsondata = "";
		if(b) {
			jsondata = "1";
		}else {
			jsondata = "0";
		}
		
		out.print(jsondata);
		out.flush();
	}
	
	/**
	 * 一键清空所有重复记录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void clearRepeat(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		List<Long> list = this.systemCheckRepeatService.getAllRepeatRsdaId();
		String jsondata = "";
		if(list == null || list.size() < 1){
			jsondata = "0";
			out.print(jsondata);
			out.flush();
			return;
		}
		
		//如果不止一个查询条件，即全部重复的资源
		ResourceRepeatBean repeatBean = new ResourceRepeatBean();
		repeatBean = parseRepeatBean(request, repeatBean);
		
		counter = list.size();
		Long id;
		boolean b = true;
		for(int i=0,s=list.size(); i<s; i++){
			id = list.get(i);
			if(id > 0L){
				if(this.systemCheckRepeatService.deleteRepeatByRsdaId(id, repeatBean) == false){
					b = false;
				}
			}
			
			progress = (i+1);
		}
		
		if(b) {
			jsondata = "1";
		}else {
			jsondata = "0";
		}
		
		out.print(jsondata);
		out.flush();
	}
	
	/**
	 * 资源总数
	 */
	private float counter = 0;
	
	/**
	 * 已完成数量
	 */
	private float progress = 0;
	
	/**
	 * 获取一键清空重复记录的清空百分比
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getDeleteProgress(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		float p = progress / counter;
		p = p * 100;
		int result = Math.round(p);
		
		out.print(result * 2);
		out.flush();
	}
}
