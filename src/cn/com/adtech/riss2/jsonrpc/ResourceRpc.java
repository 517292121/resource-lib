package cn.com.adtech.riss2.jsonrpc;

import java.io.Serializable;
import java.util.List;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.jdbcms.ToolUnit;

public class ResourceRpc extends CommonServiceImp implements IResourceRpc, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final private static String splitStr = ConstantsObj.ELEMENT_SPLIT;
	final private static String dataSplit = ConstantsObj.ELEMENT_DATA_SPLIT;
	
	/**
	 * 检查中图法是否存在
	 * @param ZTFname
	 * @return 如果存在，返回true 反之亦然
	 */
	public boolean checkZTFcode(String ZTFcode){
		ZTFcode = ZTFcode.toUpperCase();
		String sql = "select count(BZTY_ID) from RES_MIDDLE_TYPE where BZTY_CODE='"+ZTFcode+"'";
		long count = ToolUnit.StrToLong(getFirstData(sql), 0);
		return count > 0 ? true: false;
	}
	
	public String getSourceInfo(long sourceId, long languageId, long store_id, long stype){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select ele.REPO_NAME,ele.REPO_TITLE,da.REDA_DATA,ele.REPO_ID,ele.REPO_PARENT ");
		buffer.append("from RES_ELEMENT_POOL ele, RES_EXTEND_DATA da ");
		buffer.append(" where da.REDA_REPO_ID=ele.REPO_ID");
		buffer.append(" and da.REDA_RSDA_ID=" + sourceId);
		buffer.append(" and da.REDA_LANGUAGE=" + languageId);
		buffer.append(" and ele.repo_id in (select RTME_REEL_ID from RES_TEMPLET_META where RTME_RSIN_ID="+store_id+" and RTME_RLTY_ID="+stype+")");
		buffer.append(" order by ele.REPO_ID");
		String sql = buffer.toString();
		buffer = null;
		
		List<String[]> datalist =  getStrList(sql, null);
		if(datalist == null || datalist.size()<1){
			return "";
		}
		String[] dataArr = null;
		buffer = new StringBuffer();
		for(int i=0,s=datalist.size(); i<s; i++){
			dataArr = datalist.get(i);
			buffer.append( "data_" + ToolUnit.StrToLong(dataArr[3], 0));
			buffer.append(dataSplit);
			buffer.append(ToolUnit.stringToJson(dataArr[2]));
			buffer.append(splitStr);
		}
		
		String result = buffer.toString();
		buffer = null;
		
		return result;
	}

	/**
	 * 根据提名检验资源是否存在。如果存在则返回该资源的ID ，否则返回 0
	 * @param title
	 * @param selfId
	 * @return
	 */
	public long checkResourceTitle(String title, long selfId) {
		String hql = "select rsdaId from ResSourceData where rsdaTitle=?";
		List<Long> list = getHibernateTemplate().find(hql,title);
		if(list == null || list.size() < 1){
			return 0;
		}
		for(int i=0, s=list.size(); i<s; i++){
			if(list.get(i) != selfId){
				return list.get(i);
			}
		}
		return 0;
	}

}
