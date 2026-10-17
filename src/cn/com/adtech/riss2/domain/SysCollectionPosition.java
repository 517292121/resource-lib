package cn.com.adtech.riss2.domain;

/**
 * 系统馆藏位置实体类
 * @author MaChao
 * 2011-04-21
 */

public class SysCollectionPosition implements java.io.Serializable {

	private Long scpId;
	private String scpName;
	private Integer scpIndex;
	
	public SysCollectionPosition() {
		super();
	}

	public SysCollectionPosition(Long scpId, String scpName, Integer scpIndex) {
		super();
		this.scpId = scpId;
		this.scpName = scpName;
		this.scpIndex = scpIndex;
	}

	public Long getScpId() {
		return scpId;
	}

	public void setScpId(Long scpId) {
		this.scpId = scpId;
	}

	public String getScpName() {
		return scpName;
	}

	public void setScpName(String scpName) {
		this.scpName = scpName;
	}

	public Integer getScpIndex() {
		return scpIndex;
	}

	public void setScpIndex(Integer scpIndex) {
		this.scpIndex = scpIndex;
	}

}
