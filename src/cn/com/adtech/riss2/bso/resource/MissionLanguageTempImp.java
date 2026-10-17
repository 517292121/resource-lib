package cn.com.adtech.riss2.bso.resource;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.domain.resource.ResMissDevolve;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.domain.resource.ResMissionLanguage;
import cn.com.adtech.riss2.domain.resource.ResSourceLanguage;

import com.jbetter.framework.session.Pagination;

public class MissionLanguageTempImp extends CommonServiceImp implements IMissionLanguageTempService {

	@Override
	public String getMissionLanguageList(long missionId, String userColumn, int start, int limit) {
		//String selectHql = "select rml.rmlaId,rml.resMissionId,rml.resLanguageId,rml.rmlaStatus,rml.rmlaProcessor,rml.rmlaAssessor,rml.rmlaProctime,rml.rmlaAssetime,rml.rmlaIsfirst,rml.resMissionInfo.rminMissname as missionname,rml.resLanguageType.rltyName as languageName,rml."+userColumn+".smanName as userName";
		//String selectHql = "select rml.rmlaId,rml.resMissionId,rml.resLanguageId,rml.rmlaStatus,rml.rmlaProcessor,rml.rmlaAssessor,rml.rmlaProctime,rml.rmlaAssetime,rml.rmlaIsfirst,rml.resMissionInfo.rminMissname as missionname,rlt.rltyName as languageName,rml."+userColumn+".smanName as userName";             
		//String fromHql = " from ResMissionLanguage rml";
		//fromHql += " right join resLanguageType rlt on rml.resLanguageId = rlt.rltyId and rml.resMissionId="+missionId;
		//fromHql += " where rml.resMissionId="+missionId;
		//List<Object> list = getPageHibernateTemplate().find(pagination, selectHql+fromHql);
		//hql查询不出来老，换sql
		String sourceCountHql = "(select count(rsl.rsla_id) from res_source_data rsda left join res_source_language rsl on rsda.rsda_id = rsl.rsla_rsda_id where rsda.rsda_rmin_id="+missionId+" and rsl.rsla_rlty_id=rlt.rlty_id) as interpretNum,";
		String sql = "select rml.*,rlt.*, "+sourceCountHql+" sm.sman_name from RES_MISSION_LANGUAGE rml right join RES_LANGUAGE_TYPE rlt on rml.rmla_rlty_id=rlt.rlty_id and rml.rmla_rmin_id=" + missionId;
		sql += " left join sys_manager sm on rml."+userColumn+"=sm.sman_id";
		
		System.out.println(sql);
		String jsondata = getListToJson(sql, null, start, limit);
	
		return jsondata;
	}

