package cn.com.adtech.riss2.bso;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.domain.SysRole;
import cn.com.adtech.riss2.domain.SysUserType;
import cn.com.adtech.riss2.exception.ErrorMessageException;

@SuppressWarnings("unchecked")
public class SysUserTypeServiceImp extends CommonServiceImp implements ISysUserTypeService {
	@Override
	public Wrapper getAllUserTypesBySearchCondition(String condition, PageExtNative page) {
		String hqlCount = "select count(*) from SysUserType sut";
		String hql = "select sut.sutyId,sut.sutyName,sut.sutyDoc," + "sut.sutyPic,sut.sutyVideo,sut.sutyDefault,sut.sutyPurview from SysUserType sut";
		if(condition != null) {
			hqlCount += " where sut.sutyName like '%" + condition + "%'";
			hql += " where sut.sutyName like '%" + condition + "%'";
		}
		if(page != null) {
			if(ServiceUtil.isNotNull(page.getSort()) && ServiceUtil.isNotNull(page.getDir())) {
				hqlCount += " order by sut." + page.getSort() + " " + page.getDir();
				hql += " order by sut." + page.getSort() + " " + page.getDir();
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
		return new Wrapper(list, listCount.get(0).toString());
	}

	@Override
	public void updateUserType(SysUserType userType, Long sutyId) {
		String confirmNameHql = "select ut from SysUserType ut where ut.sutyName=?";
		List list = this.getHibernateTemplate().find(confirmNameHql, new Object[] {userType.getSutyName()});
		if(list != null && list.size() > 0 && !((SysUserType)list.get(0)).getSutyId().equals(sutyId)) {
			throw new ErrorMessageException("用户类型名重复，请重新填写!");
		}
		SysUserType sysUserType = (SysUserType)get(SysUserType.class, sutyId);
		sysUserType.setSutyDefault(userType.getSutyDefault());
		sysUserType.setSutyDoc(userType.getSutyDoc());
		sysUserType.setSutyName(userType.getSutyName());
		sysUserType.setSutyPic(userType.getSutyPic());
		sysUserType.setSutyVideo(userType.getSutyVideo());
		sysUserType.setSutyPurview(userType.getSutyPurview());
		getHibernateTemplate().saveOrUpdate(sysUserType);
		if(sysUserType.getSutyDefault()){
			getHibernateTemplate().bulkUpdate("update SysUserType sysUserType set sysUserType.sutyDefault=false where sysUserType.sutyId!="+sutyId);
		}
	}

	@Override
	public SysUserType getSysUserTypeById(Long id) {
		return (SysUserType)this.get(SysUserType.class, id);// this.getHibernateTemplate().get(SysUserType.class, id);
	}

	@Override
	public SysUserType saveSysUserType(SysUserType sut) {
		String confirmNameHql = "select ut from SysUserType ut where ut.sutyName=?";
		List list = this.getHibernateTemplate().find(confirmNameHql, new Object[] {sut.getSutyName()});
		if(list != null && list.size() > 0) {
			throw new ErrorMessageException("用户类型名重复，请重新填写!!!");
		}
		if(sut.getSutyDefault() == true) {
			String hql = "update cn.com.adtech.riss2.domain.SysUserType sut set sut.sutyDefault=false where sut.sutyDefault=true";
			this.getSession().createQuery(hql).executeUpdate();
		}
		this.getHibernateTemplate().save(sut);
		return sut;
	}

	@Override
	public void deleteSysUserType(Long id) {
		String sysUserTypeHql = "select sui.SUIN_ID from SYS_USER_INFO sui where sui.SUIN_SUTY_ID=?";
		List sysUsers = this.getSession().createSQLQuery(sysUserTypeHql).setLong(0, id).list();
		if(sysUsers != null && sysUsers.size() > 0) {
			throw new ErrorMessageException("有会员关联此会员类型不能删除");
		}
		this.getHibernateTemplate().delete(this.get(SysUserType.class, id));
	}

	@Override
	public List getUserTypeList() {
		String hql = "select userType.sutyId,userType.sutyName from SysUserType userType order by userType.sutyId";
		return getHibernateTemplate().find(hql);
	}
}