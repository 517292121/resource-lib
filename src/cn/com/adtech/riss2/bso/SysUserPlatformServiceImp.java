package cn.com.adtech.riss2.bso;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import cn.com.adtech.riss2.bso.platform.PlatformBasicService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.IpUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.IdsInformation;
import cn.com.adtech.riss2.domain.IdsInformationType;
import cn.com.adtech.riss2.domain.SysEmailActivation;
import cn.com.adtech.riss2.domain.SysForgetPassword;
import cn.com.adtech.riss2.domain.SysTouristBrowseCache;
import cn.com.adtech.riss2.domain.SysUserType;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.count.SysAccessCounter;
import cn.com.adtech.riss2.domain.ib.IndexBanner;
import cn.com.adtech.riss2.domain.infosubscription.InfoSubscription;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResLiterType;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.domain.resource.ResMissionLanguage;
import cn.com.adtech.riss2.domain.resource.ResSourceComt;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceFile;
import cn.com.adtech.riss2.domain.resource.ResSourceLanguage;
import cn.com.adtech.riss2.domain.resource.ResSubjectType;
import cn.com.adtech.riss2.domain.resource.ResTagInfo;
import cn.com.adtech.riss2.domain.sc.SysLink;
import cn.com.adtech.riss2.domain.text.ResLibraryClassify;
import cn.com.adtech.riss2.domain.text.ResTextResource;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.jdbcms.ToolUnit;

import com.jbetter.framework.session.Pagination;
import com.jbetter.hibernate.IdGenerator;

/**
 * 前台检索及会员中心服务类
 * 
 * @author MaChao
 * @date 2010-9-13
 */
@SuppressWarnings("unchecked")
public class SysUserPlatformServiceImp extends CommonServiceCommonlyImp implements ISysUserPlatformService {
	private static final Logger logger = Logger.getLogger(SysUserPlatformServiceImp.class);

	@Override
	public List<IdsInformation> getInformationList() {
		String hql = "select new IdsInformation(idsi.idsiId,idsi.idsiTitle) from IdsInformation idsi order by idsi.idsiDate desc";
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(0);
		pagination.setPageResults(10);
		return getPageHibernateTemplate().find(pagination, hql);
	}

	@Override
	public List<ResMetaStore> getMetaStoreList() {
		String hql = "select rmstId,rmstName from ResMetaStore where rmstStatus=" + ResMetaStore.STATUS_USEABLENESS+" order by rmstIndexSort";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<ResSourceData> getSourceDataListForIndex(Pagination pagination, int type, int sortParam, int selectType, int sortType,
			boolean more) {
		List<ResSourceData> list = null;
		String hql = "select sourceData from ResSourceData sourceData " + "where sourceData.rsdaStatus=" + ResSourceData.STATUS_PASS
				+ " and sourceData.rsdaIsuse=" + ResSourceData.ISUSE_USEABLENESS + " and sourceData.rsdaRmstStatus="
				+ ResMetaStore.STATUS_USEABLENESS + " and ((sourceData.rsdaRminStatus=" + ResMissionInfo.RMINSTATUS_COMMIT_PASS 
				+ ") " + " or sourceData.rsdaAuthortype="
				+ ResSourceData.AUTHORTYPE_MEMBER_INPUT + ")";
//				+ " and (RSDA_RIGHTS is null or RSDA_RIGHTS <2 )";
				
		List param = new ArrayList();
		if(selectType == 1) {
			hql += " and sourceData.rsdaRcmd=" + ResSourceData.IS_RECOMMEND;
		}
		if(selectType == 2) {//取消后台时间设置
//			int lastTime = 0;
//			try {
//				lastTime = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getRiss2IndexHotResource()));
//			}catch(Exception e) {
//				lastTime = 0;
//			}
//			if(lastTime > 0) {
//				Calendar calendar = Calendar.getInstance();
//				calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONTH) - lastTime);
//				hql += " and sourceData.rsdaBuilddate>?";
//				param.add(calendar.getTime());
//			}
		}
		switch(type) {
		case 1:
			hql += " and sourceData.resLiterType.rltyType=" + ResLiterType.TYPE_PICTURE;
			break;
		case 2:
			hql += " and sourceData.resLiterType.rltyType=" + ResLiterType.TYPE_DOCUMENT;
			break;
		case 3:
			hql += " and sourceData.resLiterType.rltyType=" + ResLiterType.TYPE_VIDEO;
			break;
		case 4:
			hql += " and sourceData.resLiterType.rltyType=" + ResLiterType.TYPE_MEDIA;
			break;
		case 5:
			hql += " and sourceData.resLiterType.rltyType=" + ResLiterType.TYPE_VIDEO;
			break;
		default:
			break;
		}
		Boolean hasOrder = false;
		if(sortType == 1) {
			hql += " order by sourceData.rsdaBuilddate asc";
			hasOrder = true;
		}else if(sortType == 2) {
			hql += " order by sourceData.rsdaBuilddate desc";
			hasOrder = true;
		}
		if(selectType == 2) {
			if(hasOrder) {
				hql += ",";
			}else {
				hql += " order by ";
			}
			hql += "sourceData.rsdaLooknum desc";
		}
		list = getPageHibernateTemplate().find(pagination, hql, param.toArray());
		return list;
	}

	@Override
	public List<ResTagInfo> getTagInfoList(Long suinId, Long rsdaId) {
		String hql = "select tagInfo from ResTagInfo tagInfo ";
		hql += "where tagInfo.rtinRsdaId=" + rsdaId;
		if(suinId != null && suinId > 0) {
			hql += " and tagInfo.rtinSuinId=" + suinId;
		}
		return getPageHibernateTemplate().find(hql);
	}

	@Override
	public List getTagInfoListForIndex() {
		return (List)getHibernateTemplate().execute(new HibernateCallback(){
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder sql = new StringBuilder("");
				return session.createSQLQuery(sql.toString()).list();
			}
		});
	}

	@Override
	public List<ResSourceData> getSourceDataListForSimpleSearch(Pagination pagination, int sortParam, Long threeBigType, String keyWord,
			Long rmstId, Long rstyId) {
		List param = new ArrayList();
		StringBuffer hql = new StringBuffer("select sourceData from ResSourceData sourceData ");
		hql.append(" where sourceData.rsdaIsuse=" + ResSourceData.ISUSE_USEABLENESS);
		hql.append(" and sourceData.resMetaStore.rmstStatus=").append(ResMetaStore.STATUS_USEABLENESS);
		hql.append(" and sourceData.rsdaTitle like ? and sourceData.rsdaStatus=").append(ResSourceData.STATUS_PASS);
		hql.append(" and (sourceData.resMissionInfo.rminStatus=").append(ResMissionInfo.RMINSTATUS_COMMIT_PASS);
		hql.append(" or sourceData.resMissionInfo.rminStatus=").append(ResMissionInfo.RMINSTATUS_INTERPRET_CHECKOUT);
		hql.append(" or sourceData.resMissionInfo.rminStatus=").append(ResMissionInfo.RMINSTATUS_INTERPRET_FAIL + ") ");
		param.add("%" + keyWord + "%");
		if(threeBigType.shortValue() == ResLiterType.TYPE_DOCUMENT.shortValue()) {
			hql.append(" and sourceData.resLiterType.rltyType=?");
			param.add(threeBigType.shortValue());
		}else if(threeBigType.shortValue() == ResLiterType.TYPE_MEDIA.shortValue()) {
			hql.append(" and sourceData.resLiterType.rltyType=?");
			param.add(threeBigType.shortValue());
		}else if(threeBigType.shortValue() == ResLiterType.TYPE_PICTURE.shortValue()) {
			hql.append(" and sourceData.resLiterType.rltyType=?");
			param.add(threeBigType.shortValue());
		}
		if(rmstId != 0) {
			hql.append(" and sourceData.rsdaRmstId=?");
			param.add(rmstId);
		}
		if(rstyId != 0) {
			hql.append(" and sourceData.rsdaRstyId=?");
			param.add(rstyId);
		}
		// Map map = getNavigationResult(parser);
		switch(sortParam) {
		case 1:
		case 2:
			hql.append(" order by sourceData.rsdaGrade");
			break;
		case 3:
		case 4:
			hql.append(" order by sourceData.rsdaGrade");
			break;
		case 5:
		case 6:
			hql.append(" order by sourceData.rsdaGrade");
			break;
		case 7:
		case 8:
			hql.append(" order by sourceData.rsdaLooknum");
			break;
		case 9:
		case 10:
			hql.append(" order by sourceData.rsdaBuilddate");
			break;
		default:
			hql.append(" order by sourceData.rsdaBuilddate");
			break;
		}
		if(sortParam % 2 == 1) {
			hql.append(" asc ");
		}else {
			hql.append(" desc ");
		}
		return getPageHibernateTemplate().find(pagination, hql.toString(), param.toArray());
	}

