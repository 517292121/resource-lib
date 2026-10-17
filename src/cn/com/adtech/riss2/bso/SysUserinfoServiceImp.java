package cn.com.adtech.riss2.bso;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.domain.SysRole;
import cn.com.adtech.riss2.domain.SysUserType;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.count.SysAccessCounter;

public class SysUserinfoServiceImp extends CommonServiceImp implements ISysUserinfoService {
	@Override
	public Wrapper getAllUsersBySearchCondition(String condition, Boolean suinIsStop, PageExtNative page) {
		String hqlCount = "select count(*) from SysUserinfo sui where sui.suinLoginName is not null";
		String hql = "select sui.suinId, sui.suinLoginName, sui.suinPassword, sui.suinName, "
				+ "sui.suinSex, sui.suinBirthday, sui.suinEmail,sui.suinIsStop, sui.suinJoinDate,"
				+ "sui.suinLastvisitDate, sui.suinMobileTelephone, sui.suinState,"
				+ "sui.suinAuditMode,sui.suinQuestion, sui.suinAnswer, sui.suinHint, "
				+ "sut.sutyId, sut.sutyName from SysUserinfo sui, SysUserType sut " + "where sui.sysUserType.sutyId=sut.sutyId";
		String suffixHql = ""; 
		int param = 0;
		if(condition != null && !condition.trim().equals("")) {
			suffixHql = " and (sui.suinLoginName like ? or sui.suinName like ? )";
			hqlCount += suffixHql;
			hql += suffixHql;
		}
		if(suinIsStop != null) {
			hql += " and sui.suinIsStop=?";
			hqlCount += " and sui.suinIsStop=?";
		}
		if(page != null) {
			if(ServiceUtil.isNotNull(page.getSort()) && ServiceUtil.isNotNull(page.getDir())) {
				if(!page.getSort().trim().equals("sutyName")) {
					hqlCount += " order by sui." + page.getSort() + " " + page.getDir();
					hql += " order by sui." + page.getSort() + " " + page.getDir();
				} else {
					hqlCount += " order by sut." + page.getSort() + " " + page.getDir();
					hql += " order by sut." + page.getSort() + " " + page.getDir();
				}
			}
		}
		Session s = this.getSession();
		Query q1 = s.createQuery(hqlCount);
		Long listCount = 0L;
		Query q = s.createQuery(hql);
		if(condition != null && !condition.trim().equals("")) {
			q.setParameter(param, "%" + condition.trim() + "%");
			q1.setParameter(param, "%" + condition.trim() + "%");
			param++;
			q.setParameter(param, "%" + condition.trim() + "%");
			q1.setParameter(param, "%" + condition.trim() + "%");
			param++;
		} 
		if(suinIsStop != null) {
			q.setParameter(param, suinIsStop);
			q1.setParameter(param, suinIsStop);
		} 
		if(page != null && page.getLimit() != 0) {
			q.setFirstResult(page.getStart());
			q.setMaxResults(page.getLimit());
		}
		List<SysRole> list = q.list();
		listCount = ((Long) q1.uniqueResult());
		releaseSession(s);
		return new Wrapper(list, String.valueOf(listCount.longValue()));
	}

	@Override
	public void deleteUsers(Long[] userIds) {
		if(userIds != null && userIds.length > 0) {
			for(int i = 0; i < userIds.length; i++) {
				String hql = "update SysAccessCounter set sacoSuinId=null where sacoSuinId = " + userIds[i];
				getHibernateTemplate().bulkUpdate(hql);
				hql = "delete from RssUserData where rssDataUserId="+userIds[i];
				getHibernateTemplate().bulkUpdate(hql);
				this.getHibernateTemplate().delete(this.get(SysUserinfo.class, userIds[i]));
			}
		}
	}
	/**
	 * 判断用户是否参与文献求助,资源评论
	 * @param userIds
	 * @return
	 */
	@Override
	public Boolean getIssu(Long[] userIds){
		for(int i = 0; i < userIds.length; i++) {
			String hqlSsue = "select count(*) from ScIssue where scisSuinId = " + userIds[i];
			String hqlSrsu = "select count(*) from ScIssueReply where scirSuinId = "+ userIds[i];
			String hqlComt = "select count(*) from ResSourceComt where rscoSuinId = "+ userIds[i] +"or rscoRefid = "+ userIds[i];
			Long count3 = super.getCountNumber(hqlComt);
			Long count1 = super.getCountNumber(hqlSsue);
			Long count2 = super.getCountNumber(hqlSrsu);
			if(count1>0 || count2>0 || count3>0)
				return false;
		}	
		return true;
	}
	@Override
	public void updateUsersPassword(Long[] userIds, String newPassword) {
		if(userIds != null && userIds.length > 0) {
			for(int i = 0; i < userIds.length; i++) {
				SysUserinfo sui = ((SysUserinfo)this.get(SysUserinfo.class, userIds[i]));
				sui.setSuinPassword(newPassword);
				this.getHibernateTemplate().update(sui);
			}
		}
	}

