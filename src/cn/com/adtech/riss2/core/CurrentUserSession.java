package cn.com.adtech.riss2.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import cn.com.adtech.riss2.bso.ICommonServiceCommonlyService;
import cn.com.adtech.riss2.domain.SysCatalog;
import cn.com.adtech.riss2.domain.SysFunction;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.SysRole;
import cn.com.adtech.riss2.domain.SysRoleFunction;

import com.jbetter.framework.session.PageGrid;
import com.jbetter.framework.session.PageSession;
import com.jbetter.framework.session.UserSession;

@SuppressWarnings( {"unchecked", "serial"})
public class CurrentUserSession extends UserSession {
	private static Logger logger = Logger.getLogger(CurrentUserSession.class);

	private Long id;

	// 当前用户
	private SysManager currentUser;

	// 当前用户所拥有的角色集合
	private List currentUserRoles;

	// 当前用户所拥有的功能对象树
	private List accessableSysCatalogs = new ArrayList();

	// 当前用户所拥有功能点集合
	private List functions = new ArrayList();

	// 当前用户所拥有功能点，以功能点Id为索引。
	private Map<Long, SysFunction> functionIndex = new HashMap<Long, SysFunction>();

	// 当前用户所拥有的功能点id. add by liulm
	private Set functionIds = new HashSet();

	// 当前用户所拥有的功能点所对应的所有URL集合
	private List functionURLs = new ArrayList();

	// 是否拥有网站后台管理员权限
	private boolean manager;

	// 是否调用过刷新角色功能方法
	private boolean hasRefleshRoleFunction = false;

	private void addFunction(SysFunction function) {
		if(function == null)
			return;
		if(!functions.contains(function)) {
			for(int i = 0; i < functions.size(); i++) {
				if(((SysFunction)functions.get(i)).getSfunId().equals(function.getSfunId())) {
					return;
				}
			}
			// add by liulm
			functionIds.add(function.getSfunId());
			functionIndex.put(function.getSfunId(), function);
			functions.add(function);
			// 添加该用户拥有的功能点包括的URL集合,
			addFunctionUrl(function.getSfunUrl());
			// String urls = function.getSfunUrlsMethods();
			// if(urls != null && !urls.trim().equals("")) {
			// String[] strUrls = urls.split(",");
			// for(int j = 0; j < strUrls.length; j++) {
			// String temp = strUrls[j];
			// if(!temp.trim().equals("")) {
			// addFunctionUrl(temp);
			// }
			// }
			// }
		}
	}

	private void addFunctionUrl(String url) {
		if(!functionURLs.contains(url)) {
			functionURLs.add(url);
		}
	}

	public CurrentUserSession() {
		hasRefleshRoleFunction = false;
		id = new Long(System.currentTimeMillis());
	}

	public CurrentUserSession(SysManager currentUser) {
		hasRefleshRoleFunction = false;
		this.currentUser = currentUser;
		id = new Long(System.currentTimeMillis());
	}

	/**
	 * 根据角色集合，构造角色集所拥有的功能集合
	 * 
	 * @param roles
	 */
	private void makeFunctions(List roles) {
		if(functions != null && functions.size() > 0){
			functions = new ArrayList();
		}
		functionURLs = new LinkedList();
		// add by liulm
		functionIds = new HashSet();
		functionIndex = new HashMap<Long, SysFunction>();
		for(int i = 0; i < roles.size(); i++) {
			SysRole role = (SysRole)roles.get(i);
			Set roleFunctions = role.getSysRoleFunctions();
			if(roleFunctions != null) {
				Iterator it = roleFunctions.iterator();
				while(it.hasNext()) {
					SysRoleFunction roleFunction = (SysRoleFunction)it.next();
					addFunction(roleFunction.getSysFunction());
				}
			}
		}
		// 按功能的index排序
		try {
			Collections.sort(functions, new Comparator(){
				public int compare(Object o1, Object o2) {
					SysFunction f1 = (SysFunction)o1;
					SysFunction f2 = (SysFunction)o2;
					return (f1.getSfunIndex().shortValue() - f2.getSfunIndex().shortValue());
				}
			});
		}catch(ConcurrentModificationException e) {
			e.printStackTrace();
		}
	}

	public List getFunctions() {
		return functions;
	}

	public void setFunctions(List functions) {
		this.functions = functions;
	}

	public static CurrentUserSession findUserSession(HttpServletRequest request) {
		CurrentUserSession userSession = (CurrentUserSession)request.getSession().getAttribute("userSession");
		if(userSession == null) {
			userSession = new CurrentUserSession(null);
			request.getSession().setAttribute("userSession", userSession);
		}
		return userSession;
	}

	public PageSession findPageSession(HttpServletRequest request) {
		PageSession pageSession = null;
		String s = request.getParameter("pageId");
		if(ServiceUtil.isNotNull(s)) {
			pageSession = find(Long.parseLong(s));
			if(pageSession != null) {
				PageGrid pageGride = (PageGrid)pageSession.get(request.getParameter("setId"));
				if(pageGride != null) {
					String tempPage = request.getParameter("currentPage");
					if(tempPage != null) {
						pageGride.getPagination().setCurrentPage(Integer.parseInt(tempPage));
					}
				}
			}
		}
		return pageSession;
	}

