package cn.com.adtech.riss2.bso.resource;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.bso.infosubscription.IInfoSubscriptionService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.calisSchema.MetaDataEntity;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysUserType;
import cn.com.adtech.riss2.jdbcms.SqlBasicUnit;
import cn.com.adtech.riss2.domain.resource.ResDynamicType;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceFile;
import cn.com.adtech.riss2.domain.resource.ResSourceSubject;
import cn.com.adtech.riss2.jdbcms.ToolUnit;
import cn.com.adtech.riss2.upload.IUploadService;

import com.jbetter.framework.session.Pagination;
import com.jbetter.hibernate.IdGenerator;

public class ResourceDataServiceImp extends CommonServiceImp implements IResourceDataService {
	private IUploadService localUploadService = null;

	private IUploadService remoteUploadService = null;
	
	private IInfoSubscriptionService infoSubscriptionService; 

	public IInfoSubscriptionService getInfoSubscriptionService() {
		return infoSubscriptionService;
	}

	public void setInfoSubscriptionService(IInfoSubscriptionService infoSubscriptionService) {
		this.infoSubscriptionService = infoSubscriptionService;
	}

	public void setLocalUploadService(IUploadService localUploadService) {
		this.localUploadService = localUploadService;
	}

	public void setRemoteUploadService(IUploadService remoteUploadService) {
		this.remoteUploadService = remoteUploadService;
	}

	private IUploadService uploadService;

	private String params = "";

	public IUploadService getLocalUploadService() {
		return localUploadService;
	}

	public IUploadService getRemoteUploadService() {
		return remoteUploadService;
	}

	public void setParams(String params) {
		this.params = params;
	}

	/**
	 * 获取自定义类型options下拉选项列表
	 * 
	 * @param store_id
	 *            特色库ID
	 * @return
	 */
	public String getDynamicTypeOptions(long store_id, long selectid, int rdtyType) {
		String hql = "from ResDynamicType rdt where rdt.rmstId=" + store_id + " and rdt.rdtyType=" + rdtyType;
		List<ResDynamicType> list = getHibernateTemplate().find(hql);
		if(null == list || list.size() < 1) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		ResDynamicType type = null;
		ResDynamicType two = null;
		ResDynamicType three = null;
		buffer.append("<option value=''>请选择...</option>");
		for(int i = 0, s = list.size(); i < s; i++) {
			type = list.get(i);
			if(type.getRdtyPid() == null || type.getRdtyPid() == 0) { // 一级分类
				buffer.append("<option value='" + type.getRdtyId() + "' ");
				if(selectid == type.getRdtyId()) {
					buffer.append(" selected=true ");
				}
				buffer.append(">├" + type.getRdtyName() + "</option>\n");
				for(int b = 0; b < s; b++) { // 二级分类
					two = list.get(b);
					if(two.getRdtyGrade() == 2 && two.getRdtyPid().equals(type.getRdtyId())) {
						buffer.append("<option value='" + two.getRdtyId() + "' ");
						if(selectid == two.getRdtyId()) {
							buffer.append(" selected=true ");
						}
						buffer.append(">├─" + two.getRdtyName() + "</option>\n");
						for(int c = 0; c < s; c++) {
							three = list.get(c);
							if(three.getRdtyGrade() == 3 && three.getRdtyPid().equals(two.getRdtyId())) {
								buffer.append("<option value='" + three.getRdtyId() + "' ");
								if(selectid == three.getRdtyId()) {
									buffer.append(" selected=true ");
								}
								buffer.append(">├──" + three.getRdtyName() + "</option>\n");
							}
						}
					}
				}
			}
		}
		String result = buffer.toString();
		buffer = null;
		return result;
	}

	/**
	 * 获取资源列表的JSON数据
	 * 
	 * @param userid
	 *            用户ID
	 * @param store_id
	 *            特色库ID
	 * @param sourceType
	 *            资源类型ID
	 * @param rmin_id
	 *            作业ID
	 * @param start
	 * @param limit
	 * @return
	 */
	public String getResourceListJson(long userid, long store_id, long sourceType, long rmin_id, int start, int limit) {
		String sql = "select rsd.RSDA_ID,rsd.RSDA_MIDDLENAME,rmi.RMIN_MISSNAME,rsd.RSDA_BUILDDATE,rsd.RSDA_STATUS,rsd.rsda_title,rsda_subject";
		sql += " from RES_SOURCE_DATA rsd, RES_MISSION_INFO rmi ";
		sql += " where rsd.rsda_rmin_id=rmi.rmin_id ";
		if(sourceType > 0) {
			sql += " and rsd.RSDA_RLTY_ID=" + sourceType;
		}
		if(store_id > 0) {
			sql += " and rsd.RSDA_RMST_ID=" + store_id;
		}
		if(rmin_id > 0) {
			sql += " and rsd.rsda_RMIN_ID=" + rmin_id;
		}
		String json = getListToJson(sql, null, start, limit);
		return json;
	}

	/**
	 * 获取指定作业下资源列表的JSON数据
	 * 
	 * @param userid
	 *            用户ID
	 * @param store_id
	 *            特色库ID
	 * @param sourceType
	 *            资源类型ID
	 * @param rmin_id
	 *            作业ID
	 * @param start
	 * @param limit
	 * @return
	 */
	public String getResourceListJsonByMission(long rmin_id, int start, int limit, String orderColumn, String orderType) {
		String sql = "select rsd.RSDA_ID, rsd.RSDA_MIDDLENAME,rsd.RSDA_BUILDDATE,rsd.RSDA_STATUS,rsd.rsda_title,rsd.rsda_subject,rsd.RSDA_AUDITING,rsd.RSDA_CREATOR,rsd.RSDA_PUBLISHER,rss.rssu_name from RES_SOURCE_DATA rsd left join res_source_subject rss on rsd.rsda_rssu_id=rss.rssu_id where rsd.RSDA_ISUSE=1 and rsd.rsda_RMIN_ID="
				+ rmin_id + params;
		sql += " order by " + orderColumn + " " + orderType;
		String json = getListToJson(sql, null, start, limit);
		return json;
	}

