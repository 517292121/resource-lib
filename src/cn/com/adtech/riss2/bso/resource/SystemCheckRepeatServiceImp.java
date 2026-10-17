package cn.com.adtech.riss2.bso.resource;

import java.util.LinkedList;
import java.util.List;

import com.jbetter.framework.session.Pagination;


import cn.com.adtech.riss2.bean.ResourceRepeatBean;
import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSystemRepeat;

public class SystemCheckRepeatServiceImp extends CommonServiceImp implements ISystemCheckRepeatService {

	private IResourceDataService resourceService = null;
	public void setResourceService(IResourceDataService resourceService) {
		this.resourceService = resourceService;
	}

	/**
	 * 系统资源查重入口函数
	 * @param paramList
	 */
	@Override
	public void saveResourceRepeat(List<String> paramList) {
		deleteAll();
		
		String columns = getSelectColumn(paramList, "r");
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select max(r.rsdaId),count(r.rsdaId),"+columns+" from ResSourceData r ");
		buffer.append("where r.rsdaId.rsdaStatus= " + ResSourceData.STATUS_PASS);
		buffer.append(" group by ").append(columns);
		buffer.append(" having count(r.rsdaId)>1 ");
		
		String hql = buffer.toString();
		
		List<Object[]> list = getHibernateTemplate().find(hql);
		if(list == null || list.size() < 1){
			return;
		}
		ResSystemRepeat systemRepeat = null;
		Object[] o = null;
		for(int i=0,s=list.size(); i<s; i++){
			o = list.get(i);
			systemRepeat = new ResSystemRepeat();
			systemRepeat.setRsyrRepeatnum(Integer.parseInt(o[1].toString()));
			systemRepeat.setRsyrRsdaId(Long.parseLong(o[0].toString()));
			save(systemRepeat);
		}
	}
	
	private void deleteAll(){
		String hql = "delete from ResSystemRepeat";
		getHibernateTemplate().bulkUpdate(hql);
	}
	
	private String getSelectColumn(List<String> columnList, String as){
		String result = "";
		if(columnList == null || columnList.size() < 1){
			return result;
		}
		
		for(int i=0,s=columnList.size(); i<s; i++){
			if(result.length() > 1){
				result += ",";
			}
			result += as + "." + columnList.get(i);
		}
		return result;
	}

	
	/**
	 * 获取查重结果
	 * @return
	 */
	@Override
	public List getRepeatResultList(Pagination pagination, String orderColumn, String orderType) {
		String hql = "select r.rsyrId,r.rsyrRsdaId,r.rsyrRepeatnum,r.sourceData.rsdaTitle,r.sourceData.rsdaSubject,r.sourceData.rsdaDescription,r.sourceData.rsdaSource,r.sourceData.rsdaLanguage,r.sourceData.rsdaRelation,r.sourceData.rsdaCoverage,r.sourceData.rsdaCreator,r.sourceData.rsdaPublisher,r.sourceData.rsdaContributor,r.sourceData.rsdaRights,r.sourceData.rsdaDate,r.sourceData.rsdaType,r.sourceData.rsdaFormat,r.sourceData.rsdaIdentifier,r.sourceData.rsdaFileType,r.sourceData.rsdaFileSize from ResSystemRepeat r";
		hql += " order by r." + orderColumn + " " + orderType;
		List list = getPageHibernateTemplate().find(hql);
		return list;
	}

