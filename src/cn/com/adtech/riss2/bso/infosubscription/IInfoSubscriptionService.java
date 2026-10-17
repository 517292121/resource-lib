package cn.com.adtech.riss2.bso.infosubscription;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.infosubscription.InfoSubscription;
import cn.com.adtech.riss2.domain.infosubscription.IsIgnoreResSource;

/**
 * 消息订阅推送
 * @author MaChao
 *
 */
public interface IInfoSubscriptionService extends ICommonService {

	Long addInfoSubscription(InfoSubscription infoSubscription);
	
	void addInfoSubscriptions(InfoSubscription[] infoSubscriptions);

	List<InfoSubscription> getAllInfoSubscriptionByUserId(Long userId, Pagination pagination);
	
	void deleteInfoSubscriptionById(Long infoSubscriptionId);
	
	void deleteInfoSubscriptions(Long[] ids);
	
	Long addIgoreResource(IsIgnoreResSource ignoreResource);
	
	void addIgoreResources(IsIgnoreResSource[] ignoreResources);
	
	List<IsIgnoreResSource> getAllIgnoreResourcesByUserId(Long userId, Pagination pagination);
	
	void deleteIgnoreResources(Long[] ignoredResourcesIds);
}