	public String getResourceListJsonByMission(long rmin_id, long languageId, int start, int limit, String sort, String dir, String title,
			int statu) {
		String sql = "select rsd.RSDA_ID, rsd.RSDA_MIDDLENAME,rsd.RSDA_BUILDDATE,rsd.RSDA_STATUS,rsd.rsda_title,rsd.rsda_subject,rsd.RSDA_AUDITING,rsl.RSLA_STATUS,rsl.RSLA_ID,rsl.RSLA_DATE,rsd.RSDA_CREATOR,rsd.RSDA_PUBLISHER from RES_SOURCE_DATA rsd ";
		sql += " left join RES_SOURCE_LANGUAGE rsl on rsd.RSDA_ID = rsl.RSLA_RSDA_ID and rsl.RSLA_RLTY_ID=" + languageId;
		sql += " where rsd.RSDA_ISUSE=1 and rsd.RSDA_RMIN_ID=" + rmin_id;
		if(title != null && title.length() > 0) {
			sql += " and rsd.RSDA_TITLE like '%" + title + "%'";
		}
		if(statu == 1) {
			sql += " and (RSLA_ID is not null and RSLA_ID>0)";
		}
		if(statu == 2) {
			sql += " and (RSLA_ID is null or RSLA_ID=0)";
		}
		sql += " order by " + sort + " " + dir;
		String json = getListToJson(sql, null, start, limit);
		return json;
	}

	/**
	 * 获取所有资源列表的JSON数据
	 * 
	 * @param userid
	 *            用户ID
	 * @param store_id
	 *            特色库ID
	 * @param sourceType
	 *            资源类型ID
	 * @param rmin_id
	 *            作业ID
	 * @param start
	 * @param limit
	 * @return
	 */
	public String getAllResourceListJson(long storeId, long sourceType, int start, int limit, String orderColumn, String orderType, String rsdaTitle) {
		return getAllResourceListJson(storeId, sourceType, start, limit, orderColumn, orderType, rsdaTitle, null,null);
	}
	public String getAllResourceListJson(long storeId, long sourceType, int start, int limit, String orderColumn, String orderType, String rsdaTitle, String beginDay, String endDay) {
		StringBuffer buffer = new StringBuffer();
		buffer
				.append("select rsd.RSDA_ID,rsd.RSDA_RLTY_ID,rmst.RMST_NAME,rlty.RLTY_NAME,rsd.RSDA_MIDDLENAME,rsd.RSDA_BUILDDATE,rsd.RSDA_STATUS,rsd.rsda_title,rsd.rsda_subject, rsd.RSDA_GRADE, rsd.RSDA_CMTSTUS, rsd.RSDA_RCMD, rsd.RSDA_ISUSE, rsd.RSDA_LOOKNUM");
		buffer.append(" from  RES_SOURCE_DATA rsd ");
		buffer.append(" left join RES_META_STORE rmst on rsd.RSDA_RMST_ID=rmst.RMST_ID ");
		buffer.append(" left join RES_LITER_TYPE rlty on rsd.RSDA_RLTY_ID=rlty.RLTY_ID");
		buffer.append(" left join RES_MISSION_INFO rmin on rmin.RMIN_ID=rsd.RSDA_RMIN_ID"); // 关联作业的目的主要是为了查询作业为通过的资源
		buffer.append(" where rmst.RMST_STATUS=1 and rsd.RSDA_STATUS=3 and (rmin.RMIN_STATUS=3 or rsd.rsda_authortype>2)"); // author的1和2是作业中录入或导入。其它都不是
		if(storeId > 0) {
			buffer.append(" and rsd.RSDA_RMST_ID=" + storeId); 
		}
		if(sourceType > 0) {
			buffer.append(" and rsd.RSDA_RLTY_ID=" + sourceType);
		}
		if(rsdaTitle != null && rsdaTitle.trim().length() > 0){
			buffer.append(" and rsd.RSDA_TITLE like '%"+ToolUnit.sqlFilter(rsdaTitle)+ "%'");
		}
		if(null != beginDay && !(beginDay.trim().equals(""))){
			buffer.append(" and rsd.RSDA_BUILDDATE >=to_date( '"+beginDay+ ":00:00:00 ','yyyy-MM-dd:HH24:mi:ss')");
		}
		if(null != endDay && !(endDay.trim().equals(""))){
			buffer.append(" and rsd.RSDA_BUILDDATE <= to_date( '"+endDay+ ":23:59:59 ','yyyy-MM-dd:HH24:mi:ss')");
		}
		buffer.append(" order by " + orderColumn + " " + orderType);
		String sql = buffer.toString();
		buffer = null;
		String json = getListToJson(sql, null, start, limit);
		return json;
	}