	/**
	 * 刷新当前登录用户的角色集合和系统功能树集合及功能点集合
	 * 
	 * @param schoolUser
	 * @param userSession
	 */
	public void refreshRoleFunctions(ICommonServiceCommonlyService commonService) {
		hasRefleshRoleFunction = true;
		// 得到角色
		List roles = commonService.getUserRoles(currentUser);
		System.out.println("角色列表---->:" + roles);
		if(roles == null) {
			roles = new ArrayList();
		}
		if(currentUser != null) {
			// 添加已登录用户角色
			SysRole loginUserRole = (SysRole)commonService.get(SysRole.class, SysRole.ID_LOGIN_USER);
			if(loginUserRole != null) {
				roles.add(loginUserRole);
			}
			/*
			 * if(SysUserinfo.TYPE_READER.equals(currentUser.getSysUserType().getSutyId())) { // 一般读者 SysRole role =
			 * (SysRole)commonService.get(SysRole.class, SysRole.ID_READER); if(role != null) { roles.add(role); } }else
			 * if(SysUserinfo.TYPE_NETUSER.equals(currentUser.getSysUserType().getSutyId())) {// 网络注册用户
			 * if(currentUser.getSuinNetuserPassed().booleanValue() == true) { // 已认证网络注册用户 SysRole role =
			 * (SysRole)commonService.get(SysRole.class, SysRole.ID_VALIDATE_NETUSER); if(role != null) { roles.add(role); } }else { //
			 * 未认证网络注册用户 SysRole role = (SysRole)commonService.get(SysRole.class, SysRole.ID_NOT_VALIDATE_NETUSER); if(role != null) {
			 * roles.add(role); } } }else if(SysUserinfo.TYPE_WORKER.equals(currentUser.getSysUserType().getSutyId())) { // 图书馆工作人员 SysRole
			 * role = (SysRole)commonService.get(SysRole.class, SysRole.ID_WORKER); if(role != null) { roles.add(role); } SysRole role2 =
			 * (SysRole)commonService.get(SysRole.class, SysRole.ID_READER); if(role2 != null) { roles.add(role2); } }else
			 * if(SysUserinfo.TYPE_SCHOOLFELLOW.equals(currentUser.getSysUserType().getSutyId())) { // 校友
			 * if(currentUser.getSuinNetuserPassed().booleanValue() == true) { // 已认证校友用户 SysRole role =
			 * (SysRole)commonService.get(SysRole.class, SysRole.ID_SCHOOL_FELLOW); if(role != null) { roles.add(role); } }else { //
			 * 未认证网络注册用户 SysRole role = (SysRole)commonService.get(SysRole.class, SysRole.ID_NOT_VALIDATE_NETUSER); if(role != null) {
			 * roles.add(role); } } }
			 */
		}
		/*
		 * 添加所有人角色 SysRole role = (SysRole)commonService.get(SysRole.class, SysRole.ID_EVERYONE); if(role != null) { roles.add(role); }
		 */
		setCurrentUserRoles(roles);
		// 并生成功能列表
		makeFunctions(roles);
		// 根据角色集合，获得角色集有权限操作的系统功能树
		List catalogs = commonService.getAccessableSysCatalogs(this.currentUserRoles);
		setAccessableSysCatalogs(catalogs);
		// 刷新功能点JSON
		refreshJSONRoleFunctions();
		// 设置该用户是否拥有后台管理员权限
		setManager(false);
		// 获得该用户在后台管理方面的权限.
		List sysCatalogs = getAccessableSysCatalogs();
		if(sysCatalogs != null && sysCatalogs.size() > 0) {
			// 如果该用户拥有后台管理方面的权限,则设置他是管理员.
			setManager(true);
		}
	}

	/**
	 * 判断用户是否有该功能权限
	 * 
	 * @param funId
	 * @return
	 */
	public boolean isHasFunction(Long funId) {
		boolean result = false;
		if(functionIds != null && functionIds.size() > 0) {
			result = functionIds.contains(funId);
		}
		return result;
	}

	public Long getId() {
		return id;
	}

	public SysManager getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(SysManager user) {
		this.currentUser = user;
	}

	public List getCurrentUserRoles() {
		return currentUserRoles;
	}

	public void setCurrentUserRoles(List currentUserRoles) {
		this.currentUserRoles = currentUserRoles;
	}

	public boolean isManager() {
		return manager;
	}

	public void setManager(boolean manager) {
		this.manager = manager;
	}

	/**
	 * 获得当前用户有权限访问的顶级系统对象集合
	 * 
	 * @return
	 */
	public List getAccessableSysCatalogs() {
		return accessableSysCatalogs;
	}

