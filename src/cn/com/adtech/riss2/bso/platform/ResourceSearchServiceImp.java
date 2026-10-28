package cn.com.adtech.riss2.bso.platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashSet;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResDynamicType;
import cn.com.adtech.riss2.domain.resource.ResIndexKey;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceFile;
import cn.com.adtech.riss2.domain.resource.ResSubjectType;
import cn.com.adtech.riss2.jdbcms.SqlBasicUnit;
import cn.com.unionlib.lucene.base.SearchOccurrenceCounter;

import com.jbetter.framework.session.Pagination;

public class ResourceSearchServiceImp extends CommonServiceImp implements IResourceSearchService{
	@Override
	public long getIdentifierHitCount(Map<String, Object> query) {
		String key = CommonUtil.nullToBlank(query.get("key"));
		if (key.isEmpty()) return 0L;
		String hql = "select rsda.rsdaIdentifier from ResSourceData rsda where "
				+ PlatformBasicService.getSearchCommonConditionByHql("rsda", !Boolean.TRUE.equals(query.get("literatureFields")))
				+ " and rsda.rsdaIdentifier like ?";
		List<Object> params = new ArrayList<Object>();
		params.add("%" + key + "%");
		if (query.containsKey("rmstId")) {
			hql += " and rsda.rsdaRmstId=?";
			params.add(query.get("rmstId"));
		}
		if (query.containsKey("rltyId")) {
			hql += " and rsda.rsdaRltyId=?";
			params.add(query.get("rltyId"));
		}
		if (query.containsKey("literatureCategoryId")) {
			hql += " and " + literatureCategoryCondition((Long)query.get("literatureCategoryId"), (Long)query.get("rmstId"));
		}
		long hits = 0L;
		for (Object value : getHibernateTemplate().find(hql, params.toArray())) {
			hits += SearchOccurrenceCounter.countLiteralOccurrences(CommonUtil.nullToBlank(value), key);
		}
		return hits;
	}

