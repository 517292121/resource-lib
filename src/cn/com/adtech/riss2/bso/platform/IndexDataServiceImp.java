package cn.com.adtech.riss2.bso.platform;

import java.util.ArrayList;
import java.util.List;

import cn.com.adtech.riss2.bean.ResourceForTypeBean;
import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.cache.PlatformCacheFactory;
import cn.com.adtech.riss2.core.CommonUtil;

import com.jbetter.framework.session.Pagination;

public class IndexDataServiceImp extends CommonServiceImp implements IIndexDataService{

	@Override
	public List<ResourceForTypeBean> getRcmdResource() {
		List<ResourceForTypeBean> beanList = new ArrayList<ResourceForTypeBean>();
		String where = PlatformBasicService.getSearchCommonConditionByHql("rsda", true);
		
		StringBuffer sb = new StringBuffer();
		sb.append("select rlty.rltyId,");
		sb.append("rlty.rltyName,");
		sb.append("(select count(rsda.rsdaId) from ResSourceData rsda where rlty.rltyId=rsda.rsdaRltyId and rsda.rsdaRcmd=1 and "+where+")");
		sb.append(" from ResLiterType rlty order by rlty.rltyId ASC ");
		String hql = sb.toString();
		sb = null;
		
		List<Object[]> dataList = getHibernateTemplate().find(hql);
		ResourceForTypeBean bean = null;
		String resHql = "select rsda.rsdaId, rsda.rsdaTitle, rsda.rsdaBigimgUrl, rsda.rsdaSourceType, rsda.rsdaFileUrl from ResSourceData rsda where rsda.rsdaRltyId=? and rsda.rsdaRcmd=1 and " + where;
		Pagination p = new Pagination();
		p.setPageResults(10);
		for(Object[] o : dataList){
			if(o == null || o.length < 3 || o[2] == null){
				continue;
			}
			int cmdCount = Integer.parseInt(o[2].toString()); 
			if(cmdCount < 1){
				continue;
			}
			
			List<Object[]> resList = getPageHibernateTemplate().find(p, resHql, o[0]);
			//转换ID
			CommonUtil.parserId(resList);
			bean = new ResourceForTypeBean((Long) o[0], o[1].toString(), resList);
			beanList.add(bean);
		}
		return beanList;
	}
	
	@Override
	public List<Object[]> getIndexResource(String sort, int num) {
		String where = PlatformBasicService.getSearchCommonConditionByHql("rsda", true);
		StringBuffer sb = new StringBuffer();
		sb.append("select rsda.rsdaId,");
		sb.append("rsda.rsdaTitle,");
		sb.append("rsda.rsdaBuilddate,");
		sb.append("rsda.rsdaLooknum");
		
		sb.append(" from ResSourceData rsda");
		sb.append(" where ").append(where);
		sb.append(" order by rsda.").append(sort).append(" DESC");
		String hql = sb.toString();
		sb = null;
		
		Pagination p = new Pagination();
		p.setPageResults(num);
		List<Object[]> dataList = getPageHibernateTemplate().find(p, hql);
		return dataList;
	}

	@Override
	public List<String> getHotSearchKey(int num) {
		String hql = "select rike.rikeName from ResIndexKey rike order by rike.rikeSearchCount DESC";
		Pagination pagination = new Pagination();
		pagination.setPageResults(num);
		List<String> keyList = getPageHibernateTemplate().find(pagination, hql);
		return keyList;
	}
	
	@Override
	public List<Object[]> getInfoList(int num) {
		StringBuffer sb = new StringBuffer();
		sb.append("select ii.idsiId,");
		sb.append("ii.idsiTitle,");
		sb.append("ii.idsiDescription");
		sb.append(" from IdsInformation ii");
		sb.append(" order by ii.idsiDate DESC");
		
		String hql = sb.toString();
		sb = null;
		Pagination p = new Pagination();
		p.setPageResults(num);
		List<Object[]> dataList = getPageHibernateTemplate().find(p, hql);
		return dataList;
	}
}
