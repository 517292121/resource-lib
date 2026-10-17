package cn.com.adtech.riss2.dwrFacade;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.adtech.riss2.bso.ISysUserPlatformService;
import cn.com.adtech.riss2.bso.infosubscription.IInfoSubscriptionService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.infosubscription.InfoSubscription;
import cn.com.adtech.riss2.domain.resource.ResLiterType;
import cn.com.adtech.riss2.domain.resource.ResSourceBrowseHistory;
import cn.com.adtech.riss2.domain.resource.ResSourceComt;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceDataCache;
import cn.com.adtech.riss2.domain.resource.ResTagInfo;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.jdbcms.ToolUnit;
import cn.com.adtech.riss2.upload.IUploadService;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class SysUserPlatformFacadeImp implements ISysUserPlatformFacade {
	private ISysUserPlatformService sysUserPlatformService;
	private IInfoSubscriptionService infoSubscriptionService;
	private IUploadService localUploadService;

	public ISysUserPlatformService getSysUserPlatformService() {
		return sysUserPlatformService;
	}

	public void setSysUserPlatformService(ISysUserPlatformService sysUserPlatformService) {
		this.sysUserPlatformService = sysUserPlatformService;
	}

	public IUploadService getLocalUploadService() {
		return localUploadService;
	}

	public void setLocalUploadService(IUploadService localUploadService) {
		this.localUploadService = localUploadService;
	}

	@Override
	public String getResSourceComtList(Long rsdaId, int currentPage, HttpServletRequest request) {
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(currentPage);
		int size = 2;
//		try {
//			size = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
//		}catch(Exception e) {
//			size = 20;
//		}
		pagination.setPageResults(size);
		SysUserinfo sysUserinfo = (SysUserinfo)request.getSession().getAttribute("sysUserinfo");
		List<ResSourceComt> resSourceComtList = sysUserPlatformService.getResSourceComtByRsdaId(rsdaId, pagination, sysUserinfo);
		StringBuilder result = new StringBuilder("{\"data\":[");
		int i = 0;
		int len = resSourceComtList.size();
		for(ResSourceComt resSourceComt : resSourceComtList) {
			i++;
			result.append("{");
			result.append("\"rscoId\":\"").append(resSourceComt.getRscoId()).append("\"");
			result.append(",\"suinName\":\"").append(resSourceComt.getSysUserinfo().getSuinName()).append("\"");
			result.append(",\"rscoDate\":\"").append(ConstantsObj.FORMAT_DATE_TIME.format(resSourceComt.getRscoDate())).append("\"");
			result.append(",\"rscoGrade\":\"").append(resSourceComt.getRscoGrade()).append("\"");
			result.append(",\"rscoContent\":\"").append(replaceSpecialCharacter(resSourceComt.getRscoContent())).append("\"");
			result.append(",\"rscoStatus\":\"").append(resSourceComt.getRscoStatus()).append("\"");
			String suinImageUrl = resSourceComt.getSysUserinfo().getSuinImageUrl();
			boolean temp = false;
			if(CommonUtil.isNotNull(resSourceComt.getSysUserinfo().getSuinImageLocalName())) {
				temp = true;
			}else {
				temp = false;
				if(SysUserinfo.SEX_MALE.equals(resSourceComt.getSysUserinfo().getSuinSex())) {
					suinImageUrl = ConstantsObj.SUIN_IMAGE_MALE;
				}else if(SysUserinfo.SEX_FEMALE.equals(resSourceComt.getSysUserinfo().getSuinSex())) {
					suinImageUrl = ConstantsObj.SUIN_IMAGE_FEMALE;
				}else {
					suinImageUrl = ConstantsObj.SUIN_IMAGE_UNKNOWN;
				}
			}
			result.append(",\"suinImageUrl\":\"").append(suinImageUrl).append("\"");
			result.append(",\"isUserOwnImg\":\"").append(temp).append("\"");
			result.append("}");
			if(i != len)
				result.append(",");
		}
		result.append("],totalPages:").append(pagination.getTotalPages()).append(",totalResult:").append(pagination.getTotalResults())
				.append("}");
		return result.toString();
	}

	@Override
	public String saveResSourceReview(Long rsdaId, String reviewContent, Short grade, HttpServletRequest request) {
		ResourceBundle rb = ServiceUtil.getUserLocale(request);
		SysUserinfo sysUserinfo = (SysUserinfo)request.getSession().getAttribute("sysUserinfo");
		if(sysUserinfo == null) {
			return "{\"errorMessage\":\"" + rb.getString("resource.msg_1") + "\",\"isError\":true}";
		}
		if(!ServiceUtil.isNotNull(reviewContent)) {
			return "{\"errorMessage\":\"" + rb.getString("resource.msg_2") + "\",\"isError\":true}";
		}
		if(reviewContent.length() > 300) {
			return "{\"errorMessage\":\"" + rb.getString("resource.msg_3") + "\",\"isError\":true}";
		}
		try {
			ResSourceComt resSourceComt = new ResSourceComt();
			resSourceComt.setRscoRsdaId(rsdaId);
			resSourceComt.setRscoContent(reviewContent);
			resSourceComt.setRscoGrade((short)0);
			resSourceComt.setRscoSuinId(sysUserinfo.getSuinId());
			resSourceComt.setRscoDate(new Date());
			sysUserPlatformService.saveResSourceReview(resSourceComt);
			String suinImageUrl = sysUserinfo.getSuinImageUrl();
			if(!CommonUtil.isNotNull(sysUserinfo.getSuinImageLocalName())) {
				if(SysUserinfo.SEX_MALE.equals(sysUserinfo.getSuinSex())) {
					suinImageUrl = ConstantsObj.SUIN_IMAGE_MALE;
				}else if(SysUserinfo.SEX_FEMALE.equals(sysUserinfo.getSuinSex())) {
					suinImageUrl = ConstantsObj.SUIN_IMAGE_FEMALE;
				}else {
					suinImageUrl = ConstantsObj.SUIN_IMAGE_UNKNOWN;
				}
			}
			return "{\"data\":{\"rscoId\":\"" + resSourceComt.getRscoId() + "\",\"rscoDate\":\""
					+ ConstantsObj.FORMAT_DATE_TIME.format(resSourceComt.getRscoDate()) + "\",\"rscoGrade\":\""
					+ resSourceComt.getRscoGrade() + "\",\"suinImageUrl\":\""+suinImageUrl+"\"},\"isError\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"errorMessage\":\"" + rb.getString("resource.msg_4") + "\",\"isError\":true}";
		}
	}

	@Override
	public String saveResSourceReviewReply(Long rsdaId, Long rscoId, String replyContent, HttpServletRequest request) {
		SysUserinfo sysUserinfo = (SysUserinfo)request.getSession().getAttribute("sysUserinfo");
		if(sysUserinfo == null) {
			return "{\"errorMessage\":\"请登录后回复信息！\",\"isError\":true}";
		}
		if(!ServiceUtil.isNotNull(replyContent)) {
			return "{\"errorMessage\":\"回复内容不能为空！\",\"isError\":true}";
		}
		if(replyContent.length() > 300) {
			return "{\"errorMessage\":\"回复内容不得超过300字！\",\"isError\":true}";
		}
		if(rscoId <= 0l) {
			return "{\"errorMessage\":\"回复失败，请刷新页面后重试！\",\"isError\":true}";
		}
		try {
			ResSourceComt resSourceComt = new ResSourceComt();
			resSourceComt.setRscoRsdaId(rsdaId);
			resSourceComt.setRscoContent(replyContent);
			resSourceComt.setRscoRefid(rscoId);
			resSourceComt.setRscoSuinId(sysUserinfo.getSuinId());
			resSourceComt.setRscoDate(new Date());
			resSourceComt.setRscoStatus(ResSourceComt.STATUS_PAST);
			sysUserPlatformService.save(resSourceComt);
			return "{\"successMessage\":\"回复成功，请等待审核！\",\"isError\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"errorMessage\":\"回复失败，请稍后再试！\",\"isError\":true}";
		}
	}

	@Override
	public String getResSourceReviewReplyList(Long rscoId, HttpServletRequest request) {
		return null;
	}

	@Override
	public String cutoverLanguageForResourcesById(Long rsdaId, Long rltyId, HttpServletRequest request) {
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
				+ "/";
		return sysUserPlatformService.getSourceDataForDetailByLanguageId(basePath, rsdaId, rltyId, request);
	}

	@Override
	public String getSourceDataListForIndex(int selectType, int formatType, int sortType, int currentPage, HttpServletRequest request) {
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(currentPage);
//		int size = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		int size = 15;
		pagination.setPageResults(size);
		List<ResSourceDataCache> list = sysUserPlatformService.getSourceDataListForIndexMainList(pagination, formatType, selectType,
				sortType);
		StringBuilder result = new StringBuilder("{\"data\":[");
		for(int i = 0; i < list.size(); i++) {
			ResSourceDataCache dataCache = list.get(i);
			if(i != 0) {
				result.append(",");
			}
			result.append("{");
			result.append("\"rsdaId\":\"").append(dataCache.getRsdcId()).append("\"");
			result.append(",\"rsdaBigimgUrl\":\"").append(
					ServiceUtil.isNotNull(dataCache.getRsdcBigimgUrl()) ? "<img class='img' width='100px' height='120px' src='"+dataCache.getRsdcFileAddress() + dataCache.getRsdcBigimgUrl()+"' />"
							: dataCache.getRsdcRltyType()).append("\"");
			result.append(",\"rltyId\":\"").append(dataCache.getRsdcRltyId()).append("\"");// 类型id
			result.append(",\"rltyName\":\"").append(dataCache.getRsdcRltyName()).append("\"");// 类型名称
			result.append(",\"rsdaTitle\":\"").append(replaceSpecialCharacter(ToolUnit.subString(dataCache.getRsdcTitle(), 24))).append("\"");
			result.append(",\"rsdaCreator\":\"").append(
					!ServiceUtil.isNotNull(dataCache.getRsdcCreator()) ? "" : replaceSpecialCharacter(ServiceUtil.subString(dataCache
							.getRsdcCreator(), ConstantsObj.ResourceLength.CREATER_LENGTH, ""))).append("\"");
			result.append(",\"rsdaPublisher\":\"").append(
					!ServiceUtil.isNotNull(dataCache.getRsdcPublisher()) ? "" : replaceSpecialCharacter(ServiceUtil.subString(dataCache
							.getRsdcPublisher(), ConstantsObj.ResourceLength.PUBLISHER_LENGTH, ""))).append("\"");
			result.append(",\"rsdaDescription\":\"").append(
					!ServiceUtil.isNotNull(dataCache.getRsdcDescription()) ? "" : replaceSpecialCharacter(ServiceUtil.subString(dataCache
							.getRsdcDescription(), ConstantsObj.ResourceLength.DESCRIPTION_LENGTH, ""))).append("\"");
			result.append(",\"rsdaDate\":\"").append(
					dataCache.getRsdcBuildDate() == null ? "" : ConstantsObj.FORMAT_DATE_TIME.format(dataCache.getRsdcBuildDate())).append(
					"\"");
			result.append(",\"iconType\":\"").append(ServiceUtil.isNotNull(dataCache.getRsdcIconType())?dataCache.getRsdcIconType():new Random().nextInt(2)).append("\"");// 图标类型，用于图标的显示
			result.append(",\"reviewnum\":\"").append(dataCache.getRsdcReviewNum()).append("\"");// 评论数
			result.append("}");
		}
		result.append("],\"totalPages\":").append("\"").append(pagination.getTotalPages()).append("\"").append(",\"totalResult\":").append(
				"\"").append(pagination.getTotalResults()).append("\"").append("}");
		String jsonData = result.toString();
		jsonData = ToolUnit.stringToJsondata(jsonData);
		jsonData = jsonData.replaceAll("\"null\"", "\"\"");
		return jsonData;
	}

	private String replaceSpecialCharacter(String source) {
		if(ServiceUtil.isNotNull(source)) {
//			if(source.indexOf("'") != -1) {
//				// 将单引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
//				source = source.replaceAll("'", "\\'");
//			}
//			if(source.indexOf("\"") != -1) {
//				// 将双引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
//				source = source.replaceAll("\"", "\\\"");
//			}
//			if(source.indexOf("\r\n") != -1) {
//				// 将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行
//				source = source.replaceAll("\r\n", "\\u000d\\u000a");
//			}
//			if(source.indexOf("\n") != -1) {
//				// 将换行转换一下，因为JSON串中字符串不能出现显式的换行
//				source = source.replaceAll("\n", "\\u000a");
//			}
			return source.replace("\\", "\\\\").replace("\'", "\\\'").replace("\"", "\\\"").
			replace("/", "\\/").replace("\b", "\\b").replace("\f", "\\f").
			replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
		}else {
			return "";
		}
	}

//	private String getIconType(ResSourceData resSource) {
//		if(ServiceUtil.isNotNull(resSource.getRsdaFileLocalName())) {
//			return ServiceUtil.getIconTypeBySpread(resSource.getRsdaFileLocalName());
//		}
//		if(ServiceUtil.isNotNull(resSource.getRsdaViewfileLocalName())) {
//			return ServiceUtil.getIconTypeBySpread(resSource.getRsdaViewfileLocalName());
//		}
//		return "";
//	}

	@Override
	public String getSourceBrowseHistory(String cookieIds, HttpServletRequest request) {
		ResourceBundle rb = ServiceUtil.getUserLocale(request);
		SysUserinfo sysUserinfo = (SysUserinfo)request.getSession().getAttribute("sysUserinfo");
		StringBuilder result = new StringBuilder();
		try {
			if(sysUserinfo == null)
				throw new ErrorMessageException(rb.getString("resource.tag.tisi.nouser"));
			// 获取最新浏览历史记录
			List<ResSourceBrowseHistory> list = this.sysUserPlatformService.getSourceBrowseHistory(sysUserinfo.getSuinId(), "DESC", null);
			result.append("{\"isError\":false,\"data\":[");
			for(int i = 0, j = list.size(); i < j; i++) {
				if(i == ConstantsObj.BROWSE_HISTORY_LENGTH)
					break;
				result.append("{");
				ResSourceBrowseHistory resSourceBrowseHistory = list.get(i);
				ResSourceData resSourceData = resSourceBrowseHistory.getResSourceData();
				result.append("\"rsdaId\":\"").append(resSourceData.getRsdaId()).append("\"");
				result.append(",\"subRsdaTitle\":\"").append(replaceSpecialCharacter(resSourceData.getSubRsdaTitle())).append("\"");
				result.append(",\"rsdaTitle\":\"").append(replaceSpecialCharacter(resSourceData.getRsdaTitle())).append("\"");
				result.append(", \"docTypeName\":").append("\"").append(resSourceData.getRsdaFileType()).append("\"");
				result.append("}");
				if(i < j - 1) {
					result.append(",");
				}
			}
			result.append("]}");
		}catch(Exception e) {
			if(cookieIds.trim().length() > 0) {
				/**
				 * 对Cookie值进行解码
				 */
				try {
					cookieIds = this.localUploadService.decode(cookieIds);
				}catch(Exception e1) {
					throw new ErrorMessageException(rb.getString("resource.history.decode"));
				}
				String[] rsdaIds = cookieIds.split("@");
				result.append("{\"isError\":false,\"data\":[");
				for(int i = 0, j = rsdaIds.length; i < j; i++) {
					if(i == ConstantsObj.BROWSE_HISTORY_LENGTH)
						break;
					Long id = Long.parseLong(rsdaIds[i]);
					ResSourceData resSourceData = (ResSourceData)this.sysUserPlatformService.get(ResSourceData.class, id);
					if(resSourceData != null) {
						result.append("{");
						result.append("\"rsdaId\":\"").append(resSourceData.getRsdaId()).append("\"");
						result.append(",\"subRsdaTitle\":\"").append(replaceSpecialCharacter(resSourceData.getSubRsdaTitle())).append("\"");
						result.append(",\"rsdaTitle\":\"").append(replaceSpecialCharacter(resSourceData.getRsdaTitle())).append("\"");
						result.append(", \"docTypeName\":").append("\"").append(resSourceData.getRsdaFileType()).append("\"");
						result.append("}");
						if(i < j - 1) {
							result.append(",");
						}
					}
				}
				result.append("]}");
				return result.toString();
			}else {
				return "{\"isError\":false,\"data\":[]}";
			}
		}
		return result.toString();
	}

	@Override
	public String checkUserNameIsExit(String loginName) {
		String msg = "";
		try {
			if(sysUserPlatformService.getUserinfoByLoginName(loginName) != null) {
				msg = "{\"isExit\":true}";
			}else {
				msg = "{\"isExit\":false}";
			}
		}catch(Exception e) {
			msg = "{\"isExit\":false}";
		}
		return msg;
	}

	@Override
	public String saveSelfTag(Long rsdaId, String rtinTitle, HttpServletRequest request) {
		try {
			SysUserinfo sysUserinfo = (SysUserinfo)request.getSession().getAttribute("sysUserinfo");
			if(sysUserinfo == null) {
				return "{\"isError\":true,\"errorMessage\":\"" + ServiceUtil.getUserLocale(request).getString("resource.tag.tisi.nouser")
						+ "\"}";
			}else if(rsdaId == null || rsdaId <= 0) {
				return "{\"isError\":true,\"errorMessage\":\"" + ServiceUtil.getUserLocale(request).getString("resource.tag.tisi.nodata")
						+ "\"}";
			}else {
				sysUserPlatformService.saveSelfTag(rsdaId, sysUserinfo.getSuinId(), rtinTitle);
				return "{\"isError\":false}";
			}
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			return "{\"isError\":true,\"errorMessage\":\"" + ServiceUtil.getUserLocale(request).getString(e.getMessage()) + "\"}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"isError\":true,\"errorMessage\":\"" + ServiceUtil.getUserLocale(request).getString("resource.tag.tisi.adderror")
					+ "\"}";
		}
	}

	@Override
	public String checkEmailIsUse(String suinEmail) {
		String msg = "";
		try {
			if(sysUserPlatformService.getUserinfoBySuinEmail(suinEmail) != null) {
				msg = "{\"isExit\":true}";
			}else {
				msg = "{\"isExit\":false}";
			}
		}catch(Exception e) {
			msg = "{\"isExit\":false}";
		}
		return msg;
	}

	@Override
	public String saveSysUserStyle(String type, HttpServletRequest request, HttpServletResponse response) {
//		Long skinId = 0l;
//		SysUserinfo sysUserinfo = (SysUserinfo)request.getSession().getAttribute("sysUserinfo");
//		if(sysUserinfo == null) {
			// 未登录用户切换皮肤时存cookie
			String skinCookieId = "skin";
			String skinCookieValue = "";
			try {
				skinCookieValue = localUploadService.encode(type);
			}catch(Exception e) {
			}
			int maxAge = 60 * 60 * 24 * 365;
			Cookie cookie = new Cookie(skinCookieId, skinCookieValue);
			cookie.setMaxAge(maxAge);
			cookie.setPath("/");// 此处的路径不能修改否则其它的地方拿不到此cookie值
			response.addCookie(cookie);
			return "";
//		}
//		if(!ServiceUtil.isNotNull(type)) {
//			type = "1";
//		}
//		try {
//			skinId = Long.parseLong(type);
//		}catch(Exception e) {
//			try {
//				skinId = Long.parseLong(ConstantsObj.getSysProperty(MmSettingConstant.getRiss2DefaultSkin()));
//			}catch(Exception ex) {
//				skinId = 2l;
//			}
//		}
//		sysUserinfo.setSuinSstyId(skinId);
//		this.sysUserPlatformService.save(sysUserinfo);
//		request.getSession().setAttribute("sysUserinfo", sysUserinfo);
	}

	@Override
	public String getCorrelationSourceDataList(Long rsdaId, Long rsdaRltyId, Long rsdaRmstId, HttpServletRequest request) {
		StringBuilder result = new StringBuilder("{\"data\":[");
		try {
			List<ResSourceData> list = sysUserPlatformService.getCorrelationSourceDataList(rsdaId, rsdaRltyId, rsdaRmstId);
			for(int i = 0; i < list.size(); i++) {
				ResSourceData resSourceData = list.get(i);
				ResLiterType resLiterType = (ResLiterType)sysUserPlatformService.get(ResLiterType.class, rsdaRltyId);
				if(i != 0) {
					result.append(",");
				}
				result.append("{");
				result.append("\"rsdaId\":\"").append(resSourceData.getRsdaId()).append("\"");
				result.append(",\"rsdaTitle\":\"").append(replaceSpecialCharacter(resSourceData.getRsdaTitle())).append("\"");
				result.append(",\"rsdaBigimgUrl\":\"").append(ServiceUtil.getImgUrl(resSourceData, request)).append("\"");
				result.append(",\"rsdaGrade\":\"").append(resSourceData.getRsdaGrade()).append("\"");
				result.append(",\"rsdaLooknum\":\"").append(resSourceData.getRsdaLooknum()).append("\"");
				result.append(",\"rsdaRltyType\":\"").append(resLiterType.getRltyType()).append("\"");
				result.append("}");
			}
			result.append("],\"isError\":false}");
		}catch(Exception e) {
			e.printStackTrace();
			result.append("],\"isError\":true}");
		}
		return result.toString();
	}

	@Override
	public String getSubjectHotSourceDataList(Long rsdaId, Long rsdaRstyId, HttpServletRequest request) {
		StringBuilder result = new StringBuilder("{\"data\":[");
		try {
			List<Object[]> list = sysUserPlatformService.getSubjectHotSourceDataList(rsdaId, rsdaRstyId);
			for(int i = 0; i < list.size(); i++) {
				Object[] objects = list.get(i);
				if(i != 0) {
					result.append(",");
				}
				result.append("{");
				result.append("\"rsdaId\":\"").append(objects[0]).append("\"");
				result.append(",\"rsdaTitle\":\"").append(replaceSpecialCharacter(objects[1]+"")).append("\"");
				result.append(",\"rsdaFormat\":\"").append(objects[2]).append("\"");
				result.append("}");
			}
			result.append("],\"isError\":false}");
		}catch(Exception e) {
			e.printStackTrace();
			result.append("],\"isError\":true}");
		}
		return result.toString();
	}

	@Override
	public String delTagInfo(String ids, HttpServletRequest request) {
		try {
			if(!ServiceUtil.isNotNull(ids))
				throw new ErrorMessageException(ServiceUtil.getUserLocale(request).getString("resource.tag.tisi.delerror.noresult"));
			this.sysUserPlatformService.deleteTagInfo(ids.substring(0, ids.length() - 1));
			return "{\"isError\":false}";
		}catch(Exception e) {
			return "{\"isError\":true,\"errorMessage\":\"" + e.getMessage() + "\"}";
		}
	}

	@Override
	public String getResourceTagInfoList(Long suinId, Long rsdaId, HttpServletRequest request) {
		try {
			if(rsdaId == null)
				throw new ErrorMessageException(ServiceUtil.getUserLocale(request).getString("resource.tag.tisi.nodata"));
			List<ResTagInfo> tagInfoList = null;
			if(suinId == null) {
				tagInfoList = this.sysUserPlatformService.getTagInfoList(null, rsdaId);
			}else {
				tagInfoList = this.sysUserPlatformService.getTagInfoList(suinId, rsdaId);
			}
			String tagList = "";
			boolean hasData = false;
			for(ResTagInfo resTagInfo : tagInfoList) {
				if(suinId != null) {
					tagList += "<span style='float:left'><input class='tag_check' type='checkbox' style='border: none;float:left' name='tagName' value=" + resTagInfo.getRtinId() + ">";
				}
				tagList += "<a href='javascript:void(0);' onclick=searchForTag('" + resTagInfo.getRtinTitle() + "','"
						+ resTagInfo.getRtinId() + "') title='" + resTagInfo.getRtinTitle() + "'>" + resTagInfo.getRtinTitle()
						+ "</a></span>";
				hasData = true;
			}
			if(suinId != null && tagList.trim().length() < 1) {
				tagList = ServiceUtil.getUserLocale(request).getString("resource.tag.tisi.adderror.noresult");
			}
			return "{\"isError\":false,\"tagList\":\"" + tagList.trim() + "\",\"hasData\":\"" + hasData + "\"}";
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			return "{\"isError\":true,\"errorMessage\":\"" + ServiceUtil.getUserLocale(request).getString(e.getMessage()) + "\"}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"isError\":true,\"errorMessage\":\"" + ServiceUtil.getUserLocale(request).getString("resource.tag.tisi.adderror")
					+ "\"}";
		}
	}
	
	@Override
	public String saveResSourceGrade(Long rsdaId, Short grade, HttpServletRequest request, HttpServletResponse response){
		ResourceBundle rb = ServiceUtil.getUserLocale(request);
		String browseHistoryListCookieName = "gradeCookieList";
		ResSourceComt rsc = null;
		
		try {
			SysUserinfo sysUserinfo = (SysUserinfo)request.getSession().getAttribute("sysUserinfo");
			// 游客评分
			if(sysUserinfo == null) {
				// 获取 Cookie值
				Cookie[] cookies = request.getCookies();
				// 存在评分cookie，则不能再次评分
				for(int i=0;i<cookies.length;i++){
					if( browseHistoryListCookieName.equals(cookies[i].getName()) ) {
						return "{\"errorMessage\":\"" + rb.getString("resource.msg_6") + "\",\"isError\":true}";
					}
				}
				rsc = new ResSourceComt();
				rsc.setRscoRsdaId(rsdaId);
				rsc.setRscoGrade(grade);
				rsc.setRscoDate(new Date());
			// 登录用户评分
			}else{
				boolean b = sysUserPlatformService.findRssSourceGradeOfUser(sysUserinfo.getSuinId() , rsdaId);
				if(b == false){
					rsc = new ResSourceComt();
					rsc.setRscoSuinId(sysUserinfo.getSuinId());
					rsc.setRscoRsdaId(rsdaId);
					rsc.setRscoGrade(grade);
					rsc.setRscoDate(new Date());
				}else{
					return "{\"errorMessage\":\"" + rb.getString("resource.msg_7") + "\",\"isError\":true}";
				}
			}
			
			// 保存评分信息
			sysUserPlatformService.saveResSourceReview(rsc);
			Double avgGrade = (Double)sysUserPlatformService.getResSourceForGrade(rsc.getRscoRsdaId());
			ResSourceData rsd = (ResSourceData)sysUserPlatformService.get(ResSourceData.class, rsc.getRscoRsdaId());
			rsd.setRsdaGrade((long)avgGrade.intValue());
			sysUserPlatformService.save(rsd);
			int maxAge = 60 ;
			Cookie cookie = new Cookie(browseHistoryListCookieName, String.valueOf(rsc.getRscoId()));
			// 设置评分cookie有限时间
			cookie.setMaxAge(maxAge);
			// cookie设置到客户端浏览器
			response.addCookie(cookie);
			
			return "{\"data\":{\"rscoId\":\"" + rsc.getRscoId() + "\",\"rscoDate\":\""
					+ ConstantsObj.FORMAT_DATE_TIME.format(rsc.getRscoDate()) + "\",\"rscoGrade\":\""
					+ rsd.getRsdaGrade() + "\"},\"isError\":false}";
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"errorMessage\":\"" + rb.getString("resource.msg_5") + "\",\"isError\":true}";
		}
	}
	
	public String getKeyWordsInfoList(Long userId,HttpServletRequest request, HttpServletResponse response){
		ResourceBundle rb = ServiceUtil.getUserLocale(request);
		
		try{
			if(userId == null || userId == 0 ){
				throw new ErrorMessageException(rb.getString("rsschannels.addrsstoresalert"));
			}
			List<InfoSubscription> list = sysUserPlatformService.getKeyWordsInfoList(userId);
			StringBuffer result = new StringBuffer("");
			result.append("{\"result\":\"");
			for(InfoSubscription is : list) {
//				result.append("<span style='float:left'><input class='tag_check' type='checkbox' style='border: none; margin: 5px 0px' title='" + is.getIssKeyWord() ).append(
//						"' name='tagName' value=" + is.getIssId() + ">&nbsp;" + is.getIssKeyWord() + "&nbsp;&nbsp;&nbsp;&nbsp</span>");
				result.append("<li class='color1' ref="+is.getIssId()+"><span>" + is.getIssKeyWord() + "</span>");
				result.append("<i class='x-close' title='删除' onclick='delSelfTag("+is.getIssId()+");'></i></li>");
			}
			if(list.size() > 0){
				result.append("<li class='color2 cpointer' onclick='delSelfTag(0);'><span >清空</span></li>");
			}
			result.append("\",\"isError\":false");
			if(list.size() > 0){
				result.append(",\"hasData\":true}");
			}else{
				result.append(",\"hasData\":\"false\"}");
			}
			return result.toString();
		}catch(Exception e){
			e.printStackTrace();
			return "{\"errorMessage\":\"" + rb.getString("usercenter.resource.error.message") + "\",\"isError\":true}";
		}
	}
	
	public String delKeyWordsInfo(String ids,HttpServletRequest request, HttpServletResponse response){
		ResourceBundle rb = ServiceUtil.getUserLocale(request);
		
		try {
			if(!ServiceUtil.isNotNull(ids))
				throw new ErrorMessageException(rb.getString("resource.tag.tisi.adderror.nokeywords"));
			ids = ids.substring(0, ids.length() - 1);
			if(ids != null && !ids.trim().equals("")) {
				String[] idsStr = ids.split(",");
				Long[] infoIds = new Long[idsStr.length];
				for(int i = 0; i < idsStr.length; i++) {
					infoIds[i] = Long.parseLong(idsStr[i]);
				}
				this.infoSubscriptionService.deleteInfoSubscriptions(infoIds);
			}
			return "{\"isError\":false}";
		}catch(Exception e) {
			return "{\"isError\":true,\"errorMessage\":\"" + e.getMessage() + "\"}";
		}
	}
	
	public String addKeyWordsInfo(String keywords,HttpServletRequest request, HttpServletResponse response){
		ResourceBundle rb = ServiceUtil.getUserLocale(request);
		
		try {
			SysUserinfo sysUserinfo = (SysUserinfo)request.getSession().getAttribute("sysUserinfo");
			if(ServiceUtil.getStringRealLength(keywords) > 256)
				throw new ErrorMessageException(rb.getString("usercenter.infosubscription.title.tooLong"));
			String[] words = keywords.split(",|，");
			InfoSubscription[] infos = new InfoSubscription[words.length];
			for (int i = 0; i < words.length; i++) {
				if(words[i] != null && !words[i].trim().equals("")) {
					InfoSubscription infoSubscription = new InfoSubscription();
					infoSubscription.setIssDate(new Date());
					infoSubscription.setIssUserId(sysUserinfo.getSuinId());
					if(ServiceUtil.getStringRealLength(words[i].trim()) > 256){
						throw new ErrorMessageException(rb.getString("usercenter.infosubscription.title.tooLong"));
					}else{
						infoSubscription.setIssKeyWord(words[i].trim());
					}
					infos[i] = infoSubscription;
				}
			}
			this.infoSubscriptionService.addInfoSubscriptions(infos);
			return "{\"isError\":false}";
		}catch(Exception e) {
			return "{\"isError\":true,\"errorMessage\":\"" + e.getMessage() + "\"}";
		}
	}

	public IInfoSubscriptionService getInfoSubscriptionService() {
		return infoSubscriptionService;
	}

	public void setInfoSubscriptionService(
			IInfoSubscriptionService infoSubscriptionService) {
		this.infoSubscriptionService = infoSubscriptionService;
	}
}
