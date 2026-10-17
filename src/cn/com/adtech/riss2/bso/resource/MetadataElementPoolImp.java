package cn.com.adtech.riss2.bso.resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResImportTemplet;
import cn.com.adtech.riss2.domain.resource.ResSourceData;

public class MetadataElementPoolImp extends CommonServiceImp implements IMetadataElementPoolService {


	public List<ResElementPool> getElementList(long rsty) {
		String hql = "from ResElementPool rce where rce.resLiterType.rltyId=? and rce.repoType<3 order by rce.repoId";
		return this.getHibernateTemplate().find(hql,rsty);
	}

	public Serializable saveElement(ResElementPool element) {
		Serializable s = getHibernateTemplate().save(element);
		return s;
	}

	
	public List<ResElementPool> getElementByTemplet(long store,
			long sourceType) {
		String sql = "select ele.REPO_ID, ele.repo_name,ele.repo_type,ele.REPO_DEFAULTDATA, ele.repo_title, ele.repo_restrict,ele.repo_code, ele.repo_parent,ele.REPO_DATATYPE,ele.REPO_DATAOPTIONS from RES_ELEMENT_POOL ele,(select rtme_reel_id from RES_TEMPLET_META where rtme_rsin_id="+store+" and rtme_rlty_id="+sourceType+") tmp where ele.repo_id = tmp.rtme_reel_id order by ele.REPO_ID";
		List<Map<String, String>> datas = getMapList(sql, null);
		if(datas == null){
			return null;
		}
		Map<String, String> map = null;
		List<ResElementPool> elelist = new LinkedList<ResElementPool>();
		ResElementPool ele = null;
		for(int i=0,s=datas.size(); i<s; i++){
			map = datas.get(i);
			ele = new ResElementPool();
			ele.setRepoId(Long.parseLong(map.get("REPO_ID")));
			ele.setRepoType(Long.parseLong(map.get("REPO_TYPE")));
			ele.setRepoName(map.get("REPO_NAME"));
			ele.setRepoTitle(map.get("REPO_TITLE"));
			ele.setRepoRestrict(map.get("REPO_RESTRICT"));
			ele.setRepoCode(map.get("REPO_CODE"));
			ele.setRepoParent(Long.parseLong(map.get("REPO_PARENT")));
			ele.setRepoDatatype(map.get("REPO_DATATYPE") == null ? 0 : Integer.parseInt(map.get("REPO_DATATYPE")));
			ele.setRepoDataOptions(map.get("REPO_DATAOPTIONS"));
			ele.setRepoDefaultdata(map.get("REPO_DEFAULTDATA"));
			elelist.add(ele);
		}
		map = null;
        datas = null;
		return elelist;
	}
	
	public List<ResElementPool> getTempletElement(long store, long sourceType){
		StringBuffer buffer = new StringBuffer();
		buffer
				.append("select ele.REPO_ID, ele.repo_name,ele.repo_type, ele.repo_title, ele.repo_restrict,ele.repo_code, ele.repo_parent,rite.RITE_COLUMN ");
		buffer.append(" from");
		buffer.append(" (select rtme_reel_id from RES_TEMPLET_META where rtme_rsin_id=" + store + " and rtme_rlty_id=" + sourceType
				+ ") tmp");
		buffer.append(" left join RES_ELEMENT_POOL ele on tmp.rtme_reel_id = ele.repo_id");
		buffer.append(" left join RES_IMPORT_TEMPLET rite on ele.REPO_ID = rite.RITE_REPO_ID and rite.RITE_RMST_ID="+store+" and rite.RITE_RLTY_ID=" + sourceType);
		buffer.append(" order by ele.REPO_ID ");
		String sql = buffer.toString();
		List<Map<String, String>> datas = getMapList(sql, null);
		if(datas == null) {
			return null;
		}
		Map<String, String> map = null;
		List<ResElementPool> elelist = new LinkedList<ResElementPool>();
		ResElementPool ele = null;
		for(int i = 0, s = datas.size(); i < s; i++) {
			map = datas.get(i);
			ele = new ResElementPool();
			ele.setRepoId(Long.parseLong(map.get("REPO_ID")));
			ele.setRepoType(Long.parseLong(map.get("REPO_TYPE")));
			ele.setRepoName(map.get("REPO_NAME"));
			ele.setRepoTitle(map.get("REPO_TITLE"));
			ele.setRepoRestrict(map.get("REPO_RESTRICT"));
			ele.setRepoCode(map.get("REPO_CODE"));
			ele.setRepoParent(Long.parseLong(map.get("REPO_PARENT")));
			ele.setExcelColumn(map.get("RITE_COLUMN"));
			elelist.add(ele);
		}
		map = null;
		datas = null;
		return elelist;
	}

	public List<ResElementPool> getElementByUnTemplet(long store,
			long sourceType, int eleType) {
		String hql = "from ResElementPool rce where rce.repoPltyId="+sourceType;
		if(eleType == 1){
			hql += " and rce.repoType=1";
		}
		if(eleType > 1){
			hql += " and rce.repoType>1";
		}
		hql += " and rce.repoId not in(select rtmeRepoId from ResTempletMeta where rtmeRsinId="+store+" and rtmeRltyId="+sourceType + ") order by rce.repoType";
		return this.getHibernateTemplate().find(hql);
	}

	public Serializable AddChildElement(ResElementPool element) {
		Serializable s = getHibernateTemplate().save(element);
		return s;
	}

	public ResElementPool getElementById(long repoId) {
		ResElementPool element = (ResElementPool) getHibernateTemplate().get(ResElementPool.class, repoId);
		return element;
	}

	public boolean updateElement(ResElementPool element) {
		try {
			getHibernateTemplate().update(element);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<Long> getElementIdByTemplet(long store, long sourceType) {
		String hql = "select rtmeRepoId from ResTempletMeta where rtmeRsinId="+store+" and rtmeRltyId="+sourceType+"";
		return this.getHibernateTemplate().find(hql);
	}

	public long saveResource(ResSourceData resource) {
		Serializable s = getHibernateTemplate().save(resource);
		if(null != s){
			return Long.parseLong(s.toString());
		}else{
			return 0;
		}
	}

	public boolean updateResource(ResSourceData resource) {
		try {
			getHibernateTemplate().update(resource);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取模板中除元素外，其它分类和文件名的对应对象
	 * @param store
	 * @param sourceType
	 * @return
	 */
	public Map<String, String> getTempletElementByGroup(long store,
			long sourceType) {
		//riteRepoId<10 10以内的为各种分类和文件所映射的ID
		List<ResImportTemplet> list = getTempletByGroup(store, sourceType);
		if(list == null || list.size() < 1){
			return null;
		}
		ResImportTemplet templet = null;
		Map<String, String> map = new HashMap<String, String>();
		for(int i=0,s=list.size(); i<s; i++){
			templet = list.get(i);
			map.put("id_" + templet.getRiteRepoId(), templet.getRiteColumn());
		}
		list = null;
		return map;
	}

	/**
	 * 获取模板中除元素外，其它分类和文件名的对应对象
	 * @param store
	 * @param sourceType
	 * @return
	 */
	public List<ResImportTemplet> getTempletByGroup(long store, long sourceType) {
		String hql = "from ResImportTemplet where riteRepoId<10 and resRmstId=" + store + " and resRltyId=" + sourceType ;
		List<ResImportTemplet> list = getHibernateTemplate().find(hql);
		return list;
	}

}
