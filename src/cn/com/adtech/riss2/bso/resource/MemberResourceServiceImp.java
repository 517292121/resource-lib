package cn.com.adtech.riss2.bso.resource;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.resource.ResLiterType;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResStoreLiter;

public class MemberResourceServiceImp extends CommonServiceImp implements IMemberResourceService{

	@Override
	public List<ResSourceData> getMemberResourceList(long memberId,	String keyWord, int sourceType, Pagination pagination) {
		String hql = "from ResSourceData rsda where rsda.rsdaUserId=" + memberId;
		if(keyWord != null && keyWord.length() > 0){
			hql += " and rsda.rsdaTitle like '%"+keyWord+"%'";
		}
		if(sourceType > 0){
			hql += " and rsda.resLiterType.rltyType=" + sourceType;
		}
		hql += " order by rsda.rsdaBuilddate DESC";
		if(pagination == null){
			List<ResSourceData> list = getHibernateTemplate().find(hql);
			return list;
		}else{
			List<ResSourceData> list = getPageHibernateTemplate().find(pagination, hql);
			return list;
		}
	}

	public List<ResMetaStore> getStoreList() {
		String hql = "from ResMetaStore rmst where rmst.rmstStatus=1 and rmst.rmstIsUploadResource=1";
		List<ResMetaStore> list = getHibernateTemplate().find(hql);
		return list;
	}

	public List<ResLiterType> getLiterListByStore(long rmstId) {
		String hql = "from ResStoreLiter rsli where rsli.rmstId="+rmstId;
		List<ResStoreLiter> rlList = getHibernateTemplate().find(hql);
		if(rlList != null && rlList.size() > 0){
			List<ResLiterType> literList = new LinkedList<ResLiterType>();
			for(int i=0,s=rlList.size(); i<s; i++){
				literList.add(rlList.get(i).getResLiterType());
			}
			return literList;
		}else{
			return null;
		}
	}

	@Override
	public String getListOptionByStore(long rmstId, HttpServletRequest request) {
		List<ResLiterType> list = getLiterListByStore(rmstId);
		if(list == null || list.size() < 1){
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for(int i=0,s=list.size(); i<s; i++){
			buffer.append("<option value='"+list.get(i).getRltyId()+"'>"+list.get(i).getRltyName()+"</option>\n");
		}
		String result = buffer.toString();
		buffer = null;
		
		return result;
	}

	@Override
	public List<Object> getMemberResourceList(Pagination pagination,
			String title, int statu, String orderColumn, String orderType) {
		String hql = "select rsda.rsdaId, rsda.rsdaTitle, rsda.rsdaRltyId, rsda.resLiterType.rltyName, rsda.rsdaBuilddate, rsda.rsdaStatus, suin.suinLoginName from ResSourceData rsda, SysUserinfo suin where rsda.rsdaUserId=suin.suinId ";
		String where = "";
		if(title != null && title.length() > 0){
			where += "and rsda.rsdaTitle like '%"+title+"%'";
		}
		if(statu > 0){
			where += "and rsda.rsdaStatus=" + statu;
		}
		hql += where + " order by rsda." + orderColumn + " " + orderType;
		
		String countHql = "select count(rsda.rsdaId) from ResSourceData rsda, SysUserinfo suin where rsda.rsdaUserId=suin.suinId " + where;
		List<Object> list = getPageHibernateTemplate().find(pagination,hql,countHql,null);
		return list;
	}

}
