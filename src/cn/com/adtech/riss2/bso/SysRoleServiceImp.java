package cn.com.adtech.riss2.bso;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.domain.SysFunction;
import cn.com.adtech.riss2.domain.SysRole;
import cn.com.adtech.riss2.domain.SysRoleFunction;
import cn.com.adtech.riss2.exception.ErrorMessageException;

@SuppressWarnings("unchecked")
public class SysRoleServiceImp extends CommonServiceImp implements ISysRoleService {
	@Override
	public Wrapper getAllRolesBySearchCondition(String condition, PageExtNative page) {
		String hqlCount = "select count(*) from SysRole role ";
		String hql = "select role.srolId, role.srolName, role.srolCode, role.srolDesc, " + "role.srolIsSysRole from SysRole role";
		String suffixHql = " where role.srolName like ?";
		// + "%' or role.suinName like '%" + condition + "%' )";
		if(condition != null && !condition.trim().equals("")) {
			hqlCount += suffixHql;
			hql += suffixHql;
		}
		if(page != null) {
			if(ServiceUtil.isNotNull(page.getSort()) && ServiceUtil.isNotNull(page.getDir())) {
				hqlCount += " order by role." + page.getSort() + " " + page.getDir();
				hql += " order by role." + page.getSort() + " " + page.getDir();
			}
		}
		Session s = this.getSession();
		List listCount = new ArrayList();
		if(condition != null && !condition.trim().equals("")) {
			listCount = this.getHibernateTemplate().find(hqlCount, "%" + condition.trim() + "%");
		} else {
			listCount = this.getHibernateTemplate().find(hqlCount);
			
		}
		Query q = s.createQuery(hql);
		if(condition != null && !condition.trim().equals("")) {
			q.setParameter(0, "%" + condition.trim() + "%");
		}
		if(page != null && page.getLimit() != 0) {
			q.setFirstResult(page.getStart());
			q.setMaxResults(page.getLimit());
		}
		List<SysRole> list = q.list();
		releaseSession(s);
		return new Wrapper(list, listCount.get(0).toString());
	}

	@Override
	public SysRole saveRole(SysRole role) {
		if(getRoleByName(role.getSrolName()) != null) {
			throw new ErrorMessageException("角色名重复，请重新填写");
		}
		;
		// String hql = "select max(role.srolCode) from SysRole role";
		// String code = (String) this.getHibernateTemplate().find(hql).get(0);
		// role.setSrolCode(code + 1);
		this.save(role);
		return role;
	}

	@Override
	public void deleteRole(SysRole role) {
		if(role.getSrolIsSysRole() == true) {
			throw new ErrorMessageException("系统角色不能删除!!!");
		}
		this.getHibernateTemplate().delete(role);
	}

	@Override
	public void updateRole(SysRole role) {
		SysRole oldRole = this.getRoleById(role.getSrolId());
		if(getRoleByName(role.getSrolName()) != null && !oldRole.getSrolName().equals(role.getSrolName())) {
			throw new ErrorMessageException("角色名重复，请重新填写");
		}
		this.getSession().evict(oldRole);
		this.getHibernateTemplate().update(role);
	}

	@Override
	public SysRole getRoleById(Long id) {
		return (SysRole)this.getHibernateTemplate().get(SysRole.class, id);
	}

	private SysRole getRoleByName(String name) {
		String hql = "from SysRole role where role.srolName=?";
		SysRole role = null;
		List roles = this.getHibernateTemplate().find(hql, new Object[] {name});
		if(roles != null && roles.size() != 0) {
			role = (SysRole)roles.get(0);
		}
		return role;
	}

	@Override
	public Wrapper getAllFunctionsByCataLogId(Long id, PageExtNative page) {
		String hqlCount = "select count(*) from cn.com.adtech.riss2.domain.SysFunction sf ";
		String hql = "select sf.sfunId,sf.sfunName,sf.sfunDesc,sf.sfunUrl,"
				+ "sf.sfunIndex,sf.sfunCode,sf.sfunIsHidden from SysFunction sf where sf.parentCatalog.scatId=" + id
				+ " or sf.parentCatalog.parentCatalog.scatId=" + id;
		Session s = this.getSession();
		List listCount = this.getHibernateTemplate().find(hqlCount);
		Query q = s.createQuery(hql);
		if(page != null && page.getLimit() != 0) {
			q.setFirstResult(page.getStart());
			q.setMaxResults(page.getLimit());
		}
		List list = this.getHibernateTemplate().find(hql);
		if(list == null || list.size() == 0) {
			list = this.getHibernateTemplate().find(hql.substring(0, hql.indexOf("where")) + "	where sf.sfunId=" + id);
		}
		releaseSession(s);
		return new Wrapper(list, listCount.get(0).toString());
	}

	@Override
	public Wrapper getAllFunctionsByRoleId(Long id, PageExtNative page) {
		String hqlCount = "select count(*) from cn.com.adtech.riss2.domain.SysRoleFunction srf " + "where srf.sysRole.srolId=" + id;
		String hql = "select distinct sf.sfunId,sf.sfunName,sf.sfunDesc,sf.sfunUrl,"
				+ "sf.sfunIndex,sf.sfunCode,sf.sfunIsHidden from SysFunction sf, SysRoleFunction srf" + " where srf.sysRole.srolId=" + id
				+ " and srf.sysFunction.sfunId=sf.sfunId";
		if(page != null) {
			if(ServiceUtil.isNotNull(page.getSort()) && ServiceUtil.isNotNull(page.getDir())) {
				hqlCount += " order by srf." + page.getSort() + " " + page.getDir();
				hql += " order by srf." + page.getSort() + " " + page.getDir();
			}
		}
		Session s = this.getSession();
		List listCount = this.getHibernateTemplate().find(hqlCount);
		Query q = s.createQuery(hql);
		if(page != null && page.getLimit() != 0) {
			q.setFirstResult(page.getStart());
			q.setMaxResults(page.getLimit());
		}
		List list = this.getHibernateTemplate().find(hql);
		releaseSession(s);
		return new Wrapper(list, listCount.get(0).toString());
	}

	@Override
	public void saveFunctionsToRole(Long[] funcsId, Long roleId) {
		for(int i = 0; i < funcsId.length; i++) {
			List sysRoles = this.getHibernateTemplate().find(
					"select srf from SysRoleFunction srf where " + "srf.sysFunction.sfunId=" + funcsId[i] + " " + "and srf.sysRole.srolId="
							+ roleId);
			if(sysRoles != null && sysRoles.size() != 0) {
				continue;
			}
			SysRoleFunction srf = new SysRoleFunction();
			srf.setSysFunction((SysFunction)this.getHibernateTemplate().get(SysFunction.class, funcsId[i]));
			srf.setSysRole((SysRole)this.getHibernateTemplate().get(SysRole.class, roleId));
			this.getHibernateTemplate().save(srf);
		}
	}

	@Override
	public void deleteFunctionsFromRole(Long[] funcsId, Long roleId) {
		for(int i = 0; i < funcsId.length; i++) {
			List srf = this.getHibernateTemplate().find(
					"select srf from SysRoleFunction srf where " + "srf.sysFunction.sfunId=" + funcsId[i] + " and srf.sysRole.srolId="
							+ roleId);
			if(srf != null && srf.size() > 0) {
				this.getHibernateTemplate().delete(srf.get(0));
			}
		}
	}
}