	@Override
	public Map<String, Object> getSourceDataForDetail(String basePath, final Long rsdaId, Long rsdaLanguageId, HttpServletRequest request, SysUserType userType) {
		ResourceBundle rb = ServiceUtil.getUserLocale(request);
		// 资源基本信息对象，供页面灵活处理
		ResSourceData sourceData = null;
		try {
			sourceData = (ResSourceData)get(ResSourceData.class, rsdaId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(sourceData == null){
			return null;
		}
		/////基本验证
		if(sourceData == null) {
			throw new ErrorMessageException(rb.getString("resource.error.isbuilding"));
		} 
		if(sourceData.getRsdaStatus().equals(ResSourceData.WAITING_CHECK)) {
			throw new ErrorMessageException(rb.getString("resource.error.ischecking"));
		}
		if(sourceData.getRsdaStatus().equals(ResSourceData.NO_PAST_CHECK)) {
			throw new ErrorMessageException(rb.getString("resource.error.isnotchecked"));
		}
		if(sourceData.getRsdaIsuse().equals(ResSourceData.ISUSE_FREEZE)) {
			throw new ErrorMessageException(rb.getString("resource.error.isfreeze"));
		}
		if(sourceData.getResMetaStore() == null || !sourceData.getResMetaStore().getRmstStatus().equals(ResMetaStore.STATUS_USEABLENESS)) {
			throw new ErrorMessageException(rb.getString("resource.error.cannotuse"));
		}
		if(userType.getSutyPurview().equals(SysUserType.PURIVEW_NOSEE)){
			throw new ErrorMessageException("对不起，您没有该资源的访问权限！");
		}
		//用户对资源的访问权限验证
		String purviewHql = "select reda.redaData from ResExtendData reda where reda.redaRsdaId=" + sourceData.getRsdaId() + " and LOWER(reda.resElementPool.repoName)='accessrights'";
		String data = getSingleData(purviewHql);
		int purview = 0;
		if(CommonUtil.isNotNull(data)){
			try {
				purview = Integer.parseInt(data);
				if(purview < 1){
					//如果资源没设置权限。则以最高权限（可下载）处理
					purview = SysUserType.PURIVEW_DOWNLOAD;
				}
			} catch (Exception e) {
				//如果资源没设置权限。则以最高权限（可下载）处理
				purview = SysUserType.PURIVEW_DOWNLOAD;
			}
			if(purview == SysUserType.PURIVEW_NOSEE){
				throw new ErrorMessageException("对不起，您没有该资源的访问权限！");
			}
		}else{
			//如果资源没设置权限。则以最高权限（可下载）处理
			purview = SysUserType.PURIVEW_DOWNLOAD;
		}
		
		
		//////特色库IP地址验证////////////////////////////
		List<String> ipRanges =getHibernateTemplate().find("select ipDeploy.rsidIpPart from ResStoreIpDeploy ipDeploy where ipDeploy.rsidRmstId="+sourceData.getResMetaStore().getRmstId());
		if(ipRanges != null && ipRanges.size() > 0 && !IpUtil.isInMyIp(request.getRemoteAddr(),ipRanges)){ // 对资源所在特色库ip访问的限制
			/**资源访问权限控制——根据IP的限制**/
			
			//特色库IP段外全部阻止型
//			String message = "你的IP是：" + request.getRemoteAddr() + " ，不在该资源所在特色库的访问权限之内！";
//			throw new ErrorMessageException(message);
			
			//特色库IP段外，且没有登录阻止，登录之后正常根据会员类型权限访问型
			if(request.getSession().getAttribute("sysUserinfo") == null){
				String message = "你的IP是：" + request.getRemoteAddr() + " ，不在该资源所在特色库的访问权限之内, 请登录！";
				throw new ErrorMessageException(message);
			}
			
			//将是否在IP段内的状态设置到界面，再根据情况处理型——当前为不存在时无法下载
			//request.setAttribute("ipIsTrue", "0");
		}else{
			//request.setAttribute("ipIsTrue", "1");
		}
		request.setAttribute("ipIsTrue", "1");
		System.out.println(" ------------------------------------ 查询资源DATA begin --------------------------------");
		
		///// 更新点击量，放到控制器里更新////////////////////////
		if(sourceData.getRsdaLooknum() == null) {
			sourceData.setRsdaLooknum(1l);
		}else {
			sourceData.setRsdaLooknum(sourceData.getRsdaLooknum() + 1l);
		}
		
		/*********2011-05-25 王林修改（将查询资源语种改为如果资源来源为学生上传的则不通过之前的关联查询）****/
		// 资源语种信息
		List<Object[]> languageList = null;
		if(sourceData.getRsdaAuthortype() > 2){
			String hql = "select rlty.rltyId, rlty.rltyName from ResLanguageType rlty where rltyId in ";
			hql+= "(select rsl.resLanguageId from ResSourceLanguage rsl where rsl.resSourceId = ?)";
			languageList = getHibernateTemplate().find(hql, sourceData.getRsdaId());
		}else{
			languageList = (List)getHibernateTemplate().execute(new HibernateCallback(){
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					StringBuffer sql = new StringBuffer("select distinct rla.RLTY_ID id,rla.RLTY_NAME name ");
					sql.append(" from RES_LANGUAGE_TYPE rla ");
					sql.append(" left join RES_SOURCE_LANGUAGE rsl on rsl.RSLA_RLTY_ID=rla.RLTY_ID");
					sql.append(" left join RES_MISSION_LANGUAGE rml on rml.RMLA_RLTY_ID=rla.RLTY_ID ");
					sql.append(" left join RES_MISSION_INFO rmi on rmi.RMIN_ID=rml.RMLA_RMIN_ID");
					// sql.append(" where rsl.RSLA_STATUS=").append(ResSourceLanguage.STATUS_PAST);
					sql.append(" where rml.RMLA_STATUS=").append(ResMissionLanguage.STATU_CHECK_SUCCESS);
					sql.append(" and rmi.RMIN_STATUS=").append(ResMissionInfo.RMINSTATUS_COMMIT_PASS);
					sql.append(" and rsl.RSLA_RSDA_ID=").append(rsdaId);
					sql.append(" order by rla.RLTY_ID");
					return session.createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING).addScalar("name", Hibernate.STRING).list();
				}
			});
		}
		/*********2011-05-25 王林修改 END****/
		
		List<Map> languageTypeList = new ArrayList<Map>();
		String currentLanguageId = getCurrentLanguageId(request);
		Boolean hasCurrentLanguage = false;//当前资源是否有网站语言
		for(Object[] sourceLanguage : languageList) {
			Map language = new HashMap();
			language.put("rltyId", sourceLanguage[0]);
			if(currentLanguageId.equals(sourceLanguage[0])){
				hasCurrentLanguage = true;
			}
			language.put("rltyName", sourceLanguage[1]);
			languageTypeList.add(language);
		}
		String hql = "select sourceLanguage from ResSourceLanguage sourceLanguage where";
		// hql += "sourceLanguage.rslaStatus=" + ResSourceLanguage.STATUS_PAST;
		hql += " sourceLanguage.resSourceId=" + rsdaId;
		if(hasCurrentLanguage){
			hql += " and sourceLanguage.resLanguageId=" + currentLanguageId;
		}else {
			hql += " and sourceLanguage.rslaIsfirst=" + ResSourceLanguage.IS_FIRST_LANGUAGE;
		}
		List<ResSourceLanguage> sourceLanguageList = getHibernateTemplate().find(hql);
		if(rsdaLanguageId <= 0l && sourceLanguageList != null && sourceLanguageList.size() > 0) {
			ResSourceLanguage sourceLanguage = (ResSourceLanguage)sourceLanguageList.get(0);
			rsdaLanguageId = sourceLanguage.getResLanguageType().getRltyId();
		}
		// 资源详细信息内容
		final Long rsdaLanguage = rsdaLanguageId;
		List<Object[]> list = (List)getHibernateTemplate().execute(new HibernateCallback(){
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer sql = new StringBuffer("select REPO_NAME as name,REPO_TITLE as title,REDA_DATA as data");
				sql.append(" from RES_ELEMENT_POOL left join RES_EXTEND_DATA on REPO_ID=REDA_REPO_ID");
				sql.append(" where REDA_RSDA_ID=").append(rsdaId).append(" and REDA_LANGUAGE=").append(rsdaLanguage);
//				sql.append(" and REPO_TYPE=").append(ResElementPool.REPO_TYPE_CORE); //取消只显示核心元素设置
//				sql.append(" and REDA_DATA is not null order by REDA_ID ASC");
				sql.append(" and (REPO_SHOWMODE=1 or ((REPO_SHOWMODE is null or REPO_SHOWMODE=0) and REDA_DATA is not null)) order by REDA_ID ASC");
				return session.createSQLQuery(sql.toString()).addScalar("name", Hibernate.STRING).addScalar("title", Hibernate.STRING)
						.addScalar("data", Hibernate.STRING).list();
			}
		});
//		if(!hasCurrentLanguage){//取资源第一语种的对应国际化文件，此后的rb对象不能通用
//			rb = getResourceBundleByLanguageId(rsdaLanguageId,request);
//		}
		String title = "";
		String abstracts = "";
		String description = "";
		String content = "";
		String eletitle = "";
		String reference = "";
		String fieldName = "";//字段名称
		String fieldContent = "";//字段内容
		
