package cn.com.adtech.riss2.bso.platform;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;

/**
 * 
 * 文档库资源管理
 *
 * @author MaChao
 *
 * @date：Jan 22, 2018
 */
public interface ITextResourceService extends ICommonService{

	/**
	 * 查询资源列表
	 * @param p
	 * @param rtliId
	 * @param rlclId
	 * @return
	 */
	public List<Object[]> getResourceList(Pagination p, Long rtliId, Long rlclId, String sort,int isCmd);
}
