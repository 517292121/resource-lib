package cn.com.adtech.riss2.bso.resource;

import java.util.LinkedList;
import java.util.List;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.domain.resource.ResMissDevolve;
import cn.com.adtech.riss2.domain.resource.ResMissionLanguage;

import com.jbetter.framework.session.Pagination;

public class MissionDeliverServiceImp extends CommonServiceImp implements IMissionDeliverService {

	public List<ResMissDevolve> getDevolveList(long userId, int type, int missionType, Pagination pagination, String missiontTypes) {
		String hql = "select rmd.rmdeId, rmd.resMissionInfo.resMissionInfo.rminMissname,rmd.rmdeRminId,rmd.preSysManager.smanName,rmd.rmdePreuid,rmd.aftSysManager.smanName,rmd.rmdeAftuid,rmd.rmdeMissdate,rmd.rmdeDevodate,rmd.rmdeRemark,rmd.rmdeStatus,rmd.rmdeMind,rmd.rmdeAudidate,rmd.rmdeMissiontype from ResMissDevolve rmd where rmd.rmdeMissiontype in ("+missiontTypes+") ";
		if(type == 1){
			hql += " and rmd.rmdePreuid=" + userId;
		}else if (type == 2){
			hql += " and rmd.rmdeAftuid=" + userId;
		}else{
			hql += " and (rmd.rmdeAftuid="+userId+" or rmd.rmdePreuid="+userId+") ";
		}
		
		if(missionType > 0){
			hql += " and rmd.rmdeMissiontype=" + missionType;
		}
		hql += " order by rmd.rmdeStatus, rmd.rmdeId desc";
		List<ResMissDevolve> list = getPageHibernateTemplate().find(pagination, hql);
		return list;
	}

