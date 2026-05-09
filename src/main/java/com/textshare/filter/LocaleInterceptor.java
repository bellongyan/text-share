package com.textshare.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;

@Component
public class LocaleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String xLocale = request.getHeader("X-Locale");
        if (xLocale != null && (xLocale.equals("zh") || xLocale.equals("en"))) {
            LocaleContextHolder.setLocale(parseLocale(xLocale));
        } else {
            String acceptLanguage = request.getHeader("Accept-Language");
            if (acceptLanguage != null && acceptLanguage.toLowerCase().startsWith("en")) {
                LocaleContextHolder.setLocale(Locale.ENGLISH);
            } else {
                LocaleContextHolder.setLocale(Locale.CHINESE);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LocaleContextHolder.resetLocaleContext();
    }

    private Locale parseLocale(String locale) {
        return "en".equals(locale) ? Locale.ENGLISH : Locale.CHINESE;
    }
}
