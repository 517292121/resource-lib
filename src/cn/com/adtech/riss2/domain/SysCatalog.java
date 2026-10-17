package cn.com.adtech.riss2.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** @author MaChao */
@SuppressWarnings( {"serial", "unchecked"})
public class SysCatalog implements Serializable, Comparable {
	/** identifier field */
	private Long scatId;

	/** persistent field */
	private String scatName;

	/** nullable persistent field */
	private String scatDesc;

	/** persistent field */
	private Short scatCode;

	/** nullable persistent field */
	private SysCatalog parentCatalog;

	/** 不由Hibernate自动加载，而是由程序来填写，在根据用户角色集合获得用户有权限操作的菜单时使用 */
	private List sysFunctions = new ArrayList();

	/** 不由Hibernate自动加载，而是由程序来填写,在根据用户角色集合获得用户有权限操作的菜单时使用 */
	private List childrenCatalogs = new ArrayList();

	/** default constructor */
	public SysCatalog() {}

	/** minimal constructor */
	public SysCatalog(Long scatId, String scatName, Short scatCode, List sysFunctions) {
		this.scatId = scatId;
		this.scatName = scatName;
		this.scatCode = scatCode;
		this.sysFunctions = sysFunctions;
	}

	public Long getScatId() {
		return this.scatId;
	}

	public void setScatId(Long scatId) {
		this.scatId = scatId;
	}

	public String getScatName() {
		return this.scatName;
	}

	public void setScatName(String scatName) {
		this.scatName = scatName;
	}

	public String getScatDesc() {
		return this.scatDesc;
	}

	public void setScatDesc(String scatDesc) {
		this.scatDesc = scatDesc;
	}

	public SysCatalog getParentCatalog() {
		return this.parentCatalog;
	}

	public void setParentCatalog(SysCatalog sysCatalog) {
		this.parentCatalog = sysCatalog;
	}

	public List getSysFunctions() {
		return this.sysFunctions;
	}

	public void setSysFunctions(List sysFunctions) {
		this.sysFunctions = sysFunctions;
	}

	public String toString() {
		return this.getScatId() + "";
	}

	public Short getScatCode() {
		return scatCode;
	}

	public void setScatCode(Short scatCode) {
		this.scatCode = scatCode;
	}

	public List getChildrenCatalogs() {
		return childrenCatalogs;
	}

	public void setChildrenCatalogs(List childrenCatalogs) {
		this.childrenCatalogs = childrenCatalogs;
	}

	public boolean equals(Object obj) {
		if(obj instanceof SysCatalog) {
			return this.getScatId().longValue() == ((SysCatalog)obj).getScatId().longValue();
		}
		return false;
	}

	public int compareTo(Object o) {
		SysCatalog that = (SysCatalog)o;
		if(that.getScatCode().intValue() <= this.getScatCode().intValue())
			return 1;
		return 0;
	}
}
