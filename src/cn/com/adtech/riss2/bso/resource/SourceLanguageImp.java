package cn.com.adtech.riss2.bso.resource;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResLanguageType;
import cn.com.adtech.riss2.domain.resource.ResSourceLanguage;

public class SourceLanguageImp extends CommonServiceImp implements ISourceLanguageService {

	public Serializable saveSourceLanguage(ResSourceLanguage language) {
		Serializable s = getHibernateTemplate().save(language);
		
		return s;
	}

	public Serializable saveSourceLanguage(HttpServletRequest request, List<ResElementPool> elementList) {
		long sourceid = MyRequestUtil.getLongParameter(request, "sourceId");
		long languageId = MyRequestUtil.getLongParameter(request, "language_id");
		
		ResSourceLanguage rsl = new ResSourceLanguage();
		rsl.setResLanguageId(languageId);
		rsl.setResSourceId(sourceid);
		rsl.setRslaDate(new Date().toLocaleString());
		rsl.setRslaStatus(2L);
		rsl.setRslaAuthorName(MyRequestUtil.getSmanName(request));
		rsl.setRslaUserId(MyRequestUtil.getLoginUserId(request));
		rsl.setRslaUsername(MyRequestUtil.getLoginUserName(request));
		rsl.setRslaIsfirst(false);
		
		Serializable sid = saveSourceLanguage(rsl);
		
		String eleid = "", dataValue = "" , sql = "";
		long myid;
		List<String> sqlList = new LinkedList<String>();
		// 循环保存所有元素值到数据库
		for(int i = 0, s = elementList.size(); i < s; i++) {
			eleid = elementList.get(i).getRepoId().toString();
			myid = System.currentTimeMillis() + i;
			dataValue = MyRequestUtil.getStringParameter(request, eleid);
			sql = "insert into RES_EXTEND_DATA(REDA_ID,REDA_REPO_ID,REDA_RSDA_ID,REDA_DATA,REDA_LANGUAGE) values(" + myid + "," + eleid
					+ "," + sourceid + ",'" + dataValue.replace("'", "''") + "'," + languageId + ")";
			sqlList.add(sql);
		}
		executeMultiSql(sqlList);
		return sid;
	}

	public String getSourceLanguageOption(long sourceId, int haveType,
			long selectid) {
		List<ResLanguageType> lanList = null;
		if(haveType == 3){
			lanList = getSourceUnLanguageAndSelf(sourceId,selectid);
		}else if(haveType == 1){
			lanList = getSourceOnLanguage(sourceId);
		}else if (haveType == 4){
			lanList = getSourceOnLanguageByPass(sourceId);
		}else{
			lanList = getSourceUnLanguage(sourceId);
		}
		if(lanList == null || lanList.size() < 1){
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		ResLanguageType lan = null;
		for(int i=0,s=lanList.size(); i<s; i++){
			lan = lanList.get(i);
			buffer.append("<option value='"+lan.getRltyId()+"' ");
			if(lan.getRltyId() == selectid){
				buffer.append(" selected=true ");
			}
			buffer.append(">");
			buffer.append(lan.getRltyName());
			buffer.append("</option>\n");
		}
		String options = buffer.toString();
		buffer = null;
		
		return options;
	}

	public List<ResLanguageType> getSourceOnLanguage(long sourceId) {
		String hql = "from ResLanguageType rlty where rlty.rltyId in (select resLanguageId from ResSourceLanguage where resSourceId="+sourceId+")";
		List<ResLanguageType> lanList = getHibernateTemplate().find(hql);
		return lanList;
	}

	public List<ResLanguageType> getSourceUnLanguage(long sourceId) {
		String hql = "from ResLanguageType rlty where rlty.rltyId not in (select resLanguageId from ResSourceLanguage where resSourceId="+sourceId+")";
		List<ResLanguageType> lanList = getHibernateTemplate().find(hql);
		return lanList;
	}
	
	public List<ResLanguageType> getSourceUnLanguageAndSelf(long sourceId, long languageId) {
		String hql = "from ResLanguageType rlty where rlty.rltyId not in (select resLanguageId from ResSourceLanguage where resSourceId="+sourceId+" and resLanguageId!="+languageId+")";
		List<ResLanguageType> lanList = getHibernateTemplate().find(hql);
		return lanList;
	}
	
	/**
	 * 获取资源已审核通过的语种
	 * @return
	 */
	public List<ResLanguageType> getSourceOnLanguageByPass(long sourceId){
		String hql = "from ResLanguageType rlty where rlty.rltyId in (select resLanguageId from ResSourceLanguage where resSourceId="+sourceId+" and rslaStatus=3)";
		List<ResLanguageType> lanList = getHibernateTemplate().find(hql);
		return lanList;
	}

	public long getSourceFirstLanguage(long sourceId) {
		String hql = "select resLanguageId from ResSourceLanguage where rslaIsfirst=true and resSourceId="+sourceId;
		List list = getHibernateTemplate().find(hql);
		if(list == null || list.size() < 1){
			return 0;
		}else{
			return Long.parseLong(list.get(0).toString());
		}
	}

	public String getSourceLanguageListJson(long sourceId, int begin, int limit, String wheres) {
		String sql = "select rsl.*,rlt.RLTY_NAME from RES_SOURCE_LANGUAGE rsl, RES_LANGUAGE_TYPE rlt where rsl.RSLA_RLTY_ID=rlt.RLTY_ID and rsl.RSLA_RSDA_ID=" + sourceId + wheres;
		String jsondata = getListToJson(sql, null, begin, limit);
		return jsondata;
	}

	@Override
	public ResLanguageType getLanguage(long languageId) {
		ResLanguageType l = (ResLanguageType) getHibernateTemplate().get(ResLanguageType.class, languageId);
		return l;
	}

	public boolean updateSourceLanguage(long sourceId, long preLanguage,
			long nowLanguage) {
		String sql = "update RES_SOURCE_LANGUAGE set RSLA_RLTY_ID=" + nowLanguage + " where RSLA_RSDA_ID=" + sourceId + " and RSLA_RLTY_ID=" + preLanguage;
		
		boolean b = execute(sql);
		
		return b;
	}

	public long getNextResourceId(long rminId, long languageId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select rsd.RSDA_ID from RES_SOURCE_DATA rsd left join RES_SOURCE_LANGUAGE rsl on rsd.RSDA_ID = rsl.RSLA_RSDA_ID and rsl.RSLA_RLTY_ID="
						+ languageId);
		buffer.append(" where rsd.RSDA_ISUSE=1 and rsd.RSDA_RMIN_ID=" + rminId);
		buffer.append(" and rsl.rsla_status is null");
		buffer.append(" order by rsl.RSLA_DATE desc");
		String sql = buffer.toString();
		buffer = null;
		
		String rsdaId = getFirstData(sql);
		try {
			return Long.parseLong(rsdaId);
		} catch (Exception e) {
			return 0;
		}
	}

}
