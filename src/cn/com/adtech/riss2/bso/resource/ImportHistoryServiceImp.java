package cn.com.adtech.riss2.bso.resource;

import java.io.File;
import java.util.List;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.domain.resource.ResImportHistory;

import com.jbetter.framework.session.Pagination;

public class ImportHistoryServiceImp extends CommonServiceImp implements IImportHistoryService{

	
	private IResourceDataService resourceService = null;
	public void setResourceService(IResourceDataService resourceService) {
		this.resourceService = resourceService;
	}
	
	public List<ResImportHistory> getImportHistoryList(long rminId, Pagination pagination, String orderType, String orderColumn) {
		String hql = "select rih.rmhiId, rih.rmhiCheckMust, rih.rmhiFilefolder, rih.rmhiLocalFilename, rih.rmhiFilename, rih.rmhiAddress, rih.rmhiImportDate ";
		hql += ", (select count(rsd.rsdaId) from ResSourceData rsd where rih.rmhiId = rsd.rsdaRihiId) as sourceNum";
		hql += " from ResImportHistory rih where rih.rmhiRminId="+rminId+" order by "+orderColumn+" "+orderType;
		String countHql = "select count(rih.rmhiId) from ResImportHistory rih where rih.rmhiRminId="+rminId;
		
		List<ResImportHistory> list = getPageHibernateTemplate().find(pagination, hql,countHql,null);
		return list;
	}

	public String getResourceByImportHistory(long rihiId, int start, int limit,
			String orderColumn, String orderType) {
		String sql = "select rsd.RSDA_ID, rsd.RSDA_MIDDLENAME,rsd.RSDA_BUILDDATE,rsd.RSDA_STATUS,rsd.rsda_title,rsd.rsda_subject,rsd.RSDA_AUDITING,rsd.RSDA_CREATOR,rsd.RSDA_PUBLISHER from RES_SOURCE_DATA rsd where rsd.RSDA_ISUSE=1 and rsd.RSDA_RIHI_ID=" + rihiId;
		sql += " order by " + orderColumn + " " + orderType;
		String json = getListToJson(sql, null, start, limit);
		return json;
	}

	@Override
	public void deleteImportHistory(Long rmhiId) {
		String fileFolder = SysConfigInfo.IMP_EXCEL_DIR;
		ResImportHistory importHistory = (ResImportHistory)get(ResImportHistory.class, rmhiId);
		String filePath = fileFolder + importHistory.getRmhiFilename();
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
		getHibernateTemplate().delete(importHistory);
	}

	@Override
	public void deleteImportHistoryAndResource(Long rmhiId) {
		String hql = "select rsda.rsdaId from ResSourceData rsda where rsda.rsdaRihiId=?";
		List<Long> idlist = getHibernateTemplate().find(hql, rmhiId);
		deleteImportHistory(rmhiId);
		if(idlist == null || idlist.size() < 1){
			return;
		}
		Long rsdaId = null;
		for(int i=0,s=idlist.size(); i<s; i++){
			rsdaId = idlist.get(i);
			resourceService.deleteResource(rsdaId, null);
		}
	}


}
