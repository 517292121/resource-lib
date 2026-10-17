package cn.com.adtech.riss2.bso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.orm.hibernate3.HibernateTemplate;

import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.PasswordAuthenticator;
import cn.com.adtech.riss2.domain.MmSetting;
import cn.com.adtech.riss2.domain.SysCatalog;
import cn.com.adtech.riss2.domain.SysFunction;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.SysRole;
import cn.com.adtech.riss2.domain.SysRoleFunction;
import cn.com.adtech.riss2.domain.SysUserinfo;

public class CommonServiceCommonlyImp extends CommonServiceImp implements ICommonServiceCommonlyService {
	
	public CommonServiceCommonlyImp() {
		
	}

	public List<Object> getAccessableSysCatalogs(List roles) {
		if(roles == null)
			return null;
		if(roles.size() == 0)
			return null;
		StringBuffer roleIds = new StringBuffer();
		List categories = new ArrayList();
		List sysFunctionList = new ArrayList(); // 用于存放用户拥有的功能权限
		// 第一步：获得该用户所拥有的所有功能权限
		for(int i = 0; i < roles.size(); i++) {
			SysRole role = (SysRole)roles.get(i);
			roleIds.append(role.getSrolId());
			if(i < roles.size() - 1)
				roleIds.append(",");
			Set sysRoleFunctions = role.getSysRoleFunctions();
			Iterator iter = sysRoleFunctions.iterator();
			while(iter.hasNext()) {
				SysRoleFunction roleFunction = (SysRoleFunction)iter.next();
				sysFunctionList.add(roleFunction.getSysFunction());
			}
		}
		return getRootCategories(sysFunctionList);
	}

	@Override
	public List getRootCategories(List sysFunctionList) {
		if(sysFunctionList == null) {
			return new ArrayList();
		}
		List categories;
		StringBuffer sysFunctionIds = new StringBuffer();
		CataLogMap temp = new CataLogMap();// 用于存所有被访问到的SysCatalog，因为在建树时，SysCatalog一个对象可能会被反复访问到，
		// 加一个缓寸可以减少数据库访问量，同时方便建树
		Map rootCatalog = new HashMap();// 用来存放最顶层的SysCatalog，在这些顶层的SysCatalog中包含有它们的子SysCatalog，
		// 这些SysCatalog形成了一个树结构
		// Set tempSet = new HashSet(sysFunctionList);
		// sysFunctionList = new ArrayList(tempSet);
		// for(int i = 0; i < sysFunctionList.size(); i++) {
		// SysFunction element = (SysFunction)sysFunctionList.get(i);
		// for(int j = i + 1; j < sysFunctionList.size(); j++) {
		// SysFunction function = (SysFunction)sysFunctionList.get(j);
		// if(element.getSfunId().equals(function.getSfunId())) {
		// sysFunctionList.remove(i);
		// }
		// }
		// }
		/* 原过滤重复的算法(以上屏蔽代码)找到重复直接从正在循环的集合中remove,导致i++和j++后跳位,每次过滤后的size都不相同 */
		/* 修改过滤算法如下,保证完整过滤且保持不变 by GaoYi at 2008-7-11 */
		List noRepeatSysFunctionList = new ArrayList();
		for(int i = 0, size = sysFunctionList.size(); i < size; i++) {
			SysFunction element = (SysFunction)sysFunctionList.get(i);
			boolean hasRepeat = false;
			for(int j = 0, size2 = noRepeatSysFunctionList.size(); j < size2; j++) {
				SysFunction function = (SysFunction)noRepeatSysFunctionList.get(j);
				if(element.getSfunId().equals(function.getSfunId())) {
					hasRepeat = true;
					break;
				}
			}
			if(!hasRepeat) {
				noRepeatSysFunctionList.add(element);
			}
		}
		// 第一步完
		if(noRepeatSysFunctionList.size() == 0) // 没有功能
			return null;
		// 第二步 将用户拥有的SysFunction的ID组装成串，便于数据库查询
		for(int i = 0; i < noRepeatSysFunctionList.size(); i++) {
			SysFunction function = (SysFunction)noRepeatSysFunctionList.get(i);
			sysFunctionIds.append(function.getSfunId());
			if(i < noRepeatSysFunctionList.size() - 1)
				sysFunctionIds.append(",");
		}
		// 第二步完
		// 第三步 获取用户可以访问的SysCatalog
		categories = getAccessableSysCatalogs(sysFunctionIds.toString());
		for(int i = 0; i < categories.size(); i++) {
			SysCatalog catalog = (SysCatalog)categories.get(i);
			// 构件SysCatalog的访问树，这一步很重要
			// 这个方法会返回顶层SysCatalog，这些顶层SysCatalog中还包含有它们的子SysCatalog，所以SysCatalog是以树结构存储在MAP中的
			SysCatalog root = getRootCatalog(catalog, temp, noRepeatSysFunctionList);
			if(root != null)
				rootCatalog.put(root.getScatId(), root);
		}
		categories = new ArrayList();
		categories.addAll(rootCatalog.values());
		java.util.Collections.sort(categories);
		/*
		 * for (Iterator iter = categories.iterator(); iter.hasNext();) { SysCatalog element = (SysCatalog) iter.next(); List child =
		 * element.getChildrenCatalogs(); for (Iterator iterator = child.iterator(); iterator.hasNext();) { SysCatalog sys = (SysCatalog)
		 * iterator.next(); List fun = sys.getSysFunctions(); for (Iterator iterator1 = fun.iterator(); iterator1.hasNext();) { SysFunction
		 * f = (SysFunction) iterator1.next(); System.out.print(f.getSfunIndex()+"\t"); System.out.println(f.getSfunName()); } } }
		 */
		return categories;
	}

