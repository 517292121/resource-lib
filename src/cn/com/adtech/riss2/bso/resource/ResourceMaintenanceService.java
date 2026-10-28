package cn.com.adtech.riss2.bso.resource;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.core.ResourceResearchLinks;
import cn.com.adtech.riss2.domain.resource.*;
import com.jbetter.framework.session.Pagination;
import java.util.*;

/** Bounded, parameterized selectors used only by the authenticated maintenance form. */
public class ResourceMaintenanceService extends CommonServiceImp {
    private static final String MISSION_ACCESS=" (m.rminUser=? or m.rminAuthor=? or exists (from ResMetaPerm p where p.rmstId=m.rminRmstId and p.smanId=? and p.rmpeType in (1,2)))";
    private static final String RESOURCE_ACCESS=" (r.rsdaSmanId=? or exists (from ResMetaPerm p where p.rmstId=r.rsdaRmstId and p.smanId=? and p.rmpeType in (1,2)))";

    public List<ResLiterType> getCreationTypes() {
        return getHibernateTemplate().find("from ResLiterType where rltyName in ('图书','专著','论文') order by rltyId");
    }
    public List<Object[]> getMissions(long admin, long type, String key, Pagination page) {
        String from=" from ResMissionInfo m where m.rminRltyId=? and m.resMetaStore.rmstStatus=1 and"+MISSION_ACCESS;
        List<Object> args=new ArrayList<Object>(Arrays.<Object>asList(type,admin,admin,admin));
        if (key!=null && !key.trim().isEmpty()) { from+=" and (m.rminMissname like ? escape '!' or m.rminRemark like ? escape '!')"; args.add(like(key)); args.add(like(key)); }
        return getPageHibernateTemplate().find(page,"select m.rminId,m.rminMissname,m.rminRemark,m.resMetaStore.rmstName"+from+" order by m.rminBuilddate desc,m.rminId desc","select count(*)"+from,args.toArray());
    }
    public ResMissionInfo getCreationMission(long admin, long id, long type) {
        List<ResMissionInfo> list=getHibernateTemplate().find("from ResMissionInfo m where m.rminId=? and m.resMetaStore.rmstStatus=1 and"+MISSION_ACCESS,id,admin,admin,admin);
        if (list.isEmpty()) throw new IllegalArgumentException("作业不存在、所属资源库停用或没有加工权限，请重新选择");
        ResMissionInfo mission=list.get(0);
        ResLiterType resourceType=(ResLiterType)getHibernateTemplate().get(ResLiterType.class,mission.getRminRltyId());
        if (resourceType==null || ResourceResearchLinks.kind(resourceType.getRltyName()).isEmpty() || !mission.getRminRltyId().equals(type))
            throw new IllegalArgumentException("资源类型与所属作业不一致，请重新选择");
        return mission;
    }
    public List<Object[]> getTargets(long admin, long self, String key, Pagination page) {
        String from=" from ResSourceData r where r.rsdaId<>? and r.rsdaIsuse=1 and r.rsdaRmstStatus=1 and"+RESOURCE_ACCESS;
        List<Object> args=new ArrayList<Object>(Arrays.<Object>asList(self,admin,admin));
        if (key!=null && !key.trim().isEmpty()) { from+=" and r.rsdaTitle like ? escape '!'"; args.add(like(key)); }
        return getPageHibernateTemplate().find(page,"select r.rsdaId,r.rsdaTitle,r.resMetaStore.rmstName"+from+" order by r.rsdaBuilddate desc,r.rsdaId desc","select count(*)"+from,args.toArray());
    }
    public List<Object[]> getSelectedTargets(long research) {
        return getHibernateTemplate().find("select r.rsdaId,r.rsdaTitle from ResSourceData r where exists (from ResResearchLink l where l.researchId=? and l.resourceId=r.rsdaId) order by r.rsdaId",research);
    }
    public void getValidatedLinks(long admin, Long self, Long type, javax.servlet.http.HttpServletRequest request) {
        ResourceResearchLinks.validate(getHibernateTemplate(),self,type,request);
        for (Long id:ResourceResearchLinks.ids(request)) {
            List<Long> count=getHibernateTemplate().find("select count(*) from ResSourceData r where r.rsdaId=? and"+RESOURCE_ACCESS,id,admin,admin);
            if (count.isEmpty() || count.get(0)==0) throw new IllegalArgumentException("没有关联资源的维护权限，请重新选择");
        }
    }
    private static String like(String key) {
        String value=key.trim();
        if(value.length()>200) throw new IllegalArgumentException("检索词不能超过200字");
        return "%"+value.replace("!","!!").replace("%","!%").replace("_","!_")+"%";
    }
}
