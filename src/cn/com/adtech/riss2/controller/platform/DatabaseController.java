package cn.com.adtech.riss2.controller.platform;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import cn.com.adtech.riss2.bso.platform.IIndexDataService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.unionlib.lucene.dao.IAllDataIndexService;
import net.sf.json.JSONObject;

/** Public aggregate data only; console endpoints and their access controls remain private. */
@Controller
public class DatabaseController {
    @Resource(name="indexDataService") private IIndexDataService indexDataService;
    @Resource(name="alldataIndexService") private IAllDataIndexService indexService;

    @RequestMapping(value="/database/statistics", method=org.springframework.web.bind.annotation.RequestMethod.GET)
    public void statistics(@RequestParam(value="year", required=false) Integer requestedYear,
                           HttpServletResponse response) throws IOException {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int year = requestedYear == null ? currentYear : requestedYear;
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        if (year < 1900 || year > currentYear) {
            response.setStatus(400); response.getWriter().write("{\"success\":false}"); return;
        }
        try {
            Map<String, Object> data = indexDataService.getDatabaseStatistics(year);
            // rsdaPage is a transient display field. Use the existing real page index instead.
            try { data.put("pageTotal", Long.valueOf(indexService.getResCounts().split(",")[3])); }
            catch (Exception ex) { data.put("pageTotal", null); }
            // Reuse the public news feed, falling back to the latest published resources.
            try {
                List<Map<String, Object>> latest = new ArrayList<Map<String, Object>>();
                List<Object[]> rows = indexDataService.getInfoList(5);
                String type = "info";
                if (rows == null || rows.isEmpty()) {
                    // Interface language controls UI labels, not which published resources exist.
                    rows = indexDataService.getIndexResource("rsdaBuilddate", 5, false);
                    type = "resource";
                }
                if (rows != null) for (Object[] row : rows) {
                    Map<String, Object> item = new LinkedHashMap<String, Object>();
                    item.put("id", CommonUtil.idToString(String.valueOf(row[0])));
                    item.put("title", row[1]); item.put("type", type); latest.add(item);
                }
                data.put("latest", latest);
            } catch (Exception ex) {
                org.apache.log4j.Logger.getLogger(DatabaseController.class).warn("Unable to load dashboard news", ex);
                data.put("latest", null);
            }
            data.put("success", true);
            response.getWriter().write(JSONObject.fromObject(data).toString());
        } catch (Exception ex) {
            org.apache.log4j.Logger.getLogger(DatabaseController.class).error("Unable to load database statistics", ex);
            response.setStatus(500);
            response.getWriter().write("{\"success\":false}");
        }
    }
}