	/** 根据sysFunctionIds返回有访问权限的SysCatalog */
	private List getAccessableSysCatalogs(String sysFunctionIds) {
		if(!CommonUtil.isNotNull(sysFunctionIds))
			return null;
		List catalogs = null;
		String hql = "select distinct catalog from SysFunction fun,SysCatalog catalog where fun.parentCatalog = catalog and fun.sfunId in ("
				+ sysFunctionIds + ")";
		catalogs = this.getHibernateTemplate().find(hql);
		return catalogs;
	}
	
	/***************************************************************************************************************************************
	 * 该方法的算法思路是获得一个SysCatalog，如果该SysCatalog为最顶层SysCatalog则在权限赋值完成后返回
	 * 如果该SysCatalog有父SysCatalog，则进入递归体访问其它的所有上层SysCatalog，并对这些SysCatalog赋予相应的访问权限，
	 * 同时将这些SysCatalog都添加到SysCatalog的对象树中，当访问到最顶层SysCatalog时返回该对象树 注意：SysCatalog对象本身拥有childrenCatalogs属性，所以对象树是建立在SysCatalog对象中的
	 **************************************************************************************************************************************/
	private SysCatalog getRootCatalog(SysCatalog catalog, CataLogMap catalogMap, List functionList) {
		if(catalog == null)
			return null;
		// 判断当前SysCatalog是否在缓存中，如果不在则加入之
		if(!catalogMap.isExists(catalog)) {
			// 在加入缓存前先将该SysCatalog的权限加载到SysCatalog中
			// 应该需要排序 
			catalog = setSysFunctionToCataLog(catalog, functionList);
			// 放入缓存
			catalogMap.addCataLog(catalog);
		}
		// 有父类Catalog
		if(catalog.getParentCatalog() != null) {
			SysCatalog parent = catalog.getParentCatalog();
			// 将以前处理过的相同的对象进行数据同步，以确保处理的正确
			if(catalogMap.isExists(parent))
			/*
			 * 如果catalogMap中已经有该对象了则直接返回已经缓存在该MAP中的对象， 因为这个对象以前处理过，已经和其他对象建立了树的关系并且已经赋予了权限， 这样可以方便前台建立导航和检查权限
			 */
			{
				parent = catalogMap.getSysCatalog(parent);
			}else {
				// 如果catalogMap中没有该对象，则在该对象中加入SysFunction以方便前台检查权限
				parent = setSysFunctionToCataLog(parent, functionList);
			}
			Set set = new HashSet();
			set.addAll(parent.getChildrenCatalogs());
			set.add(catalog);// Set集合不允许重复的数据，这样可以剔除重复的数据
			Object[] obj = set.toArray();
			List list = new ArrayList();
			for(int i = 0; i < obj.length; i++) {
				list.add(obj[i]);
			}
			Collections.sort(list, new Comparator<SysCatalog>(){
				public int compare(SysCatalog sysCatalog1, SysCatalog sysCatalog2) {
					return sysCatalog1.getScatCode() - sysCatalog2.getScatCode();
				}
			});
			parent.getChildrenCatalogs().clear();
			parent.getChildrenCatalogs().addAll(list);
			catalogMap.addCataLog(parent);
			if(parent.getParentCatalog() == null)
				return parent;
			return getRootCatalog(parent, catalogMap, functionList);
		}
		return catalog;
	}
	
