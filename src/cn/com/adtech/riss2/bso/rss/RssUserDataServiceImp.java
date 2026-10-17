package cn.com.adtech.riss2.bso.rss;

import java.util.Date;
import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.CommonServiceCommonlyImp;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.rss.RssUserData;

public class RssUserDataServiceImp extends CommonServiceCommonlyImp implements IRssUserDataService {

	@Override
	public Long addRssUserData(RssUserData rssUserData) {
		this.save(rssUserData);
		return rssUserData.getRssDataId();
	}

	@Override
	public void deleteRssUserData(Long dataId) {
		this.getHibernateTemplate().bulkUpdate("delete from RssUserData rss where rss.rssDataId=?", dataId);
	}

	@Override
	public List<RssUserData> getAllRssUserDataByUserId(Long userId, Pagination pagination) {
		String hql = "from RssUserData rss where rss.rssDataUserId=? order by rss.rssDataOrder asc, rss.rssDataLastModifyDate desc";
		return pagination == null ? this.getHibernateTemplate().find(hql, userId) : 
			getPageHibernateTemplate().find(pagination, hql, userId);
	}

	@Override
	public void deleteRssUserDatas(Long[] dataIds) {
		if(dataIds != null && dataIds.length > 0) {
			for (int i = 0; i < dataIds.length; i++) {
				if(dataIds[i] != null) {
					deleteRssUserData(dataIds[i]);
				}
			}
		}
	}

	@Override
	public void updateRssUserData(RssUserData rssUserData) {
		RssUserData rss = (RssUserData) get(RssUserData.class, rssUserData.getRssDataId());
		rssUserData.setRssDataAddDate(rss.getRssDataAddDate());
		rssUserData.setRssDataLastModifyDate(new Date());
		this.getHibernateTemplate().merge(rssUserData);
	}

	@Override
	public Long addToMyRssData(Long metaStoreId, String path, SysUserinfo userinfo) {
		ResMetaStore metaStore = (ResMetaStore) get(ResMetaStore.class, metaStoreId);
		Long rssDataId = null;
		if(metaStore != null) {
			List<RssUserData> rsss = getHibernateTemplate().find("select rss from RssUserData rss where rss.rssDataSourceUrl='" + path + "'");
			if(rsss != null && rsss.size() > 0) return rssDataId;
			RssUserData rssUserData = new RssUserData(metaStore.getRmstName(), 
					path, userinfo.getSuinId(), new Date(), new Date());
			rssUserData.setRssDataOrder(RssUserData.RSS_DATA_METASTORE_ORDER);
			addRssUserData(rssUserData);
			rssDataId = rssUserData.getRssDataId();
		}
		return rssDataId;
	}

}
