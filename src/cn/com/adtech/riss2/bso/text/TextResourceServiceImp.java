package cn.com.adtech.riss2.bso.text;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.text.ResLibraryClassify;

public class TextResourceServiceImp extends CommonServiceImp implements ITextResourceService {
    /**
     * 获取资源列表
     */
    @Override
    public List<Object[]> getResourceList(Pagination pagination, Long rtliId, Long rlclId, String keyStr, String key, String val, String sort,
            String dir) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("select rtre.rtreId,");
        buffer.append("rtre.rtreTitle,");
        if (CommonUtil.isNotNull(keyStr)) {
            buffer.append(keyStr);
        } else {
            buffer.append("rtre.rtreRtliId,");
            buffer.append("rtre.resTextLibrary.rtliName,");
        }
        buffer.append("rlcl.rlclName,");
        buffer.append("rtre.rtreUsed,");
        buffer.append("rtre.rtreRecommend,");
        buffer.append("rtre.rtreDate");
        buffer.append(" from ResTextResource rtre left join rtre.resLibraryClassify rlcl");
        buffer.append(" where rtre.rtreId>0");
        if (rtliId != null && rtliId > 0L) {
            buffer.append(" and rtre.rtreRtliId=").append(rtliId);
            if (rlclId != null && rlclId > 0L) {
                buffer.append(" and rtre.rtreRlclId=").append(rlclId);
            }
        }

        if (CommonUtil.isNotNull(key) && CommonUtil.isNotNull(val)) {
            buffer.append(" and rtre.").append(key).append(" like ?");
        }

        buffer.append(" order by ").append(sort).append(" ").append(dir);

        String hql = buffer.toString();
        buffer = null;

        List<Object[]> list = null;
        if (CommonUtil.isNotNull(key) && CommonUtil.isNotNull(val)) {
            list = getPageHibernateTemplate().find(pagination, hql, "%" + val + "%");
        } else {
            list = getPageHibernateTemplate().find(pagination, hql);
        }
        return list;
    }

    /**
     * 设置资源推荐状态
     */
    @Override
    public boolean updateRcmdStatus(String idlist) {
        if (idlist == null || idlist.length() < 1) {
            return true;
        }
        String hql = "update ResTextResource s set s.rtreRecommend = (case when s.rtreRecommend=1 then 0 else 1 end) where s.rtreId in (" + idlist
                + ")";
        try {
            getHibernateTemplate().bulkUpdate(hql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置资源启用状态
     */
    @Override
    public boolean updateUseStatus(String idlist) {
        if (idlist == null || idlist.length() < 1) {
            return true;
        }
        String hql = "update ResTextResource s set s.rtreUsed = (case when s.rtreUsed=1 then 0 else 1 end) where s.rtreId in (" + idlist + ")";
        try {
            getHibernateTemplate().bulkUpdate(hql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getJsonByType(Long store) {
        List<ResLibraryClassify> list = null;
        String json = "";
        String hql = "from ResLibraryClassify rlcl where rlclRtliId=" + store + " order by rlcl.rlclIndex ASC";

        list = this.getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            json = loopOptions(list, 0, null);
        }

        return json;
    }

    private String loopOptions(List<ResLibraryClassify> list, int grade, Long parentid) {
        StringBuffer buffer = new StringBuffer();
        String tree = "";
        for (int i = 0; i < grade; i++) {
            tree += "─";
        }
        int now_grade = grade + 1;
        ResLibraryClassify dynamic = null;
        for (int i = 0, s = list.size(); i < s; i++) {
            dynamic = list.get(i);
            if (parentid == null || parentid < 1) {
                if (dynamic.getRlclParentId() == null || dynamic.getRlclParentId() < 1) {
                    buffer.append("['").append(tree);
                    buffer.append(dynamic.getRlclName());
                    buffer.append("','");
                    buffer.append(dynamic.getRlclId());
                    buffer.append("'],");

                    buffer.append(loopOptions(list, now_grade, dynamic.getRlclId()));
                }
            } else if (parentid.equals(dynamic.getRlclParentId())) {
                buffer.append("['|").append(tree);
                buffer.append(dynamic.getRlclName());
                buffer.append("','");
                buffer.append(dynamic.getRlclId());
                buffer.append("'],");

                buffer.append(loopOptions(list, now_grade, dynamic.getRlclId()));
            }
        }

        return buffer.toString();
    }

	@Override
	public String getLibLanguage(Long rtliId) {
		if(rtliId == null){
			return "";
		}
		String hql = "select rtli.rtliLanguage from ResTextLibrary rtli where rtli.rtliId=" + rtliId;
		String lan = getSingleData(hql);
		return lan;
	}
}
