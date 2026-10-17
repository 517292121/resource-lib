package cn.com.adtech.riss2.bso.resource;

import java.io.File;
import java.util.List;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.DbmsBakHistory;

import com.jbetter.framework.session.Pagination;

public class DataBakServiceImp extends CommonServiceImp implements
		IDataBakService {

	@Override
	public boolean dataBak(String exec, DbmsBakHistory dbms) {
		boolean b = false;
		try {
//			Runtime.getRuntime().exec(exec);
			getHibernateTemplate().save(dbms);
			b = true;
		} catch (Exception e) {
			b=false;
		} 
		return b;
	}

	@Override
	public boolean deleteDataBakByDbmsId(Long dbmsId) {
		boolean b = false;
		DbmsBakHistory dbms = (DbmsBakHistory)getHibernateTemplate().get(DbmsBakHistory.class, dbmsId);
		String path = SysConfigInfo.DATA_BAK_DIR + dbms.getDbmsFilename();
		File file = new File(path);
		file.delete();
		try{
			getHibernateTemplate().delete(dbms);
			b =true;
		}catch(Exception e){
			b =false;
		}
		return b;
	}

	@Override
	public String getDataBakByJson(Pagination pagination, String orderColumn,
			String orderType) {
		
		String hql = "select dbmsId,dbmsFilename,dbmsUrlpath,dbmsBakdate from DbmsBakHistory";
		hql+=" order by "+orderColumn+" "+orderType;
		
		List<Object> list = getPageHibernateTemplate().find(hql);
		
		String[] keys = {"dbmsId","dbmsFilename","dbmsUrlpath","dbmsBakdate"};
		
		String json = CommonUtil.createJsonTable(list, keys, pagination.getTotalResults()+"");
		
		return json;
	}

}
