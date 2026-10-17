package cn.com.adtech.riss2.bso.links;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.sc.SysLink;

public class LinkInfoServiceImp extends CommonServiceImp implements
		ILinkInfoService {

	@Override
	public boolean deleteLinkInfo(Long slinId) {
		try{
			SysLink sysLink = (SysLink)this.getHibernateTemplate().get(SysLink.class, slinId);
			this.getHibernateTemplate().delete(sysLink);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public String getLinkInfoToJson(Pagination pagination, String orderColumn,
			String orderType) {
		String hql = "select slinId,slinName,slinImgpath,slinLinkpath,slinType,slinIndex from SysLink";
		  
		hql += " order by " + orderColumn + " " + orderType;
		List<Object> list = null;
		list = getPageHibernateTemplate().find(pagination,hql);
		//设置json字符串的key值数组
		String[] keys = new String[]{"slinId","slinName","slinImgpath","slinLinkpath","slinType","slinIndex"};
		
		//将对象集合和key值数据转换成json字符串
		String jsonData = CommonUtil.createJsonTable(list, keys, pagination.getTotalResults() + "");
		return jsonData;
	}

	@Override
	public boolean saveLinkInfo(SysLink sysLink) {
		try{
			this.getHibernateTemplate().save(sysLink);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean updateLinkInfo(SysLink sysLink) {
		try{
			this.getHibernateTemplate().update(sysLink);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getSlinIndex() {
		
		String hql = " from SysLink order by slinIndex desc";
		try
		{
			List<SysLink> list = this.getHibernateTemplate().find(hql);
			if(list.size()>0){
				SysLink sysLink = list.get(0);
				
				return sysLink.getSlinIndex()+1;
			}else{
				return 1;
			}
			
		}catch(Exception e){
			return 0;
		}
		
	}

}
