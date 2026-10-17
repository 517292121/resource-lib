package cn.com.adtech.riss2.jsonrpc;

import cn.com.adtech.riss2.bso.ICommonService;

public interface IMetadataTemletService extends ICommonService{
	public boolean addMetaElement(long store, long stype, long eleid);
	
	public boolean updateSingleValue(String colname,String data, long id);
	
	public boolean DeleteElement(long eleid, long parentid);
	
	public boolean checkElementName(String eleName, long sourceTypeId);
}
