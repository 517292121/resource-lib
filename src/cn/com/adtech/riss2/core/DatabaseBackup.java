/***************************************************************************************
 	* 数据库自动备份处理
 	* 目前支持oracle（需与项目在同一服务器），mysql（windows版）自动备份
 	*	 
 	* 该接口已测试过
 	*
 	* @see UpdateAllCache.java
 	* 
 *
 * 开发公司: 成都联图科技有限责任公司
 * 
 * 版权:本文件版权归属成都联图科技有限责任公司
 * 
 	* @author MaChao
 	* 
 	* @date  (开发日期): 2019-09
 	* 
 	* @version 1.2
 	*
 	* 
 	* 该方法在UpdateAllCache.java类中，随项目启动时调用
 	* 第一次执行为晚上23点，以后每五天执行一次
 	* 
 *
 * 最后更新日期: 2019-10-09
 *
 * 更改人: 王林
 * 
 * 更改内容: 更新执行时间及周期，调整备份存储路径为file.dir配置路径
 *   
 * 复审人:   王林
 * 
 * 复审时间： 2016-07-06
 * 
 * 复审结果:  已完成
 *
 **************************************************************************************/
package cn.com.adtech.riss2.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.com.adtech.riss2.cache.SysConfigInfo;

/**
 * 数据库自动备份处理
 * 
 * @author MaChao
 *
 */
public class DatabaseBackup {

	//一天的秒数
	private int onedaySeconds = 86400;
	

