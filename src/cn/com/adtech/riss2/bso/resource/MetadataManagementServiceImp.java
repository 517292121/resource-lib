package cn.com.adtech.riss2.bso.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.cache.PlatformCacheFactory;
import cn.com.adtech.riss2.controller.platform.rss.RssSubscribeToMyRssResourceController;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResStoreLiter;
import cn.com.adtech.riss2.domain.resource.ResSubjectType;
import cn.com.adtech.riss2.domain.rss.RssUserData;
import cn.com.adtech.riss2.jdbcms.SqlBasicUnit;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class MetadataManagementServiceImp extends CommonServiceImp implements IMetadataManagementService {
	
	@Override
	public List getResMetaStoreList(Pagination pagination, Map<String, Object> conditions, int rmpeType, long userId) {
		List<Object> param = new ArrayList<Object>();
		String selectHql = "Select rms.rmstId,rms.sysManager.smanName,rms.rmstName,rms.rmstCompany,rms.rmstBuilddate,rms.rmstStatus,rms.rmstFolder,rms.rmstIndexSort,rms.rmstGroupOne,rms.rmstGroupTwo,rms.rmstGroupThree, rms.rmstLanguage ";
		StringBuffer fromHql = new StringBuffer("From ResMetaStore rms, ResMetaPerm rmp where rms.rmstId=rmp.rmstId and rmp.rmpeType="
				+ rmpeType + " and rmp.smanId= " + userId + " ");
		StringBuffer orderHql = new StringBuffer("");
		// 添加查询条件
		String sort = (String)conditions.get("sort");
		String dir = (String)conditions.get("dir");
		// 添加排序方式
		if(sort != null && sort.length() > 0 && dir != null && dir.length() > 0) {
			if("smanName".equals(sort))
				orderHql.append(" order by rms.sysManager.smanName ").append(dir);
			else
				orderHql.append(" order by rms.").append(sort).append(" ").append(dir);
		}
		String countHql = "select count(*) " + fromHql.toString();
		String hql = selectHql + fromHql.toString() + orderHql.toString();
		return getPageHibernateTemplate().find(pagination, hql, countHql, param.toArray());
	}

	public List getResMetaStoreListByRmpeType(Pagination pagination, Map<String, Object> conditions) {
		List<Object> param = new ArrayList<Object>();
		String selectHql = "select rmp.rmstId,rmp.sysManager.smanName,rmp.resMetaStore.rmstName,rmp.resMetaStore.rmstCompany,rmp.resMetaStore.rmstBuilddate,rmp.resMetaStore.rmstStatus ";
		StringBuffer fromHql = new StringBuffer("from ResMetaPerm rmp where rmp.rmpeType = ? and rmp.smanId = ? ");
		StringBuffer orderHql = new StringBuffer("");
		Long rmpeType = (Long)conditions.get("rmpeType");
		Long smanId = (Long)conditions.get("smanId");
		param.add(rmpeType);
		param.add(smanId);
		// 查询条件
		List rmstNamesList = (List)conditions.get("rmstNamesList");
		if(rmstNamesList != null) {
			int len = rmstNamesList.size();
			if(len > 0) {
				fromHql.append(" and ( ");
				for(int i = 0; i < len; i++) {
					fromHql.append("  rmp.resMetaStore.rmstName like ? ");
					if(i != len - 1) {
						fromHql.append(" or ");
					}
					param.add("%" + ((List)rmstNamesList).get(i) + "%");
				}
				fromHql.append(" ) ");
			}
		}
		String sort = (String)conditions.get("sort");
		String dir = (String)conditions.get("dir");
		// 添加排序方式
		if(sort != null && sort.length() > 0 && dir != null && dir.length() > 0) {
			if("smanName".equals(sort))
				orderHql.append(" order by rmp.sysManager.smanName ").append(dir);
			else
				orderHql.append(" order by rmp.resMetaStore.").append(sort).append(" ").append(dir);
		}
		String countHql = "select count(*) " + fromHql.toString();
		String hql = selectHql + fromHql.toString() + orderHql.toString();
		return getPageHibernateTemplate().find(pagination, hql, countHql, param.toArray());
	}

	@Override
	public List getResLiterTypeObejctList() {
		String hql = "from ResLiterType rlt order by rlt.rltyId ASC ";
		return this.getHibernateTemplate().find(hql);
	}

	@Override
	public List getRltyIdsList(Long rmstId) {
		String hql = "select rsl.rltyId from ResStoreLiter rsl where rsl.rmstId = ? order by rsl.rsliId ASC ";
		return this.getHibernateTemplate().find(hql, new Object[] {rmstId});
	}

	@Override
	public List getSysManIdsList(Long rmstId) {
		String hql = "select rmp.smanId from ResMetaPerm rmp where rmp.rmstId = ? and rmp.rmpeType = 1  order by rmp.rmpeId ASC ";
		return this.getHibernateTemplate().find(hql, new Object[] {rmstId});
	}

	@Override
	public List getSysManagerObjectList() {
		String hql = "from SysManager sm where smanType= ? order by sm.smanId ASC";
		return this.getHibernateTemplate().find(hql,SysManager.SMANTYPE_ADMIN);
	}

	@Override
	public ResMetaStore isResMetaStoreRmstNameExit(String arg) {
		String hql = "From ResMetaStore rms where rms.rmstName = ? ";
		List list = this.getHibernateTemplate().find(hql, arg);
		if(list.size() > 0)
			return (ResMetaStore)list.get(0);
		else
			return null;
	}

	@Override
	public List getResMetaPermList(Long rmstId, Long rmpeType, Long adminId, Long type) {
		List<Object> param = new ArrayList<Object>();
		StringBuffer fromHql = new StringBuffer("From ResMetaPerm rmp ");
		String orderHql = "order by rmp.rmpeId ASC";
		String selectHql = "";
		// 查询特色库管理员
		if(type == 1) {
			selectHql = "Select rmp.smanId ";
			fromHql.append("Where rmp.rmstId = ? ");
			param.add(rmstId);
			// 根据 特色库和管理员查询
		}else if(type == 2) {
			fromHql.append("Where rmp.rmstId = ? and rmp.smanId = ? ");
			param.add(rmstId);
			param.add(adminId);
		}else if(type == 3) {
			fromHql.append("Where rmp.rmstId = ? and rmp.smanId = ? and rmp.rmpeType = ? ");
			param.add(rmstId);
			param.add(adminId);
			param.add(rmpeType);
		}else if(type == 4) {
			selectHql = "Select rmp.rmpeType ";
			fromHql.append("Where rmp.rmstId = ? and rmp.smanId = ? ");
			param.add(rmstId);
			param.add(adminId);
		}
		return this.getHibernateTemplate().find(selectHql + fromHql.toString() + orderHql, param.toArray());
	}

	@Override
	public List getResStoreLiterList(Long rmstId) {
		String hql = "Select rsl.rltyId From ResStoreLiter rsl where rsl.rmstId = ? order by rsl.rsliId ASC ";
		return this.getHibernateTemplate().find(hql, new Object[] {rmstId});
	}

	@Override
	public ResStoreLiter getResStoreLiter(Long rmstId, Long rltyId) {
		String hql = "From ResStoreLiter rsl where rsl.rmstId = ? and rsl.rltyId= ? ";
		List list = this.getHibernateTemplate().find(hql, new Object[] {rmstId, rltyId});
		if(list.size() > 0)
			return (ResStoreLiter)list.get(0);
		return null;
	}

	@Override
	public List getResElementPoolList(Long rltyId) {
		String hql = "select repo.repoId from ResElementPool repo where repo.repoPltyId = ?  ";
		return this.getHibernateTemplate().find(hql, new Object[] {rltyId});
	}

	@Override
	public boolean deleteResMetaStore(String basePath, Long rmstId) {
		final String[] arr = new String[20];
		Number countNum = (Number) getHibernateTemplate().find(
				"select count(rsdaId) from ResSourceData where rsdaRmstId = ?", rmstId).get(0);
		if(countNum.longValue() > 0){
			return false;
		}
		
		String hql = "delete from ResSourceSubject where rssuRminId in (select rminId from ResMissionInfo where rminRmstId= "+rmstId+")";
		getHibernateTemplate().bulkUpdate(hql);   //删除特色库相关主题
		
		arr[0] = "delete from SC_FAVORITES where SCF_RES_SOURCE_ID in (select RSDA_ID from RES_SOURCE_DATA where RSDA_RMST_ID="+rmstId+")";
		arr[1] = "delete from res_tag_info where RTIN_RSDA_ID in (select RSDA_ID from RES_SOURCE_DATA where RSDA_RMST_ID="+rmstId+")";
		arr[2] = "delete from res_source_remark where RSRE_RSDA_ID in (select RSDA_ID from RES_SOURCE_DATA where RSDA_RMST_ID="+rmstId+")";
		arr[3] = "delete from res_source_comt where RSCO_RSDA_ID in (select RSDA_ID from RES_SOURCE_DATA where RSDA_RMST_ID="+rmstId+")";
		arr[4] = "delete from res_source_browse_historey where RSBH_RSDA_ID in (select RSDA_ID from RES_SOURCE_DATA where RSDA_RMST_ID="+rmstId+")";
		arr[5] = "delete from res_loads_list where RLLI_RMIN_ID in (select RMIN_ID from RES_MISSION_INFO where RMIN_RMST_ID="+rmstId+")"; 
		arr[6] = "delete from res_import_history where RMHI_RMIN_ID in (select RMIN_ID from RES_MISSION_INFO where RMIN_RMST_ID="+rmstId+")";
		arr[7] = "delete from res_extend_data where REDA_RSDA_ID in (select RSDA_ID from RES_SOURCE_DATA where RSDA_RMST_ID="+rmstId+")";
		arr[8] = "delete from res_source_language where RSLA_RSDA_ID in (select RSDA_ID from RES_SOURCE_DATA where RSDA_RMST_ID="+rmstId+")";
		arr[9] = "delete from res_source_data where RSDA_RMST_ID="+rmstId;
		arr[10] = "delete from res_miss_devolve where RMDE_RMIN_ID in (SELECT RMLA_ID FROM RES_MISSION_LANGUAGE WHERE RMLA_RMIN_ID IN (select RMIN_ID from RES_MISSION_INFO where RMIN_RMST_ID="+rmstId+"))";
		arr[11] = "delete from res_mission_language WHERE RMLA_RMIN_ID IN (select RMIN_ID from RES_MISSION_INFO where RMIN_RMST_ID="+rmstId+")";
		arr[12] = "delete from res_mission_info where RMIN_RMST_ID="+rmstId;
		arr[13] = "delete from res_store_ip_deploy WHERE RSID_RMST_ID="+rmstId;
		arr[14] = "delete from res_dynamic_type WHERE RMST_ID="+rmstId;
		arr[15] = "delete from res_import_templet WHERE RITE_RMST_ID="+rmstId;
		arr[16] = "delete from RES_TEMPLET_META WHERE RTME_RSIN_ID="+rmstId;
		arr[17] = "delete from res_meta_perm WHERE RMST_ID="+rmstId; 
		arr[18] = "delete from res_store_liter WHERE RMST_ID="+rmstId;
		arr[19] = "delete from res_meta_store WHERE RMST_ID="+rmstId; 
		// Join the surrounding Spring transaction. The legacy JDBC batch helper
		// commits early and enables auto-commit, breaking the outer transaction.
		getHibernateTemplate().execute(new HibernateCallback<Void>() {
			public Void doInHibernate(Session session) {
				for(String sql : arr) {
					session.createSQLQuery(sql).executeUpdate();
				}
				return null;
			}
		});
		
		
		// 同时删除会员RSS订阅中的特色库RSS订阅资源
		hql = "delete from RssUserData rss where rss.rssDataSourceUrl = ?";
		getHibernateTemplate().bulkUpdate(hql,
				basePath + RssSubscribeToMyRssResourceController.RSS_BASE_PATH + rmstId);
		
		return true;
	}

	@Override
	public List getResDynamicTypeList(Long rdtyId, Long rmstId, Long rdtyPid, String rdtyName, Long rdtyGrade) {
		List<Object> param = new ArrayList<Object>();
		StringBuilder fromHql = new StringBuilder("From ResDynamicType rdt where rdt.rmstId = ? and rdt.rdtyGrade = ?  ");
		String orderHql = "order by rdt.rdtyId ASC ";
		String selectHql = "select rdt.rdtyId , rdt.rdtyName , rdt.rdtyGrade ";
		param.add(rmstId);
		param.add(rdtyGrade);
		if(rdtyGrade != 1) {
			fromHql.append(" and rdt.rdtyPid = ? ");
			param.add(rdtyPid);
		}
		if(rdtyName != null) {
			fromHql.append(" and rdt.rdtyName = ? ");
			param.add(rdtyName);
		}
		return this.getHibernateTemplate().find(selectHql + fromHql.toString() + orderHql, param.toArray());
	}

	@Override
	public void deleteResTempletMetaByBatch(Long rmstId, Long rltyId) {
		String hql = "select rtme.rtmeId from ResTempletMeta rtme where rtme.rtmeRsinId = ? and rtme.rtmeRltyId = ? ";
		List list = this.getHibernateTemplate().find(hql, new Object[] {rmstId, rltyId});
		if(list.size() > 0) {
			String strIds = listToString(list);
			String delHql = "delete from ResTempletMeta rtm where rtm.rtmeId in (" + strIds + ")";
			getHibernateTemplate().bulkUpdate(delHql);
		}
	}

	private String listToString(List list) {
		String temp = "";
		if(list.size() < 0)
			return temp;
		for(int i = 0; i < list.size(); i++) {
			temp += list.get(i);
			if(i != list.size() - 1)
				temp += ",";
		}
		return temp;
	}

	@Override
	public List getResMetaPermList(Pagination pagination, Map<String, Object> conditions) {
		List<Object> param = new ArrayList<Object>();
		String selectHql = "select rmp.rmpeId,rmp.rmpeType,rmp.sysManager.smanName,rmp.rmpeBuilddate ";
		StringBuffer fromHql = new StringBuffer("from ResMetaPerm rmp where rmp.rmstId = ? ");
		StringBuffer orderHql = new StringBuffer("");
		Long rmstId = (Long)conditions.get("rmstId");
		param.add(rmstId);
		Long rmpeType = (Long)conditions.get("rmpeType");
		if(rmpeType == 1 || rmpeType == -1)
			fromHql.append(" and rmp.rmpeType > 1 ");
		else {
			fromHql.append(" and rmp.rmpeType = ? ");
			param.add(rmpeType);
		}
		String sort = (String)conditions.get("sort");
		String dir = (String)conditions.get("dir");
		// 添加排序方式
		if(sort != null && sort.length() > 0 && dir != null && dir.length() > 0) {
			if("smanName".equals(sort))
				orderHql.append(" order by rmp.sysManager.smanName ").append(dir);
			else
				orderHql.append(" order by rmp.").append(sort).append(" ").append(dir);
		}
		String countHql = "select count(*) " + fromHql.toString();
		String hql = selectHql + fromHql.toString() + orderHql.toString();
		return getPageHibernateTemplate().find(pagination, hql, countHql, param.toArray());
	}

	@Override
	public List getSysManagerList(Long rmstId, Long rmpeType, Long smanId, String type) {
		List<Object> param = new ArrayList<Object>();
		String selectHql = "";
		String fromHql = "";
		String orderHql = "";
		if("1".equals(type)) {
			selectHql = "select sm.smanId , sm.smanName ";
			fromHql = "from SysManager sm where sm.smanId not in ( select rmp2.smanId from ResMetaPerm rmp2 where rmp2.rmpeType = ? and rmp2.rmstId = ? ) ";
			orderHql = "order by sm.smanId ASC ";
			param.add(rmpeType);
			param.add(rmstId);
		}
		return getHibernateTemplate().find(selectHql + fromHql + orderHql, param.toArray());
	}

	@Override
	public List getResLanguageTypeList(String rltyName, String sort, String dir) {
		List<Object> param = new ArrayList<Object>();
		String hql = "select rlt.rltyId , rlt.rltyName from ResLanguageType rlt ";
		if(CommonUtil.isNotNull(rltyName)) {
			hql += "where rlt.rltyName = ? ";
			param.add(rltyName);
		}
		if(CommonUtil.isNotNull(sort) && CommonUtil.isNotNull(dir))
			hql += "order by rlt." + sort + " " + dir;
		return this.getHibernateTemplate().find(hql, param.toArray());
	}

	@Override
	public List getResSubjectTypeList(Long rstyPid, String rstyName) {
		List<Object> param = new ArrayList<Object>();
		String hql = "from ResSubjectType rst where rst.rstyId > 0 ";
		if(rstyPid != null) {
			hql += " and rst.rstyPid = ? ";
			param.add(rstyPid);
		}
		if(rstyName != null) {
			hql += " and rst.rstyName = ? ";
			param.add(rstyName);
		}
		return this.getHibernateTemplate().find(hql, param.toArray());
	}

	@Override
	public boolean deleteBatchResSubjectType(Long rstyId) {
		String ids = getDeleteIds(rstyId);
		
		String hql = "delete from ResSubjectType where rstyId in ("+ids+")";
		try {
			getHibernateTemplate().bulkUpdate(hql);
			return true;
		} catch (Exception e) {
			return false;
		}
//		String sql = "delete from RES_SUBJECT_TYPE rst where rst.RSTY_ID in ( " + ids + " ) ";
//		DBMS dbms = new DBMS();
//		boolean b = dbms.executeBatch(sql);
//		dbms = null;
//		return b;
	}

	/**
	 * 批量删除
	 * 
	 * @param rstyId
	 * @return
	 */
	private String getDeleteIds(Long rstyId) {
		String ids = "";
		String hql = "select rst.rstyId from ResSubjectType rst where rst.rstyPid = ? ";
		List list = this.getHibernateTemplate().find(hql, new Object[] {rstyId});
		if(list.size() > 0) {
			ids += rstyId;
			for(int i = 0; i < list.size(); i++) {
				if(ids.length() > 1)
					ids += ",";
				Long obj = (Long)list.get(i);
				ids += getDeleteIds(obj);
			}
		}else {
			if(ids.length() > 1)
				ids += ",";
			ids += rstyId;
		}
		return ids;
	}

	@Override
	public List getResSubjectTypeList(Long rmstyId, Long rstyPid, String rstyName, Long rstyGrade) {
		List<Object> param = new ArrayList<Object>();
		String fromHql = "from ResSubjectType rst where rst.rstyGrade = ? ";
		String orderHql = "order by rst.rstyId ASC ";
		String selectHql = "select rst.rstyId , rst.rstyName , rst.rstyGrade ";
		param.add(rstyGrade);
		if(rstyGrade != 1) {
			fromHql += " and rst.rstyPid = ? ";
			param.add(rstyPid);
		}
		return this.getHibernateTemplate().find(selectHql + fromHql.toString() + orderHql, param.toArray());
	}

	@Override
	public List getResSubjectTypeList() {
		return this.getHibernateTemplate().find("from ResSubjectType rst order by rst.rstyPid ,rst.rstyIndex ASC ");
	}

	@Override
	public ResSubjectType getResSubjectTypeIndex(Long pid, String rstyName, Integer rstyIndex, String type) {
		List params = new ArrayList();
		String hql = "from ResSubjectType rst where rst.rstyPid = ?  ";
		String orderHql = "";
		params.add(pid);
		if("2".equals(type)) {
			orderHql = " order by rst.rstyPid ,rst.rstyIndex DESC ";
			hql += "and rst.rstyIndex < ? ";
			params.add(rstyIndex);
		}
		if("3".equals(type)) {
			orderHql = " order by rst.rstyPid ,rst.rstyIndex ASC ";
			hql += "and rst.rstyIndex > ? ";
			params.add(rstyIndex);
		}
		if("1".equals(type)) {
			orderHql = " order by rst.rstyPid ,rst.rstyIndex DESC ";
		}
		if(rstyName != null) {
			hql += "and rst.rstyName = ? ";
			params.add(rstyName);
		}
		List list = this.getHibernateTemplate().find(hql + orderHql, params.toArray());
		if(list.size() > 0) {
			return (ResSubjectType)list.get(0);
		}
		return null;
	}

	@Override
	public List getResDynamicTypeList(Long pid, Long rmstId, String rdtyName, Long rdtyGrade, Integer rdtyIndex, String type, int rdtyType) {
		String selectSql = "select t.RDTY_ID , t.RDTY_PID , t.RDTY_NAME , t.RDTY_INDEX ";
		String fromSql = "from res_dynamic_type t where t.rmst_id = ? and t.RDTY_TYPE = " + rdtyType;
		String orderSql = "order by t.rdty_pid , t.rdty_index ";
		List params = new ArrayList();
		params.add(rmstId);
		if(pid != null) {
			fromSql += "and t.RDTY_PID = ? ";
			params.add(pid);
		}
		if(rdtyGrade != null) {
			fromSql += "and t.rdty_grade = ? ";
			params.add(rdtyGrade);
		}
		if(rdtyName != null) {
			fromSql += "and t.RDTY_NAME = ? ";
			params.add(rdtyName);
		}
		if("2".equals(type) && rdtyIndex > 0) {
			orderSql += "DESC ";
			fromSql += "and t.rdty_index < ? ";
			params.add(rdtyIndex);
		}
		if("3".equals(type) && rdtyIndex > 0) {
			orderSql += "ASC ";
			fromSql += "and t.rdty_index > ? ";
			params.add(rdtyIndex);
		}
		if("1".equals(type)) {
			orderSql += "DESC ";
		}
		List list = getStrList(selectSql + fromSql + orderSql, params);
		return list;
	}

	@Override
	public void updateResDynamicType(Long rdtyId, Integer index) {
		String sql = "update res_dynamic_type set RDTY_INDEX = " + index + " where RDTY_ID = " + rdtyId;
		execute(sql);
	}

	@Override
	public List getResSourceData(Long rmstId,Long rsdaCmtstus) {
		String hql = "select rsd.rsdaId from ResSourceData rsd where rsd.rsdaRmstId = ? ";
		if(rsdaCmtstus != null){
			hql += "and rsd.rsdaCmtstus = " + rsdaCmtstus;
		}
		return this.getHibernateTemplate().find(hql,new Object[]{rmstId});
	}

	@Override
	public Boolean updateResSourceDataCmtStusByBatch(String ids,Long rsdaCmtstus) {
		String sql = "update RES_SOURCE_DATA set RSDA_CMTSTUS = "+rsdaCmtstus+" where RSDA_ID in ("+ids+")";
		return execute(sql);
	}

	@Override
	public List<ResSourceData> getAllRessourcesByMetastoreId(Long metaStoreId, Pagination pagination) {
		String hql = "select res from ResSourceData res where res.rsdaRmstId=? order by res.rsdaBuilddate desc";
		return pagination == null ? getHibernateTemplate().find(hql, metaStoreId) : 
				getPageHibernateTemplate().find(pagination, hql, metaStoreId);
	}

	@Override
	public void addMetaStore(String basePath, ResMetaStore metastore) {
		save(metastore);
		if(metastore.getRmstStatus().longValue() == ResMetaStore.STATUS_USEABLENESS.longValue() ) {
			String usefulUserHql = "select user from SysUserinfo user where user.suinState=" + SysUserinfo.STATE_PAST;
			List<SysUserinfo> users = getHibernateTemplate().find(usefulUserHql);
			// 订阅到每个审核通过的用户的的RSS订阅中
			if(users != null && users.size() > 0) {
				for(SysUserinfo user : users) {
					RssUserData rss = new RssUserData();
					rss.setRssDataAddDate(new Date());
					rss.setRssDataLastModifyDate(new Date());
					rss.setRssDataName(metastore.getRmstName());
					rss.setRssDataOrder(RssUserData.RSS_DATA_METASTORE_ORDER);
					rss.setRssDataSourceUrl(basePath + RssSubscribeToMyRssResourceController.RSS_BASE_PATH + metastore.getRmstId());
					rss.setRssDataUserId(user.getSuinId());
					save(rss);
				}
			}
		}
	}
	
	public List getResStoreIpDeployList(Pagination pagination, Map<String, Object> conditions, Long metaStoreId) {
		
		StringBuffer sqlParam = new StringBuffer("");
		String selectHql = "select rsid.rsidId,rsid.rsidRmstId,rsid.rsidIpPart from ResStoreIpDeploy rsid where rsid.rsidRmstId = " + metaStoreId;
		
		// 添加查询条件
		String sort = (String)conditions.get("sort");
		String dir = (String)conditions.get("dir");
		String search = (String)conditions.get("search");
		// 添加排序方式
		if(ServiceUtil.isNotNull(search)){
			sqlParam.append(" and rsid.rsidIpPart like '%" + search + "%'");
		}
		if(sort != null && sort.length() > 0 && dir != null && dir.length() > 0) {
			if("rsidIpPart".equals(sort)){
				sqlParam.append(" order by rsid.rsidIpPart ").append(dir);
			}
		}
		
		String hql = selectHql + sqlParam.toString();
		List list = getPageHibernateTemplate().find(pagination, hql);
		return list;
	}


	@Override
	public List getResSubjectTypeList(Long pid, Long rstyId, String rstyName,
			Long rstyGrade, Integer rstyIndex, String type) {
		String selectSql = "select t.RSTY_ID , t.RSTY_PID , t.RSTY_NAME , t.RSTY_INDEX ";
		String fromSql = "from res_subject_type t where 1=1 ";
		String orderSql = "order by t.rsty_pid , t.rsty_index ";
		List params = new ArrayList();
		if(pid != null) {
			fromSql += "and t.RSTY_PID = ? ";
			params.add(pid);
		}
		if(rstyGrade != null) {
			fromSql += "and t.rsty_grade = ? ";
			params.add(rstyGrade);
		}
		if(rstyName != null) {
			fromSql += "and t.RSTY_NAME = ? ";
			params.add(rstyName);
		}
		if("2".equals(type) && rstyIndex > 0) {
			orderSql += "DESC ";
			fromSql += "and t.rsty_index < ? ";
			params.add(rstyIndex);
		}
		if("3".equals(type) && rstyIndex > 0) {
			orderSql += "ASC ";
			fromSql += "and t.rsty_index > ? ";
			params.add(rstyIndex);
		}
		if("1".equals(type)) {
			orderSql += "DESC ";
		}
		List list = getStrList(selectSql + fromSql + orderSql, params);
		return list;
	}
	
	@Override
	public List getResmMetaStoreList(){
		String sql ="SELECT r.RMST_ID,r.RMST_NAME,r.RMST_LOGO FROM RES_META_STORE r";
		return getStrList(SqlBasicUnit.parseSql(sql, 0, 8),null);
	}
}










