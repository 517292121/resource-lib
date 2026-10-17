package cn.com.adtech.riss2.domain.resource;

/**
 * @author MaChao
 * @date 2010-8-31
 */
@SuppressWarnings("serial")
public class ResStoreLiter implements java.io.Serializable {
	// Fields
	private Long rsliId;

	private ResMetaStore resMetaStore;
	private Long rmstId;

	private ResLiterType resLiterType;
	private Long rltyId;

	// Constructors
	public ResStoreLiter() {}
	
	public ResStoreLiter(Long rsliId, ResMetaStore resMetaStore, Long rmstId, ResLiterType resLiterType, Long rltyId) {
		super();
		this.rsliId = rsliId;
		this.resMetaStore = resMetaStore;
		this.rmstId = rmstId;
		this.resLiterType = resLiterType;
		this.rltyId = rltyId;
	}

	public Long getRsliId() {
		return rsliId;
	}

	public void setRsliId(Long rsliId) {
		this.rsliId = rsliId;
	}

	public ResMetaStore getResMetaStore() {
		return resMetaStore;
	}

	public void setResMetaStore(ResMetaStore resMetaStore) {
		this.resMetaStore = resMetaStore;
	}

	public Long getRmstId() {
		return rmstId;
	}

	public void setRmstId(Long rmstId) {
		this.rmstId = rmstId;
	}

	public ResLiterType getResLiterType() {
		return resLiterType;
	}

	public void setResLiterType(ResLiterType resLiterType) {
		this.resLiterType = resLiterType;
	}

	public Long getRltyId() {
		return rltyId;
	}

	public void setRltyId(Long rltyId) {
		this.rltyId = rltyId;
	}
	
}