	/**
	 * 获取资源所有数据以及元素信息
	 * 
	 * @param store
	 *            特色库ID
	 * @param sourceType
	 *            资源类型ID
	 * @param languageId
	 *            语言ID
	 * @param resourceId
	 *            资源ID
	 * @return
	 */
	public List<ResElementPool> getResourceAllData(long store, long sourceType, long languageId, long resourceId) {
		StringBuffer buffer = new StringBuffer();
		buffer
				.append("select ele.REPO_ID, ele.repo_name,ele.repo_type, ele.repo_title, ele.repo_restrict,ele.repo_code, ele.repo_parent,red.REDA_DATA,ele.REPO_DATATYPE,ele.REPO_DATAOPTIONS ");
		buffer.append(" from");
		buffer.append(" (select rtme_reel_id from RES_TEMPLET_META where rtme_rsin_id=" + store + " and rtme_rlty_id=" + sourceType
				+ ") tmp");
		buffer.append(" left join RES_ELEMENT_POOL ele on tmp.rtme_reel_id = ele.repo_id");
		buffer.append(" left join RES_EXTEND_DATA red on red.REDA_RSDA_id=" + resourceId + " and red.REDA_LANGUAGE=" + languageId
				+ " and ele.repo_id = red.REDA_REPO_ID");
		buffer.append(" order by ele.REPO_ID ");
		String sql = buffer.toString();
		
		List<Map<String, String>> datas = getMapList(sql, null);
		if(datas == null) {
			return null;
		}
		Map<String, String> map = null;
		List<ResElementPool> elelist = new LinkedList<ResElementPool>();
		ResElementPool ele = null; 
		for(int i = 0, s = datas.size(); i < s; i++) {
			map = datas.get(i);
			ele = new ResElementPool();
			ele.setRepoId(Long.parseLong(map.get("REPO_ID")));
			ele.setRepoType(Long.parseLong(map.get("REPO_TYPE")));
			ele.setRepoName(map.get("REPO_NAME"));
			ele.setRepoTitle(map.get("REPO_TITLE"));
			ele.setRepoRestrict(map.get("REPO_RESTRICT"));
			ele.setRepoCode(map.get("REPO_CODE"));
			ele.setRepoParent(Long.parseLong(map.get("REPO_PARENT")));
			ele.setRepoData(map.get("REDA_DATA"));
			ele.setRepoDatatype(map.get("REPO_DATATYPE") == null ? 0 : Integer.parseInt(map.get("REPO_DATATYPE")));
			ele.setRepoDataOptions(map.get("REPO_DATAOPTIONS"));
			elelist.add(ele);
		}
		map = null;
		datas = null;
		return elelist;
	}

	/**
	 * 获取资源基本信息
	 * 
	 * @param sourceId
	 * @return
	 */
	public ResSourceData getResourceMetaInfo(long sourceId) {
		ResSourceData source = (ResSourceData)getHibernateTemplate().get(ResSourceData.class, sourceId);
		return source;
	}

