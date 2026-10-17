package cn.com.adtech.riss2.controller.console.resource.build;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import cn.com.adtech.riss2.bean.ResourceRepeatBean;
import cn.com.adtech.riss2.bso.resource.IMissionInfoManagementService;
import cn.com.adtech.riss2.bso.resource.IResourceDataService;
import cn.com.adtech.riss2.bso.resource.IResourceRepeatService;
import cn.com.adtech.riss2.bso.resource.ISourceLanguageService;
import cn.com.adtech.riss2.bso.resource.ResourceRepeatServiceImp;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.controller.console.resource.manager.CheckRepeatBasicContoller;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResLanguageType;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceRepeat;

import com.jbetter.framework.session.Pagination;

/**
 * 资源查重控制器
 * 
 * @author MaChao
 * 
 * @date 2011-08-03
 *
 */
public class ResourceCheckRepeatController extends CheckRepeatBasicContoller{
	
	private IMissionInfoManagementService missionService = null;
	public void setMissionService(IMissionInfoManagementService missionService) {
		this.missionService = missionService;
	}
	
	private IResourceRepeatService repeatService = null;
	public void setRepeatService(IResourceRepeatService repeatService) {
		this.repeatService = repeatService;
	}
	
	private IResourceDataService resourceService = null;
	public void setResourceService(IResourceDataService resourceService) {
		this.resourceService = resourceService;
	}
	
	private ISourceLanguageService sldao = null;
	public void setSldao(ISourceLanguageService sldao) {
		this.sldao = sldao;
	}

