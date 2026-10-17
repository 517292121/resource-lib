package cn.com.adtech.riss2.bso.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.resource.ResSourceData;

/**
 * 
 * @description 资源的索引处理
 *
 * @author MaChao
 * 
 * @version 1.0
 *
 * @date Aug 17, 20155:16:17 PM
 *
 */
public interface IResourceLuceneService extends ICommonService{

	/**
	 * 根据作业ID批量添加资源到索引
	 *
	 * @author MaChao
	 * @date Aug 18, 2015
	 *
	 * @param rminId
	 */
	public void addIndexByMission(HttpServletRequest request, Long rminId);
	
	/**
	 * 修改资源的索引 
	 *
	 * @author MaChao
	 * @date Aug 18, 2015
	 *
	 * @param resource
	 */
	public void updateResourceToIndex(ResSourceData resource);
	
	/**
	 * 根据资源ID删除索引
	 *
	 * @author MaChao
	 * @date Aug 18, 2015
	 *
	 * @param rsdaId
	 */
	public void deleteIndexById(Long rsdaId);
	
	/**
	 * 删除作业中所有资源的索引
	 */
	public void deleteMissionIndex(Long rminId) throws IOException ;
}
