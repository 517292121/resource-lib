package cn.com.adtech.riss2.bso.resource;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bean.ResourceRepeatBean;
import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceRepeat;

public class ResourceRepeatServiceImp extends CommonServiceImp implements IResourceRepeatService {
	
	/**全局查重状态开关，查重中为true,未查重时为false */
	public static boolean REPEAT_STATE = false;
	
	/**当前查重开关，当为false时退出查重*/
	private boolean repeatOpen = true;
	
	//已完成查重资源数量
	private float progress = 0;
	//查重的资源总数
	private float counter = 0;
	
	/**
	 * 资源查重入口函数
	 * @param resourceRepeatBean 封面查重条件和参数的bean对象
	 */
	@Override
	public void saveResourceRepeat(ResourceRepeatBean resourceRepeatBean) {
		REPEAT_STATE = true; //打开查重开关
		repeatOpen = true;
		progress = 0;
		counter = 0;
		
		//因为ResSourceRepeat表只是临时存放查重结果，所以查重前先将之前的数据清空。
		if(resourceRepeatBean.getRminId() != null && resourceRepeatBean.getRminId() > 0){
			String hql = "delete from ResSourceRepeat where rsreRminId=" + resourceRepeatBean.getRminId();
			getHibernateTemplate().bulkUpdate(hql);
		}else if (resourceRepeatBean.getRsdaIdArr() != null && resourceRepeatBean.getRsdaIdArr().length > 0){
			deleteResourceRepeatByIdArr(resourceRepeatBean.getRsdaIdArr());
		}
		
		//待查重的资源集合
		List<ResSourceData> repeatReourceList = resourceRepeatBean.getRepeatResourceList();
		counter = repeatReourceList.size(); //查重总数
		ResSourceData resource = null;
		ResSourceRepeat resourceRepeat = null;
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query;
		String basicHql = "select count(rsda.rsdaId) from ResSourceData rsda where rsdaStatus = " + ResSourceData.STATUS_PASS;
//		if(resourceRepeatBean.getRsdaStatus() > 0){
//			basicHql += " and rsda.rsdaStatus=" + resourceRepeatBean.getRsdaStatus();
//		}
		String queryHql = "";
		String resultHql = "";
		String allHql = "";
		List<Long> result;
		List<Object> params = null;
		boolean showAll = resourceRepeatBean.isRepeatAll();
		try {
			//循环查重所有资源
			for(int i=0,s=repeatReourceList.size(); i<s; i++){
				if(repeatOpen == false){
					break;
				}
				progress = i;
				resource = repeatReourceList.get(i);
				params = new LinkedList<Object>();
				
				//查重结果对象
				resourceRepeat = new ResSourceRepeat();
				resourceRepeat.setRsreRminId(resourceRepeatBean.getRminId());
				resourceRepeat.setRsreRsdaId(resource.getRsdaId());
				resourceRepeat.setRsreRsdaTitle(resource.getRsdaTitle());
				
				//设置查询语句和基本条件
				queryHql = basicHql + " and rsda.rsdaId != " + resource.getRsdaId();
				allHql = queryHql; 
				
				//查重标题
				if(resourceRepeatBean.isCheckTitle()){
					if(showAll){
						resultHql = queryHql + " and rsda.rsdaTitle=? ";
						query = session.createQuery(resultHql);
						query.setParameter(0, resource.getRsdaTitle());
						try {
							result = query.list();
							resourceRepeat.setRsreTitle(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsreTitle(null);
						}
					}
					
					//加入全部匹配参数
					allHql += " and rsda.rsdaTitle=? ";
					params.add(resource.getRsdaTitle());
				}
				
				//查重出版者
				if(resourceRepeatBean.isCheckPublisher()){
					if(showAll){
						if(resource.getRsdaPublisher() == null){
							resultHql = queryHql + " and rsda.rsdaPublisher is null ";
							query = session.createQuery(resultHql);
						}else{
							resultHql = queryHql + " and rsda.rsdaPublisher=? ";
							query = session.createQuery(resultHql);
							query.setParameter(0, resource.getRsdaPublisher());
						}
						try {
							result = query.list();
							resourceRepeat.setRsrePublisher(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsrePublisher(null);
						}
					}
					//加入全部匹配参数
					if(resource.getRsdaPublisher() == null){
						allHql += " and rsda.rsdaPublisher is null ";
					}else{
						allHql += " and rsda.rsdaPublisher=? ";
						params.add(resource.getRsdaPublisher());
					}
				}
				
				//查重资源类型
				if(resourceRepeatBean.isCheckFiletype()){
					if(showAll){
						if(resource.getRsdaFileType() == null){
							resultHql = queryHql + " and rsda.rsdaFileType is null ";
							query = session.createQuery(resultHql);
						}else{
							resultHql = queryHql + " and rsda.rsdaFileType=? ";
							query = session.createQuery(resultHql);
							query.setParameter(0, resource.getRsdaFileType());
						}
						try {
							result = query.list();
							resourceRepeat.setRsreFileType(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsreFileType(null);
						}
					}
					//加入全部匹配参数
					if(resource.getRsdaFileType() == null){
						allHql += " and rsda.rsdaFileType is null ";
					}else{
						allHql += " and rsda.rsdaFileType=? ";
						params.add(resource.getRsdaFileType());
					}
				}
				
				//查重资源大小
				if(resourceRepeatBean.isCheckFilesize()){
					if(showAll){
						if(resource.getRsdaFileSize() == null){
							resultHql = queryHql + " and rsda.rsdaFileSize is null ";
							query = session.createQuery(resultHql);
						}else{
							resultHql = queryHql + " and rsda.rsdaFileSize=? ";
							query = session.createQuery(resultHql);
							query.setParameter(0, resource.getRsdaFileSize());
						}
						try {
							result = query.list();
							resourceRepeat.setRsreFileSize(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsreFileSize(null);
						}
					}
					//加入全部匹配参数
					if(resource.getRsdaFileSize() == null){
						allHql += " and rsda.rsdaFileSize is null ";
					}else{
						allHql += " and rsda.rsdaFileSize=? ";
						params.add(resource.getRsdaFileSize());
					}
				}
				
				//查重资源主题
				if(resourceRepeatBean.isCheckSubject()){
					if(showAll){
						if(resource.getRsdaSubject() == null){
							resultHql = queryHql + " and rsda.rsdaSubject is null ";
							query = session.createQuery(resultHql);
						}else{
							resultHql = queryHql + " and rsda.rsdaSubject=? ";
							query = session.createQuery(resultHql);
							query.setParameter(0, resource.getRsdaSubject());
						}
						try {
							result = query.list();
							resourceRepeat.setRsreSubject(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsreSubject(null);
						}
					}
					//加入全部匹配参数
					if(resource.getRsdaSubject() == null){
						allHql += " and rsda.rsdaSubject is null ";
					}else{
						allHql += " and rsda.rsdaSubject=? ";
						params.add(resource.getRsdaSubject());
					}
				}
				
				//查重资源描述
				if(resourceRepeatBean.isCheckDescription()){
					if(showAll){
						if(resource.getRsdaDescription() == null){
							resultHql = queryHql + " and rsda.rsdaDescription is null ";
							query = session.createQuery(resultHql);
						}else{
							resultHql = queryHql + " and rsda.rsdaDescription=? ";
							query = session.createQuery(resultHql);
							query.setParameter(0, resource.getRsdaDescription());
						}
						try {
							result = query.list();
							resourceRepeat.setRsreDescription(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsreDescription(null);
						}
					}
					//加入全部匹配参数
					if(resource.getRsdaDescription() == null){
						allHql += " and rsda.rsdaDescription is null ";
					}else{
						allHql += " and rsda.rsdaDescription=? ";
						params.add(resource.getRsdaDescription());
					}
				}
				
				//查重资源来源
				if(resourceRepeatBean.isCheckSource()){
					if(showAll){
						if(resource.getRsdaSource() == null){
							resultHql = queryHql + " and rsda.rsdaSource is null ";
							query = session.createQuery(resultHql);
						}else{
							resultHql = queryHql + " and rsda.rsdaSource=? ";
							query = session.createQuery(resultHql);
							query.setParameter(0, resource.getRsdaSource());
						}
						try {
							result = query.list();
							resourceRepeat.setRsreSource(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsreSource(null);
						}
					}
					//加入全部匹配参数
					if(resource.getRsdaSource() == null){
						allHql += " and rsda.rsdaSource is null ";
					}else{
						allHql += " and rsda.rsdaSource=? ";
						params.add(resource.getRsdaSource());
					}
				}
				
				//查重资源语种
				if(resourceRepeatBean.isCheckLanguage()){
					if(showAll){
						if(resource.getRsdaLanguage() == null){
							resultHql = queryHql + " and rsda.rsdaLanguage is null ";
							query = session.createQuery(resultHql);
						}else{
							resultHql = queryHql + " and rsda.rsdaLanguage=? ";
							query = session.createQuery(resultHql);
							query.setParameter(0, resource.getRsdaLanguage());
						}
						try {
							result = query.list();
							resourceRepeat.setRsreLanguage(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsreLanguage(null);
						}
					}
					//加入全部匹配参数
					if(resource.getRsdaLanguage() == null){
						allHql += " and rsda.rsdaLanguage is null ";
					}else{
						allHql += " and rsda.rsdaLanguage=? ";
						params.add(resource.getRsdaLanguage());
					}
				}
				
				//查重资源关联
				if(resourceRepeatBean.isCheckRelation()){
					if(showAll){
						if(resource.getRsdaRelation() == null){
							resultHql = queryHql + " and rsda.rsdaRelation is null ";
							query = session.createQuery(resultHql);
						}else{
							resultHql = queryHql + " and rsda.rsdaRelation=? ";
							query = session.createQuery(resultHql);
							query.setParameter(0, resource.getRsdaRelation());
						}
						try {
							result = query.list();
							resourceRepeat.setRsreRelation(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsreRelation(null);
						}
					}
					//加入全部匹配参数
					if(resource.getRsdaRelation() == null){
						allHql += " and rsda.rsdaRelation is null ";
					}else{
						allHql += " and rsda.rsdaRelation=? ";
						params.add(resource.getRsdaRelation());
					}
				}
				
				//查重资源覆盖范围
				if(resourceRepeatBean.isCheckCoverage()){
					if(showAll){
						if(resource.getRsdaCoverage() == null){
							resultHql = queryHql + " and rsda.rsdaCoverage is null ";
							query = session.createQuery(resultHql);
						}else{
							resultHql = queryHql + " and rsda.rsdaCoverage=? ";
							query = session.createQuery(resultHql);
							query.setParameter(0, resource.getRsdaCoverage());
						}
						try {
							result = query.list();
							resourceRepeat.setRsreCoverage(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsreCoverage(null);
						}
					}
					//加入全部匹配参数
					if(resource.getRsdaCoverage() == null){
						allHql += " and rsda.rsdaCoverage is null ";
					}else{
						allHql += " and rsda.rsdaCoverage=? ";
						params.add(resource.getRsdaCoverage());
					}
				}
				
				//查重资源创建者
				if(resourceRepeatBean.isCheckCreator()){
					if(showAll){
						if(resource.getRsdaCreator() == null){
							resultHql = queryHql + " and rsda.rsdaCreator is null ";
							query = session.createQuery(resultHql);
						}else{
							resultHql = queryHql + " and rsda.rsdaCreator=? ";
							query = session.createQuery(resultHql);
							query.setParameter(0, resource.getRsdaCreator());
						}
						try {
							result = query.list();
							resourceRepeat.setRsreCreator(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsreCreator(null);
						}
					}
					//加入全部匹配参数
					if(resource.getRsdaCreator() == null){
						allHql += " and rsda.rsdaCreator is null ";
					}else{
						allHql += " and rsda.rsdaCreator=? ";
						params.add(resource.getRsdaCreator());
					}
				}
				
				//查重资源其它责任者
				if(resourceRepeatBean.isCheckContributor()){
					if(showAll){
						if(resource.getRsdaContributor() == null){
							resultHql = queryHql + " and rsda.rsdaContributor is null ";
							query = session.createQuery(resultHql);
						}else{
							resultHql = queryHql + " and rsda.rsdaContributor=? ";
							query = session.createQuery(resultHql);
							query.setParameter(0, resource.getRsdaContributor());
						}
						try {
							result = query.list();
							resourceRepeat.setRsreContributor(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsreContributor(null);
						}
					}
					//加入全部匹配参数
					if(resource.getRsdaContributor() == null){
						allHql += " and rsda.rsdaContributor is null ";
					}else{
						allHql += " and rsda.rsdaContributor=? ";
						params.add(resource.getRsdaContributor());
					}
				}
				
				//查重资源权限
				if(resourceRepeatBean.isCheckRights()){
					if(showAll){
						if(resource.getRsdaRights() == null){
							resultHql = queryHql + " and rsda.rsdaRights is null ";
							query = session.createQuery(resultHql);
						}else{
							resultHql = queryHql + " and rsda.rsdaRights=? ";
							query = session.createQuery(resultHql);
							query.setParameter(0, resource.getRsdaRights());
						}
						try {
							result = query.list();
							resourceRepeat.setRsreRights(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsreRights(null);
						}
					}
					//加入全部匹配参数
					if(resource.getRsdaRights() == null){
						allHql += " and rsda.rsdaRights is null ";
					}else{
						allHql += " and rsda.rsdaRights=? ";
						params.add(resource.getRsdaRights());
					}
				}
				
				//查重资源日期
				if(resourceRepeatBean.isCheckDate()){
					if(showAll){
						if(resource.getRsdaDate() == null){
							resultHql = queryHql + " and rsda.rsdaDate is null ";
							query = session.createQuery(resultHql);
						}else{
							resultHql = queryHql + " and rsda.rsdaDate=? ";
							query = session.createQuery(resultHql);
							query.setParameter(0, resource.getRsdaDate());
						}
						try {
							result = query.list();
							resourceRepeat.setRsreDate(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsreDate(null);
						}
					}
					//加入全部匹配参数
					if(resource.getRsdaDate() == null){
						allHql += " and rsda.rsdaDate is null ";
					}else{
						allHql += " and rsda.rsdaDate=? ";
						params.add(resource.getRsdaDate());
					}
				}
				
				//查重资源类型
				if(resourceRepeatBean.isCheckType()){
					if(showAll){
						if(resource.getRsdaType() == null){
							resultHql = queryHql + " and rsda.rsdaType is null ";
							query = session.createQuery(resultHql);
						}else{
							resultHql = queryHql + " and rsda.rsdaType=? ";
							query = session.createQuery(resultHql);
							query.setParameter(0, resource.getRsdaType());
						}
						try {
							result = query.list();
							resourceRepeat.setRsreType(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsreType(null);
						}
					}
					//加入全部匹配参数
					if(resource.getRsdaType() == null){
						allHql += " and rsda.rsdaType is null ";
					}else{
						allHql += " and rsda.rsdaType=? ";
						params.add(resource.getRsdaType());
					}
				}
				
				//查重资源格式
				if(resourceRepeatBean.isCheckFormat()){
					if(showAll){
						if(resource.getRsdaFormat() == null){
							resultHql = queryHql + " and rsda.rsdaFormat is null ";
							query = session.createQuery(resultHql);
						}else{
							resultHql = queryHql + " and rsda.rsdaFormat=? ";
							query = session.createQuery(resultHql);
							query.setParameter(0, resource.getRsdaFormat());
						}
						try {
							result = query.list();
							resourceRepeat.setRsreFormat(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsreFormat(null);
						}
					}
					//加入全部匹配参数
					if(resource.getRsdaFormat() == null){
						allHql += " and rsda.rsdaFormat is null ";
					}else{
						allHql += " and rsda.rsdaFormat=? ";
						params.add(resource.getRsdaFormat());
					}
				}
				
				//查重资源标识符
				if(resourceRepeatBean.isCheckIdentifier()){
					if(showAll){
						if(resource.getRsdaIdentifier() == null){
							resultHql = queryHql + " and rsda.rsdaIdentifier is null ";
							query = session.createQuery(resultHql);
						}else{
							resultHql = queryHql + " and rsda.rsdaIdentifier=? ";
							query = session.createQuery(resultHql);
							query.setParameter(0, resource.getRsdaIdentifier());
						}
						try {
							result = query.list();
							resourceRepeat.setRsreIdentifier(result.get(0) == null ? 0 : result.get(0));
						} catch (Exception e) {
							logger.error(e);
							resourceRepeat.setRsreIdentifier(null);
						}
					}
					//加入全部匹配参数
					if(resource.getRsdaIdentifier() == null){
						allHql += " and rsda.rsdaIdentifier is null ";
					}else{
						allHql += " and rsda.rsdaIdentifier=? ";
						params.add(resource.getRsdaIdentifier());
					}
				}
				
				//查询总的匹配数
				query = session.createQuery(allHql);
				for(int p=0,ps=params.size(); p<ps; p++){
					query.setParameter(p, params.get(p));
				}
				try {
					result = query.list();
					resourceRepeat.setRsreRepeatAll(result.get(0) == null ? 0 : result.get(0));
				} catch (Exception e) {
					logger.error(e);
					resourceRepeat.setRsreRepeatAll(null);
				}
				
				session.save(resourceRepeat);
				
//				Thread.sleep(1000);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		
		REPEAT_STATE = false; //查重完成后关闭查重开关		
	}

	private void deleteResourceRepeatByIdArr(Long[] rsdaIdArr){
		String hql;
		for(int i=0; i<rsdaIdArr.length; i++){
			hql = "delete from ResSourceRepeat where rsreRsdaId = " + rsdaIdArr[i];
			getHibernateTemplate().bulkUpdate(hql);
		}
	}

	/**
	 * 根据以逗号分类的ID字符串查询对应的资源集合
	 * @param ids 以逗号分类的ID字符串
	 * @return
	 */
	@Override
	public List<ResSourceData> getResourceDataByIdArr(String ids) {
		String hql = "from ResSourceData where rsdaId in ("+ids+")";
		List<ResSourceData> sourceList = getHibernateTemplate().find(hql);
		return sourceList;
	}

	/**
	 * 获取资源已查重的百分比
	 * @return
	 */
	@Override
	public int getProgress() {
		float p = progress / counter;
		p = p * 100;
		int result = Math.round(p);
		return result * 2;
	}

	/**
	 * 根据作业获取查重结果集合
	 * @return
	 */
	@Override
	public List<ResSourceRepeat> getRepeatByMission(Pagination pagination,
			Long rminId, String orderColumn, String orderType, String rsdaTitle,boolean showall) {
		List<ResSourceRepeat> list = null;
		String hql = "select rsre.rsreId, rsre.rsreRminId, rsre.rsreRsdaId,rsre.rsreRsdaTitle, rsre.rsreTitle, rsre.rsrePublisher, rsre.rsreFileSize, rsre.rsreFileType,rsre.rsreSubject,rsre.rsreDescription,rsre.rsreSource,rsre.rsreLanguage,rsre.rsreRelation,rsre.rsreCoverage,rsre.rsreCreator,rsre.rsreContributor,rsre.rsreRights,rsre.rsreDate,rsre.rsreType,rsre.rsreFormat,rsre.rsreIdentifier, rsre.rsreRepeatAll from ResSourceRepeat rsre where rsre.rsreRminId="+rminId;
		if(showall == false){
			hql += " and rsre.rsreRepeatAll > 0  ";
		}
		
		if(CommonUtil.isNotNull(rsdaTitle)){
			hql += " and rsre.rsreRsdaTitle like ? order by rsre."+orderColumn+" " + orderType;
			list = getPageHibernateTemplate().find(pagination, hql, "%" + rsdaTitle + "%");
		}else{
			hql += " order by rsre."+orderColumn+" " + orderType;
			list = getPageHibernateTemplate().find(pagination, hql);
		}
		return list;
	}

	/**
	 * 根据资源id获取查重结果集合
	 * @return
	 */
	@Override
	public List<ResSourceRepeat> getRepeatByResources(String rsdaIds, String orderColumn, String orderType, String rsdaTitle,boolean showall) {
		List<ResSourceRepeat> list = null;
		String hql = "select rsre.rsreId,rsre.rsreRminId, rsre.rsreRsdaId,rsre.rsreRsdaTitle, rsre.rsreTitle, rsre.rsrePublisher, rsre.rsreFileSize, rsre.rsreFileType,rsre.rsreSubject,rsre.rsreDescription,rsre.rsreSource,rsre.rsreLanguage,rsre.rsreRelation,rsre.rsreCoverage,rsre.rsreCreator,rsre.rsreContributor,rsre.rsreRights,rsre.rsreDate,rsre.rsreType,rsre.rsreFormat,rsre.rsreIdentifier, rsre.rsreRepeatAll from ResSourceRepeat rsre where rsre.rsreRsdaId in ("+rsdaIds+")"; 
		if(showall == false){
			hql += " and rsre.rsreRepeatAll > 0  ";
		}
		
		if(CommonUtil.isNotNull(rsdaTitle)){
			hql += " and rsre.rsreRsdaTitle like ? order by rsre."+orderColumn+" " + orderType;
			list = getHibernateTemplate().find(hql, "%" + rsdaTitle + "%");
		}else{
			hql += " order by rsre."+orderColumn+" " + orderType;
			list = getHibernateTemplate().find(hql);
		}		
		return list;
	}

	/**
	 * 根据查重的某个元素获取所重复的值
	 * 
	 * @param pagination
	 *            分页对象
	 * @param selfRsdaId
	 *            资源本身ID
	 * @param eleColumn
	 *            所匹配的元素名称
	 * @param rsdaState
	 *            查重范围(资源状态)
	 * @param orderColumn
	 *            排序列
	 * @param orderType
	 *            排序类型
	 * @return
	 */
	@Override
	public List<Object[]> getRepeatResourceByElement(Pagination pagination,
			Long selfRsdaId, String eleColumn,
			String orderColumn, String orderType) {
		String hql = "select rsda."+eleColumn+" from ResSourceData rsda where rsda.rsdaId=" + selfRsdaId;
		List<Object> list = getHibernateTemplate().find(hql);
		Object data = null;
		if(list != null && list.size() > 0){
			try {
				data = list.get(0);
			} catch (Exception e) {
				e.printStackTrace();
				data = null;
			}
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("select rsda.rsdaId, rsda.rsdaTitle, rsda.resMetaStore.rmstName, rsda.resLiterType.rltyName from ResSourceData rsda ");
		buffer.append(" where rsda.rsdaId != ").append(selfRsdaId); //查询除自己之外的资源
		
		if(data == null){
			buffer.append(" and rsda."+eleColumn+" is null "); //值等于查重的值
		}else{
			buffer.append(" and rsda."+eleColumn+" = ?"); //值等于查重的值
		}
		
		buffer.append(" and rsda.rsdaStatus=").append(ResSourceData.STATUS_PASS);
		buffer.append(" order by rsda.").append(orderColumn);
		buffer.append(" ").append(orderType);
		
		hql = buffer.toString();
		buffer = null;
		
		List<Object[]> datalist;
		if(data == null){
			datalist = getPageHibernateTemplate().find(pagination, hql);
		}else{
			datalist = getPageHibernateTemplate().find(pagination, hql,data);
		}
		return datalist;
	}

	/**
	 * 查询全部匹配的资源
	 * 
	 * @param pagination
	 *            分页对象
	 * @param repeatBean
	 *            查重条件bean
	 * @param selfRsdaId
	 *            资源本身ID
	 * @param orderColumn
	 *            排序列
	 * @param orderType
	 *            排序类型
	 * @return
	 */
	@Override
	public List<Object[]> getRepeatResourceByAll(Pagination pagination,
			ResourceRepeatBean repeatBean, Long selfRsdaId, String orderColumn,
			String orderType, int haveSelf) {
		ResSourceData resource = (ResSourceData) get(ResSourceData.class, selfRsdaId);
		if(resource == null){
			return null;
		}
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer buffer = new StringBuffer();
		buffer.append("select rsda.rsdaId, rsda.rsdaTitle, rsda.resMetaStore.rmstName, rsda.resLiterType.rltyName from ResSourceData rsda ");
		buffer.append(" where rsda.rsdaStatus=").append(ResSourceData.STATUS_PASS);
		if(haveSelf < 1){
			buffer.append(" and rsda.rsdaId != ").append(selfRsdaId); //查询除自己之外的资源
		}
		
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
		buffer.append(" order by rsda.").append(orderColumn);
		buffer.append(" ").append(orderType);
		
		Object[] o = params.toArray();
		String hql = buffer.toString();
		params = null;
		buffer = null;
		
		List<Object[]> list = getPageHibernateTemplate().find(pagination, hql, o);
		return list;
	}

	/**
	 * 获取自定义分类名称，主要用于查看资源详细信息时
	 * @param rdtyId
	 * @return
	 */
	@Override
	public String getDynamicName(Long rdtyId) {
		if(rdtyId == null || rdtyId < 1L){
			return "";
		}
		String hql = "select rdty.rdtyName from ResDynamicType rdty where rdty.rdtyId=" + rdtyId;
		String rdtyName = getSingleData(hql);
		return rdtyName;
	}

	/**
	 * 清空所有查重数据
	 */
	@Override
	public void deleteAllRepeat() {
		String hql = "delete from ResSourceRepeat";
		getHibernateTemplate().bulkUpdate(hql);		
	}

	/**
	 * 设置查重局部开关，主要用于结束查重
	 * @param repeatOpen
	 */
	public void setRepeatOpen(boolean repeatOpen) {
		this.repeatOpen = repeatOpen;
	}
	

	/**
	 * 删除重复的资源，本身
	 * @param rminId
	 * @param rsdaIdArr
	 * @return
	 */
	@Override
	public boolean deleteRepeatResource(Long rminId, Long[] rsdaIdArr, IResourceDataService resourceService) {
		
		boolean b = true;
		String hql = "";
		if(rsdaIdArr != null && rsdaIdArr.length > 0){
			for(int i=0; i<rsdaIdArr.length; i++){
				if(resourceService.deleteResource(rsdaIdArr[i], null)){ //删除资源
					hql = "delete from ResSourceRepeat where rsreRsdaId=" + rsdaIdArr[i];
					getHibernateTemplate().bulkUpdate(hql); //删除查重记录
				}else{
					b = false;
				}
			}
		}else if (rminId != null && rminId > 0L){
			hql = "select rsreRsdaId from ResSourceRepeat where rsreRminId=? and rsreRepeatAll>0";
			List list = getHibernateTemplate().find(hql,rminId);
			if(list == null || list.size() < 1){
				return false;
			}
			Long rsdaId;
			for(int i=0,s=list.size(); i<s; i++){
				rsdaId = (Long) list.get(i);
				if(resourceService.deleteResource(rsdaId, null)){ //删除资源
					hql = "delete from ResSourceRepeat where rsreRsdaId=" + rsdaId;
					getHibernateTemplate().bulkUpdate(hql); //删除查重记录
				}else{
					b = false;
				}
			}
		}
		return b;
	}
	

}
