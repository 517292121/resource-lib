package cn.com.adtech.riss2.annotation;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StreamUtils;

/** Parameterized JDBC operations; each mutation is an atomic, version-checked statement. */
public class TextAnnotationRepository {
    public static final String PENDING="PENDING", APPROVED="APPROVED", REJECTED="REJECTED";
    private JdbcTemplate jdbc;
    public void setDataSource(DataSource source) { jdbc = new JdbcTemplate(source); }

    public void initializeSchema() throws Exception {
        try (Connection connection = jdbc.getDataSource().getConnection()) {
            String product = connection.getMetaData().getDatabaseProductName();
            String sql;
            try (java.io.InputStream input = new ClassPathResource("db/text-annotations-mysql.sql").getInputStream()) {
                sql = StreamUtils.copyToString(input, StandardCharsets.UTF_8).replaceAll("(?m)^--.*$", "");
            }
            if (product.equalsIgnoreCase("H2")) sql = sql.replaceAll("(?s) ENGINE=InnoDB.*", "");
            else if (!product.toLowerCase(java.util.Locale.ROOT).contains("mysql") && !product.toLowerCase(java.util.Locale.ROOT).contains("mariadb")) {
                throw new IllegalStateException("文献批注建表脚本需要 MySQL 数据库");
            }
            try (java.sql.Statement statement=connection.createStatement()) { statement.execute(sql); }
            upgradeAuditSchema(connection);
        }
    }

    /** Add missing columns without replacing the table or reclassifying existing reviewed records. */
    private void upgradeAuditSchema(Connection connection) throws SQLException {
        String[][] columns={{"AUDIT_STATUS","VARCHAR(16) NOT NULL DEFAULT 'PENDING'"},
                {"AUDIT_REMARK","VARCHAR(1000) NULL"},{"AUDIT_BY","BIGINT NULL"},{"AUDIT_AT","DATETIME(3) NULL"}};
        for(String[] column:columns) if(!hasColumn(connection,column[0])) {
            try(java.sql.Statement statement=connection.createStatement()) {
                statement.execute("ALTER TABLE RES_TEXT_ANNOTATION ADD COLUMN "+column[0]+" "+column[1]);
            } catch(SQLException race) { if(!hasColumn(connection,column[0])) throw race; }
        }
        if(!hasAuditIndex(connection)) {
            try(java.sql.Statement statement=connection.createStatement()) {
                statement.execute("CREATE INDEX IDX_TEXT_ANNOTATION_AUDIT ON RES_TEXT_ANNOTATION (AUDIT_STATUS, CREATED_AT)");
            } catch(SQLException race) { if(!hasAuditIndex(connection)) throw race; }
        }
    }
    private boolean hasColumn(Connection connection,String name) throws SQLException {
        try(ResultSet columns=connection.getMetaData().getColumns(connection.getCatalog(),null,"RES_TEXT_ANNOTATION",name)) {
            while(columns.next()) if("RES_TEXT_ANNOTATION".equalsIgnoreCase(columns.getString("TABLE_NAME")) && name.equalsIgnoreCase(columns.getString("COLUMN_NAME"))) return true;
        }
        return false;
    }
    private boolean hasAuditIndex(Connection connection) throws SQLException {
        try(ResultSet indexes=connection.getMetaData().getIndexInfo(connection.getCatalog(),null,"RES_TEXT_ANNOTATION",false,false)) {
            while(indexes.next()) if("IDX_TEXT_ANNOTATION_AUDIT".equalsIgnoreCase(indexes.getString("INDEX_NAME")))return true;
        }
        return false;
    }

    private static final String SELECT = "SELECT a.*, u.SUIN_LOGIN_NAME AS AUTHOR, r.RSDA_TITLE AS RESOURCE_TITLE "
            + "FROM RES_TEXT_ANNOTATION a LEFT JOIN SYS_USER_INFO u ON u.SUIN_ID=a.USER_ID "
            + "LEFT JOIN RES_SOURCE_DATA r ON r.RSDA_ID=a.RESOURCE_ID ";

