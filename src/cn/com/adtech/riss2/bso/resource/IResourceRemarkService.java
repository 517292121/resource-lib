package cn.com.adtech.riss2.bso.resource;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.resource.ResSourceRemark;

public interface IResourceRemarkService extends ICommonService{

	public long saveResourcRemark(ResSourceRemark sourceRemark);
	
	public long saveResourceRemark(long rsdaId, String remark);
}