	/**
	 * @throws Exception 
	 *
	 *
	 */
	public static void main(String[] args) throws Exception {
		DatabaseBackup ref = new DatabaseBackup();
		ref.listenerRun();
		
//		expbase();
	}
	/**
	 * 启动数据同步监听
	 */
	public void listenerRun() {
		SysConfigInfo.initConfig();
		System.out.println(new Date().toLocaleString() + "数据库备份监听开始-----------------------");
		TimerTask task = new TimerTask(){
			@Override
			public void run() {
				try {
					expbase();
				} catch (Exception e) {
					if(e.getMessage() != null){
						System.out.println("异常：" + e.getMessage());
					}
				}
			}
		};
		
		//计算当前时间距离晚上23点还差多少毫秒，如果已经过了，则从第二天23点开始
		Calendar now = Calendar.getInstance();
		String str = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH)+1) + "-" + now.get(Calendar.DAY_OF_MONTH) + " 23:00";
		Date today23 = null;
		try {
			today23 = ConstantsObj.FORMAT_DATE_TIME.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			today23 = new Date();
		}
		
		long t23 = today23.getTime();
		long tnow = System.currentTimeMillis();
		long m = t23 - tnow;
		if(m < 1){
			m = m + (1000 * onedaySeconds);
		}
		
		Timer timer = new Timer();
		//等到晚上23点开始执行，以后每隔两天执行一次
		//参数，1：执行任务   2：第一次等待多久开始执行  3：第一次执行后，每间隔多久重复执行一次；时间单位：毫秒
		timer.schedule(task, m, onedaySeconds * 1000 * 5);
	}

	public static void expbase() throws Exception{
		System.out.println(new Date().toLocaleString() + " 执行");
//		SysConfigInfo.initConfig();
		String databaseType = SysConfigInfo.DATABASE_DIALECT;
		
		if(databaseType != null && databaseType.toLowerCase().indexOf("mysql") >= 0){
			expMysql();
		} else if (databaseType.equals("org.hibernate.dialect.Oracle10gDialect")){
			expOracle();
		}
	}
	
	/**
	 * 导出mysql数据库
	 *
	 */
	private static void expMysql() throws Exception{
		String user = SysConfigInfo.JDBC_USERNAME; // 数据库帐号
		String password = SysConfigInfo.JDBC_PASSWORD; // 登陆密码
		String jdbcUrl = SysConfigInfo.JDBC_URL;//数据库链接地址
		String database = jdbcUrl.substring(jdbcUrl.lastIndexOf("/") + 1); // 需要备份的数据库名
		int queryStart = database.indexOf('?');
		if(queryStart >= 0){
			database = database.substring(0, queryStart);
		}
		String rootPath = SysConfigInfo.DB_BACKUP_DIR;//数据库备份路径
		
		//如果没有备份路径 则在项目同级目录下创建一个目录
		if(rootPath==null){
			File file = new File("");
			String filePath = file.getCanonicalPath();
			rootPath  = filePath+"/bakdb/";
			System.out.println(filePath);
		}
		
		File saveFile = new File(rootPath);
		if (!saveFile.exists()) {// 如果目录不存在
			saveFile.mkdirs();// 创建文件夹
		}
		String hostPort = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2, jdbcUrl.lastIndexOf("/"));
		String host = hostPort;
		int hostPortSeparator = hostPort.lastIndexOf(':');
		if(hostPortSeparator > 0){
			host = hostPort.substring(0, hostPortSeparator);
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dates  =dfs.format(new Date());
		String date  =df.format(new Date());
         
		String filename =date+ ".DMP";//导出文件名
		rootPath = rootPath + filename;//导出路径

		String stmt1 = "mysqldump " + database + " -h "+host+" " + " -u "+ user + " -p" + password+ " --default-character-set=utf8 --result-file=" + rootPath;
		
		System.out.println(stmt1);
		System.out.println("当前时间:"+dates);
		Runtime.getRuntime().exec(stmt1);
		System.out.println("数据库备份成功！");
		
		//导出成功后，删除其它备份文件，只保留最后一次备份文件即可
		File[] files = saveFile.listFiles();
		int filelen = filename.length();
		for(File f : files){
			if(f.getName().equals(filename)){
				//本次备份文件不能删除
				continue;
			}
			if(f.getName().length() != filelen){
				//由于备份文件名比较统一，长度肯定相同。因此过滤文件长度不与自动生成文件名长度的文件，即简单过滤非系统备份的文件
				continue;
			}
			f.delete();
		}
		
	} 
	
	
	/**
	 * 导出oracle数据库
	 *
	 */
	private static void expOracle() throws Exception{
		String username = SysConfigInfo.JDBC_USERNAME; // 数据库帐号
		String password = SysConfigInfo.JDBC_PASSWORD; // 登陆密码
		String jdbcUrl = SysConfigInfo.JDBC_URL;//数据库链接地址
		String databaseSid = jdbcUrl.substring(jdbcUrl.lastIndexOf(":") + 1);
		String rootPath = SysConfigInfo.DB_BACKUP_DIR;//数据库备份路径
		
		//如果没有备份路径 则在项目同级目录下创建一个目录
		if(rootPath==null){
			File file = new File("");
			String filePath = file.getCanonicalPath();
			rootPath  = filePath+"/bakdb/";
			System.out.println(filePath);
		}

		File saveFile = new File(rootPath);
		if (!saveFile.exists()) {// 如果目录不存在
			saveFile.mkdirs();// 创建文件夹
		}
		String host = jdbcUrl.substring(jdbcUrl.indexOf("@") + 1);
		host = host.substring(0, host.indexOf(":")).toLowerCase();
		if(host.equals("localhost") == false && host.equals("127.0.0.1") == false){
			throw new Exception("数据库与项目不在同一服务器，无法支持远程备份！");
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String dates  =dfs.format(new Date());
		 String date  =df.format(new Date());
		 
		String filename =date+".DMP";
		rootPath = rootPath + filename;
		
		String cmd = "exp " + username + "/" + password + "@"+databaseSid+" file=" + rootPath;
		System.out.println("开始导出 :" + cmd);
		System.out.println("当前时间:"+dates);
		
		Process p = Runtime.getRuntime().exec(cmd);
		InputStreamReader isr = new InputStreamReader(p.getErrorStream());
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while ((line = br.readLine()) != null) {
			if(line.indexOf("错误") > 0){
				break;
			}
		}
		p.destroy();
		p.waitFor();
		System.out.println("导出成功");
		
		//导出成功后，删除其它备份文件，只保留最后一次备份文件即可
		File[] files = saveFile.listFiles();
		int filelen = filename.length();
		for(File f : files){
			if(f.getName().equals(filename)){
				//本次备份文件不能删除
				continue;
			}
			if(f.getName().length() != filelen){
				//由于备份文件名比较统一，长度肯定相同。因此过滤文件长度不与自动生成文件名长度的文件，即简单过滤非系统备份的文件
				continue;
			}
			f.delete();
		}
	}
}