	@Override
	public void updateUsersState(Long[] userIds) {
		if(userIds != null && userIds.length > 0) {
			for(int i = 0; i < userIds.length; i++) {
				SysUserinfo sui = ((SysUserinfo)this.get(SysUserinfo.class, userIds[i]));
				sui.setSuinIsStop(!sui.getSuinIsStop());
				this.getHibernateTemplate().update(sui);
			}
		}
	}

	@Override
	public Wrapper getAllNotAuditedUsersBySearchCondition(String condition, String selectUserState, PageExtNative page) {
		String hqlCount = "select count(*) from SysUserinfo sui where sui.suinLoginName is not null and (sui.suinState=0 or sui.suinState=-1) ";
		String hql = "select sui.suinId, sui.suinLoginName, sui.suinPassword, sui.suinName, "
				+ "sui.suinSex, sui.suinBirthday, sui.suinEmail,sui.suinIsStop, sui.suinJoinDate,"
				+ "sui.suinLastvisitDate, sui.suinMobileTelephone, sui.suinState,"
				+ "sui.suinAuditMode,sui.suinQuestion, sui.suinAnswer, sui.suinHint,"
				+ "sut.sutyId, sut.sutyName from SysUserinfo sui, SysUserType sut "
				+ "where sui.sysUserType.sutyId=sut.sutyId ";
		if(selectUserState != null && !selectUserState.trim().equals("")) {
			if(selectUserState.equals("all")) {
				hql += "and (sui.suinState="+SysUserinfo.STATE_NOT_AUDIT+" or sui.suinState="+SysUserinfo.STATE_NOT_PAST+")";
			} else if(selectUserState.equals(String.valueOf(SysUserinfo.STATE_NOT_AUDIT.shortValue())) || selectUserState.equals(String.valueOf(SysUserinfo.STATE_NOT_PAST.shortValue()))) {
				hql += "and sui.suinState="+Short.parseShort(selectUserState);
			}
		} else {
			hql += "and sui.suinState=" + SysUserinfo.STATE_NOT_AUDIT;
		}
		String suffixHql = " and (sui.suinLoginName like '%" + condition + "%' or sui.suinName like '%" + condition + "%') ";
		hqlCount += suffixHql;
		hql += suffixHql;
		if(page != null) {
			if(ServiceUtil.isNotNull(page.getSort()) && ServiceUtil.isNotNull(page.getDir())) {
				hqlCount += " order by sui." + page.getSort() + " " + page.getDir();
				hql += " order by sui." + page.getSort() + " " + page.getDir();
			}
		}
		Session s = this.getSession();
		List listCount = this.getHibernateTemplate().find(hqlCount);
		Query q = s.createQuery(hql);
		if(page != null && page.getLimit() != 0) {
			q.setFirstResult(page.getStart());
			q.setMaxResults(page.getLimit());
		}
		List<SysRole> list = q.list();
		releaseSession(s);
		return new Wrapper(list, listCount.get(0).toString());
	}

	@Override
	public void updateUsersTypes(Long[] userIds, Long userTypeId) {
		if(userIds != null && userIds.length > 0 && userTypeId != null) {
			for(int i = 0; i < userIds.length; i++) {
				getHibernateTemplate().bulkUpdate("update SysUserinfo sui set sui.sysUserType=? where sui.suinId=? ", new Object[] {get(SysUserType.class, userTypeId), userIds[i]});
			}
		}
	}
}