    private static final RowMapper<Map<String,Object>> ROW = new RowMapper<Map<String,Object>>() {
        public Map<String,Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String,Object> row = new LinkedHashMap<String,Object>();
            row.put("id", rs.getString("ANNOTATION_ID"));
            row.put("resourceId", String.valueOf(rs.getLong("RESOURCE_ID")));
            row.put("userId", String.valueOf(rs.getLong("USER_ID")));
            row.put("chapter", rs.getString("CHAPTER_PATH"));
            row.put("textHash", rs.getString("TEXT_HASH"));
            row.put("start", rs.getInt("START_OFFSET")); row.put("end", rs.getInt("END_OFFSET"));
            row.put("scope", rs.getString("ANNOTATION_SCOPE")); row.put("quote", rs.getString("QUOTED_TEXT"));
            row.put("content", rs.getString("CONTENT")); row.put("version", rs.getInt("VERSION"));
            row.put("author", rs.getString("AUTHOR") == null ? "已注销会员" : rs.getString("AUTHOR"));
            row.put("resourceTitle", rs.getString("RESOURCE_TITLE") == null ? "资源已移除" : rs.getString("RESOURCE_TITLE"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            row.put("createdAt", format.format(rs.getTimestamp("CREATED_AT")));
            row.put("updatedAt", format.format(rs.getTimestamp("UPDATED_AT")));
            String auditStatus=rs.getString("AUDIT_STATUS");
            row.put("auditStatus",auditStatus);
            row.put("auditStatusLabel",APPROVED.equals(auditStatus)?"已通过":REJECTED.equals(auditStatus)?"未通过":"待审核");
            row.put("auditRemark",rs.getString("AUDIT_REMARK")==null?"":rs.getString("AUDIT_REMARK"));
            Timestamp audited=rs.getTimestamp("AUDIT_AT");
            row.put("auditedAt",audited==null?"":format.format(audited));
            return row;
        }
    };

    public List<Map<String,Object>> chapter(long resource, String chapter) {
        return jdbc.query(SELECT + "WHERE a.RESOURCE_ID=? AND a.CHAPTER_PATH=? AND a.AUDIT_STATUS='APPROVED' ORDER BY a.CREATED_AT,a.ANNOTATION_ID",
                ROW, resource, chapter);
    }

    public List<Map<String,Object>> resource(long resource) {
        return jdbc.query(SELECT + "WHERE a.RESOURCE_ID=? AND a.AUDIT_STATUS='APPROVED' ORDER BY a.CREATED_AT,a.ANNOTATION_ID", ROW, resource);
    }

    /** Homepage totals expose only reviewed annotations attached to publicly listed documents. */
    public long countPublished() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM RES_TEXT_ANNOTATION a "
                + "JOIN RES_SOURCE_DATA r ON r.RSDA_ID=a.RESOURCE_ID "
                + "WHERE a.AUDIT_STATUS=? AND r.RSDA_STATUS=3 AND r.RSDA_ISUSE=1 AND r.RSDA_RMST_STATUS=1",
                Long.class, APPROVED);
    }

    public Map<String,Object> find(String id) {
        List<Map<String,Object>> rows = jdbc.query(SELECT + "WHERE a.ANNOTATION_ID=?", ROW, id);
        return rows.isEmpty() ? null : rows.get(0);
    }

    public String create(long resource, long user, String chapter, String hash, int start, int end,
                         String scope, String quote, String content) {
        String id = UUID.randomUUID().toString();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        jdbc.update("INSERT INTO RES_TEXT_ANNOTATION (ANNOTATION_ID,RESOURCE_ID,USER_ID,CHAPTER_PATH,TEXT_HASH,"
                + "START_OFFSET,END_OFFSET,ANNOTATION_SCOPE,QUOTED_TEXT,CONTENT,CREATED_AT,UPDATED_AT,VERSION,AUDIT_STATUS) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,0,'PENDING')", id, resource, user, chapter, hash, start, end, scope, quote, content, now, now);
        return id;
    }

    public boolean edit(String id, long owner, boolean admin, int version, String content) {
        return jdbc.update("UPDATE RES_TEXT_ANNOTATION SET CONTENT=?,UPDATED_AT=?,VERSION=VERSION+1,"
                + "AUDIT_STATUS='PENDING',AUDIT_REMARK=NULL,AUDIT_BY=NULL,AUDIT_AT=NULL "
                + "WHERE ANNOTATION_ID=? AND VERSION=?" + (admin ? "" : " AND USER_ID=?"),
                admin ? new Object[]{content,new Timestamp(System.currentTimeMillis()),id,version}
                      : new Object[]{content,new Timestamp(System.currentTimeMillis()),id,version,owner}) == 1;
    }

    public boolean delete(String id, long owner, boolean admin, int version) {
        return jdbc.update("DELETE FROM RES_TEXT_ANNOTATION WHERE ANNOTATION_ID=? AND VERSION=?" + (admin ? "" : " AND USER_ID=?"),
                admin ? new Object[]{id,version} : new Object[]{id,version,owner}) == 1;
    }

    public Map<String,Object> records(Long owner, String search, int page) {
        return records(owner,search,page,"");
    }

    public boolean review(String id,long administrator,int version,String auditStatus,String remark) {
        if(!APPROVED.equals(auditStatus)&&!REJECTED.equals(auditStatus))throw new IllegalArgumentException("Invalid review status");
        return jdbc.update("UPDATE RES_TEXT_ANNOTATION SET AUDIT_STATUS=?,AUDIT_REMARK=?,AUDIT_BY=?,AUDIT_AT=?,VERSION=VERSION+1 "
                + "WHERE ANNOTATION_ID=? AND VERSION=?",auditStatus,remark,administrator,new Timestamp(System.currentTimeMillis()),id,version)==1;
    }

    public Map<String,Object> records(Long owner, String search, int page, String auditStatus) {
        String where = owner == null ? " WHERE 1=1" : " WHERE a.USER_ID=?";
        List<Object> args = new ArrayList<Object>();
        if (owner != null) args.add(owner);
        if(!auditStatus.isEmpty()) { where+=" AND a.AUDIT_STATUS=?";args.add(auditStatus); }
        if (!search.isEmpty()) {
            where += " AND (a.CONTENT LIKE ? OR r.RSDA_TITLE LIKE ? OR a.CHAPTER_PATH LIKE ? OR u.SUIN_LOGIN_NAME LIKE ?)";
            for (int i=0;i<4;i++) args.add("%" + search + "%");
        }
        String from = SELECT.substring(SELECT.indexOf("FROM "));
        long count = jdbc.queryForObject("SELECT COUNT(*) " + from + where, args.toArray(), Long.class);
        int lastPage = (int)Math.max(1, (count + 19) / 20);
        page = Math.min(Math.max(1,page), lastPage);
        List<Object> paged = new ArrayList<Object>(args); paged.add(20); paged.add((page-1)*20);
        List<Map<String,Object>> rows = jdbc.query(SELECT + where + " ORDER BY a.CREATED_AT DESC,a.ANNOTATION_ID LIMIT ? OFFSET ?", ROW, paged.toArray());
        Map<String,Object> result = new LinkedHashMap<String,Object>();
        result.put("records", rows); result.put("total", count); result.put("page", page); result.put("pages", lastPage);
        return result;
    }
}
