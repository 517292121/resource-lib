package cn.com.adtech.riss2.core.json;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import cn.com.adtech.riss2.exception.ErrorMessageException;

import com.jbetter.framework.session.Pagination;

public class JsonUtil {
	
	//日期格式化配置
	private static JsonConfig ymdConfig = null;
	//日期时间格式化配置
	private static JsonConfig ymdhmConfig = null;
	
	public static String pageListToJson(Pagination p, List<Object[]> dataList, String[] keyArr, JsonConfig config){
		PagedataBean pageBean = new PagedataBean();
		if(p != null){
			pageBean.setCurrtPage(p.getCurrentPage());
			pageBean.setTotalRows(p.getTotalResults());
			pageBean.setTotalPage(p.getTotalPages());
			pageBean.setPageResults(p.getPageResults());
		}
		pageBean.setSuccess(true);
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		if(dataList == null || dataList.size() < 1 || dataList.get(0) == null){
			pageBean.setDataList(mapList);
			String json = JSONObject.fromObject(pageBean).toString();
			return json;
		}
		
		if(dataList.get(0).length != keyArr.length){
			throw new ErrorMessageException("数据对象中的列数量与需要构造的JSON报文KEY数量不相等！");
		}	
		
		Map<String, Object> map = null;
		for(Object[] row : dataList){
			map = new HashMap<String, Object>();
			for(int i=0,s=row.length; i<s; i++){
				map.put(keyArr[i], row[i]);
			}
			mapList.add(map);
		}
		pageBean.setDataList(mapList);
		
		String json = null;
		if(config == null){
			json = JSONObject.fromObject(pageBean).toString();
		}else{
			json = JSONObject.fromObject(pageBean, config).toString();
		}
		return json;
	} 
	
	/**
	 * 发生异常后错误信息JSON转换
	 * @param msg
	 * @return
	 */
	public static String parseErrorJson(String msg){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", false);
		map.put("msg", "错误：" + msg);
		
		String json = JSONObject.fromObject(map).toString();
		map = null;
		return json;
	}
	
	public static JsonConfig getYmdConfig(){
		if(ymdConfig == null){
			ymdConfig = new JsonConfig();
		}
		ymdConfig.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd"));
		return ymdConfig;
	}
	
	public static JsonConfig getYmdHmConfig(){
		if(ymdhmConfig == null){
			ymdhmConfig = new JsonConfig();
		}
		ymdhmConfig.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm"));
		return ymdhmConfig;
	}
}
