package cn.com.adtech.riss2.controller.console.links;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.links.ILinkInfoService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.CorporationPropertiesKeys;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.sc.SysLink;
import cn.com.adtech.riss2.upload.IUploadService;

import com.jbetter.framework.session.Pagination;

public class LinkController extends MultiActionController {
	
	private ILinkInfoService linkInfoService;
	public void setLinkInfoService(ILinkInfoService linkInfoService) {
		this.linkInfoService = linkInfoService;
	}
	
	/**本地文件上传处理对象**/
	protected IUploadService localUploadService = null;
	public void setLocalUploadService(IUploadService localUploadService) {
		this.localUploadService = localUploadService;
	}
	
	
	/**
	 * 友情链接管理EXT入口
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public ModelAndView initPage(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("links/linkList");
	}
	
	/**
	 * 保存与修改友情链接入口
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public ModelAndView saveOrUpdateInitpage(HttpServletRequest request, HttpServletResponse response) {
		Long slinId = ServletRequestUtils.getLongParameter(request,"slinId",0L);  
		int index = this.linkInfoService.getSlinIndex();  
		request.setAttribute("slinIndex", index);
		
		if(slinId!=0L){
			SysLink sysLink = (SysLink)linkInfoService.get(SysLink.class, slinId);
			request.setAttribute("sysLink", sysLink);
			request.setAttribute("slinIndex", sysLink.getSlinIndex());
		}
		
		return new ModelAndView("links/linkInfoInit");
	}
	
	/**
	 * 保存链接
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public ModelAndView saveOrUpdateLinkInfo(HttpServletRequest request, HttpServletResponse response)throws IOException{
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();  
		//链接ID
		Long slinId = ServletRequestUtils.getLongParameter(request,"slinId",0L);  
		//链接名字
		String slinName = ServletRequestUtils.getStringParameter(request, "slinName","");
		//链接名字不能为空
		if(slinName.trim().equals("")){
			request.setAttribute("result", "<script>alert('链接名称不能为空！');</script>"); 
			return saveOrUpdateInitpage(request,response);
		}
		
		//如果是新增就new
		SysLink sysLink=null;
		if(slinId!=0l){
			sysLink = (SysLink)this.linkInfoService.get(SysLink.class, slinId); 
		}else{
			sysLink=new SysLink();
		}
		

		String slinLinkpath = ServletRequestUtils.getStringParameter(request, "slinLinkpath","");
		int slinType = ServletRequestUtils.getIntParameter(request, "slinType",0);
		int slinIndex = ServletRequestUtils.getIntParameter(request, "slinIndex",0);
		
		int loadfile_statu = ServletRequestUtils.getIntParameter(request, "loadfile_statu",0);
		
		sysLink.setSlinLinkpath(slinLinkpath);
		sysLink.setSlinIndex(slinIndex);
		sysLink.setSlinName(slinName);
		sysLink.setSlinType(slinType);
		
		if(loadfile_statu==1){
			String downloadPath = SysConfigInfo.FILE_ROOT_DIR;
			String localpath = CommonUtil.parseUrlName(sysLink.getSlinImgpath());
			File file = new File(downloadPath + localpath);
			if(file.isFile()){
				file.delete();
			}
			sysLink.setSlinImgpath("");
		}
		
		if(loadfile_statu!=2){
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		
				MultipartFile slinImgpath = multiRequest.getFile("slinImgpath");
				if(!slinImgpath.isEmpty()){
					
					//验证源文大小
					int sourceMax = CorporationPropertiesKeys.CorporationCcinImageSize;
					if(slinImgpath.getSize() > (1024 * 1024 * sourceMax)){
						request.setAttribute("result", "<script>alert('logo图片不得超过 "+sourceMax+" M！');</script>");
						return saveOrUpdateInitpage(request,response);
					}
					try {
						String imageUrl = loadSourceFile(slinImgpath, CorporationPropertiesKeys.LinkImage);
						if(imageUrl.equals("")){
							request.setAttribute("result", "<script>alert('上传logo不正确！');</script>"); 
							return saveOrUpdateInitpage(request,response);
						}
						sysLink.setSlinImgpath(CommonUtil.getUrl(imageUrl, ""));
						
					} catch (Exception e) {
						logger.error("上传文件发生了错误");
						e.printStackTrace();
						request.setAttribute("result", "<script>alert('上传封面发生了错误！');</script>"); 
						return saveOrUpdateInitpage(request,response);
					}
				}
		}
		
		
		boolean b = false;  
		if(slinId!=0L){
			b=this.linkInfoService.updateLinkInfo(sysLink);
		}else{ 
			b=this.linkInfoService.saveLinkInfo(sysLink);
		}
		
		if(b){  
			request.setAttribute("result", "<script>alert('保存成功！');</script>"); 
			return saveOrUpdateInitpage(request,response);
		}else{  
			request.setAttribute("result", "<script>alert('保存失败！');</script>"); 
			return saveOrUpdateInitpage(request,response);
		}  
	}
	
	/**
	 * 上传文件----用于上传封面以及文本编辑器的上传文件
	 * @param paperInfo
	 * @param sourceFile
	 * @param deptFolder
	 * @return
	 * @throws Exception 
	 */
	protected String loadSourceFile(MultipartFile sourceFile, String folder) throws Exception{
		String sourceFilename = sourceFile.getOriginalFilename();
		if(ServiceUtil.isNotNull(sourceFilename) == false){
			return "";
		}
		String extName = sourceFilename.substring(sourceFilename.lastIndexOf(".") + 1); //扩展名
		if(!isImg(extName.toLowerCase())) 
			return "";
		String localFileName = System.currentTimeMillis() + "." + extName;
		String filePath = folder + "/";
		
		try {
			localUploadService.saveFile(filePath, localFileName, sourceFile.getBytes()); 
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return "";
		} 
		return filePath+localFileName;
	}
	
	/**
	 * 封面预览 
	 */
	public ModelAndView companyImgShow(HttpServletRequest request, HttpServletResponse response)throws IOException{
		String url = ServletRequestUtils.getStringParameter(request, "url",""); 
		request.setAttribute("url", url);
		return new ModelAndView("links/linkImgShow");
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
	
	/**
	 * EXT查询友情链接
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getLinkInfoToJson(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		// 添加分页器
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(currentPage);// 设置当前页
		pagination.setPageResults(limit);// 设置每页记录数
		
		//排序相关
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "slinIndex");// 排序字段
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "asc");// 排序方式
		
		String datajson = linkInfoService.getLinkInfoToJson(pagination, sort, dir);
		out.print(datajson);
		out.flush();
	}
	
	/**
	 * 删除链接
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void deleteLinkInfo(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();  
		Long slinId = ServletRequestUtils.getLongParameter(request,"slinId",0l);  
		SysLink sysLink = (SysLink)linkInfoService.get(SysLink.class, slinId);
		String linkImg = sysLink.getSlinImgpath();
		if(null!=linkImg){
			File file = new File(SysConfigInfo.FILE_ROOT_DIR + sysLink.getSlinImgpath());
			if(file.isFile()){
				file.delete();
			}
			sysLink.setSlinImgpath("");
		}
		boolean b = false;  
			b = linkInfoService.deleteLinkInfo(slinId);
		if(b){
			out.print("删除成功！");
		}else{
			out.print("删除失败!");
		}
		out.flush();
	}
	
}
