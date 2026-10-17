package cn.com.adtech.riss2.bso.resource;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.PinYin2Abbreviation;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.domain.resource.ResMissionLanguage;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceFile;
import cn.com.adtech.riss2.domain.resource.ResSourceLanguage;
import cn.com.adtech.riss2.jdbcms.SqlBasicUnit;
import cn.com.adtech.riss2.jdbcms.ToolUnit;

import com.jbetter.framework.session.Pagination;
import com.jbetter.hibernate.IdGenerator;

@SuppressWarnings("unchecked")
public class MissionInfoManagementServiceImp extends CommonServiceImp implements IMissionInfoManagementService {
	
	private static int missionNameCode = 0;
	private IMissionLanguageTempService missionLanguageService = null;
	public void setMissionLanguageService(
			IMissionLanguageTempService missionLanguageService) {
		this.missionLanguageService = missionLanguageService;
	}

	/**
	 *  资源录入(第一语) 的作业列表, 
	 */
	public List getResMissionInfoList(Pagination pagination, Map<String, Object> conditions) {
		String username = " rmi.rminUserSysManager.smanName as smanUseName , rmi.rminAuthorSysManager.smanName as smanCreateName, ";
		if(conditions.get("username") != null && conditions.get("username").toString().length() > 1){
			username = conditions.get("username").toString();
		}
		
		List<Object> param = new ArrayList<Object>();
		StringBuffer orderSql = new StringBuffer(" order by ");
		String selectHQL = "select rmi.rminId , rmi.rminRmstId , rmi.rminMissname , rmi.resMetaStore.rmstName , rmi.rminResLiterType.rltyName , "+username+" rmi.rminBuilddate , rmi.rminEnddate , rmi.rminStatus ,rmi.rminRemark , rmi.rminResCount ";
//		String rsdaCountHql = "select count(rsdaId) from ResSourceData rsdaNumber where rmi.rminId=rsdaNumber.rsdaRminId ";
		StringBuilder fromHQL = new StringBuilder();
		fromHQL.append("from ResMissionInfo rmi where rmi.rminRmstId in ( ").append(
				"select rmp.rmstId from ResMetaPerm rmp where rmp.rmpeType = ? and rmp.smanId = ? and rmp.resMetaStore.rmstStatus = 1 ").append(") ");
		if(conditions != null && conditions.size() > 0) {
			Long rmpeType = (Long)conditions.get("rmpeType");
			String otherLanguage = (String)conditions.get("otherLanguage");
			if(rmpeType != null && rmpeType > 0) {
				param.add(rmpeType);
				if (rmpeType == 2){
				    fromHQL.append(" and rmi.rminUser=" + conditions.get("smanId"))	;
				}
				else if(rmpeType == 3){  // 第一语作业审核
					fromHQL.append(" and rmi.rminStatus > 1 ");
//					Long smanId = (Long)conditions.get("smanId");
//					fromHQL.append(" and rmi.rminStatus >1 and (rmi.rminCheckuser="+smanId+" or rmi.rminCheckuser is null or rmi.rminCheckuser=0 ) and (rmi.rminCheckuser<>-1 or rmi.rminCheckuser is null) ");  // 只显示待校验的
				}
				fromHQL.append(" and rmi.rminStatus<5 ");
			}
			Long smanId = (Long)conditions.get("smanId");
			if(smanId != null && smanId > 0)
				param.add(smanId);
			Long rmstId = (Long)conditions.get("rmstId");
			if(rmstId != null && rmstId > 0) {
				fromHQL.append(" and rmi.rminRmstId = ? ");
				param.add(rmstId);
			}
			Long rltyId = (Long)conditions.get("rltyId");
			if(rltyId != null && rltyId > 0) {
				fromHQL.append(" and rmi.rminRltyId = ? ");
				param.add(rltyId);
			}
			Long rminStatus = (Long)conditions.get("rminStatus");
			if(rminStatus != null && rminStatus > 0) {
				fromHQL.append(" and rmi.rminStatus = ? ");
				param.add(rminStatus);
			}
			String sort = (String)conditions.get("sort");
			String dir = (String)conditions.get("dir");
			// 添加排序方式
			if(sort != null && sort.length() > 0 && dir != null && dir.length() > 0) {
				if("rminMissName".equals(sort))
					orderSql.append(" rmi.rminMissname ");
				else if("rmstName".equals(sort))
					orderSql.append(" rmi.resMetaStore.rmstName ");
				else if("rltyName".equals(sort))
					orderSql.append(" rmi.rminResLiterType.rltyName ");
				else if("smanUseName".equals(sort))
					orderSql.append(" rmi.rminUserSysManager.smanName ");
				else if("smanCreateName".equals(sort))
					orderSql.append(" rmi.rminAuthorSysManager.smanName ");
				else if("rminBuildDate".equals(sort))
					orderSql.append(" rmi.rminBuilddate ");
				else if("rminEndDate".equals(sort))
					orderSql.append(" rmi.rminEnddate ");
				else
					orderSql.append(" rmi.rminStatus ");
				orderSql.append(dir);
			}
//			String rsdaCountType = (String)conditions.get("rsdaCountType");
//			if(CommonUtil.isNotNull(rsdaCountType)){
//				rsdaCountHql = " select count(*) as rsdCount from ResSourceLanguage rsl where rsl.resSourceData.rsdaId in ( select rsd2.rsdaId from ResSourceData rsd2 where rmi.rminId=rsd2.rsdaRminId  ) and rsl.rslaStatus != 3 ";
//			}
		}
		selectHQL += " as rminNum, rmi.rminNameremark ";
		String countHql = "select count(*) " + fromHQL.toString();
		
		List lists = getPageHibernateTemplate().find(pagination, selectHQL + fromHQL.toString() + orderSql.toString(), countHql,
				param.toArray());
		return lists;
	}

