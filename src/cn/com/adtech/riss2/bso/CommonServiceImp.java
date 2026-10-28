/***************************************************************************************
 	*	 
 	* 该类未被编译测试过
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
import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysFunction;
import cn.com.adtech.riss2.domain.SysTouristBrowseCache;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.count.SysAccessCounter;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.jdbcms.SqlBasicUnit;
import cn.com.adtech.riss2.jdbcms.ToolUnit;

import com.jbetter.framework.session.Pagination;
import com.jbetter.spring.orm.HibernateDao;
import com.jbetter.spring.orm.PageHibernateTemplate;

/**
 * 
 * 该类是通过spring封装Hibernate和Jdbc的操作数据库相关方法
 * 
 * @author MaChao
 *
 */
@SuppressWarnings("unchecked")
public class CommonServiceImp extends HibernateDao implements ICommonService {
	
	public HibernateTemplate getServiceHibernateTemplate() {
		return getHibernateTemplate();
	}
	public PageHibernateTemplate getServicePageHibernateTemplate() {
		return getPageHibernateTemplate();
	}

	/***************************************************************************************************************************************
	 * 以下是spring封装hibernate操作数据库的基本方法
	 **************************************************************************************************************************************/
		
	/**
	 * 保存实体对象
	 */
	public Object save(Object object) {
		getHibernateTemplate().saveOrUpdate(object);
		return object;
	}

	/**
	 * 获取实体对象
	 */
	public Object get(Class clazz, Serializable id) {
		return getHibernateTemplate().get(clazz, id);
	}

	/**
	 * 删除实体对象
	 */
	public void delete(Object object) {
		getHibernateTemplate().delete(object);
	}

	/**
	 * 通过Hql语句查询对象集合
	 */
	public List find(String queryString) {
		return getHibernateTemplate().find(queryString);
	}

	/**
	 * 根据实体名称和排序字段属性查询对象集合
	 */
	public List getSupportData(String className, String order) {
		String lowercase = className.substring(0, 1).toLowerCase() + className.substring(1);
		String hql = "from " + className + " " + lowercase;
		if(order != null && !order.equals("")) {
			hql += " order by " + lowercase + "." + order;
		}
		org.springframework.orm.hibernate3.HibernateTemplate temp = getHibernateTemplate();
		temp.setCacheQueries(true);
		List list = temp.find(hql);
		return list;
	}
	
	/**
	 * 根据实体名称和排序字段属性加查询条件查询对象集合
	 */
	public List getSupportData(String className, String order, String where) {
		String lowercase = className.substring(0, 1).toLowerCase() + className.substring(1);
		String hql = "from " + className + " " + lowercase;
		if(CommonUtil.isNotNull(where))
			hql += " where " + where;
		if(order != null && !order.equals("")) {
			hql += " order by " + lowercase + "." + order;
		}
		org.springframework.orm.hibernate3.HibernateTemplate temp = getHibernateTemplate();
		temp.setCacheQueries(true);
		List list = temp.find(hql);
		return list;
	}

	/**
	 * 根据hql查询总记录数
	 * @param count_hql
	 * @return
	 */
	public long getCountNumber(final String count_hql){
		List list = getHibernateTemplate().find(count_hql);
		if(list != null && list.size() > 0){
			return (Long)list.get(0);
		}else{
			return 0;
		}
	}
	
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
	public long getCountNumber(final String count_hql, Object...objects){
		List list = getHibernateTemplate().find(count_hql, objects);
		if(list != null && list.size() > 0){
			return (Long)list.get(0);
		}else{
			return 0;
		}
	}
	
	/**
	 * 获取单个对象
	 * @param hql
	 * @return
	 */
	public Object getFirstDataByHql(String hql){
		List list = getHibernateTemplate().find(hql);
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}


	@Override
	public Long getSysFunction(String url) {
			String[] u = url.split("/", 3);
			String[] uu = u[2].split("&",2);
			//System.out.println(u[2]);
			String hql = "from SysFunction sf where sf.sfunUrl='/"+ uu[0] +"'";
			List<SysFunction> list = this.getHibernateTemplate().find(hql);
			return list.get(0).getSfunId();
	}