		for(Object[] o : list) {
			if(o.length < 3) {
				continue;
			}
			if(o[0] != null){
				eletitle = o[0].toString().toLowerCase();
			}
			fieldName = o[1].toString();
			if(o[2] != null){
				fieldContent = o[2].toString();
				//作者和关键词要打断后加检索链接，因此不能转换成html标签
				if(!eletitle.equals("keyword") && !eletitle.equals("creator")) {
					fieldContent = CommonUtil.hangleTextArea(fieldContent);
				}
			}
			if(eletitle.equals("title")) {
				continue;
			}
			
			//摘要
			if(eletitle.equals("abstract")) {
				abstracts = checkMore(fieldName,fieldContent,100);
				continue;
			}
			
			//描述
			if(eletitle.equals("description")) {
				description = checkMore(fieldName,fieldContent,100);
				continue;
			}
			
			//切分关键词与作者
			if(eletitle.equals("keyword") || eletitle.equals("creator")) {
				if(fieldContent != null && fieldContent.length() > 1){
					content += "<li><span>" +fieldName + "：</span><div>";
					String keyword = fieldContent;
					keyword = keyword.replace(" ", ";");
					keyword = keyword.replace("；", ";");
					keyword = keyword.replace(",", ";");
					keyword = keyword.replace("，", ";");
					String[] keys = keyword.split(";");
					for(int i=0; i<keys.length; i++){
						if(CommonUtil.isNotNull(keys[i].trim())){
							content += "<a href='javascript:void(0);' onclick='keyJump(\""+keys[i].trim()+"\");'>" + keys[i].trim() + "</a>、";
						}
					}
					content = content.substring(0,content.lastIndexOf('、'));
					content += "</div></li>";
				}
				continue;
			}
			
			//设置来源和关联两个元素的超连接
			if(eletitle.equals("relation") || eletitle.equals("source")){
				if(fieldContent != null && fieldContent.length() > 3){
					if(fieldContent.contains("www") || fieldContent.contains("http")){
						if(fieldContent.substring(0, 3).toLowerCase().equals("www")){
							reference = "<a href='http://"+fieldContent+"' target='_blank'>" + fieldContent + "</a>";
						}else{
							reference = "<a href='"+fieldContent+"' target='_blank'>" + fieldContent + "</a>";
						}
					}else{
						reference = fieldContent+"";
					}
					reference = "<li><span>" + fieldName + "：</span><div>" + reference + "</div></li>";
					continue;
				}
			}
			
			content += "<li><span>" + fieldName + "：</span><div>" + fieldContent + "</div></li>";
		}
		content = title + content;
		
		//封装非元数据信息
		if(sourceData.getResLiterType() != null) {
			content += "<li><span>资源类型：</span><div>" + sourceData.getResLiterType().getRltyName() + "</div></li>";
		}
		if(sourceData.getResMetaStore() != null) {
			content += "<li><span>所属资源库：</span><div>" + sourceData.getResMetaStore().getRmstName() + "</div></li>";
		}
		if(sourceData.getResSubjectType() != null) {
			content += "<li><span>所属学科：</span><div>" + sourceData.getResSubjectType().getRstyName() + "</div></li>";
		}
		if(sourceData.getResDynamicType() != null) {
			//自定义分类一
			content += "<li><span>" + sourceData.getResMetaStore().getRmstGroupOne() + "：</span><div>" + sourceData.getResDynamicType().getRdtyName() + "</div></li>";
		}
		content += "<li><span>创建时间：</span><div>" + ConstantsObj.FORMAT_DATE.format(sourceData.getRsdaBuilddate()) + "</div></li>";
		content += "<li><span>访问量：</span><div>" + sourceData.getRsdaLooknum() + "</div></li>";
		
		// 组装最终结果
		content += abstracts;
		content += description;
		content += reference;
		content = "<ul class=\"search_zynrL\">" + content + "</ul><div class=\"clear\"></div>";
		
		// 获得评论列表
		StringBuilder resSourceComtListHql = new StringBuilder("from ResSourceComt resSourceComt where resSourceComt.rscoRsdaId=");
		resSourceComtListHql.append(rsdaId).append(" and resSourceComt.resSourceData.rsdaCmtstus=")
				.append(ResSourceData.REVIEW_SWITCH_OPEN);
		String resSourceComtSize = "select count(*) " + resSourceComtListHql.toString() + " and resSourceComt.rscoStatus="
				+ ResSourceComt.STATUS_PAST;
		// resSourceComtListHql.append(" order by resSourceComt.rscoDate asc ");
		// List<ResSourceComt> resSourceComtList = getHibernateTemplate().find("select resSourceComt " + resSourceComtListHql.toString());
		List resSourceComtSizeList = getHibernateTemplate().find(resSourceComtSize);
		// 组装返回结果
		Map sourceDataMap = new HashMap<String, Object>();
		sourceDataMap.put("purview", purview);
		sourceDataMap.put("sourceLanguageList", languageTypeList);
		sourceDataMap.put("sourceData", sourceData);
		sourceDataMap.put("sourceDataContent", ToolUnit.jsonToString(content.replace("null", "")));
		// sourceDataMap.put("resSourceComtList", resSourceComtList);
		sourceDataMap.put("resSourceComtSize", resSourceComtSizeList.get(0));
		if(sourceData.getRsdaCmtstus().equals(ResSourceData.REVIEW_SWITCH_OPEN)) {
			sourceDataMap.put("rsdaCmtstus", true);
		}
		System.out.println(" ------------------------------------ 查询资源DATA end --------------------------------");
		return sourceDataMap;
	}

	/**验证字段内容是否超过指定长度，折叠展示*/
	private String checkMore(String fieldName,String fieldContent,int length){
		if(fieldContent.length() > length){
			return "<li><span>" + fieldName + "：</span><div class='min-h'>" + fieldContent + "<b class='more' onclick='checkMore(this);'>更多>></b></div></li>";
		}else{
			return "<li><span>" + fieldName + "：</span><div>" + fieldContent + "</div></li>";
		}
	}
	
	@Override
	public SysUserinfo getUserinfoByLoginName(String loginName) {
		List<SysUserinfo> list = getHibernateTemplate().find("from SysUserinfo where suinLoginName='" + loginName + "'");
		if(list.size() > 0) {
			return list.get(0);
		}else {
			return null;
		}
	}

	@Override
	public List<ResSourceComt> getResSourceComtByRsdaId(Long rsdaId, Pagination pagination, SysUserinfo sysUserinfo) {
		StringBuilder hql = new StringBuilder("select resSourceComt from ResSourceComt resSourceComt where resSourceComt.rscoRsdaId=");
		hql.append(rsdaId).append(" and resSourceComt.resSourceData.rsdaCmtstus=").append(ResSourceData.REVIEW_SWITCH_OPEN);
		hql.append(" and resSourceComt.rscoContent is not null ");
		hql.append(" and (resSourceComt.rscoStatus = ").append(ResSourceComt.STATUS_PAST);
		if(sysUserinfo != null){
			hql.append(" or resSourceComt.rscoSuinId = ").append(sysUserinfo.getSuinId());
		}
		hql.append(") order by resSourceComt.rscoDate asc ");
		return getPageHibernateTemplate().find(pagination, hql.toString());
	}
	
	@Override
	public IdsInformation getInfoPre(IdsInformation info){
		StringBuffer fromHql = new StringBuffer("select min(ii) from IdsInformation ii where ii.idsiId not in("+info.getIdsiId()+") ");
		fromHql.append(" and ii.idsiDate > ");
		fromHql.append("'"+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(info.getIdsiDate()))+"'");
		return (IdsInformation)getHibernateTemplate().find(fromHql.toString()).get(0);
	}
	@Override 
	public IdsInformation getInfoNext(IdsInformation info){
		StringBuffer fromHql = new StringBuffer("select max(ii) from IdsInformation ii where ii.idsiId not in("+info.getIdsiId()+") ");
		fromHql.append(" and ii.idsiDate < ");
		fromHql.append("'"+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(info.getIdsiDate()))+"'");
		return (IdsInformation)getHibernateTemplate().find(fromHql.toString()).get(0);
	}

	@Override
	public List<IdsInformation> getIdsInformationList(Pagination pagination, Long idstId,String title) {
		List param = new ArrayList();
		StringBuffer fromHql = new StringBuffer("from IdsInformation ii where ii.idsiId > 0 ");
		if(idstId > 0) {
			fromHql.append(" and ii.idsiIdstId = ").append(idstId);
		}
		if(CommonUtil.isNotNull(title)){
			fromHql.append(" and ii.idsiTitle like ").append("'%"+title+"%'");
		}
		fromHql.append(" order by ii.idsiDate DESC ");
		return getPageHibernateTemplate().find(pagination,fromHql.toString());
	}

	@Override
	public List<IdsInformationType> getIdsInformationTypeList() {
		String hql = "from IdsInformationType iit order by iit.idstNumber DESC ";
		return this.getHibernateTemplate().find(hql);
	}

	@Override
	public String getSourceDataForDetailByLanguageId(String basePath, final Long rsdaId, Long rltyId, HttpServletRequest request) {
		// 资源基本信息对象，供页面灵活处理
		ResSourceData sourceData = (ResSourceData)get(ResSourceData.class, rsdaId);
		if(sourceData == null) {
			return "";
		}
		String hql = "select sourceLanguage from ResSourceLanguage sourceLanguage where sourceLanguage.rslaStatus="
				+ ResSourceLanguage.STATUS_PAST + " and sourceLanguage.resSourceId=" + rsdaId + " and sourceLanguage.rslaIsfirst="
				+ ResSourceLanguage.IS_FIRST_LANGUAGE;
		List<ResSourceLanguage> sourceLanguageList = getHibernateTemplate().find(hql);
		if(rltyId <= 0l && sourceLanguageList != null && sourceLanguageList.size() > 0) {
			ResSourceLanguage sourceLanguage = (ResSourceLanguage)sourceLanguageList.get(0);
			rltyId = sourceLanguage.getResLanguageType().getRltyId();
		}
		// 资源详细信息内容
		final Long rsdaLanguage = rltyId;
		List<Object[]> list = (List)getHibernateTemplate().execute(new HibernateCallback(){
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer sql = new StringBuffer("select REPO_NAME as name,REPO_TITLE as title,REDA_DATA as data");
				sql.append(" from RES_ELEMENT_POOL left join RES_EXTEND_DATA on REPO_ID=REDA_REPO_ID");
				sql.append(" where REDA_RSDA_ID=? and REDA_LANGUAGE=?");
				sql.append(" and REPO_TYPE=").append(ResElementPool.REPO_TYPE_CORE);
				sql.append(" and REDA_DATA is not null order by REDA_ID ASC");
				return session.createSQLQuery(sql.toString()).addScalar("name", Hibernate.STRING).addScalar("title", Hibernate.STRING)
						.addScalar("data", Hibernate.STRING).setLong(0, rsdaId).setLong(1, rsdaLanguage).list();
			}
		});
		String title = "";
		String abstracts = "";
		String description = "";
		String content = "";
		String eletitle = "";
