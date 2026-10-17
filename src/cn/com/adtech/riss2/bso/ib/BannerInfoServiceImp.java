package cn.com.adtech.riss2.bso.ib;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.ib.IndexBanner;
import cn.com.adtech.riss2.domain.sc.SysLink;

public class BannerInfoServiceImp extends CommonServiceImp implements
		IBannerInfoService {

	@Override
	public boolean deleteLinkInfo(IndexBanner ib) {
		try{
			this.getHibernateTemplate().delete(ib);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public String getLinkInfoToJson(Pagination pagination, String orderColumn,
			String orderType) {
		String hql = "select ibId,ibName,ibImgUrl,ibUrl,ibIndex,ibIsShow from IndexBanner";
		  
		hql += " order by " + orderColumn + " " + orderType;
		List<Object> list = null;
		list = getPageHibernateTemplate().find(pagination,hql);
		//设置json字符串的key值数组
		String[] keys = new String[]{"ibId","ibName","ibImgUrl","ibUrl","ibIndex","ibIsShow"};
		
		//将对象集合和key值数据转换成json字符串
		String jsonData = CommonUtil.createJsonTable(list, keys, pagination.getTotalResults() + "");
		return jsonData;
	}

	@Override
	public boolean saveLinkInfo(IndexBanner ib) {
		try{
			this.getHibernateTemplate().save(ib);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean updateLinkInfo(IndexBanner ib) {
		try{
			this.getHibernateTemplate().update(ib);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getSlinIndex() {
		
		String hql = " from IndexBanner order by ibIndex desc";
		try
		{ 
			List<IndexBanner> list = this.getHibernateTemplate().find(hql);
			if(list.size()>0){
				IndexBanner ib = list.get(0);
				return ib.getIbIndex()+1;
			}else{
				return 1;
			}
			
		}catch(Exception e){
			return 0;
		}
		
	}

}