	/**
	 * 根据isWhere 生成的hql，开头部分是使用where 还是 and
	 * 
	 * @param whereHql
	 * @param isWhere
	 * @return
	 */
	protected boolean createWhereAnd(StringBuffer whereHql, boolean isWhere) {
		if(isWhere) {
			whereHql.append(" where ");
			isWhere = false;
		}else {
			whereHql.append(" and ");
		}
		return isWhere;
	}

	private boolean isDateStr(String dateStr) {
		if(dateStr != null && dateStr.length() >= 10)
			return true;
		else
			return false;
	}

	@Override
	public ResMissionInfo getResMissionInfo(Long rminRmstId, Long rminRltyId, String rminMissName) {
		String hql = "from ResMissionInfo rmi where rmi.rminRmstId = ? and rmi.rminRltyId = ? and rmi.rminMissname = ? ";
		List list = getHibernateTemplate().find(hql.toString(), new Object[] {rminRmstId, rminRltyId, rminMissName});
		if(list.size() > 0)
			return (ResMissionInfo)list.get(0);
		return null;
	}

	
	
	/**
	 * 其它语加工的作业列表
	 */
	public String getInterpretMissionListByJson(long store, long sourceType, int statu, long userId, int start, int limit, String orderColumn, String orderType) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select RMI.RMIN_ID, RMI.RMIN_RMST_ID,RMI.RMIN_RLTY_ID,RMI.RMIN_MISSNAME,RMI.RMIN_BUILDDATE,RMI.RMIN_STATUS,RMI.RMIN_ENDDATE, RMI.RMIN_REMARK, ");
		buffer.append("RLT.RLTY_NAME, ");
		buffer.append("RMS.RMST_NAME, ");
		buffer.append("(select count(rsda.rsda_id) from RES_SOURCE_DATA rsda where rsda.RSDA_RMIN_ID=RMI.RMIN_ID) as sourceNum ");
		buffer.append(" from RES_MISSION_INFO RMI ");
		buffer.append(" left join RES_LITER_TYPE RLT on RMI.RMIN_RLTY_ID=RLT.RLTY_ID ");
		buffer.append(" left join RES_META_STORE RMS on RMI.RMIN_RMST_ID=RMS.RMST_ID ");
		buffer.append(" left join RES_META_PERM RMP on RMS.RMST_ID=RMP.RMST_ID ");
		buffer.append(" where RMS.RMST_STATUS=1");
		buffer.append(" and RMI.RMIN_STATUS > 1 ");
		buffer.append(" AND (RMP.RMPE_TYPE = 2 AND RMP.RMPE_ADMINID= "+userId+")");
		if(sourceType > 0) {
			buffer.append(" AND RMI.RMIN_RLTY_ID=" + sourceType);
		}
		if(statu > 0) {
			buffer.append(" AND RMI.RMIN_STATUS=" + statu);
		}	
		if(store > 0) {
			buffer.append(" and RMI.RMIN_RMST_ID=" + store);
		}
		
