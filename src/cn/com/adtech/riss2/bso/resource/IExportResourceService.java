package cn.com.adtech.riss2.bso.resource;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;

/**
 * 
 * 资源批量导出管理
 *
 * @author MaChao
 * 
 * @version 1.0
 *
 * @date Nov 4, 20169:47:33 AM
 *
 */
public interface IExportResourceService extends ICommonService{


	/**
	 * 获取资源列表
	 *
	 * @author MaChao
	 * @date Nov 4, 2016
	 *
	 * @param p  分页对象
	 * @param rmstId  特色库ID 
	 * @param rltyId  资源类型ID
	 * @param map  其它检索参数
	 * @param sort
	 * @param dir
	 * @return
	 */
	public List<Object[]> getResourceList(Pagination p, Long rmstId,
			Long rltyId, Map<String, Object> map, String sort, String dir);
	
	
	/**
	 * 资源导出
	 *
	 * @author MaChao
	 * @date Nov 7, 2016
	 *
	 * @param outStream
	 * @param map
	 */
	public void exportToExcel(OutputStream outStream, Map<String, Object> map) throws IOException;
}
