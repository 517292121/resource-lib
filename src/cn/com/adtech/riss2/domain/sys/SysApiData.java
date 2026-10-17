package cn.com.adtech.riss2.domain.sys;

import java.util.Date;

/**
 * SysApiData entity. 
 * @author MaChao
 * @date 2019.11.1
 */

public class SysApiData implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long sadaId;
	private String sadaType;	//取值：通用接口、定制接口、其它接口
	private String sadaName;
	private String sadaReqUrl;
	private String sadaDemoUrl;
	private String sadaReqType;
	private String sadaReqParams;
	private String sadaResult;
	private String sadaRemark;
	private Integer sadaIndex;
	private Date sadaLastUpdate;
	private String sadaUpdateRemark;

	/** default constructor */
	public SysApiData() {
	}

	public Long getSadaId() {
		return this.sadaId;
	}

	public void setSadaId(Long sadaId) {
		this.sadaId = sadaId;
	}

	public String getSadaType() {
		return this.sadaType;
	}

	public void setSadaType(String sadaType) {
		this.sadaType = sadaType;
	}

	public String getSadaName() {
		return this.sadaName;
	}

	public void setSadaName(String sadaName) {
		this.sadaName = sadaName;
	}

	public String getSadaReqUrl() {
		return this.sadaReqUrl;
	}

	public void setSadaReqUrl(String sadaReqUrl) {
		this.sadaReqUrl = sadaReqUrl;
	}

	public String getSadaDemoUrl() {
		return this.sadaDemoUrl;
	}

	public void setSadaDemoUrl(String sadaDemoUrl) {
		this.sadaDemoUrl = sadaDemoUrl;
	}

	public String getSadaReqType() {
		return this.sadaReqType;
	}

	public void setSadaReqType(String sadaReqType) {
		this.sadaReqType = sadaReqType;
	}

	public String getSadaReqParams() {
		return this.sadaReqParams;
	}

	public void setSadaReqParams(String sadaReqParams) {
		this.sadaReqParams = sadaReqParams;
	}

	public String getSadaResult() {
		return this.sadaResult;
	}

	public void setSadaResult(String sadaResult) {
		this.sadaResult = sadaResult;
	}

	public String getSadaRemark() {
		return this.sadaRemark;
	}

	public void setSadaRemark(String sadaRemark) {
		this.sadaRemark = sadaRemark;
	}

	public Integer getSadaIndex() {
		return this.sadaIndex;
	}

	public void setSadaIndex(Integer sadaIndex) {
		this.sadaIndex = sadaIndex;
	}

	public Date getSadaLastUpdate() {
		return this.sadaLastUpdate;
	}

	public void setSadaLastUpdate(Date sadaLastUpdate) {
		this.sadaLastUpdate = sadaLastUpdate;
	}

	public String getSadaUpdateRemark() {
		return this.sadaUpdateRemark;
	}

	public void setSadaUpdateRemark(String sadaUpdateRemark) {
		this.sadaUpdateRemark = sadaUpdateRemark;
	}

}
