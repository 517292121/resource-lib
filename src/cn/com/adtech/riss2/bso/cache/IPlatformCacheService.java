package cn.com.adtech.riss2.bso.cache;

import cn.com.adtech.riss2.bso.ICommonService;

/**
 * 
 * 平台缓存数据统一管理
 *
 * @author MaChao
 *
 * @date：Jan 5, 2018
 */
public interface IPlatformCacheService extends ICommonService {

	/**
	 * 更新特色库缓存
	 */
	public void updateMetastoreList();
	
	/**
	 * 更新文本库缓存
	 */
	public void updateTextstoreList();
	
	/**
	 * 更新按资源类型存储的资源集合
	 */
	public void updateResforType();
	
	/**
	 * 更新资源统计
	 */
	public void updateResCount();
	
	/**
	 * 更新信息分类缓存
	 */
	public void updateInfoClassList();
	
	
	/**
	 * 更新所有
	 */
	public void updateAll();
}
