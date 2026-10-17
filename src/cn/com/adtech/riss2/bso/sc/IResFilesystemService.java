package cn.com.adtech.riss2.bso.sc;

import java.io.Serializable;
import java.util.List;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.ResFilesystem;

public interface IResFilesystemService extends ICommonService {

	public Serializable saveFilesystem(ResFilesystem filesystem);

	public boolean updateFilesystem(ResFilesystem filesystem);
	
	public boolean deleteResFilesystem(long id);
	
	public ResFilesystem getFilesystem(Long id);
	
	public String getFilesystemJson(int begin, int pageNum, String orderColumn, String orderType);
	
	public String getAllFileServiceOption(Long selectid);
	
	public String getAllFileServiceOptionJson();
	
}
