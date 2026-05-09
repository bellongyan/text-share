package com.textshare.util;

public final class HtmlEscapeUtil {

    private HtmlEscapeUtil() {
    }

    public static String escape(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }
        return content
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    public static String unescape(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }
        return content
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&#x27;", "'");
    }
}
