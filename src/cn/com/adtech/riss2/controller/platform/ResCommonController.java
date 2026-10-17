package cn.com.adtech.riss2.controller.platform;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import cn.com.adtech.riss2.bso.platform.IResourceSearchService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.IpUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.file.MyFileUtil;
import cn.com.adtech.riss2.domain.SysUserType;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.count.SysAccessCounter;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResSourceBrowseHistory;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.exception.ErrorMessageException;

@Controller
public class ResCommonController {

	//资源信息服务对象
	@Resource(name="resourceSearchService")
	protected IResourceSearchService resourceSearchService;

	//获取资源详细信息
	public String getResDetail(String rsdaId, Model model, HttpServletRequest request){
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		Long numId = null;
		try {
			numId = Long.parseLong(rsdaId);
		} catch (Exception e) {
			numId = CommonUtil.strToId(rsdaId);
		}
		String error = null;
		try {
			if(numId == null || numId < 1L){
				throw new ErrorMessageException("对不起，未找到该资源信息！");
			}
			//获取用户对象
			SysUserinfo sysUserinfo = (SysUserinfo)request.getSession().getAttribute("sysUserinfo");
			SysUserType userType = null;
			if(sysUserinfo == null) {
				userType = (SysUserType)resourceSearchService.get(SysUserType.class, SysUserType.USER_TYPE_VISITOR);
			}else {
				userType = sysUserinfo.getSysUserType();
			}

			ResSourceData resource = (ResSourceData) this.resourceSearchService.get(ResSourceData.class, numId);
			if(resource == null){
				throw new ErrorMessageException("对不起，未找到该资源信息。该资源可能已下架！");
			}
			int userRight = userType.getSutyPurview();
			if(userRight == SysUserType.PURIVEW_NOSEE){
				throw new ErrorMessageException("对不起，您没有资源的访问权限！");
			}
//			if(resource.getRsdaStatus().equals(ResSourceData.WAITING_CHECK)) {
//				throw new ErrorMessageException("该资源正在审核中，暂不能访问！");
//			}
			if(resource.getRsdaStatus().equals(ResSourceData.NO_PAST_CHECK)) {
				throw new ErrorMessageException("该资源审核未通过，暂不能访问！");
			}
			if(resource.getRsdaIsuse().equals(ResSourceData.ISUSE_FREEZE)) {
				throw new ErrorMessageException("对不起，该资源已禁止访问！");
			}
			if(resource.getResMetaStore() == null || !resource.getResMetaStore().getRmstStatus().equals(ResMetaStore.STATUS_USEABLENESS)) {
				throw new ErrorMessageException("对不起，该资源所属资源库已禁止访问！");
			}
			if(checkIp(resource.getRsdaRmstId(), request) == false){
				throw new ErrorMessageException("你的IP是：" + request.getRemoteAddr() + " ，不在该资源所在特色库的访问权限之内, 请登录！");
			}

			//更新访问量
			Long views = resource.getRsdaLooknum();
			views = views == null ? 1 : views+1;
			String hql = "update ResSourceData rsda set rsda.rsdaLooknum=" + views + " where rsda.rsdaId=" + numId;
			this.resourceSearchService.getServiceHibernateTemplate().bulkUpdate(hql);

			//后台保存浏览历史、日志等与页面展示无关的信息
			Long userId = sysUserinfo == null ? null : sysUserinfo.getSuinId();
			saveHisLog(numId, userId, request.getRemoteAddr(), resource.getRsdaTitle());

			//根据权限解析资源预览文件，源文件等信息
			Map<String, Object> resViewResult = parseRight(userRight, resource);

			//判断图书类型时，是否有源文pdf，如果有则同时提供pdf预览
			int t = resource.getRsdaSourceType() == null ? 0 : resource.getRsdaSourceType();
			if(t == 1){
				String dirpath = resource.getRsdaFileUrl();
				File fileDir = new File(dirpath);
				if(fileDir.exists()){

					File pdfFile = new File(dirpath, fileDir.getName() + ".pdf");
					if(pdfFile.exists()){
						map.put("existPdf", true);
					}
				}
			}

			//转换封面
			parseResCover(request, resource);

			//判断文件预览方式
			boolean isPdfViewer = false;
			if(CommonUtil.isNotNull(resource.getRsdaFileType())){
				String format = resource.getRsdaFileType().toLowerCase();
				String swf = resource.getRsdaFileSwfUrl();
				if(CommonUtil.isNotNull(swf) && swf.startsWith("/FileDownload")){
					//如果有swf文件，则采用传统模式预览
					isPdfViewer = false;
				}else{
					if(format.equals("pdf")){
						isPdfViewer = true;
					}
					if(format.equals("doc") || format.equals("docx")){
						if(CommonUtil.isNotNull(swf)){
							isPdfViewer = true;
						}
					}
				}
			}

			//文件大小
			if(resource.getRsdaFileSize() != null && resource.getRsdaFileSize() > 0){
				String fileSize = MyFileUtil.formatFileSize(resource.getRsdaFileSize());
				map.put("fileSize", fileSize);
			}

			//保存参数
			map.putAll(resViewResult);
			map.put("title", resource.getRsdaTitle());
			map.put("resource", resource);
			map.put("idcode", rsdaId);
			map.put("userRight", userRight);
			map.put("isPdfViewer", isPdfViewer);
			model.addAllAttributes(map);
			return null;
		} catch (ErrorMessageException e) {
			e.printStackTrace();
			error = e.getMessage();
			return error;
		} catch (Exception e) {
			e.printStackTrace();
			error = "错误：" + e.getMessage();
			return error;
		}
	}