	/** 将用户权限设置到对应的Catalog中 */
	private SysCatalog setSysFunctionToCataLog(SysCatalog catalog, List functionList) {
		if(catalog == null)
			return null;
		Long scatId = catalog.getScatId();
		List functions = new ArrayList();
		for(Iterator iter = functionList.iterator(); iter.hasNext();) {
			SysFunction element = (SysFunction)iter.next();
			if(element.getParentCatalog().getScatId().equals(scatId)) {
				Boolean flag = false;
				for(Iterator iterator = functions.iterator(); iterator.hasNext();) {
					SysFunction function = (SysFunction)iterator.next();
					if(function.getSfunId().equals(element.getSfunId()))
						flag = true;
				}
				if(flag == false)
					functions.add(element);
			}
		}
		Set temp = new HashSet(functions);
		functions = new ArrayList(temp);
		Collections.sort(functions, new Comparator<SysFunction>(){
			public int compare(SysFunction sysFunction1, SysFunction sysFunction2) {
				return sysFunction1.getSfunIndex() - sysFunction2.getSfunIndex();
			}
		});
		// for (Iterator iter = functions.iterator(); iter.hasNext();) {
		// SysFunction element = (SysFunction) iter.next();
		// System.out.print(element.getSfunId()+"\t");
		// System.out.print(element.getSfunIndex()+"\t");
		// System.out.println(element.getSfunName()); }
		catalog.setSysFunctions(functions);
		return catalog;
	}
	
	@Override
	public MmSetting getSettingParam(String name, Short type) {
		MmSetting result = null;
		List list = getHibernateTemplate().find("from MmSetting setting where setting.msetName=?  and setting.msetType=?",
				new Object[] {name, type});
		if(list != null && list.size() > 0) {
			result = (MmSetting)list.get(0);
		}
		return result;
	}

	@Override
	public String getSysProperty(String name) {
		String hql = "select mmSetting.msetValue from MmSetting mmSetting where mmSetting.msetName = ?";
		List list = getHibernateTemplate().find(hql, name);
		if(list.size() == 0)
			return null;
		else
			return list.get(0).toString();
	}

	@Override
	public List<Object> getUserRoles(SysManager user) {
		List result = null;
		if(user != null) {
			HibernateTemplate template = getHibernateTemplate();			
			result = template.find("select ur.role from SysUserRole ur where ur.user.smanId=?", user.getSmanId());
		}
		return result;
	}

