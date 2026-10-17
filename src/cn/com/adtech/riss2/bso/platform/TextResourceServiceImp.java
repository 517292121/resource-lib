package cn.com.adtech.riss2.bso.platform;

import java.util.List;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.jdbcms.SqlBasicUnit;

import com.jbetter.framework.session.Pagination;

public class TextResourceServiceImp extends CommonServiceImp implements ITextResourceService{

	@Override
	public List<Object[]> getResourceList(Pagination p, Long rtliId, Long rlclId, String sort,int isCmd) {
		StringBuffer buffer = new StringBuffer();
        buffer.append("select rtre.rtreId,");
        buffer.append("rtre.rtreTitle,");
        buffer.append("rtre.rtreCover,");
        buffer.append("rtre.rtreAccess,");
        buffer.append("rtre.rtreDate,");
        buffer.append("rtre.rtreCol1");
        buffer.append(" from ResTextResource rtre");
        buffer.append(" where rtre.rtreUsed=1");
        if (rtliId != null && rtliId > 0L) {
            buffer.append(" and rtre.rtreRtliId=").append(rtliId);
            if (rlclId != null && rlclId > 0L) {
            	String classIds = getClassChilds(rlclId);
                buffer.append(" and rtre.rtreRlclId in (").append(classIds).append(")");
            }
        }
        if(isCmd>0){
        	 buffer.append(" and rtre.rtreRecommend=1");
        }
        buffer.append(" order by rtre.").append(sort).append(" DESC");

        String hql = buffer.toString();
        buffer = null;
        List<Object[]> list = getPageHibernateTemplate().find(p, hql);
        return list;
	}

	private String getClassChilds(Long rlclId){
		if(rlclId == null || rlclId < 1){
			return "0";
		}
		String sql = SqlBasicUnit.hierarchyIdsSql("RES_LIBRARY_CLASSIFY", "RLCL_ID", "RLCL_PARENT_ID", rlclId);
		List<Object> idlist = getSingleColoumList(sql, null);
		if(idlist == null || idlist.size() < 1){
			return "0";
		}
		StringBuffer buffer = new StringBuffer();
		String ids = "";
		for(int i=0,s=idlist.size(); i<s; i++){
			buffer.append(idlist.get(i).toString());
			if(i<s-1){
				buffer.append(",");
			}
		}
		ids = buffer.toString();
		buffer = null;
		idlist = null;
		return ids;
	} 
}
