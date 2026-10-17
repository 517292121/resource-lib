package cn.com.adtech.riss2.controller.console.ids;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.ids.IIdsService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ImgHandleUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.TagsChecker;
import cn.com.adtech.riss2.domain.IdsInformation;
import cn.com.adtech.riss2.domain.IdsInformationType;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.upload.IUploadService;
import cn.com.unionlib.lucene.bean.ResMainBean;
import cn.com.unionlib.lucene.dao.ILuceneDaoService;
import cn.com.unionlib.lucene.dao.LuceneDaoServiceImp;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class IdsInformationController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(IdsInformationController.class);

	private IIdsService idsServices;

	public IIdsService getIdsServices() {
		return idsServices;
	}

	public void setIdsServices(IIdsService idsServices) {
		this.idsServices = idsServices;
	}

	/**本地文件上传处理对象**/
	protected IUploadService localUploadService = null;
	public void setLocalUploadService(IUploadService localUploadService) {
		this.localUploadService = localUploadService;
	}
	
	//索引读写服务
	private ILuceneDaoService luceneDaoService = new LuceneDaoServiceImp();
	
	/**
	 * 初始化信息管理页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-26
	 */
	public ModelAndView initIdsView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BackHasSerach(request);
		return new ModelAndView("ids/idsInformation");
	}
	
	/**
	 * 初始化信息界面
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView saveOrUpdateInitpage(HttpServletRequest request, HttpServletResponse response){
		Long idsiId = ServletRequestUtils.getLongParameter(request, "idsiId", 0);
		Long groupId = ServletRequestUtils.getLongParameter(request, "idstIds", 0L);
		String idsTitleCondition = ServletRequestUtils.getStringParameter(request, "idsTitle_Condition", "").trim();
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); 
		StringBuffer sb = new StringBuffer("&start="+start);
		if(!"".equals(idsTitleCondition)){
			sb.append("&idsTitle_Condition="+idsTitleCondition);
		}
		if(groupId!=0){
			sb.append("&idstId="+groupId);
		}
		request.setAttribute("backurl", sb.toString());

		Long idstId = -1L;
		Map<String, Object> map = new HashMap<String, Object>();
		if(!idsiId.equals(0L)){
			IdsInformation idsinfo = idsServices.getIdsInfomarion(idsiId);
			if(CommonUtil.isNotNull(idsinfo.getIdsiImgUrl())){
				String img = idsinfo.getIdsiImgUrl();
				if(img.contains("downloadPath")){
					String fileService = SysConfigInfo.FILE_URL_PATH;
					img = fileService + img;
					idsinfo.setIdsiImgUrl(img);
				}
			}
			idstId = idsinfo.getIdsiIdstId();
			map.put("idsinfo",idsinfo);
		}
		String selectHtml = this.idsServices.getIdsInformationOptions(idstId);
		request.setAttribute("idsType", selectHtml);
		return new ModelAndView("ids/idsInfo",map);
	}

	/**
	 * 保存信息
	 * @throws Exception 
	 * 
	 */
	public ModelAndView saveOrUpdateIdsInfomatin(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Long groupId = ServletRequestUtils.getLongParameter(request, "idstId", 0L);
		String idsTitleCondition = ServletRequestUtils.getStringParameter(request, "idsTitle_Condition", "").trim();
		request.setAttribute("idsTitle_Condition", idsTitleCondition);
		request.setAttribute("groupId", groupId);
		boolean b = false;
		long idsiId = ServletRequestUtils.getLongParameter(request, "idsiId", 0);
		if(idsiId == 0){
			b = saveIdsI(request, response);
		}else{
			b = updateIdsI(request, response);
		}
		
		if(b){
			request.setAttribute("result", "<script>alert('保存成功')</script>");
			return saveOrUpdateInitpage(request,response);
		}else{
			request.setAttribute("result", "<script>alert('保存失败')</script>");
			return saveOrUpdateInitpage(request,response);
		}
		
	}
	
	
	/**
	 * 获取信息内容 
	 * 
	 */
	public ModelAndView getIdsContent(HttpServletRequest request, HttpServletResponse response)throws IOException{
		
		Long cid = ServletRequestUtils.getLongParameter(request, "cid",0L);
		
		String content = "";
		IdsInformation idsinfo = idsServices.getIdsInfomarion(cid);
		if(idsinfo!=null){
			content = idsinfo.getIdsiContent();
		}
		
		request.setAttribute("content", content);
		
		return new ModelAndView("../../html/content");
	}
	
	
	/**
	 * 构造信息分类下拉框
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-26
	 */
	public void getIdsITCombox(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		List list = this.idsServices.getIdsInformationTypeList(null, "idstIndex", "DESC");
		String jsondata = createJsonFromList(list);
		out.write(jsondata);
		out.flush();
	}
	
	

	/**
	 * 获取信息列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-26
	 */
	public void getIdsIList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
		if(sysManager == null) {
			return;
		}
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
		String idsTitleCondition = ServletRequestUtils.getStringParameter(request, "idsTitle_Condition", "").trim();
		if(idsTitleCondition != null && idsTitleCondition.length() > 0) {
			String[] searchCon = StringUtils.split(idsTitleCondition, " ,");
			List idsTitleConditionList = new ArrayList();
			addToCollection(idsTitleConditionList, searchCon);
			conditions.put("idsTitleCondition", idsTitleConditionList);
		}
		Long groupId = ServletRequestUtils.getLongParameter(request, "idstId", 0L);
		if(groupId > 0L){
			conditions.put("groupId", groupId);
		}
		
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "idsiDate");
		conditions.put("sort", sort);
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		conditions.put("dir", dir);
		List list = this.idsServices.getIdsInformationList(pagination, conditions);
		String result = CommonUtil.createJsonTable(list, new String[] {"idsiId", "idsiIdstId", "idstName", "smanName", "idsiTitle",
				"idsiDate", "idsiHitCount"}, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}
	
	public void getGroupJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		
		String hql = "select g.idstId, g.idstName from IdsInformationType g order by g.idstIndex ASC";
		List<Object[]> list = this.idsServices.find(hql);
		
		String jsondata = CommonUtil.createOptionsList(list);
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
		out.close();
	}

	/**
	 * 保存信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-26
	 */
	public boolean saveIdsI(HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean b = false;
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			String idsiTitle = ServletRequestUtils.getStringParameter(request, "idsiTitle", "").trim();// 标题
			Long idsiIdstId = ServletRequestUtils.getLongParameter(request, "idsiIdstId", 0); // 信息分类ID
			String idsiContent = ServletRequestUtils.getStringParameter(request, "content", "").trim();// 内容
			if(!CommonUtil.isNotNull(idsiTitle))
				throw new ErrorMessageException("信息标题不能为空！");
			if(idsiTitle.length() > 80)
				throw new ErrorMessageException("信息标题不能超过80个字符！");
			if(idsiIdstId == 0)
				throw new ErrorMessageException("信息分类不正确！");
			if(!CommonUtil.isNotNull(idsiContent))
				throw new ErrorMessageException("信息内容不能为空！");
			if(idsiContent.length() > 50000)
				throw new ErrorMessageException("信息内容不能超过5万个字符！");
			IdsInformationType iit = (IdsInformationType)this.idsServices.get(IdsInformationType.class, idsiIdstId);
			if(iit == null)
				throw new ErrorMessageException("信息分类不存在！");
			// 新增信息
//			List list = this.idsServices.getIdsInformation(idsiTitle);
//			if(list.size() > 0)
//				throw new ErrorMessageException("该标题已经存在！");
			IdsInformation ii = new IdsInformation();
			
			//封面处理，先上传封面。如果用户没有上传，再从正方中提取
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			MultipartFile coverFile = multiRequest.getFile("infoCover");
			String idsiImgUrl = null;
			idsiImgUrl = loadSourceFile(coverFile, "pic");
			if(CommonUtil.isNotNull(idsiImgUrl) == false){
				//如果没有上传封面，再根据内容判断是否包含img 如果包含，剔出图片链接
				Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");//<img[^<>]*src=[\'\"]([0-9A-Za-z.\\/]*)[\'\"].(.*?)>");
				Matcher m = p.matcher(idsiContent);
				if(m.find()){
					ImgHandleUtil.pictureHandle(idsiImgUrl, 240, 160, false);
					idsiImgUrl = m.group(1);
				}
			}else{
				ImgHandleUtil.pictureHandle(idsiImgUrl, 240, 160, false);
				idsiImgUrl = getUrl(idsiImgUrl, "封面");
			}
			ii.setIdsiImgUrl(idsiImgUrl);
			 
			//判断内容中HTML标签是否闭合完整，避免页面紊乱
			if(TagsChecker.check(idsiContent) == false){
				idsiContent = TagsChecker.fix(idsiContent);
			}
			//剔出内容中的所有文字
			 String idsiDescription="";
			 idsiDescription = idsiContent.replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "").replaceAll("</[a-zA-Z]+[1-9]?>", "");
			 if(idsiDescription.length() > 300){
				 idsiDescription=idsiDescription.substring(0,300);
			 }
			 ii.setIdsiDescription(idsiDescription);
			  
			ii.setIdsiContent(idsiContent);
			ii.setIdsiDate(new Date());
			ii.setIdsiIdstId(idsiIdstId);
			ii.setIdsiSmanId(sysManager.getSmanId());
			ii.setIdsiTitle(idsiTitle);
			ii.setIdsiHitCount(0);
			ii.setIdsiLanguage(iit.getIdstLanguage());
			this.idsServices.save(ii);
			// 更新分类中信息数量
			iit.setIdstNumber(iit.getIdstNumber() + 1);
			this.idsServices.save(iit);
			
			//加入索引
			ResMainBean dataBean = this.idsServices.infoToDatabean(ii);
			this.luceneDaoService.addDatabean(dataBean);
			b = true;
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			b = false;
		}catch(Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
			b = false;
		}
		return b;
	}

	/**
	 * 更新信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-26
	 */
	public boolean updateIdsI(HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean b = false;
		
		try {
			String idsiTitle = ServletRequestUtils.getStringParameter(request, "idsiTitle", "").trim();// 标题
			Long idsiIdstId = ServletRequestUtils.getLongParameter(request, "idsiIdstId", 0); // 信息分类ID
			String idsiContent = ServletRequestUtils.getStringParameter(request, "content", "").trim();// 内容
			Long idsiId = ServletRequestUtils.getLongParameter(request, "idsiId", 0);
			if(!CommonUtil.isNotNull(idsiTitle))
				throw new ErrorMessageException("信息标题不能为空！");
			if(idsiTitle.length() > 80)
				throw new ErrorMessageException("信息标题不能超过80个字符！");
			if(idsiIdstId == 0)
				throw new ErrorMessageException("信息分类不正确！");
			if(!CommonUtil.isNotNull(idsiContent))
				throw new ErrorMessageException("信息内容不能为空！");
			if(idsiContent.length() > 50000)
				throw new ErrorMessageException("信息内容不能超过5万个字符！");
			IdsInformationType iit = (IdsInformationType)this.idsServices.get(IdsInformationType.class, idsiIdstId);
			if(iit == null)
				throw new ErrorMessageException("信息分类不存在！");
			if(idsiId == 0)
				throw new ErrorMessageException("信息ID不能为空！");
			IdsInformation ii = (IdsInformation)this.idsServices.get(IdsInformation.class, idsiId);
			if(ii == null)
				throw new ErrorMessageException("该信息不存在！");
			List list = this.idsServices.getIdsInformation(idsiTitle);
			if(list.size() > 0) {
				IdsInformation tempII = (IdsInformation)list.get(0);
				if(!tempII.getIdsiId().equals(idsiId))
					throw new ErrorMessageException("该标题已经存在！");
			}
			// 保存旧分类ID
			Long oldId = ii.getIdsiIdstId();
			
			//图片改变状态
			String imgChang = ServletRequestUtils.getStringParameter(request, "imgChang", "0");
			if(imgChang.equals("1")){
				//因为路径编码过，所以无法找到物理路径进行删除
				if(CommonUtil.isNotNull(ii.getIdsiImgUrl())){
					String localpath = CommonUtil.parseUrlName(ii.getIdsiImgUrl());
					this.localUploadService.deleteFile(localpath, "");
				}
				
				//封面处理，先上传封面。如果用户没有上传，再从正方中提取
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				MultipartFile coverFile = multiRequest.getFile("infoCover");
				String idsiImgUrl = null;
				idsiImgUrl = loadSourceFile(coverFile, "pic");
				if(CommonUtil.isNotNull(idsiImgUrl) == false){
					//如果没有上传封面，再根据内容判断是否包含img 如果包含，剔出图片链接
					Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");//<img[^<>]*src=[\'\"]([0-9A-Za-z.\\/]*)[\'\"].(.*?)>");
					Matcher m = p.matcher(idsiContent);
					if(m.find()){
						ImgHandleUtil.pictureHandle(idsiImgUrl, 240, 160, false);
						idsiImgUrl = m.group(1);
					}
				}else{
					ImgHandleUtil.pictureHandle(idsiImgUrl, 240, 160, false);
					idsiImgUrl = getUrl(idsiImgUrl, "封面");
				}
				ii.setIdsiImgUrl(idsiImgUrl);
			}
			  
			//判断内容中HTML标签是否闭合完整，避免页面紊乱
			if(TagsChecker.check(idsiContent) == false){
				idsiContent = TagsChecker.fix(idsiContent);
			}
			//剔出内容中的所有文字
			 String idsiDescription="";
			 idsiDescription = idsiContent.replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "").replaceAll("</[a-zA-Z]+[1-9]?>", "");
			 if(idsiDescription.length() > 300){
				 idsiDescription=idsiDescription.substring(0,300);
			 }
			 ii.setIdsiDescription(idsiDescription);
			 
			ii.setIdsiContent(idsiContent);
			ii.setIdsiTitle(idsiTitle);
			ii.setIdsiIdstId(idsiIdstId);
			ii.setIdsiLanguage(iit.getIdstLanguage());
			
			this.idsServices.save(ii);
			// 当分类发生改变时，更新分类中信息数量
			if(!oldId.equals(idsiIdstId)) {
				IdsInformationType oldIit = (IdsInformationType)this.idsServices.get(IdsInformationType.class, oldId);
				if(oldIit != null){
					Long tempId = oldIit.getIdstNumber();
					if(tempId > 0)
						oldIit.setIdstNumber(tempId-1);
					else
						oldIit.setIdstNumber(0L);
					this.idsServices.save(oldIit);
					iit.setIdstNumber(iit.getIdstNumber() + 1);
					this.idsServices.save(iit);
				}
			}
			

			//更新索引
			ResMainBean dataBean = this.idsServices.infoToDatabean(ii);
			this.luceneDaoService.updateDatabean(dataBean);
			b = true;
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			b = false;
		}catch(Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
			b = false;
		}
		return b;
	}

	/**
	 * 获取信息内容
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-26
	 */
	public void getIdsIContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long idsiId = ServletRequestUtils.getLongParameter(request, "idsiId", 0);
			if(idsiId == 0)
				throw new ErrorMessageException("信息ID不存在！");
			IdsInformation ii = (IdsInformation)this.idsServices.get(IdsInformation.class, idsiId);
			if(ii == null)
				throw new ErrorMessageException("信息不存在！");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("success", true);
			result.put("idsiIdstId", ii.getIdsiIdstId());
			result.put("idsiTitle", ii.getIdsiTitle());
			result.put("idsiContent", ii.getIdsiContent());
			JSONObject jsonObject = JSONObject.fromObject(result);
			out.write(jsonObject.toString());
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
	 * 删除信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-10-26
	 */
	public void delIdsI(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long idsiId = ServletRequestUtils.getLongParameter(request, "idsiId", 0);
			if(idsiId == 0)
				throw new ErrorMessageException("信息ID不能为空！");
			IdsInformation ii = (IdsInformation)this.idsServices.get(IdsInformation.class, idsiId);
			if(ii == null)
				throw new ErrorMessageException("该信息不存在！");
			
//			if(CommonUtil.isNotNull(ii.getIdsiImgUrl())){
//				this.localUploadService.deleteFile(ii.getIdsiImgUrl(), "");
//			}
			this.idsServices.delete(ii);
			// 更新分类中信息数量
			IdsInformationType iit = ii.getIdsInformationType();
			Long temp = iit.getIdstNumber();
			if(temp > 0)
				iit.setIdstNumber(temp - 1);
			else
				iit.setIdstNumber(0L);
			this.idsServices.save(iit);
			
			//删除索引
			this.luceneDaoService.deleteDatabean(String.valueOf(idsiId));
			out.write("{success:true,msg:'删除信息成功！'}");
			out.flush();
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}
	}

	private String createJsonFromList(List list) {
		if(list.size() == 0)
			return "[]";
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

	private static void addToCollection(Collection collection, Object[] arr) {
		if(null != collection && null != arr) {
			CollectionUtils.addAll(collection, arr);
		}
	}
	
	/**
	 * 上传信息内容图片 
	 * @throws Exception 
	 */
	public ModelAndView uploadAction(HttpServletRequest request, HttpServletResponse response)throws Exception{
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter(); 
		String fileName = "";
		String path = request.getContextPath();
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		String webServicePath = SysConfigInfo.FILE_URL_PATH;
		String type = ServletRequestUtils.getStringParameter(request, "type","");
		
		try{
			
			MultipartFile ccinFile = multiRequest.getFile("file");
			
			fileName = loadSourceFile(ccinFile,type);
			
			String filename = ccinFile.getOriginalFilename();
	
			out.println("<script language=\'javascript\'>parent.document.form1.url.value='"+webServicePath+getUrl(fileName,filename) + "';"); 
	        out.println("parent.document.form1.wjm.value='"+filename+"';</script>");
	        out.println("<script language=\'javascript\'>window.location='"+path+"/html/uploadfile.jsp?r=s';");
	        out.println("</script>");
	
	    	out.flush();
	    	out.close();
	    } catch (Exception e) {
	        out.println("Unable to upload the file."+e.getMessage()+"<br>");
	        out.println("Error :/"+fileName);
	    }
	    
	    return null;
	}
	
	protected String getUrl(String pathAndLocalFileName, String viewFileName) throws Exception {
		return "/FileDownload?downloadPath=" + localUploadService.encode(pathAndLocalFileName) + "&fileName="
				+ localUploadService.encode(viewFileName);
	}

	/**
	 * 上传文件----用于文本编辑器的上传图片
	 * @param paperInfo
	 * @param sourceFile
	 * @param deptFolder
	 * @return
	 * @throws Exception 
	 */
	protected String loadSourceFile(MultipartFile sourceFile,String type) throws Exception{
		String sourceFilename = sourceFile.getOriginalFilename();
		if(ServiceUtil.isNotNull(sourceFilename) == false){
			return "";
		}
		String extName = sourceFilename.substring(sourceFilename.lastIndexOf(".") + 1); //扩展名
		if(type == "pic"){
			if(!isImg(extName.toLowerCase())) 
				return "";
		}
		
		String localFileName = System.currentTimeMillis() + "." + extName;
		Calendar now = Calendar.getInstance();
		String date = now.get(Calendar.YEAR) +""+ (now.get(Calendar.MONTH)+1) +""+now.get(Calendar.DAY_OF_MONTH) + "";
		String filePath = SysConfigInfo.INFORMATION_DIR + date + "/";
		
		try {
			localUploadService.saveFile(filePath, localFileName, sourceFile.getBytes()); 
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return "";
		} 
		return filePath+localFileName;
	}
	
	
	public boolean isImg(String type){
		// 图像
		if("jpg".equals(type)) {
			return true;
		}else if("bmp".equals(type)) {
			return true;
		}else if("png".equals(type)) {
			return true;
		}else if("gif".equals(type)) {
			return true;
		}else if("jpeg".equals(type)) {
			return true;
		}else 
			return false;
	}
	
	
	public void BackHasSerach(HttpServletRequest request){
		Long groupId = ServletRequestUtils.getLongParameter(request, "idstId", 0L);
		String idsTitleCondition = ServletRequestUtils.getStringParameter(request, "idsTitle_Condition", "").trim();
		int start = ServletRequestUtils.getIntParameter(request, "start", 0);
		request.setAttribute("idsTitle_Condition", idsTitleCondition);
		request.setAttribute("start", start);
		if(groupId!=0){
			request.setAttribute("groupId", groupId);
		}
	}
}














