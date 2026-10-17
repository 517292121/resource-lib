package cn.com.adtech.riss2.bso.resource.key;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.CommonServiceCommonlyImp;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResIndexKey;

/**
 * 根据分页获取关键词信息
 * 
 * @author MaChao
 * @date 2018-2-25
 */
@SuppressWarnings("unchecked")
public class ResourceIndexKeyServiceImp extends CommonServiceCommonlyImp implements IResourceIndexKeyService{
	
	/**
	 * 根据分页、关键词获取检索词信息列表
	 * 
	 * @author MaChao
	 * @date 2018-2-25
	 */
	@Override
	public List<ResIndexKey> getIndexKeyList(Pagination pagination, String rikeName,String sort, String dir) {
		String hql = "select " +
				"rik.rikeId , " +
				"rik.rikeName , " +
				"rik.rikeSearchCount" +
				" from ResIndexKey rik where rik.rikeId <> 0";
		//检索条件
		if(CommonUtil.isNotNull(rikeName)){
			hql += " and rik.rikeName like '%"+rikeName+"%'";
		}
		hql += " order by";
		// 添加排序
		if(CommonUtil.isNotNull(sort) && CommonUtil.isNotNull(dir)) {
			if("rikeName".equals(sort)) {
				//检索词排序
				hql += " rik.rikeName";
			}else {
				//检索量排序
				hql += " rik.rikeSearchCount";
			}
			hql += " "+dir;
		}
		return this.getPageHibernateTemplate().find(pagination, hql);
	}

	/**
	 * 判断检索词是否存在
	 * 
	 * @author MaChao
	 * @date 2018-2-26
	 */
	@Override
	public boolean valIndexKey(Long rikeId, String rikeName) {
		boolean flg = false;
		List<ResIndexKey> rikList = null;
		String hql = "from ResIndexKey rik where rik.rikeName='"+ rikeName+"'";
		if(rikeId != 0){
			//id不等于0 ---->编辑
			hql += " and rik.rikeId != "+rikeId;
		}
		rikList = this.getHibernateTemplate().find(hql);
		if(rikList.size() > 0){
			flg = true;
		}
		return flg;
	}
	
	
}
