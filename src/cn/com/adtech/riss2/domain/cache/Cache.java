package cn.com.adtech.riss2.domain.cache;

/**
 * 缓存类
 * @author MaChao
 * @date 2017-3-28
 */
public class Cache {
	//父id
	private Long pid;
	//中文名称
	private String name;
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
