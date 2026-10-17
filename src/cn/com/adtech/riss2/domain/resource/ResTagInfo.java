package cn.com.adtech.riss2.domain.resource;

import java.io.Serializable;
import java.util.Date;

import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysUserinfo;

/**
 * 资源Tag信息表 RES_TAG_INFO
 * 
 * @author MaChao
 * @date 2010-9-13
 */
public class ResTagInfo implements Serializable {
	/** RTIN_ID */
	private Long rtinId;

	/** 资源 */
	private ResSourceData resSourceData;

	/** 资源id */
	private Long rtinRsdaId;

	/** 创建人ID */
	private Long rtinSuinId;

	private SysUserinfo sysUserinfo;

	/** tag标题 */
	private String rtinTitle;

	/** 创建时间 */
	private Date rtinDate;

	/** 点击次数 */
	private Integer rtinHitNum = 0;

	public ResTagInfo() {
		super();
	}

	public Long getRtinId() {
		return rtinId;
	}

	public void setRtinId(Long rtinId) {
		this.rtinId = rtinId;
	}

	public ResSourceData getResSourceData() {
		return resSourceData;
	}

	public void setResSourceData(ResSourceData resSourceData) {
		this.resSourceData = resSourceData;
	}

	public Long getRtinRsdaId() {
		return rtinRsdaId;
	}

	public void setRtinRsdaId(Long rtinRsdaId) {
		this.rtinRsdaId = rtinRsdaId;
	}

	public Long getRtinSuinId() {
		return rtinSuinId;
	}

	public void setRtinSuinId(Long rtinSuinId) {
		this.rtinSuinId = rtinSuinId;
	}

	public SysUserinfo getSysUserinfo() {
		return sysUserinfo;
	}

	public void setSysUserinfo(SysUserinfo sysUserinfo) {
		this.sysUserinfo = sysUserinfo;
	}

	public String getRtinTitle() {
		return rtinTitle;
	}

	public void setRtinTitle(String rtinTitle) {
		this.rtinTitle = rtinTitle;
	}

	public Date getRtinDate() {
		return rtinDate;
	}

	public void setRtinDate(Date rtinDate) {
		this.rtinDate = rtinDate;
	}

	public Integer getRtinHitNum() {
		return rtinHitNum;
	}

	public void setRtinHitNum(Integer rtinHitNum) {
		this.rtinHitNum = rtinHitNum;
	}
	
	public String getSubRtinTitle(){
		String title = "";
		if(this.rtinTitle.trim().length() <= ConstantsObj.TAG_LENGTH){
			title = this.rtinTitle;
		}else{
			title = ServiceUtil.subString(this.rtinTitle, ConstantsObj.TAG_LENGTH, "");
		}
		return title;
	}
}
