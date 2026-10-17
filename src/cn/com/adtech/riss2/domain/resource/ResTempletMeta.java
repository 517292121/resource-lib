package cn.com.adtech.riss2.domain.resource;

/**
 * 元数据模板元素
 * 
 * @author MaChao
 * @date 2010-9-3
 */
@SuppressWarnings("serial")
public class ResTempletMeta implements java.io.Serializable {
	// Fields
	private Long rtmeId;

	private ResElementPool resElementPool;

	private Long rtmeRsinId;

	private ResMetaStore resMetaStore;

	private Long rtmeRltyId;

	private ResLiterType resLiterType;

	private Long rtmeRepoId;

	public ResMetaStore getResMetaStore() {
		return resMetaStore;
	}

	public void setResMetaStore(ResMetaStore resMetaStore) {
		this.resMetaStore = resMetaStore;
	}

	public ResLiterType getResLiterType() {
		return resLiterType;
	}

	public void setResLiterType(ResLiterType resLiterType) {
		this.resLiterType = resLiterType;
	}

	public Long getRtmeRepoId() {
		return rtmeRepoId;
	}

	public void setRtmeRepoId(Long rtmeRepoId) {
		this.rtmeRepoId = rtmeRepoId;
	}

	// Constructors
	/** default constructor */
	public ResTempletMeta() {}

	// Property accessors
	public Long getRtmeId() {
		return this.rtmeId;
	}

	public void setRtmeId(Long rtmeId) {
		this.rtmeId = rtmeId;
	}

	public ResElementPool getResElementPool() {
		return this.resElementPool;
	}

	public void setResElementPool(ResElementPool resElementPool) {
		this.resElementPool = resElementPool;
	}

	public Long getRtmeRsinId() {
		return this.rtmeRsinId;
	}

	public void setRtmeRsinId(Long rtmeRsinId) {
		this.rtmeRsinId = rtmeRsinId;
	}

	public Long getRtmeRltyId() {
		return this.rtmeRltyId;
	}

	public void setRtmeRltyId(Long rtmeRltyId) {
		this.rtmeRltyId = rtmeRltyId;
	}
}
