package cn.com.adtech.riss2.bso.platform;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;

/**
 * 
 * 资源评论管理
 *
 * @author MaChao
 *
 * @date：Jan 17, 2018
 */
public interface IResourceCommentsService extends ICommonService{

	/**
	 * 获取评论列表
	 * @param rsdaId 资源ID
	 * @param page 起始下标记录，每次查询20条
	 * @return
	 */
	public List<Object[]> getCommentsList(Long rsdaId, Long userId, Pagination p);
}