	/**
	 * 修改资源
	 * 
	 * @param resource
	 * @return
	 */
	public boolean updateResource(ResSourceData resource, List<ResElementPool> elementList, HttpServletRequest request) {
		try {
			getHibernateTemplate().update(resource);
			String sql = "", eleid = "", data = "";
			long languageId = MyRequestUtil.getLongParameter(request, "lanId");
			List<String> sqlList = new LinkedList<String>();
			long myid;
			long sourceid = resource.getRsdaId();
			sqlList.add("delete from RES_EXTEND_DATA where REDA_RSDA_ID="+sourceid+" and REDA_LANGUAGE=" + languageId);
			for(int i = 0, s = elementList.size(); i < s; i++) {
				eleid = elementList.get(i).getRepoId().toString();
				data = MyRequestUtil.getStringParameter(request, eleid);
//				sql = "update RES_EXTEND_DATA set REDA_DATA='" + data + "' where REDA_RSDA_ID=" + resource.getRsdaId()
//						+ " and REDA_REPO_ID=" + eleid + " and REDA_LANGUAGE=" + languageId;
//				
				myid = IdGenerator.generateId();
				sql = "insert into RES_EXTEND_DATA(REDA_ID,REDA_REPO_ID,REDA_RSDA_ID,REDA_DATA,REDA_LANGUAGE) values(" + myid + "," + eleid	+ "," + sourceid + ",'" + data.replace("'", "''") + "'," + languageId + ")";
				sqlList.add(sql);
			}
			executeMultiSql(sqlList);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除资源
	 * 
	 * @param resourceId
	 *            资源ID
	 * @param languageId
	 *            语种ID 如果该资源只有一种语种，则删除基本信息和其它相应信息，如果不只一种语种的信息，则不删除资源基本信息
	 * @return
	 */
	public boolean deleteResource(long resourceId, long languageId, HttpServletRequest request) {
		// long lans = BasicDao.getCountNumber("RES_SOURCE_LANGUAGE", "RSLA_ID",
		// " RSLA_RSDA_ID=" + resourceId);
		String count_hql = "select count(rsl.rslaId) from ResSourceLanguage rsl where rsl.resSourceId=" + resourceId;
		long lans = getCountNumber(count_hql);
		int len = lans < 2 ? 4 : 3;
		//String[] sql = new String[len];
		List<String> sqllist = new LinkedList<String>();
		sqllist.add("delete from RES_EXTEND_DATA where REDA_RSDA_ID=" + resourceId);
		sqllist.add("delete from RES_SOURCE_LANGUAGE where RSLA_RSDA_ID=" + resourceId);
		sqllist.add("delete from RES_SOURCE_REMARK where RSRE_RSDA_ID=" + resourceId);
		
//		sql[0] = "delete from RES_EXTEND_DATA where REDA_RSDA_ID=" + resourceId;
//		sql[1] = "delete from RES_SOURCE_LANGUAGE where RSLA_RSDA_ID=" + resourceId;
//		sql[2] = "delete from RES_SOURCE_REMARK where RSRE_RSDA_ID=" + resourceId;
		ResSourceData source = null;
		if(lans < 2) {
			//sql[3] = "delete from RES_SOURCE_DATA where RSDA_ID = " + resourceId;
			sqllist.add("delete from RES_SOURCE_DATA where RSDA_ID = " + resourceId);
			sqllist.add("delete from RES_SOURCE_COMT where RSCO_RSDA_ID=" + resourceId);
			sqllist.add("delete from SC_FAVORITES where SCF_RES_SOURCE_ID=" + resourceId);
			sqllist.add("delete from RES_TAG_INFO where RTIN_RSDA_ID=" + resourceId);
			sqllist.add("delete from RES_SOURCE_BROWSE_HISTOREY where RSBH_RSDA_ID=" + resourceId);
			sqllist.add("delete from RES_SOURCE_DATA_CACHE where RSDC_ID=" + resourceId);
			source = (ResSourceData)getHibernateTemplate().get(ResSourceData.class, resourceId);
			if(source != null){
				uploadService = getUploadServiceById(source.getResMetaStore().getRmstRfilId());
			}
		}
		
		
		boolean b = executeMultiSql(sqllist);
		// 删除资源相关文件
		if(b && source != null) {
			// 删原文件
			List<String> fileList = null;
			String comName = source.getRsdaFileLocalName();
			if(ServiceUtil.isNotNull(comName)) {
				uploadService.removeFile(comName);
				comName = comName.substring(0, comName.lastIndexOf("_") + 1);
				fileList = new LinkedList<String>();
				fileList.add(comName + "data.flv");
				fileList.add(comName + "data.swf");
				fileList.add(comName + "data.mp3");
				fileList.add(comName + "c.flv");
				fileList.add(comName + "c.swf");
				fileList.add(comName + "c.mp3");
				uploadService.removeFileList(fileList);
			}
			// 删封面
			uploadService.removeFile(source.getRsdaBigimgLocalName());
			comName = source.getRsdaBigimgLocalName();
			if(ServiceUtil.isNotNull(comName)) {
				comName = comName.substring(0, comName.lastIndexOf("_") + 1);
				uploadService.removeFile(comName + "jpg.jpeg");
			}
			
			String hql = "from ResSourceFile rsfi where rsfi.resRsdaId=?";
			List<ResSourceFile> sourcefile = this.getHibernateTemplate().find(hql, resourceId);
			if(sourcefile != null && sourcefile.size() > 0){
				fileList = new ArrayList<String>();
				for(int i=0,s=sourcefile.size(); i<s; i++){
					if(CommonUtil.isNotNull(sourcefile.get(i).getResLocalPath())){
						String filename = sourcefile.get(i).getResLocalPath();
						filename = filename.substring(0, filename.lastIndexOf("."));
						fileList.add(sourcefile.get(i).getResLocalPath());
						fileList.add(filename + ".flv");
						fileList.add(filename + ".jpg");
						fileList.add(filename + ".mp3");
						fileList.add(filename + ".swf");
					}
				}
				uploadService.removeFileList(fileList);
				this.getHibernateTemplate().deleteAll(sourcefile);
			}
			
			if(source.getRsdaRminId() != null){
				String updateHql = "update ResMissionInfo rmin set rmin.rminResCount=rmin.rminResCount-1 where rmin.rminId=?";
				getHibernateTemplate().bulkUpdate(updateHql, source.getRsdaRminId());
			}
		}
		return b;
	}

	/**
	 * 删除资源下的具体某种语种的数据
	 * 
	 * @param sourceId
	 * @param languageId
	 * @return
	 */
	public boolean deleteResourceByLanguage(long sourceId, long languageId) {
		String[] sql = new String[2];
		sql[0] = "delete from RES_EXTEND_DATA where REDA_RSDA_ID=" + sourceId + " and REDA_LANGUAGE=" + languageId;
		sql[1] = "delete from RES_SOURCE_LANGUAGE where RSLA_RSDA_ID=" + sourceId + " and RSLA_RLTY_ID=" + languageId;
		boolean b = executeMultiSql(sql);
		return b;
	}

	/**
	 * 删除整个资源
	 * 
	 * @param resourceId
	 * @param request
	 * @return
	 */
	public boolean deleteResource(long resourceId, HttpServletRequest request) {
		String[] sql = new String[3];
		sql[0] = "delete from RES_EXTEND_DATA where REDA_RSDA_ID=" + resourceId;
		sql[1] = "delete from RES_SOURCE_LANGUAGE where RSLA_RSDA_ID=" + resourceId;
		sql[2] = "delete from RES_SOURCE_DATA where RSDA_ID = " + resourceId;
		
		List<String> sqllist = new ArrayList<String>();
		sqllist.add("delete from RES_EXTEND_DATA where REDA_RSDA_ID=" + resourceId);
		sqllist.add("delete from RES_SOURCE_LANGUAGE where RSLA_RSDA_ID=" + resourceId);
		sqllist.add("delete from RES_SOURCE_REMARK where RSRE_RSDA_ID=" + resourceId);
		
		sqllist.add("delete from RES_SOURCE_COMT where RSCO_RSDA_ID=" + resourceId);
		sqllist.add("delete from SC_FAVORITES where SCF_RES_SOURCE_ID=" + resourceId);
		sqllist.add("delete from RES_TAG_INFO where RTIN_RSDA_ID=" + resourceId);
		sqllist.add("delete from RES_SOURCE_BROWSE_HISTOREY where RSBH_RSDA_ID=" + resourceId);
		sqllist.add("delete from RES_SOURCE_DATA_CACHE where RSDC_ID=" + resourceId);
		sqllist.add("delete from RES_SOURCE_DATA where RSDA_ID = " + resourceId);
		
		ResSourceData source = (ResSourceData)getHibernateTemplate().get(ResSourceData.class, resourceId);
		boolean b = executeMultiSql(sql);
		// 删除资源相关文件
		if(b && source != null) {
			this.uploadService = this.getUploadServiceById(source.getResMetaStore().getRmstRfilId());
			
			// 删原文件
			List<String> fileList = null;
			String comName = source.getRsdaFileLocalName();
			
			try {
				//删除附件目录
				if(CommonUtil.isNotNull(comName)){
					String rootPath = SysConfigInfo.RES_DIR;
					String att = comName.split("_data_")[0];
					File arrDir = new File(rootPath + "/" + att);
					FileUtils.deleteDirectory(arrDir);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(ServiceUtil.isNotNull(comName)) {
				uploadService.removeFile(comName);
				comName = comName.substring(0, comName.lastIndexOf("_") + 1);
				fileList = new LinkedList<String>();
				fileList.add(comName + "data.flv");
				fileList.add(comName + "data.swf");
				fileList.add(comName + "data.mp3");
				fileList.add(comName + "c.flv");
				fileList.add(comName + "c.swf");
				fileList.add(comName + "c.mp3");
				uploadService.removeFileList(fileList);
			}
			// 删封面
			uploadService.removeFile(source.getRsdaBigimgLocalName());
			comName = source.getRsdaBigimgLocalName();
			if(ServiceUtil.isNotNull(comName)) {
				comName = comName.substring(0, comName.lastIndexOf("_") + 1);
				uploadService.removeFile(comName + "jpg.jpeg");
			}
			
			//删除资源附件及相关文件
			String hql = "from ResSourceFile rsfi where rsfi.resRsdaId=?";
			List<ResSourceFile> sourcefile = this.getHibernateTemplate().find(hql, resourceId);
			if(sourcefile != null && sourcefile.size() > 0){
				fileList = new ArrayList<String>();
				for(int i=0,s=sourcefile.size(); i<s; i++){
					if(CommonUtil.isNotNull(sourcefile.get(i).getResLocalPath())){
						String filename = sourcefile.get(i).getResLocalPath();
						filename = filename.substring(0, filename.lastIndexOf("."));
						fileList.add(sourcefile.get(i).getResLocalPath());
						fileList.add(filename + ".flv");
						fileList.add(filename + ".jpg");
						fileList.add(filename + ".mp3");
						fileList.add(filename + ".swf");
					}
				}
				uploadService.removeFileList(fileList);
				this.getHibernateTemplate().deleteAll(sourcefile);
			}
			
			if(source.getRsdaRminId() != null){
				String updateHql = "update ResMissionInfo rmin set rmin.rminResCount=rmin.rminResCount-1 where rmin.rminId=? and rmin.rminResCount >=1";
				getHibernateTemplate().bulkUpdate(updateHql, source.getRsdaRminId());
			}
		}
		
		return b;
	}

	/**
	 * 设置资源评论状态
	 * 
	 * @param idlist
	 * @param statu
	 * @return
	 */
	public boolean updateCommentStatus(String idlist) {
		if(idlist == null || idlist.length() < 1) {
			return true;
		}
		String sql = "update RES_SOURCE_DATA set RSDA_CMTSTUS= (case when RSDA_CMTSTUS=1 then 0 else 1 end) where RSDA_ID in (" + idlist
				+ ")";
		boolean b = executeUpdate(sql);
		return b;
	}

	/**
	 * 设置资源显示状态
	 * 
	 * @param idlist
	 * @param statu
	 * @return
	 */
	public boolean updateUseStatus(String idlist) {
		if(idlist == null || idlist.length() < 1) {
			return true;
		}
		String sql = "update RES_SOURCE_DATA set RSDA_ISUSE= (case when RSDA_ISUSE=1 then 0 else 1 end) where RSDA_ID in (" + idlist + ")";
		boolean b = executeUpdate(sql);
		return b;
	}

	/**
	 * 设置推荐资源状态
	 * 
	 * @param idlist
	 * @param statu
	 * @return
	 */
	public boolean updateRcmdStatus(String idlist) {
		if(idlist == null || idlist.length() < 1) {
			return true;
		}
		String sql = "update RES_SOURCE_DATA set RSDA_RCMD=(case when RSDA_RCMD=1 then 0 else 1 end) where RSDA_ID in (" + idlist + ")";
		boolean b = executeUpdate(sql);
		return b;
	}

	/**
	 * 根据特色库ID获取该特色库文件存放盘符
	 * 
	 * @return
	 */
	public String getStoreFolder(long storeId) {
		String data = "select rmstFolder from ResMetaStore where rmstId = ?";
		List list = getHibernateTemplate().find(data, storeId);
		if(list != null && list.size() > 0) {
			if(list.get(0) != null) {
				data = list.get(0).toString();
			}else {
				data = "";
			}
			list = null;
			return data;
		}
		return "";
	}

	/**
	 * 根据资源ID获取所属作业ID
	 * 
	 * @return
	 */
	public long getSourceRminId(long sourceId) {
		String sql = "select RSDA_RMIN_ID from RES_SOURCE_DATA where RSDA_ID =" + sourceId;
		long id = ToolUnit.StrToLong(getFirstData(sql), 0);
		return id;
	}

	public String getSubjectOptions(final long selected, final long rootId, final String type) {
		final String sql = SqlBasicUnit.subjectHierarchySql();
		String options = getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer buffer = new StringBuffer();
				try {
					Connection conn = session.connection();
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql);
					buffer.append("<option value=''>请选择...</option>");
					String icon = "├";
					while(rs.next()) {
						int level = rs.getInt("level");
						if(level == 1) {
							if("1".equals(type))
								continue;
						}
						for(int i = 0; i < level - 2; i++) {
							icon += "─";
						}
						buffer.append("<option value='" + rs.getString("RSTY_ID") + "'");
						if(rs.getLong("RSTY_ID") == selected) {
							buffer.append(" selected='true' ");
						}
						buffer.append(">" + icon + rs.getString("RSTY_NAME") + "</option>\n");
						icon = "├";
					}
					try {
						if(rs != null)
							rs.close();
						rs = null;
						stmt = null;
					}catch(SQLException e) {
						e.printStackTrace();
					}
				}catch(SQLException e) {
					e.printStackTrace();
					System.out.println("在getOptions中发生异常，异常SQL：" + sql);
					return "";
				}
				String options = buffer.toString();
				buffer = null;
				return options;
			}
		}).toString();
		return options;
	}

