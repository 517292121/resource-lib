package cn.com.adtech.riss2.bso.resource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceLanguage;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class ResMissionInfoCheckServiceImp extends CommonServiceImp implements IResMissionInfoCheckService {
	@Override
	public List getResSourceDataList(Pagination pagination, Map<String, Object> conditions) {
		List<Object> param = new ArrayList<Object>();
		String selectHql = "select rsd.rsdaId , rsd.rsdaTitle , rsd.resLiterType.rltyName , rsd.sysManager.smanName , rsd.rsdaBuilddate , rsd.rsdaStatus , rsd.rsdaRltyId , rsd.rsdaCreator, rsd.rsdaPublisher ";
		StringBuilder fromHql = new StringBuilder("from ResSourceData rsd where rsd.rsdaRminId = ? ");
		StringBuilder orderHql = new StringBuilder("order by ");
		if(conditions != null && conditions.size() > 0) {
			Long rminId = (Long)conditions.get("rminId");
			if(rminId != null && rminId > 0)
				param.add(rminId);
			Long rsdaStatus = (Long)conditions.get("rslaStatus");
			String searchtitle = (String) conditions.get("searchtitle");
			String otherLanguageRSD = (String)conditions.get("otherLanguageRSD");
			
			if (searchtitle !=null && !searchtitle.equals("")) {
				fromHql.append(" and rsd.rsdaTitle like ? ");
				param.add("%"+searchtitle+"%");
			}
			if(rsdaStatus != null && rsdaStatus > 0) {
				fromHql.append(" and rsd.rsdaStatus = ? ");
				param.add(rsdaStatus);
			}else if("1".equals(otherLanguageRSD)) {
				// 添加一列：资源语种列记录
				selectHql += " ,( select count(*) from ResSourceLanguage rsl_ where rsl_.resSourceId = rsd.rsdaId and rsl_.rslaStatus !=3 and rsl_.rslaIsfirst = 0 ) as rslaNum ";
			}
			String sort = (String)conditions.get("sort");
			String dir = (String)conditions.get("dir");
			// 添加排序方式
			if(sort != null && sort.length() > 0 && dir != null && dir.length() > 0) {
				if("rsdaTitle".equals(sort))
					orderHql.append(" rsd.rsdaTitle ");
				else if("rslaUsername".equals(sort))
					orderHql.append(" rsd.sysManager.smanName ");
				else if("rslaDate".equals(sort))
					orderHql.append(" rsd.rsdaBuilddate ");
				else if("rslaStatus".equals(sort))
					orderHql.append(" rsl.rslaStatus ");
				else
					orderHql.append(" rsd.rsdaStatus ");
				orderHql.append(dir);
			}
		}
		String countHql = "select count(*) " + fromHql.toString();
		String hql = selectHql + fromHql.toString() + orderHql.toString();
		List lists = getPageHibernateTemplate().find(pagination, hql, countHql, param.toArray());
		return lists;
	}
	@Override
	public List getResSourceDataDetailInfo(Long rsdaId, Long rmstId, Long rltyId, String type, Long rltyId2) {
		List parmaList = new ArrayList();
		String selectSql = "SELECT rep.repo_title , rep.repo_name , red.reda_data , rep.repo_parent ,rep.REPO_ID ";
		String fromSql = "FROM res_extend_data red INNER JOIN res_element_pool rep ON red.reda_repo_id = rep.repo_id WHERE red.reda_rsda_id = ? ";
		if("1".equals(type)) {
			selectSql += " , ( select red_.REDA_DATA from res_extend_data red_ where red_.REDA_REPO_ID = red.reda_repo_id and red_.REDA_LANGUAGE = ? and red_.REDA_RSDA_ID = red.REDA_RSDA_ID ) as otherLanguage ";
			parmaList.add(rltyId2);
		}
		String sql = selectSql + fromSql;
		parmaList.add(rsdaId);
		if(rltyId != null) {
			sql += " and red.REDA_LANGUAGE = ?  ";
			parmaList.add(rltyId);
		}
		sql += " ORDER BY rep.repo_id ASC  ";
		List list = getStrList(sql, parmaList);
		return list;
	}

	@Override
	public void updateResSourceLanguageStatus(Long rsdaId, int isFirst, Long status) {
		String sql = "update RES_SOURCE_LANGUAGE set RSLA_STATUS = " + status + " where RSLA_RSDA_ID = " + rsdaId + " and RSLA_ISFIRST = "
				+ isFirst;
		executeUpdate(sql);
	}

	@Override
	public List getRSDFirstLanguage(Long rsdaId, Boolean isFirst, String type, Long rslaRltyId) {
		List<Object> params = new ArrayList<Object>();
		String selectHql = "";
		String fromHql = "from ResSourceLanguage rsl where rsl.resSourceId = ? ";
		params.add(rsdaId);
		if("1".equals(type)) {
			selectHql = "select rsl.resLanguageId ";
		}
		if(rslaRltyId != null) {
			fromHql += " and rsl.resLanguageId =  ?";
			params.add(rslaRltyId);
		}
		if(isFirst != null) {
			fromHql += "and rsl.rslaIsfirst = ? ";
			params.add(isFirst);
		}
		return this.getHibernateTemplate().find(selectHql + fromHql, params.toArray());
	}

	@Override
	public long getUserId(long rminId, String userType) {
		String hql = "select rmi." + userType + " from ResMissionInfo rmi where rmi.rminId=?";
		List<Long> list = getHibernateTemplate().find(hql, rminId);
		if(list == null || list.size() < 1) {
			return 0;
		}else {
			try {
				long id = list.get(0);
				list = null;
				return id;
			}catch(Exception e) {
				return 0;
			}
		}
	}

	@Override
	public List getNextRSD(Long rminId, int authorType,String titlename) {
		
		if(authorType != ResSourceData.AUTHORTYPE_ADMIN_INPUT && authorType != ResSourceData.AUTHORTYPE_ADMIN_IMPORT){
			String hql = "select rsd.rsdaId , rsd.rsdaRltyId from ResSourceData rsd where rsd.rsdaStatus =2 and rsd.rsdaAuthortype="+authorType;
			return findFirst(hql);
		}else{
			String hql = "select rsd.rsdaId , rsd.rsdaRltyId from ResSourceData rsd where rsd.rsdaTitle like '%"+titlename+"%' and rsd.rsdaRminId="+rminId+" and rsd.rsdaStatus =2 ";
			return findFirst(hql);
		}
	}

	private List findFirst(final String hql) {
		return (List)this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setMaxResults(1);
				return query.list();
			}
		});
	}
	@Override
	public List getNextOtherLanguageRSD(Long rsdaId , Long rminId, Long rltyId) {
		String hql = "select rsd.rsdaId  from ResSourceLanguage rsl inner join rsl.resSourceData rsd where rsd.rsdaId != ? and  rsd.rsdaRminId = ? and rsl.resLanguageId = ?  and rsl.rslaStatus != 3 order by rsd.rsdaId DESC  ";
		return this.getHibernateTemplate().find(hql, new Object[] {rsdaId,rminId,rltyId});
	}

	@Override
	public List getResSourceDataList(Pagination pagination, String sort, String dir, Long rminId, Long rltyId, Long status, String title) {
		List<Object> param = new ArrayList<Object>();
		String selectHql = "select rsd.rsdaId , rsd.rsdaTitle , rsd.resLiterType.rltyName , rsl.rslaUsername , rsl.rslaDate , rsl.rslaStatus , rsl.resLanguageId , rsd.rsdaCreator, rsd.rsdaPublisher ";
		StringBuilder fromHql = new StringBuilder(
				"from ResSourceLanguage rsl inner join rsl.resSourceData rsd where rsd.rsdaRminId = ? and rsl.resLanguageId = ?  ");
		StringBuilder orderHql = new StringBuilder("order by ");
		param.add(rminId);
		param.add(rltyId);
		// 添加查询条件
		if(status > 0) {
			fromHql.append(" and rsl.rslaStatus = ? ");
			param.add(status);
		}
		if(title != null && title.length() > 0){
			fromHql.append(" and rsd.rsdaTitle like '%"+title+"%'");
		}
		// 添加排序方式
		if(sort != null && sort.length() > 0 && dir != null && dir.length() > 0) {
			if("rsdaTitle".equals(sort))
				orderHql.append(" rsd.rsdaTitle ");
			else if("smanName".equals(sort))
				orderHql.append(" rsl.rslaUsername ");
			else if("rslaDate".equals(sort))
				orderHql.append(" rsl.rslaDate ");
			else if("rslaStatus".equals(sort))
				orderHql.append(" rsl.rslaStatus ");
			else if("rltyName".equals(sort))
				orderHql.append(" rsd.resLiterType.rltyName ");
			orderHql.append(dir);
		}
		String countHql = "select count(*) " + fromHql.toString();
		String hql = selectHql + fromHql.toString() + orderHql.toString();
		List lists = getPageHibernateTemplate().find(pagination, hql, countHql, param.toArray());
		return lists;
	}

	public boolean updateResourceChecked(ResSourceData resource, long statu) {
		try {
			this.save(resource);
			String sql = "update RES_SOURCE_LANGUAGE set RSLA_STATUS = " + statu + " where RSLA_RSDA_ID = " + resource.getRsdaId();
			boolean b = executeUpdate(sql);
			return b;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	@Override
	public ResSourceLanguage getResSourceLanguage(Long rsdaId, int isFirst) {
		String hql = "from ResSourceLanguage rsl where rsl.rslaIsfirst = " + isFirst + " and rsl.resSourceId = "+rsdaId ;
		List list = this.getHibernateTemplate().find(hql);
		if(list.size()>0){
			return (ResSourceLanguage)list.get(0);
		}
		return null;
	}

	@Override
	public void updateBatchCheckResSourceData(String rsdaIds, Long rsdaStatus, SysManager sysManager) {
		getHibernateTemplate().bulkUpdate("update ResSourceData resSourceData set resSourceData.rsdaStatus = " + rsdaStatus + " where resSourceData.rsdaId in (" + rsdaIds + ")");
		String updateLanguage = "update ResSourceLanguage language set language.rslaStatus=" + rsdaStatus + ",language.rslCheckLoginname='" + sysManager.getSmanLoginName();
		updateLanguage += "',language.rslCheckSuinname='"+sysManager.getSmanName()+"' where language.rslaIsfirst=1 and language.resSourceId in (" + rsdaIds + ")";
		getHibernateTemplate().bulkUpdate(updateLanguage);
	}

	/**
	 * 根据作业ID批量审核作业下所有资源
	 * @param rminId
	 * @param rsdaStatus
	 */
	public void updateCheckAllByMission(long rminId, long rsdaStatus, SysManager sysManager) {
		String updateLanguage = "update ResSourceLanguage language set language.rslaStatus=" + rsdaStatus + ",language.rslCheckLoginname='" + sysManager.getSmanLoginName();
		updateLanguage += "',language.rslCheckSuinname='"+sysManager.getSmanName()+"' where language.rslaIsfirst=1 and language.resSourceId in (SELECT rsdaId FROM ResSourceData WHERE rsdaRminId="+rminId+")";
		getHibernateTemplate().bulkUpdate(updateLanguage);
		getHibernateTemplate().bulkUpdate("update ResSourceData resSourceData set resSourceData.rsdaStatus = " + rsdaStatus + " where resSourceData.rsdaRminId =" + rminId);
	}	
}
