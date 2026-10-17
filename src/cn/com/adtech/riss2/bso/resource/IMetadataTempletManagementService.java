package cn.com.adtech.riss2.bso.resource;


public interface IMetadataTempletManagementService {

	public String getSourceTypeList(long user_id, long store_id, long stype_id, int begin, int pageNum);
	
	public String getSourceTypeByStore(long store_id);
	
	/**
	 * 更新资源录入模板中的元素
	 * @param store_id 特色库ID
	 * @param sourceType  资源类型ID
	 * @param EleIdArr 元素ID数据
	 * @return
	 */
	public boolean updateTempletElements(long store_id, long sourceType, String[] EleIdArr);
	
	/**
	 * 获取模板中的拥有元素名称
	 * @return
	 */
	public String[] getTempletAllElementName(Long storeId, Long sourceTypeId);
}
