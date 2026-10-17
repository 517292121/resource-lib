package cn.com.adtech.riss2.controller.console.ib;

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

import cn.com.adtech.riss2.bso.ib.IBannerInfoService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.ib.IndexBanner;
import cn.com.adtech.riss2.upload.IUploadService;

import com.jbetter.framework.session.Pagination;

public class BannerController extends MultiActionController {
	
	private IBannerInfoService bannerInfoService;
	public void setBannerInfoService(IBannerInfoService bannerInfoService) {
		this.bannerInfoService = bannerInfoService;
	}
	
	/**本地文件上传处理对象**/
	protected IUploadService localUploadService = null;
	public void setLocalUploadService(IUploadService localUploadService) {
		this.localUploadService = localUploadService;
	}
	
	
	/**
	 * 友情链接管理EXT入口
	 */
	public ModelAndView initPage(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("ib/ibList");
	}
	
	/**
	 * 保存与修改友情链接入口
	 */
	public ModelAndView saveOrUpdateInitpage(HttpServletRequest request, HttpServletResponse response) {
		Long ibId = ServletRequestUtils.getLongParameter(request,"ibId",0L);  
		if(ibId!=0L){
			IndexBanner ib = (IndexBanner)bannerInfoService.get(IndexBanner.class, ibId);
			request.setAttribute("ib", ib);
			request.setAttribute("ibIndex", ib.getIbIndex());
		}else{
			int index = this.bannerInfoService.getSlinIndex();  
			request.setAttribute("ibIndex", index);
		}
		return new ModelAndView("ib/ibInfoInit");
	}
	
	/**
	 * 保存链接
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public ModelAndView saveOrUpdateLinkInfo(HttpServletRequest request, HttpServletResponse response)throws IOException{
		Long ibId = ServletRequestUtils.getLongParameter(request,"ibId",0L);  
		String ibName = ServletRequestUtils.getStringParameter(request, "ibName","");
		if(ibName.trim().equals("")){
			request.setAttribute("result", "<script>alert('链接名称不能为空！');</script>"); 
			return saveOrUpdateInitpage(request,response);
		}
		
		//如果是新增就new
		IndexBanner ib=null;
		if(ibId!=0l){
			ib = (IndexBanner)this.bannerInfoService.get(IndexBanner.class, ibId); 
		}else{
			ib = new IndexBanner();
		}
		
		String ibUrl = ServletRequestUtils.getStringParameter(request, "ibUrl","");
		String ibBgColor = ServletRequestUtils.getStringParameter(request, "ibBgColor","");
		String ibDescription = ServletRequestUtils.getStringParameter(request, "ibDescription","");
		int ibIndex = ServletRequestUtils.getIntParameter(request, "ibIndex",0);
		Long ibIsShow = ServletRequestUtils.getLongParameter(request, "ibIsShow",1);

		ib.setIbUrl(ibUrl);
		ib.setIbIndex(ibIndex);
		ib.setIbName(ibName);
		ib.setIbBgColor(ibBgColor);
		ib.setIbIsShow(ibIsShow);
		ib.setIbDescription(ibDescription);
		
		int loadfile_statu = ServletRequestUtils.getIntParameter(request, "loadfile_statu",0);
		if(loadfile_statu==1){
			String path = CommonUtil.parseUrlName(ib.getIbImgUrl());
			File file = new File(SysConfigInfo.FILE_ROOT_DIR + path);
			if(file.isFile()){
				file.delete();
			}
			ib.setIbImgUrl("");
		}
		
		if (loadfile_statu != 2) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

			MultipartFile slinImgpath = multiRequest.getFile("ibImgUrl");
			if (!slinImgpath.isEmpty()) {
				// 验证源文大小
				int sourceMax = 10;
				if (slinImgpath.getSize() > (1024 * 1024 * sourceMax)) {
					request.setAttribute("result", "<script>alert('logo图片不得超过 " + sourceMax + " M！');</script>");
					return saveOrUpdateInitpage(request, response);
				}
				try {
					String imageUrl = loadSourceFile(slinImgpath, SysConfigInfo.OTHER_DIR);
					if (imageUrl.equals("")) {
						request.setAttribute("result", "<script>alert('上传logo不正确！');</script>");
						return saveOrUpdateInitpage(request, response);
					}
					ib.setIbImgUrl(CommonUtil.getUrl(imageUrl, ""));
				} catch (Exception e) {
					logger.error("上传文件发生了错误");
					e.printStackTrace();
					request.setAttribute("result", "<script>alert('上传封面发生了错误！');</script>");
					return saveOrUpdateInitpage(request, response);
				}
			}
		}
		
		
		boolean b = false;  
		if(ibId!=0L){
			b=this.bannerInfoService.updateLinkInfo(ib);
		}else{ 
			b=this.bannerInfoService.saveLinkInfo(ib);
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
		
		try {
			localUploadService.saveFile(folder, localFileName, sourceFile.getBytes()); 
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return "";
		} 
		return folder+localFileName;
	}
	
	/**
	 * 封面预览 
	 */
	public ModelAndView companyImgShow(HttpServletRequest request, HttpServletResponse response)throws IOException{
		String url = ServletRequestUtils.getStringParameter(request, "url",""); 
		request.setAttribute("url", url);
		return new ModelAndView("ib/ibImgShow");
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
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "ibIndex");// 排序字段
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "asc");// 排序方式
		
		String datajson = bannerInfoService.getLinkInfoToJson(pagination, sort, dir);
		out.print(datajson);
		out.flush();
	}
	
	/**
	 * 删除链接
	 */
	public void deleteLinkInfo(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();  
		Long ibId = ServletRequestUtils.getLongParameter(request,"ibId",0l); 
		IndexBanner ib = (IndexBanner)bannerInfoService.get(IndexBanner.class, ibId);
		String linkImg = ib.getIbImgUrl();
		if(null!=linkImg){
			String downloadPath = SysConfigInfo.FILE_ROOT_DIR;
			File file = new File(downloadPath+ib.getIbImgUrl());
			if(file.isFile()){
				file.delete();
			}
			ib.setIbImgUrl("");
		}
		boolean b = false;  
		b = bannerInfoService.deleteLinkInfo(ib);
		if(b){
			out.print("删除成功！");
		}else{
			out.print("删除失败!");
		}
		out.flush();
	}
}
