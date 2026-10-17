package cn.com.adtech.riss2.bso.sc;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.core.ServiceLocator;
import cn.com.adtech.riss2.domain.ResFilesystem;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;

public class ResFilesystemServiceImp extends CommonServiceImp implements IResFilesystemService {

	public boolean deleteResFilesystem(long id) {
		try {
			ResFilesystem system = (ResFilesystem) getHibernateTemplate().load(ResFilesystem.class, id);
			getHibernateTemplate().delete(system);
			initFileServiceMap();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public ResFilesystem getFilesystem(Long id) {
		if(id == null || id.equals(0)){
			return null;
		}
		ResFilesystem system = (ResFilesystem) getHibernateTemplate().get(ResFilesystem.class, id);
		return system;
	}

	public String getFilesystemJson(int begin, int pageNum, String orderColumn,
			String orderType) {
		String sql = "select * from RES_FILESYSTEM order by "+orderColumn + " "  + orderType ;
		String jsondata = getListToJson(sql, null, begin, pageNum);
		return jsondata;
	}

	public ResFilesystem saveOrUpdateFilesystem(ResFilesystem filesystem) {
		filesystem = (ResFilesystem) save(filesystem);
		initFileServiceMap();
		return filesystem;
	}

	@Override
	public Serializable saveFilesystem(ResFilesystem filesystem) {
		Serializable s = getHibernateTemplate().save(filesystem);
		initFileServiceMap();
		return s;
	}

	@Override
	public boolean updateFilesystem(ResFilesystem filesystem) {
		try {
			getHibernateTemplate().update(filesystem);
			initFileServiceMap();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getAllFileServiceOption(Long selectid){
		List<ResFilesystem> list = getHibernateTemplate().find("select new ResFilesystem(rf.rfilId, rf.rfilName) from ResFilesystem rf order by rf.rfilNum");
		if(list == null || list.size() < 1){
			return "<option value='0'>本地服务器...</option>";
		}
		StringBuffer buffer = new StringBuffer();
		String selecting = (selectid == null || selectid.equals(0)) ? "selected=true" : ""; 
		buffer.append("<option value='0' "+selecting+">本地服务器...</option>");
		for(int i=0,s=list.size(); i<s; i++){
			buffer.append("<option value='"+list.get(i).getRfilId()+"' ");
			if(list.get(i).getRfilId().equals(selectid)){
				buffer.append("selected=true");
			}
			buffer.append(">"+list.get(i).getRfilName()+"</option>\n");
		}
		
		String result = buffer.toString();
		buffer = null;
		list = null;
		return result;
	}
	
	public String getAllFileServiceOptionJson(){
		List<ResFilesystem> list = getHibernateTemplate().find("select new ResFilesystem(rf.rfilId, rf.rfilName) from ResFilesystem rf order by rf.rfilNum");
		if(list == null || list.size() < 1){
			return "[]";
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		buffer.append("['0','本地服务器...']");
		for(int i=0,s=list.size(); i<s; i++){
			buffer.append(",['"+list.get(i).getRfilId()+"','"+list.get(i).getRfilName()+"']");
		}
		buffer.append("]");
		String result = buffer.toString();
		buffer = null;
		list = null;
		return result;
	}
	
	public static Map<Long, ResFilesystem> fileServiceMap = null;
	
	
	public static void initFileServiceMap(){
		IResFilesystemService fileServiceDao = (IResFilesystemService) ServiceLocator.getInstance().getService("fileSystemService");
		//String sql = "select RFIL_ID,RFIL_NAME,RFIL_ADDRESS,RFIL_FTP_HOST,RFIL_FTP_USERNAME,RFIL_FTP_PASSWORD,RFIL_FPT_PATH,RFIL_NUM from RES_FILESYSTEM";        
		List<Object> list = fileServiceDao.find("from ResFilesystem");
		if(list == null || list.size() < 1){
			fileServiceMap = null;
			return;
		}
		fileServiceMap = new HashMap<Long, ResFilesystem>();
		ResFilesystem item = null;
		for(int i=0,s=list.size(); i<s; i++){
			item = (ResFilesystem) list.get(i);
			fileServiceMap.put(item.getRfilId(), item);
		}
	}
	
	public static Map<Long, ResFilesystem> getFileServiceMap(){
		if(fileServiceMap == null || fileServiceMap.size() < 1){
			initFileServiceMap();
		}
		return fileServiceMap;
	}
	
	public static ResFilesystem getFilesystemByMap(Long id) {
		if(id == null || id.equals(0)){
			return null;
		}
		ResFilesystem system = getFileServiceMap().get(id);
		return system;
	}
	
	public static ResMetaStore getMetaStore(Long rmstId){
		IResFilesystemService fileServiceDao = (IResFilesystemService) ServiceLocator.getInstance().getService("fileSystemService");
		return (ResMetaStore) fileServiceDao.get(ResMetaStore.class, rmstId);
	}
}
