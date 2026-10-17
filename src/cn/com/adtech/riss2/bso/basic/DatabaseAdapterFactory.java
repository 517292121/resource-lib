package cn.com.adtech.riss2.bso.basic;

import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;

/**
 * 
 * @description 数据库适配器管理工厂，以单例模式管理
 *
 * @author MaChao
 *
 * @date 2012-8-10上午11:26:49
 *
 */
public class DatabaseAdapterFactory {
	
	/**
	 * 定义私有函数防止实例化
	 */
	private DatabaseAdapterFactory(){
		
	}

	/**
	 * 自身静态变量
	 */
	private static DatabaseAdapterFactory instance = null;
	
	/**
	 * 数据库适配器接口
	 */
	private static IDatabaseAdapter databaseAdapter = null;
	
	/**
	 * 获取自身实例对象
	 * @return
	 */
	public static DatabaseAdapterFactory getInstance(){
		if(instance == null){
			instance = new DatabaseAdapterFactory();
		}
		return instance;
	}
	
	/**
	 * 获取数据库函数适配器。默认为oracle
	 * @return
	 */
	public IDatabaseAdapter getDatabaseAdapter(){
		if(databaseAdapter != null){
			return databaseAdapter;
		}
		
		String dialect = SysConfigInfo.DATABASE_DIALECT;
		//如果没有正常配置方言，则返回oracle的适配器
		if(CommonUtil.isNotNull(dialect) == false){
			databaseAdapter = new DatabaseAdapterForOracle();
			return databaseAdapter;
		}
		if(dialect.equals("org.hibernate.dialect.MySQLDialect")
				|| dialect.equals("org.hibernate.dialect.MySQL5Dialect")
				|| dialect.equals("org.hibernate.dialect.MySQL5InnoDBDialect")
				|| dialect.equals("org.hibernate.dialect.MySQLInnoDBDialect")){
			databaseAdapter = new DatabaseAdapterForMysql();
		} 
		else if (dialect.equals("org.hibernate.dialect.Oracle10gDialect")) {
			databaseAdapter = new DatabaseAdapterForOracle();
		}
		else{
			//求知方言类型时也返回oracle适配器
			databaseAdapter = new DatabaseAdapterForOracle();
		}
		return databaseAdapter;
	}
	
}
