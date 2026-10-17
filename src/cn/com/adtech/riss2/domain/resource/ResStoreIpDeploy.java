package cn.com.adtech.riss2.domain.resource;

/**
 * 特色库IP配置
 * @author MaChao
 * @time 2011-03-24
 */

public class ResStoreIpDeploy implements java.io.Serializable {

	private Long rsidId;	// ID
	private Long rsidRmstId;	// 特色库ID
	private String rsidIpPart;	// 特色库IP段
	
	public ResStoreIpDeploy() {
		super();
	}
	
	public ResStoreIpDeploy(Long rsidId, Long rsidRmstId, String rsidIpPart) {
		super();
		this.rsidId = rsidId;
		this.rsidRmstId = rsidRmstId;
		this.rsidIpPart = rsidIpPart;
	}
	
	public Long getRsidId() {
		return rsidId;
	}
	public void setRsidId(Long rsidId) {
		this.rsidId = rsidId;
	}
	public Long getRsidRmstId() {
		return rsidRmstId;
	}
	public void setRsidRmstId(Long rsidRmstId) {
		this.rsidRmstId = rsidRmstId;
	}
	public String getRsidIpPart() {
		return rsidIpPart;
	}
	public void setRsidIpPart(String rsidIpPart) {
		this.rsidIpPart = rsidIpPart;
	}
	
}