	/**
	 * 验证IP是否合法
	 * @param rmstId
	 * @param request
	 * @return 合法返回true
	 */
	private boolean checkIp(Long rmstId, HttpServletRequest request){
		List<String> ipList = this.resourceSearchService.getStoreIpList(rmstId);
		//如果特色库未设置IP则验证通过
		if(ipList == null || ipList.size() < 1){
			return true;
		}

		String userIp = request.getRemoteAddr();
		boolean b = IpUtil.isInMyIp(userIp, ipList);

		//如果不在IP段内，用户登录后也不能预览，可直接返回 b

		if(b){
			return true;
		}
		if(request.getSession().getAttribute("sysUserinfo") == null){
			return false;
		}else{
			//如果用户已登录，也允许访问
			return true;
		}
	}

	/**
	 * 获得用户对资源的在线浏览链接、下载链接、资源类型、提示信息
	 *
	 * @author MaChao
	 */
	private Map<String, Object> parseRight(int userRight, ResSourceData resource) {
		Map<String, Object> map = new HashMap<String, Object>();
		String type = resource.getRsdaFileName(); //文件实际名称
		String fileSwfUrl = resource.getRsdaFileSwfUrl();//原文件的预览文件
		if(!CommonUtil.isNotNull(type)){
			map.put("browseUrl", "");
			map.put("downloadUrl", "");
			map.put("resType", "");
			map.put("filemsg", "该资源只有元数据信息！");
			return map;
		}
		String extname = type.substring(type.lastIndexOf(".") + 1).toLowerCase();
		if(extname.equals("tif")){ //tif。暂时无法转换的图片格式
			type = "photo";
		}else{
			type = ServiceUtil.getFileTypeBySpread(type); //获取文件载体类型
		}
		map.put("resType", type);

		if(userRight == SysUserType.PURIVEW_CANNOT_BROWSE){
			map.put("browseUrl", "");
			map.put("downloadUrl", "");
			map.put("filemsg", "您没有资源的阅读权限！");
			return map;
		}
		if(userRight == SysUserType.PURIVEW_BROWSE_SOURCE){
			map.put("browseUrl", fileSwfUrl);
			map.put("downloadUrl", "");
			if(!CommonUtil.isNotNull(fileSwfUrl)) {
				if(ServiceUtil.isNotNull(resource.getRsdaFileUrl())){
					map.put("filemsg", "该资源暂不支持在线预览！");
				}else{
					map.put("filemsg", "该资源只有元数据信息！");
				}
			}
			return map;
		}
		if(userRight == SysUserType.PURIVEW_DOWNLOAD){
			map.put("browseUrl", fileSwfUrl);
			map.put("downloadUrl", resource.getRsdaFileUrl());

			if(!CommonUtil.isNotNull(fileSwfUrl)) {
				if(ServiceUtil.isNotNull(resource.getRsdaFileUrl())){
					map.put("filemsg", "该资源暂不支持在线预览，请下载查看！");
				}else{
					map.put("filemsg", "该资源只有元数据信息！");
				}
			}
		}
		return map;
	}

