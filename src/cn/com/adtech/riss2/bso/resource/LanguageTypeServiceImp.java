package cn.com.adtech.riss2.bso.resource;

import java.util.List;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResLanguageType;

public class LanguageTypeServiceImp extends CommonServiceImp implements ILanguageTypeService{

	
	/**
	 * 获取语种options下拉选项
	 * @param selectid 选中的ID
	 * @return
	 */
	public String getLanguageSelectOption(long selectid){
		StringBuffer buffer = new StringBuffer();
		String sql = "select rlty_id, rlty_name from res_language_type order by rlty_id";
		List<String[]> list = getStrList(sql, null);
		String[] arr = null;
		for(int i=0,s=list.size(); i<s; i++){
			arr = list.get(i);
			buffer.append("<option value='"+arr[0]+"' ");
			if(selectid == Long.parseLong(arr[0])){
				buffer.append(" selected=true ");
			}
			buffer.append(">" + arr[1] + "</option>\n");
		}
		
		arr = null;
		list = null;
		String options = buffer.toString();
		
		buffer = null;
		return options;
	}
	
	public String getLanguageSelectOptionByExt(){
		String hql = "select rlt.rltyId, rlt.rltyName from ResLanguageType rlt";
		List<Object> list = getHibernateTemplate().find(hql);
		String jsondata = CommonUtil.createOptionsList(list);
		return jsondata;
	}
	
	public ResLanguageType getResLanguage(long lanId){
		ResLanguageType language = (ResLanguageType) getHibernateTemplate().get(ResLanguageType.class, lanId);
		return language;
	}
}