	/**
	 * 初始化作业资源查重列表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public ModelAndView initMissionResource(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0L);
		request.setAttribute("rminId", rminId);
		
		return new ModelAndView("resource/build/checkRepeat");
	}
	
	/**
	 * 根据作业查重作业下所有资源
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView checkRepeatByMission(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//作业ID
		Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0L);
		
		if(rminId < 1){
			request.setAttribute("result", "<script>Msg.fail('错误，该查重作业不存在！')</script>");
			return initMissionResource(request, response);
		}
		List<ResSourceData> sourceList = missionService.getResSourceDataByMission(rminId);
		if(sourceList == null || sourceList.size() < 1){
			request.setAttribute("result", "<script>Msg.fail('该作业中不存在任何资源！')</script>");
			return initMissionResource(request, response);
		}
		ResourceRepeatBean repeatBean = new ResourceRepeatBean();
		repeatBean.setRepeatResourceList(sourceList);
		repeatBean.setRminId(rminId);
		
		//资源ID数组，主要是为删除之前的资源用.这里是根据作业查重，所以直接通过作业ID删除即可
		repeatBean.setRsdaIdArr(null);
		repeatBean = parseRepeatBean(request, repeatBean);
		
		try {
			repeatService.saveResourceRepeat(repeatBean);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("result", "<script>Msg.fail('查重失败，错误：\""+e.getMessage()+"\"')</script>");
			ResourceRepeatServiceImp.REPEAT_STATE = false; //出现异常后也要关闭查重开关
		}
		
		request.setAttribute("rminId", rminId);
		request.setAttribute("reqType", 1);
		request.setAttribute("repeatBean", repeatBean);
		return new ModelAndView("resource/build/checkRepeatResult");
	}
	
	/**
	 * 根据指定的资源查重
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView checkRepeatByResource(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//查重请求类型，主要是为以后资源维护中及其它页面也要查重后结果页返回作保留. 1:作业中的资源进行查重
		int reqType = ServletRequestUtils.getIntParameter(request, "reqType", 1);
		String rsdaIds = ServletRequestUtils.getStringParameter(request, "rsdaId","");
		//作业ID
		Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0L);
		request.setAttribute("rminId", rminId);

		request.setAttribute("reqType", reqType);
		
		if(ResourceRepeatServiceImp.REPEAT_STATE){
			//如果是资源维护中的则提示错误
			request.setAttribute("result", "<script>Msg.fail(\"<span style='color:red'>对不起，系统正在查重中！</span>\",4)</script>");
			return initMissionResource(request, response);
		}
		
		//如果没有选择任何资源
		if(rsdaIds.length() < 1){
			//如果是作业中的，则直接查重整个作业
			if(reqType == 1){
				return checkRepeatByMission(request, response);
			}else{
				//如果是资源维护中的则提示错误
				request.setAttribute("result", "<script>Msg.fail('您没有选择任何资源进行查重！')</script>");
				return new ModelAndView("");
			}
		}else{
			Long[] idArr = parseRsdaIdToArrary(rsdaIds);
			List<ResSourceData> sourceList = repeatService.getResourceDataByIdArr(rsdaIds);
			
			ResourceRepeatBean repeatBean = new ResourceRepeatBean();
			repeatBean.setRepeatResourceList(sourceList);
			repeatBean.setRminId(ServletRequestUtils.getLongParameter(request, "rminId", 0L));
			repeatBean.setRsdaIdArr(idArr); //资源ID数组，主要是为删除之前的资源用
			
			repeatBean = parseRepeatBean(request, repeatBean);
			
			try {
				repeatService.saveResourceRepeat(repeatBean);
			} catch (Exception e) {
				request.setAttribute("result", "<script>Msg.fail('查重失败，错误：\""+e.getMessage()+"\"', 5)</script>");
				ResourceRepeatServiceImp.REPEAT_STATE = false; //出现异常后也要关闭查重开关
			}
			request.setAttribute("repeatBean", repeatBean);
			return new ModelAndView("resource/build/checkRepeatResult");
		}
	}
	
	
	
	
	
	
	/**
	 * 获取已查重完成的百分比
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void getCompleted(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int c = this.repeatService.getProgress();
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(c);
		out.flush();
	}
	
	
	public void getRepeatResultJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rsreRepeatAll");
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		
		Long rminId = ServletRequestUtils.getLongParameter(request, "rminId",0L);
		String rsdaIds = ServletRequestUtils.getStringParameter(request, "rsdaIds","");
		String rsdaTitle = ServletRequestUtils.getStringParameter(request, "rsdaTitle","");
		
		boolean showall = ServletRequestUtils.getBooleanParameter(request, "showAll", false);
		
		List<ResSourceRepeat> repeatList = null;
		if(rminId > 0L){ //如果作业不为空，则根据作业查找
			repeatList = repeatService.getRepeatByMission(pagination, rminId, sort, dir,rsdaTitle, showall);
		}else if (CommonUtil.isNotNull(rsdaIds)) {
			repeatList = repeatService.getRepeatByResources(rsdaIds, sort, dir,rsdaTitle, showall);
		}
		
		String[] keys = new String[] { "rsreId", "rsreRminId", "rsreRsdaId",
				"rsreSourceTitle", "rsreTitle", "rsrePublisher",
				"rsreFileSize", "rsreFileType", "rsreSubject",
				"rsreDescription", "rsreSource", "rsreLanguage",
				"rsreRelation", "rsreCoverage", "rsreCreator",
				"rsreContributor", "rsreRights", "rsreDate", "rsreType",
				"rsreFormat", "rsreIdentifier", "rsreRepeatAll" };
	
		String result = CommonUtil.createJsonTable(repeatList, keys, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}
	
	public void getRepeatResourceJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 15); // 每页记录数
		int currentPage = start / limit;
		// 添加分页器
		Pagination pagination = new Pagination();
		// 设置当前页
		pagination.setCurrentPage(currentPage);
		// 设置每页记录数
		pagination.setPageResults(limit);
		
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rsdaId");
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		
		if(sort.equals("rmstName")){
			sort = "resMetaStore.rmstName";
		}
		if(sort.equals("rltyName")){
			sort = "resLiterType.rltyName";
		}
		
		List<Object[]> list = null;
		
		//资源本身ID
		Long rsdaId = ServletRequestUtils.getLongParameter(request, "rsdaId", 0L);
		
		//是否查询包含本身的资源,默认为是(0)
		int isSelf = ServletRequestUtils.getIntParameter(request, "haveSelf", 0); 
		
		//查重条件(1 题名 、 2 出版者、 3 资源大小、 4 资源类型)
		String cctj = ServletRequestUtils.getStringParameter(request, "cctj", "0");
		
		//如果只有一种查询条件，即查询指定属性的重复数
		if(cctj!=null && cctj.indexOf(",") < 1){
			int tj = Integer.parseInt(cctj);
			String eleColumn = parseColumn(tj);
			//查询重复资源
			list = repeatService.getRepeatResourceByElement(pagination, rsdaId, eleColumn, sort, dir);
		}else{
			//如果不止一个查询条件，即全部重复的资源
			ResourceRepeatBean repeatBean = new ResourceRepeatBean();
			repeatBean = parseRepeatBean(request, repeatBean);
			list = repeatService.getRepeatResourceByAll(pagination, repeatBean, rsdaId, sort, dir, isSelf);
		}
		
		String[] keys = new String[] { "rsdaId", "rsdaTitle", "rmstName","rltyName" };
		String result = CommonUtil.createJsonTable(list, keys, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}
	
	/**
	 * 预览资源详细信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView detail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long rsdaId = ServletRequestUtils.getLongParameter(request, "rsdaId", 0L);
		
		//获取资源核心表单
		ResSourceData resource = resourceService.getResourceMetaInfo(rsdaId);
		
		//获取资源的第一语种
		Long languageId = sldao.getSourceFirstLanguage(rsdaId);
		
		ResLanguageType language = (ResLanguageType) repeatService.get(ResLanguageType.class, languageId);
		
		// 获取所有的元素信息
		List<ResElementPool> EleDataList = resourceService.getResourceAllData(
				resource.getRsdaRmstId(), resource.getRsdaRltyId(), languageId,
				rsdaId);
		
		String rssuName = ""; //资源所属主题名称
		if(resource.getRsdaRssuId() != null && resource.getRsdaRssuId() > 0L){
			String hql = "select rssu.rssuName from ResSourceSubject rssu where rssu.rssuId=" + resource.getRsdaRssuId();
			rssuName = repeatService.getSingleData(hql);
		}
		
		//自定义分类1名称
		String rdtyName1 = repeatService.getDynamicName(resource.getRsdaRdtyId());
		//自定义分类2名称
		String rdtyName2 = repeatService.getDynamicName(resource.getRsdaRdtyTwo());
		//自定义分类三名称
		String rdtyName3 = repeatService.getDynamicName(resource.getRsdaRdtyThree());
		
		String fileAddress = SysConfigInfo.FILE_URL_PATH;
		
		//设值
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rdtyName1", rdtyName1);
		map.put("rdtyName2", rdtyName2);
		map.put("rdtyName3", rdtyName3);
		map.put("EleDataList", EleDataList);
		map.put("resource", resource);
		map.put("rssuName", rssuName);
		map.put("language", language.getRltyName());
		map.put("fileAddress", fileAddress);
		return new ModelAndView("resource/build/resourceView", map);
	}
	
	/**
	 * 清空所有查重临时数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void clearRepeat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String jsondata = "";
		if(ResourceRepeatServiceImp.REPEAT_STATE) {
			jsondata = "系统正在查重中，现无法清空临时数据！";
		}else {
			repeatService.deleteAllRepeat();
			jsondata = "1";
		}
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}
	
	/**
	 * 删除重复的资源
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void deleteResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long rminId = ServletRequestUtils.getLongParameter(request, "rminId",0L);
		String rsdaIds = ServletRequestUtils.getStringParameter(request, "rsdaIds","");
		
		Long[] rsdaIdlist = null;
		if(CommonUtil.isNotNull(rsdaIds)){
			rsdaIdlist = parseRsdaIdToArrary(rsdaIds);
		}
		
		boolean b = repeatService.deleteRepeatResource(rminId, rsdaIdlist, resourceService);
		
		String jsondata = b ? "1" : "0";
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}
	
	/**
	 * 结束当前查重
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void endRepeat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		repeatService.setRepeatOpen(false);
		ResourceRepeatServiceImp.REPEAT_STATE = false;
	}
	
	
}