//		ResourceBundle rb = getResourceBundleByLanguageId(rltyId,request);
		ResourceBundle rb = ServiceUtil.getUserLocale(request);
		for(Object[] o : list) {
			if(o.length < 3 || o[2] == null || o[2] == "") {
				continue;
			}
			eletitle = o[0].toString().toLowerCase();
			if(eletitle.equals("title")) {
				title = "<li><span>" + rb.getString("resource.detail."+eletitle) + "：</span>" + ToolUnit.stringToJson((String)o[2]) + "</li>";
				continue;
			}
			if(eletitle.equals("abstract")) {
				abstracts = "<div class=\"ms search_lase\"><span>" + rb.getString("resource.detail."+eletitle) + "：</span><span>" + ToolUnit.stringToJson((String)o[2]) + "</span></div>";
				continue;
			}
			if(eletitle.equals("description")) {
				description = "<div class=\"ms search_lase\"><span>" + rb.getString("resource.detail."+eletitle) + "：</span><span>" + ToolUnit.stringToJson((String)o[2]) + "</span></div>";
				continue;
			}
			content += "<li><span>" + rb.getString("resource.detail."+eletitle) + "：</span>" + ToolUnit.stringToJson((String)o[2]) + "</li>";
		}
		content = title + content;
		if(ServiceUtil.isNotNull(content)) {
			if(sourceData.getResLiterType() != null) {
				content += "<li><span>" + rb.getString("resource.detail.sourcetype") + "：</span>" + sourceData.getResLiterType().getRltyName() + "</li>";
			}
			if(sourceData.getResMetaStore() != null) {
				content += "<li><span>" + rb.getString("resource.detail.inmetastore") + "：</span>" + sourceData.getResMetaStore().getRmstName() + "</li>";
			}
			if(sourceData.getResSubjectType() != null) {
				content += "<li><span>" + rb.getString("resource.detail.insubject") + "：</span>" + sourceData.getResSubjectType().getRstyName() + "</li>";
			}
			if(sourceData.getResDynamicType() != null) {
				content += "<li><span>" + rb.getString("resource.detail.inmetastoretype") + "：</span>" + sourceData.getResDynamicType().getRdtyName() + "</li>";
			}
			// content += "<li>创建人：" + sourceData.getSysManager().getSmanName() + "</li>";
			content += "<li><span>" + rb.getString("resource.detail.createtime") + "：</span>" + sourceData.getRsdaBuilddate() + "</li>";
			// content += "<li>预览文件名：" + sourceData.getRsdaViewfile() + "</li>";
			// content += "<li>下载文件名：" + sourceData.getRsdaLoadfile() + "</li>";
			// content += "<li>缩略图：" + sourceData.getRsdaSmallImg() + "</li>";
			// content += "<li>预览图：" + sourceData.getRsdaBigImg() + "</li>";
			// content += "<li>状态：" + sourceData.getRsdaStatus() + "</li>";
			// content += "<li>评分：<img src=\"" + basePath + "images/start" + sourceData.getRsdaGrade() + ".png\"
			// align=\"absmiddle\"/></li>";
			// content += "<li>评论开关状态：" + sourceData.getRsdaCmtstus() + "</li>";
			content += "<li><span>" + rb.getString("resource.detail.isrecommendresources") + "：</span>";
			if(sourceData.getRsdaRcmd() == 1) {
				content += rb.getString("resource.detail.yes");
			}else {
				content += rb.getString("resource.detail.no");
			}
			content += "</li>";
			content += "<li><span>" + rb.getString("resource.detail.hitnum") + "：</span>" + sourceData.getRsdaLooknum() + "</li>";
			// 组装最终结果
			content = "<ul class=\"search_zynrL\">" + content + "</ul><div class=\"clear\"></div>";
			content += abstracts;
			content += description;
		}
		return content.replace("null", "");
	}

	@Override
	public void saveResSourceReview(ResSourceComt resSourceComt) {
		save(resSourceComt);
//		String hql = "select rscoGrade from ResSourceComt where rscoStatus="+ResSourceComt.STATUS_PAST+" and rscoRsdaId=" + resSourceComt.getRscoRsdaId();
//		List<Short> list = getHibernateTemplate().find(hql);
//		Long total = 0l;
//		if(list != null && list.size() > 0) {
//			for(Short rscoGrade : list) {
//				total += rscoGrade;
//			}
//			total /= list.size();
//		}
//		getHibernateTemplate().bulkUpdate("update ResSourceData set rsdaGrade=" + total + " where rsdaId=" + resSourceComt.getRscoRsdaId());
	}

	@Override
	public List getSourceBrowseHistory(Long suinId, String dir, Long rsdaId) {
		try {
			String hql = "from ResSourceBrowseHistory rsbh where rsbh.suinId = " + suinId;
			if(rsdaId != null && rsdaId > 0) {
				hql += " and rsbh.rsdaId = " + rsdaId;
			}
			String orderHql = " order by rsbh.rsbhDate " + dir;
			return getPageHibernateTemplate().find(new Pagination(ConstantsObj.BROWSE_HISTORY_LENGTH), hql.toString() + orderHql.toString());
		} catch (Exception e) {
			//将发生异常，且用户不为空时。则很有可能是浏览历史中的资源有被删除，却没有删除浏览历史的数据。则直接删除所有该用户的所有浏览历史
			if(suinId != null && suinId > 0){
				String hql = "delete from ResSourceBrowseHistory rsbh where rsbh.suinId=?";
				getHibernateTemplate().bulkUpdate(hql, suinId);
			}
			return null;
		}
	}

	@Override
	public List getSourceDataListForIndexMainList(Pagination pagination, int type, int selectType, int sortType) {
		StringBuilder hql = new StringBuilder("select dataCache from ResSourceDataCache dataCache");
		Boolean hasWhere = false;
		if(selectType == 1) {
			hql.append(" where dataCache.rsdcRcmd=").append(ResSourceData.IS_RECOMMEND);
			hasWhere = true;
		}
		switch(type) {
		case 1:
			hql.append(hasWhere ? " and " : " where ").append(" dataCache.rsdcRltyType=").append(ResLiterType.TYPE_PICTURE);
			break;
		case 2:
			hql.append(hasWhere ? " and " : " where ").append(" dataCache.rsdcRltyType=").append(ResLiterType.TYPE_DOCUMENT);
			break;
		case 3:
			hql.append(hasWhere ? " and " : " where ").append(" dataCache.rsdcRltyType=").append(ResLiterType.TYPE_VIDEO);
			break;
		case 4:
			hql.append(hasWhere ? " and " : " where ").append(" dataCache.rsdcRltyType=").append(ResLiterType.TYPE_MEDIA);
			break;
		default:
			break;
		}
		Boolean hasOrder = false;
		if(sortType == 1) {
			hql.append(" order by dataCache.rsdcBuildDate asc");
			hasOrder = true;
		}else if(sortType == 2) {
			hql.append(" order by dataCache.rsdcBuildDate desc");
			hasOrder = true;
		}
		if(selectType == 2) {
			if(hasOrder) {
				hql.append(",");
			}else {
				hql.append(" order by ");
			}
			hql.append(" dataCache.rsdcLookNum desc");
		}
		// StringBuilder sql = new StringBuilder("select RSDA_ID rsdaId,RLTY_ID rltyId,RLTY_NAME rltyName,RSDA_TITLE rsdaTitle");
		// sql.append(",RSDA_CREATOR rsdaCreator,RSDA_PUBLISHER rsdaPublisher,RSDA_DESCRIPTION rsdaDescription,RSDA_BUILDDATE rsdaDate");
		// sql.append(",RSDA_FILE_LOCAL_NAME filename,RSDA_VIEWFILE_LOCAL_NAME viewfilename,RSDA_BIGIMG_URL imgurl");
		// sql.append(",RMST_RFIL_ID remotefileserviceid");
		// sql.append(",(select count(RSCO_ID) from RES_SOURCE_COMT where RSCO_RSDA_ID=RSDA_ID and RSCO_STATUS=1) countnum");
		// StringBuilder tempsql = new StringBuilder(" from RES_SOURCE_DATA left join RES_META_STORE on RMST_ID=RSDA_RMST_ID");
		// tempsql.append(" left join RES_MISSION_INFO on RMIN_ID=RSDA_RMIN_ID left join RES_LITER_TYPE on RLTY_ID=RSDA_RLTY_ID");
		// tempsql.append(" where RSDA_STATUS=").append(ResSourceData.STATUS_PASS);
		// tempsql.append(" and RSDA_ISUSE=").append(ResSourceData.ISUSE_USEABLENESS);
		// tempsql.append(" and RMST_STATUS=").append(ResMetaStore.STATUS_USEABLENESS);
		// tempsql.append(" and ((RMIN_STATUS in (").append(
		// ResMissionInfo.RMINSTATUS_COMMIT_PASS + "," + ResMissionInfo.RMINSTATUS_COMMIT_PASS);
		// tempsql.append("," + ResMissionInfo.RMINSTATUS_INTERPRET_FAIL).append(")) or RSDA_AUTHORTYPE=").append(
		// ResSourceData.AUTHORTYPE_MEMBER_INPUT).append(")");
		// if(selectType == 1) {
		// tempsql.append(" and RSDA_RCMD=").append(ResSourceData.IS_RECOMMEND);
		// }
		// switch(type) {
		// case 1:
		// tempsql.append(" and RLTY_TYPE=").append(ResLiterType.TYPE_PICTURE);
		// break;
		// case 2:
		// tempsql.append(" and RLTY_TYPE=").append(ResLiterType.TYPE_DOCUMENT);
		// break;
		// case 3:
		// tempsql.append(" and RLTY_TYPE=").append(ResLiterType.TYPE_VIDEO);
		// break;
		// case 4:
		// tempsql.append(" and RLTY_TYPE=").append(ResLiterType.TYPE_MEDIA);
		// break;
		// default:
		// break;
		// }
		// Boolean hasOrder = false;
		// if(sortType == 1) {
		// tempsql.append(" order by RSDA_BUILDDATE asc");
		// hasOrder = true;
		// }else if(sortType == 2) {
		// tempsql.append(" order by RSDA_BUILDDATE desc");
		// hasOrder = true;
		// }
		// if(selectType == 2) {
		// if(hasOrder) {
		// tempsql.append(",");
		// }else {
		// tempsql.append(" order by ");
		// }
		// tempsql.append("RSDA_LOOKNUM desc");
		// }
		// final String countsql = "select count(*) cont" + tempsql.toString();
		// List<Integer> countlist = (List)getHibernateTemplate().execute(new HibernateCallback(){
		// @Override
		// public Object doInHibernate(Session session) throws HibernateException, SQLException {
		// return session.createSQLQuery(countsql).addScalar("cont", Hibernate.INTEGER).list();
		// }
		// });
		// pagination.setTotalResults(countlist.get(0));
		// pagination.setTotalPages(pagination.getTotalResults() / pagination.getPageResults());
		// final String finalsql = sql.toString() + tempsql.toString();
		// List list = (List)getHibernateTemplate().execute(new HibernateCallback(){
		// @Override
		// public Object doInHibernate(Session session) throws HibernateException, SQLException {
		// return session.createSQLQuery(finalsql).addScalar("rsdaId", Hibernate.LONG).addScalar("rltyId", Hibernate.LONG).addScalar(
		// "rltyName", Hibernate.STRING).addScalar("rsdaTitle", Hibernate.STRING).addScalar("rsdaCreator", Hibernate.STRING)
		// .addScalar("rsdaPublisher", Hibernate.STRING).addScalar("rsdaDescription", Hibernate.STRING).addScalar("rsdaDate",
		// Hibernate.TIMESTAMP).addScalar("filename", Hibernate.STRING).addScalar("viewfilename", Hibernate.STRING)
		// .addScalar("imgurl", Hibernate.STRING).addScalar("remotefileserviceid", Hibernate.LONG).addScalar("countnum",
		// Hibernate.INTEGER).setFirstResult(pagination.getCurrentPage() * pagination.getPageResults()).setMaxResults(
		// pagination.getPageResults()).list();
		// }
		// });
		// return list;
		return getPageHibernateTemplate().find(pagination, hql.toString());
	}

	@Override
	public SysForgetPassword getSysForgetPassword(Long suinId) {
		String hql = "from SysForgetPassword sfpa where sfpa.sfpaSuinId = ? ";
		SysForgetPassword sysForgetPassword = null;
		List list = this.getHibernateTemplate().find(hql, suinId);
		if(list.size() > 0) {
			sysForgetPassword = (SysForgetPassword)list.get(0);
		}
		return sysForgetPassword;
	}

	@Override
	public void deleteForgetPasswordRecord() {
		List<SysForgetPassword> list = this.getHibernateTemplate().find("from SysForgetPassword");
		for(SysForgetPassword sysForgetPassword : list) {
			if(compareDate(new Date(), sysForgetPassword.getSfpaDate()) > 0) {
				this.getHibernateTemplate().delete(sysForgetPassword);
			}
		}
	}

	/**
	 * 比较两个日期大小
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	private int compareDate(Date start, Date end) {
		int result = 0;
		Long time1 = start.getTime();
		Long time2 = end.getTime();
		if(time1 > time2) {
			result = 1;
		}else if(time1 < time2) {
			result = -1;
		}
		return result;
	}

	@Override
	public List<ResTagInfo> saveSelfTag(Long rsdaId, Long suinId, String rtinTitle) {
		String hql = "from ResTagInfo r where r.rtinSuinId = " + suinId + " and r.rtinRsdaId = " + rsdaId;
		List<ResTagInfo> myExitTagList = this.getHibernateTemplate().find(hql);
		rtinTitle = rtinTitle.trim();
		if(!ServiceUtil.isNotNull(rtinTitle)) {
			throw new ErrorMessageException("resource.tag.tisi.notnull");
		}
		if(ServiceUtil.getStringRealLength(rtinTitle) > 24) {
			throw new ErrorMessageException("resource.tag.tisi.length");
		}
		// 限制同一用户对同一资源的标签数不能超过10条
		if(myExitTagList.size() >= 10){
			throw new ErrorMessageException("resource.tag.tisi.adderror.isoverten");
		}
		// 判断是否存在该标签
		for(ResTagInfo mytag : myExitTagList) {
			if(mytag.getRtinTitle().equals(rtinTitle))
				throw new ErrorMessageException("resource.tag.tisi.adderror.isexit");
		}
		ResTagInfo tagInfo = new ResTagInfo();
		tagInfo.setRtinDate(new Date());
		tagInfo.setRtinHitNum(0);
		tagInfo.setRtinRsdaId(rsdaId);
		tagInfo.setRtinSuinId(suinId);
		tagInfo.setRtinTitle(CommonUtil.hangleTextArea(rtinTitle));
		this.getHibernateTemplate().save(tagInfo);
		myExitTagList.add(tagInfo);
		return myExitTagList;
	}

	@Override
	public List<Object[]> getHotTagList() {
//	public List getHotTagList() {
//		List<String> list = (List)getHibernateTemplate().execute(new HibernateCallback(){
//			@Override
//			public Object doInHibernate(Session session) throws HibernateException, SQLException {
//				String sql = "select RTIN_TITLE rtinTitle from (select RTIN_TITLE from RES_TAG_INFO group by RTIN_TITLE"
//						+ " order by sum(RTIN_HIT_NUM) desc) where rownum <= 12";
//				List<String> list = session.createSQLQuery(sql).addScalar("rtinTitle", Hibernate.STRING).list();
//				return list;
//			}
//		});
		final String hql = "select rtin.rtinId,rtin.rtinTitle from ResTagInfo rtin";
		List<Object[]> list =(List<Object[]>)this.getHibernateTemplate().execute(new HibernateCallback(){
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createQuery(hql).setMaxResults(12).list();
			}
		});
		return list;
	}

	@Override
	public SysEmailActivation getSysEmailActivation(Long suinId, Long key) {
		String hql = "from SysEmailActivation sea where sea.seacSuinId = ? and sea.seacKey =  ? ";
		List list = this.getHibernateTemplate().find(hql, new Object[] {suinId, key});
		SysEmailActivation sysEmailActivation = null;
		if(list.size() > 0) {
			sysEmailActivation = (SysEmailActivation)list.get(0);
		}
		return sysEmailActivation;
	}

	public void updateTagInfo(HttpServletRequest request) {
		String keyWord = "";
		Long rtinId = 0l;
		try {
			keyWord = ServletRequestUtils.getStringParameter(request, "keyWord", "");
			if(!ServiceUtil.isNotNull(keyWord)) {
				keyWord = ServletRequestUtils.getStringParameter(request, "conditionValue0", "");
			}
			rtinId = ServletRequestUtils.getLongParameter(request, "rtinId", 0l);
		}catch(Exception e) {
		}
		if(ServiceUtil.isNotNull(keyWord)) {
			ResTagInfo resTagInfo = null;
			StringBuilder hql = new StringBuilder("select tagInfo from ResTagInfo tagInfo where tagInfo.rtinTitle='").append(keyWord)
					.append("'");
			if(rtinId.intValue() != 0) {
				hql.append(" and tagInfo.rtinId=").append(rtinId);
			}else {
				hql.append(" and tagInfo.rtinRsdaId=null and tagInfo.rtinSuinId=null");
			}
			List list = getHibernateTemplate().find(hql.toString());
			if(list != null && list.size() > 0) {
				resTagInfo = (ResTagInfo)list.get(0);
				resTagInfo.setRtinHitNum(resTagInfo.getRtinHitNum() + 1);
			}else {
				resTagInfo = new ResTagInfo();
				resTagInfo.setRtinDate(new Date());
				resTagInfo.setRtinTitle(keyWord);
				resTagInfo.setRtinHitNum(1);
			}
			save(resTagInfo);
		}
	}

	@Override
	public ResSourceData getPreOrNextResource(ResSourceData resource, String type) {
		String dir = type.equals(">") ? "ASC" : "DESC";
		String hql = "select new ResSourceData(" + "sourceData.rsdaId, " + "sourceData.rsdaBigimgUrl," + "sourceData.rsdaTitle,"
				+ "sourceData.rsdaGrade," + "sourceData.rsdaLooknum ) " + "from ResSourceData sourceData "
				+ "where sourceData.rsdaRmstStatus=" + ResMetaStore.STATUS_USEABLENESS + " and sourceData.rsdaStatus="
				+ ResSourceData.STATUS_PASS + " and sourceData.rsdaIsuse=" + ResSourceData.ISUSE_USEABLENESS
				+ " and sourceData.rsdaId" + type + "? and sourceData.rsdaRltyId=" + resource.getRsdaRltyId()
				+ " and sourceData.rsdaRmstId=" + resource.getRsdaRmstId() + " and sourceData.rsdaRminStatus="
				+ ResMissionInfo.RMINSTATUS_COMMIT_PASS + " order by sourceData.rsdaId " + dir;
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(0);
		pagination.setPageResults(1);
		List<ResSourceData> list = getPageHibernateTemplate().find(pagination, hql, resource.getRsdaId());
		if(list == null || list.size() < 1) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public SysUserinfo getUserinfoBySuinEmail(String suinEmail) {
		List<SysUserinfo> list = getHibernateTemplate().find("from SysUserinfo where suinEmail='" + suinEmail + "'");
		if(list.size() > 0) {
			return list.get(0);
		}else {
			return null;
		}
	}

	@Override
	public SysUserType getDefaultSysUserType() {
		List<SysUserType> list = getHibernateTemplate().find("from SysUserType sysUserType where sysUserType.sutyDefault=true");
		if(list != null && list.size() > 0) {
			return list.get(0);
		}else {
			return null;
		}
	}

	@Override
	public void updateSysTouristBroseCache() {
		logger.info("-------------------- 定时任务成功启动_每天零晨执行更新游客访问记录 ： begin " + new Date() + " ----------------------------");
		// 系统允许的最大游客缓存值
		int size = ConstantsObj.SysTouristBrowseHistoryCache.TOURIST_SIZE;
		// 系统缓存
		Map<String, Long> map = ConstantsObj.SysTouristBrowseHistoryCache.map;
		// 获取数据库中游客访问记录
		List<SysTouristBrowseCache> list = getSysTouristBrowseCacheList();
		// SQL集合
		List sqlList = new ArrayList();
		for(String key : map.keySet()) {
			int i = 0;
			// 存在的话直接更新数据
			for(SysTouristBrowseCache stbc : list) {
				if(stbc.getStbhcKey().equals(key)) {
					sqlList.add(createSysTouristBrowseCacheUpdateSql(key, map.get(key) + stbc.getStbhcValue()));
					i++;
					break;
				}
			}
			// 不存在的话，新增数据
			if(i == 0) {
				sqlList.add(createSysTouristBrowseCacheInsertSql(key, map.get(key)));
			}
		}
		// 批量执行SQL
		boolean result = executeMultiSql(sqlList);
		if(!result) {
			map.clear();
		}
		// 清理数据库的数据：将常期活动的用户访问记录保存，其它数据删除；
		List<SysTouristBrowseCache> latestList = getSysTouristBrowseCacheList();
		if(latestList.size() > size) {
			String ids = createSysTouristBrowseCacheIds(latestList, size);
			String sql = "delete from SYS_TOURIST_BROWSE_CACHE where STBHC_ID in ( " + ids + " )";
			executeUpdate(sql);
		}
		// 从新更新缓存中数据
		ConstantsObj.SysTouristBrowseHistoryCache.initSysTouristBrowseHistoryCache(commonService);
		logger.info("-------------------- 定时任务成功启动_每天零晨执行更新游客访问记录 ： end " + new Date() + " ----------------------------");
	}

	private String createSysTouristBrowseCacheInsertSql(String key, Long value) {
		return "insert into SYS_TOURIST_BROWSE_CACHE(STBHC_ID,STBHC_KEY,STBHC_VALUE) values(" + IdGenerator.generateId() + ",'" + key.replace("'", "''")
				+ "'," + value + ")";
	}

	private String createSysTouristBrowseCacheUpdateSql(String key, Long value) {
		return "update SYS_TOURIST_BROWSE_CACHE set STBHC_VALUE = " + value + " where STBHC_KEY = '" + key + "'";
	}

	private String createSysTouristBrowseCacheIds(List list, int begin) {
		String result = "";
		for(int i = begin, size = list.size(); i < size; i++) {
			SysTouristBrowseCache stbc = (SysTouristBrowseCache)list.get(i);
			result += stbc.getStbhcId();
			if(i != size - 1) {
				result += ",";
			}
		}
		return result;
	}

	private ICommonService commonService;

	public ICommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	private String downloadPath;

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
//		updateMainPageDataCache();
//		updateResSourceDataCache();
	}


	@Override
	public void saveAccessCounterListener(String ip) {
		SysAccessCounter accessCounter = new SysAccessCounter();
		accessCounter.setSacoType(SysAccessCounter.TYPE_ACCESS);
		accessCounter.setSacoSuinName(SysAccessCounter.TYPE_NO);
		accessCounter.setSacoDate(new Date());
		accessCounter.setSacoIp(ip);
		save(accessCounter);
	}

	@Override
	public void deleteTagInfo(String rtinIds) {
		String hql = "delete from ResTagInfo resTagInfo where resTagInfo.rtinId in (" + rtinIds + " )";
		this.getHibernateTemplate().bulkUpdate(hql);
	}

	@Override
	public List getCorrelationSourceDataList(Long rsdaId, Long rsdaRltyId, Long rsdaRmstId) {
		Pagination pagination = new Pagination();
		pagination.setPageResults(5);
		String correlationSourceDataHql = "select new ResSourceData(" + "sourceData.rsdaId, " + "sourceData.rsdaBigimgUrl,"
				+ "sourceData.rsdaTitle," + "sourceData.rsdaGrade," + "sourceData.rsdaLooknum, sourceData.rsdaRltyId" 
				+ " ) " + "from ResSourceData sourceData "
				+ "where sourceData.rsdaRmstStatus=" + ResMetaStore.STATUS_USEABLENESS + " and sourceData.rsdaStatus="
				+ ResSourceData.STATUS_PASS + " and sourceData.rsdaIsuse=" + ResSourceData.ISUSE_USEABLENESS + " and sourceData.rsdaId!="
				+ rsdaId + " and sourceData.rsdaRltyId=" + rsdaRltyId + " and sourceData.rsdaRmstId=" + rsdaRmstId
				+ " and sourceData.rsdaRminStatus=" + ResMissionInfo.RMINSTATUS_COMMIT_PASS + " order by sourceData.rsdaLooknum desc";
		List<ResSourceData> correlationSourceDataList = getPageHibernateTemplate().find(pagination, correlationSourceDataHql);
		return correlationSourceDataList;
	}

	@Override
	public List getSubjectHotSourceDataList(Long rsdaId, Long rsdaRstyId) {
		StringBuilder sql = new StringBuilder("select ressource.RSDA_ID,ressource.RSDA_TITLE,ressource.RSDA_FILE_TYPE");
		sql.append(" from RES_SOURCE_DATA ressource");
		sql.append(" where ressource.RSDA_RMST_STATUS=").append(ResMetaStore.STATUS_USEABLENESS);
		sql.append(" and ressource.RSDA_STATUS=").append(ResSourceData.STATUS_PASS);
		sql.append(" and ressource.RSDA_ISUSE=").append(ResSourceData.ISUSE_USEABLENESS);
		sql.append(" and ressource.RSDA_RMIN_STATUS=").append(ResMissionInfo.RMINSTATUS_COMMIT_PASS);
		if(rsdaRstyId != null){
			sql.append(" and ressource.RSDA_RSTY_ID=").append(rsdaRstyId);
		}
		sql.append(" and ressource.RSDA_ID<>").append(rsdaId);
		sql.append(" order by ressource.RSDA_LOOKNUM desc");
		final String sqlfinal = sql.toString();
		List subjectHotSourceDataList = (List)getHibernateTemplate().execute(new HibernateCallback(){
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createSQLQuery(sqlfinal).setFirstResult(0).setMaxResults(10).setFetchSize(10).setReadOnly(true).list();
			}
		});
		return subjectHotSourceDataList;
	}
	
	/**
	 * 获得网站的当前语言
	 * @author MaChao
	 * @date 2011-3-10
	 * @param request
	 * @return【对应RES_LANGUAGE_TYPE中的内置数据id:1,'简体中文';3,'英文';4,'藏文'】
	 */
	private String getCurrentLanguageId(HttpServletRequest request){
		Locale locale = (Locale)request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		if(locale == null){
			locale = request.getLocale();
		}
		if(locale != null){
			String currentLanguage = locale.getLanguage();
			if("zh".equals(currentLanguage)){ // 简体中文
				return "1";
			}else if("en".equals(currentLanguage)){ // 英文
				return "3";
			}else if("zw".equals(currentLanguage)){// 藏文
				return "4";
			}
		}
		return "";
	}
	
	/**
	 * 根据系统默认语种获得对应国际化资源文件,此后的rb对象不能通用
	 * @author MaChao
	 * @date 2011-3-11
	 * @param languageId【系统默认语种id】
	 * @param request
	 * @return
	 */
	private ResourceBundle getResourceBundleByLanguageId(Long languageId,HttpServletRequest request){
		Locale locale = null;
		switch(languageId.intValue()) {
		case 1:// 简体中文
			locale = new Locale("zh", "CN");
			break;
		case 2:// 繁体中文
			locale = new Locale("zh", "CN");
			break;
		case 3:// 英文
			locale = new Locale("en", "US");
			break;
		case 4:// 藏文
			locale = new Locale("zw", "XZ");
			break;
		default:
			Object val = request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
			locale = null;
			if(val != null){
				locale = (Locale)val;
			}else {
				locale = request.getLocale();
			}
			break;
		}
		return ResourceBundle.getBundle("messages",locale);
	}
	
	public Object getResSourceForGrade(Long rscId){
		String hql = "select avg(rsc.rscoGrade) from ResSourceComt rsc where rsc.rscoRsdaId = " + rscId.toString() + " and rsc.rscoContent is null";
		List list = getHibernateTemplate().find(hql);
		return list.get(0);
	}
	
	public boolean findRssSourceGradeOfUser(Long userId , Long rscId){
		String hql = "from ResSourceComt rsc where rsc.rscoRsdaId = " + rscId.toString() + " and rsc.rscoSuinId = " + userId.toString()
			+ " and rsc.rscoGrade is not null and rsc.rscoContent is null";
		List list = getHibernateTemplate().find(hql);
		if(list == null || list.size() == 0){
			return false;
		}else{
			return true;
		}
	}
	
	public List<InfoSubscription> getKeyWordsInfoList(Long userId){
		String hql = "from InfoSubscription iss where iss.issUserId=" + userId + " order by iss.issDate desc";
		List<InfoSubscription> list = getHibernateTemplate().find(hql);
		return list;
	}
	
	@Override
	public List<ResSourceFile> getResRourceFile(Long rsdaId, Long selfId){
		String hql = "from ResSourceFile where resRsdaId = ? and resFileAccessType='PUBLIC'";
		if(selfId != null && selfId > 0L){
			hql += " and rsfiId <> " + selfId;
		}
		hql += " order by resIndex asc";
		List<ResSourceFile> list  = getHibernateTemplate().find(hql, rsdaId);
		return list;
	}

	@Override
	public ResSourceFile getResRourceFileViewUrl(Long rsfiId) {
		ResSourceFile rsfi = (ResSourceFile)getHibernateTemplate().get(ResSourceFile.class, rsfiId);
		return rsfi;
	}

	@Override
	public List<ResSourceData> getOtherSubjectList(long rsdaId) {
//		ResSourceData rsda = (ResSourceData)getHibernateTemplate().get(ResSourceData.class, rsdaId);
//		
//		String hql = " from ResSourceData where rsdaRssuId = ? order by rsdaBuilddate";
//		List<ResSourceData> list = getHibernateTemplate().find(hql,(rsda.getRsdaRssuId()==null)? 0 : rsda.getRsdaRssuId());
//		return list;
		
		String rssuIdSql = "select rsda.rsdaRssuId from ResSourceData rsda where rsda.rsdaId=" + rsdaId;
		Long rsdaRssuId = null;
		String str = getSingleData(rssuIdSql);
		if(CommonUtil.isNotNull(str)){
			rsdaRssuId = Long.parseLong(str);
		}
		
		List<ResSourceData> list = new LinkedList<ResSourceData>();
		if(rsdaRssuId == null){
			return list;
		}
		String hql = " from ResSourceData where rsdaRssuId = ? order by rsdaBuilddate";
		list = getHibernateTemplate().find(hql,rsdaRssuId);
		return list;
	}
	
	@Override
	public List<ResSourceData> getOtherRmstList(long rmstId) {
		String hql = " from ResSourceData where resLiterType.rltyType = 2 and rsdaRmstId = ? order by rsdaBuilddate";
		List<ResSourceData> list = new LinkedList<ResSourceData>();
		list = getHibernateTemplate().find(hql,rmstId);
		return list;
	}

	@Override
	public long getNexRsda(long rsdaId,String type,String sort) {
		String rssuIdSql = "select rsda.rsdaRssuId from ResSourceData rsda where rsda.rsdaId=" + rsdaId;
		Long rsdaRssuId = null;
		String str = getSingleData(rssuIdSql);
		if(CommonUtil.isNotNull(str)){
			rsdaRssuId = Long.parseLong(str);
		}
		
		String commonWhere = PlatformBasicService.getSearchCommonConditionByHql("rsda", true);
		String hql = "select min(rsda.rsdaId) from ResSourceData rsda where rsda.rsdaId>" + rsdaId +" and " + commonWhere + " and rsda.resLiterType.rltyType=" + type;
		if(rsdaRssuId != null){
			hql += " and rsda.rsdaRssuId = " + rsdaRssuId;
		}
		
		String nextId = getSingleData(hql);
	    Long rsdaNextId = null;
	    
		if(CommonUtil.isNotNull(nextId)){
			rsdaNextId = Long.parseLong(nextId);
		}else{
			hql = "select min(rsda.rsdaId) from ResSourceData rsda where rsda.rsdaId>" + rsdaId +" and " + commonWhere + " and rsda.resLiterType.rltyType=" + type;
			
			nextId = getSingleData(hql);
			if(CommonUtil.isNotNull(nextId)){
				rsdaNextId = Long.parseLong(nextId);
			}
		}
		return rsdaNextId == null ? 0 : rsdaNextId;	
	}

	@Override
	public long getPreRsda(long rsdaId, String type, String sort) {
		String rssuIdSql = "select rsda.rsdaRssuId from ResSourceData rsda where rsda.rsdaId=" + rsdaId;
		Long rsdaRssuId = null;
		String str = getSingleData(rssuIdSql);
		if(CommonUtil.isNotNull(str)){
			rsdaRssuId = Long.parseLong(str);
		}
		
		String commonWhere = PlatformBasicService.getSearchCommonConditionByHql("rsda", true);
		String hql = "select max(rsda.rsdaId) from ResSourceData rsda where rsda.rsdaId<" + rsdaId +" and " + commonWhere + " and rsda.resLiterType.rltyType=" + type;
		if(rsdaRssuId != null){
			hql += " and rsda.rsdaRssuId = " + rsdaRssuId;
		}
		
		String preId = getSingleData(hql);
	    Long rsdaPreId = null;
		
		if(CommonUtil.isNotNull(preId)){
			rsdaPreId = Long.parseLong(preId);
		}else{
			hql = "select max(rsda.rsdaId) from ResSourceData rsda where rsda.rsdaId<" + rsdaId +" and " + commonWhere + " and rsda.resLiterType.rltyType=" + type;
			preId = getSingleData(hql);
			if(CommonUtil.isNotNull(preId)){
				rsdaPreId = Long.parseLong(preId);
			}
		}
		return rsdaPreId == null ? 0 : rsdaPreId;
	}
	


	@Override
	public List<SysLink> getSysLink() {

		String hql = " from SysLink order by slinIndex";
		
		List<SysLink> list =null;
		
		try{
			 list = this.getHibernateTemplate().find(hql);
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			list =null;
		}
		
		return list;
	}
	@Override
	public List<IndexBanner> getBannerList() {
		
		String hql = " from IndexBanner where ibIsShow=1 order by ibIndex";
		
		List<IndexBanner> list =null;
		
		try{
			list = this.getHibernateTemplate().find(hql);
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			list =null;
		}
		
		return list;
	}

	@Override
	public List<ResSubjectType> getSubjectOptions() {
		String hql = " from ResSubjectType rsty where rsty.rstyId > 0  order by rsty.rstyIndex";
		List<ResSubjectType> list =null;
		try{
			 list = this.getHibernateTemplate().find(hql);
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			list =null;
		}
		return list;
	}

	@Override
	public Map<String, Object> getResourceNum(Map<String, Object> map) {
		String hql = "select count(rsda.rsdaId) from ResSourceData rsda where rsda.rsdaStatus=3 ";
		String hql1 =hql + " and rsda.resLiterType.rltyType = 1";
		String hql2 =hql + " and rsda.resLiterType.rltyType = 2";
		String hql3 =hql + " and rsda.resLiterType.rltyType = 3";
		String hql4 =hql + " and rsda.resLiterType.rltyType = 4";
		map.put("allNum", this.getHibernateTemplate().find(hql).get(0));
		map.put("documentNum", this.getHibernateTemplate().find(hql1).get(0));
		map.put("picNum", this.getHibernateTemplate().find(hql2).get(0));
		map.put("audioNum", this.getHibernateTemplate().find(hql3).get(0));
		map.put("videoNum", this.getHibernateTemplate().find(hql4).get(0));
		return map;
	}
	
	/**
	 * 获取资源基本信息
	 *
	 * @author MaChao
	 * @date Oct 28, 2016
	 * @time 4:01:51 PM
	 */
	@Override
	public Map<String, Object> getDataDetailInfo(ResSourceData sourceData){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		final Long rsdaId = sourceData.getRsdaId();
		List<Object[]> list = (List)getHibernateTemplate().execute(new HibernateCallback(){
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer sql = new StringBuffer("select REPO_NAME as name,REPO_TITLE as title,REDA_DATA as data");
				sql.append(" from RES_ELEMENT_POOL left join RES_EXTEND_DATA on REPO_ID=REDA_REPO_ID");
				sql.append(" where REDA_RSDA_ID=").append(rsdaId).append(" and REDA_LANGUAGE=1");
//				sql.append(" and REPO_TYPE=").append(ResElementPool.REPO_TYPE_CORE); //取消只显示核心元素设置
//				sql.append(" and REDA_DATA is not null order by REDA_ID ASC");
				sql.append(" and (REPO_SHOWMODE=1 or ((REPO_SHOWMODE is null or REPO_SHOWMODE=0) and REDA_DATA is not null)) order by REDA_ID ASC");
				return session.createSQLQuery(sql.toString()).addScalar("name", Hibernate.STRING).addScalar("title", Hibernate.STRING)
						.addScalar("data", Hibernate.STRING).list();
			}
		});

		String title = "";
		String abstracts = "";
		String description = "";
		String content = "";
		String eletitle = "";
		String reference = "";
		
		//封装元数据信息
		for(Object[] o : list) {
			if(o.length < 3) {
				continue;
			}
			if(o[0] != null){
				eletitle = o[0].toString().toLowerCase();
			}
			if(o[2] != null){
				//作者和关键词要打断后加检索链接，因此不能转换成html标签
				if(!eletitle.equals("keyword") && !eletitle.equals("creator")) {
					o[2] = CommonUtil.hangleTextArea(o[2].toString());
				}
			}
			if(eletitle.equals("title")) {
//				title = "<li><span>" + rb.getString("resource.detail."+eletitle) + "：</span>" + o[2] + "</li>";
				continue;
			}
			if(eletitle.equals("abstract")) {
				abstracts = "<div class=\"more_plate\"><span>" + o[1] + "：</span>" + o[2] + "</div>";
				continue;
			}
			if(eletitle.equals("description")) {
				description = "<div class=\"more_plate\"><span>" + o[1] + "：</span>" + o[2] + "</div>";
				continue;
			}
			
//			if(eletitle.equals("keyword") || eletitle.equals("creator")) {
//				if(o[2] != null && o[2].toString().length() > 1){
//					content += "<div class=\"more_plate\"><span>" +o[1] + "：</span>";
//					String keyword = o[2].toString();
//					keyword = keyword.replace(" ", ";");
//					keyword = keyword.replace("；", ";");
//					keyword = keyword.replace(",", ";");
//					keyword = keyword.replace("，", ";");
//					String[] keys = keyword.split(";");
//					for(int i=0; i<keys.length; i++){
//						content += "<a href='javascript:void(0);' onclick='keyJump(\""+keys[i].trim()+"\");'>" + keys[i].trim() + "</a>;&nbsp";
//					}
//					content += "</div>";
//				}
//				continue;
//			}
			
			//设置来源和关联两个元素的超连接
			if(eletitle.equals("relation") || eletitle.equals("source")){
				if(o[2] != null && o[2].toString().length() > 3){
					if(o[2].toString().contains("www") || o[2].toString().contains("http")){
						if(o[2].toString().substring(0, 3).toLowerCase().equals("www")){
							reference = "<a href='http://"+o[2]+"' target='_blank'>" + o[2] + "</a>";
						}else{
							reference = "<a href='"+o[2]+"' target='_blank'>" + o[2] + "</a>";
						}
					}else{
						reference = o[2]+"";
					}
					reference = "<div class=\"more_plate\"><span>" + o[1] + "：</span>" + reference + "</div>";
					continue;
				}
			}
			content += "<li><span>" + o[1] + "：</span>" + o[2] + "</li>";
		}
		content = title + content;
		
		//封装非元数据信息
		if(sourceData.getResLiterType() != null) {
			content += "<li><span>资源类型：</span>" + sourceData.getResLiterType().getRltyName() + "</li>";
		}
		if(sourceData.getResMetaStore() != null) {
			content += "<li><span>所属资源库：</span>" + sourceData.getResMetaStore().getRmstName() + "</li>";
		}
		if(sourceData.getResSubjectType() != null) {
			content += "<li><span>所属学科：</span>" + sourceData.getResSubjectType().getRstyName() + "</li>";
		}
		if(sourceData.getResDynamicType() != null) {
			//自定义分类一
			content += "<li><span>" + sourceData.getResMetaStore().getRmstGroupOne() + "：</span>" + sourceData.getResDynamicType().getRdtyName() + "</li>";
		}
		content += "<li><span>访问量：</span>" + sourceData.getRsdaLooknum() + "</li>";
		
		// 组装最终结果
		content = "<ul class=\"info_plate\">" + content + "</ul><div class=\"clear\"></div>";
		content += abstracts;
		content += description;
		content += reference;
		resultMap.put("sourceDetailData", content);
		return resultMap;
	}
	
	/**
	 * 获取文档资源类型数据列表
	 */
    @Override
    public List<ResTextResource> getTextResList(Pagination pagination, Long rtliId, Long rlclId, String rtreTitle) {
        List<ResTextResource> list =null;
        StringBuffer fromHql = new StringBuffer("from ResTextResource rtre where rtre.rtreId > 0");
        fromHql.append(" and rtre.rtreUsed = 1 ");
        if(rtliId > 0) {
            fromHql.append(" and rtre.rtreRtliId = ").append(rtliId);
        }
        if(rlclId > 0) {
            fromHql.append(" and rtre.rtreRlclId = ").append(rlclId);
        }
        if(CommonUtil.isNotNull(rtreTitle)){
            fromHql.append(" and rtre.rtreTitle like ").append("'%"+rtreTitle+"%'");
        }
        fromHql.append(" order by rtre.rtreRecommend DESC,rtre.rtreDate DESC");
        try{
             list = getPageHibernateTemplate().find(pagination,fromHql.toString());
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            list =null;
        }
        return list;
    }

    /**
     * 获取资源分类列表
     */
    @Override
    public List<ResLibraryClassify> getResClassfiyList(Long rtliId) {
        List<ResLibraryClassify> list = null;
        String hql = "from ResLibraryClassify rlcl where rlcl.rlclRtliId=? order by rlcl.rlclIndex ASC";
        
        try{
            list = getHibernateTemplate().find(hql, rtliId);
       }catch(Exception e){
           logger.error(e.getMessage());
           e.printStackTrace();
           list =null;
       }
        return list;
    }

}
