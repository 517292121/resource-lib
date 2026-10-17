package cn.com.adtech.riss2.bso.sc;

import java.util.ArrayList;
import java.util.List;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.sc.ScConsultation;
import cn.com.adtech.riss2.domain.sc.ScConsultationType;
import cn.com.adtech.riss2.domain.sc.ScFaqLibrary;
import cn.com.adtech.riss2.domain.sc.ScIssue;
import cn.com.adtech.riss2.domain.sc.ScIssueReply;
import cn.com.adtech.riss2.domain.sc.ScIssueType;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class ScServiceImp extends CommonServiceImp implements IScService {
	@Override
	public List getScIssueTypeList(String scitName, String sort, String dir) {
		List<Object> param = new ArrayList<Object>();
		String hql = "select sit.scitId , sit.scitName , sit.scitIndex  from ScIssueType sit ";
		if(CommonUtil.isNotNull(scitName)) {
			hql += "where sit.scitName = ? ";
			param.add(scitName);
		}
		if(CommonUtil.isNotNull(sort) && CommonUtil.isNotNull(dir))
			hql += "order by sit." + sort + " " + dir;
		return this.getHibernateTemplate().find(hql, param.toArray());
	}

	@Override
	public List getScIssueTypeList() {
		return this.getHibernateTemplate().find("from ScIssueType sit order by sit.scitIndex DESC ");
	}

	@Override
	public List getScIssueReplyList(Pagination pagination, Long scisId, Long scirId, String type) {
		List<Object> param = new ArrayList<Object>();
		StringBuilder fromHql = new StringBuilder("from ScIssueReply sir where sir.scirIsuse = 1 ");
		String orderHql = "order by sir.scirReplyTime ASC ";
		String selectHql = "";
		if(scisId != null && scisId > 0) {
			fromHql.append("and sir.scirScisId = ? ");
			param.add(scisId);
		}
		if(scirId != null && scirId > 0) {
			fromHql.append("and sir.scirId != ? ");
			param.add(scirId);
		}
		String countHql = "select count(*) " + fromHql.toString();
		if("1".equals(type))
			selectHql = "select sir.scirId ,sir.sysUserinfo.suinName ,sir.scirReplyTime ,sir.scirContent ,sir.scirAttachmentName ,sir.scirAttachmentUrl ";
		String hql = selectHql + fromHql.toString() + orderHql.toString();
		return getPageHibernateTemplate().find(pagination, hql, countHql, param.toArray());
	}

	@Override
	public List getScIssueList(Pagination pagination, Long scitId, String condition, Long scisState, String sort, String dir) {
		String selectHql = "select sis.scisId , sis.scisTitle , sis.scIssueType.scitName , sis.scisSuin.suinName , sis.scisCreationTime , sis.scisState , sis.scisReplyNumber , sis.scisIsuse , sis.scisLastReplyTime ";
		StringBuffer fromHql = new StringBuffer("from ScIssue sis ");
		StringBuffer orderHql = new StringBuffer("order by ");
		List<Object> param = new ArrayList<Object>();
		boolean isWhere = true;
		// 添加查询条件
		if(scitId != null && scitId > -1) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sis.scisScitId = ? ");
			param.add(scitId);
		}
		if(CommonUtil.isNotNull(condition) && condition.length() > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sis.scisTitle like ? ");
			param.add("%" + condition + "%");
		}
		if(scisState != null && scisState > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sis.scisState = ? ");
			param.add(scisState);
		}
		// 添加排序
		if(CommonUtil.isNotNull(sort) && CommonUtil.isNotNull(dir)) {
			if("scitName".equals(sort)) {
				orderHql.append("sis.scIssueType.scitName");
			}else if("suinName".equals(sort)) {
				orderHql.append("sis.scisSuin.suinName");
			}else {
				orderHql.append("sis.").append(sort);
			}
			orderHql.append(" ").append(dir);
		}
		String countHql = "select count(*) " + fromHql.toString();
		String hql = selectHql + fromHql.toString() + orderHql.toString();
		return getPageHibernateTemplate().find(pagination, hql, countHql, param.toArray());
	}

	/**
	 * 根据isWhere 生成的hql，开头部分是使用where 还是 and
	 * 
	 * @param whereHql
	 * @param isWhere
	 * @return
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

	@Override
	public List getScIssueReplyList(Pagination pagination, Long scitId, String condition, Short scirGrade, String sort, String dir) {
		String selectHql = "select sir.scirId , sir.scIssue.scIssueType.scitName, sir.scIssue.scisTitle , sir.sysUserinfo.suinName , sir.scirReplyTime , sir.scirGrade , sir.scirIsuse , sir.scirContent ";
		StringBuffer fromHql = new StringBuffer(" from ScIssueReply sir ");
		StringBuffer orderHql = new StringBuffer("order by ");
		List<Object> param = new ArrayList<Object>();
		boolean isWhere = true;
		// 添加查询条件
		if(scitId != null && scitId > -1) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sir.scIssue.scisScitId = ? ");
			param.add(scitId);
		}
		if(CommonUtil.isNotNull(condition) && condition.length() > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sir.scIssue.scisTitle like ? ");
			param.add("%" + condition + "%");
		}
		if(scirGrade != null && scirGrade > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sir.scirGrade = ? ");
			param.add(scirGrade);
		}
		// 添加排序
		if(CommonUtil.isNotNull(sort) && CommonUtil.isNotNull(dir)) {
			if("scitName".equals(sort)) {
				orderHql.append("sir.scIssue.scIssueType.scitName ");
			}else if("suinName".equals(sort)) {
				orderHql.append("sir.sysUserinfo.suinName ");
			}else if("scisTitle".equals(sort)) {
				orderHql.append("sir.scIssue.scisTitle ");
			}else {
				orderHql.append("sir.").append(sort);
			}
			orderHql.append(" ").append(dir);
		}
		String countHql = "select count(*) " + fromHql.toString();
		String hql = selectHql + fromHql.toString() + orderHql.toString();
		return getPageHibernateTemplate().find(pagination, hql, countHql, param.toArray());
	}

	@Override
	public List getScFaqTypeList(Long scftId, Long scftParentId, String scftName, Long scftGrade, String type, Long scftIndex) {
		StringBuffer fromHql = new StringBuffer("from ScFaqType sft ");
		List<Object> param = new ArrayList<Object>();
		String orderHql = "order by sft.scftParentId , sft.scftIndex ASC";
		boolean isWhere = true;
		if(scftId != null && scftId > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sft.scftId = ? ");
			param.add(scftId);
		}
		if(scftParentId != null && scftParentId > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sft.scftParentId = ? ");
			param.add(scftParentId);
		}
		if(scftName != null && scftName.trim().length() > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sft.scftName = ? ");
			param.add(scftName);
		}
		if(scftGrade != null && scftGrade > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sft.scftGrade = ? ");
			param.add(scftGrade);
		}
		if("1".equals(type)) {
			orderHql = " order by sft.scftParentId ,sft.scftIndex DESC ";
		}
		if("2".equals(type)) {
			orderHql = " order by sft.scftParentId ,sft.scftIndex DESC ";
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sft.scftIndex < ? ");
			param.add(scftIndex);
		}
		if("3".equals(type)) {
			orderHql = " order by sft.scftParentId ,sft.scftIndex ASC ";
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sft.scftIndex > ? ");
			param.add(scftIndex);
		}
		return this.getHibernateTemplate().find(fromHql.toString() + orderHql, param.toArray());
	}

	@Override
	public List getScFaqLibraryList(Pagination pagination, String scflQuestion, String sort, String dir) {
		String selectHql = "select sfl.scflId , sfl.scFaqType.scftName , sfl.scflQuestion , sfl.scflTopTime , sfl.scflIstop ";
		StringBuffer fromHql = new StringBuffer(" from ScFaqLibrary sfl ");
		StringBuffer orderHql = new StringBuffer("order by ");
		List<Object> param = new ArrayList<Object>();
		// 查询条件
		if(CommonUtil.isNotNull(scflQuestion)) {
			fromHql.append(" where sfl.scflQuestion = ? ");
			param.add(scflQuestion);
		}
		// 添加排序
		if(CommonUtil.isNotNull(sort) && CommonUtil.isNotNull(dir)) {
			if("scftName".equals(sort)) {
				orderHql.append("sfl.scFaqType.scftName ");
			}else {
				orderHql.append("sfl.").append(sort);
			}
			orderHql.append(" ").append(dir);
		}
		String countHql = "select count(*) " + fromHql.toString();
		String hql = selectHql + fromHql.toString() + orderHql.toString();
		return getPageHibernateTemplate().find(pagination, hql, countHql, param.toArray());
	}

	@Override
	public List getScIssueType(String scitName, Long scitIndex, String type) {
		StringBuffer fromHql = new StringBuffer("from ScIssueType sit ");
		List<Object> param = new ArrayList<Object>();
		String orderHql = "order by sit.scitIndex ASC";
		boolean isWhere = true;
		if(scitName != null && scitName.trim().length() > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sit.scitName = ? ");
			param.add(scitName);
		}
		if("1".equals(type)) {
			orderHql = " order by sit.scitIndex  DESC ";
		}
		if("2".equals(type)) {
			orderHql = " order by sit.scitIndex DESC ";
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sit.scitIndex < ? ");
			param.add(scitIndex);
		}
		if("3".equals(type)) {
			orderHql = " order by sit.scitIndex ASC ";
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sit.scitIndex > ? ");
			param.add(scitIndex);
		}
		return this.getHibernateTemplate().find(fromHql.toString() + orderHql, param.toArray());
	}

	@Override
	public List getScIssueList(Pagination pagination, Long scitId, Long suinId, String scisTitle, Long scisState, String type) {
		StringBuffer fromHql = new StringBuffer("from ScIssue sis ");
		StringBuffer orderHql = new StringBuffer("order by sis.scisCreationTime DESC ");
		List<Object> param = new ArrayList<Object>();
		boolean isWhere = true;
		isWhere = createWhereAnd(fromHql, isWhere);
		fromHql.append(" sis.scisIsuse = 1 ");
		// 添加查询条件
		if(scitId != null && scitId > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sis.scisScitId = ? ");
			param.add(scitId);
		}
		if(suinId != null && !"1".equals(type)) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sis.scisSuinId = ? ");
			param.add(suinId);
		}
		if(CommonUtil.isNotNull(scisTitle) && scisTitle.length() > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sis.scisTitle like ? ");
			param.add("%" + scisTitle + "%");
		}
		if(scisState != null) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sis.scisState = ? ");
			param.add(scisState);
		}
		if("1".equals(type)) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql
					.append(" sis.scisId in ( select sir.scirScisId from ScIssueReply sir where sir.scirSuinId = ? ) and sis.scisSuinId != ? ");
			param.add(suinId);
			param.add(suinId);
		}
		String countHql = "select count(*) " + fromHql.toString();
		String hql = fromHql.toString() + orderHql.toString();
		return getPageHibernateTemplate().find(pagination, hql, countHql, param.toArray());
	}

	@Override
	public List getScFaqLibraryList(Pagination pagination, Long scftId, String scflQuestion) {
		StringBuffer fromHql = new StringBuffer("from ScFaqLibrary sfl ");
		StringBuffer orderHql = new StringBuffer("order by sfl.scflIstop DESC,sfl.scflTopTime DESC ");
		List<Object> param = new ArrayList<Object>();
		boolean isWhere = true;
		// 添加查询条件
		if(scftId != null && scftId > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sfl.scflScftId = ? ");
			param.add(scftId);
		}
		if(CommonUtil.isNotNull(scflQuestion) && scflQuestion.length() > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sfl.scflQuestion like ? ");
			param.add("%" + scflQuestion + "%");
		}
		String countHql = "select count(*) " + fromHql.toString();
		String hql = fromHql.toString() + orderHql.toString();
		return getPageHibernateTemplate().find(pagination, hql, countHql, param.toArray());
	}

	@Override
	public List getScConsultationType(String sctyName, Long sctyIndex, String type) {
		StringBuffer fromHql = new StringBuffer("from ScConsultationType sct ");
		List<Object> param = new ArrayList<Object>();
		String orderHql = "order by sct.sctyIndex ASC";
		boolean isWhere = true;
		if(sctyName != null && sctyName.trim().length() > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sct.sctyName = ? ");
			param.add(sctyName);
		}
		if("1".equals(type)) {
			orderHql = " order by sct.sctyIndex  DESC ";
		}
		if("2".equals(type)) {
			orderHql = " order by sct.sctyIndex DESC ";
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sct.sctyIndex < ? ");
			param.add(sctyIndex);
		}
		if("3".equals(type)) {
			orderHql = " order by sct.sctyIndex ASC ";
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sct.sctyIndex > ? ");
			param.add(sctyIndex);
		}
		return this.getHibernateTemplate().find(fromHql.toString() + orderHql, param.toArray());
	}

	@Override
	public List getScConsultationList(Pagination pagination, Long sctyId, String sccoTitle, String suinLoginName, Long sccoState,
			String sort, String dir, String type, Long suinId) {
		String selectHql = "select sc.sccoId ,sc.sccoTitle ,sc.scConsultationType.sctyName ,sc.sysUserinfo.suinName ,sc.sysUserinfo.suinLoginName ,sc.sccoCreationTime  ,sc.sccoState  ";
		StringBuffer fromHql = new StringBuffer("from ScConsultation sc ");
		StringBuffer orderHql = new StringBuffer("order by ");
		List<Object> param = new ArrayList<Object>();
		boolean isWhere = true;
		// 添加查询条件
		if("1".equals(type))
			selectHql = "";
		if(sctyId != null && sctyId > -1) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sc.sctyId = ? ");
			param.add(sctyId);
		}
		if(CommonUtil.isNotNull(sccoTitle) && sccoTitle.length() > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sc.sccoTitle like ? ");
			param.add("%" + sccoTitle + "%");
		}
		if(CommonUtil.isNotNull(suinLoginName) && suinLoginName.length() > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sc.sysUserinfo.suinLoginName like ? ");
			param.add("%" + suinLoginName + "%");
		}
		if(sccoState != null && sccoState > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sc.sccoState = ? ");
			param.add(sccoState);
		}
		if(suinId != null && suinId > 0) {
			isWhere = createWhereAnd(fromHql, isWhere);
			fromHql.append(" sc.suinId = ? ");
			param.add(suinId);
		}
		// 添加排序
		if(CommonUtil.isNotNull(sort) && CommonUtil.isNotNull(dir)) {
			if("sctyName".equals(sort)) {
				orderHql.append("sc.scConsultationType.sctyName");
			}else if("suinName".equals(sort) || "suinLoginName".equals(sort)) {
				orderHql.append("sc.sysUserinfo.").append(sort);
			}else {
				orderHql.append("sc.").append(sort);
			}
			orderHql.append(" ").append(dir).append(" , sc.sccoCreationTime DESC ");
		}
		String countHql = "select count(*) " + fromHql.toString();
		String hql = selectHql + fromHql.toString() + orderHql.toString();
		return getPageHibernateTemplate().find(pagination, hql, countHql, param.toArray());
	}

	@Override
	public ScIssue getScIssue(Long scirId) {
		String hql = "from ScIssue sc where sc.scisCloseScirId = " + scirId;
		List list = getHibernateTemplate().find(hql);
		if(list.size() > 0)
			return (ScIssue)list.get(0);
		else
			return null;
	}
}
