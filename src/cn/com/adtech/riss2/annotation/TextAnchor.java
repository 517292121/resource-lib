package cn.com.adtech.riss2.annotation;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/** Offsets are UTF-16 character offsets in normalized chapter text, never screen page numbers. */
public final class TextAnchor {
    private TextAnchor() { }

    /** Chapter names are filesystem identifiers, not HTML. Keep '&', quotes and percent signs literal. */
    public static String chapterParameter(javax.servlet.http.HttpServletRequest request, String name) {
        while (request instanceof cn.com.adtech.riss2.filter.XsslHttpServletRequestWrapper) {
            request = (javax.servlet.http.HttpServletRequest)((javax.servlet.http.HttpServletRequestWrapper)request).getRequest();
        }
        String value = request.getParameter(name);
        return value == null ? "" : value;
    }

    public static String normalize(String text) {
        return text.replaceFirst("^\\uFEFF", "").replace("\r\n", "\n").replace('\r', '\n');
    }

    public static String hash(String text) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(normalize(text).getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder(64);
            for (byte b : digest) result.append(String.format("%02x", b & 255));
            return result.toString();
        } catch (java.security.NoSuchAlgorithmException e) { throw new IllegalStateException(e); }
    }

    public static boolean matches(String text, String hash, int start, int end, String quote) {
        text = normalize(text);
        return start >= 0 && end > start && end <= text.length() && boundary(text, start) && boundary(text, end)
                && hash(text).equals(hash) && text.substring(start, end).equals(quote);
    }

    private static boolean boundary(String text, int offset) {
        return offset == 0 || offset == text.length() || !Character.isLowSurrogate(text.charAt(offset))
                || !Character.isHighSurrogate(text.charAt(offset - 1));
    }
}
