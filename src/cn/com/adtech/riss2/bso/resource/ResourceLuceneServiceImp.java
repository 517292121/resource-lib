package cn.com.adtech.riss2.bso.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.com.adtech.riss2.bso.platform.PlatformBasicService;
import cn.com.adtech.riss2.controller.platform.ResCommonController;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.unionlib.lucene.bean.ResMainBean;
import cn.com.unionlib.lucene.dao.ILuceneDaoService;
import cn.com.unionlib.lucene.dao.IndexCommonService;
import cn.com.unionlib.lucene.dao.LuceneDaoServiceImp;

import com.jbetter.framework.session.Pagination;

/**
 * 
 * @description 资源的索引处理
 *
 * @author MaChao
 * 
 * @version 1.0
 *
 * @date Aug 17, 20155:16:17 PM
 *
 */
public class ResourceLuceneServiceImp extends IndexCommonService implements IResourceLuceneService{
		
	/**
	 * 批量加入多个资源到索引（资源部分属性模式）
	 *
	 * @author MaChao
	 * @date Aug 18, 2015
	 *
	 * @param resList
	 */
	private void addResourceToIndex(List<Object[]> resList) {
		List<ResMainBean> beanList = new ArrayList<ResMainBean>();
		ResMainBean bean = null;
		for(int i=0,s=resList.size(); i<s; i++){
			bean = resourceToDatabean(resList.get(i));
			beanList.add(bean);
		}
		ILuceneDaoService luceneService = new LuceneDaoServiceImp();
		luceneService.addMultiDatabean(beanList);
	}
	

