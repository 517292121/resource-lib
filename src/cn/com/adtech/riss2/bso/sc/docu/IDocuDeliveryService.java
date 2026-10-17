package cn.com.adtech.riss2.bso.sc.docu;
import java.util.List;
import java.util.Map;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;
/**
 * 
 * @author MaChao
 *
 * @date Aug 27, 2015 - 2:58:03 PM
 */
public interface IDocuDeliveryService extends ICommonService {

	/**
	 * 获取所有学科
	 */
	public String getSubjectOptions(final long selected, final long rootId,final String type);
	
	/**
	 * 获取资源类型
	 */
	String getRltyString(long selected);
	
	/** 
	 * 文献传递服务-列表
	 */
	List getDocuList(Pagination pagination,Long suinId);
	
	/** 
	 * 文献传递服务
	 */
	List getDocuList(Pagination pagination, Map<String, Object> map);
	
}
