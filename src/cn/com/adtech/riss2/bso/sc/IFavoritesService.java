package cn.com.adtech.riss2.bso.sc;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.sc.ScFavorites;

/**
 * 收藏
 * @author MaChao
 *
 */
public interface IFavoritesService extends ICommonService {
	
	void addFavorite(Long userId, Long sourceId);
	
	void deleteFavorite(Long userId, Long sourceId);
	
	void deleteAllFavoritesByUserId(Long userId);
	
	List<ScFavorites> getFavoritesByUserId(Long userId, Pagination pagination);
	
	/**
	 * 获取收藏记录
	 * @param suinId
	 * @param rltyType
	 * @param search
	 * @param pagination
	 * @return
	 * @author MaChao
	 * @date 2010-12-8
	 */
	List<Object[]> getFavoritesList(Long suinId,Short rltyType,String search, Pagination pagination);
	/**
	 * 获取我的评论列表
	 * @param suinId
	 * @param pagination
	 * @return
	 */
	List getMySourceComtList(Long suinId,Pagination pagination,Short rltyType,String search);
}
