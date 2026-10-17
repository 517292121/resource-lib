package cn.com.adtech.riss2.controller.dataapi;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.adtech.riss2.bso.ids.IIdsService;
import cn.com.adtech.riss2.bso.platform.IResourceCommentsService;
import cn.com.adtech.riss2.bso.platform.IResourceSearchService;
import cn.com.adtech.riss2.bso.platform.ITextResourceService;
import cn.com.adtech.riss2.bso.resource.ISourceTypeService;
import cn.com.adtech.riss2.bso.sc.IFavoritesService;
import cn.com.adtech.riss2.bso.text.ITextLibClassfiyService;
import cn.com.adtech.riss2.cache.PlatformCacheFactory;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.controller.platform.ResCommonController;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.json.JsonUtil;
import cn.com.adtech.riss2.domain.IdsInformation;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.resource.ResLiterType;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.text.ResTextResource;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.unionlib.lucene.bean.PageBean;
import cn.com.unionlib.lucene.dao.ILuceneSearchService;

import com.jbetter.framework.session.Pagination;

/**
 * 通用接口控制层
 * @author MaChao
 * @date 2019-11-1
 */
@Controller
public class InterfaceController {
	//资源库
	@Resource(name="resourceSearchService")
	private IResourceSearchService resourceSearchService;
	@Resource(name="sourceTypeServices")
	private ISourceTypeService sourceTypeService;
	//信息中心
	@Resource(name="idsServices")
	private IIdsService idsServices;
	//文本库
	@Resource(name="textLibClassfiyService")
	private ITextLibClassfiyService textLibClassiService;
	@Resource(name="textResService")
	private ITextResourceService textResourceService;
	//资源类型
	@Resource(name="resourceCommentsService")
	protected IResourceCommentsService resCommentsService;
	//资源收藏服务对象
	@Resource(name="favoritesService")
	protected IFavoritesService favoritesService;
	//资源检索服务层
	@Resource(name="luceneSearchService")
	private ILuceneSearchService luceneSearchService;
	//获取特色库列表
	@RequestMapping(value="/interface/getRmst/list")
	public void getRmstList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		String json = "";
		try {
			List<Object[]> dataList = PlatformCacheFactory.instance().getMetastoreList();
			String keyArr[]={"rmstId","rmstName","rmstRemark","rmstLogo"};
			
			json = JsonUtil.pageListToJson(null,dataList,keyArr,null);
		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
		} finally {
			out.print(json);
			out.flush();
			out.close();
		}
	}
	//获取特色库的自定义分类
	@RequestMapping(value="/interface/rdty/list")
	public void getRmstType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		String json = "";
		PrintWriter out = response.getWriter();
		//文档库id
		String rmstId=ServletRequestUtils.getStringParameter(request, "rmstId", "");
		Long numId = null;
		try {
			numId = Long.parseLong(rmstId);
		} catch (Exception e) {
			numId = CommonUtil.strToId(rmstId);
		}
		try {
			if(numId==null){
				throw new ErrorMessageException("特色库id不能为空！");
			}
			//获取该资源库的自定义分类
			List<Object[]> dataList = this.resourceSearchService.getRdtyList(numId);
			String keyArr[]={"rdtyId","rdtyName","rdtyGrade","rdtyPid","rdtyZwName","rdtyEnName","rdtyType"};
			json = JsonUtil.pageListToJson(null,dataList,keyArr,null);
		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
		} finally {
			out.print(json);
			out.flush();
			out.close();
		}
	}
	//获取特色库资源列表
	@RequestMapping(value="/interface/res/list")
	public void getResList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		String json = "";
		//获取参数
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);		//特色库ID
		Long rltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0L);		//资源类型ID
		Long rstyId = ServletRequestUtils.getLongParameter(request, "rstyId",0L);       //学科分类ID
		String tchar = ServletRequestUtils.getStringParameter(request, "tchar", "");
		Long rdtyAId = ServletRequestUtils.getLongParameter(request, "rdtyAId", 0L);
		Long rdtyBId = ServletRequestUtils.getLongParameter(request, "rdtyBId", 0L);
		Long rdtyCId = ServletRequestUtils.getLongParameter(request, "rdtyCId", 0L);
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "");
		int isCmd = ServletRequestUtils.getIntParameter(request, "isCmd",0); //是否获取推荐资源   1是 0不是 即所有
		int size = ServletRequestUtils.getIntParameter(request, "size", Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize())));//每页显示条数
		
		//分页设置
		int currentPage = ServletRequestUtils.getIntParameter(request, "pn", 0);
		Pagination pagination = new Pagination();
		pagination.setPageResults(size);
		pagination.setCurrentPage(currentPage);
		
		if(rmstId > 0L){
			map.put("rmstId", rmstId);
		}
		if(isCmd > 0){
			map.put("isCmd", isCmd);
		}
		if(rstyId > 0L){
			map.put("rstyId", rstyId);
		}
		if(rltyId > 0L){
			map.put("rltyId", rltyId);
		}
		if(CommonUtil.isNotNull(tchar)){
			map.put("tchar", tchar);
		}
		if(rdtyAId > 0L){
			map.put("rdtyAId", rdtyAId);
		}
		if(rdtyBId > 0L){
			map.put("rdtyBId", rdtyBId);
		}
		if(rdtyCId > 0L){
			map.put("rdtyCId", rdtyCId);
		}
		if(CommonUtil.isNotNull(sort)){
			map.put("sort", sort);
		}
		
		JsonConfig ymdConfig = JsonUtil.getYmdConfig();
		try {
			List<Object[]> dataList = this.resourceSearchService.getResourceList(pagination, map);
			ResCommonController.parseCoverList(request, dataList, 7, 11, 10);
			for(Object[] data:dataList){
				if(data[11]!=null&&data[11].equals(0)){
					data[10]=SysConfigInfo.FILE_URL_PATH +data[10];
				}
			}
			String keyArr[]={"rsdaId","rsdaTitle","rsdaCreator","rsdaPublisher","rsdaDate","rsdaDescription","rsdaKeyword","rsdaBigimgUrl","rsdaBuilddate","rsdaLooknum","rsdaFileUrl","rsdaSourceType"};
			json = JsonUtil.pageListToJson(pagination,dataList,keyArr,ymdConfig);
		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
		} finally {
			out.print(json);
			out.flush();
			out.close();
		}
	}
	//获取资源详情
	@RequestMapping(value="/interface/res/detail")
	public void getResDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		
		Long rsdaId=ServletRequestUtils.getLongParameter(request, "rsdaId", 0L);
		JsonConfig ymdConfig = JsonUtil.getYmdConfig();
		try {
			if(rsdaId<=0L){
				throw new ErrorMessageException("资源id不能为空！");
			}
			
			ResSourceData rsda=(ResSourceData) this.resourceSearchService.get(ResSourceData.class, rsdaId);
			if(rsda==null){
				throw new ErrorMessageException("该资源不存在！");
			}
			rsda=this.resourceSearchService.parseResCover(request,rsda);
			Object[] data=new Object[11];
			data[0]=rsda.getRsdaId();
			data[1]=rsda.getResMetaStore().getRmstName();
			data[2]=rsda.getResLiterType().getRltyName();
			data[3]=rsda.getRsdaTitle();
			data[4]=rsda.getRsdaBigimgUrl();
			if(CommonUtil.isNotNull(rsda.getRsdaFileUrl())){
				if(rsda.getRsdaSourceType()==0){
					data[5]=SysConfigInfo.FILE_URL_PATH +rsda.getRsdaFileUrl();
				}else{
					data[5]=rsda.getRsdaFileUrl();
				}
			}else{
				data[5]="";
			}
			data[6]=rsda.getRsdaPublisher();
			data[7]=rsda.getRsdaBuilddate();
			data[8]=rsda.getRsdaLooknum();
			data[9]=rsda.getRsdaKeyword();
			data[10]=rsda.getRsdaSourceType();
			Map<String,Object> map2=new HashMap<String, Object>();
			String keyArr[]={"rsdaId","rmstName","rltyName","rsdaTitle","rsdaBigimgUrl","rsdaFileUrl","rsdaPublisher","rsdaBuilddate","rsdaLooknum","rsdaKeyword","rsdaSourceType"};
			for(int i=0;i<keyArr.length;i++){
				map2.put(keyArr[i], data[i]);
			}
			//更新访问量
			Long views = rsda.getRsdaLooknum();
			views = views == null ? 1 : views+1;
			String hql = "update ResSourceData rsda set rsda.rsdaLooknum=" + views + " where rsda.rsdaId=" + rsdaId;
			this.resourceSearchService.getServiceHibernateTemplate().bulkUpdate(hql);			
			
			map.put("success", true);
			map.put("data", map2);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误：" + e.getMessage());
		} finally {
			String json = JSONObject.fromObject(map,ymdConfig).toString();
			out.print(json);
			out.flush();
			out.close();
		}
	}
	//获取资源元数据信息
	@RequestMapping(value="/interface/res/redaInfo")
	public void getResRedaInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		String json="";
		PrintWriter out = response.getWriter();
		
		Long rsdaId=ServletRequestUtils.getLongParameter(request, "rsdaId", 0L);
		try {
			if(rsdaId<=0L){
				throw new ErrorMessageException("资源id不能为空！");
			}

			List<Object[]> dataList=this.resourceSearchService.getMetaData(rsdaId);
			String[] keyArr={"repoName","repoTitle","redaData"};
			
			json = JsonUtil.pageListToJson(null,dataList,keyArr,null);
		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
		} finally {
			out.print(json);
			out.flush();
			out.close();
		}
	}
	//获取所有资源类型
	@RequestMapping(value="/interface/resType/list")
	public void getResType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		String json="";
		PrintWriter out = response.getWriter();
		
		Short type=(short) ServletRequestUtils.getIntParameter(request, "type", 0);
		try {
			List<ResLiterType> dataList=sourceTypeService.getAllTypesByRltyType(type);
			List<Object[]> dataLists=new ArrayList<Object[]>();
			for(int i =0;i<dataList.size();i++){
				Object[] obj=new Object[4];
				obj[0]=dataList.get(i).getRltyId();
				obj[1]=dataList.get(i).getRltyName();
				obj[2]=dataList.get(i).getRltyCode();
				obj[3]=dataList.get(i).getRltyType();
				dataLists.add(obj);
			}
			String[] keyArr={"rltyId","rltyName","rltyCode","rltyType"};
			
			json = JsonUtil.pageListToJson(null,dataLists,keyArr,null);
		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
		} finally {
			out.print(json);
			out.flush();
			out.close();
		}
	}
	//获取指定特色库的资源类型
	@RequestMapping(value="/interface/rmst/resType")
	public void getResTypeByRmstId(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		
		String rmstId=ServletRequestUtils.getStringParameter(request, "rmstId", "");
		Long numId = null;
		try {
			numId = Long.parseLong(rmstId);
		} catch (Exception e) {
			numId = CommonUtil.strToId(rmstId);
		}
		String json="";
		try {
			if(numId==null){
				throw new ErrorMessageException("特色库id不能为空！");
			}
			List<Object[]> dataList=this.resourceSearchService.getResType(numId);
			String[] keyArr={"rltyId","rltyName"};
			
			json = JsonUtil.pageListToJson(null,dataList,keyArr,null);
		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
		} finally {
			out.print(json);
			out.flush();
			out.close();
		}
	}
	//获取文档库列表
	@RequestMapping(value="/interface/text/list")
	public void getTextList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		String json="";
		try {
			List<Object[]> dataList = PlatformCacheFactory.instance().getTextstoreList();
			String[] keyArr={"rtliId","rtliName"};
			
			json = JsonUtil.pageListToJson(null,dataList,keyArr,null);
		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
		} finally {
			out.print(json);
			out.flush();
			out.close();
		}
	}
	//获取指定文档库的分类列表
	@RequestMapping(value="/interface/textType/list")
	public void getTextTypeList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		//文档库id
		String rtliId=ServletRequestUtils.getStringParameter(request, "rtliId", "");
		Long numId = null;
		try {
			numId = Long.parseLong(rtliId);
		} catch (Exception e) {
			numId = CommonUtil.strToId(rtliId);
		}
		String json="";
		try {
			if(numId==null){
				throw new ErrorMessageException("文档库id不能为空！");
			}
			//获取该资源库的自定义分类
			List<Object[]> dataList = this.textLibClassiService.getLibClassfiyLists(numId);
			String[] keyArr={"rlclId","rlclName"};
			
			json = JsonUtil.pageListToJson(null,dataList,keyArr,null);
		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
		} finally {
			out.print(json);
			out.flush();
			out.close();
		}
	}
	//获取文档资源列表
	@RequestMapping(value="/interface/text/reslist")
	public void getTextResList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		String json="";
		JsonConfig ymdConfig = JsonUtil.getYmdConfig();
		//获取参数
		//分类ID
		Long rlclId = ServletRequestUtils.getLongParameter(request, "rlclId", 0L);
		Long rtliId = ServletRequestUtils.getLongParameter(request, "rtliId", 0L);//文档库id
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "");
		int isCmd = ServletRequestUtils.getIntParameter(request, "isCmd",0); //是否获取推荐资源   1是 0不是 即所有
		if(!CommonUtil.isNotNull(sort)){
			sort = "rtreDate";
		}
		
		//分页设置
		int pagesize = ServletRequestUtils.getIntParameter(request, "size", 0);
		pagesize = pagesize < 1 ? Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize())) : pagesize;
		int currentPage = ServletRequestUtils.getIntParameter(request, "pn", 0);
		Pagination pagination = new Pagination();
		pagination.setPageResults(pagesize);
		pagination.setCurrentPage(currentPage);
		
		
		try {
			List<Object[]> dataList = this.textResourceService.getResourceList(pagination, rtliId, rlclId, sort,isCmd);
			for(Object[] obj:dataList){
				if(obj[2]!=null&&CommonUtil.isNotNull(obj[2].toString())){
					obj[2]=SysConfigInfo.FILE_URL_PATH+obj[2];
				}else{
					obj[2]="";
				}
			}
			String[] keyArr={"rtreId","rtreTitle","rtreCover","rtreAccess","rtreDate","rtreCol1"};
			
			json = JsonUtil.pageListToJson(pagination,dataList,keyArr,ymdConfig);
		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
		} finally {
			out.print(json);
			out.flush();
			out.close();
		}
	}
	//获取文档资源详情
	@RequestMapping(value="/interface/text/resDetail")
	public void getTextDetail(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		
		Long rtreId=ServletRequestUtils.getLongParameter(request, "rtreId", 0L);
		JsonConfig ymdConfig = JsonUtil.getYmdConfig();
		try {
			if(rtreId<=0L){
				throw new ErrorMessageException("资源id不能为空！");
			}
			
			ResTextResource rtre=(ResTextResource) this.resourceSearchService.get(ResTextResource.class, rtreId);
			if(rtre==null){
				throw new ErrorMessageException("未找到该资源！");
			}
			if(CommonUtil.isNotNull(rtre.getRtreCover())){
				rtre.setRtreCover(SysConfigInfo.FILE_URL_PATH+rtre.getRtreCover());
			}else{
				rtre.setRtreCover("");
			}
			Map<String,Object> map2=new HashMap<String, Object>();
			map2.put("rtreId", rtre.getRtreId());
			//所属文本库名称
			map2.put("rtliName", rtre.getResTextLibrary().getRtliName());
			if(rtre.getResLibraryClassify()!=null){
				//所属文本库分类名称
				map2.put("rlclName", rtre.getResLibraryClassify().getRlclName());
			}
			map2.put("rtreCover", rtre.getRtreCover());
			map2.put("rtreDate", rtre.getRtreDate());
			map2.put("rtreTitle", rtre.getRtreTitle());
			map2.put("rtreAccess", rtre.getRtreAccess());
			if(CommonUtil.isNotNull(rtre.getRtreCol1())){
				map2.put("rtreCol1", rtre.getRtreCol1());
			}
			if(CommonUtil.isNotNull(rtre.getRtreCol2())){
				map2.put("rtreCol2", rtre.getRtreCol2());
			}
			if(CommonUtil.isNotNull(rtre.getRtreCol3())){
				map2.put("rtreCol3", rtre.getRtreCol3());
			}
			if(CommonUtil.isNotNull(rtre.getRtreCol4())){
				map2.put("rtreCol4", rtre.getRtreCol4());
			}
			if(CommonUtil.isNotNull(rtre.getRtreCol5())){
				map2.put("rtreCol5", rtre.getRtreCol5());
			}
			if(CommonUtil.isNotNull(rtre.getRtreCol6())){
				map2.put("rtreCol6", rtre.getRtreCol6());
			}
			if(CommonUtil.isNotNull(rtre.getRtreCol7())){
				map2.put("rtreCol7", rtre.getRtreCol7());
			}
			if(CommonUtil.isNotNull(rtre.getRtreCol8())){
				map2.put("rtreCol8", rtre.getRtreCol8());
			}
			if(CommonUtil.isNotNull(rtre.getRtreCol9())){
				map2.put("rtreCol9", rtre.getRtreCol9());
			}
			if(CommonUtil.isNotNull(rtre.getRtreCol10())){
				map2.put("rtreCol10", rtre.getRtreCol10());
			}
			if(CommonUtil.isNotNull(rtre.getRtreCol11())){
				map2.put("rtreCol11", rtre.getRtreCol11());
			}
			if(CommonUtil.isNotNull(rtre.getRtreCol12())){
				map2.put("rtreCol12", rtre.getRtreCol12());
			}
			if(CommonUtil.isNotNull(rtre.getRtreCol13())){
				map2.put("rtreCol13", rtre.getRtreCol13());
			}
			if(CommonUtil.isNotNull(rtre.getRtreCol14())){
				map2.put("rtreCol14", rtre.getRtreCol14());
			}
			if(CommonUtil.isNotNull(rtre.getRtreCol15())){
				map2.put("rtreCol15", rtre.getRtreCol15());
			}
			
			long viewNum = rtre.getRtreAccess() == null ? 1 : rtre.getRtreAccess() + 1;
			String hql = "update ResTextResource r set r.rtreAccess="+viewNum+" where r.rtreId=" + rtreId;
			this.textResourceService.getServiceHibernateTemplate().bulkUpdate(hql);
			
			map.put("success", true);
			map.put("data", map2);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误：" + e.getMessage());
		} finally {
			String json = JSONObject.fromObject(map,ymdConfig).toString();
			out.print(json);
			out.flush();
			out.close();
		}
	}
	//获取信息分类
	@RequestMapping(value="/interface/infoType/list")
	public void getInfoType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		String json="";
		try {
			List<Object[]> dataList = PlatformCacheFactory.instance().getInfoClassList();
			String[] keyArr={"idstId","idstName","idstNumber"};
			
			json = JsonUtil.pageListToJson(null,dataList,keyArr,null);
		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
		} finally {
			out.print(json);
			out.flush();
			out.close();
		}
	}
	//获取信息列表
	@RequestMapping(value="/interface/info/list")
	public void infoList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		String json="";
		
		String cid = ServletRequestUtils.getStringParameter(request, "cid", "");
		Long classId = null;
		try {
			classId = Long.parseLong(cid);
		} catch (Exception e) {
			classId = CommonUtil.strToId(cid);
		}
		classId = classId == null ? 0L : classId;
		int size = ServletRequestUtils.getIntParameter(request, "size", Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize())));//每页显示条数
		//分页设置
		int currentPage = ServletRequestUtils.getIntParameter(request, "pn", 0);
		Pagination pagination = new Pagination();
		pagination.setPageResults(size);
		pagination.setCurrentPage(currentPage);
		
		JsonConfig jcon=JsonUtil.getYmdConfig();
		try {
			List<Object[]> dataList = this.idsServices.getInfoList(pagination, classId);
			for(Object[] obj:dataList){
				if(obj[4]!=null&&CommonUtil.isNotNull(obj[4].toString())){
					obj[4]=SysConfigInfo.FILE_URL_PATH+obj[4];
				}else{
					obj[4]="";
				}
			}
			String[] keyArr={"idsiId","idsiTitle","idsiDescription","idsiDate","idsiImgUrl","idsiHitCount"};
			
			json = JsonUtil.pageListToJson(pagination,dataList,keyArr,jcon);
		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
		} finally {
			out.print(json);
			out.flush();
			out.close();
		}
	}
	//获取信息详情
	@RequestMapping(value="/interface/info/detail")
	public void infoDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		Map<String,Object> map=new HashMap<String, Object>();
		
		String idsiId = ServletRequestUtils.getStringParameter(request, "idsiId", "");
		Long infoId = null;
		try {
			infoId = Long.parseLong(idsiId);
		} catch (Exception e) {
			infoId = CommonUtil.strToId(idsiId);
		}
		infoId = infoId == null ? 0L : infoId;
		
		JsonConfig jcon=JsonUtil.getYmdConfig();
		try {
			if(infoId<=0L){
				throw new ErrorMessageException("信息id不能为空！");
			}
			IdsInformation idsi=(IdsInformation) this.idsServices.get(IdsInformation.class, infoId);
			if(idsi==null){
				throw new ErrorMessageException("未找到该信息！");
			}
			if(CommonUtil.isNotNull(idsi.getIdsiImgUrl())){
				idsi.setIdsiImgUrl(SysConfigInfo.FILE_URL_PATH+idsi.getIdsiImgUrl());
			}else{
				idsi.setIdsiImgUrl("");
			}
			Map<String,Object> map2=new HashMap<String, Object>();
			map2.put("idsiId", idsi.getIdsiId());
			map2.put("idsiContent", idsi.getIdsiContent());
			map2.put("idsiDate", idsi.getIdsiDate());
			map2.put("idsiDescription", idsi.getIdsiDescription());
			map2.put("idsiHitCount", idsi.getIdsiHitCount());
			map2.put("idsiImgUrl", idsi.getIdsiImgUrl());
			map2.put("idsiTitle", idsi.getIdsiTitle());
			if(idsi.getIdsInformationType()!=null){
				map2.put("idstName", idsi.getIdsInformationType().getIdstName());
			}
			
			//更新点击量
			long viewNum = idsi.getIdsiHitCount() == null ? 1 : idsi.getIdsiHitCount() + 1;
			String hql = "update IdsInformation info set info.idsiHitCount="+viewNum+" where info.idsiId=" + infoId;
			this.idsServices.getServiceHibernateTemplate().bulkUpdate(hql);
			
			map.put("success", true);
			map.put("data", map2);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误：" + e.getMessage());
		} finally {
			String json = JSONObject.fromObject(map,jcon).toString();
			out.print(json);
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 获取资源同一主题下其它资源
	 * @param rsdaId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/interface/res/related/{rssuId}")
	public void getResRelated(@PathVariable Long rssuId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		Long rsdaId = ServletRequestUtils.getLongParameter(request, "rsdaId", 0L);
		String json = "";
		
		PrintWriter out = response.getWriter();
		try {
			List<Object[]> dataList = this.resourceSearchService.getResRelated(rssuId, rsdaId);
			
			if(dataList != null && dataList.size() > 0){
				ResCommonController.parseCoverList(request, dataList, 2, 3, 4);
			}
			for(Object[] data:dataList){
				if(data[3]!=null&&data[3].equals(0)){
					data[4]=SysConfigInfo.FILE_URL_PATH +data[4];
				}
			}
			
			String[] keyArr={"rsdaId","rsdaTitle","rsdaBigimgUrl","rsdaSourceType","rsdaFileUrl"};
			
			json = JsonUtil.pageListToJson(null,dataList,keyArr,null);
		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
		}finally{
			out.print(json);
			out.flush();
			out.close();
		}
	}
	/**
	 * 获取相关资源（按相同资源库与资源类型获取）
	 * @param rsdaId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/interface/res/similar")
	public void getSimilarRes(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		Long rsdaId = ServletRequestUtils.getLongParameter(request, "rsdaId", 0L);
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);
		Long rltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0L);
		
		String json = "";
		PrintWriter out = response.getWriter();
		try {
			List<Object[]> dataList = this.resourceSearchService.getSimilarRes(rsdaId, rmstId, rltyId);
			
			if(dataList != null && dataList.size() > 0){
				ResCommonController.parseCoverList(request, dataList, 3, 4, 5);
			}
			for(Object[] data:dataList){
				if(data[4]!=null&&data[4].equals(0)){
					data[5]=SysConfigInfo.FILE_URL_PATH +data[5];
				}
			}
			String[] keyArr={"rsdaId","rsdaTitle","rsdaCreator","rsdaBigimgUrl","rsdaSourceType","rsdaFileUrl"};
			
			json = JsonUtil.pageListToJson(null,dataList,keyArr,null);
		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
		}finally{
			out.print(json);
			out.flush();
			out.close();
		}
	}
	/**
	 * 获取资源附件
	 * @param rsdaId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/interface/res/attfile/{rsdaId}")
	public void getAttachFile(@PathVariable Long rsdaId, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		String json = "";
		try {
			List<Object[]> dataList = this.resourceSearchService.getAttachFile(rsdaId);
			
			for(Object[] data:dataList){
				if(data[2]!=null&&CommonUtil.isNotNull(data[2].toString())){
					data[2]=SysConfigInfo.FILE_URL_PATH +data[2];
				}
				if(data[3]!=null&&CommonUtil.isNotNull(data[3].toString())){
					data[3]=SysConfigInfo.FILE_URL_PATH +data[3];
				}
			}
			
			String[] keyArr={"rsfiId","resFileName","resFilePath","resViewPath"};
			
			json = JsonUtil.pageListToJson(null,dataList,keyArr,null);

		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
		}finally{
			out.print(json);
			out.flush();
			out.close();
		}
	}
	/**
	 * 作者文本库通过作者名查找作品
	 * @author MaChao
	 * @date 2019-11-14
	 * @param rsdaId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/interface/text/works/{autor}")
	public void getWorks(@PathVariable String autor, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		String json = "";
		try {
			List<Object[]> dataList = this.resourceSearchService.getWorksByAutor(autor);
			
			String[] keyArr={"rsdaId","rsdaTitle"};
			
			json = JsonUtil.pageListToJson(null,dataList,keyArr,null);

		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
		}finally{
			out.print(json);
			out.flush();
			out.close();
		}
	}
	/*******************************************************用户相关接口***************************************************************/
	
	/**
	 * 获取资源评论
	 * @param rsdaId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/interface/res/comments/{rsdaId}")
	public void getResComments(@PathVariable Long rsdaId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		int size = ServletRequestUtils.getIntParameter(request, "size", Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize())));//每页显示条数
		int page = ServletRequestUtils.getIntParameter(request, "pn", 0);
		Pagination p = new Pagination();
		p.setCurrentPage(page);
		p.setPageResults(size);
		
		JsonConfig jcon=JsonUtil.getYmdConfig();
		PrintWriter out = response.getWriter();
		String json = "";
		try {
			Long userId = MyRequestUtil.getMemberId(request);
			List<Object[]> dataList = this.resCommentsService.getCommentsList(rsdaId, userId, p);
			String[] keyArr={"suinLoginName","suinName","suinImageUrl","rscoContent","rscoStatus","rscoDate"};
			
			json = JsonUtil.pageListToJson(p,dataList,keyArr,jcon);

		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
		}finally{
			out.print(json);
			out.flush();
			out.close();
		}
	}
	
	//获取用户基本信息
	@RequestMapping(value="/interface/user/info")
	public void userInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		Map<String,Object> map=new HashMap<String, Object>();
		
		JsonConfig jcon=JsonUtil.getYmdConfig();
		try {
			SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
			if(userinfo==null){
				throw new ErrorMessageException("未找到用户信息！");
			}
			Map<String,Object> map2=new HashMap<String, Object>();
			map2.put("suinId", userinfo.getSuinId());
			map2.put("suinBirthday", userinfo.getSuinBirthday());
			map2.put("suinEmail", userinfo.getSuinEmail());
			map2.put("suinIntegral", userinfo.getSuinIntegral());
			map2.put("suinImageUrl", userinfo.getSuinImageUrl());
			map2.put("suinLoginName", userinfo.getSuinLoginName());
			map2.put("suinName", userinfo.getSuinName());
			map2.put("suinSex", userinfo.getSuinSex());
			map2.put("suinJoinDate", userinfo.getSuinJoinDate());
			map2.put("suinMobileTelephone", userinfo.getSuinMobileTelephone());
			map2.put("suinLastvisitDate", userinfo.getSuinLastvisitDate());
			map2.put("suinDownNum", userinfo.getSuinDownNum());
			
			map.put("success", true);
			map.put("data", map2);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误：" + e.getMessage());
		} finally {
			String json = JSONObject.fromObject(map,jcon).toString();
			out.print(json);
			out.flush();
			out.close();
		}
	}
	
	//退出登录
	@RequestMapping(value="/interface/member/exit")
	public void loginOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		try {
			request.getSession().setAttribute("sysUserinfo", null);
			map.put("success", "true");
			String json =  JSONObject.fromObject(map).toString();
			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", "false");
			map.put("msg", e.getMessage());
			String json =  JSONObject.fromObject(map).toString();
			out.print(json);
		}
		out.flush();
		out.close();
	}
	/**
	 * 加入收藏
	 * @param rsdaId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/interface/res/favorite")
	public void saveFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		try {
			Long rsdaId = ServletRequestUtils.getLongParameter(request, "rsdaId", 0L);
			if(rsdaId < 1L){
				throw new ErrorMessageException("参数错误，收藏失败！");
			}
			SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
			if(user == null) {
				throw new ErrorMessageException("您没有登录或登录已过期，请先登录!!!");
			}
			this.favoritesService.addFavorite(user.getSuinId(), rsdaId);
			map.put("success", "true");
		} catch (ErrorMessageException e) {
			e.printStackTrace();
			map.put("success", "false");
			map.put("errmsg", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", "false");
			map.put("errmsg", "操作失败：" + e.getMessage());
		}
		String json = JSONObject.fromObject(map).toString();
		out.print(json);
		out.flush();
		out.close();
	}
	
}