	/**
	 * 根据资源ID和查重条件删除重复记录
	 * @param rsdaId
	 * @param repeatBean
	 * @return
	 */
	@Override
	public boolean deleteRepeatByRsdaId(Long rsdaId,
			ResourceRepeatBean repeatBean) {
		ResSourceData resource = (ResSourceData) get(ResSourceData.class, rsdaId);
		if(resource == null){
			return false;
		}
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer buffer = new StringBuffer();
		buffer.append("select rsda.rsdaId from ResSourceData rsda ");
		buffer.append(" where rsda.rsdaStatus=").append(ResSourceData.STATUS_PASS);
		buffer.append(" and rsda.rsdaId != ").append(rsdaId); //查询除自己之外的资源
		
		if(repeatBean.isCheckTitle()){
			if(resource.getRsdaTitle() == null){
				buffer.append(" and rsda.rsdaTitle is null ");
			}else{
				buffer.append(" and rsda.rsdaTitle=?");
				params.add(resource.getRsdaTitle());
			}
		}
		if(repeatBean.isCheckPublisher()){
			if(resource.getRsdaPublisher() == null){
				buffer.append(" and rsda.rsdaPublisher is null");
			}else{
				buffer.append(" and rsda.rsdaPublisher=?");
				params.add(resource.getRsdaPublisher());
			}
		}
		if(repeatBean.isCheckFilesize()){
			if(resource.getRsdaFileSize() == null){
				buffer.append(" and rsda.rsdaFileSize is null");
			}else{
				buffer.append(" and rsda.rsdaFileSize=?");
				params.add(resource.getRsdaFileSize());
			}
		}
		if(repeatBean.isCheckFiletype()){
			if(resource.getRsdaFileType() == null){
				buffer.append(" and rsda.rsdaFileType is null");
			}else{
				buffer.append(" and rsda.rsdaFileType=?");
				params.add(resource.getRsdaFileType());
			}
		}
		
		
		if(repeatBean.isCheckSubject()){
			if(resource.getRsdaSubject() == null){
				buffer.append(" and rsda.rsdaSubject is null");
			}else{
				buffer.append(" and rsda.rsdaSubject=?");
				params.add(resource.getRsdaSubject());
			}
		}
		if(repeatBean.isCheckDescription()){
			if(resource.getRsdaDescription() == null){
				buffer.append(" and rsda.rsdaDescription is null");
			}else{
				buffer.append(" and rsda.rsdaDescription=?");
				params.add(resource.getRsdaDescription());
			}
		}
		if(repeatBean.isCheckSource()){
			if(resource.getRsdaSource() == null){
				buffer.append(" and rsda.rsdaSource is null");
			}else{
				buffer.append(" and rsda.rsdaSource=?");
				params.add(resource.getRsdaSource());
			}
		}
		if(repeatBean.isCheckLanguage()){
			if(resource.getRsdaLanguage() == null){
				buffer.append(" and rsda.rsdaLanguage is null");
			}else{
				buffer.append(" and rsda.rsdaLanguage=?");
				params.add(resource.getRsdaLanguage());
			}
		}
		if(repeatBean.isCheckRelation()){
			if(resource.getRsdaRelation() == null){
				
			}else{
				buffer.append(" and rsda.rsdaRelation=?");
				params.add(resource.getRsdaRelation());
			}
		}
		if(repeatBean.isCheckCoverage()){
			if(resource.getRsdaCoverage() == null){
				buffer.append(" and rsda.rsdaCoverage is null");
			}else{
				buffer.append(" and rsda.rsdaCoverage=?");
				params.add(resource.getRsdaCoverage());
			}
		}
		if(repeatBean.isCheckCreator()){
			if(resource.getRsdaCreator() == null){
				buffer.append(" and rsda.rsdaCreator is null");
			}else{
				buffer.append(" and rsda.rsdaCreator=?");
				params.add(resource.getRsdaCreator());
			}
		}
		if(repeatBean.isCheckContributor()){
			if(resource.getRsdaContributor() == null){
				buffer.append(" and rsda.rsdaContributor is null");
			}else{
				buffer.append(" and rsda.rsdaContributor=?");
				params.add(resource.getRsdaContributor());
			}
		}
		if(repeatBean.isCheckRights()){
			if(resource.getRsdaRights() == null){
				buffer.append(" and rsda.rsdaRights is null");
			}else{
				buffer.append(" and rsda.rsdaRights=?");
				params.add(resource.getRsdaRights());
			}
		}
		if(repeatBean.isCheckDate()){
			if(resource.getRsdaDate() == null){
				buffer.append(" and rsda.rsdaDate is null");
			}else{
				buffer.append(" and rsda.rsdaDate=?");
				params.add(resource.getRsdaDate());
			}
		}
		if(repeatBean.isCheckType()){
			if(resource.getRsdaType() == null){
				buffer.append(" and rsda.rsdaType is null");
			}else{
				buffer.append(" and rsda.rsdaType=?");
				params.add(resource.getRsdaType());
			}
		}
		if(repeatBean.isCheckFormat()){
			if(resource.getRsdaFormat() == null){
				buffer.append(" and rsda.rsdaFormat is null");
			}else{
				buffer.append(" and rsda.rsdaFormat=?");
				params.add(resource.getRsdaFormat());
			}
		}
		if(repeatBean.isCheckIdentifier()){
			if(resource.getRsdaIdentifier() == null){
				buffer.append(" and rsda.rsdaIdentifier is null");
			}else{
				buffer.append(" and rsda.rsdaIdentifier=?");
				params.add(resource.getRsdaIdentifier());
			}
		}
		
		Object[] o = params.toArray();
		String hql = buffer.toString();
		params = null;
		buffer = null;
		
		List<Long> list = getHibernateTemplate().find(hql, o);
		if(list == null || list.size() < 1){
			return false;
		}
		boolean b = true;
		for(int i=0,s=list.size(); i<s; i++){
			if(this.resourceService.deleteResource(list.get(i), null) == false){
				b = false;
			}
		}
		
		if(b){
			String deletehql = "delete from ResSystemRepeat where rsyrRsdaId=" + rsdaId;
			getHibernateTemplate().bulkUpdate(deletehql);
		}
		return b;
	}

	/**
	 * 获取所有重复记录的资源ID
	 * @return
	 */
	@Override
	public List<Long> getAllRepeatRsdaId() {
		String hql = "select rsyrRsdaId from ResSystemRepeat";
		List<Long> list = getHibernateTemplate().find(hql);
		return list;
	}
	

}
