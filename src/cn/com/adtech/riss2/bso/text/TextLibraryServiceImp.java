package cn.com.adtech.riss2.bso.text;

import java.util.List;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.cache.PlatformCacheFactory;

import com.jbetter.framework.session.Pagination;

public class TextLibraryServiceImp extends CommonServiceImp implements ITextLibraryService {

    /**
     * 获取资源库列表
     */
    @Override
    public List<Object[]> getLibraryList(String sort, String dir, Pagination pagination) {
        StringBuffer sb = new StringBuffer();
        sb.append("select rtli.rtliId, rtli.rtliName, rtli.rtliIndex, rtli.rtliLanguage");
        sb.append(" from ResTextLibrary rtli");
        sb.append(" order by rtli.").append(sort).append(" ").append(dir);
        
        String hql = sb.toString();
        sb = null;
        
        List<Object[]> list = getPageHibernateTemplate().find(pagination, hql);
        return list;
    }

	@Override
	public void updateCache() {
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
				PlatformCacheFactory.instance().setTextstoreList(dataList);
			}	
		}.start();
	}

}
