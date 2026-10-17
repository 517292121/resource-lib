package cn.com.adtech.riss2.jdbcms;

import cn.com.adtech.riss2.cache.SysConfigInfo;

/**
 * SQL语句处理对象
 * @author MaChao
 *
 */
public class SqlBasicUnit {

	
	final public static int DB_TYPE_ORACLE = 1;
	final public static int DB_TYPE_MYSQL = 2;
	final public static int DB_TYPE_MSSQL = 3;
	
	/**
	 * ��封装ORACLE数据库分页SQL语句 
	 * @param sql 普通的SQL查询语句
	 * 	 * @return 封装后的分页查询语句
	 */
	public static String ParseSqlByOracle(String sql, int begin, int num){
		if(null == sql || sql.length() < 3){
			return "";
		}
		if(begin == 0 && num ==0){
			return sql;
		}
		sql = "select * from (select rownum as linenum,sourceTable.* from("+sql+") sourceTable where rownum<="+(begin+num)+") where linenum>" + begin;
		return sql;
	}
	
	/**
	 * ��封装MYSQL数据库分页SQL语句 
	 * @param sql 普通的SQL查询语句
	 * 	 * @return 封装后的分页查询语句
	 */
	public static String ParseSqlByMysql(String sql, int begin, int num){
		if(null == sql || sql.length() < 3){
			return "";
		}
		if(begin == 0 && num ==0){
			return sql;
		}
		sql = "select mysql.* from ("+sql+") as mysql limit " + begin + "," + num; 
		return sql;
	}
	
	/**
	 * ��封装ORACLE数据库查询总记录SQL语句 
	 */
	public static String ParseCountSqlByOracle(String sql){
		if(null == sql || sql.length() < 3){
			return "";
		}
		sql = "select count(*) from ("+sql+")"; 
		return sql;
	}

	/**
	 * ��封装MYSQL数据库查询总记录SQL语句 
	 */
	public static String ParseCountSqlByMysql(String sql, String countColumn){
		if(null == sql || sql.length() < 3){
			return "";
		}
		sql = "select count(countQuery."+countColumn +") from ("+sql+") as countQuery"; 
		return sql;
	}

	/**
	 * Apply the pagination syntax for the configured database.  Older callers
	 * used the Oracle-only methods directly, which made the same screens fail
	 * after switching to MySQL.
	 */
	public static String parseSql(String sql, int begin, int num){
		if (isMysql()) {
			return ParseSqlByMysql(sql, begin, num);
		}
		return ParseSqlByOracle(sql, begin, num);
	}

	public static String parseCountSql(String sql){
		if (isMysql()) {
			if(null == sql || sql.length() < 3){
				return "";
			}
			return "select count(*) from ("+sql+") as countQuery";
		}
		return ParseCountSqlByOracle(sql);
	}

	public static boolean isMysql(){
		String dialect = SysConfigInfo.DATABASE_DIALECT;
		return dialect != null && dialect.toLowerCase().indexOf("mysql") >= 0;
	}

	/**
	 * Build the old START WITH/CONNECT BY descendant query for the configured
	 * database. MySQL 8 uses a recursive CTE for the equivalent hierarchy.
	 */
	public static String hierarchyIdsSql(String table, String idColumn, String parentColumn, long rootId){
		if(!isMysql()){
			return "select " + idColumn + " from " + table + " start with " + idColumn + " = " + rootId
					+ " connect by prior " + idColumn + " = " + parentColumn;
		}
		return "WITH RECURSIVE hierarchy AS ("
				+ " SELECT " + idColumn + " AS node_id FROM " + table + " WHERE " + idColumn + " = " + rootId
				+ " UNION ALL SELECT child." + idColumn + " FROM " + table + " child"
				+ " INNER JOIN hierarchy parent ON child." + parentColumn + " = parent.node_id"
				+ ") SELECT node_id FROM hierarchy";
	}

	/** Build the subject hierarchy query used by the legacy HTML option lists. */
	public static String subjectHierarchySql(){
		if(!isMysql()){
			return "select level,a.RSTY_ID,a.RSTY_NAME from RES_SUBJECT_TYPE a start with RSTY_ID=0"
					+ " connect by prior RSTY_ID=RSTY_PID";
		}
		return "WITH RECURSIVE hierarchy AS ("
				+ " SELECT 1 AS hierarchy_level, a.RSTY_ID, a.RSTY_NAME"
				+ " FROM RES_SUBJECT_TYPE a WHERE a.RSTY_ID=0"
				+ " UNION ALL SELECT h.hierarchy_level + 1, child.RSTY_ID, child.RSTY_NAME"
				+ " FROM RES_SUBJECT_TYPE child INNER JOIN hierarchy h"
				+ " ON child.RSTY_PID=h.RSTY_ID"
				+ ") SELECT hierarchy_level AS level, RSTY_ID, RSTY_NAME FROM hierarchy";
	}
	
	
	public static void main(String[] args){
		String sql = "select a.rmst_name,b.rlty_name from RES_META_STORE a,RES_LITER_TYPE b,RES_STORE_LITER t where b.rlty_id=t.rlty_id and a.rmst_id = t.rmst_id ";
		System.out.println(ParseSqlByOracle(sql, 2, 2));
	}
}











