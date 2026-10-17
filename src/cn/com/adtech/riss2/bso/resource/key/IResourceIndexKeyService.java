package cn.com.adtech.riss2.bso.resource.key;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonServiceCommonlyService;
import cn.com.adtech.riss2.domain.resource.ResIndexKey;

/**
 * 检索词管理
 * @author MaChao
 * @date 2018-2-25
 */

public interface IResourceIndexKeyService extends ICommonServiceCommonlyService{
	
	/**
	 *  根据分页，关键词获取检索词信息列表
	 * @param pagination
	 * @param rikeName
	 * @return
	 * @author MaChao
	 * @date 2018-2-27
	 */
	List<ResIndexKey> getIndexKeyList(Pagination pagination, String rikeName,String sort, String dir);
	
	/**
	 * 判断检索词是否存在
	 * @param rikeId
	 * @param rikeName
	 * @return
	 * @author MaChao
	 * @date 2018-2-27
	 */
	boolean valIndexKey(Long rikeId, String rikeName);
}
