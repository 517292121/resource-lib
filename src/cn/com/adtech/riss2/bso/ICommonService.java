/***************************************************************************************
 	*	 
 	* 该接口未被编译测试过
 	*
 	* @see (HibernateDao.java、DBMS.java)
 	* 
 *
 * 开发公司: 重庆亚德科技研发中心
 * 
 * 版权:本文件版权归属重庆亚德科技研发中心
 * 
 	* @author MaChao
 	* 
 	* @since 该文件所支持的JDK版本号: jdk1.6 
 	*
 	* @date  (开发日期): 2010-08-05
 	* 
 	* @version 1.2
 	* 
 *
 * 最后更新日期: 2010-11-05
 *
 * 更改人: 王林
 * 
 * 更改内容:将原先DBMS类中通过自定义连接池访问数据库的方法整合到本类通过spring管理。
 *   
 * 复审人:   
 * 
 * 复审时间： 
 * 
 * 复审结果:
 *
 **************************************************************************************/

package cn.com.adtech.riss2.bso;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.orm.hibernate3.HibernateTemplate;

import cn.com.adtech.riss2.domain.SysTouristBrowseCache;
import cn.com.adtech.riss2.domain.resource.ResSourceData;

import com.jbetter.framework.session.Pagination;
import com.jbetter.spring.orm.PageHibernateTemplate;


/**
 * 
 * 该接口是通过spring封装Hibernate和Jdbc的操作数据库相关方法
 * 
 * @author MaChao
 *
 */
@SuppressWarnings("unchecked")
public interface ICommonService {
	
	public HibernateTemplate getServiceHibernateTemplate();
	
	public PageHibernateTemplate getServicePageHibernateTemplate();
	
	
	/*******************************************************************************************
	 * 以下是spring封装hibernate操作数据库的基本方法
	 *******************************************************************************************/
	
	/**
	 * 保存实体对象
	 */
	public Object save(Object object);

	
	/**
	 * 获取实体对象
	 */
	public Object get(Class clazz, Serializable id);

	
	/**
	 * 删除实体对象
	 */
	public void delete(Object object);
	
	
	/**
	 * 通过Hql语句查询对象集合
	 */
	public List find(String queryString);
	
	
	/**
	 * 根据实体名称和排序字段属性查询对象集合
	 */
	public List getSupportData(String className, String order);

	
	/**
	 * 根据实体名称和排序字段属性加查询条件查询对象集合
	 */
	public List getSupportData(String className, String order, String where);


	
	/**
	 * 通过每个url地址查询SysFunction中对应的ID
	 * */
	public Long getSysFunction(String url) ;
	
	
	/**
	 * 根据hql查询总记录数
	 */
	public long getCountNumber(final String count_hql);
	
	
	/**
	 * 	根据hql查询总记录数,预防sql注入
	 * @param count_hql
	 * @param objects		参数
	 * @return
	 * 
	 * @author MaChao
	 * @date	2020.04.15
	 * @version	1.0
	 */
	public long getCountNumber(final String count_hql, Object...objects);
	
	/**
	 * 获取一个值的hql查询
	 * @param hql
	 * @return
	 */
	public String getSingleData(String hql);
	

	/****************************************************************************************************
	 * 以下是通过spring封装jdbc操作数据库的基本方法
	 ****************************************************************************************************/
	
	/**
	 * 通过sql将查询出的数据通过List<Map<String, String>>形式返回.
	 * Map<String, String>为一条数据，其中key为表中字段名称，value为字段对应的值
	 */
	public List<Map<String, String>> getMapList(final String querySql,final List<Object> paramlist);
	
	
	/**
	 * 通过sql将查询出的数据通过List<String[]>形式返回.
	 * String[]为一条数据，其中数组中的值为根据查询的字段一一对应 
	 */
	public List<String[]> getStrList(final String sql, final List paramlist);
	
	
	/**
	 * 该方法为sql查询一个字段，一列多行的查询，将所有的值通过对象保存到集合中返回
	 */
	public List<Object> getSingleColoumList(final String sql, final List paramlist);
	
	
	/**
	 * 该方法为用于sql查询一个值时用，但都是封装成的字符串，通常用于统计查询
	 */
	public String getFirstData(final String sql);
	
	
	/**
	 * 该方法是封装jdbc中的execute，可用于删除和修改的sql
	 */
	public boolean execute(final String sql);
	
	
	/**
	 * 该方法是封装jdbc中的executeUpdate
	 */
	public boolean executeUpdate(final String sql);
	
	
	/**
	 * 该方法是封装jdbc中的execute，但可以同时执行多条sql,已开启事物管理
	 */
	public boolean executeMultiSql(final String[] sqlArr);
	
	
	/**
	 * 该方法是封装jdbc中的execute，但可以同时执行多条sql,已开启事物管理
	 */
	public boolean executeMultiSql(List<String> sqlList);
	
	
	/**
	 * 该方法是封装jdbc中的executeUpdate，但可以同时执行多条sql,已开启事物管理
	 */
	public boolean executeUpdateMultiSql(final String[] sqlArr);
	
	
	
	/**************************************************************************************************
	 * 以下是spring通过jdbc封装Ext数据的基本操作
	 **************************************************************************************************/
	
	/**
	 * 通过sql语句封装Ext中Table的Json数据库
	 * @param page_sql 基本查询sql语句
	 * @param paramlist 参数集合，如果没有参数可以为null
	 * @param begin 起始记录
	 * @param num 每页查询数量
	 */
	public String getListToJson(final String page_sql, final List<Object> paramlist,final int begin, final int num);
	
	
	/**
	 * 获取下拉选择框的Json数据
	 */
	public String getJsonByOption(final String sql);
	
	
	/**
	 * 获取游客访问缓存记录
	 * @return
	 * @author MaChao
	 * @date 2010-12-31
	 */
	public List<SysTouristBrowseCache> getSysTouristBrowseCacheList();
	
	/**
	 * 获得馆藏位置列表
	 * @return List
	 * @author MaChao
	 * @date 2011-04-21
	 */
	public List getSCPList(Pagination pagination, Map<String, String> conditions);
	
	/**
	 * 获得馆藏位置列表
	 * @return List
	 * @author MaChao
	 * @date 2011-04-21
	 */
	public List getSCPList();
	
	/**
	 * 获得敏感词列表
	 * @return List
	 * @author MaChao
	 * @date 2011-04-22
	 */
	public List getSSWList(Pagination pagination, Map<String, String> conditions);
	
	/**
	 * 获得敏感词列表
	 * @return
	 */
	public List getSSWList();
	
	/**
	 * 积分增加
	 */
	public void updateUsersPoint(Long userId,String type);
	
	/**
	 * 日志记录
	 *
	 * @author MaChao
	 * @date Nov 28, 2015
	 * @time 5:15:58 PM
	 */
	public void saveAccessLog(String type,String ip,Long id,String con);
	
	
	/**
	 * 获取当前游客当天的下载量
	 *
	 * @author MaChao
	 * @date Jul 22, 2016
	 * @time 2:17:53 PM
	 */
	public Integer getDownNumByVistor(String ip);
	/**
	 * 单个资源封面转换
	 * @author MaChao
	 * @date 2019-11-13
	 * @param request
	 * @param resource
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public ResSourceData parseResCover(HttpServletRequest request,
			ResSourceData resource) throws UnsupportedEncodingException;
}
