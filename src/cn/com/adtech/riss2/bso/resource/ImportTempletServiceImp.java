package cn.com.adtech.riss2.bso.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResImportTemplet;
import cn.com.adtech.riss2.jdbcms.ToolUnit;

public class ImportTempletServiceImp extends CommonServiceImp implements IImportTempletService{

	public boolean saveImportTempletList(Long rmstId, Long rltyId,List<ResImportTemplet> templetElements) {
		if(templetElements == null || templetElements.size() < 1){
			return false;
		}
		String hql = "delete from ResImportTemplet where resRmstId="+rmstId+" and resRltyId=" + rltyId;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Transaction tran = null;
		try {
			tran = session.beginTransaction();
			Query q = session.createQuery(hql);
			q.executeUpdate();
			for(int i=0,s=templetElements.size(); i<s; i++){
				session.save(templetElements.get(i));
			}
			tran.commit();
			return true;
		} catch (Exception e) {
			logger.error("在ImportTempletServiceImp的saveImportTempletList中发生异常：" + e.getMessage());
			e.printStackTrace();
			tran.rollback();
			return false;
		} finally {
			session.close();
		}
	}

	public Map<Long, String> getOtherElementData(Long rmstId, Long rltyId) {
		String hql = "select rite.riteRepoId, rite.riteColumn from ResImportTemplet rite where rite.resRmstId="+rmstId+" and rite.resRltyId="+rltyId+" and rite.riteRepoId<100";
		List<Object[]> list = getHibernateTemplate().find(hql);
		if(list == null || list.size() < 1){
			return null;
		}
		Map<Long, String> map = new HashMap<Long, String>();
		Object[] obj;
		for(int i=0,s=list.size(); i<s; i++){
			obj = list.get(i);
			map.put(Long.parseLong(obj[0].toString()), obj[1].toString());
		}
		return map;
	}

	public Map<Long, String> getTempletElement(Long rmstId, Long rltyId) {
		String hql = "select rite.riteRepoId, rite.riteColumn from ResImportTemplet rite where rite.resRmstId="+rmstId+" and rite.resRltyId="+rltyId;
		List<Object[]> list = getHibernateTemplate().find(hql);
		if(list == null || list.size() < 1){
			return null;
		}
		Map<Long, String> map = new HashMap<Long, String>();
		Object[] obj;
		for(int i=0,s=list.size(); i<s; i++){
			obj = list.get(i);
			map.put(Long.parseLong(ToolUnit.objectToStr(obj[0], "0")), ToolUnit.objectToStr(obj[1], ""));
		}
		return map;
	}

	public List<ResImportTemplet> getTempletElementList(Long rmstId, Long rltyId) {
		String hql = "select rite.riteRepoId, rite.riteColumn,repo.repoName, repo.repoRestrict, repo.repoTitle from ResImportTemplet rite,ResElementPool repo where rite.riteRepoId=repo.repoId and rite.resRmstId="+rmstId+" and rite.resRltyId="+rltyId;
		List<Object[]> list = getHibernateTemplate().find(hql);
		if(list == null || list.size() < 1){
			return null;
		}
		List<ResImportTemplet> elementlist = new ArrayList<ResImportTemplet>();
		ResImportTemplet importTemplet = null;
		Object[] obj;
		for(int i=0,s=list.size(); i<s; i++){
			importTemplet = new ResImportTemplet();
			obj = list.get(i);
			importTemplet.setRiteRepoId(Long.parseLong(ToolUnit.objectToStr(obj[0], "0")));
			importTemplet.setRiteColumn(ToolUnit.objectToStr(obj[1], ""));
			importTemplet.setElementName(ToolUnit.objectToStr(obj[2], ""));
			importTemplet.setRepoRestrict(ToolUnit.objectToStr(obj[3], ""));
			importTemplet.setElementTitle(ToolUnit.objectToStr(obj[4], ""));
			elementlist.add(importTemplet);
		}
		obj = null;
		list = null;
		return elementlist;
	}

	/**
	 * 获取模板中一共设置了多少列
	 * @param rmstId
	 * @param rltyId
	 * @return
	 */
	public int getTempletNumber(long rmstId, long rltyId) {
		String hql = "select count(rit.riteId) from ResImportTemplet rit where rit.riteColumn is not null and rit.resRmstId=" + rmstId + " and rit.resRltyId=" + rltyId;
		List list = getHibernateTemplate().find(hql);
		if(list != null && list.size() > 0){
			Object o = list.get(0);
			list = null;
			return Integer.parseInt(o.toString());
		}else{
			return 0;
		}
	}

}
