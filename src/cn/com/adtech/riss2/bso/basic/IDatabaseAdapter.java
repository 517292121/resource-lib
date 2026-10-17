package cn.com.adtech.riss2.bso.basic;

/**
 * @description 各种数据库函数处理接口<br>
 * 每个数据库对函数、sql语句都会有一些不同的处理方式。当要用到hql以外的语句时，必须由适配器做统一的转换<br>
 * 本接口是主定义了数据库常用的一些函数处理，已实现的暂时有oracle和mysql数据库<br>
 * 经测试，几个常用的聚合函数：count、sum、max、min、avg。是各个数据库都兼容，无须转换即可直接使用<br>
 * 
 * @author MaChao
 *
 * @date 2012-8-10上午10:55:39
 *
 */
public interface IDatabaseAdapter {

	/**
	 * 将查询的内容转换成小写的函数，比如用户登录需要将用户名统一转成小写
	 * 
	 * @param eleName
	 *          查询字段名
	 * @return 
	 * 			已转成小写的查询字段名
	 */
	public String toLower(String eleName);
	
	/**
	 * 将查询的内容转换成大写的函数
	 * 
	 * @param eleName
	 *          查询字段名
	 * @return 
	 * 			已转成大写的查询字段名
	 */
	public String toUpper(String eleName);
	
	
	/**
	 * 将查询的内容转换成日期格式类型(yyyy-MM-dd)的字符串
	 * @param eleName
	 * 			查询字段名
	 * @return
	 * 			日期格式类型字符串，注意这里只能转成这种格式，不是通用的字符串转换函数
	 */
	public String toDateString(String eleName);
	
	
	/**
	 * 转换成日期类型，格式(yyyy-MM-dd)
	 * 
	 * @param eleName
	 *            查询字段名
	 * @return
	 */
	public String toDate(String dateStr);
	
	/**
	 * 数据库中的字段转换成日期类型，格式(yyyy-MM-dd)
	 * 
	 * @param eleName
	 *            查询字段名
	 * @return
	 */
	public String dbToDate(String dateStr);
	
	/**
	 * 将属性值为null的数据转换为0<br>
	 * 注：该函数目前hql只支持oracle，对mysql的的ifnull函数尚不支持。因此尽量在程序中进行对null的处理<br>
	 * 
	 * @param eleName
	 * @return
	 */
	public String nullTo0(String eleName);
	
	/**
	 * 将日期的年转为字符串
	 * @param eleName 待查询的属性
	 * @return
	 */
	public String dataYearToString(String eleName);
	
	/**
	 * 将日期的月和日转为MM-dd格式的字符串
	 * @param eleName 待查询的属性
	 * @return
	 */
	public String dataMdToString(String eleName);
	
	/**
	 * 将字符串转换成数字
	 * @param eleName
	 * @return
	 */
	public String toNumber(String eleName);
}
