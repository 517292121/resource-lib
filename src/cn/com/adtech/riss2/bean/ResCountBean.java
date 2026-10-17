package cn.com.adtech.riss2.bean;

/**
 * 
 * 资源统计存储对象
 *
 * @author MaChao
 *
 * @date：Jan 5, 2018
 */
public class ResCountBean {
	
	private Long id;

	//统计对象名称
	private String groupName;
	
	//资源总数
	private int resCount;
	
	//资源占比百分比
	private String resPercent;
	
	public ResCountBean(Long id, String gname, int count, String percent){
		this.id = id;
		this.groupName = gname;
		this.resCount = count;
		this.resPercent = percent;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getResCount() {
		return resCount;
	}

	public void setResCount(int resCount) {
		this.resCount = resCount;
	}

	public String getResPercent() {
		return resPercent;
	}

	public void setResPercent(String resPercent) {
		this.resPercent = resPercent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