	public boolean revertDeliver(long deliverId, int statu, String remark) {
		try {
			long userid = 0;
			long missionid = 0;
			
			ResMissDevolve devolve = getMissDevolveById(deliverId);
			long missLanId = devolve.getRmdeRminId(); //作业语种ID
			ResMissionLanguage missionLanguage = (ResMissionLanguage) getHibernateTemplate().get(ResMissionLanguage.class, missLanId);
			missionid = missionLanguage.getResMissionId(); //作业ID
			
			short missionType = devolve.getRmdeMissiontype();
			if(statu == 2){ //如果成功，则操作者为转交之后的用户
				userid = devolve.getRmdeAftuid();
			}if(statu == 3){ //如果失败，则还是由之前的用户操作
				userid = devolve.getRmdePreuid();
			}
			
			String[] sqlArr = new String[3];
			sqlArr[0] = "update RES_MISS_DEVOLVE set RMDE_STATUS="+statu+",RMDE_MIND='"+remark+"' where RMDE_ID="+deliverId;
			
			if(missionType == ResMissDevolve.MISSION_JIAGONG){  //加工类型
				sqlArr[1] = "update RES_MISSION_INFO set RMIN_USER = "+userid+",RMIN_STATUS=1 where RMIN_ID=" + missionid;
				sqlArr[2] = "update RES_MISSION_LANGUAGE set RMLA_PROCESSOR="+userid+",RMLA_STATUS=1 where RMLA_RMIN_ID="+missionid+" and RMLA_ISFIRST=1";
			}
			if(missionType == ResMissDevolve.MISSION_FANYI){   //其它语录入类型
				sqlArr[1] = "update RES_MISSION_LANGUAGE set RMLA_PROCESSOR = "+userid+",RMLA_STATUS=1 where RMLA_ID=" + missLanId;
			}
			if(missionType == ResMissDevolve.MISSION_XIAOYAN){ //校验类型
				sqlArr[1] = "update RES_MISSION_INFO set RMIN_CHECKUSER = "+userid+",RMIN_STATUS=2 where RMIN_ID=" + missionid;
				sqlArr[2] = "update RES_MISSION_LANGUAGE set RMLA_ASSESSOR="+userid+",RMLA_STATUS=2 where RMLA_RMIN_ID="+missionid+" and RMLA_ISFIRST=1";
			}
			if(missionType == ResMissDevolve.MISSION_OTHERCHECK){ //其它语校验类型
				sqlArr[1] = "update RES_MISSION_LANGUAGE set RMLA_ASSESSOR = "+userid+",RMLA_STATUS=2 where RMLA_ID=" + missLanId;
			}
			boolean b = executeMultiSql(sqlArr);
			
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public int getDevolveListCount(long userId, int type, int missionType, String missiontTypes) {
		String hql = "select count(rmd.rmdeId) as countNum from ResMissDevolve rmd where rmd.rmdeMissiontype in ("+missiontTypes+") ";
		if(type == 1){
			hql += " and rmd.rmdePreuid=" + userId;
		}else if (type == 2){
			hql += " and rmd.rmdeAftuid=" + userId;
		}else{
			hql += " and (rmd.rmdeAftuid="+userId+" or rmd.rmdePreuid="+userId+") ";
		}
		
		if(missionType > 0){
			hql += " and rmd.rmdeMissiontype=" + missionType;
		}
		List list = getHibernateTemplate().find(hql);
		if(list != null && list.size() > 0){
			return Integer.parseInt(list.get(0).toString());
		}else{
			return 0;
		}
	}

	public ResMissDevolve getMissDevolveById(long devolveId) {
		String hql = "select new ResMissDevolve(rmd.rmdeRminId,rmd.rmdePreuid, rmd.rmdeAftuid, rmd.rmdeMissiontype) from ResMissDevolve rmd where rmd.rmdeId=" + devolveId;
		List<ResMissDevolve> list = getHibernateTemplate().find(hql);
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public boolean deleteDevolveById(long id) {
		try {
			String sql = "delete from RES_MISS_DEVOLVE where RMDE_ID = " + id;
			boolean b = execute(sql);
			
			return b;
		} catch (Exception e) {
			return false;
		}
	}

//	public boolean chexiao(long id) {
//		try {
//			ResMissDevolve devolve = getMissDevolveById(id);
//			long missionid = devolve.getRmdeRminId();
//			short missionType = devolve.getRmdeMissiontype();
//			long userid = devolve.getRmdePreuid();
//			
//			String[] sqlArr = new String[3];
//			sqlArr[0] = "delete from RES_MISS_DEVOLVE where RMDE_ID = " + id;
//			
//			if(missionType == ResMissDevolve.MISSION_JIAGONG){  //加工类型
//				sqlArr[1] = "update RES_MISSION_INFO set RMIN_USER = "+userid+" where RMIN_ID=" + missionid;
//				sqlArr[2] = "update RES_MISSION_LANGUAGE set RMLA_PROCESSOR="+userid+" where RMLA_RMIN_ID="+missionid+" and RMLA_ISFIRST=1";
//			}if(missionType == ResMissDevolve.MISSION_FANYI){   //其它语录入类型
//				sqlArr[1] = "update RES_MISSION_INFO set RMIN_INTERUSER = "+userid+" where RMIN_ID=" + missionid;
//			
//			}if(missionType == ResMissDevolve.MISSION_XIAOYAN){ //校验类型
//				sqlArr[1] = "update RES_MISSION_INFO set RMIN_CHECKUSER = "+userid+" where RMIN_ID=" + missionid;
//				sqlArr[2] = "update RES_MISSION_LANGUAGE set RMLA_ASSESSOR="+userid+" where RMLA_RMIN_ID="+missionid+" and RMLA_ISFIRST=1";
//			}if(missionType == ResMissDevolve.MISSION_OTHERCHECK){ //其它语校验
//				sqlArr[1] = "update RES_MISSION_INFO set RMIN_OTHERCHECK = "+userid+" where RMIN_ID=" + missionid;
//			}
//			DBMS dbms = new DBMS();
//			boolean b = dbms.executeMultiSql(sqlArr);
//			
//			dbms = null;
//			return b;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
	
	public boolean chexiao(long id) {
		try {
			ResMissDevolve devolve = getMissDevolveById(id);
			long missLanId = devolve.getRmdeRminId(); //作业语种ID
			ResMissionLanguage missionLanguage = (ResMissionLanguage) getHibernateTemplate().get(ResMissionLanguage.class, missLanId);
			long missionid = missionLanguage.getResMissionId(); //作业ID
			
			short missionType = devolve.getRmdeMissiontype();
			long userid = devolve.getRmdePreuid();
			
			List<String> list = new LinkedList<String>();
			list.add("delete from RES_MISS_DEVOLVE where RMDE_ID = " + id);
			
			if(missionType == ResMissDevolve.MISSION_JIAGONG){  //加工类型
				list.add("update RES_MISSION_INFO set RMIN_STATUS=1, RMIN_USER = "+userid+" where RMIN_ID=" + missionid);
				list.add("update RES_MISSION_LANGUAGE set RMLA_STATUS=1, RMLA_PROCESSOR="+userid+" where RMLA_RMIN_ID="+missionid+" and RMLA_ISFIRST=1");
			}
			if(missionType == ResMissDevolve.MISSION_FANYI){   //其它语录入类型
				list.add("update RES_MISSION_LANGUAGE set RMLA_STATUS=1, RMLA_PROCESSOR = "+userid+" where RMLA_ID=" + missLanId);
				System.out.println(list.get(1));
			}
			if(missionType == ResMissDevolve.MISSION_XIAOYAN){ //校验类型
				list.add("update RES_MISSION_INFO set RMIN_STATUS=2,RMIN_CHECKUSER = "+userid+" where RMIN_ID=" + missionid);
				list.add("update RES_MISSION_LANGUAGE set RMLA_STATUS=2,RMLA_ASSESSOR="+userid+" where RMLA_RMIN_ID="+missionid+" and RMLA_ISFIRST=1" );
			}
			if(missionType == ResMissDevolve.MISSION_OTHERCHECK){ //其它语校验
				list.add("update RES_MISSION_LANGUAGE set RMLA_STATUS=2,RMLA_ASSESSOR = "+userid+" where RMLA_ID=" + missLanId);
			}
			boolean b = executeMultiSql(list);
			
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}
