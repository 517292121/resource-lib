package cn.com.adtech.riss2.bso.rss;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.rss.RssUserData;

/**
 * 会员RSS资源操作
 * @author MaChao
 *
 */
public interface IRssUserDataService extends ICommonService {
	
	Long addRssUserData(RssUserData rssUserData);

	void deleteRssUserData(Long dataId);
	
	void deleteRssUserDatas(Long[] dataIds);
	
	List<RssUserData> getAllRssUserDataByUserId(Long userId, Pagination pagination);
	
	void updateRssUserData(RssUserData rssUserData);
	
	/**
	 * 添加特色库到我的RSS订阅
	 * @param metaStoreId
	 * @param path
	 * @param userinfo
	 * @return
	 */
	Long addToMyRssData(Long metaStoreId, String path, SysUserinfo userinfo);
}
