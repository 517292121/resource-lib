package cn.com.adtech.riss2.bso.count;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.jdbcms.ToolUnit;

import com.jbetter.framework.session.Pagination;

public class SysCountServiceImp extends CommonServiceImp implements ISysCountService {
	@Override
	public List getAccessCounterList(final int beginYear, final int endYear) {
		List list = (List)getHibernateTemplate().execute(new HibernateCallback(){
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "select DATE_FORMAT(counter.SACO_DATE,'%Y-%m'),count(counter.SACO_ID) from SYS_ACCESS_COUNTER counter ";
				sql += " where counter.SACO_SUIN_ID is null or counter.SACO_SUIN_ID=0";
				if(beginYear > 0){
					sql += " and YEAR(counter.SACO_DATE)>=" + beginYear;
				}
				if(endYear > 0){
					sql += " and YEAR(counter.SACO_DATE)<=" + endYear;
				}
				sql += " group by DATE_FORMAT(counter.SACO_DATE,'%Y-%m')";
				//sql += " having YEAR(counter.SACO_DATE)>=" + beginYear + " and YEAR(counter.SACO_DATE)<=" + endYear;
				sql += " order by DATE_FORMAT(counter.SACO_DATE, '%Y-%m') desc";
				return session.createSQLQuery(sql).list();
			}
		});
		return list;
	}

	@Override
	public Object[] getResourceDistributingCountList(final String endTime, String sort, String dir,final Pagination pagination,final String type) {
		final StringBuilder sql = new StringBuilder();
		if(sort.equals("rmstTitle")){
			sort = "rmst.rmst_name";
		}
		sql.append("select  rmst.rmst_name , ")
		   .append("(select count(rsda2.rsda_id) from RES_SOURCE_DATA rsda2 where rsda2.rsda_rmst_id = rmst.rmst_id  and rsda2.rsda_status = 2 ");
			if(ServiceUtil.isNotNull(endTime)){
				sql.append(" and rsda2.rsda_builddate <= to_date(").append("'").append(endTime).append("','yyyy-MM-dd')");
			}
		    sql.append(") checking , ")
		   .append("(select count(rsda2.rsda_id) from RES_SOURCE_DATA rsda2 where rsda2.rsda_rmst_id = rmst.rmst_id  and rsda2.rsda_status = 3 ");
		    if(ServiceUtil.isNotNull(endTime)){
		    	sql.append(" and rsda2.rsda_builddate <= to_date(").append("'").append(endTime).append("','yyyy-MM-dd')");
		    }
		    sql.append(") passcheck , ")
		   .append("(select count(rsda2.rsda_id) from RES_SOURCE_DATA rsda2 where rsda2.rsda_rmst_id = rmst.rmst_id  and rsda2.rsda_status = 4 ");
		    if(ServiceUtil.isNotNull(endTime)){
		    	sql.append(" and rsda2.rsda_builddate <= to_date(").append("'").append(endTime).append("','yyyy-MM-dd')");
		    }
		    sql.append(") notpasscheck , ")
		   .append("(select count(rsda2.rsda_id) from RES_SOURCE_DATA rsda2 where rsda2.rsda_rmst_id = rmst.rmst_id  ");
		    if(ServiceUtil.isNotNull(endTime)){
		    	sql.append(" and rsda2.rsda_builddate <= to_date(").append("'").append(endTime).append("','yyyy-MM-dd')");
		    }
		    sql.append(") rsdaCount , rmst.rmst_id ")
		   .append("from RES_META_STORE rmst left join RES_SOURCE_DATA rsda on rmst.rmst_id = rsda.rsda_rmst_id ").append("  group by rmst.rmst_id , rmst.rmst_name ")
		   .append("order by ").append(sort).append(" ").append(dir);
		
		final String countSQL = "select count(r.rmstId) from ResMetaStore r ";
		
		List list = (List)getHibernateTemplate().execute(new HibernateCallback(){
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				// 得到记录数
				Query queryObject = session.createQuery(countSQL);
				List list = queryObject.list();
				int amount = 0;
				if(!list.isEmpty()) {
					amount = ((Long)list.get(0)).intValue();
				}
				// 设置刻录总数到分页对象中
				if(!"1".equals(type)){
					pagination.setTotalResults(amount);
				}
				if(amount > 0) {
					// 得到记录列表
					queryObject = session.createSQLQuery(sql.toString());
					if(!"1".equals(type) && pagination.getPageResults() > 0) {
						queryObject.setFirstResult(pagination.getFirstResult());
						queryObject.setMaxResults(pagination.getPageResults());
					}
					return queryObject.list();
				}
				return new ArrayList();
			}
		});
		Object[] result = new Object[2];
		result[0] = list;
		List countList = null;
		if(ServiceUtil.isNotNull(type) && "1".equals(type)){
			String countHql = "select count(r.rsdaId) from ResSourceData r ";
			if(ServiceUtil.isNotNull(endTime)){
				countHql += " where r.rsdaBuilddate <= to_date('"+endTime+"','yyyy-MM-dd') ";
			}
			countList = getHibernateTemplate().find(countHql);
			if(countList.size()>0){
				result[1] = countList.get(0).toString();
			}else{
				result[1] = 0;
			}
		}else{
			result[1] = 0;
		}
		return result;
	}
	
	@Override
	public Wrapper getUserLoginCountList(final PageExtNative page) {
		return (Wrapper) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Wrapper wrap = new Wrapper();
						String username = page.getMSearch().get("userName");
						String startDate = page.getMSearch().get("beginD");
						String endDate = page.getMSearch().get("endD");
						String dir = page.getMSearch().get("dir");
						String sql = "";      
						sql += "  select count(saco_suin_id) as num , t.saco_suin_id from sys_access_counter t where  1=1 ";

						if (ServiceUtil.isNotNull(startDate)) {
							sql += " and  t.saco_date >=to_date(?,'yyyy-MM-dd:HH24:mi:ss') ";
						}
						if (ServiceUtil.isNotNull(endDate)) {
							sql += " and  t.saco_date <=to_date(?,'yyyy-MM-dd:HH24:mi:ss') ";
						}
						sql += " group by t.saco_suin_id having t.saco_suin_id is not null ) tc on tu.suin_id = tc.saco_suin_id where tc.num !=0 ";
						if (ServiceUtil.isNotNull(username)) {
							sql += " and tu.SUIN_LOGIN_NAME like ?  ";
						}

						// 查询记录sql
						String sqlQuery = " select tu.SUIN_LOGIN_NAME ,tc.num  from SYS_USER_INFO tu left join ( ";
						// 查询记录数sql
						String sqlCount = " select count(*)  from SYS_USER_INFO tu left join ( ";

						Query query = session.createSQLQuery(sqlQuery + sql + " order by tc.num " + dir);
						Query queryCount = session.createSQLQuery(sqlCount + sql);
						//参数标识
						int index = 0;
						if (ServiceUtil.isNotNull(startDate)) {
							query.setParameter(index, startDate);
							queryCount.setParameter(index, startDate);
							index ++;
						}
						if (ServiceUtil.isNotNull(endDate)) {
							query.setParameter(index, endDate);
							queryCount.setParameter(index, endDate);
							index ++;
						}
						if (ServiceUtil.isNotNull(username)) {
							query.setParameter(index, "%" + username + "%");
							queryCount.setParameter(index, "%" + username + "%");
						}
						String totalCount = "";
						//是否分页
						if(page.getLimit() >0){
							query.setFirstResult(page.getStart());
							query.setMaxResults(page.getLimit());
							totalCount = queryCount.list().get(0).toString();
						}
						List list = query.list();
						wrap.setResult(list);
						wrap.setMessage(totalCount);
						return wrap;
					}

				});
	}

	@Override
	public Wrapper getUploadResourcesCountList(final PageExtNative page) {
		return (Wrapper) this.getHibernateTemplate().execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						// 条件
						String userName = page.getMSearch().get("userName");
						String realName = page.getMSearch().get("realName");
						String beginD =  page.getMSearch().get("beginD");
						String endD =  page.getMSearch().get("endD");
						String dir =  page.getMSearch().get("dir");
						String sort =  page.getMSearch().get("sort");
						String sql = "";
						String dataSql = " select tu.SUIN_LOGIN_NAME ,tu.SUIN_NAME,t2.countNum  from SYS_USER_INFO tu left join (";
						String countSql = " select count(*)  from SYS_USER_INFO tu left join (";
						sql += " select count(ts.RSDA_USER_ID) as countNum,ts.RSDA_USER_ID from RES_SOURCE_DATA ts where 1=1 ";
						// 开始时间
						if (ServiceUtil.isNotNull(beginD)) {
							sql += " and ts.rsda_builddate >= to_date('" + beginD + "','yyyy-mm-dd')";
						}
						// 结束时间
						if (ServiceUtil.isNotNull(endD)) {
							sql += " and ts.rsda_builddate <= to_date('" + endD + "','yyyy-mm-dd')";
						}

						sql += " group by ts.RSDA_USER_ID having ts.RSDA_USER_ID in ( select u.SUIN_ID from SYS_USER_INFO u where 1=1 ";
						// 登录名
						if (ServiceUtil.isNotNull(userName)) {
							sql += " and u.SUIN_LOGIN_NAME like '%" + userName + "%'";
						}
						// 姓名
						if (ServiceUtil.isNotNull(realName)) {
							sql += " and u.SUIN_NAME like '%" + realName + "%'";
						}

						sql += " ) ) t2 on tu.suin_id = t2.RSDA_USER_ID  where t2.countNum !=0";

						// 登录名
						if (ServiceUtil.isNotNull(userName)) {
							sql += " and  tu.SUIN_LOGIN_NAME like '%" + userName + "%'";
						}
						// 姓名
						if (ServiceUtil.isNotNull(realName)) {
							sql += " and tu.SUIN_NAME  like '%" + realName + "%'";
						}
						// 排序
						if (ServiceUtil.isNotNull(sort)) {
							sql += " order by t2." + sort;
						}
						if (ServiceUtil.isNotNull(dir)) {
							sql += " " + dir;
						}

						Wrapper wrap = new Wrapper();
						//结果
						Query query = session.createSQLQuery(dataSql + sql);
						//总记录数
						Query queryCount = session.createSQLQuery(countSql + sql);
						String totalCount = "";
						//是否分页
						if(page.getLimit() >0){
							query.setFirstResult(page.getStart());
							query.setMaxResults(page.getLimit());		
							totalCount = queryCount.list().get(0).toString();
						}
						List list = query.list();
						wrap.setResult(list);
						wrap.setMessage(totalCount);
						return wrap;
					}
				});
	}
	@Override
	public Wrapper getResourcesUsedCountList(final PageExtNative page) {
		return (Wrapper) this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Wrapper wrap = new Wrapper();
				// 条件
				String resourcesName = page.getMSearch().get("resourcesName");
				String beginD =  page.getMSearch().get("beginD");
				String endD =  page.getMSearch().get("endD");
				String minHits =  page.getMSearch().get("minHits");
				String maxHits =  page.getMSearch().get("maxHits");
				String dir =  page.getMSearch().get("dir");
				String sql = " where 1=1  ";
				//资源类型
				if (ServiceUtil.isNotNull(resourcesName)) {
					sql += " and t.rsda_rlty_id=  " + resourcesName;
				}	
				if (ServiceUtil.isNotNull(minHits) ) {
					sql += " and t.rsda_looknum >=  " + minHits;
				}	
				if (ServiceUtil.isNotNull(maxHits) ) {
					sql += " and t.rsda_looknum <=  " + maxHits;
				}					
				
				sql += " and t.rsda_id in ( select  d.rsbh_rsda_id from RES_SOURCE_BROWSE_HISTOREY d where 1=1 ";
				
				if (ServiceUtil.isNotNull(beginD)) {
					sql += " and d.rsbh_date >=to_date(?,'yyyy-mm-dd') ";
				}		
				if (ServiceUtil.isNotNull(endD)) {
					sql += " and d.rsbh_date <=to_date(?,'yyyy-mm-dd')";
				}					
				sql += " )";
				
				//结果
				String dataSql = " select t.rsda_title,t.RSDA_LOOKNUM from RES_SOURCE_DATA t";
				//总记录数
				String sqlCount = " select count(*) from RES_SOURCE_DATA t";
				Query query = session.createSQLQuery(dataSql + sql + " order by t.RSDA_LOOKNUM " + dir);
				Query queryCount = session.createSQLQuery(sqlCount + sql);
				
				//参数标识
				int index = 0;
				if (ServiceUtil.isNotNull(beginD)) {
					query.setParameter(index, beginD);
					queryCount.setParameter(index, beginD);
					index ++;
				}
				if (ServiceUtil.isNotNull(endD)) {
					query.setParameter(index, endD);
					queryCount.setParameter(index, endD);
				}

				String totalCount = "";
				//是否分页
				if(page.getLimit() >0){
					query.setFirstResult(page.getStart());
					query.setMaxResults(page.getLimit());	
					totalCount = queryCount.list().get(0).toString();
				}
				List list = query.list();
				wrap.setResult(list);
				wrap.setMessage(totalCount);
				return wrap;
			}
		});
	}
	
	
	public String getResourceAddInfoJson(long rmstId, String beginYear,
			String endYear, int beginNum, int pageNum, String orderType,
			String orderColumn) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select rmst.RMST_NAME, YEAR(rsda.RSDA_BUILDDATE) as year, count(rmst.RMST_NAME) as resourcenum ");
		buffer.append("from res_meta_store rmst ");
		buffer.append("left join res_source_data rsda on rmst.rmst_id=rsda.rsda_rmst_id ");
		buffer.append(" where rmst.rmst_status=1 and rsda.rsda_status=3 ");
		if(rmstId > 0){
			buffer.append(" and rmst.rmst_id=").append(rmstId);
		}
		if(beginYear != null && beginYear.length() > 1){
			buffer.append(" and YEAR(rsda.RSDA_BUILDDATE)>=" + beginYear);
		}
		if(endYear != null && endYear.length() > 1){
			buffer.append(" and YEAR(rsda.RSDA_BUILDDATE)<=" + endYear);
		}
		buffer.append(" group by rmst.RMST_NAME ,YEAR(rsda.RSDA_BUILDDATE) ");
		buffer.append(" order by ").append(orderColumn).append(" ").append(orderType);
		String sql = buffer.toString();
		buffer = null;
		
		String jsondata = getListToJson(sql, null, beginNum, pageNum);
		return jsondata;
	}

	@Override
	public List<String[]> getResourceAddInfoList(long rmstId, String beginYear,
			String endYear) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select rmst.RMST_NAME, YEAR(rsda.RSDA_BUILDDATE) as year, count(rmst.RMST_NAME) as resourcenum ");
		buffer.append("from res_meta_store rmst ");
		buffer.append("left join res_source_data rsda on rmst.rmst_id=rsda.rsda_rmst_id ");
		buffer.append(" where rmst.rmst_status=1 and rsda.rsda_status=3 ");
		if(rmstId > 0){
			buffer.append(" and rmst.rmst_id=").append(rmstId);
		}
		if(beginYear != null && beginYear.length() > 1){
			buffer.append(" and YEAR(rsda.RSDA_BUILDDATE)>=" + beginYear);
		}
		if(endYear != null && endYear.length() > 1){
			buffer.append(" and YEAR(rsda.RSDA_BUILDDATE)<=" + endYear);
		}
		buffer.append(" group by rmst.RMST_NAME ,YEAR(rsda.RSDA_BUILDDATE) ");
		String sql = buffer.toString();
		buffer = null;
		
		List<String[]> list = getStrList(sql, null);
		return list;
	}

	@Override
	public Wrapper getResourcesHandlerCountList(final PageExtNative page) {
		return (Wrapper) this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Wrapper wrap = new Wrapper();
				/**
				 * 工作量统计:
				 * 工作类型：第一语加工、其它语加工、第一语审核和其它语审核
				 * 由于对资源的操作类型只是通过一个状态来设定的，所以，一次性查询不能统计出所有的工作类型。
				 * 此处的实现方式为：
				 * 1、知道工作类型时，指定工作类型查询。
				 * 2、当不知道工作类型时，需要分开查询。然后再将两次统计出来的结果存在一个结果集里面返回页面显示。
				 * 
				 */
				List resultList = new ArrayList();//结果集分两部分：加工和审核
				//查询条件
				String type =  page.getMSearch().get("type");// 工作类型：第一语种加工，其它语加工，第一语种审核，其它语审核。
				String endTime = page.getMSearch().get("endTime");
				String beginTime =  page.getMSearch().get("beginTime");
				String suinLoginName =  page.getMSearch().get("suinLoginName");
				String suinName =  page.getMSearch().get("suinName");
				String rmstId = page.getMSearch().get("rmstId");
				String sort = page.getSort();
				String dir = page.getDir();
				//相关的查询SQL语句
				String selectSql_build = "select rsl.rsla_username ,rsl.rsla_authorname , count(rsl.rsla_rsda_id) rsdaCount ,rsl.rsla_isfirst || '1'  ";
				String selectSql_check = "select rsl.rsla_check_loginname, rsl.rsla_check_suinname , count(rsl.rsla_rsda_id) rsdaCount ,rsl.rsla_isfirst || '2'  ";
				StringBuffer fromSql = new StringBuffer("from res_source_language rsl ");
				StringBuffer whereSql_build = new StringBuffer("");
				StringBuffer whereSql_check = new StringBuffer("");
				String ordersql_build = "order by ";
				String ordersql_check = "order by ";
				String groupSql_build = " group by rsl.rsla_username,rsl.rsla_authorname,rsl.rsla_isfirst  ";
				String groupSql_check = " group by rsl.rsla_check_loginname,rsl.rsla_check_suinname,rsl.rsla_isfirst  ";
				boolean isWhere_build = true;
				boolean isWhere_check = true;
				
				//默认情况下，只从资源语种表中统计数据，只有当添加了相关的查询条件后，才会去连接多张表。
				if(ServiceUtil.isNotNull(rmstId) && !"0".equals(rmstId)){
					fromSql.append(" left join res_source_data rsd on rsl.rsla_rsda_id = rsd.rsda_id ");
					fromSql.append(" left join res_meta_store rms on rsd.rsda_rmst_id = rms.rmst_id "); 
				}else{
					if(ServiceUtil.isNotNull(beginTime) || ServiceUtil.isNotNull(endTime)){
						fromSql.append(" left join res_source_data rsd on rsl.rsla_rsda_id = rsd.rsda_id ");
					}
				}
				//开始时间
				if(ServiceUtil.isNotNull(beginTime)){
					beginTime = beginTime.substring(0, 10);
					isWhere_build = createWhereAnd(whereSql_build, isWhere_build);
					whereSql_build.append(" rsd.rsda_builddate >= to_date('").append(beginTime).append("','yyyy-mm-dd') ");
					isWhere_check = createWhereAnd(whereSql_check, isWhere_check);
					whereSql_check.append(" rsd.rsda_builddate >= to_date('").append(beginTime).append("','yyyy-mm-dd') ");
				}
				//结束时间
				if(ServiceUtil.isNotNull(endTime)){
					endTime = endTime.substring(0, 10);
					isWhere_build = createWhereAnd(whereSql_build, isWhere_build);
					whereSql_build.append(" rsd.rsda_builddate <= to_date('").append(endTime).append("','yyyy-mm-dd') ");
					isWhere_check = createWhereAnd(whereSql_check, isWhere_check);
					whereSql_check.append(" rsd.rsda_builddate <= to_date('").append(endTime).append("','yyyy-mm-dd') ");
				}
				//特色库
				if(ServiceUtil.isNotNull(rmstId) && !"0".equals(rmstId)){
					isWhere_build = createWhereAnd(whereSql_build, isWhere_build);
					whereSql_build.append(" rms.rmst_id = ").append(rmstId);
					isWhere_check = createWhereAnd(whereSql_check, isWhere_check);
					whereSql_check.append(" rms.rmst_id = ").append(rmstId);
				}
				//用户登录名
				if(ServiceUtil.isNotNull(suinLoginName)){
					isWhere_build = createWhereAnd(whereSql_build, isWhere_build);
					isWhere_check = createWhereAnd(whereSql_check, isWhere_check);
					if(ServiceUtil.isNotNull(type) && "1".equals(type) || ServiceUtil.isNotNull(type) && "2".equals(type)){
						// 加工
						whereSql_build.append(" rsl.rsla_username like '%").append(suinLoginName).append("%' ");
					}else if(ServiceUtil.isNotNull(type) && "3".equals(type) || ServiceUtil.isNotNull(type) && "4".equals(type)){
						// 审核
						whereSql_check.append(" rsl.rsla_check_loginname like '%").append(suinLoginName).append("%' ");
					}else{
						whereSql_build.append(" rsl.rsla_username like '%").append(suinLoginName).append("%' ");
						whereSql_check.append(" rsl.rsla_check_loginname like '%").append(suinLoginName).append("%' ");
					}
				}
				//用户姓名
				if(ServiceUtil.isNotNull(suinName)){
					isWhere_build = createWhereAnd(whereSql_build, isWhere_build);
					isWhere_check = createWhereAnd(whereSql_check, isWhere_check);
					if(ServiceUtil.isNotNull(type) && "1".equals(type) || ServiceUtil.isNotNull(type) && "2".equals(type)){
						// 加工
						whereSql_build.append(" rsl.rsla_authorname like '%").append(suinName).append("%' ");
					}else if(ServiceUtil.isNotNull(type) && "3".equals(type) || ServiceUtil.isNotNull(type) && "4".equals(type)){
						// 审核
						whereSql_check.append(" rsl.rsla_check_suinname like '%").append(suinName).append("%' ");
					}else{
						whereSql_build.append(" rsl.rsla_authorname like '%").append(suinName).append("%' ");
						whereSql_check.append(" rsl.rsla_check_suinname like '%").append(suinName).append("%' ");
					}
				}
				//根据工作类型，对工作量进行统计
				if(ServiceUtil.isNotNull(type) && "1".equals(type)){
					// 第一语种加工
					isWhere_build = createWhereAnd(whereSql_build, isWhere_build);
					whereSql_build.append(" rsl.rsla_isfirst = 1 ");
				}else if(ServiceUtil.isNotNull(type) && "2".equals(type)){
					// 其它语加工
					isWhere_build = createWhereAnd(whereSql_build, isWhere_build);
					whereSql_build.append(" rsl.rsla_isfirst != 1 ");
				}else if(ServiceUtil.isNotNull(type) && "3".equals(type)){
					// 第一语种审核
					isWhere_check = createWhereAnd(whereSql_check, isWhere_check);
					whereSql_check.append(" rsl.rsla_status in (1,3) and rsl.rsla_isfirst = 1 ");
				}else if(ServiceUtil.isNotNull(type) && "4".equals(type)){
					// 其它语审核
					isWhere_check = createWhereAnd(whereSql_check, isWhere_check);
					whereSql_check.append(" rsl.rsla_status in (1,3) and rsl.rsla_isfirst != 1 ");
				}
				// 去除不合法的数据 
				isWhere_build = createWhereAnd(whereSql_build, isWhere_build);
				isWhere_check = createWhereAnd(whereSql_check, isWhere_check);
				whereSql_build.append(" rsl.rsla_username is not null and rsl.rsla_authorname is not null ");
				whereSql_check.append(" rsl.rsla_check_loginname is not null and rsl.rsla_check_suinname is not null ");
				
				//添加排序：1、公有的排序字段。2、需要区分的排序字段。
				if(ServiceUtil.isNotNull(sort) && "rsdaCount".equals(sort)){
					ordersql_build += " rsdaCount ";
					ordersql_check += " rsdaCount "; 
				}
				//需要区分的排序字段
				if(ServiceUtil.isNotNull(type) && "1".equals(type) || ServiceUtil.isNotNull(type) && "2".equals(type)){
					// 加工
					if(ServiceUtil.isNotNull(sort) && "rsla_username".equals(sort)){
						ordersql_build += " rsl.rsla_username ";
					}else if(ServiceUtil.isNotNull(sort) && "rsla_authorname".equals(sort)){
						ordersql_build += " rsl.rsla_authorname ";
					}
				}else if(ServiceUtil.isNotNull(type) && "3".equals(type) || ServiceUtil.isNotNull(type) && "4".equals(type)){
					// 审核
					if(ServiceUtil.isNotNull(sort) && "rsla_username".equals(sort)){
						ordersql_check += " rsl.rsla_check_loginname ";
					}else if(ServiceUtil.isNotNull(sort) && "rsla_authorname".equals(sort)){
						ordersql_check += " rsl.rsla_check_suinname ";
					}
				}else{
					if(ServiceUtil.isNotNull(sort) && "rsla_username".equals(sort)){
						ordersql_build += " rsl.rsla_username ";
						ordersql_check += " rsl.rsla_check_loginname ";
					}else if(ServiceUtil.isNotNull(sort) && "rsla_authorname".equals(sort)){
						ordersql_build += " rsl.rsla_authorname ";
						ordersql_check += " rsl.rsla_check_suinname ";
					}
				}
				//公有的排序字段
				if(ServiceUtil.isNotNull(sort) && "rsla_isfirst_status".equals(sort)){
					ordersql_build += " rsl.rsla_isfirst ";
					ordersql_check += " rsl.rsla_isfirst ";
				}
				if(ServiceUtil.isNotNull(dir)){
					ordersql_build += dir;
					ordersql_check += dir;
				}else{
					ordersql_build += " DESC ";
					ordersql_check += " DESC ";
				}
				//执行SQL
				List list_build = null;
				List list_check = null;
				String build = selectSql_build + fromSql.toString() + whereSql_build.toString() + groupSql_build + ordersql_build;
				String check = selectSql_check + fromSql.toString() + whereSql_check.toString() + groupSql_check + ordersql_check;
				if(ServiceUtil.isNotNull(type) && "1".equals(type) || ServiceUtil.isNotNull(type) && "2".equals(type)){
					// 加工
					list_build = session.createSQLQuery(build).list();
					resultList.addAll(list_build);
				}else if(ServiceUtil.isNotNull(type) && "3".equals(type) || ServiceUtil.isNotNull(type) && "4".equals(type)){
					// 审核
					list_check = session.createSQLQuery(check).list();
					resultList.addAll(list_check);
				}else{
					list_build = session.createSQLQuery(build).list();
					list_check = session.createSQLQuery(check).list();
					resultList.addAll(list_build);
					resultList.addAll(list_check);
				}
				wrap.setResult(resultList);
				/*
				 * 当导出到Excel时，执行以下程序
				 */
				String methodType =  page.getMSearch().get("methodType");
				if("1".equals(methodType)){
					String temp_build = whereSql_build.toString();
					String temp_check = whereSql_check.toString();
					//第一语加工
					String countSql = "select count(*) "+fromSql.toString();
					String firstLanBuild = countSql;
					if(ServiceUtil.isNotNull(temp_build)){
						firstLanBuild += temp_build + " and ";
					}else{
						firstLanBuild += " where ";
					}
					firstLanBuild += " rsl.rsla_isfirst = 1 ";
					List firstLanBuildList = session.createSQLQuery(firstLanBuild).list();
					//其它语加工
					String otherLanBuild = countSql;
					if(ServiceUtil.isNotNull(temp_build)){
						otherLanBuild += temp_build + " and ";
					}else{
						otherLanBuild += " where ";
					}
					otherLanBuild += " rsl.rsla_isfirst != 1 ";
					List otherLanBuildList = session.createSQLQuery(otherLanBuild).list();
					//第一语审核
					String firstLanCheck = countSql;
					if(ServiceUtil.isNotNull(temp_check)){
						firstLanCheck += temp_check + " and ";
					}else{
						firstLanCheck += " where ";
					}
					firstLanCheck += " rsl.rsla_status in (1,3) and rsl.rsla_isfirst = 1 ";
					List firstLanCheckList = session.createSQLQuery(firstLanCheck).list();
					//其它语审核
					String otherLanCheck = countSql;
					if(ServiceUtil.isNotNull(temp_check)){
						otherLanCheck += temp_check + " and ";
					}else{
						otherLanCheck += " where ";
					}
					otherLanCheck += " rsl.rsla_status in (1,3) and rsl.rsla_isfirst != 1 ";
					List otherLanCheckList = session.createSQLQuery(otherLanCheck).list();
					//保存统计总计信息
					int firstLanBuildCount = 0;
					int otherLanBuildCount = 0;
					int firstLanCheckCount = 0;
					int otherLanCheckCount = 0;
					if(firstLanBuildList.size() > 0){
						firstLanBuildCount = ((BigDecimal)firstLanBuildList.get(0)).intValue();
					}
					if(otherLanBuildList.size() > 0){
						otherLanBuildCount = ((BigDecimal)otherLanBuildList.get(0)).intValue();
					}
					if(firstLanCheckList.size() > 0){
						firstLanCheckCount = ((BigDecimal)firstLanCheckList.get(0)).intValue();
					}
					if(otherLanCheckList.size() > 0){
						otherLanCheckCount = ((BigDecimal)otherLanCheckList.get(0)).intValue();
					}
//					int allCount = firstLanBuildCount + otherLanBuildCount + firstLanCheckCount + otherLanCheckCount;
					wrap.setMessage("第一语加工："+firstLanBuildCount+"条，其它语加工："+otherLanBuildCount+"条，第一语审核："+firstLanCheckCount+"条,其它语审核："+otherLanCheckCount+"条！");
				}
				return wrap;
			}
		});
	}
	
	protected boolean createWhereAnd(StringBuffer whereHql, boolean isWhere) {
		if(isWhere) {
			whereHql.append(" where ");
			isWhere = false;
		}else {
			whereHql.append(" and ");
		}
		return isWhere;
	}
	
	@Override
	public String getRisMetaStoreJson(Pagination pagination , String  sort,String dir, int year, int month) { 
//		String sql ="select rsda_rmst_id as rmstId,rmst_name as rmstName,sum(rsda_looknum) as rmstNum "+
//				"from(select s.rsda_rmst_id,m.rmst_name,s.rsda_looknum "+
//							"from RES_SOURCE_DATA s "+
//							"left join RES_META_STORE m "+
//							"on s.rsda_rmst_id=m.rmst_id) "+
//				"group by rsda_rmst_id,rmst_name order by "+sort+" "+dir+" ";
		
		String dateStr = "";
		String patter = "";
		if(year > 0){
			dateStr += year;
			patter = "yyyy";
			if(month > 0){
				if(month < 10){
					dateStr += "-0" + month;
				}else{
					dateStr += "-" + month;
				}
				patter += "-MM";
			}
		}
		String dateWhere = "";
		if(CommonUtil.isNotNull(dateStr)){
			String mysqlPattern = "yyyy".equals(patter) ? "%Y" : "%Y-%m";
			dateWhere = " AND DATE_FORMAT(rsbh.RSBH_DATE, '"+mysqlPattern+"') = '"+dateStr+"'";
		}
		
		String sql = "select rmst.rmst_id as rmstId, rmst.rmst_name as rmstName,";
		sql += "(select count(*) from RES_SOURCE_BROWSE_HISTOREY rsbh,RES_SOURCE_DATA rsda,RES_META_STORE m";
		sql += " where rsbh.rsbh_rsda_id=rsda.rsda_id";
		sql += " and rsda.rsda_rmst_id=m.rmst_id";
		sql += " and m.rmst_id=rmst.rmst_id";
		sql += dateWhere;
		sql += ") as rmstNum";
		sql += " from RES_META_STORE rmst";

		System.out.println("sql：" + sql);
		String jsondata = getListToJson(sql, null, pagination.getCurrentPage(), pagination.getPageResults());
		return jsondata;
	}
	
	@Override
	public String getRisMetaStoreTwoJson(Pagination pagination,Long MetaId,int year, int month, String sort,
			String dir) {
		String dateStr = "";
		String patter = "";
		if(year > 0){
			dateStr += year;
			patter = "yyyy";
			if(month > 0){
				if(month < 10){
					dateStr += "-0" + month;
				}else{
					dateStr += "-" + month;
				}
				patter += "-MM";
			}
		}
		String dateWhere = "";
		if(CommonUtil.isNotNull(dateStr)){
			String mysqlPattern = "yyyy".equals(patter) ? "%Y" : "%Y-%m";
			dateWhere = " AND DATE_FORMAT(rsbh.RSBH_DATE, '"+mysqlPattern+"') = '"+dateStr+"'";
		}
		
		String sql = "select rlty.rlty_id as rltyId, rlty.rlty_name as rltyName,"+
		 "(select count(*) from RES_SOURCE_BROWSE_HISTOREY rsbh,RES_SOURCE_DATA rsda,RES_LITER_TYPE t"+
		 " where rsbh.rsbh_rsda_id=rsda.rsda_id "+
		 " and rsda.rsda_rlty_id=t.rlty_id  "+
		 " and t.rlty_id=rlty.rlty_id  "+
		 " and rsda.rsda_rmst_id = '"+MetaId+"' ";
		sql += dateWhere;
		sql += ") as countNum";
		sql += " from RES_LITER_TYPE rlty";
		
		sql +=" order by "+sort+" "+dir+" ";
		String jsondata = getListToJson(sql, null, pagination.getCurrentPage(), pagination.getPageResults());
		return jsondata;
	}
	
	@Override
	public String getPointcountToJson(Pagination pagination, String loginname,
			String realname, String sort, String dir) {
		try {
			String hql ="select suinId,suinLoginName,suinName,suinIntegral from SysUserinfo sysu where suinId<>0 ";
			List<Object> prama = new ArrayList<Object>();
			if(CommonUtil.isNotNull(loginname.trim())){
				hql+=" and sysu.suinLoginName like ? ";
				prama.add("%"+loginname.trim()+"%");
			}
			if(CommonUtil.isNotNull(realname.trim())){
				hql+=" and sysu.suinName like ? ";
				prama.add("%"+realname.trim()+"%");
			}
			
			hql +=" order by "+sort+" "+dir+" ";
			
			List<Object> list = new ArrayList<Object>();
			if(prama.size()>0){
				list = getPageHibernateTemplate().find(pagination, hql,prama.toArray());
			}else {
				list = getPageHibernateTemplate().find(pagination, hql );
			}
			String[] keys = new String[]{"suinId","suinLoginName","suinName","suinIntegral"}; 
			String jsonData = CommonUtil.createJsonTable(list, keys, pagination.getTotalResults() + "");
			return jsonData;
		} catch (Exception e) {
			logger.error(e.getMessage());
				e.printStackTrace();
				return null;
		}
	}
	
	/**
	 * 用户操作统计列表
	 *
	 * @author MaChao
	 * @date Nov 30, 2015
	 * @time 12:56:51 PM
	 */
	@Override
	public Wrapper getUserAccessCountList(final PageExtNative page) {
		return (Wrapper) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					@Override
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
						Wrapper wrap = new Wrapper();
						String countType = page.getMSearch().get("countType");
						String username = page.getMSearch().get("userName");
						String startDate = page.getMSearch().get("beginD");
						String endDate = page.getMSearch().get("endD");
						String dir = page.getMSearch().get("dir");
						String sql = "";      
						sql += " select t.saco_suin_name,t.saco_type,count(t.saco_suin_name) as num from sys_access_counter t where 1=1 ";
						
						if (ServiceUtil.isNotNull(countType)) {
							sql += " and  t.saco_type =? "; 
						}
						if (ServiceUtil.isNotNull(startDate)) {
							sql += " and  t.saco_date >=to_date(?,'yyyy-MM-dd:HH24:mi:ss') ";
						}
						if (ServiceUtil.isNotNull(endDate)) {
							sql += " and  t.saco_date <=to_date(?,'yyyy-MM-dd:HH24:mi:ss') ";
						}
						if (ServiceUtil.isNotNull(username)) {
							sql += " and t.saco_suin_name like ?  ";
						}
						sql += "group by t.saco_suin_name,t.saco_type";
						
						// 查询记录数sql
						String sqlCount = " select count(*)  from ("+sql +")";
						
						Query query = session.createSQLQuery( sql + " order by num " + dir);
						Query queryCount = session.createSQLQuery(sqlCount );
						//参数标识
						int index = 0;
						if (ServiceUtil.isNotNull(countType)) {
							query.setParameter(index, countType);
							queryCount.setParameter(index, countType);
							index ++;
						}
						if (ServiceUtil.isNotNull(startDate)) {
							query.setParameter(index, startDate);
							queryCount.setParameter(index, startDate);
							index ++;
						}
						if (ServiceUtil.isNotNull(endDate)) {
							query.setParameter(index, endDate);
							queryCount.setParameter(index, endDate);
							index ++;
						}
						if (ServiceUtil.isNotNull(username)) {
							query.setParameter(index, "%" + username + "%");
							queryCount.setParameter(index, "%" + username + "%");
						}
						String totalCount = "";
						//是否分页
						if(page.getLimit() >0){
							query.setFirstResult(page.getStart());
							query.setMaxResults(page.getLimit());
							totalCount = queryCount.list().get(0).toString();
						}
						List list = query.list();
						wrap.setResult(list);
						wrap.setMessage(totalCount);
						return wrap;
					}
					
				});
	}

	/**
	 * 用户操作日志列表-json
	 *
	 * @author MaChao
	 * @date Nov 30, 2015
	 * @time 3:20:00 PM
	 */
	@Override
	public String getLoglistJson(Pagination pagination, String name,String type,String beginD,String endD,String orderColumn,String orderType) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select");
		buffer.append(" saco.sacoId,");
		buffer.append(" saco.sacoSuinName,");
		buffer.append(" saco.sacoType,");
		buffer.append(" saco.sacoDate,");
		buffer.append(" saco.sacoContent,");
		buffer.append(" saco.sacoIp");
		buffer.append(" from SysAccessCounter saco where 1=1");
		
		//参数
		List<Object> params = new LinkedList<Object>();
		if(CommonUtil.isNotNull(name)){
			buffer.append(" and saco.sacoSuinName =?");
			params.add(name);
		}
		if(CommonUtil.isNotNull(type)){
			buffer.append(" and saco.sacoType =?");
			params.add(type);
		} 
		if (ServiceUtil.isNotNull(beginD)) {
			buffer.append(" and saco.sacoDate >= to_date(?,'yyyy-MM-dd:HH24:mi:ss')");
			params.add(beginD);
		}
		if (ServiceUtil.isNotNull(endD)) {
			buffer.append(" and saco.sacoDate <= to_date(?,'yyyy-MM-dd:HH24:mi:ss')");
			params.add(endD);
		}
		buffer.append(" order by saco.").append(orderColumn).append(" "+orderType);
		
		String hql = buffer.toString();
		buffer = null;
		List list =null;
		if(pagination == null){
			list = this.getHibernateTemplate().find(hql, params.toArray());
		}else{
			list = this.getPageHibernateTemplate().find(pagination,hql, params.toArray());
		}
		String[] keys = new String[]{"sacoId", "sacoSuinName", "sacoType", "sacoDate", "sacoContent","sacoIp"}; 
		String jsondata = CommonUtil.createJsonTable(list, keys, pagination.getTotalResults() + "");
		return jsondata;
	}
	
	/**
	 * 用户操作日志列表-obj
	 *
	 * @author MaChao
	 * @date Dec 1, 2015
	 * @time 11:34:42 AM
	 */
	@Override
	public List getLoglistObj(Pagination pagination, String name,String type,String beginD,String endD,String orderColumn,String orderType) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select");
		buffer.append(" saco.sacoSuinName,");
		buffer.append(" saco.sacoType,"); 
		buffer.append(" saco.sacoDate,");
		buffer.append(" saco.sacoContent,");
		buffer.append(" saco.sacoIp");
		buffer.append(" from SysAccessCounter saco where 1=1");
		
		//参数
		List<Object> params = new LinkedList<Object>();
		if(CommonUtil.isNotNull(name)){
			buffer.append(" and saco.sacoSuinName like ?");
			params.add("%"+name+"%"); 
		}
		if(CommonUtil.isNotNull(type)){
			buffer.append(" and saco.sacoType =?");
			params.add(type);
		}
		if (ServiceUtil.isNotNull(beginD)) {
			buffer.append(" and saco.sacoDate >= to_date(?,'yyyy-MM-dd:HH24:mi:ss')");
			params.add(beginD);
		}
		if (ServiceUtil.isNotNull(endD)) {
			buffer.append(" and saco.sacoDate <= to_date(?,'yyyy-MM-dd:HH24:mi:ss')");
			params.add(endD);
		}
		
		buffer.append(" order by saco.").append(orderColumn).append(" "+orderType);
		
		String hql = buffer.toString();
		buffer = null;
		List list =null;
		if(pagination == null){
			list = this.getHibernateTemplate().find(hql, params.toArray());
		}else{
			list = this.getPageHibernateTemplate().find(pagination,hql, params.toArray());
		}
		return list;
	}
}
