package cn.com.adtech.riss2.bso.text;

import java.util.List;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.domain.text.ResLibraryClassify;

/**
 * 资源库分类设置服务
 *   
 * textLibClassfiyServiceImp  
 *   
 * 2017-10-17 下午05:43:39  
 *   
 * @athor lichangkun
 * @version 1.0.0  
 *
 */
public class textLibClassfiyServiceImp extends CommonServiceImp implements ITextLibClassfiyService {

    /**
     * 根据ID获取分类列表
     */
    @Override
    public List<ResLibraryClassify> getLibClassfiyList(Long rtliId) {
        String hql = "from ResLibraryClassify rlcl where rlcl.rlclRtliId=? order by rlcl.rlclIndex ASC";
        List<ResLibraryClassify> list = this.getHibernateTemplate().find(hql,rtliId); 
        return list;
    }
    @Override
    public List<Object[]> getLibClassfiyLists(Long rtliId) {
        String hql = "select rlclId,rlclName from ResLibraryClassify rlcl where rlcl.rlclRtliId=? order by rlcl.rlclIndex ASC";
        List<Object[]> list = this.getHibernateTemplate().find(hql,rtliId); 
        return list;
    }
    /**
     * 查询资源库分类下拉选项
     */
    @Override
    public String getLibClassfiyOptions(Long rtliId, Long selectId) {
        String hql = "select rlcl.rlclId, rlcl.rlclParentId, rlcl.rlclName from ResLibraryClassify rlcl where rlcl.rlclRtliId="+rtliId+" order by rlcl.rlclIndex ASC";
        List<Object[]> list = find(hql);
        if(list == null || list.size() < 1){
            return "";
        }
        buffer = new StringBuffer();
        String options = "<option value='0'>无</option>" + loopOptions(list, selectId, 0, null);
        return options;
    }

    private StringBuffer buffer = new StringBuffer();
    private String loopOptions(List<Object[]> list, Long selectid, int grade, Long parentid){
        String tree = "";
        for(int i=0; i<grade; i++){
            tree += "─";
        }
        int now_grade = grade + 1;
        Object[] group = null;
        for(int i=0,s=list.size(); i<s; i++){
            group = list.get(i);
            if(parentid == null || parentid < 1){
                if(group[1] == null || (Long)group[1] < 1){
                    buffer.append("<option value='"+group[0]+"'");
                    if(selectid != null && selectid.equals(group[0])){
                        buffer.append(" selected=true ");
                    }
                    buffer.append(">├").append(tree).append(group[2]).append("</option>");
                    loopOptions(list, selectid, now_grade, (Long)group[0]);
                }
            }
            else if(parentid.equals(group[1])){
                buffer.append("<option value='"+group[0]+"'");
                if(selectid != null && selectid.equals(group[0])){
                    buffer.append(" selected=true ");
                }
                buffer.append(">├").append(tree).append(group[2]).append("</option>");
                loopOptions(list, selectid, now_grade, (Long)group[0]);
            }
        }
        
        return buffer.toString();
    }
    
    /**
     * 保存资源类型分类
     */
    @Override
    public boolean saveLibClassfiy(Long rtliId,Long rlclId, Long rlclParentId, String rlclName, Integer rlclIndex) {
        ResLibraryClassify subject = null;
        if(rlclId < 1){
            rlclId = null;
        }
        subject = new ResLibraryClassify();
        subject.setRlclId(rlclId);
        subject.setRlclRtliId(rtliId);
        subject.setRlclParentId(rlclParentId);
        subject.setRlclName(rlclName);
        subject.setRlclIndex(rlclIndex.longValue());
        try {
            save(subject);
            return true;
        } catch (Exception e) {
            logger.error("资源库分类保存异常：" ,e);
            return false;
        }
    }
    
    /**
     * 删除资源库分类
     */
    @Override
    public boolean deleteLibClassfiy(Long rlclId) {
        if(rlclId == null){
            return false;
        }
        String hql = "delete from ResLibraryClassify rlcl where rlcl.rlclId = ? or rlcl.rlclParentId = ?";
        try {
            getHibernateTemplate().bulkUpdate(hql, new Object[]{rlclId,rlclId});
            return true;
        } catch (Exception e) {
            return false; 
        }
    }
}
