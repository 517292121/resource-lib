package cn.com.adtech.riss2.calisSchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 元素实体
 * @author MaChao
 */
public class MetaDataEntity {
	/**
	 * 元素名称
	 */
	private String name;
	
	/**
	 * 元素值
	 */
	private String value;
	
	/**
	 * 子集元素
	 */
	private List<MetaDataEntity> list = new ArrayList<MetaDataEntity>();
	
	/**
	 * 元素属性
	 */
	private Map<String, String> map = new HashMap<String, String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<MetaDataEntity> getList() {
		return list;
	}

	public void setList(List<MetaDataEntity> list) {
		this.list = list;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	
	
}