		buffer.append(" order by "+orderColumn+" " + orderType);
        String sql = buffer.toString();
        buffer = null;
        
		String jsondata = getListToJson(sql, null, start, limit);
		return jsondata;
	}
	
	/**
	 * 资源审核(第一语) 资源列表 没有复用
	 */
	public String getCheckedMissionListByJson(long store, long sourceType, long userId, int start, int limit, String orderColumn, String orderType) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select RMI.RMIN_ID, RMI.RMIN_RMST_ID,RMI.RMIN_RLTY_ID,RMI.RMIN_MISSNAME,RMI.RMIN_BUILDDATE,RMI.RMIN_STATUS,RMI.RMIN_ENDDATE, RMI.RMIN_REMARK, RMI.RMIN_CHECKUSER, ");
		buffer.append("SM.SMAN_NAME, ");
		buffer.append("RLT.RLTY_NAME, ");
		buffer.append("RMS.RMST_NAME, ");
		
		buffer.append(" (select count(rsda.RSDA_ID) from RES_SOURCE_DATA rsda where rsda.RSDA_RMIN_ID=RMI.RMIN_ID and rsda.RSDA_ISUSE=1 and rsda.RSDA_STATUS="+ResSourceData.STATUS_CHECK+") as sourceNum, ");// 未审核的资源数
		buffer.append(" (select count(rsda.RSDA_ID) from RES_SOURCE_DATA rsda where rsda.RSDA_RMIN_ID=RMI.RMIN_ID and rsda.RSDA_ISUSE=1 and rsda.RSDA_STATUS=4) as noPassNum ");// 未通过的资源数
		
		buffer.append(" from RES_MISSION_INFO RMI ");
		buffer.append(" left join RES_LITER_TYPE RLT on RMI.RMIN_RLTY_ID=RLT.RLTY_ID ");
		buffer.append(" left join RES_META_STORE RMS on RMI.RMIN_RMST_ID=RMS.RMST_ID ");
		buffer.append(" left join RES_META_PERM RMP on RMS.RMST_ID=RMP.RMST_ID ");
		buffer.append(" left join SYS_MANAGER SM on RMI.RMIN_CHECKUSER=SM.SMAN_ID ");
		buffer.append(" where RMS.RMST_STATUS=1");
		buffer.append(" and RMI.RMIN_STATUS > 1 ");  //1为加工中的作业
		buffer.append(" and RMI.RMIN_STATUS < 5 ");  //5为转交中的作业
		buffer.append(" AND RMP.RMPE_TYPE = 3");
		buffer.append(" AND (RMI.RMIN_CHECKUSER <> -1 or RMI.RMIN_CHECKUSER is null) ");
		buffer.append(" AND RMP.RMPE_ADMINID= " + userId);
		buffer.append(" AND (RMI.RMIN_CHECKUSER="+userId+" or RMI.RMIN_CHECKUSER is null or RMI.RMIN_CHECKUSER=0) ");
		if(sourceType > 0) {
			buffer.append(" AND RMI.RMIN_RLTY_ID=" + sourceType);
		}
		if(store > 0) {
			buffer.append(" and RMI.RMIN_RMST_ID=" + store);
		}
		
		buffer.append(" order by "+orderColumn+" " + orderType);
        String sql = buffer.toString();
        buffer = null;
        

		String jsondata = getListToJson(sql, null, start, limit);
		return jsondata;
	}
	
	/**
	 * 其它语作业校验列表
	 */
	public String getOtherLanguageCheckedMissionListByJson(long store, long sourceType, long userId, int start, int limit, String orderColumn, String orderType) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select RMI.RMIN_ID, RMI.RMIN_RMST_ID,RMI.RMIN_RLTY_ID,RMI.RMIN_MISSNAME,RMI.RMIN_BUILDDATE,RMI.RMIN_STATUS,RMI.RMIN_ENDDATE, RMI.RMIN_REMARK, ");
		buffer.append("RLT.RLTY_NAME, ");
		buffer.append("RMS.RMST_NAME ");
		buffer.append(" from RES_MISSION_INFO RMI ");
		buffer.append(" left join RES_LITER_TYPE RLT on RMI.RMIN_RLTY_ID=RLT.RLTY_ID ");
		buffer.append(" left join RES_META_STORE RMS on RMI.RMIN_RMST_ID=RMS.RMST_ID ");
		buffer.append(" left join RES_META_PERM RMP on RMS.RMST_ID=RMP.RMST_ID ");
		buffer.append(" where RMS.RMST_STATUS=1");
		buffer.append(" and RMI.RMIN_STATUS = 3 "); //状态3为第一语种审核通过
		buffer.append(" AND (RMP.RMPE_TYPE = 3 AND RMP.RMPE_ADMINID="+userId+" )");
		if(sourceType > 0) {
			buffer.append(" AND RMI.RMIN_RLTY_ID=" + sourceType);
		}
		if(store > 0) {
			buffer.append(" and RMI.RMIN_RMST_ID=" + store);
		}
		
		buffer.append(" order by "+orderColumn+" " + orderType);
	    String sql = buffer.toString();
	    
	    buffer = null;
	    
	
		String jsondata = getJsonData(sql, null, start, limit);
		return jsondata;
	}

	@Override
	public List getSysManagerList(Long rmpeType, Long rmstId , Long smanId) {
		List<Object> param = new ArrayList<Object>();
		String hql = "select rmp.smanId , rmp.sysManager.smanName from ResMetaPerm rmp where rmp.rmpeType = ? and rmp.rmstId = ? ";
		param.add(rmpeType);
		param.add(rmstId);
		if(smanId!=null){
			hql += " and rmp.smanId !=  " + smanId;
			//param.add(smanId);
		}
		return getHibernateTemplate().find(hql,param.toArray());
	}

	@Override
	public ResMissionInfo getResMissionInfoById(long rminId) {
		ResMissionInfo mission = (ResMissionInfo) getHibernateTemplate().get(ResMissionInfo.class, rminId);
		return mission;
	}

	@Override
	public boolean updateMissionStatus(long missionId, long statu) {
		try {
			ResMissionInfo mission = getResMissionInfoById(missionId);
			Integer defaultStatu = mission.getResMetaStore().getRmstResourceAudit();
			try {
				statu = Long.parseLong(defaultStatu.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			mission.setRminStatus(statu);
			mission.setRminEnddate( new Date());
			getHibernateTemplate().update(mission);
			missionLanguageService.updateMissionFirstStatu(missionId, Integer.valueOf(statu + ""));
			
			String hql = "update ResSourceData rsda set rsda.rsdaStatus=" + statu + " where rsda.rsdaRminId=" + missionId;
			getHibernateTemplate().bulkUpdate(hql);
			return true;
		} catch (Exception e) {
			System.out.println("异常：" + e.getMessage());
			return false;
		}
	}

	public String getMissionNameById(long rminId) {
		String hql = "select rmin.rminMissname from ResMissionInfo rmin where rmin.rminId=" + rminId;
		Object name = getFirstDataByHql(hql);
		if(name != null){
			return name.toString();
		}else{
			return "";
		}
	}

	@Override
	public List getResSourceDataList(Long rminId, Long rsdaStatus) {
		List<Object> param = new ArrayList<Object>();
		String hql = "from ResSourceData rsd where rsd.rsdaRminId = ? ";
		param.add(rminId);
		if(rsdaStatus != null){
			hql+=" and rsd.rsdaStatus != 3 ";
		}
		hql+=" order by rsd.rsdaId DESC ";
		return this.getHibernateTemplate().find(hql,param.toArray());
	}

	@Override
	public List getOtherLanguageResSourceDataList(Long rminId) {
		String hql = "from ResSourceLanguage rsl where rsl.resSourceData.rsdaRminId = ? and rsl.rslaStatus != 3  ";
		return this.getHibernateTemplate().find(hql,new Object[]{rminId});
	}

	public boolean setMissionUser(int userType, long userid, long missionId, long languageId) {
		boolean b = false;
		System.out.println("userType:" + userType);
		switch (userType) {
		case 2:
			b = missionLanguageService.updateSingleColumn(missionId, languageId, IMissionLanguageTempService.rmlaAssessor, userid);
			break;
		case 3:
			b = setFirstCheck(missionId, userid, languageId);
			break;
		case 4:
			b = missionLanguageService.updateSingleColumn(missionId, languageId, IMissionLanguageTempService.rmlaAssessor, userid);
			break;
		default:
			b = false;
			break;
		}
		return b;
	}
	
	public boolean setFirstCheck(long rminId, long userId, long languageid){
		try {
			String hql = "update ResMissionInfo set rminCheckuser="+userId + " where rminId=" + rminId;
			getHibernateTemplate().bulkUpdate(hql);
			hql = "update ResMissionLanguage set rmlaAssessor=" + userId + " where rmlaIsfirst=true and resMissionId=" + rminId;
			getHibernateTemplate().bulkUpdate(hql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public long getMissionCountSource(long missionId) {
		String hql = "select count(rsda.rsdaId) from ResSourceData rsda where rsda.rsdaRminId=?";
		List<Long> list = getHibernateTemplate().find(hql, missionId);
		if (list != null && list.size() > 0) {
			long count = list.get(0);
			list = null;
			return count;
		} else {
			return 0;
		}
	}
	
	
	public String getJsonData(final String page_sql, final List paramlist, final int begin, final int num){
		String jsondata = getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer buffer = new StringBuffer();
				try {
					Connection conn = session.connection();
					String countSql = SqlBasicUnit.parseCountSql(page_sql);
					String pageSql = SqlBasicUnit.parseSql(page_sql, begin, num);
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(countSql);
					int count = 0;
					if(rs.next()){
						count = rs.getInt(1);
					}
					
				    PreparedStatement ps = conn.prepareStatement(pageSql);
				    if(paramlist != null && paramlist.size() > 0){
				    	for(int i=1; i<= paramlist.size(); i++){
				    		ps.setObject(i, paramlist.get(i -1));
				    	}
				    }
				    
					rs = ps.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData();
					int s = rsmd.getColumnCount();
					
					buffer.append("{");
					buffer.append("totalProperty:"+count+",root:[");
					String dataValue = "";
					Date date = null;
					while(rs.next()){
						buffer.append("{");
						for(int i = 1; i <= s; i++){
							if(rsmd.getColumnTypeName(i).toUpperCase().equals("DATE")){
								if(ToolUnit.checkDateTime(rs.getString(rsmd.getColumnName(i)))){
									dataValue = rs.getString(rsmd.getColumnName(i));
									dataValue = dataValue.substring(0, dataValue.length()-2);
								}else{
									Calendar calendar = Calendar.getInstance();
									date = rs.getDate(rsmd.getColumnName(i));
									if(date == null){
										dataValue = "";
									}else{
										if(calendar.get(Calendar.HOUR_OF_DAY) == 0 && calendar.get(Calendar.MINUTE) == 0
												&& calendar.get(Calendar.SECOND) == 0) {
											dataValue = ConstantsObj.FORMAT_DATE.format(date);
										}else {
											dataValue = ConstantsObj.FORMAT_DATE_TIME.format(date);
										}
									}
								}
							}else{
								try {
									dataValue = rs.getString(rsmd.getColumnName(i));
									dataValue = ToolUnit.stringToJson(dataValue);
								} catch (Exception e) {
									dataValue = "";
								}
							}
							String tempColumnName = rsmd.getColumnName(i);
						    buffer.append(tempColumnName+":\""+ dataValue + "\"");
						    if("RMIN_ID".equals(tempColumnName)){
						    	buffer.append(",RMIN_LANGUAGE_TYPE:\""+ getRminLanguageType(dataValue) + "\"");
						    }
						    if(i < s){
				            	buffer.append(",");
				            }
						}
						buffer.append("},");
					}
					rsmd = null;
					ps = null;
					String jsonData = buffer.toString();
					buffer = null;
					if(jsonData.length() > 25){
						jsonData = jsonData.substring(0,jsonData.length()-1);
					}
					jsonData += "]}";
					return jsonData;
				} catch (SQLException e) {
					System.out.println("在MissionInfoManagement中的getJsonData发生异常，异常SQL》 " +page_sql);
					e.printStackTrace();
					return "";
				}
			}
			
		}).toString();
		
		return jsondata;
	}
	
	
	private String getRminLanguageType(String rminId){
		String sql = "select rlt.RLTY_NAME from RES_MISSION_LANGUAGE rml inner join RES_LANGUAGE_TYPE rlt on rml.RMLA_RLTY_ID = rlt.RLTY_ID where rml.RMLA_STATUS = 2 and  rml.RMLA_RMIN_ID = "+rminId;
		List<Object> list = getSingleColoumList(sql, null);
		String result = "";
		for(int i = 0,s=list.size(); i < s; i++) {
			result += list.get(i).toString();
			if(i<s-1){
				result+="、";
			}
		}
		return result;
	}

	public long saveResource(ResSourceData resource, List<ResElementPool> elementList, HttpServletRequest request) {
		try {
			//同步资源题名的首字母
			String en = PinYin2Abbreviation.cn2py(resource.getRsdaTitle());
			resource.setRsdaTitleEn(en);
			
			Serializable sid = getHibernateTemplate().save(resource);
			if(sid == null){
				return 0;
			}
			long sourceId = Long.parseLong(sid.toString());
			long languageId = MyRequestUtil.getLongParameter(request, "language_id");
			//封装保存资源语种记录
			ResSourceLanguage rsl = new ResSourceLanguage();
			rsl.setResLanguageId(languageId);
			rsl.setResSourceId(sourceId);
			rsl.setRslaDate(new Date().toLocaleString());
			rsl.setRslaStatus(2L);
			rsl.setRslaUserId(MyRequestUtil.getLoginUserId(request));
			rsl.setRslaUsername(MyRequestUtil.getLoginUserName(request));
			rsl.setRslaAuthorName(MyRequestUtil.getSmanName(request));
			rsl.setRslaIsfirst(true);
			
			getHibernateTemplate().save(rsl);
			
			long myid;
			String eleid = "", dataValue="", sql = "";
			List<String> sqlList = new LinkedList<String>();
			// 循环保存所有元素值到数据库
			for(int i = 0, s = elementList.size(); i < s; i++) {
				eleid = elementList.get(i).getRepoId().toString();
				myid = IdGenerator.generateId();
				dataValue = MyRequestUtil.getStringParameter(request, eleid);
				sql = "insert into RES_EXTEND_DATA(REDA_ID,REDA_REPO_ID,REDA_RSDA_ID,REDA_DATA,REDA_LANGUAGE) values(" + myid + "," + eleid	+ "," + sourceId + ",'" + dataValue.replace("'", "''") + "'," + languageId + ")";
				sqlList.add(sql);
			}
			executeMultiSql(sqlList);
			
			//更新作业的数量
			if(resource.getRsdaRminId() != null){
				String hql = "update ResMissionInfo rmin set rmin.rminResCount=rmin.rminResCount+1 where rmin.rminId=?";
				getHibernateTemplate().bulkUpdate(hql, resource.getRsdaRminId());
			}
			return sourceId;
		} catch (Exception e) {
			logger.error("保存资源发生异常：" + e.getMessage());
			e.printStackTrace();
			return 0;
		}
		
	}

	@Override
	public boolean checkFirstLanguage(ResMissionInfo missionInfo, Long rminId) {
		try {
			save(missionInfo);
			String hql = "update ResMissionLanguage set rmlaAssetime=?, rmlaStatus="+missionInfo.getRminStatus()+" where resMissionId="+rminId+" and resLanguageId=" + missionInfo.getRminLanugage();
			getHibernateTemplate().bulkUpdate(hql, new Date());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public ResMissionInfo saveMissionInfo(ResMissionInfo mission) {
		ResMissionInfo s = (ResMissionInfo) save(mission);
		try {
			long missionId = s.getRminId();
			ResMissionLanguage missionLanguage = new ResMissionLanguage();
			missionLanguage.setResLanguageId(mission.getRminLanugage());
			missionLanguage.setResMissionId(missionId);
			missionLanguage.setRmlaIsfirst(true);
			missionLanguage.setRmlaStatus(ResMissionLanguage.STATU_PROCESSING);
			missionLanguage.setRmlaProcessor(mission.getRminUser());
			save(missionLanguage);
		} catch (Exception e) {
			logger.error("异常：" + e.getMessage());
			e.printStackTrace();
		}
		return mission;
	}

	@Override
	public String getSourceViewHtml(List<ResElementPool> elementList,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	public String getMissionDefaultName() {
		String y, m, d;
		Calendar cal = Calendar.getInstance();
		y = cal.get(Calendar.YEAR) + "";
		m = (cal.get(Calendar.MONTH) + 1) + "";
		d = cal.get(Calendar.DATE) + "";
		if(m.length() < 2){
			m = "0" + m;
		}
		if(d.length() < 2){
			d = "0" + d;
		}
		
		cal = null;
		String folder = y + m + d + "_";
		
		if(missionNameCode < 1000){
			String lastName = getLastMissionName();
			String[] arr = lastName.split("_");
			int nameNumberCode;
			if(arr != null && arr.length > 1){
				try {
					nameNumberCode = Integer.parseInt(arr[1]);
//				folder += Integer.parseInt(arr[1]) + 1;
				} catch (Exception e) {
					//如果发生异常，则随机生成一个10000-20000的序号
					java.util.Random r=new java.util.Random(); 
					nameNumberCode = Math.abs(r.nextInt())% (int)(Math.pow(10000, 1)) + 10000;
					e.printStackTrace();
				}
			}else{
				nameNumberCode = 1000;
			}
			missionNameCode = nameNumberCode + 1;
		}else{
			missionNameCode = missionNameCode + 1;
		}
		folder += missionNameCode;
		return folder;
	}
	
	private String getLastMissionName(){
		String hql = "select rmin.rminMissname from ResMissionInfo rmin order by rmin.rminBuilddate desc";
		String lastMisssionName;
		try {
			List<String> list = getHibernateTemplate().find(hql);
			if(list == null || list.size() < 1){
				lastMisssionName = "";
			}else{
				lastMisssionName = list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			lastMisssionName = "";
		}
		return lastMisssionName;
	}

	@Override
	public long saveAttachmentFile(ResSourceFile resFile) {
		try{
			Serializable sid = getHibernateTemplate().save(resFile);
			if(sid == null){
				return 0;
			}else{
				return Long.parseLong(sid.toString());
			}
		}catch(Exception e){
			
			logger.error("保存资源附件发生异常：" + e.getMessage());
			e.printStackTrace();
			return 0;
		}	
	}
	
	public List<ResSourceFile> getResSourceFileToResSourceId(Long resId){
		List<ResSourceFile> list = null;
		try {
			list = getHibernateTemplate().find("from ResSourceFile where resRsdaId = '"+resId+"' order by resIndex asc");
		} catch (Exception e) {
			logger.error("查找资源附件发生异常：" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	public boolean deleteResSourceFile(Long resFId){
		try {
			ResSourceFile resSourceFile = (ResSourceFile)getHibernateTemplate().get(ResSourceFile.class, resFId);
//			int oldIndex = resSourceFile.getResIndex(); 
//			List<ResSourceFile> list = getHibernateTemplate().find("from ResSourceFile where resRsdaId = '"+resSourceFile.getResRsdaId()+"' and resIndex >'"+oldIndex+"' order by resIndex asc");
//			if(null!=list){
//				for(int i=0,s=list.size();i<s;i++){
//					ResSourceFile indexFile = list.get(i);
//					int ascindex = indexFile.getResIndex();
//					indexFile.setResIndex(ascindex-1);
//					getHibernateTemplate().save(indexFile);
//				}
//			} 
			
			String fileRoot = SysConfigInfo.FILE_ROOT_DIR;
			String fileloaclpath = resSourceFile.getResLocalPath();
			String filePath = fileRoot + fileloaclpath;
			fileloaclpath = fileloaclpath.substring(0, fileloaclpath.lastIndexOf("."));
			File file = new File(filePath);
			
			if(file.exists()){
				file.delete();
			}
			
			file = new File(fileRoot + fileloaclpath + ".swf");
			if(file.exists()){
				file.delete();
			}
			file = new File(fileRoot + fileloaclpath + ".flv");
			if(file.exists()){
				file.delete();
			}
			file = new File(fileRoot + fileloaclpath + ".jpg");
			if(file.exists()){
				file.delete();
			}
			file = new File(fileRoot + fileloaclpath + ".mp3");
			if(file.exists()){
				file.delete();
			}
			getHibernateTemplate().delete(resSourceFile);
		}catch (Exception e) { 
			logger.error("删除资源附件发生异常："+e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	} 
	
	public boolean updateResSourceFileIndex(Long resFId , int index){
		try {
			if(index<1){
				index=1;
			}
			ResSourceFile resSourceFile = (ResSourceFile)getHibernateTemplate().get(ResSourceFile.class, resFId);
			int oldIndex = resSourceFile.getResIndex(); 
			List<ResSourceFile> list = getHibernateTemplate().find("from ResSourceFile where resRsdaId = '"+resSourceFile.getResRsdaId()+"' and resIndex >'"+oldIndex+"' order by resIndex asc");
			if(null!=list){
				for(int i=0,s=list.size();i<s;i++){
					ResSourceFile indexFile = list.get(i);
					int ascindex = indexFile.getResIndex();
					indexFile.setResIndex(ascindex-1);
					getHibernateTemplate().save(indexFile);
				}
			}
			List<ResSourceFile> list2 = getHibernateTemplate().find("from ResSourceFile where resRsdaId = '"+resSourceFile.getResRsdaId()+"' and resIndex >='"+index+"' order by resIndex asc");
			if(null!=list){
				for(int i=0,s=list2.size();i<s;i++){
					ResSourceFile indexFile = list2.get(i);
					int ascindex = indexFile.getResIndex();
					indexFile.setResIndex(ascindex+1);
					getHibernateTemplate().save(indexFile);
				}
			}
			resSourceFile.setResIndex(index);
			getHibernateTemplate().save(resSourceFile);
		} catch (Exception e) {
			logger.error("修改资源附件排序发生异常："+e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

	public int getFileIndex(Long resSourceId){
		try {
			List<ResSourceFile> list = getHibernateTemplate().find("from ResSourceFile where resRsdaId = '"+resSourceId+"' order by resIndex desc");
			if(list.size()>0){
				return list.get(0).getResIndex();
			}else{
				return 1;
			}
		} catch (Exception e) {
			logger.error("查询资源附件排序发生异常："+e.getMessage());
			e.printStackTrace();
			return 1;
		}
	}

	@Override
	public void deleteSubjectByRminId(long rminId) {
		String hql = "delete from ResSourceSubject a where a.rssuRminId = ? ";
		getHibernateTemplate().bulkUpdate(hql, rminId);
	}

	@Override
	public List getResSourceDataByMission(Long rminId) {
		String hql = "from ResSourceData rsd where rsd.rsdaRminId = ? ";
		return this.getHibernateTemplate().find(hql,rminId);
	}
	
	
}