	@Override
	public SysManager managerLogin(String userName, String password,
			String remoteHost) {
		SysManager result = null;
		String hql = "from SysManager sui where sui.smanLoginName = ? and sui.smanIsStop is false";
		List list = getHibernateTemplate().find(hql, userName);
		if(list != null && list.size() > 0) {
			result = (SysManager)list.get(0);
			try {
				if(!PasswordAuthenticator.authenticatePassword(password, result.getSmanPwd())) {
					result = null;
				} 
			}catch(Exception e) {
				e.printStackTrace();
				result = null;
			}
		}
		return result;
	}

//	@Override
//	public void saveAllMmSettings(final HttpServletRequest request) {
//		getHibernateTemplate().execute(new HibernateCallback(){
//			public Object doInHibernate(Session session) throws HibernateException, SQLException{
//				Connection conn = session.connection();
//				Statement cc = conn.createStatement();
//				try {
//					String sourceFileSize = ServletRequestUtils.getStringParameter(request, "RESOURCE_UPLOAD_SOURCEFILE_MAXSIZE", "");
//					String viewFileSize = ServletRequestUtils.getStringParameter(request, "RESOURCE_UPLOAD_VIEWFILE_MAXSIZE", "");
//					String photoFileSize = ServletRequestUtils.getStringParameter(request, "RESOURCE_UPLOAD_PHOTOFILE_MAXSIZE", "");
//					String locationOptions = ServletRequestUtils.getStringParameter(request, "SOURCE_LOCATION_OPTIONS", "");
//					// 执行批量更新
//					cc.addBatch("update SYS_SETTINGS set SSET_VALUE='"+sourceFileSize+"' where SSET_KEY='"+ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_SOURCEFILE_MAXSIZE+"'");
//					cc.addBatch("update SYS_SETTINGS set SSET_VALUE='"+viewFileSize+"' where SSET_KEY='"+ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_VIEWFILE_MAXSIZE+"'");
//					cc.addBatch("update SYS_SETTINGS set SSET_VALUE='"+photoFileSize+"' where SSET_KEY='"+ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_PHOTOFILE_MAXSIZE+"'");
//					cc.addBatch("update SYS_SETTINGS set SSET_VALUE='"+locationOptions+"' where SSET_KEY='"+ConstantsObj.SysSettingKey.SOURCE_LOCATION_OPTIONS+"'");
//					cc.executeBatch();
//					// 添加到内存
//					ConstantsObj.setSysProperty(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_SOURCEFILE_MAXSIZE,sourceFileSize);
//					ConstantsObj.setSysProperty(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_VIEWFILE_MAXSIZE,viewFileSize);
//					ConstantsObj.setSysProperty(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_PHOTOFILE_MAXSIZE,photoFileSize);
//					ConstantsObj.setSysProperty(ConstantsObj.SysSettingKey.SOURCE_LOCATION_OPTIONS,locationOptions);
//				} catch (Exception e) {
//					e.printStackTrace();
//					throw new ErrorMessageException("保存失败！");
//				}finally {
//					cc.close();
//				}
//				return null;
//			}
//		});	
//
//	}

	@Override
	public void saveMmSettingParam(MmSetting setting) {
		MmSetting dbSetting = getSettingParam(setting.getMsetName(), setting.getMsetType());
		if(dbSetting != null) {
			dbSetting.setMsetValue(setting.getMsetValue());
			save(dbSetting);
		}else {
			save(setting);
		}
	}

