package cn.com.adtech.riss2.domain.cache;

import java.util.List;

/**
 * 树
 * ClassName: Tree 
 * @Description: TODO
 * @author MaChao
 * @date 2017-3-29
 */
public class Tree {
	private Long id;
	
	private String name;
	
	private Long pid;
	
	private Integer count;
	
	private List<Tree> childrenList;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public List<Tree> getChildrenList() {
		return childrenList;
	}
	public void setChildrenList(List<Tree> childrenList) {
		this.childrenList = childrenList;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	
}