	/**
	 * 根据SysCatalog的ID值,从 accessableSysCatalogs中获得指定的SysCatalog对象
	 * 
	 * @param id
	 * @return
	 */
	public SysCatalog getSysCatalogsById(String id) {
		SysCatalog sysCatalog = null;
		if(ServiceUtil.isNotNull(id)) {
			try {
				long idValue = Long.parseLong(id);
				if(accessableSysCatalogs != null) {
					for(int i = 0; i < accessableSysCatalogs.size(); i++) {
						SysCatalog catalog = (SysCatalog)accessableSysCatalogs.get(i);
						if(catalog.getScatId().longValue() == idValue) {
							sysCatalog = catalog;
							break;
						}
					}
				}
			}catch(Exception e) {
				// 忽略
			}
		}
		return sysCatalog;
	}

	public void setAccessableSysCatalogs(List accessableSysCatalogs) {
		this.accessableSysCatalogs = accessableSysCatalogs;
	}

	public boolean isHasRefleshRoleFunction() {
		return hasRefleshRoleFunction;
	}

	public void setHasRefleshRoleFunction(boolean isRefleshRoleFunction) {
		this.hasRefleshRoleFunction = isRefleshRoleFunction;
	}

	public List getFunctionURLs() {
		return functionURLs;
	}

	public Set getFunctionIds() {
		return functionIds;
	}

	public Map<Long, SysFunction> getFunctionIndex() {
		return functionIndex;
	}

	public void setFunctionIndex(Map<Long, SysFunction> functionIndex) {
		this.functionIndex = functionIndex;
	}

	/**
	 * 根据id获得功能点链接，当无功能点时返回null，否则返回链接值。
	 * 
	 * @param funcId
	 *            功能点id
	 * @return
	 * @author MaChao
	 */
	public String getFunctionUrl(Long funcId) {
		SysFunction function = functionIndex.get(funcId);
		if(function != null)
			return function.getSfunUrl();
		return null;
	}

	// 保存用户有权限功能树的json一
	private Map<Long, String> jsonFunctionTreeMap = new HashMap<Long, String>();

	public Map<Long, String> getJsonFunctionTreeMap() {
		return jsonFunctionTreeMap;
	}

	/**
	 * 通过顶级分类获得该分类下的树
	 * 
	 * @param catalogId
	 *            分类id
	 * @return
	 * @author MaChao
	 */
	public String getJsonFunctionTree(Long catalogId) {
		String json = null;
		if(catalogId != null) {
			json = jsonFunctionTreeMap.get(catalogId);
		}
		if(json == null)
			json = "";
		return json;
	}

	/**
	 * 刷新用户有权限功能树的json一 该方法访问accessableSysCatalogs生成功能菜单JSON一
	 * 
	 * @author MaChao
	 */
	private void refreshJSONRoleFunctions() {
		jsonFunctionTreeMap.clear();
		StringBuilder json = new StringBuilder("");
		List accessableSysCatalogsList = getAccessableSysCatalogs();
		if(accessableSysCatalogsList != null) {
			for(int i = 0, size = accessableSysCatalogsList.size(); i < size; i++) {
				json.setLength(0);
				json.append("[");
				SysCatalog sysCatalog = (SysCatalog)accessableSysCatalogsList.get(i);
				List childrenCatalogs = sysCatalog.getChildrenCatalogs();
				// 构造分类目录
				if(childrenCatalogs != null && childrenCatalogs.size() > 0) {
					for(int j = 0; j < childrenCatalogs.size(); j++) {
						SysCatalog subSysCatalog = (SysCatalog)childrenCatalogs.get(j);
						if(j > 0)
							json.append(",");
						createFuncJson(json, subSysCatalog);
					}
				}
				json.append("]");
				if(logger.isDebugEnabled())
					logger.debug(json.toString());
				jsonFunctionTreeMap.put(sysCatalog.getScatId(), json.toString());
			}
		}
	}

	/**
	 * 递归生成功能树json
	 * 
	 * @param json
	 * @param sysCatalog
	 * @author MaChao
	 */
	private void createFuncJson(StringBuilder json, SysCatalog sysCatalog) {
		json.append("{id:'cat").append(sysCatalog.getScatId()).append("'").append(",text:'").append(sysCatalog.getScatName()).append("'");
		json.append(",children:[");
		List childrenCatalogs = sysCatalog.getChildrenCatalogs();
		// 构造分类目录
		if(childrenCatalogs != null && childrenCatalogs.size() > 0) {
			for(int j = 0; j < childrenCatalogs.size(); j++) {
				SysCatalog subSysCatalog = (SysCatalog)childrenCatalogs.get(j);
				if(j > 0)
					json.append(",");
				createFuncJson(json, subSysCatalog);
			}
		}
		List sysFuncList = sysCatalog.getSysFunctions();
		// 构造功能列表
		if(sysFuncList != null && sysFuncList.size() > 0) {
			// 如果前面有分类需要增加逗号分隔
			if(childrenCatalogs != null && childrenCatalogs.size() > 0) {
				json.append(",");
			}
			for(int k = 0; k < sysFuncList.size(); k++) {
				SysFunction func = (SysFunction)sysFuncList.get(k);
				if(k > 0)
					json.append(",");
				json.append("{id:'fun").append(func.getSfunId()).append("'").append(",text:'").append(func.getSfunName()).append("'")
						.append(",leaf:true,url:'").append(func.getSfunUrl()).append("'}");
			}
		}
		json.append("]}");
	}
}
