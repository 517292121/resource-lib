package cn.com.adtech.riss2.core.i18n;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import net.sf.json.*;

/** Public UI messages only. Locale is session-scoped; resource content is never translated here. */
public final class FrontendMessages {
    private static final Properties CHINESE = properties("messages_zh.properties");
    private static final Properties TIBETAN = properties("messages_zw.properties");
    private static final Map<String,String> CATALOG = catalog();
    private static final Map<String,String> ZH = dictionary(false);
    private static final Map<String,String> ZW = dictionary(true);
    private FrontendMessages() { }

    private static Properties properties(String name) {
        Properties values = new Properties();
        try (InputStream stream = FrontendMessages.class.getClassLoader().getResourceAsStream(name)) {
            if (stream != null) values.load(stream);
        } catch (IOException error) { throw new IllegalStateException("Cannot load UI messages: " + name, error); }
        return values;
    }
    private static Map<String,String> catalog() {
        Map<String,String> values = new LinkedHashMap<String,String>();
        try (InputStream stream = FrontendMessages.class.getClassLoader().getResourceAsStream("frontend-ui.json")) {
            if (stream == null) return values;
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(); byte[] buffer = new byte[4096]; int length;
            while ((length=stream.read(buffer))!=-1) bytes.write(buffer,0,length);
            JSONArray entries = JSONArray.fromObject(new String(bytes.toByteArray(),"UTF-8"));
            for (Object value:entries) { JSONObject entry=(JSONObject)value; values.put(entry.getString("text"),entry.getString("key")); }
        } catch (IOException error) { throw new IllegalStateException("Cannot load frontend UI catalog",error); }
        return Collections.unmodifiableMap(values);
    }
    public static String normalize(String text) { return text == null ? "" : text.replaceAll("[\\s\\u00a0\\u3000]+", ""); }
    private static Map<String,String> dictionary(boolean tibetan) {
        Map<String,String> values=new LinkedHashMap<String,String>();
        for (Map.Entry<String,String> entry:CATALOG.entrySet()) {
            String source=entry.getKey(), key=entry.getValue();
            String value=tibetan ? TIBETAN.getProperty(key,"").trim() : "";
            // Missing translations have an intentional, visible Chinese fallback.
            if (value.isEmpty()) value=CHINESE.getProperty(key,source);
            values.put(normalize(source),value);
        }
        return Collections.unmodifiableMap(values);
    }
    public static boolean tibetan(HttpServletRequest request) {
        if (Boolean.TRUE.equals(request.getAttribute("annotationAdmin"))) return false;
        HttpSession session=request.getSession(false);
        Object value=session==null?null:session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
        Locale locale=value instanceof Locale?(Locale)value:LocaleContextHolder.getLocale();
        return "zw".equals(locale.getLanguage()) || "bo".equals(locale.getLanguage());
    }
    public static String text(HttpServletRequest request,String source) {
        Map<String,String> values=tibetan(request)?ZW:ZH;
        String result=values.get(normalize(source));
        return result==null || normalize(result).equals(normalize(source))?source:result;
    }
    /** Select a backend-maintained name directly, without looking it up in the static UI dictionary. */
    public static String backendName(HttpServletRequest request,String chinese,String tibetan) {
        if (tibetan(request) && tibetan != null && !tibetan.trim().isEmpty()) return tibetan;
        return chinese == null ? "" : chinese;
    }
    private static String escape(String value) {
        return value.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("'","&#39;");
    }
    public static String html(HttpServletRequest request,String source) { return escape(text(request,source)); }
    public static void prepare(HttpServletRequest request) {
        if (request.getAttribute("frontendText")!=null) return;
        Map<String,String> values=new LinkedHashMap<String,String>();
        for(String source:CATALOG.keySet()) values.put(source,html(request,source));
        request.setAttribute("frontendText",Collections.unmodifiableMap(values));
    }
    public static String clientData(HttpServletRequest request) {
        Map<String,Object> data=new LinkedHashMap<String,Object>();
        data.put("language",tibetan(request)?"zw":"zh"); data.put("messages",tibetan(request)?ZW:ZH);
        return JSONObject.fromObject(data).toString().replace("<","\\u003c").replace(">","\\u003e")
                .replace("&","\\u0026").replace("\u2028","\\u2028").replace("\u2029","\\u2029");
    }
}