	//开个新线程执行，因为本方法保存的信息与页面展示的内容无关，所以后台执行即可，添加详细页面加载速度
	private void saveHisLog(final Long rsdaId, final Long userId, final String ip, final String rsdaTitle){
		new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					//保存浏览记录
					ResSourceBrowseHistory browseHis = new ResSourceBrowseHistory();
					browseHis.setRsdaId(rsdaId);
					browseHis.setRsbhDate(new Date());
					browseHis.setSuinId(userId);
					resourceSearchService.save(browseHis);

					////保存访问日志
					resourceSearchService.saveAccessLog(SysAccessCounter.TYPE_READ, ip, userId, rsdaTitle);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 解析资源封面
	 * 资源预览方式升级后，除传统文件保持原有预览方式外，其它预览类型存储方式都发生了变化
	 * 本方法将所有资源进行封面文件进行访问方式进行解析
	 *
	 * @param resList
	 * @param typeIndex
	 * @param dirIndex
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static List<Object[]> parseCoverList(HttpServletRequest request, List<Object[]> resList, int imgIndex, int typeIndex, int dirIndex) throws UnsupportedEncodingException{
		if(resList == null || resList.size() < 1){
			return resList;
		}
		Object[] res = null;
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		for(int i=0,s=resList.size(); i<s; i++){
			res = resList.get(i);
			int t = res[typeIndex] == null ? 0 : (Integer) res[typeIndex];

			if(t == 0){ //传统资源格式
				if(res[imgIndex] != null && CommonUtil.isNotNull(res[imgIndex].toString())){
					String imgUrl = res[imgIndex].toString();
					if(!imgUrl.startsWith(SysConfigInfo.FILE_URL_PATH)){
						res[imgIndex] = SysConfigInfo.FILE_URL_PATH + imgUrl;
					}
				}
				continue;
			}

			if(res[dirIndex] == null || !CommonUtil.isNotNull(res[dirIndex].toString())){
				continue;
			}

			String dirpath = res[dirIndex].toString();
			File coverFile = new File(dirpath, "cover.jpg");
			if(coverFile.exists()){
				//多线程情况下，主线程结束后,HttpServletRequest中的数据可能会被释放，子线程数据将不能正常访问，path为空时不存访问前缀
				res[imgIndex] = path == null ? "" : basePath + "file/access/" + CommonUtil.encode(coverFile.getAbsolutePath());
			}else{
				res[imgIndex] = "";
			}
		}
		return resList;
	}
	public static ResSourceData parseResCover(HttpServletRequest request,ResSourceData resource) throws UnsupportedEncodingException{
		if(resource == null){
			return resource;
		}
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		int t = resource.getRsdaSourceType() == null ? 0 : resource.getRsdaSourceType();

		if(t == 0){ //传统资源格式
			if(resource.getRsdaBigimgUrl() != null && CommonUtil.isNotNull(resource.getRsdaBigimgUrl())){
				resource.setRsdaBigimgUrl(SysConfigInfo.FILE_URL_PATH + resource.getRsdaBigimgUrl());
			}
			return resource;
		}

		if(resource.getRsdaFileUrl() == null || !CommonUtil.isNotNull(resource.getRsdaFileUrl())){
			return resource;
		}

		String dirpath = resource.getRsdaFileUrl();
		File coverFile = new File(dirpath, "cover.jpg");
		if(coverFile.exists()){
			resource.setRsdaBigimgUrl(basePath + "file/access/" + CommonUtil.encode(coverFile.getAbsolutePath()));
		}else{
			resource.setRsdaBigimgUrl("");
		}
		return resource;
	}
}
