package cn.com.adtech.riss2.jsonrpc;

import cn.com.adtech.riss2.bso.ICommonService;

/**
 * 资源的ajax相关操作
 * @author MaChao
 * @date: 2010-11-4
 */
public interface IResourceRpc extends ICommonService {

	/**
	 * 检查中图法是否存在
	 * @param ZTFname
	 * @return 如果存在，返回true 反之亦然
	 */
	public boolean checkZTFcode(String ZTFcode);
	
	/**
	 * 根据提名检验资源是否存在。如果存在则返回该资源的ID ，否则返回 0
	 * @param title
	 * @param selfId
	 * @return
	 */
	public long checkResourceTitle(String title, long selfId);
	
	public String getSourceInfo(long sourceId, long languageId, long store_id, long stype);
}