	public Serializable saveMissionLanguage(ResMissionLanguage missionLanguage) {
		try {
			Serializable s = getHibernateTemplate().save(missionLanguage);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean updateSingleColumn(long missionId, long languageId,
			String columnName, Object dataValue) {
		try {
			String sql = "update ResMissionLanguage set "+columnName+"="+dataValue+" where resMissionId="+missionId+" and resLanguageId=" + languageId;
			getHibernateTemplate().bulkUpdate(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean saveMissionLanguageAuthor(long rminId, long lanId, int statu, long processor){
		ResMissionLanguage rml = new ResMissionLanguage();
		rml.setResLanguageId(lanId);
		rml.setResMissionId(rminId);
		rml.setRmlaIsfirst(false);
		rml.setRmlaProcessor(processor);
		rml.setRmlaStatus(statu);
		
		Serializable s = saveMissionLanguage(rml);
		return s == null ? false : true;
	}

	@Override
	public boolean updateMissionLanguageStatu(long rmla_id, int statu) {
		try {
//			ResMissionLanguage rml = (ResMissionLanguage) getHibernateTemplate().load(ResMissionLanguage.class, rmla_id);
//			rml.setRmlaStatus(statu);
//			if(statu == ResMissionInfo.RMINSTATUS_COMMIT_NOTCHECK){
//				rml.setRmlaProctime(new Date());
//			}
//			if(statu == ResMissionInfo.RMINSTATUS_COMMIT_PASS || statu == ResMissionInfo.RMINSTATUS_COMMIT_NOTPASS){
//				rml.setRmlaAssetime(new Date());	
//			}
//			getHibernateTemplate().update(rml);
//			
			String hql = "update ResMissionLanguage set rmlaStatus=? ";
			if(statu == ResMissionInfo.RMINSTATUS_COMMIT_NOTCHECK){
				hql += ", rmlaProctime=?";
			}
			if(statu == ResMissionInfo.RMINSTATUS_COMMIT_PASS || statu == ResMissionInfo.RMINSTATUS_COMMIT_NOTPASS){
				hql += ", rmlaAssetime=?";	
			}
			hql += " where rmlaId=?";
			getHibernateTemplate().bulkUpdate(hql, new Object[]{statu,new Date(), rmla_id});
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void updateMissionFirstStatu(long missionId, int statu) {
		String and = "";
		if(statu < 3){
			and = ", rmlaProctime=?";
		}else{
			and = ", rmlaAssetime=?";
		}
		String hql = "update ResMissionLanguage set rmlaStatus=? "+and+" where rmlaIsfirst=true and resMissionId=" +missionId;
		System.out.println("hql: " + hql);
		getHibernateTemplate().bulkUpdate(hql,new Object[]{statu, new Date()});
	}

	@Override
	public boolean updateSingleColumn(long missLanId, String columnName,
			Object dataValue) {
		String sql = "update ResMissionLanguage set "+columnName+"="+dataValue+"  where rmlaId=" + missLanId;
		try {
			getHibernateTemplate().bulkUpdate(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void deleteById(long rmla_id) {
		ResMissionLanguage rml = (ResMissionLanguage) getHibernateTemplate().load(ResMissionLanguage.class, rmla_id);
		getHibernateTemplate().delete(rml);		
	}

	@Override
	public void deleteByMissionId(long rminId) {
		String hql = "delete from ResMissionLanguage rml where rml.resMissionId="+rminId;
		getHibernateTemplate().bulkUpdate(hql);
	}

	@Override
	public long getMissionFirstId(long rminId) {
		String hql = "select rmlaId from ResMissionLanguage where resMissionId="+rminId+" and rmlaIsfirst=true";
		List<Object> list = getHibernateTemplate().find(hql);
		if(list != null && list.size() > 0){
			return Long.parseLong(list.get(0).toString());
		}else{
			return 0;
		}
	}

	/**
	 * 获取作业校验的作业语种任务JSON
	 */
	public List<Object> getMissionLanguageByJson(Pagination pagination, long rminId, short statu) {
		String hql = "select rml.rmlaId,rml.resMissionId,rml.resLanguageId,rml.rmlaStatus,rml.rmlaAssessor,rml.rmlaProctime,rml.rmlaAssetime,rml.rmlaIsfirst,rml.resLanguageType.rltyName as languageName,author.smanName as userName ";
		hql += ", (select count(rsl.rslaId) from ResSourceLanguage rsl where rsl.resSourceData.rsdaRminId="+rminId+" and rsl.resLanguageId=rml.resLanguageId and rsl.rslaStatus=2) as sourceNum";
		String from = " from ResMissionLanguage rml left join rml.assessorAuthor author where rml.rmlaStatus>1 and rml.rmlaIsfirst=false and rml.resMissionId=" + rminId;
		if(statu > 0){
			from += " and rml.rmlaStatus=" + statu;
		}
		hql = hql + from;
		System.out.println("hql>>" + hql);
		List<Object> list = getHibernateTemplate().find(hql);
		return list;
	}

	@Override
	public boolean missionChecked(long missLanId, long missId, boolean isfirst, short statu) {
		boolean b = updateMissionLanguageStatu(missLanId, statu);
		if(isfirst){
			String hql = "update ResMissionInfo set rminStatus="+statu+" where rminId=?";
			getHibernateTemplate().bulkUpdate(hql,missId);
		}
		return b;
	}

	@Override
	public boolean missionDeliver(ResMissDevolve deliver, boolean isfirst) {
		deliver = (ResMissDevolve) save(deliver);
		boolean b = updateSingleColumn(deliver.getRmdeRminId(), rmlaStatus, ResMissionInfo.RMINSTATUS_DELIVERING);
		if(isfirst){
			if(deliver.getMissionId() > 0){
				String hql = "update ResMissionInfo set rminStatus=" + ResMissionInfo.RMINSTATUS_DELIVERING + " where rminId=?";
				getHibernateTemplate().bulkUpdate(hql,deliver.getMissionId());
			}
		}
		return b;
	}

	@Override
	public boolean missionChecked(long missLanId, long statu) {
		String hql = "update ResMissionLanguage set rmlaStatus="+statu+" ,rmlaAssetime=? where rmlaId=" + missLanId;
		try {
			getHibernateTemplate().bulkUpdate(hql,new Date());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int getUnCheckNumber(long missLanId) {
		ResMissionLanguage missionLanguage = (ResMissionLanguage) getHibernateTemplate().get(ResMissionLanguage.class, missLanId);
		if(missionLanguage == null){
			return 0;
		}
		String hql = "select count(rsl.rslaId) from ResSourceLanguage rsl where rsl.resSourceData.rsdaRminId="+missionLanguage.getResMissionId()+" and rsl.resLanguageId="+missionLanguage.getResLanguageId()+" and (rsl.rslaStatus=2 or rsl.rslaStatus=4)";
		List<Object> list = getHibernateTemplate().find(hql);
		if(list == null || list.size() < 1){
			return 0;
		}else{
			int count = Integer.parseInt(list.get(0).toString());
			list = null;
			return count;
		}
		
	}

	@Override
	public List getResSourceLanguageList() {
		String hql = "from ResSourceLanguage rsl where rsl.rslaUsername is null or rsl.rslaAuthorName is null or rsl.rslCheckLoginname is null or rsl.rslCheckSuinname is null ";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public ResMissionLanguage getResMissionLanguge(Long rminId, Long rltyId) {
		String hql = "from ResMissionLanguage rml where rml.resMissionId = ? and rml.resLanguageId = ? ";
		List list = getHibernateTemplate().find(hql,new Object[]{rminId,rltyId});
		if(list.size()>0){
			return (ResMissionLanguage)list.get(0);
		}
		return null;
	}

	

}

