package cn.com.adtech.riss2.bso.resource;

import java.util.LinkedList;
import java.util.List;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.domain.resource.ResElementPool;

public class MetadataTempletManagementImp extends CommonServiceImp implements IMetadataTempletManagementService{


	/**
	 * user_id: 拥有特色库管理权限的用户ID
	 */
	public String getSourceTypeList(long user_id, long store_id, long stype_id, int begin, int pageNum) {
		String sql = "select a.rmst_id,a.rmst_name,a.RMST_BUILDDATE,b.rlty_id,b.rlty_name from RES_META_STORE a,RES_LITER_TYPE b,RES_STORE_LITER t where b.rlty_id=t.rlty_id and a.rmst_id = t.rmst_id ";
		if(store_id > 0){
			sql += " and a.rmst_id = " + store_id; 
		}
		if(stype_id > 0){
			sql += " and b.rlty_id = " + stype_id;
		}
		sql += " and a.rmst_id in (select RMST_ID from RES_META_PERM where RMPE_ADMINID="+user_id+" and RMPE_TYPE=1) order by a.rmst_id desc";
		
		String json = getListToJson(sql, null, begin, pageNum);
		
		return json;
	}

	public String getSourceTypeByStore(long store_id){
		String json = "select b.rlty_id,b.rlty_name from RES_META_STORE a,RES_LITER_TYPE b,RES_STORE_LITER t where b.rlty_id=t.rlty_id and a.rmst_id = t.rmst_id and a.rmst_id= " + store_id;
		json = getJsonByOption(json);
		return json;
	}

	public boolean updateTempletElements(long store_id, long sourceType,
			String[] EleIdArr) {
		if(null == EleIdArr || EleIdArr.length < 1){
			return false;
		}
		String deleSql = "delete from RES_TEMPLET_META where RTME_RSIN_ID="+store_id + " and RTME_RLTY_ID=" + sourceType;
		
		String[] insertSqlArr = new String[EleIdArr.length + 1];
		insertSqlArr[0] = deleSql;
		for(int i=0; i<EleIdArr.length; i++){
			insertSqlArr[i+1] = "insert into RES_TEMPLET_META(RTME_ID,RTME_RSIN_ID,RTME_RLTY_ID,RTME_REEL_ID) values("+(System.currentTimeMillis()+i)+","+store_id+","+sourceType+","+EleIdArr[i]+")";
		}
		
		boolean b = executeUpdateMultiSql(insertSqlArr);
		return b;
	}

	@Override
	public String[] getTempletAllElementName(Long storeId, Long sourceTypeId) {
		String hql = "from ResElementPool ele where ele.repoId in (select rtm.rtmeRepoId from ResTempletMeta rtm where rtm.rtmeRsinId=? and rtm.rtmeRltyId=?)";
		
		List<Object> list = getHibernateTemplate().find(hql, new Object[]{storeId, sourceTypeId});
		if(list == null || list.size() < 1){
			return null;
		}
		ResElementPool parentElement = null;
		ResElementPool childElement = null;
		List<String> elementNameList = new LinkedList<String>();
		for(int i=0,s=list.size(); i<s; i++){
			parentElement = (ResElementPool) list.get(i);
			if(parentElement.getRepoType() == 1 || parentElement.getRepoType() == 2){ //核心元素或父元素
				elementNameList.add(parentElement.getRepoTitle() + "("+parentElement.getRepoName()+")");
				for(int j=0; j<s; j++){
					childElement = (ResElementPool) list.get(j);
					if(parentElement.getRepoId().equals(childElement.getRepoParent())){
						elementNameList.add(childElement.getRepoTitle() + "("+childElement.getRepoName()+")");
					}
				}
			}
		}
		
		String[] names = new String[elementNameList.size()];
		for(int i=0,s=elementNameList.size(); i<s; i++){
			names[i] = elementNameList.get(i);
		}
		return names;
	}
}
