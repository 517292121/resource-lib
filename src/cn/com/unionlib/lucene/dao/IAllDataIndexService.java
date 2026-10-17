package cn.com.unionlib.lucene.dao;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import cn.com.adtech.riss2.bso.ICommonService;

/**
 * 
 * 平台所有数据一键（全部）创建索引管理
 *
 * @author MaChao
 *
 * @date：Feb 6, 2018
 */
public interface IAllDataIndexService extends ICommonService{

	/**
	 * 一键创建所有资源到索引
	 */
	public void saveResToIndex(HttpServletRequest request);
	
	/**
	 * 一键创建所有文本库资源到索引
	 */
	public void saveTextResToIndex();
	
	/**
	 * 一键创建所有信息到索引
	 */
	public void saveInfoToIndex();
	
	
	
	/**
	 * 一键更新全部数据到索引
	 */
	public void saveAlldataToIndex(HttpServletRequest request);
	
	/**
	 * 获取资源总数
	 * @return 资源,文本资源,资讯 以“,”分割的字符串返回
	 */
	public String getResCounts() throws IOException;
	
	/**
	 * 删除无效索引
	 * @return
	 */
	public int deleteInvalidIndex() throws IOException;
}
