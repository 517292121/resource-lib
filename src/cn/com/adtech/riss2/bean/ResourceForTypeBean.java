package cn.com.adtech.riss2.bean;

import java.util.List;

/**
 * 
 * 根据资源类型存储资源
 *
 * @author MaChao
 *
 * @date：Jan 5, 2018
 */
public class ResourceForTypeBean {

	private Long rltyId; //资源类型ID
	private String rltyName; //资源类型名称
	private List<Object[]> resList; //资源列表
	
	public ResourceForTypeBean(Long id, String name, List<Object[]> list){
		this.rltyId = id;
		this.rltyName = name;
		this.resList = list;
	}
	
	
	public Long getRltyId() {
		return rltyId;
	}
	public void setRltyId(Long rltyId) {
		this.rltyId = rltyId;
	}
	public String getRltyName() {
		return rltyName;
	}
	public void setRltyName(String rltyName) {
		this.rltyName = rltyName;
	}
	public List<Object[]> getResList() {
		return resList;
	}
	public void setResList(List<Object[]> resList) {
		this.resList = resList;
	}
}