	@Override
	public SysUserinfo sysuserLogin(String userName, String password,
			String remoteHost) {
		SysUserinfo result = null;
		String hql = "from SysUserinfo sui where sui.suinLoginName = ?";//and (sui.suinIsStop<>1 or sui.suinIsStop is null) and sui.suinState=1";
		List list = getHibernateTemplate().find(hql, userName);
		if(list != null && list.size() > 0) {
			result = (SysUserinfo)list.get(0);
			try {
				if(!PasswordAuthenticator.authenticatePassword(password, result.getSuinPassword())) {
					result = null;
				}
			}catch(Exception e) {
				e.printStackTrace();
				result = null;
			}
			if(result != null) {
				Date lastvisitDate = result.getSuinLastvisitDate(); 
				result.setSuinLastvisitDate(new Date());
				getHibernateTemplate().update(result); 
				try {
					//登录成功后，加分
				    lastvisitDate = ConstantsObj.FORMAT_DATE.parse(ConstantsObj.FORMAT_DATE.format(lastvisitDate));
					Date nowvisitDate = ConstantsObj.FORMAT_DATE.parse(ConstantsObj.FORMAT_DATE.format(new Date()));
					if(lastvisitDate.getTime()<nowvisitDate.getTime()){
						updateUsersPoint(result.getSuinId(), MmSettingConstant.getAsspoint_Login());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	@Override
	public SysManager userLogin(String userName, String password,
			String remoteHost) {
		SysManager result = null;
		String hql = "from SysUserinfo sui where sui.suinLoginName = ? and (sui.suinIsStop<>1 or sui.suinIsStop is null)";
		List list = getHibernateTemplate().find(hql, userName);
		if(list != null && list.size() > 0) {
			result = (SysManager)list.get(0);
			try {
				if(!PasswordAuthenticator.authenticatePassword(password, result.getSmanPwd())) {
					result = null;
				}
			}catch(Exception e) {
				e.printStackTrace();
				result = null;
			}
			if(result != null) {
//				result.setSuinLastvisitDate(new Date());
//				getHibernateTemplate().update(result);
				// 保存用户登录日志 add by hetao 20071016
//				LogSiteLogin logSiteLogin = new LogSiteLogin();
//				logSiteLogin.setLshiDate(result.getSuinLastvisitDate());
//				logSiteLogin.setLshiSuinId(result.getSuinId());
//				logSiteLogin.setLshiClientIp(remoteHost);
//				getHibernateTemplate().save(logSiteLogin);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	class CataLogMap {
		private Map catalogMap = new HashMap();

		public void addCataLog(SysCatalog catalog) {
			if(catalog == null)
				return;
			catalogMap.put(catalog.getScatId(), catalog);
		}

		public SysCatalog getSysCatalog(SysCatalog catalog) {
			if(catalog == null)
				return null;
			if((SysCatalog)catalogMap.get(catalog.getScatId()) == null)
				return catalog;
			return (SysCatalog)catalogMap.get(catalog.getScatId());
		}

		public SysCatalog remove(SysCatalog catalog) {
			if(catalog == null)
				return null;
			return (SysCatalog)catalogMap.remove(catalog.getScatId());
		}

		public boolean isExists(SysCatalog catalog) {
			if(catalog == null)
				return false;
			return (SysCatalog)catalogMap.remove(catalog.getScatId()) != null;
		}
	}

	
	private String createMmSettingSql(String key,String value){
		ConstantsObj.setSysProperty(key, value);
		String hql = "update SYS_SETTINGS  set SSET_VALUE = '" + value + "' where SSET_KEY = '" + key + "'";
		return hql;
	}
	
	
	@Override
	public void saveMmSetting(Map conditions) {
		List<String> sqlList = new ArrayList<String>();
		
		Set<String> keySet = conditions.keySet();
		for(String key:keySet) {
			// 1.0 更新参数信息 
			sqlList.add(createMmSettingSql(key,conditions.get(key).toString()));
			// 2.0 针对部分参数进行特殊处理
			if(MmSettingConstant.getUserUploadResourceIsOpen().equals(key)){
				String rmstHql = null;
				if(conditions.get(MmSettingConstant.getUserUploadResourceIsOpen()).equals("1")){
					rmstHql = "update res_meta_store set rmst_is_upload_resource = 1 ";
				}else{
					rmstHql = "update res_meta_store set rmst_is_upload_resource = 0 ";
				}
				sqlList.add(rmstHql);
			}
		}
		
		boolean result = super.executeMultiSql(sqlList);
		if(!result){
			ConstantsObj.initSysProperties(commonService);
		}
	}
	private ICommonService commonService;
	
	public ICommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}
	
}