	@Override
	public List<Object[]> getResourceList(Pagination p, Map<String, Object> map) {
		// Interface language controls labels, not the language of documents in the literature library.
		String where = PlatformBasicService.getSearchCommonConditionByHql("rsda", !Boolean.TRUE.equals(map.get("literatureFields")));
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select rsda.rsdaId,");
		sb.append("rsda.rsdaTitle,");
		sb.append("rsda.rsdaCreator,");
		sb.append("rsda.rsdaPublisher,");
		sb.append("rsda.rsdaDate,");
		sb.append("rsda.rsdaDescription,");
		sb.append("rsda.rsdaKeyword,");
		sb.append("rsda.rsdaBigimgUrl,");
		sb.append("rsda.rsdaBuilddate,");
		sb.append("rsda.rsdaLooknum,");
		sb.append("rsda.rsdaFileUrl,");
		sb.append("rsda.rsdaSourceType");
		if (Boolean.TRUE.equals(map.get("literatureFields"))) {
			sb.append(",rsda.rsdaIdentifier,rsda.rsdaLanguage,literatureSubject.rstyName");
		}
		sb.append(" from ResSourceData rsda");
		if (Boolean.TRUE.equals(map.get("literatureFields"))) {
			// Missing subject classifications must not remove resources from the list.
			sb.append(" left join rsda.resSubjectType literatureSubject");
		}
		sb.append(" where ").append(where);
		if(map.containsKey("rmstId")){
			sb.append(" and rsda.rsdaRmstId=?");
			params.add(map.get("rmstId"));
		}
		if(map.containsKey("rltyId")){
			sb.append(" and rsda.rsdaRltyId=?");
			params.add(map.get("rltyId"));
		}
		if (map.containsKey("literatureCategoryId")) {
			sb.append(" and ").append(literatureCategoryCondition((Long)map.get("literatureCategoryId"), (Long)map.get("rmstId")));
		}
		if(map.containsKey("rstyId")){
			String ids = getSubjectChilds((Long) map.get("rstyId"));
			sb.append(" and rsda.rsdaRstyId in (").append(ids).append(")");
		}
		if(map.containsKey("rdtyAId")){
			String ids = getDynamicChilds((Long) map.get("rdtyAId"));
			sb.append(" and rsda.rsdaRdtyId in (").append(ids).append(")");
		}
		if(map.containsKey("rdtyBId")){
			String ids = getDynamicChilds((Long) map.get("rdtyBId"));
			sb.append(" and rsda.rsdaRdtyTwo in (").append(ids).append(")");
		}
		if(map.containsKey("rdtyCId")){
			String ids = getDynamicChilds((Long) map.get("rdtyCId"));
			sb.append(" and rsda.rsdaRdtyThree in (").append(ids).append(")");
		}
		if(map.containsKey("tchar")){
			sb.append(" and rsda.rsdaTitleEn=?");
			params.add(map.get("tchar"));
		}
		if(map.containsKey("isCmd")){
			sb.append(" and rsda.rsdaRcmd="+ResSourceData.IS_RECOMMEND);
		}
		if (Boolean.TRUE.equals(map.get("literatureFields")) && map.containsKey("key")) {
			String key = "%" + map.get("key").toString() + "%";
			String field = String.valueOf(map.get("field"));
			if ("title".equals(field)) {
				sb.append(" and rsda.rsdaTitle like ?");
				params.add(key);
			} else if ("identifier".equals(field)) {
				sb.append(" and rsda.rsdaIdentifier like ?");
				params.add(key);
			} else if ("creator".equals(field)) {
				sb.append(" and rsda.rsdaCreator like ?");
				params.add(key);
			} else {
				sb.append(" and (rsda.rsdaTitle like ? or rsda.rsdaIdentifier like ? or rsda.rsdaCreator like ? or rsda.rsdaKeyword like ?)");
				for (int i = 0; i < 4; i++) params.add(key);
			}
		}
		
		if(map.containsKey("sort")){
			String sort = map.get("sort").toString();
			if(sort.equals("time")){
				sb.append(" order by rsda.rsdaBuilddate DESC,rsda.rsdaId DESC");
			}else{
				sb.append(" order by rsda."+sort+" DESC");
			}
		}else{
			sb.append(" order by rsda.rsdaBuilddate DESC,rsda.rsdaId DESC");
		}
		String hql = sb.toString();
		sb = null;
		
		List<Object[]> dataList = getPageHibernateTemplate().find(p, hql, params.toArray());
		return dataList;
	}
	private String getDynamicChilds(Long rdtyId){
		if(rdtyId == null || rdtyId < 1){
			return "0";
		}
		String sql = SqlBasicUnit.hierarchyIdsSql("res_dynamic_type", "rdty_id", "rdty_pid", rdtyId);
		String ids = getAllChildIds(sql);
		return ids;
	}
	private String getSubjectChilds(Long rstyId){
		if(rstyId == null || rstyId < 1){
			return "0";
		}
		String sql = SqlBasicUnit.hierarchyIdsSql("RES_SUBJECT_TYPE", "RSTY_ID", "RSTY_PID", rstyId);
		String ids = getAllChildIds(sql);
		return ids;
	}
	public String getAllChildIds(String sql) {
		List<Object> idlist = getSingleColoumList(sql, null);
		if(idlist == null || idlist.size() < 1){
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		String ids = "";
		for(int i=0,s=idlist.size(); i<s; i++){
			buffer.append(idlist.get(i).toString());
			if(i<s-1){
				buffer.append(",");
			}
		}
		ids = buffer.toString();
		buffer = null;
		idlist = null;
		return ids;
	}

	@Override
	public List<Object[]> getResType(Long rmstId) {
		String hql = "select " +
				"rsl.resLiterType.rltyId," +
				"rsl.resLiterType.rltyName " +
				"from ResStoreLiter rsl " +
				"where rsl.rmstId="+rmstId+
						" order by rsl.resLiterType.rltyId ASC ";
		List<Object[]> dataList = find(hql);
		return dataList;
	}

	@Override
	public List<Object[]> getLiteratureTypes(Long rmstId) {
		String hql = "select d.rdtyId, d.rdtyName, d.rdtyPid, d.rmstId, d.rdtyZwName"
				+ " from ResDynamicType d where d.rdtyType=1 and d.resMetaStore.rmstStatus=1";
		List<Object> params = new ArrayList<Object>();
		if (rmstId != null && rmstId > 0L) {
			hql += " and d.rmstId=?";
			params.add(rmstId);
		}
		hql += " order by d.rdtyIndex ASC, d.rdtyId ASC";
		return getHibernateTemplate().find(hql, params.toArray());
	}

	private String literatureCategoryCondition(Long categoryId, Long rmstId) {
		List<Object[]> categories = getLiteratureTypes(rmstId);
		Long categoryStore = null;
		for (Object[] row : categories) if (categoryId.equals(row[0])) categoryStore = (Long)row[3];
		if (categoryStore == null) return "1=0";
		Set<Long> ids = new LinkedHashSet<Long>(); ids.add(categoryId);
		boolean changed;
		do {
			changed = false;
			for (Object[] row : categories) {
				if (categoryStore.equals(row[3]) && ids.contains(row[2]) && ids.add((Long)row[0])) changed = true;
			}
		} while (changed);
		StringBuilder values = new StringBuilder();
		// IDs come only from mapped numeric category fields, never from an interpolated request string.
		for (Long id : ids) { if (values.length() > 0) values.append(','); values.append(id.longValue()); }
		return "rsda.rsdaRdtyId in (" + values + ") and rsda.rsdaRmstId=" + categoryStore.longValue();
	}

	@Override
	public List<Long> getLiteratureResourceIds(Long categoryId, Long rmstId) {
		String hql = "select rsda.rsdaId from ResSourceData rsda where "
				+ PlatformBasicService.getSearchCommonConditionByHql("rsda", false)
				+ " and " + literatureCategoryCondition(categoryId, rmstId);
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<ResDynamicType> getGroupList(Long rmstId) {
		String hql = "from ResDynamicType rdty where rdty.rmstId=? order by rdty.rdtyIndex ASC";
		List<ResDynamicType> list = getHibernateTemplate().find(hql, rmstId);
		return list;
	}
	@Override
	public List<Object[]> getRdtyList(Long rmstId) {
		String hql = "select rdtyId,rdtyName,rdtyGrade,rdtyPid,rdtyZwName,rdtyEnName,rdtyType from ResDynamicType rdty where rdty.rmstId=? order by rdty.rdtyIndex ASC";
		List<Object[]> list = getHibernateTemplate().find(hql, rmstId);
		return list;
	}
	
	@Override
	public List<ResSubjectType> getSubjectClassList() {
		String hql = " from ResSubjectType rsty where rsty.rstyId>0 order by rsty.rstyIndex";
		List<ResSubjectType> list =null;
		try{
			 list = this.getHibernateTemplate().find(hql);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Object[]> getStoreList(Long rltyId) {
		String hql = "select " +
		"rsl.resMetaStore.rmstId," +
		"rsl.resMetaStore.rmstName " +
		"from ResStoreLiter rsl " +
		"where rsl.rltyId="+rltyId+
				" order by rsl.resMetaStore.rmstIndexSort ASC";
		List<Object[]> dataList = find(hql);
		return dataList;
	}
	
	@Override
	public List<String> getStoreIpList(Long rmstId) {
		String hql = "select ip.rsidIpPart from ResStoreIpDeploy ip where ip.rsidRmstId=?";
		List<String> ipList = getHibernateTemplate().find(hql, rmstId);
		return ipList;
	}
	
	@Override
	public List<Object[]> getMetaData(Long rsdaId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select reda.resElementPool.repoName,");
		sb.append("reda.resElementPool.repoTitle,");
		sb.append("reda.redaData");
		sb.append(" from ResExtendData reda");
		sb.append(" where reda.redaRsdaId=").append(rsdaId);
		sb.append(" and (reda.resElementPool.repoShowmode=1");
		sb.append(" or ((reda.resElementPool.repoShowmode is null or reda.resElementPool.repoShowmode=0) and reda.redaData is not null))");
		sb.append(" order by reda.resElementPool.repoIndex ASC");
		
		String hql = sb.toString();
		sb = null;
		List<Object[]> metaList = find(hql);
		
		if(metaList == null || metaList.size() < 1){
			return null;
		}
		
		String eleCode = null;
		String eleData = null;
		for(Object[] meta : metaList){
			eleCode = meta[0] == null ? "" : meta[0].toString().toLowerCase();
			eleData = meta[2] == null ? "" : meta[2].toString();
			if(!CommonUtil.isNotNull(eleData)){
				continue;
			}
			
			if(eleCode.equals("keyword") || eleCode.equals("creator")){
				eleData = eleData.replace(" ", ";");
				eleData = eleData.replace("；", ";");
				eleData = eleData.replace(",", ";");
				eleData = eleData.replace("，", ";");
				String[] keys = eleData.split(";");
				
				String content = "";
				for(int i=0; i<keys.length; i++){
					if(CommonUtil.isNotNull(keys[i].trim())){
						content += "<a href='javascript:void(0);' onclick='"+eleCode+"Jump(\""+keys[i].trim()+"\");'>" + keys[i].trim() + "</a> &nbsp;";
					}
				}
				meta[2] = content;
			}
			else if(eleCode.equals("relation") || eleCode.equals("source")){
				//设置来源和关联两个元素的超连接
				String reference = "";
				if(eleData.contains("www") || eleData.contains("http")){
					if(eleData.substring(0, 3).toLowerCase().equals("www")){
						reference = "<a href='http://"+eleData+"' target='_blank'>" + eleData + "</a>";
					}else{
						reference = "<a href='"+eleData+"' target='_blank'>" + eleData + "</a>";
					}
					meta[2] = reference;
				}
			}
			else{
				eleData = CommonUtil.hangleTextArea(eleData);
				meta[2] = eleData;
			}
		}
		return metaList;
	}
	
	
	@Override
	public List<Object[]> getResRelated(Long rssuId, Long rsdaId) {
		String where = PlatformBasicService.getSearchCommonConditionByHql("rsda", true);
		StringBuffer sb = new StringBuffer();
		sb.append("select rsda.rsdaId,");
		sb.append("rsda.rsdaTitle,");
		sb.append("rsda.rsdaBigimgUrl,");
		sb.append("rsda.rsdaSourceType,");
		sb.append("rsda.rsdaFileUrl");
		sb.append(" from ResSourceData rsda where rsda.rsdaRssuId = ?");
		sb.append(" and ").append(where);
		if(rsdaId != null && rsdaId > 0L){
			sb.append(" and rsda.rsdaId != ").append(rsdaId);
		}
		String hql = sb.toString();
		sb = null;
		List<Object[]> list = getHibernateTemplate().find(hql,rssuId);
		return list;
	}
	
	
	@Override
	public List<Object[]> getRelatedResearch(Pagination pagination, Long rsdaId) {
		return getRelatedResearch(pagination, rsdaId, null);
	}

	@Override
	public List<Object[]> getRelatedResearch(Pagination pagination, Long rsdaId, String kind) {
		if (rsdaId == null || rsdaId < 1L) return new ArrayList<Object[]>();
		if (kind != null && !"paper".equals(kind) && !"book".equals(kind)) throw new IllegalArgumentException("Invalid research kind");
		String sourceHql = "from ResSourceData rsda where rsda.rsdaId=? and "
				+ PlatformBasicService.getSearchCommonConditionByHql("rsda", false);
		List<ResSourceData> sources = getHibernateTemplate().find(sourceHql, rsdaId);
		if (sources.isEmpty()) {
			return new ArrayList<Object[]>();
		}
		StringBuilder hql = new StringBuilder("select rsda.rsdaId,rsda.rsdaTitle,rsda.rsdaCreator,rsda.rsdaDate,rsda.rsdaSource from ResSourceData rsda");
		if (kind != null) hql.append(" left join rsda.resLiterType researchType");
		hql.append(" where ").append(PlatformBasicService.getSearchCommonConditionByHql("rsda", true));
		Long subject=sources.get(0).getRsdaRssuId();
		hql.append(" and rsda.rsdaId<>? and (exists (from ResResearchLink link where link.researchId=rsda.rsdaId and link.resourceId=?)");
		List<Object> params = new ArrayList<Object>();
		params.add(rsdaId); params.add(rsdaId);
		if (subject!=null && subject>0) { hql.append(" or rsda.rsdaRssuId=?"); params.add(subject); }
		hql.append(")");
		if ("paper".equals(kind)) {
			hql.append(" and (researchType.rltyName like ? or researchType.rltyName like ?)");
			params.add("%论文%"); params.add("%期刊%");
		} else if ("book".equals(kind)) {
			hql.append(" and (researchType.rltyName like ? or researchType.rltyName like ? or researchType.rltyName like ?)");
			params.add("%专著%"); params.add("%图书%"); params.add("%著作%");
		}
		String countHql = "select count(*)" + hql.substring(hql.indexOf(" from "));
		hql.append(" order by rsda.rsdaBuilddate DESC,rsda.rsdaId DESC");
		return getPageHibernateTemplate().find(pagination, hql.toString(), countHql, params.toArray());
	}

	@Override
	public List<Object[]> getAttachFile(Long rsdaId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select rsf.rsfiId,");
		sb.append("rsf.resFileName,");
		sb.append("rsf.resFilePath,");
		sb.append("rsf.resViewPath");
		sb.append(" from ResSourceFile rsf");
		sb.append(" where rsf.resRsdaId=").append(rsdaId);
		sb.append(" and rsf.resFileAccessType=?");
		sb.append(" order by rsf.resIndex DESC");
		
		String hql = sb.toString();
		sb = null;
		List<Object[]> datalist  = getHibernateTemplate().find(hql, ResSourceFile.FILE_ACCESS_PUBLIC);
		return datalist;
	}
	@Override
	public List<Object[]> getWorksByAutor(String autor){
		StringBuffer sb = new StringBuffer();
		sb.append("select rsda.rsdaId,");
		sb.append("rsda.rsdaTitle");
		sb.append(" from ResSourceData rsda where rsda.rsdaCreator = ?");
		String hql = sb.toString();
		sb = null;
		List<Object[]> datalist  = getHibernateTemplate().find(hql,autor);
		return datalist;
	}
	
	@Override
	public List<Object[]> getSimilarRes(Long _rsdaId, Long rmstId, Long rltyId) {
		String where = PlatformBasicService.getSearchCommonConditionByHql("rsda", true);
		StringBuffer sb = new StringBuffer();
		sb.append("select rsda.rsdaId,");
		sb.append("rsda.rsdaTitle,");
		sb.append("rsda.rsdaCreator,");
		sb.append("rsda.rsdaBigimgUrl,");
		sb.append("rsda.rsdaSourceType,");
		sb.append("rsda.rsdaFileUrl");
		sb.append(" from ResSourceData rsda where rsda.rsdaId != ").append(_rsdaId);
		sb.append(" and rsda.rsdaRmstId=").append(rmstId);
		sb.append(" and rsda.rsdaRltyId=").append(rltyId);
		sb.append(" and ").append(where);
		sb.append(" order by rsda.rsdaLooknum DESC");
		
		String hql = sb.toString();
		sb = null;
		
		Pagination pagination = new Pagination();
		pagination.setPageResults(6);
		List<Object[]> list = getPageHibernateTemplate().find(pagination, hql);
		return list;
	}
	
	/**
	 * 保存检索记录
	 * @param searchVal
	 */
	@Override
	public void saveSearchKey(String searchVal) {
		if(!CommonUtil.isNotNull(searchVal)){
			return;
		}
//		Analyzer a = AnalyzerFactory.instance().getSmartAnalyzer();
//		List<String> keyList = SegmentWord.displayToken(searchVal, a);
//		for(String s : keyList){
//			saveOrUpdateKey(s);
//		}
		saveOrUpdateKey(searchVal);
	}
	private void saveOrUpdateKey(String key) {
		String hql = "from ResIndexKey rike where rike.rikeName = ?";
		List<ResIndexKey> list = getHibernateTemplate().find(hql, key);
		ResIndexKey indexKey = null;
		if(list == null || list.size() < 1){
			indexKey = new ResIndexKey();
			if(key.length() > 10){
				key = key.substring(0, 10);
			}
			indexKey.setRikeName(key);
			indexKey.setRikeSearchCount(1);
		}else{
			indexKey = list.get(0);
			indexKey.setRikeSearchCount(indexKey.getRikeSearchCount() + 1);
		}
		this.getHibernateTemplate().saveOrUpdate(indexKey);
	}
	
	
	@Override
	public List<Object[]> getResAdvSearch(Pagination p,
			Map<String, Object> params, String rmstIds, String rltyIds, String sort) {
		if (Boolean.TRUE.equals(params.get("advancedResultFields"))) {
			List<Object> bindings = new ArrayList<Object>();
			String hql = "select rsda.rsdaId,rsda.rsdaTitle,rsda.rsdaCreator,rsda.rsdaPublisher,rsda.rsdaDate,"
					+ "rsda.rsdaDescription,rsda.rsdaKeyword,rsda.rsdaBigimgUrl,rsda.rsdaBuilddate,rsda.rsdaLooknum,"
					+ "rsda.rsdaFileUrl,rsda.rsdaSourceType,rsda.rsdaSubject,rsda.rsdaIdentifier"
					+ advancedResultsFrom(params, rmstIds, rltyIds, bindings)
					+ " order by rsda.rsdaBuilddate DESC,rsda.rsdaId DESC";
			return getPageHibernateTemplate().find(p, hql, bindings.toArray());
		}
		String where = PlatformBasicService.getSearchCommonConditionByHql("rsda", true);
		StringBuffer sb = new StringBuffer();
		sb.append("select rsda.rsdaId,");
		sb.append("rsda.rsdaTitle,");
		sb.append("rsda.rsdaCreator,");
		sb.append("rsda.rsdaPublisher,");
		sb.append("rsda.rsdaDate,");
		sb.append("rsda.rsdaDescription,");
		sb.append("rsda.rsdaKeyword,");
		sb.append("rsda.rsdaBigimgUrl,");
		sb.append("rsda.rsdaBuilddate,");
		sb.append("rsda.rsdaLooknum,");
		sb.append("rsda.rsdaFileUrl,");
		sb.append("rsda.rsdaSourceType");
		sb.append(" from ResSourceData rsda");
		sb.append(" where ").append(where);
		
		List<Object> paramList = new ArrayList<Object>();
		
		if(CommonUtil.isNotNull(rmstIds)){
			sb.append(" and rsda.rsdaRmstId in (").append(rmstIds).append(")");
		}
		if(CommonUtil.isNotNull(rltyIds)){
			sb.append(" and rsda.rsdaRltyId in (").append(rltyIds).append(")");
		}
		
		if(params.size() > 0){
			sb.append(" and (");
		}
		
		boolean isCon = false;
		if(params.containsKey("key1")){
			String k = params.get("key1").toString();
			String l = params.get("like1").toString();
			if(l.equals("LIKE")){
				k = "%" + k + "%";
			}else{
				l="=";
			}
			sb.append(" rsda.").append(params.get("col1")).append(" ");
			sb.append(l).append(" ? ");
			paramList.add(k);
			isCon = true;
		}
		if(params.containsKey("key2")){
			if(isCon){
				sb.append(params.get("con2"));
			}
			String k = params.get("key2").toString();
			String l = params.get("like2").toString();
			if(l.equals("LIKE")){
				k = "%" + k + "%";
			}else{
				l="=";
			}
			
			sb.append(" rsda.").append(params.get("col2")).append(" ");
			sb.append(l).append(" ? ");
			paramList.add(k);
			isCon = true;
		}
		if(params.containsKey("key3")){
			if(isCon){
				sb.append(params.get("con3"));
			}
			String k = params.get("key3").toString();
			String l = params.get("like3").toString();
			if(l.equals("LIKE")){
				k = "%" + k + "%";
			}else{
				l="=";
			}
			sb.append(" rsda.").append(params.get("col3")).append(" ");
			sb.append(l).append(" ? ");
			paramList.add(k);
			isCon = true;
		}
		if(params.containsKey("key4")){
			if(isCon){
				sb.append(params.get("con4"));
			}
			String k = params.get("key4").toString();
			String l = params.get("like4").toString();
			if(l.equals("LIKE")){
				k = "%" + k + "%";
			}else{
				l="=";
			}
			sb.append(" rsda.").append(params.get("col4")).append(" ");
			sb.append(l).append(" ? ");
			paramList.add(k);
			isCon = true;
		}
		
		if(params.size() > 0){
			sb.append(")");
		}
		if(!sort.equals("rsdaBuilddate")){
			sb.append(" order by rsda.rsdaBuilddate DESC");
		}else{
			sb.append(" order by rsda."+sort+" DESC");
		}
		String hql = sb.toString();
		sb = null;
		List<Object[]> dataList = null;
		if(paramList.size() > 0){
			dataList = getPageHibernateTemplate().find(p, hql, paramList.toArray());
		}else{
			dataList = getPageHibernateTemplate().find(p, hql);
		}
		return dataList;
	}
	
	private String advancedResultsFrom(Map<String, Object> params, String libraryIds, String typeIds, List<Object> bindings) {
		StringBuilder hql = new StringBuilder(" from ResSourceData rsda where ");
		hql.append(PlatformBasicService.getSearchCommonConditionByHql("rsda", true));
		String libraries = AdvancedSearchQuery.numericIds(libraryIds);
		String types = AdvancedSearchQuery.numericIds(typeIds);
		if (!libraries.isEmpty()) hql.append(" and rsda.rsdaRmstId in (").append(libraries).append(')');
		if (!types.isEmpty()) {
			// The dialog lists the same configured category-one types as the literature page.
			hql.append(" and (");
			boolean first = true;
			for (String type : types.split(",")) {
				if (!first) hql.append(" or "); first = false;
				hql.append('(').append(literatureCategoryCondition(Long.valueOf(type), null)).append(')');
			}
			hql.append(')');
		}
		hql.append(AdvancedSearchQuery.conditions(params, bindings));
		return hql.toString();
	}

	@Override
	public long getResAdvSearchHitCount(Map<String, Object> params, String libraryIds, String typeIds) {
		List<String> fields = new ArrayList<String>();
		for (int i = 1; i <= 4; i++) {
			if (CommonUtil.nullToBlank(params.get("key" + i)).trim().isEmpty()) continue;
			String field = AdvancedSearchQuery.field(params.get("col" + i));
			if (!fields.contains(field)) fields.add(field);
		}
		if (fields.isEmpty()) return 0L;
		StringBuilder select = new StringBuilder("select ");
		for (String field : fields) { if (select.length() > 7) select.append(','); select.append("rsda.").append(field); }
		List<Object> bindings = new ArrayList<Object>();
		String hql = select + advancedResultsFrom(params, libraryIds, typeIds, bindings);
		List<?> matches = getHibernateTemplate().find(hql, bindings.toArray());
		long hits = 0L;
		for (Object match : matches) {
			Object[] row = fields.size() == 1 ? new Object[]{match} : (Object[]) match;
			Set<String> counted = new LinkedHashSet<String>();
			for (int i = 1; i <= 4; i++) {
				String key = CommonUtil.nullToBlank(params.get("key" + i));
				if (key.trim().isEmpty()) continue;
				String field = AdvancedSearchQuery.field(params.get("col" + i));
				String value = CommonUtil.nullToBlank(row[fields.indexOf(field)]);
				if ("=".equals(params.get("like" + i)) && !value.equalsIgnoreCase(key)) continue;
				if (counted.add(field + "\u0000" + key.toLowerCase(java.util.Locale.ROOT))) {
					hits += SearchOccurrenceCounter.countLiteralOccurrences(value, key);
				}
			}
		}
		return hits;
	}

	@Override
	public List<Object[]> getHotReslist(int num) {
		String where = PlatformBasicService.getSearchCommonConditionByHql("rsda", true);
		StringBuffer sb = new StringBuffer();
		sb.append("select rsda.rsdaId,");
		sb.append("rsda.rsdaTitle,");
		sb.append("rsda.rsdaCreator,");
		sb.append("rsda.rsdaLooknum");
		sb.append(" from ResSourceData rsda");
		sb.append(" where ").append(where);
		sb.append(" order by rsda.rsdaLooknum DESC");
		
		Pagination pagination = new Pagination();
		pagination.setPageResults(num);
		
		String hql = sb.toString();
		sb = null;
		List<Object[]> dataList = getPageHibernateTemplate().find(pagination, hql);
		return dataList;
	}
}















