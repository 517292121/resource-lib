package cn.com.adtech.riss2.bso.infosubscription;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.CommonServiceCommonlyImp;
import cn.com.adtech.riss2.domain.infosubscription.InfoSubscription;
import cn.com.adtech.riss2.domain.infosubscription.IsIgnoreResSource;

public class InfoSubscriptionServiceImp extends CommonServiceCommonlyImp implements
		IInfoSubscriptionService {

	@Override
	public Long addInfoSubscription(InfoSubscription infoSubscription) {
		save(infoSubscription);
		return infoSubscription.getIssId();
	}

	@Override
	public void deleteInfoSubscriptionById(Long infoSubscriptionId) {
		getHibernateTemplate().bulkUpdate("delete from InfoSubscription iss where iss.issId=?", infoSubscriptionId);
	}

	@Override
	public List<InfoSubscription> getAllInfoSubscriptionByUserId(Long userId, Pagination pagination) {
		return pagination != null ? 
				getPageHibernateTemplate().find(pagination, "from InfoSubscription iss where iss.issUserId=? order by iss.issDate desc", userId) : 
				getHibernateTemplate().find("from InfoSubscription iss where iss.issUserId=? order by iss.issDate desc", userId);
	}

	@Override
	public void deleteInfoSubscriptions(Long[] ids) {
		if(ids != null && ids.length > 0) {
			for(int i = 0; i < ids.length; i++) {
				deleteInfoSubscriptionById(ids[i]);
			}
		}
	}

	@Override
	public void addInfoSubscriptions(InfoSubscription[] infoSubscriptions) {
		if(infoSubscriptions != null && infoSubscriptions.length > 0) {
			for (int i = 0; i < infoSubscriptions.length; i++) {
				addInfoSubscription(infoSubscriptions[i]);
			}
		}
	}

	@Override
	public Long addIgoreResource(IsIgnoreResSource ignoreResource) {
		this.save(ignoreResource);
		return ignoreResource.getIsInRsId();
	}

	@Override
	public void addIgoreResources(IsIgnoreResSource[] ignoreResources) {
		if(ignoreResources != null && ignoreResources.length > 0) {
			for(int i = 0; i < ignoreResources.length; i++) {
				this.save(ignoreResources[i]);
			}
		}
	}

	@Override
	public List<IsIgnoreResSource> getAllIgnoreResourcesByUserId(Long userId, Pagination pagination) {
		return pagination != null ?
				getPageHibernateTemplate().find(pagination, "from IsIgnoreResSource iir where iir.isInRsUserId=? order by iir.isInRsDate desc", userId) : 
					getHibernateTemplate().find("from IsIgnoreResSource iir where iir.isInRsUserId=? order by iir.isInRsDate desc", userId);
	}

	@Override
	public void deleteIgnoreResources(Long[] ignoredResourcesIds) {
		if(ignoredResourcesIds != null && ignoredResourcesIds.length > 0) {
			for (int i = 0; i < ignoredResourcesIds.length; i++) {
				this.getHibernateTemplate().bulkUpdate("delete IsIgnoreResSource iir where iir.isInRsId=?", ignoredResourcesIds[i]);
			}
		}
	}

}
