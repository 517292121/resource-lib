package cn.com.adtech.riss2.core;

import cn.com.adtech.riss2.domain.resource.ResLiterType;
import cn.com.adtech.riss2.domain.resource.ResResearchLink;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.springframework.orm.hibernate3.HibernateTemplate;

/** Explicit links are separate from DC Relation text; all writes use the resource's Hibernate transaction. */
public class ResourceResearchLinks {
    private DataSource dataSource;
    public void setDataSource(DataSource value) { dataSource=value; }
    public void initializeSchema() throws Exception {
        try (Connection connection=dataSource.getConnection(); java.sql.Statement statement=connection.createStatement()) {
            String product=connection.getMetaData().getDatabaseProductName().toLowerCase(java.util.Locale.ROOT);
            if (!product.contains("mysql") && !product.contains("mariadb") && !product.equals("h2"))
                throw new IllegalStateException("资源关联表需要 MySQL 数据库");
            statement.execute("CREATE TABLE IF NOT EXISTS RES_RESEARCH_LINK (RESEARCH_ID BIGINT NOT NULL, RESOURCE_ID BIGINT NOT NULL, "
                    + "PRIMARY KEY (RESEARCH_ID,RESOURCE_ID), INDEX IDX_RESEARCH_RESOURCE (RESOURCE_ID,RESEARCH_ID))"
                    + (product.equals("h2") ? "" : " ENGINE=InnoDB"));
        }
    }

    public static boolean submitted(HttpServletRequest request) { return "1".equals(request.getParameter("researchLinksPresent")); }
    public static String kind(String name) {
        if (name==null) return "";
        if (name.contains("论文")) return "paper";
        if (name.contains("专著") || name.contains("著作")) return "monograph";
        if ("图书".equals(name)) return "book";
        return "";
    }
    public static List<Long> ids(HttpServletRequest request) {
        Set<Long> ids=new LinkedHashSet<Long>();
        String values=request.getParameter("relatedResourceIds");
        if (values!=null && !values.trim().isEmpty()) for (String value:values.split(",",-1)) {
            if (!value.trim().matches("[1-9][0-9]{0,18}")) throw new IllegalArgumentException("关联资源无效，请从下拉列表中选择");
            try { ids.add(Long.valueOf(value.trim())); }
            catch (NumberFormatException e) { throw new IllegalArgumentException("关联资源编号无效"); }
        }
        if (ids.size()>30) throw new IllegalArgumentException("最多关联30条资源");
        return new ArrayList<Long>(ids);
    }
    public static void validate(HibernateTemplate hibernate, Long self, Long type, HttpServletRequest request) {
        if (!submitted(request)) return;
        List<Long> ids=ids(request);
        ResLiterType resourceType=(ResLiterType)hibernate.get(ResLiterType.class,type);
        String kind=kind(resourceType==null?null:resourceType.getRltyName());
        if (!ids.isEmpty() && !"paper".equals(kind) && !"monograph".equals(kind))
            throw new IllegalArgumentException("仅专著、论文资源可以设置研究关联");
        for (Long id:ids) {
            if (id.equals(self)) throw new IllegalArgumentException("资源不能关联自身");
            ResSourceData target=(ResSourceData)hibernate.get(ResSourceData.class,id);
            if (target==null || !Long.valueOf(1).equals(target.getRsdaIsuse()) || !Long.valueOf(1).equals(target.getRsdaRmstStatus()))
                throw new IllegalArgumentException("关联资源已删除或停用，请重新选择");
        }
    }
    public static void replace(HibernateTemplate hibernate, ResSourceData resource, HttpServletRequest request) {
        if (!submitted(request)) return; // Older input/translation clients must not erase existing links.
        validate(hibernate,resource.getRsdaId(),resource.getRsdaRltyId(),request);
        hibernate.bulkUpdate("delete from ResResearchLink where researchId=?",resource.getRsdaId());
        for (Long target:ids(request)) hibernate.save(new ResResearchLink(resource.getRsdaId(),target));
    }
}