	/**
	 * 根据作业ID批量添加资源到索引
	 *
	 * @author MaChao
	 * @date Aug 18, 2015
	 *
	 * @param rminId
	 */
	@Override
	public void addIndexByMission(HttpServletRequest request, Long rminId) {
		if(rminId == null || rminId < 1L){
			return ;
		}
		String commonCon = PlatformBasicService.getSearchCommonConditionByHql("rsda", false);
		commonCon += " and rsda.rsdaRminId=" + rminId;
		
		//先取出总数
		String countHql = "select count(rsda.rsdaId) from ResSourceData rsda where " + commonCon;
		int countNum = 0;
		try {
			String countStr = getSingleData(countHql);
			countNum = Integer.parseInt(countStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(countNum < 1){
			System.out.println("没有可创建索引的资源——————————");
			return ;
		}
		System.out.println("一共有：" + countNum + " 条资源准备创建索引--------------------------------");
		
		//为了提高效果，只查询出了资源需要创建成索引的字段，如果需要添加字段根据情况扩展即可；
		String columns = getSearchColumn();
		String hql = "select " + columns + " from ResSourceData rsda left join rsda.resSubjectType rsty where " + commonCon;
		
		//考虑到库里面的资源可能会较多，因此不要一次性查出来进行操作；需采用按批次操作，如同分页
		int pageNum = (countNum - 1) / BATCH_SIZE + 1; //一共多少批次
		Pagination p = null;
		try {
			for(int i=0; i<pageNum; i++){
				p = new Pagination();
				p.setCurrentPage(i);
				p.setPageResults(BATCH_SIZE);
				List<Object[]> resList = getPageHibernateTemplate().find(p, hql);
				
				//封面按照新的模式进行重新解析
				resList = ResCommonController.parseCoverList(request, resList, 9, 11, 12);
				
				addResourceToIndex(resList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改资源的索引 
	 *
	 * @author MaChao
	 * @date Aug 18, 2015
	 *
	 * @param resource
	 */
	@Override
	public void updateResourceToIndex(ResSourceData resource) {
		if(resource == null || resource.getRsdaId() == null){
			return;
		}
		ResMainBean dataBean = resourceToDatabean(resource);
		ILuceneDaoService luceneService = new LuceneDaoServiceImp();
		luceneService.updateDatabean(dataBean);
	}

	/**
	 * 根据资源ID删除索引
	 *
	 * @author MaChao
	 * @date Aug 18, 2015
	 *
	 * @param rsdaId
	 */
	@Override
	public void deleteIndexById(Long rsdaId) {
		if(rsdaId == null){
			return;
		}
		ILuceneDaoService luceneService = new LuceneDaoServiceImp();
		luceneService.deleteDatabean(rsdaId.toString());
	}

	
	private ResMainBean resourceToDatabean(ResSourceData resource) {
		if (resource == null) {
			return null;
		}
		try {
			Long rsdaId = resource.getRsdaId();
			//获取资源预览模式。
			int viewType = resource.getRsdaSourceType() == null ? 0 : resource.getRsdaSourceType(); 
			
			String srcFile = CommonUtil.nullToBlank(resource.getRsdaFileLocalName());
			String srcDir = CommonUtil.nullToBlank(resource.getRsdaFileUrl());
			
			//解析资源全文（主索引）
			ResMainBean bean = parseSrcFile(rsdaId, viewType, srcFile, srcDir);
			
			bean.setTitle(CommonUtil.nullToBlank(resource.getRsdaTitle()));
			bean.setCreator(CommonUtil.nullToBlank(resource.getRsdaCreator()));
			bean.setPublisher(CommonUtil.nullToBlank(resource.getRsdaPublisher()));
			bean.setKeyword(CommonUtil.nullToBlank(resource.getRsdaKeyword()));
			bean.setDate(CommonUtil.nullToBlank(resource.getRsdaDate()));
			bean.setResType(CommonUtil.nullToBlank(resource.getResLiterType().getRltyName()));
			bean.setResLibrary(CommonUtil.nullToBlank(resource.getResMetaStore().getRmstName()));
			if(resource.getResSubjectType() == null){
				bean.setSubjectClass("");
			}else{
				bean.setSubjectClass(resource.getResSubjectType().getRstyName());
			}
			bean.setCoverUrl(resource.getRsdaBigimgUrl());
			bean.setDataType(ResMainBean.DATA_TYPE_RES);
			
			//语种
			bean.setLanguage(getFormatLan(resource.getRsdaLanguage()));
			
			return bean;
		} catch (Exception e) {
			System.out.println("封装对象为lucene对象出错：" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 删除作业中所有资源的索引
	 * @throws IOException 
	 */
	@Override
	public void deleteMissionIndex(Long rminId) throws IOException {
		if(rminId == null || rminId < 1L){
			return ;
		}
		String commonCon = PlatformBasicService.getSearchCommonConditionByHql("rsda", false);
		commonCon += " and rsda.rsdaRminId=" + rminId;
		
		//先取出总数
		String countHql = "select count(rsda.rsdaId) from ResSourceData rsda where " + commonCon;
		int countNum = 0;
		try {
			String countStr = getSingleData(countHql);
			countNum = Integer.parseInt(countStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(countNum < 1){
			System.out.println("没有可删除索引的资源——————————");
			return ;
		}
		System.out.println("一共有：" + countNum + " 条资源准备删除索引--------------------------------");
		
		//为了提高效果，只查询出了资源需要创建成索引的字段，如果需要添加字段根据情况扩展即可；
		String hql = "select rsda.rsdaId from ResSourceData rsda where " + commonCon;
		
		//考虑到库里面的资源可能会较多，因此不要一次性查出来进行操作；需采用按批次操作，如同分页
		int pageNum = (countNum - 1) / BATCH_SIZE + 1; //一共多少批次
		Pagination p = null;
		ILuceneDaoService luceneService = new LuceneDaoServiceImp();
		List<String> idlist = null;
		for(int i=0; i<pageNum; i++){
			p = new Pagination();
			p.setCurrentPage(i);
			p.setPageResults(BATCH_SIZE);
			List<Long> resList = getPageHibernateTemplate().find(p, hql);
			
			idlist = new ArrayList<String>();
			for(Long id : resList){
				idlist.add(id.toString());
			}
			luceneService.deleteBatchData(idlist);
		}
	}
}
