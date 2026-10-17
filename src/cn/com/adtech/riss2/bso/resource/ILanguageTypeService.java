package cn.com.adtech.riss2.bso.resource;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.resource.ResLanguageType;

public interface ILanguageTypeService extends ICommonService {

	/**
	 * 获取语种options下拉选项
	 * @param selectid 选中的ID
	 * @return
	 */
	public String getLanguageSelectOption(long selectid);
	
	public String getLanguageSelectOptionByExt();
	
	public ResLanguageType getResLanguage(long lanId);
}
