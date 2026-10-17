package cn.com.adtech.riss2.bso.sc.docu;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResLiterType;
import cn.com.adtech.riss2.domain.sc.docu.ScDocuDelivery;
import cn.com.adtech.riss2.jdbcms.SqlBasicUnit;
/**
 * 
 * @author MaChao
 *
 * @date Aug 27, 2015 - 3:05:58 PM
 */
@SuppressWarnings("unchecked")
public class DocuDeliveryServiceImp extends CommonServiceImp implements IDocuDeliveryService {

	@Override
	public String getRltyString(long selected) {
		String hql = "from ResLiterType rlty where rlty.rltyType=1 ";
		List<ResLiterType> list = this.getHibernateTemplate().find(hql);
		StringBuffer buffer = new StringBuffer();
		for(ResLiterType rlty : list){
			buffer.append("<option value='" + rlty.getRltyId()+ "'");
			if(rlty.getRltyId() == selected) {
				buffer.append(" selected='true' ");
			}
			buffer.append(">" + rlty.getRltyName() + "</option>\n");
		}
		String options = buffer.toString();
		buffer = null;
		return options;
	}

	@Override
	public List getDocuList(Pagination pagination,Long userId) {
		StringBuffer fromHql = new StringBuffer();
		fromHql.append(" from ScDocuDelivery sdde ");
		fromHql.append(" where sdde.sddeSuinId = "+userId);
		fromHql.append(" order by sdde.sddeCreationTime DESC ");
		String hql = fromHql.toString();
		return getPageHibernateTemplate().find(pagination, hql);
	}
	
	@Override
	public List getDocuList(Pagination pagination, Map<String, Object> map) {
		String selectHql = "select sdde.sddeId ," +
				"sdde.sddeDocuTitle ," +
				"sdde.sysUserinfo.suinName ," +
				"sdde.sysUserinfo.suinLoginName ," +
				"sdde.sddeCreationTime  ," +
				"sdde.sddeStatus  ";
		StringBuffer fromHql = new StringBuffer("from ScDocuDelivery sdde ");
		StringBuffer orderHql = new StringBuffer("order by ");
		List<Object> param = new ArrayList<Object>();
		boolean isWhere = true;
		
		// 添加查询条件
		if(map.containsKey("sddeTitle")) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sdde.sddeDocuTitle like ? ");
			param.add("%" + map.get("sddeTitle") + "%");
		}
		if(map.containsKey("loginName")) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sdde.sysUserinfo.suinLoginName like ? ");
			param.add("%" + map.get("loginName") + "%");
		}
		if(map.containsKey("sddeStatus")) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sdde.sddeStatus = ? ");
			param.add(map.get("sddeStatus"));
		}
		
		// 添加排序
		if("suinName".equals(map.get("sort")) || "suinLoginName".equals(map.get("sort"))) {
			orderHql.append("sdde.sysUserinfo.").append(map.get("sort"));
		}else {
			orderHql.append("sdde.").append(map.get("sort"));
		}
		
		orderHql.append(" ").append(map.get("dir")).append(" , sdde.sddeCreationTime DESC ");
		String countHql = "select count(*) " + fromHql.toString();
		String hql = selectHql + fromHql.toString() + orderHql.toString();
		return getPageHibernateTemplate().find(pagination, hql, countHql, param.toArray());
	}

	@Override
	public String getSubjectOptions(final long selected,final long rootId,final String type) {
		final String sql = SqlBasicUnit.subjectHierarchySql();
		String options = getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer buffer = new StringBuffer();
				try {
					Connection conn = session.connection();
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql);
					buffer.append("<option value=''>请选择...</option>");
					String icon = "├";
					while(rs.next()) {
						int level = rs.getInt("level");
						if(level == 1) {
							if("1".equals(type))
								continue;
						}
						for(int i = 0; i < level - 2; i++) {
							icon += "─";
						}
						buffer.append("<option value='" + rs.getString("RSTY_ID") + "'");
						if(rs.getLong("RSTY_ID") == selected) {
							buffer.append(" selected='true' ");
						}
						buffer.append(">" + icon + rs.getString("RSTY_NAME") + "</option>\n");
						icon = "├";
					}
					try {
						if(rs != null)
							rs.close();
						rs = null;
						stmt = null;
					}catch(SQLException e) {
						e.printStackTrace();
					}
				}catch(SQLException e) {
					e.printStackTrace();
					System.out.println("在getOptions中发生异常，异常SQL：" + sql);
					return "";
				}
				String options = buffer.toString();
				buffer = null;
				return options;
			}
		}).toString();
		return options;
	}

	/**
	 * 根据isWhere 生成的hql，开头部分是使用where 还是 and
	 */
	protected boolean createWhereAnd(StringBuffer whereHql, boolean isWhere) {
		if(isWhere) {
			whereHql.append(" where ");
			isWhere = false;
		}else {
			whereHql.append(" and ");
		}
		return isWhere;
	}
}
