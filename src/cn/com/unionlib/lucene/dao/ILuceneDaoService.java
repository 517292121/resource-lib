package cn.com.unionlib.lucene.dao;

import java.io.IOException;
import java.util.List;

import cn.com.unionlib.lucene.bean.ResMainBean;

/**
 * 
 * @description lucene数据操作接口
 *
 * @author MaChao
 * 
 * @version 1.0
 *
 * @date Jun 23, 201510:35:24 AM
 *
 */
public interface ILuceneDaoService {
	
	/**
	 * 新增一个数据对象到索引 
	 *
	 * @author MaChao
	 * @date Jul 1, 2015
	 *
	 * @param dataBean ：已经封装好的数据对象
	 * @return
	 */
	public boolean addDatabean(ResMainBean dataBean);
	
	/**
	 * 新增多个数据对象到索引 
	 *
	 * @author MaChao
	 * @date Jul 1, 2015
	 *
	 * @param beanList : 已经封装好的数据对象集合
	 * @return
	 */
	public boolean addMultiDatabean(List<ResMainBean> beanList);
	
	/**
	 * 修改数据对象
	 *
	 * @author MaChao
	 * @date Jul 1, 2015
	 *
	 * @param dataBean
	 * @return
	 */
	public boolean updateDatabean(ResMainBean dataBean);
	
	/**
	 * 根据数据ID删除数据对象
	 *
	 * @author MaChao
	 * @date Jul 1, 2015
	 *
	 * @param id
	 * @return
	 */
	public boolean deleteDatabean(String id);
	
	/**
	 * 批量删除索引
	 * @param idlist
	 * @return
	 */
	public void deleteBatchData(List<String> idlist) throws IOException ;
}