	public IUploadService getUploadServiceById(Long rfilId) {
		if(rfilId == null || rfilId.equals(0)) {
			return localUploadService;
		}else {
			return remoteUploadService;
		}
	}

	@Override
	public List<ResSourceData> getFeedResSourceDatas(int resultCount) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select rsda from ResSourceData rsda");
		buffer.append(" where rsda.rsdaStatus=").append(ResSourceData.STATUS_PASS);
		buffer.append(" and rsda.rsdaIsuse=").append(ResSourceData.ISUSE_USEABLENESS);
		buffer.append(" and rsda.rsdaRmstStatus=").append(ResMetaStore.STATUS_USEABLENESS);
//		buffer.append(" and (rsda.rsdaRights is null or rsda.rsdaRights<2)");
		buffer.append(" and (rsda.rsdaRminStatus=").append(ResMissionInfo.RMINSTATUS_COMMIT_PASS);
		buffer.append(" or rsda.rsdaAuthortype >").append(ResSourceData.AUTHORTYPE_ADMIN_IMPORT).append(")");
		buffer.append(" order by rsda.rsdaBuilddate desc");
		
		String hql = buffer.toString();
		buffer = null;
		
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(0);
		pagination.setPageResults(resultCount);
		return getPageHibernateTemplate().find(pagination, hql);
	}

	@Override
	public List<ResSourceData> getResSourceDatasByKeyWords(Long userId, String[] keyWords, Pagination pagination) {
		StringBuilder hql = new StringBuilder("select res from ResSourceData res where (");
		List<ResSourceData> ret = new ArrayList<ResSourceData>();
		if(keyWords != null && keyWords.length > 0) {
			Object[] params = null;
			String orClause = "";
			params = new Object[keyWords.length * 3 + 1];
			for(int i = 0; i < keyWords.length; i++) {
				Object[] p = {"%"+keyWords[i]+"%", "%"+keyWords[i]+"%", "%"+keyWords[i]+"%"};
				System.arraycopy(p, 0, params, i * 3, 3);
				orClause += "res.rsdaTitle like ? or res.rsdaSubject like ? or res.rsdaDescription like ? or ";
			}
			orClause = orClause.substring(0, orClause.lastIndexOf("or"));
			orClause += ")";
			hql.append(orClause);
			params[params.length - 1] = userId;
			hql.append(" and (not exists ( select iir from IsIgnoreResSource iir where iir.isInRsUserId=? and iir.isInRsSourceId=res.rsdaId ))");
			hql.append(" and res.rsdaStatus=" + ResSourceData.STATUS_PASS).append(" and res.rsdaIsuse=" + ResSourceData.ISUSE_USEABLENESS).
				append(" and res.resMetaStore.rmstStatus=" + ResMetaStore.STATUS_USEABLENESS).
				append(" and ((res.resMissionInfo is not null and res.resMissionInfo.rminStatus=").
				append(ResMissionInfo.RMINSTATUS_COMMIT_PASS).append(")").append(" or (res.rsdaAuthortype=").
				append(ResSourceData.AUTHORTYPE_MEMBER_INPUT).append("))");
			hql.append(" order by res.rsdaBuilddate desc");
			ret = this.getPageHibernateTemplate().find(pagination, hql.toString(), params);
		}
		return ret;
	}

	@Override
	public boolean saveOrUpdateSubject(ResSourceSubject rssu) {
		boolean b = false;
		try{
			if(rssu.getRssuId() == null || rssu.getRssuId() < 1l ){
				getHibernateTemplate().save(rssu);
			}else{
				getHibernateTemplate().update(rssu);
			}
			b = true;
		}catch(Exception e){
			b = false;
		}
		return b;
	}

	@Override
	public String getResourceSubjectJson( int start, int limit,
			long rssuRminId, String orderColumn, String orderType) {
		
		String sql = "select rssu_Id,rssu_Name,rssu_Rmin_Id,rssu_Remark from Res_Source_Subject where rssu_Rmin_Id = "+rssuRminId;
		
		
		sql += " order by " + orderColumn + " " + orderType;
		
		String json = getListToJson(sql, null, start, limit);
		return json;
	}

	@Override
	public boolean deleteSubject(long rssuId) {
		try{
			String hql = "from ResSourceData where rsdaRssuId = ? ";
			List list = getHibernateTemplate().find(hql,rssuId);
			if(list.size()>0){
				return false;
			}else{
				ResSourceSubject rssu = (ResSourceSubject)getHibernateTemplate().get(ResSourceSubject.class, rssuId);
				getHibernateTemplate().delete(rssu);
				return true;
			}
		}catch(Exception e){
			return false;
		}	
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getSourceSubjectOptions(long rminId, long selectId) {
		String hql = " from ResSourceSubject where rssuRminId = ? ";
		@SuppressWarnings("unused")
		List<ResSourceSubject> rssuList = getHibernateTemplate().find(hql, rminId); 
		StringBuffer buffer = new StringBuffer();
		buffer.append("<option value=''>请选择...</option>");
		
		for(ResSourceSubject r : rssuList){
			buffer.append("<option value='"+r.getRssuId()+"'");
			if(selectId == r.getRssuId()){
				buffer.append(" selected=true");
			}
			buffer.append(">"+r.getRssuName()+"</option>\n");
		}
		
		return buffer.toString();
	}

	@Override
	public String getSourceRightsOptions(long selectId) {
		String hql = " from SysUserType";
		@SuppressWarnings("unused")
		List<SysUserType> rssuList = getHibernateTemplate().find(hql); 
		StringBuffer buffer = new StringBuffer();
		
		for(SysUserType r : rssuList){
			buffer.append("<option value='"+r.getSutyId()+"'");
			if(selectId == r.getSutyId()){
				buffer.append(" selected=true");
			}
			buffer.append(">"+r.getSutyName()+"</option>\n");
		}
		
		return buffer.toString();
	}
	
	@Override
	public List<MetaDataEntity> getSourceToList(Long rsdaId){
		if(rsdaId.equals(0l)){
			return new ArrayList<MetaDataEntity>();
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("select ele.REPO_ID, ele.repo_name,ele.repo_type, ele.repo_title, ele.repo_restrict,ele.repo_code, ele.repo_parent,red.REDA_DATA ");
		buffer.append(" from");
		buffer.append(" res_element_pool ele");
		buffer.append(" left join res_extend_data red on ele.repo_id=red.reda_repo_id ");
		buffer.append(" and red.reda_rsda_id=").append(rsdaId);
		buffer.append(" where ele.repo_rlty_id=").append(getFirstData("select rsda_rlty_id from res_source_data where rsda_id=" + rsdaId));
		//20120330添加calis导出元数据根据数据库收割选项判断 1为
		buffer.append(" and ele.REPO_CALIS = 1");
		buffer.append(" order by ele.REPO_ID ");
		String sql = buffer.toString();
		
		List<Map<String, String>> datas = getMapList(sql, null);
		if(datas == null) {
			return null;
		}
		Map<String, String> map = null;
		List<ResElementPool> elelist = new LinkedList<ResElementPool>();
		ResElementPool ele = null;
		for(int i = 0, s = datas.size(); i < s; i++) {
			map = datas.get(i);
			ele = new ResElementPool();
			ele.setRepoId(Long.parseLong(map.get("REPO_ID")));
			ele.setRepoType(Long.parseLong(map.get("REPO_TYPE")));
			ele.setRepoName(map.get("REPO_NAME"));
			ele.setRepoTitle(map.get("REPO_TITLE"));
			ele.setRepoRestrict(map.get("REPO_RESTRICT"));
			ele.setRepoCode(map.get("REPO_CODE"));
			ele.setRepoParent(Long.parseLong(map.get("REPO_PARENT")));
			ele.setRepoData(map.get("REDA_DATA"));
			elelist.add(ele);
		}
		map = null;
		datas = null;
		List<MetaDataEntity> metaDataEntityList = new ArrayList<MetaDataEntity>();
		
		for(ResElementPool data : elelist){
			//查看是否是父级元素
			if(data.getRepoType().equals(1l)||data.getRepoType().equals(2l)){
				MetaDataEntity metaDataEntity = new MetaDataEntity();
				metaDataEntity.setName(data.getRepoName());
				metaDataEntity.setValue(data.getRepoData()==null?"":data.getRepoData());
				List<MetaDataEntity> childlist = new ArrayList<MetaDataEntity>();
				//遍历元素，查找子集
				for(ResElementPool childdata : elelist){
					//判断是否是子集
					if(childdata.getRepoParent().equals(data.getRepoId())){
						//子集对象
						MetaDataEntity childmetaDataEntity = new MetaDataEntity();
						childmetaDataEntity.setName(childdata.getRepoName());
						childmetaDataEntity.setValue(childdata.getRepoData()==null?"":childdata.getRepoData());
						//访问权限的判断
						if(childdata.getRepoName()!=null&&childdata.getRepoName().equals("AccessRights")&&childdata.getRepoData()!=null){
							switch (Integer.parseInt(childdata.getRepoData())) {
							case 1:
								childmetaDataEntity.setValue("不可访问");
								break;
							case 2:
								childmetaDataEntity.setValue("不可预览");
								break;
							case 3:
								childmetaDataEntity.setValue("可浏览预览版");
								break;
							case 4:
								childmetaDataEntity.setValue("可浏览源文");
								break;
							case 5:
								childmetaDataEntity.setValue("可下载");
								break;
							default: 
								break;
							}
						}
						
						//判断子集元素是否有值
							childlist.add(childmetaDataEntity);
						
					}
					
				} 
					metaDataEntity.setList(childlist);
					metaDataEntityList.add(metaDataEntity);
			}
		}
		return metaDataEntityList;
	}
	 
	@Override
	public String[] getResIdToAudit(Long storeId , Long sourceType , String rsdaTitle , String beginDay , String endDay) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select rsd.RSDA_ID ");
		buffer.append(" from  RES_SOURCE_DATA rsd ");
		buffer.append(" left join RES_META_STORE rmst on rsd.RSDA_RMST_ID=rmst.RMST_ID ");
		buffer.append(" left join RES_LITER_TYPE rlty on rsd.RSDA_RLTY_ID=rlty.RLTY_ID");
		buffer.append(" left join RES_MISSION_INFO rmin on rmin.RMIN_ID=rsd.RSDA_RMIN_ID"); // 关联作业的目的主要是为了查询作业为通过的资源
		buffer.append(" where rmst.RMST_STATUS=1 and rsd.RSDA_STATUS=3 and (rmin.RMIN_STATUS=3 or rsd.rsda_authortype>2)"); // author的1和2是作业中录入或导入。其它都不是
		if(storeId > 0) {
			buffer.append(" and rsd.RSDA_RMST_ID=" + storeId); 
		}
		if(sourceType > 0) {
			buffer.append(" and rsd.RSDA_RLTY_ID=" + sourceType);
		}
		if(rsdaTitle != null && rsdaTitle.trim().length() > 0){
			buffer.append(" and rsd.RSDA_TITLE like '%"+ToolUnit.sqlFilter(rsdaTitle)+ "%'");
		}
		if(null != beginDay && beginDay.trim().length() > 0){
			buffer.append(" and rsd.RSDA_BUILDDATE >=to_date( '"+beginDay+ ":00:00:00 ','yyyy-MM-dd:HH24:mi:ss')");
		}
		if(null != endDay && endDay.trim().length() > 0){
			buffer.append(" and rsd.RSDA_BUILDDATE <= to_date( '"+endDay+ ":23:59:59 ','yyyy-MM-dd:HH24:mi:ss')");
		}
		
		
		List<Object> list = getSession().createSQLQuery(buffer.toString()).list();
		String[] strArr = new String[list.size()];
		for(int i = 0,size = list.size();i < size ; i++){
			strArr[i] = String.valueOf(list.get(i));
		}
		return  strArr;
	} 
	
	@Override
	public Boolean saveLibrary(String library) {
		try {
			String librarytitle = MmSettingConstant.getRiss_library() ;
			getHibernateTemplate().bulkUpdate("update MmSetting sysm set sysm.msetValue='" + library + "' where sysm.msetTitle = '"+librarytitle+"'");
			ConstantsObj.setSysProperty(librarytitle,library);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 判断该资源是否可被搜索到
	 * @param rsdaId
	 * @return
	 */
	public Boolean getResAllStatusPass(Long rsdaId){
		ResSourceData source = (ResSourceData) get(ResSourceData.class, rsdaId);
		if(source.getResMetaStore().getRmstStatus()==ResMetaStore.STATUS_USEABLENESS
				&&source.getRsdaStatus()==source.STATUS_PASS
				&&(source.getResMissionInfo().getRminStatus()==ResMissionInfo.RMINSTATUS_COMMIT_PASS
						||source.getRsdaAuthortype()>source.AUTHORTYPE_ADMIN_INPUT))
			return true;
		return false;
	}

	/**
	 * 获取导出excel的资源元数据信息
	 *
	 * @author MaChao
	 * @date 2013-12-5
	 *
	 * @param params
	 * @return
	 */
	@Override
	public List<Object[]> getExportResource(Map<String, Object> params) {
//		String commonWhere = IndexCacheBasicService.getSearchCommonConditionByHql("rsda");
		String commonWhere = "rsda.rsdaStatus=" + ResSourceData.STATUS_PASS;
		
		//基本条件——资源所属特色库是否可用状态为可用
		commonWhere += " and rsda.rsdaRmstStatus=" + ResMetaStore.STATUS_USEABLENESS;
		
		//基本条件——资源所属作业为审核通过或资源来源类型大于2(导入资源)即不属于系统加工资源
		commonWhere += " and (rsda.rsdaRminStatus=" + ResMissionInfo.RMINSTATUS_COMMIT_PASS;
		commonWhere += " or rsda.rsdaAuthortype>" + ResSourceData.AUTHORTYPE_ADMIN_IMPORT + ")";
		
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("select rsda.resMetaStore.rmstName,");
		buffer.append("rsda.resLiterType.rltyName,");
		buffer.append("rsda.rsdaTitle,");
		buffer.append("rsda.rsdaSubject,");
		buffer.append("rsda.rsdaDescription,");
		buffer.append("rsda.rsdaSource,");
		buffer.append("rsda.rsdaLanguage,");
		buffer.append("rsda.rsdaRelation,");
		buffer.append("rsda.rsdaCoverage,");
		buffer.append("rsda.rsdaCreator,");
		buffer.append("rsda.rsdaPublisher,");
		buffer.append("rsda.rsdaContributor,");
		buffer.append("rsda.rsdaRights,");
		buffer.append("rsda.rsdaDate,");
		buffer.append("rsda.rsdaType,");
		buffer.append("rsda.rsdaFormat,");
		buffer.append("rsda.rsdaIdentifier");
		buffer.append(" from ResSourceData rsda");
		buffer.append(" where ").append(commonWhere);
		
		String rsdaTitle = null;
		if(CommonUtil.isNotNull(params.get("selectIds").toString())){
			buffer.append(" and rsda.rsdaId in (").append(params.get("selectIds").toString()).append(")");
		}else{
			if(params.containsKey("rmstId")){
				buffer.append(" and rsda.rsdaRmstId=").append(params.get("rmstId"));
			}
			if(params.containsKey("rltyId")){
				buffer.append(" and rsda.rsdaRltyId=").append(params.get("rltyId"));
			}
			if(params.containsKey("rsdaTitle")){
				buffer.append(" and rsda.rsdaTitle like ? ");
				rsdaTitle = params.get("rsdaTitle").toString();
			}
		}
		
		buffer.append(" order by rsda.").append(params.get("sort")).append(" ").append(params.get("dir"));
		
		List<Object[]> list = null;
		String hql = buffer.toString();
		buffer = null;
		if(CommonUtil.isNotNull(rsdaTitle)){
			list = getHibernateTemplate().find(hql, "%" + rsdaTitle + "%");
		}else{
			list = getHibernateTemplate().find(hql);
		}
		return list;
	}
	
	@Override
	public void deleteResSourceCatalogue(Long rsdaId) {
		String hql = "delete from ResSourceCatalogue rscl where  rscl.rsclRsdaId= " + rsdaId;
		getHibernateTemplate().bulkUpdate(hql);
	}

	@Override
	public void deleteResSourcePic(Long rsdaId) {
		String hql = "delete from ResSourcePic rspc where  rspc.rspcRsdaId= " + rsdaId;
		getHibernateTemplate().bulkUpdate(hql);
	}
	
	@Override
	public void deleteComt(Long rsdaId) {
		String hql = "delete from ResSourceComt rsco where rsco.rscoRsdaId="+rsdaId;
		this.getHibernateTemplate().bulkUpdate(hql);
	}
}
