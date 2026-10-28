package cn.com.adtech.riss2.bso.sc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.CommonServiceCommonlyImp;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.resource.ResLiterType;
import cn.com.adtech.riss2.domain.resource.ResSourceComt;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.sc.ScFavorites;
import cn.com.adtech.riss2.exception.ErrorMessageException;

public class FavoritesServiceImp extends CommonServiceCommonlyImp implements IFavoritesService {

	@Override
	public boolean getFavoriteState(Long userId, Long sourceId) {
		List counts = getHibernateTemplate().find(
				"select count(scf.scfId) from ScFavorites scf where scf.scfUserId=? and scf.scfSourceDataId=?",
				new Object[] { userId, sourceId });
		return !counts.isEmpty() && ((Number)counts.get(0)).longValue() > 0;
	}

	/** Set an explicit state rather than toggling, so retrying a request cannot undo it. */
	@Override
	public void updateFavoriteState(Long userId, Long sourceId, boolean favorited) {
		if (userId == null || userId < 1L || sourceId == null || sourceId < 1L) {
			throw new IllegalArgumentException("Invalid favorite owner or resource");
		}
		if (favorited) {
			if (!getFavoriteState(userId, sourceId)) save(new ScFavorites(userId, sourceId, new Date()));
		} else {
			deleteFavorite(userId, sourceId);
		}
	}

	@Override
	public void addFavorite(Long userId, Long sourceId) {
		if (getFavoriteByUserIdAndSourceId(userId, sourceId) != null) {
			throw new ErrorMessageException("您已收藏该资源，不需要再进行收藏!!!");
		}
		ScFavorites scf = new ScFavorites(userId, sourceId, new Date());
		this.save(scf);
	}

	@Override
	public void deleteFavorite(Long userId, Long sourceId) {
		this.getHibernateTemplate().bulkUpdate(
				"delete from ScFavorites scf where scf.scfUserId=? and scf.scfSourceDataId=?",
				new Object[] { userId, sourceId });
	}

	@Override
	public void deleteAllFavoritesByUserId(Long userId) {
		this.getHibernateTemplate().bulkUpdate("delete from ScFavorites scf where scf.scfUserId=?",
				userId);
	}

	@Override
	public List<ScFavorites> getFavoritesByUserId(Long userId, Pagination pagination) {
		return getPageHibernateTemplate().find(pagination,
				"from ScFavorites scf where scf.scfUserId=?", userId);
	}

	private ScFavorites getFavoriteByUserIdAndSourceId(Long userId, Long sourceId) {
		List<ScFavorites> list = getHibernateTemplate().find(
				"from ScFavorites scf where scf.scfUserId=? and scf.scfSourceDataId=?",
				new Object[] { userId, sourceId });
		return (list != null && list.size() > 0) ? list.get(0) : null;
	}

	@Override
	public List getMySourceComtList(Long suinId, Pagination pagination,Short rltyType,String search) {
		StringBuilder hql = new StringBuilder();
		List<Object> param = new ArrayList<Object>();
		// 当查询所有评论时，直接从评论表中查询相关记录，当根据资源类型进行查询时，则需要关连到多长表；
		hql.append("from ResSourceComt rsc where rsc.rscoSuinId = ? ");
		param.add(suinId);
		if(ServiceUtil.isNotNull(search)){
			hql.append("and rsc.rscoContent like ? ");
			param.add("%" + search + "%");
		}
		if(rltyType > 0){
			hql.append("and rsc.rscoRsdaId in ( ");
			hql.append("select rsd.rsdaId from ResSourceData rsd where rsd.rsdaRltyId in ( select rlt.rltyId from ResLiterType rlt where rlt.rltyType = ?  )");
			hql.append(") ");
			param.add(rltyType);
		}
		hql.append("order by rsc.rscoDate DESC ");
		return this.getPageHibernateTemplate().find(pagination, hql.toString(), param.toArray());
	}

	
	@Override
	public List<Object[]> getFavoritesList(Long suinId, Short rltyType, String search, Pagination pagination) {
		StringBuilder hql = new StringBuilder();
		List<Object> param = new ArrayList<Object>();
		hql.append("select scf.scfId,");
		hql.append("scf.scfSourceData.rsdaId,");
		hql.append("scf.scfSourceData.rsdaTitle,");
		hql.append("scf.scfDate,");
		hql.append("scf.scfSourceData.resLiterType.rltyName");
		
		hql.append(" from ScFavorites scf left join scf.scfSourceData rsda where scf.scfUserId = ? ");
		param.add(suinId);
		if(ServiceUtil.isNotNull(search)){
			hql.append(" and scf.scfSourceData.rsdaTitle like ? ");
			param.add("%" + search + "%");
		}
		if(rltyType > 0){
			hql.append(" and scf.scfSourceDataId in ( ");
			hql.append("select rsd.rsdaId from ResSourceData rsd where rsd.rsdaRltyId in ( select rlt.rltyId from ResLiterType rlt where rlt.rltyType = ?  )");
			hql.append(") ");
			param.add(rltyType);
		}
		hql.append(" order by scf.scfDate DESC ");
		String hqlStr = hql.toString();
		hql = null;
		List<Object[]> datalist = this.getPageHibernateTemplate().find(pagination, hqlStr, param.toArray());
		return datalist;
	}
}
