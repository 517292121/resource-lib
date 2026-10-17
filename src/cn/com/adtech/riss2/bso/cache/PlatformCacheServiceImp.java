package cn.com.adtech.riss2.bso.cache;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import cn.com.adtech.riss2.bean.ResCountBean;
import cn.com.adtech.riss2.bean.ResourceForTypeBean;
import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.bso.platform.IIndexDataService;
import cn.com.adtech.riss2.bso.platform.PlatformBasicService;
import cn.com.adtech.riss2.cache.PlatformCacheFactory;
import cn.com.adtech.riss2.core.CommonUtil;

/**
 * 
 * 平台缓存数据统一管理
 *
 * @author MaChao
 *
 * @date：Jan 5, 2018
 */
public class PlatformCacheServiceImp extends CommonServiceImp implements IPlatformCacheService{
	
	@Resource(name="indexDataService")
	private IIndexDataService indexDataService;
	
	@Override
	public void updateMetastoreList() {
		StringBuffer sb = new StringBuffer();
		sb.append("select rmst.rmstId,");
		sb.append("rmst.rmstName,");
		sb.append("rmst.rmstRemark,");
		sb.append("rmst.rmstLogo");
		sb.append(" from ResMetaStore rmst");
		sb.append(" where rmst.rmstStatus=1");
		sb.append(" order by rmst.rmstIndexSort ASC");
		String hql = sb.toString();
		sb = null;
		List<Object[]> list = find(hql);
		CommonUtil.parserId(list);
		PlatformCacheFactory.instance().setMetastoreList(list);	
	}

	@Override
	public void updateResCount() {
		new Thread(){
			@Override
			public synchronized void run() {
				super.run();
				List<ResCountBean> countList = new ArrayList<ResCountBean>();
				String where = PlatformBasicService.getSearchCommonConditionByHql("rsda", false);
				String countHql = "select count(rsda.rsdaId) from ResSourceData rsda where " + where;
				String s = getSingleData(countHql);
				
				//如果资源总量为空，那就没有必要再按资源类型统计了
				if(!CommonUtil.isNotNull(s)){
					PlatformCacheFactory.instance().setResCountList(countList);
					return;
				}
				
				int resCount = Integer.parseInt(s);
				
				StringBuffer sb = new StringBuffer();
				sb.append("select rlty.rltyId,");
				sb.append("rlty.rltyName,");
				sb.append("(select count(rsda.rsdaId) from ResSourceData rsda where rlty.rltyId=rsda.rsdaRltyId and "+where+")");
				sb.append(" from ResLiterType rlty");
				String hql = sb.toString();
				sb = null;
				
				List<Object[]> dataList = getHibernateTemplate().find(hql);
				ResCountBean count = null;
				for(Object[] o : dataList){
					if(o == null || o.length < 3 || o[2] == null){
						continue;
					}
					int thisCount = Integer.parseInt(o[2].toString()); 
					if(thisCount < 1){
						continue;
					}
					String percent = getPercent(thisCount, resCount);
					count = new ResCountBean((Long) o[0], o[1].toString(), thisCount, percent);
					countList.add(count);
				}
				PlatformCacheFactory.instance().setResCountList(countList);
			}	
		}.start();
		
	}
	//求 两个数的百分比%
	private static String getPercent(int a, int b){
		NumberFormat numberFormat = NumberFormat.getInstance();  
        // 设置精确到小数点后2位  
        numberFormat.setMaximumFractionDigits(2);  
        String result = numberFormat.format((float) a / (float) b * 100);  
        return result;
	}

	
	
	@Override
	public void updateResforType() {
		new Thread(){
			@Override
			public synchronized void run() {
				super.run();
				List<ResourceForTypeBean> beanList = indexDataService.getRcmdResource();
				PlatformCacheFactory.instance().setResForTypeList(beanList);
			}	
		}.start();
	}

	@Override
	public void updateTextstoreList() {
		new Thread(){
			@Override
			public synchronized void run() {
				super.run();
				StringBuffer sb = new StringBuffer();
				sb.append("select rtli.rtliId,");
				sb.append("rtli.rtliName");
				sb.append(" from ResTextLibrary rtli");
				sb.append(" order by rtli.rtliIndex ASC");
				String hql = sb.toString();
				sb = null;
				
				List<Object[]> dataList = getHibernateTemplate().find(hql);
				CommonUtil.parserId(dataList);
				PlatformCacheFactory.instance().setTextstoreList(dataList);
			}	
		}.start();
	}
	
	@Override
	public void updateInfoClassList() {
		new Thread(){
			@Override
			public synchronized void run() {
				super.run();
				StringBuffer sb = new StringBuffer();
				sb.append("select idst.idstId,");
				sb.append("idst.idstName,");
				sb.append("idst.idstNumber");
				sb.append(" from IdsInformationType idst");
				sb.append(" order by idst.idstIndex ASC");
				String hql = sb.toString();
				sb = null;
				
				List<Object[]> dataList = getHibernateTemplate().find(hql);
				CommonUtil.parserId(dataList);
				PlatformCacheFactory.instance().setInfoClassList(dataList);
			}	
		}.start();
	}

	@Override
	public void updateAll() {
		updateMetastoreList();
		updateTextstoreList();
		updateResCount();
		updateResforType();
		updateInfoClassList();
	}

}