	/****************************************************************************************************
	 * 以下是通过spring封装jdbc操作数据库的基本方法
	 ****************************************************************************************************/
	
	protected Connection conn = null;
	protected PreparedStatement ps = null;
	protected ResultSet rs = null;
	protected Statement stmt = null;
	
	/**
	 * 通过sql将查询出的数据通过List<Map<String, String>>形式返回.
	 * Map<String, String>为一条数据，其中key为表中字段名称，value为字段对应的值
	 */
	public List<Map<String, String>> getMapList(final String querySql,final List<Object> paramlist){
		List<Map<String, String>> list = getHibernateTemplate().executeFind(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List<Map<String, String>> maplist = new LinkedList<Map<String,String>>();
				conn = session.connection();
				try {
					ps = conn.prepareStatement(querySql);
					if(paramlist != null && paramlist.size() > 0){  //循环设置参数
				    	for(int i=1; i<= paramlist.size(); i++){
				    		ps.setObject(i, paramlist.get(i -1));
				    	}
				    }
				    
					rs = ps.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData();
					int s = rsmd.getColumnCount();
					Map<String, String> map = null;
					while(rs.next()){
						map = new HashMap<String, String>();
						for(int i = 1; i <= s; i++){
							map.put(rsmd.getColumnName(i), rs.getString(rsmd.getColumnName(i)));
						}
						maplist.add(map);
					}
					rsmd = null;
					return maplist;
				} catch (Exception e) {
					logger.error("在CommonServiceImp的getMapList中发生异常:" + e.getMessage());
					e.printStackTrace();
					return null;
				} 
				finally {
					if(rs != null){rs.close();}
				}
			}
		});
		return list;
	}
	
	/**
	 * 通过sql将查询出的数据通过List<String[]>形式返回.
	 * String[]为一条数据，其中数组中的值为根据查询的字段一一对应 
	 */
	public List<String[]> getStrList(final String sql, final List paramlist){
		List<String[]> list = getHibernateTemplate().executeFind(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				conn = session.connection();
				List<String[]> strlist = new LinkedList<String[]>();
				try {
					PreparedStatement ps = conn.prepareStatement(sql);
					if (paramlist != null && paramlist.size() > 0) {
						for (int i = 1; i <= paramlist.size(); i++) {
							ps.setObject(i, paramlist.get(i - 1));
						}
					}
					rs = ps.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData();
					int s = rsmd.getColumnCount();
					String[] dataArr = null;
					while (rs.next()) {
						dataArr = new String[s];
						for (int i = 1; i <= s; i++) {
							dataArr[i - 1] = rs.getString(i);
						}
						strlist.add(dataArr);
					}
					rsmd = null;
					ps.close();
					return strlist;
				} catch (SQLException e) {
					logger.error("在CommonServiceImp的getStrList中发生异常:" + e.getMessage());
					e.printStackTrace();
					return null;
				} finally {
					try {
						if(rs != null) rs.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		return list;
	}
	
	/**
	 * 该方法为sql查询一个字段，一列多行的查询，将所有的值通过对象保存到集合中返回
	 */
	public List<Object> getSingleColoumList(final String sql, final List paramlist) {
		List<Object> list = getHibernateTemplate().executeFind(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List<Object> list = new LinkedList<Object>();
				try {
					conn = session.connection();
					PreparedStatement ps = conn.prepareStatement(sql);
					if (paramlist != null && paramlist.size() > 0) {
						for (int i = 1; i <= paramlist.size(); i++) {
							ps.setObject(i, paramlist.get(i - 1));
						}
					}
					rs = ps.executeQuery();
					while (rs.next()) {
						list.add(rs.getObject(1));
					}
					if(ps != null){
						ps.close();
						ps = null;
					}
					return list;
				} catch (Exception e) {
					logger.error("在DBMS的getSingleColoumList中发生异常:" + e.getMessage());
					e.printStackTrace();
					return null;
				} finally {
					try {
						if(rs != null) rs.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		return list;
	}
	
	/**
	 * 该方法为用于sql查询一个值时用，但都是封装成的字符串，通常用于统计查询
	 */
	public String getFirstData(final String sql){
		List<Object> list = getHibernateTemplate().executeFind(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					conn = session.connection();
					String data = "";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
					if(rs != null && rs.next()){
						data = rs.getString(1);
					}
					List<String> list = new LinkedList<String>();
					list.add(data);
					return list;
				} catch (Exception e) {
					logger.error("在CommonServiceImp的getFirstData中发生异常:" + e.getMessage());
					e.printStackTrace();
					return null;
				} finally {
					try {
						if(rs != null) rs.close();
						if(stmt != null) stmt.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		if(list != null && list.size() > 0){
			return list.get(0).toString();
		}else{
			return "";
		}
	}
	
	/**
	 * 该方法是封装jdbc中的execute，可用于删除和修改的sql
	 */
	public boolean execute(final String sql){
		Object b = getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					conn = session.connection();
					stmt = conn.createStatement();
					stmt.execute(sql);
					return true;
				} catch (Exception e) {
					logger.error("在DBMS的execute中发生异常:" + e.getMessage());
					logger.error("异常SQL>>" + sql);
					e.printStackTrace();
					return false;
				} finally {
					if(stmt != null) stmt.close();
				}
			}
		});
		return (Boolean) b;
	}
	
	/**
	 * 该方法是封装jdbc中的executeUpdate
	 */
	public boolean executeUpdate(final String sql){
		Object b = getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					conn = session.connection();
					stmt = conn.createStatement();
					stmt.executeUpdate(sql);
					return true;
				} catch (Exception e) {
					logger.error("在DBMS的executeUpdate中发生异常:" + e.getMessage());
					logger.error("异常SQL>>" + sql);
					e.printStackTrace();
					return false;
				} finally {
					if(stmt != null) stmt.close();
				}
			}
		});
		return (Boolean) b;
	}
	
	/**
	 * 批量执行 SQL；加入已有事务，无外层事务时整批提交。
	 */
	public boolean executeMultiSql(final String[] sqlArr){
		return executeSqlStatements(sqlArr, false);
	}
	
	/**
	 * 该方法是封装jdbc中的execute，但可以同时执行多条sql,已开启事物管理
	 */
	public boolean executeMultiSql(List<String> sqlList){
		if(sqlList == null || sqlList.size() < 1){
			return false;
		}
		String[] arr = new String[sqlList.size()];
		arr = sqlList.toArray(arr);
		return executeMultiSql(arr);
	}
	
	/**
	 * 批量执行更新 SQL，遵循与 executeMultiSql 相同的事务边界。
	 */
	public boolean executeUpdateMultiSql(final String[] sqlArr){
		return executeSqlStatements(sqlArr, true);
	}

	private boolean executeSqlStatements(final String[] sqlArr, final boolean updateOnly){
		if(sqlArr == null || sqlArr.length < 1){
			return false;
		}
		return getHibernateTemplate().execute(new HibernateCallback<Boolean>(){
			public Boolean doInHibernate(Session session) throws HibernateException, SQLException {
				Transaction transaction = session.getTransaction();
				boolean ownsTransaction = !transaction.isActive();
				if(ownsTransaction){
					transaction.begin();
				}
				try {
					// JDBC objects belong to this invocation, not the singleton service.
					try(Statement statement = session.connection().createStatement()){
						for(String sql : sqlArr){
							if(sql != null && sql.trim().length() > 1){
								if(updateOnly){
									statement.executeUpdate(sql);
								}else{
									statement.execute(sql);
								}
							}
						}
					}
					if(ownsTransaction){
						transaction.commit();
					}
					return true;
				} catch (SQLException | RuntimeException e) {
					if(ownsTransaction && transaction.isActive()){
						try {
							transaction.rollback();
						}catch(RuntimeException rollbackFailure){
							e.addSuppressed(rollbackFailure);
						}
					}
					// Propagate failures so Spring rolls back the enclosing operation.
					throw e;
				}
			}
		});
	}
	

	/**************************************************************************************************
	 * 以下是spring通过jdbc封装Ext数据的基本操作
	 **************************************************************************************************/
	
	protected String countSql = "";
	protected String pageSql = "";
	
	/**
	 * 通过sql语句封装Ext中Table的Json数据库(2011-06-03日发现该方法bug,该方法不支持paramlist参数。否则会出错)
	 * @param page_sql 基本查询sql语句
	 * @param paramlist 参数集合，如果没有参数可以为null
	 * @param begin 起始记录
	 * @param num 每页查询数量
	 */
	public String getListToJson(final String page_sql, final List<Object> paramlist,final int begin, final int num){
		
		String jsondata = getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					countSql = SqlBasicUnit.parseCountSql(page_sql);
					pageSql = SqlBasicUnit.parseSql(page_sql, begin, num);
					conn = session.connection();
					stmt = conn.createStatement();
					rs = stmt.executeQuery(countSql);
					int count = 0;
					if(rs.next()){  //获取总页数
						count = rs.getInt(1);
					}
					
					PreparedStatement ps = conn.prepareStatement(pageSql);
				    if(paramlist != null && paramlist.size() > 0){  //设置参数
				    	for(int i=1; i<= paramlist.size(); i++){
				    		ps.setObject(i, paramlist.get(i -1));
				    	}
				    }
				    rs = ps.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData();
					int s = rsmd.getColumnCount();
					
					StringBuffer buffer = new StringBuffer();
					buffer.append("{");
					buffer.append("totalProperty:"+count+",root:[");
					String dataValue = "";
					Date date = null;
					Calendar calendar = null;
					while(rs.next()){
						buffer.append("{");
						for(int i = 1; i <= s; i++){
							/* MySQL reports ordinary column names in lower case, while
							 * the legacy Oracle/ExtJS contract uses upper case names.
							 * Preserve explicit aliases and normalize only plain
							 * underscore-separated names. */
							String columnName = rsmd.getColumnName(i);
							String columnLabel = rsmd.getColumnLabel(i);
							if(columnLabel != null && columnLabel.length() > 0
									&& !columnLabel.equalsIgnoreCase(columnName)){
								columnName = columnLabel;
							}else if(columnName != null && columnName.indexOf('_') >= 0
									&& columnName.equals(columnName.toLowerCase(Locale.ENGLISH))){
								columnName = columnName.toUpperCase(Locale.ENGLISH);
							}
							if(rsmd.getColumnTypeName(i).toUpperCase().equals("DATE")){  //如果是日期类型，则进行格式化
								if(ToolUnit.checkDateTime(rs.getString(i))){
									dataValue = rs.getString(i);
									dataValue = dataValue.substring(0, dataValue.length()-2);
								}else{
									calendar = Calendar.getInstance();
									date = rs.getDate(i);
									if(date == null){
										dataValue = "";
									}else{
										if(calendar.get(Calendar.HOUR_OF_DAY) == 0 && calendar.get(Calendar.MINUTE) == 0
												&& calendar.get(Calendar.SECOND) == 0) {
											dataValue = ConstantsObj.FORMAT_DATE.format(date);
										}else {
											dataValue = ConstantsObj.FORMAT_DATE_TIME.format(date);
										}
									}
								}
							}else{
								dataValue = ToolUnit.stringToJson(rs.getString(i));
							}
						    buffer.append(columnName+":\""+ dataValue + "\"");
						    if(i < s){
				            	buffer.append(",");
				            }
						}
						buffer.append("},");
					}
					try {
						if(rs != null) rs.close();
						if(ps != null) ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					String jsonData = buffer.toString();
					buffer = null;
					if(jsonData.length() > 25){
						jsonData = jsonData.substring(0,jsonData.length()-1);
					}
					jsonData += "]}";
					return jsonData;
				} catch (Exception e) {
					logger.error("在ExtDataOracel中的getListToJson时发生异常,异常SQL>");
					e.printStackTrace();
					return "";
				}
			}
			
		}).toString();
		return jsondata;
	}
	
	/**
	 * 获取下拉选择框的Json数据
	 */
	public synchronized String getJsonByOption(final String sql) {
		String jsondata = getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuffer buffer = new StringBuffer();
				try {
					conn = session.connection();
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
					buffer.append("[");
					while(rs.next()){
						buffer.append("['");
						buffer.append(rs.getString(1));
						buffer.append("','");
						buffer.append(rs.getString(2));
						buffer.append("'],");
					}
					String jsondata = buffer.toString();
					jsondata = jsondata.substring(0,jsondata.length() - 1);
					if(jsondata.length() > 0){
						jsondata += "]";
					}else{
						jsondata = "[]";
					}
					return jsondata;
				} catch (Exception e) {
					logger.error("在CommonServiceImp的getJsonByOption中发生异常：" + e.getMessage());
					logger.error("异常SQL>>" + sql);
					e.printStackTrace();
					return "";
				} finally {
					try {
						if(rs != null) rs.close();
						if(stmt != null) stmt.close();
						buffer = null;
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}).toString();
		
		return jsondata;
	}
	@Override
	public List<SysTouristBrowseCache> getSysTouristBrowseCacheList() {
		String hql = "from SysTouristBrowseCache stbc order by stbc.stbhcValue DESC ";
		return this.getHibernateTemplate().find(hql);
	}
	@Override
	public List getSCPList(Pagination pagination, Map<String, String> conditions) {
		List<Object> param = new ArrayList<Object>();
		String selectHql = "select scp.scpId, scp.scpName, scp.scpIndex ";
		StringBuilder fromHql = new StringBuilder("from SysCollectionPosition scp ");
		StringBuilder orderHql = new StringBuilder();
		// 按标题查询
		String scpNameCondition = conditions.get("scpNameCondition");
		if(ServiceUtil.isNotNull(scpNameCondition)) {
				fromHql.append(" where scp.scpName like ? ");
				param.add("%" + scpNameCondition + "%");
		}
		// 添加排序方式
		String sort = (String)conditions.get("sort");
		String dir = (String)conditions.get("dir");
		if(sort != null && sort.length() > 0 && dir != null && dir.length() > 0) {
			if("scpName".equals(sort))
				orderHql.append(" order by scp.scpName ").append(dir);
			else if("scpIndex".equals(sort))
				orderHql.append(" order by scp.scpIndex ").append(dir);
		}
		String countHql = "select count(*) " + fromHql.toString();
		String hql = selectHql + fromHql.toString() + orderHql.toString();
		return getPageHibernateTemplate().find(pagination, hql, countHql, param.toArray());
	}
	@Override
	public List getSCPList() {
		StringBuilder fromHql = new StringBuilder("from SysCollectionPosition scp ");
		return getHibernateTemplate().find(fromHql.toString());
	}
	@Override
	public List getSSWList(Pagination pagination, Map<String, String> conditions) {
		List<Object> param = new ArrayList<Object>();
		String selectHql = "select ssw.sswId, ssw.sswName, ssw.sswIndex ";
		StringBuilder fromHql = new StringBuilder("from SysSensitiveWord ssw ");
		StringBuilder orderHql = new StringBuilder();
		// 按标题查询
		String sswNameCondition = conditions.get("sswNameCondition");
		if(ServiceUtil.isNotNull(sswNameCondition)) {
				fromHql.append(" where ssw.sswName like ? ");
				param.add("%" + sswNameCondition + "%");
		}
		// 添加排序方式
		String sort = (String)conditions.get("sort");
		String dir = (String)conditions.get("dir");
		if(sort != null && sort.length() > 0 && dir != null && dir.length() > 0) {
			if("sswName".equals(sort))
				orderHql.append(" order by ssw.sswName ").append(dir);
			else if("sswIndex".equals(sort))
				orderHql.append(" order by ssw.sswIndex ").append(dir);
		}
		String countHql = "select count(*) " + fromHql.toString();
		String hql = selectHql + fromHql.toString() + orderHql.toString();
		return getPageHibernateTemplate().find(pagination, hql, countHql, param.toArray());
	}
	@Override
	public List getSSWList() {
		StringBuilder fromHql = new StringBuilder("from SysSensitiveWord ssw ");
		return getHibernateTemplate().find(fromHql.toString());
	}


	/**
	 * 根据hql查询第一个值，返回字符串。如果查询结果为空，则返回""
	 */
	public String getSingleData(String hql) {
		List<Object> list = getHibernateTemplate().find(hql);
		if(list == null || list.size() < 1 || list.get(0) == null){
			return "";
		}
		String data = list.get(0).toString();
		
		list = null;
		return data;
	}

	public void updateUsersPoint(Long userId,String type){
		try { 
			SysUserinfo userinfo = (SysUserinfo)getHibernateTemplate().get(SysUserinfo.class, userId);
				String  integral= ConstantsObj.getSysProperty(type);
				int oldIntegral = 0;
				if(null!=userinfo&&null!=userinfo.getSuinIntegral()){
					oldIntegral = userinfo.getSuinIntegral();
				}
				int newIntegral= oldIntegral+Integer.parseInt(integral); 
				getHibernateTemplate().bulkUpdate("update SysUserinfo sysu set sysu.suinIntegral=" + newIntegral + " where sysu.suinId=" + userId);
				
		}catch (Exception e){
			logger.error(e);
		}
	}
	
	/**
	 * 日志记录
	 */
	@Override
	public void saveAccessLog(String type,String ip,Long id,String con) {
		try{
			SysAccessCounter accessCounter = new SysAccessCounter();
			if(id != null && id > 0L){
				accessCounter.setSacoSuinId(id);
				SysUserinfo userinfo = (SysUserinfo)getHibernateTemplate().get(SysUserinfo.class, id);
				accessCounter.setSacoSuinName(userinfo.getSuinName());
			}else{
				accessCounter.setSacoSuinName(SysAccessCounter.TYPE_NO);
			}
			accessCounter.setSacoType(type);
			accessCounter.setSacoDate(new Date());
			accessCounter.setSacoIp(ip);
			accessCounter.setSacoContent(con);
			save(accessCounter);
		}catch (Exception e) {
			logger.error(e);
		}
	}
	
	/**
	 * 获取当前游客当天的下载量
	 *
	 * @author MaChao
	 * @date Jul 22, 2016
	 * @time 2:17:53 PM
	 */
	@Override
	public Integer getDownNumByVistor(String ip) {
		Calendar today = Calendar.getInstance();
		String hql = "select count(*) from SysAccessCounter saco" +
				" where saco.sacoSuinName='"+SysAccessCounter.TYPE_NO+"'" +
				" and saco.sacoType='"+SysAccessCounter.TYPE_DOWN+"'"+
				" and year(saco.sacoDate)="+today.get(Calendar.YEAR)+
				" and month(saco.sacoDate)="+(today.get(Calendar.MONTH)+1)+
				" and day(saco.sacoDate)="+today.get(Calendar.DAY_OF_MONTH)+
				" and saco.sacoIp='"+ip+"'";
		String num = getSingleData(hql);
		if(CommonUtil.isNotNull(num)){
			return Integer.parseInt(num);
		}else{
			return 0;
		}
	}
	/**
	 * 转换单个资源的封面
	 * @author MaChao
	 * @date 2019-11-13
	 * @param request
	 * @param resource
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@Override
	public ResSourceData parseResCover(HttpServletRequest request,ResSourceData resource) throws UnsupportedEncodingException{
		if(resource == null){
			return resource;
		}
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		int t = resource.getRsdaSourceType() == null ? 0 : resource.getRsdaSourceType();
		
		if(t == 0){ //传统资源格式
			if(resource.getRsdaBigimgUrl() != null && CommonUtil.isNotNull(resource.getRsdaBigimgUrl())){
				resource.setRsdaBigimgUrl(SysConfigInfo.FILE_URL_PATH + resource.getRsdaBigimgUrl());
			}
			return resource;
		}
		
		if(resource.getRsdaFileUrl() == null || !CommonUtil.isNotNull(resource.getRsdaFileUrl())){
			return resource;
		}
		
		String dirpath = resource.getRsdaFileUrl();
		File coverFile = new File(dirpath, "cover.jpg");
		if(coverFile.exists()){
			resource.setRsdaBigimgUrl(basePath + "file/access/" + CommonUtil.encode(coverFile.getAbsolutePath()));
		}else{
			resource.setRsdaBigimgUrl("");
		}
		return resource;
	}
}


















