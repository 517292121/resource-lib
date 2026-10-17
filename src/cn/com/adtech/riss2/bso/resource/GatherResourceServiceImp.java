package cn.com.adtech.riss2.bso.resource;

import java.util.ArrayList;
import java.util.List;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResSourceData;

import com.jbetter.framework.session.Pagination;

/**
 * 抓取数据审核处理
 * 
 * @author MaChao
 * 
 * @date
 *
 */
public class GatherResourceServiceImp extends CommonServiceImp implements IGatherResourceService {

	/**
	 * 获取抓取数据资源列表
	 * @param pagination
	 * @param title
	 * @param statu
	 * @param orderColumn
	 * @param orderType
	 * @return
	 */
	@Override
	public List getGatherResourceList(Pagination pagination, String title,
			int statu, String orderColumn, String orderType) {
		String titleParam = "";
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("select rsda.rsdaId, rsda.rsdaTitle, rsda.rsdaRltyId, rsda.resMetaStore.rmstName, rsda.rsdaBuilddate, rsda.rsdaStatus from ResSourceData rsda");
		buffer.append(" where rsda.rsdaAuthortype= " + ResSourceData.AUTHORTYPE_GATHER);
		if(title != null && title.length() > 0){
			buffer.append("and rsda.rsdaTitle like ?");
			titleParam = "%"+title+"%";
		}
		if(statu > 0){
			buffer.append("and rsda.rsdaStatus=" + statu);
		}
		buffer.append(" order by rsda." + orderColumn + " " + orderType);
		String hql = buffer.toString();
		buffer = null;
		
		List<Object> list;
		if(CommonUtil.isNotNull(titleParam)){
			list = getPageHibernateTemplate().find(pagination, hql, titleParam);
		}else{
			list = getPageHibernateTemplate().find(pagination, hql);
		}
		return list;
	}

	/**
	 * 设置抓取资源的审核状态
	 * @param rsdaIds 以逗号分割的资源id字符串。如果一个都没有，则设置所有的未审核的资源
	 * @param auditStatu 审核结果状态
	 */
	@Override
	public void updateGatherResourceStatus(String rsdaIds, int auditStatu) {
		String hql = "update ResSourceData set rsdaStatus=" + auditStatu;
		if(CommonUtil.isNotNull(rsdaIds)){
			hql += " where rsdaId in ("+rsdaIds+")";
		}else{
			hql += " where rsdaStatus=" + ResSourceData.STATUS_CHECK;
		}
		getHibernateTemplate().bulkUpdate(hql);
	}

}
