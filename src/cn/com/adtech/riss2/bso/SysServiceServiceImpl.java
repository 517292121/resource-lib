package cn.com.adtech.riss2.bso;

import java.util.Date;
import java.util.List;

import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.domain.resource.ResSourceComt;
import cn.com.adtech.riss2.domain.resource.ResSourceData;

import com.jbetter.framework.session.Pagination;

public class SysServiceServiceImpl extends CommonServiceImp implements ISysServiceService {
	public Wrapper getResSourceComtList(String name, String beginDate, String endDate, String state, PageExtNative page) {
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(page.getStart()/page.getLimit());
		pagination.setPageResults(page.getLimit());
		String hql = "select comt.rscoId, comt.rscoContent, comt.rscoDate, comt.sysUserinfo.suinLoginName,comt.sysUserinfo.suinName,comt.rscoStatus "
				+ "from ResSourceComt comt where comt.resSourceData.rsdaCmtstus="
				+ ResSourceData.REVIEW_SWITCH_OPEN + " and comt.rscoContent is not null"
				+ " and (comt.sysUserinfo.suinLoginName like '%" + name + "%' or comt.sysUserinfo.suinName like '%" + name + "%' )";
		// 起始时间
		if(beginDate != null && beginDate.length() >= 10) {
			beginDate = beginDate.substring(0, 10) + " 00:00:00";
			hql += " and comt.rscoDate > '" + beginDate + "'";
		}
		// 终止时间
		if(endDate != null && endDate.length() >= 10) {
			endDate = endDate.substring(0, 10) + " 23:59:59";
			hql += " and comt.rscoDate <= '" + endDate + "'";
		}
		// 审核状态
		if(state == null || state.equals("")){
			hql += " and comt.rscoStatus is null";
		}else if(state != null && !state.equals("3")) {
			hql += " and comt.rscoStatus = " + state;
		}
		
		if(page != null) {
			if(ServiceUtil.isNotNull(page.getSort()) && ServiceUtil.isNotNull(page.getDir())) {
				//tempContent suinLoginName suinName
				String sort = page.getSort();
				
				if("tempContent".equals(sort)){
					hql += " order by comt.rscoContent " + page.getDir();
				}else if("suinLoginName".equals(sort) || "suinName".equals(sort)){
					hql += " order by comt.sysUserinfo." + page.getSort() + " " + page.getDir();
				}else{
					hql += " order by comt." + page.getSort() + " " + page.getDir();
				}
				
			}
		}
		List list = getPageHibernateTemplate().find(pagination, hql);
		return new Wrapper(list, pagination.getTotalResults() + "");
	}

	@Override
	public int deleteResSourceComt(String ids) {
		if(ServiceUtil.isNotNull(ids)){
			String[]idgroup = ids.split(",");
			for(String id:idgroup){
				Long rscoId = Long.parseLong(id);
				ResSourceComt resSourceComt = (ResSourceComt)get(ResSourceComt.class, rscoId);
				Long rscoRsdaId = resSourceComt.getRscoRsdaId();
				getHibernateTemplate().delete(resSourceComt);
				String hql = "select rscoGrade from ResSourceComt where rscoStatus="+ResSourceComt.STATUS_PAST+" and rscoRsdaId=" + rscoRsdaId;
				List<Short> list = getHibernateTemplate().find(hql);
				Long total = 3l;
				if(list != null && list.size() > 0) {
					total = 0l;
					for(Short rscoGrade : list) {
						total += rscoGrade;
					}
					total /= list.size();
				}
				getHibernateTemplate().bulkUpdate("update ResSourceData set rsdaGrade=" + total + " where rsdaId=" + rscoRsdaId);
			}
		}
		return 0;
	}

	@Override
	public void updateResSourceComt(Long rscoId, Short rscoStatus) {
		getHibernateTemplate().bulkUpdate("update ResSourceComt comt set comt.rscoStatus=" + rscoStatus + " where comt.rscoId=" + rscoId);
		/*ResSourceComt resSourceComt = (ResSourceComt)get(ResSourceComt.class, rscoId);
		String hql = "select rscoGrade from ResSourceComt where rscoStatus="+ResSourceComt.STATUS_PAST+" and rscoRsdaId=" + resSourceComt.getRscoRsdaId();
		List<Short> list = getHibernateTemplate().find(hql);
		Long total = 3l;
		if(list != null && list.size() > 0) {
			total = 0l;
			for(Short rscoGrade : list) {
				total += rscoGrade;
			}
			total /= list.size();
		}
		getHibernateTemplate().bulkUpdate("update ResSourceData set rsdaGrade=" + total + " where rsdaId=" + resSourceComt.getRscoRsdaId());*/
	}
	
}
