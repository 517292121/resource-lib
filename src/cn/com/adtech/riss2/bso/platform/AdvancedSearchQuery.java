package cn.com.adtech.riss2.bso.platform;

import java.util.*;
import cn.com.adtech.riss2.core.CommonUtil;

/** Shared, bound metadata conditions for advanced results and occurrence statistics. */
public final class AdvancedSearchQuery {
    private AdvancedSearchQuery() { }
    private static final Set<String> FIELDS = new HashSet<String>(Arrays.asList(
            "rsdaTitle", "rsdaCreator", "rsdaPublisher", "rsdaSubject", "rsdaKeyword", "rsdaDescription", "rsdaIdentifier"));

    public static String field(Object value) {
        String field = CommonUtil.nullToBlank(value);
        if (field.isEmpty()) return "rsdaTitle";
        if (!FIELDS.contains(field)) throw new IllegalArgumentException("Invalid advanced search field");
        return field;
    }

    public static String conditions(Map<String, Object> criteria, List<Object> bindings) {
        StringBuilder terms = new StringBuilder();
        for (int i = 1; i <= 4; i++) {
            String key = CommonUtil.nullToBlank(criteria.get("key" + i));
            if (key.trim().isEmpty()) continue;
            String connector = CommonUtil.nullToBlank(criteria.get("con" + i));
            if (connector.isEmpty()) connector = "AND";
            if (!"AND".equals(connector) && !"OR".equals(connector)) throw new IllegalArgumentException("Invalid connector");
            if (terms.length() > 0) terms.append(' ').append(connector).append(' ');
            String match = CommonUtil.nullToBlank(criteria.get("like" + i));
            if (match.isEmpty()) match = "LIKE";
            if (!"LIKE".equals(match) && !"=".equals(match)) throw new IllegalArgumentException("Invalid match operator");
            terms.append("rsda.").append(field(criteria.get("col" + i))).append(' ').append(match).append(" ?");
            bindings.add("LIKE".equals(match) ? "%" + key + "%" : key);
        }
        return terms.length() == 0 ? "" : " and (" + terms + ")";
    }

    public static String numericIds(String ids) {
        if (ids == null || ids.trim().isEmpty()) return "";
        StringBuilder normalized = new StringBuilder();
        for (String token : ids.split(",")) {
            if (!token.trim().matches("[0-9]+")) throw new IllegalArgumentException("Invalid resource filter");
            long id = Long.parseLong(token.trim());
            if (id < 1L) throw new IllegalArgumentException("Invalid resource filter");
            if (normalized.length() > 0) normalized.append(',');
            normalized.append(id);
        }
        return normalized.toString();
    }
}
