package cn.com.adtech.riss2.bso.basic;

import cn.com.adtech.riss2.core.CommonUtil;

/**
 * 
 * @description Mysql数据库适配器操作实现
 *
 * @author MaChao
 *
 * @date 2012-8-10上午10:57:38
 *
 */
public class DatabaseAdapterForMysql implements IDatabaseAdapter {

	/**
	 * 将查询的内容转换成小写的函数，比如用户登录需要将用户名统一转成小写
	 * 
	 * @param eleName
	 *          查询字段名
	 * @return 
	 * 			已转成小写的查询字段名
	 */
	@Override
	public String toLower(String eleName) {
		if(CommonUtil.isNotNull(eleName)){
			eleName = "lower("+eleName+")";
		}
		return eleName;
	}

	/**
	 * 将查询的内容转换成大写的函数
	 * 
	 * @param eleName
	 *          查询字段名
	 * @return 
	 * 			已转成大写的查询字段名
	 */
	@Override
	public String toUpper(String eleName) {
		if(CommonUtil.isNotNull(eleName)){
			eleName = "upper("+eleName+")";
		}
		return eleName;
	}

	/**
	 * 将查询的内容转换成日期格式类型(yyyy-MM-dd)的字符串
	 * @param eleName
	 * 			查询字段名
	 * @return
	 * 			日期格式类型字符串，注意这里只能转成这种格式，不是通用的字符串转换函数
	 */
	@Override
	public String toDateString(String eleName) {
		if(CommonUtil.isNotNull(eleName)){
			eleName = "cast("+eleName+" as date)";
		}
		return eleName;
	}

	
	/**
	 * 转换成日期类型，格式(yyyy-MM-dd)
	 * 
	 * @param eleName
	 *            查询字段名
	 * @return
	 */
	@Override
	public String toDate(String dateStr) {
		if(CommonUtil.isNotNull(dateStr)){
			dateStr = "str_to_date(\'"+dateStr+"\',\'%Y-%m-%d\')";
		}
		return dateStr;
	}

	/**
	 * 将属性值为null的数据转换为0<br>
	 * hql语句对mysql的ifnull函数不支持<br>
	 * 目前尚未找到mysql的其它解决办法，建不要用该函数，尽量在程序中控制null的处理<br>
	 * @param eleName
	 * @return
	 */
	@Override
	public String nullTo0(String eleName) {
		if(CommonUtil.isNotNull(eleName)){
//			eleName = "ifnull("+eleName+",0)";
		}
		return eleName;
	}

	
	/**
	 * 将日期的年转为字符串
	 * @param eleName 待查询的属性
	 * @return
	 */
	@Override
	public String dataYearToString(String eleName) {
		if(CommonUtil.isNotNull(eleName)){
			eleName = "DATE_FORMAT("+eleName+", '%Y')";
		}
		return eleName;
	}
	
	/**
	 * 将字符串转换成数字
	 * @param eleName
	 * @return
	 */
	@Override
	public String toNumber(String eleName) {
		if(CommonUtil.isNotNull(eleName)){
			eleName = eleName + " + 0";
		}
		return eleName;
	}

	
	/**
	 * 将日期的月和日转为MM-dd格式的字符串
	 * @param eleName 待查询的属性
	 * @return
	 */
	@Override
	public String dataMdToString(String eleName) {
		if(CommonUtil.isNotNull(eleName)){
			eleName = "DATE_FORMAT("+eleName+", '%m-%d')";
		}
		return eleName;
	}

	/**
	 * 数据库中的字段转换成日期类型，格式(yyyy-MM-dd)
	 * 
	 * @param eleName
	 *            查询字段名
	 * @return
	 */
	@Override
	public String dbToDate(String dateStr) {
		if(CommonUtil.isNotNull(dateStr)){
			dateStr = "str_to_date("+dateStr+",\'%Y-%m-%d\')";
		}
		return dateStr;
	}

}
