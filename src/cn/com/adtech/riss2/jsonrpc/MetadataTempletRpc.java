package cn.com.adtech.riss2.jsonrpc;

import java.io.Serializable;

import cn.com.adtech.riss2.bso.CommonServiceImp;

/**
 * 数据模板管理jsonrpc Ajax处理对象 
 * @author MaChao
 *
 */
public class MetadataTempletRpc extends CommonServiceImp implements IMetadataTemletService,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetadataTempletRpc() {
	}

	public static void main(String[] args){
		System.out.println(System.currentTimeMillis());
		
	}

	
	/**
	 * 新增核心元素
	 * @return
	 */
//	public long AddExtendElement(long source_type, String eleName,
//			String eleTitle, long eleType, String eleCode, String repoRestrict) {
//
//		System.out.println(">>>>>>>>>>>.aaaaaaaaaaaaa");
//		ResElementPool ele = new ResElementPool();
//		ele.setChildelements(null);
//		ele.setRepoPltyId(source_type);
//		ele.setRepoName(eleName);
//		ele.setRepoTitle(eleTitle);
//		ele.setRepoType(eleType);
//		ele.setRepoCode(eleCode);
//		ele.setRepoRestrict(repoRestrict);
//		
//		IMetadataElementPoolService eledao = new MetadataElementPoolImp();
//		Serializable s = eledao.AddElement(ele);
//		return Long.parseLong(s.toString());
//	}
	
	/**
	 * 删除核心元素
	 * @param store 特色库
	 * @param stype 资源类型
	 * @param eleid 元素ID
	 * @return
	 */
//	public boolean deleteMetaElement(long store, long stype, long eleid){
//		String sql = "delete from RES_TEMPLET_META";
//		sql += " where RTME_RSIN_ID="+store;
//		sql += " and RTME_RLTY_ID=" + stype;
//		sql += " and RTME_REEL_ID=" + eleid;
//		
//		boolean b = BasicUtilRpc.deleteBySql(sql);
//		return b;
//	}

	/**
	 * 添加核心元素到模板
	 * @param store
	 * @param stype
	 * @param eleid
	 * @return
	 */
	public boolean addMetaElement(long store, long stype, long eleid){
		
		String sql = "insert into RES_TEMPLET_META values(";
		sql += System.currentTimeMillis() + ",";
		sql += store + ",";
		sql += stype + ",";
		sql += eleid + ")";
		boolean b = executeUpdate(sql);
		return b;
	}
	
//	private boolean AddElementBySql(String sql){
//		try {
//			DBMS dbms = new DBMS();
//			boolean b = dbms.executeUpdate(sql);
//			dbms = null;
//			return b;
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("在MetadataTempletRpc的addMetaElement中发生异常："+e.getMessage());
//			System.out.println("异常SQL>>" + sql);
//			return false;
//		}
//	}
	
//	public boolean AddMetaElementList(long store, long stype, String eleidlist){
//		if(eleidlist == null || eleidlist.length() < 1){
//			return false;
//		}
//		String[] idArr = eleidlist.split(",");
//		long id = 0;
//		boolean b = true;
//		String sql = "";
//		for(int i=0; i<idArr.length; i++){
//			id = Long.parseLong(idArr[i]);
//			sql = "insert into RES_TEMPLET_META values(";
//			sql += (System.currentTimeMillis()+i) + ",";
//			sql += store + ",";
//			sql += stype + ",";
//			sql += id + ")";
//			if(AddElementBySql(sql) == false){
//				b = false;
//			}
//		}
//		return b;
//	}
	
	/**
	 * 删除修饰元素
	 */
//	public boolean deleteChildElement(long eleid){
//		String sql = "delete from RES_CHILD_DATA where RCDA_RCEL_ID="+eleid;
//		DBMS dbms = new DBMS();
//		boolean b = dbms.execute(sql);
//		if(b){
//			sql = "delete from RES_CHILD_ELEMENT where RCEL_ID= " + eleid;
//			System.out.println(sql);
//				b = dbms.execute(sql);
//			System.out.println(b);
//		}
//		dbms = null;
//		return b;
//	}
//	
	public boolean updateSingleValue(String colname,String data, long id){
		String sql = "update RES_ELEMENT_POOL set "+colname + " = '"+data+"' where REPO_ID="+id;
		boolean b = execute(sql);
		return b;
	}
	
	public boolean DeleteElement(long eleid, long parentid){
		String data_sql = "select count(REDA_ID) from RES_EXTEND_DATA where REDA_REPO_ID=" + eleid;
		long count = Long.parseLong(getFirstData(data_sql));
		if(count > 0){
			return false;
		}
		
		String delElement_sql = "delete from RES_ELEMENT_POOL where REPO_ID=" + eleid;
		String delmeta_templet = "delete from RES_TEMPLET_META where RTME_REEL_ID="+eleid;
		int len = parentid == 0 ? 3 : 2;
		String[] sqlArr = new String[len];
		sqlArr[0] = delElement_sql;
		sqlArr[1] = delmeta_templet;
		if(parentid == 0){
			String delChild_sql = "delete from RES_ELEMENT_POOL where REPO_PARENT=" + eleid;
			sqlArr[2] = delChild_sql;
		}
		try {
			boolean b = executeMultiSql(sqlArr);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("在MetadataTempletRpc的DeleteElement中发生异常："+e.getMessage());
			return false;
		}
	}
	
	/**
	 * 检查元素名是否可用,返回true:可用,false:不可用
	 * @param eleName 
	 * @param sourceTypeId 资源类型ID
	 * @return
	 */
	public boolean checkElementName(String eleName, long sourceTypeId){
		eleName = eleName.trim();
		String sql = "select count(REPO_ID) from RES_ELEMENT_POOL where REPO_RLTY_ID="+sourceTypeId+" and REPO_NAME='"+eleName+"'";
		int count = Integer.parseInt(getFirstData(sql));
		return count > 0 ? false : true;
	}
}





























