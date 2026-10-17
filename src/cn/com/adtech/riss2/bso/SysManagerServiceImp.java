package cn.com.adtech.riss2.bso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.SysRole;
import cn.com.adtech.riss2.domain.SysUserRole;
import cn.com.adtech.riss2.domain.resource.ResMetaPerm;
import cn.com.adtech.riss2.exception.ErrorMessageException;

public class SysManagerServiceImp extends CommonServiceImp implements
		ISysManagerService {

	private static final String SysUserRole = null;

	@Override
	public Wrapper getAllManagersBySearchCondition(String condition, PageExtNative page) {
		String hqlCount = "select count(*) from SysManager sm where sm.smanType = "+SysManager.SMANTYPE_ADMIN+" ";
		String hql = "select sm.smanId,sm.smanName,sm.smanLoginName,"
			+ "sm.smanEmail,sm.smanTel,sm.smanRemark, sm.smanIsStop from SysManager sm where sm.smanType = "+SysManager.SMANTYPE_ADMIN+" ";
		if(condition != null && !condition.trim().equals("")) {
			hqlCount += " and ( sm.smanName like ? or sm.smanLoginName like ? ) ";
			hql += " and  (sm.smanName like ? or sm.smanLoginName like ? ) ";
		}

		if (page != null) {
			if (ServiceUtil.isNotNull(page.getSort())
					&& ServiceUtil.isNotNull(page.getDir())) {
				hqlCount += " order by sm." + page.getSort() + " "
						+ page.getDir();
				hql += " order by sm." + page.getSort() + " " + page.getDir();
			}
		}
		Session s = this.getSession();
		List listCount = new ArrayList();
		if(condition != null && !condition.trim().equals("")) {
			listCount = this.getHibernateTemplate().find(hqlCount, new Object[] {"%" + condition.trim() + "%", "%" + condition.trim() + "%" });
		} else {
			listCount = this.getHibernateTemplate().find(hqlCount);
			
		}
		Query q = s.createQuery(hql);
		if(condition != null && !condition.trim().equals("")) {
			q.setParameter(0, "%" + condition.trim() + "%");
			q.setParameter(1, "%" + condition.trim() + "%");
		}
		if (page != null && page.getLimit() != 0) {
			q.setFirstResult(page.getStart());
			q.setMaxResults(page.getLimit());
		}
		List list = q.list();
		List managerRoles = null;
		//根据各个用户的id查出对应的角色名字
		String sysUserRoles = "select sysRole.srolName from SysRole sysRole where " +
				"sysRole.srolId in " +
				"(select sur.role.srolId from SysUserRole sur, SysManager manager " +
				"where manager.smanId=? and sur.user.smanId=manager.smanId)";
		for(int i = 0; i < list.size(); i++) {
			Object[] objects = (Object[]) list.get(i);
			Object[] newObjects = new Object[objects.length + 1];
			System.arraycopy(objects, 0, newObjects, 0, objects.length);
			managerRoles = this.getHibernateTemplate().find(sysUserRoles, new Object[] {objects[0]});
			newObjects[newObjects.length - 1] = managerRoles.toString().substring(1, managerRoles.toString().length() - 1);
			list.set(i, newObjects);
		}
		releaseSession(s);
		return new Wrapper(list, listCount.get(0).toString());	
	}

	@SuppressWarnings("unchecked")
	@Override
	public Wrapper getAllRoles(PageExtNative page) {
		String hqlCount = "select count(*) from SysRole sr ";
		String hql = "select sr.srolId,sr.srolName from SysRole sr ";

		if (page != null) {
			if (ServiceUtil.isNotNull(page.getSort())
					&& ServiceUtil.isNotNull(page.getDir())) {
				hqlCount += " order by sr." + page.getSort() + " "
						+ page.getDir();
				hql += " order by sr." + page.getSort() + " " + page.getDir();
			}
		}
		Session s = this.getSession();
		List listCount = this.getHibernateTemplate().find(hqlCount);
		Query q = s.createQuery(hql);

		if (page != null && page.getLimit() != 0) {
			q.setFirstResult(page.getStart());
			q.setMaxResults(page.getLimit());
		}
		List<SysRole> list = q.list();
		releaseSession(s);
		return new Wrapper(list, listCount.get(0).toString());
	}

	@Override
	public SysManager saveManager(SysManager manager, SysRole[] roles) {
		SysManager man = this.getSysManagerByLoginName(manager.getSmanLoginName());
		if(man != null) {
			throw new ErrorMessageException("账号重复，请重新填写!");
		};
		this.save(manager);
		if(roles != null && roles.length > 0) {
	 		for(int i = 0 ; i < roles.length; i++) {
				SysUserRole sur = new SysUserRole();
				sur.setRole(roles[i]);
				sur.setUser(manager);
				this.getHibernateTemplate().save(sur);
			}
		}
		return manager;		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map getManagerInfoById(long id) {
		Map<String, List> retv = new HashMap<String, List>();
		List<SysUserRole> managerRoles = getRolesByManagerId(id);
		retv.put("managerRoles", managerRoles);
		String managerHql = "select manager from SysManager manager where manager.smanId=?";
		List<SysManager> manager = this.getHibernateTemplate().find(managerHql,
				new Object[] { id });
		retv.put("manager", manager);
		return retv;
	}

	@Override
	public void updateManagerInfo(SysManager manager, SysRole[] roles) {
		SysManager man = this.getSysManagerByLoginName(manager.getSmanLoginName());
		if(man != null && !man.getSmanId().equals(manager.getSmanId())) {
			throw new ErrorMessageException("账号重复，请重新填写!");
		}
		String managerRolesHql = "select sur from SysUserRole sur where sur.user.smanId=?";
		List managerRoles = this.getHibernateTemplate().find(managerRolesHql, new Object[] {manager.getSmanId()});
		Session session = this.getSession();
		List<Long> listRolesId = new ArrayList<Long>();//roles的id的集合
		List<Long> listManRolId = new ArrayList<Long>();//managerRole的角色id集合
		for(int j = 0; j < managerRoles.size(); j++) {
			SysUserRole sur = (SysUserRole) managerRoles.get(j);
			listManRolId.add(sur.getRole().getSrolId());
		}
		for(int j = 0; j < roles.length; j++) {
			listRolesId.add(roles[j].getSrolId());
		}
		if(roles != null && roles.length > 0) {
			if(managerRoles == null || managerRoles.size() == 0) {
				for(int i = 0; i < roles.length; i++) {
					SysUserRole newSur = new SysUserRole();
					newSur.setRole(roles[i]);
					newSur.setUser(manager);
					this.getHibernateTemplate().save(newSur);
				}
			} else {
				/*//循环当前用户包含角色
				for(int j = 0; j < managerRoles.size(); j++) {
					SysUserRole sur = (SysUserRole) managerRoles.get(j);
					//循环选中的角色集合
					for(int k = 0; k < roles.length; k++) {
						if(sur.getSuroId().equals(roles[k].getSrolId())) {
							continue;
						} else {
							this.getHibernateTemplate().delete(sur);
							SysUserRole newSur = new SysUserRole();
							newSur.setRole(roles[k]);
							newSur.setUser(manager);
							this.getHibernateTemplate().save(newSur);
						}
					}
				}*/
				for(int k = 0; k < roles.length; k++) {
					//判断用户角色id集合是否包含
					if (listManRolId.contains(roles[k].getSrolId())) {
						//包含则结束本次循环
						continue;
					}else {
						//不包含则新增管理员角色关联
						SysUserRole newSur = new SysUserRole();
						newSur.setRole(roles[k]);
						newSur.setUser(manager);
						this.getHibernateTemplate().save(newSur);
					}
				}
				for(int k = 0; k <  managerRoles.size(); k++) {
					SysUserRole sur =(SysUserRole) managerRoles.get(k);
					//判断用户角色权限id集合是否包含
					if (listRolesId.contains(sur.getRole().getSrolId())) {
						//包含结束本次循环
						continue;
					}else {
						//不包含则删除管理员角色关联
						this.getHibernateTemplate().delete(sur);
					}
				}
			}
		} else {
			session.createQuery("delete from SysUserRole sur where sur.user.smanId=:smanId").
				setLong("smanId", manager.getSmanId()).executeUpdate();
		}
		session.evict(man);
		releaseSession(session);
		this.getHibernateTemplate().update(manager);
	}

	@Override
	public SysManager getSysManagerByLoginName(String loginName) {
		String findManHql = "select m from SysManager m where m.smanLoginName=?";
		List list = this.getHibernateTemplate().find(findManHql,
				new Object[] { loginName });
		if (list != null && list.size() != 0) { 
			return (SysManager) list.get(0);
		}		
		return null;
	}

	@Override
	public List getAllRoles() {
		return this.getHibernateTemplate().find("from SysRole");
	}

	@Override
	public void deleteSysManager(SysManager manager) {
		this.getHibernateTemplate().bulkUpdate("delete from SysUserRole sur where sur.user.smanId=?", manager.getSmanId());
		this.getHibernateTemplate().delete(manager);
	}

	@Override
	public List getRolesByManagerId(long id) {
		String rolesHql = "select srole from SysUserRole srole where srole.user.smanId=?";
		List<SysUserRole> managerRoles = this.getHibernateTemplate().find(rolesHql,
				new Object[] { id });
		return managerRoles;
	}
	
	@Override
	public List getSysRoleByManagerId(long id) {
		String hql = "select sysrole from SysUserRole sur, SysRole sysrole where sur.user.smanId=? and sur.role.srolId=sysrole.srolId";
		return getHibernateTemplate().find(hql, id);
	}

	@Override
	public void updateSysManagerStatus(Long managerId) {
		SysManager manager = (SysManager) this.get(SysManager.class, managerId);
		if(manager.getSmanIsStop()) {
			manager.setSmanIsStop(false);
		} else {
			manager.setSmanIsStop(true);
		}
		this.getHibernateTemplate().merge(manager);
	}

	@Override
	public boolean isStop(Long managerId) {
		boolean b  = true;
		String hql = "from ResMetaPerm where smanId= ? and rmpeType=1";
		
		List<ResMetaPerm> list = getHibernateTemplate().find(hql,managerId);
		List<ResMetaPerm> list2 = null;
		for(ResMetaPerm rp : list){
			String hql2 = "from ResMetaPerm where rmstId =? and rmpeType=1";
			list2 = getHibernateTemplate().find(hql2,rp.getRmstId());
			if(list2.size()<2){
				b = false;
				break;
			}
		}
		
		return b;
	}
}
