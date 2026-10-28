package cn.com.adtech.riss2.bso.platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Calendar;
import java.util.Date;

import cn.com.adtech.riss2.bean.ResourceForTypeBean;
import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.cache.PlatformCacheFactory;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.count.SysAccessCounter;

import com.jbetter.framework.session.Pagination;

public class IndexDataServiceImp extends CommonServiceImp implements IIndexDataService{
	@Override
	public Map<String, Object> getHomeStatistics() {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("resourceTotal", homeCount("select count(r.rsdaId) from ResSourceData r where "
				+ PlatformBasicService.getSearchCommonConditionByHql("r", false)));
		// Login, searches, reads and downloads are activity records, not additional site visits.
		result.put("visitTotal", homeCount("select count(c.sacoId) from SysAccessCounter c where c.sacoType=?",
				SysAccessCounter.TYPE_ACCESS));
		return result;
	}

	private Long homeCount(String hql, Object... parameters) {
		try {
			return ((Number)getHibernateTemplate().find(hql, parameters).get(0)).longValue();
		} catch (org.springframework.dao.DataAccessException ex) {
			logger.error("Unable to load homepage statistic", ex);
			return null;
		}
	}

	@Override
	public Map<String, Object> getDatabaseStatistics(int year) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		int currentYear = calendar.get(Calendar.YEAR);
		int currentMonth = calendar.get(Calendar.MONTH);
		calendar.add(Calendar.DAY_OF_YEAR, -30);
		Date recent = calendar.getTime();
		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0); calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0); calendar.set(Calendar.MILLISECOND, 0);
		Date today = calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date yesterday = calendar.getTime();
		String visible = PlatformBasicService.getSearchCommonConditionByHql("r", false);
		result.put("resourceTotal", getHibernateTemplate().find("select count(r.rsdaId) from ResSourceData r where " + visible).get(0));
		result.put("recentTotal", getHibernateTemplate().find("select count(r.rsdaId) from ResSourceData r where " + visible + " and r.rsdaBuilddate>=? and r.rsdaBuilddate<=?", new Object[]{recent, now}).get(0));
		String addedHql = "select count(r.rsdaId) from ResSourceData r where " + visible + " and r.rsdaBuilddate>=? and r.rsdaBuilddate<?";
		long todayAdded = countBetween(addedHql, today, now);
		long yesterdayAdded = countBetween(addedHql, yesterday, today);
		result.put("todayAdded", todayAdded);
		result.put("dailyResourceChangePercent", dailyChange(todayAdded, yesterdayAdded));
		List<Object[]> annual = getHibernateTemplate().find("select year(r.rsdaBuilddate), count(r.rsdaId) from ResSourceData r where " + visible + " and r.rsdaBuilddate<=? group by year(r.rsdaBuilddate) order by year(r.rsdaBuilddate)", now);
		List<Map<String, Object>> growth = new ArrayList<Map<String, Object>>();
		for (int y = currentYear - 10; y <= currentYear; y++) {
			long count = 0;
			for (Object[] row : annual) if (row[0] != null && ((Number)row[0]).intValue() == y) count = ((Number)row[1]).longValue();
			Map<String, Object> item = new LinkedHashMap<String, Object>();
			item.put("year", y); item.put("count", count); growth.add(item);
		}
		result.put("growth", growth);
		result.put("hotwords", getHotSearchKey(20));
		calendar.clear(); calendar.set(year, Calendar.JANUARY, 1);
		Date begin = calendar.getTime(); calendar.add(Calendar.YEAR, 1);
		Date end = calendar.getTime();
		long[] counts = new long[12];
		String source = "resource";
		// Same join and count(*) as console's 特色库资源访问量统计; no login restriction is bypassed.
		try {
			List<Object[]> rows = getHibernateTemplate().find("select month(h.rsbhDate), count(h.rsbhId) from ResSourceBrowseHistory h, ResSourceData r, ResMetaStore m where h.rsdaId=r.rsdaId and r.rsdaRmstId=m.rmstId and h.rsbhDate>=? and h.rsbhDate<? group by month(h.rsbhDate)", new Object[]{begin, end});
			fillMonths(counts, rows);
		} catch (org.springframework.dao.DataAccessException ex) {
			logger.warn("Resource access statistics unavailable; using site statistics", ex);
			source = "site";
		}
		long resourceVisits = 0;
		for (long count : counts) resourceVisits += count;
		if (resourceVisits == 0) {
			// Parentheses are essential: both anonymous-user cases must respect the date range.
			List<Object[]> rows = getHibernateTemplate().find("select month(c.sacoDate), count(c.sacoId) from SysAccessCounter c where (c.sacoSuinId is null or c.sacoSuinId=0) and c.sacoDate>=? and c.sacoDate<? group by month(c.sacoDate)", new Object[]{begin, end});
			long[] site = new long[12]; fillMonths(site, rows);
			long siteVisits = 0; for (long count : site) siteVisits += count;
			if (siteVisits > 0 || "site".equals(source)) { counts = site; source = "site"; }
		}
		List<Map<String, Object>> access = new ArrayList<Map<String, Object>>();
		for (int month = 0; month < 12; month++) {
			Map<String, Object> item = new LinkedHashMap<String, Object>();
			item.put("month", month + 1); item.put("count", counts[month]); access.add(item);
		}
		result.put("year", year); result.put("access", access); result.put("accessSource", source);
		result.put("accessMonth", year == currentYear ? counts[currentMonth] : null);
		String dailyAccessHql = "site".equals(source)
			? "select count(c.sacoId) from SysAccessCounter c where (c.sacoSuinId is null or c.sacoSuinId=0) and c.sacoDate>=? and c.sacoDate<?"
			: "select count(h.rsbhId) from ResSourceBrowseHistory h, ResSourceData r, ResMetaStore m where h.rsdaId=r.rsdaId and r.rsdaRmstId=m.rmstId and h.rsbhDate>=? and h.rsbhDate<?";
		long todayAccess = countBetween(dailyAccessHql, today, now);
		result.put("todayAccess", year == currentYear ? todayAccess : null);
		result.put("dailyAccessChangePercent", year == currentYear ? dailyChange(todayAccess, countBetween(dailyAccessHql, yesterday, today)) : null);
		return result;
	}

	private long countBetween(String hql, Date begin, Date end) {
		return ((Number)getHibernateTemplate().find(hql, new Object[]{begin, end}).get(0)).longValue();
	}

	private static Double dailyChange(long today, long yesterday) {
		// A zero baseline has no meaningful percentage; the page shows the real count instead.
		return yesterday == 0 ? null : Double.valueOf((today - yesterday) * 100.0 / yesterday);
	}

	private static void fillMonths(long[] counts, List<Object[]> rows) {
		for (Object[] row : rows) {
			if (row[0] == null) continue;
			int month = ((Number)row[0]).intValue();
			if (month >= 1 && month <= 12) counts[month - 1] = ((Number)row[1]).longValue();
		}
	}

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
		String hql = "select rike.rikeName from ResIndexKey rike where rike.rikeName is not null and trim(rike.rikeName)<>'' order by coalesce(rike.rikeSearchCount,0) DESC, rike.rikeId ASC";
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
