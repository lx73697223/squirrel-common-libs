package com.pi.common.utils.web;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class HttpUtils {

    private final static String HTTP_PREFIX = "http://";

    private final static String HTTPS_PREFIX = "https://";

    private static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";

    private static final String[] IP_HEADER_NAMES = new String[] { "HTTP_X_FORWARDED_FOR", X_FORWARDED_FOR_HEADER,
            "HTTP_CLIENT_IP", "X-Forwarded-Host", "Proxy-Client-IP", "WL-Proxy-Client-IP" };

    public static String httpUrlChangeToHttps(String url) {
        if (url.startsWith(HTTP_PREFIX)) {
            url = HTTPS_PREFIX + url.substring(HTTP_PREFIX.length(), url.length());
        }
        return url;
    }

    public static String httpsUrlChangeToHttp(String url) {
        if (url.startsWith(HTTPS_PREFIX)) {
            url = HTTP_PREFIX + url.substring(HTTPS_PREFIX.length(), url.length());
        }
        return url;
    }

    public static boolean isAsyncRequest(HttpServletRequest request) {
        return ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With")))
               || (request.getParameter("ajax") != null);
    }

    public static String buildFullDomainUrl(HttpServletRequest request) {

        String scheme = request.getScheme().toLowerCase();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        StringBuilder domain = new StringBuilder();
        domain.append(scheme).append("://").append(serverName);

        // Only add port if not default
        if ("http".equals(scheme)) {
            if (serverPort != 80) {
                domain.append(":").append(serverPort);
            }
        } else if ("https".equals(scheme)) {
            if (serverPort != 443) {
                domain.append(":").append(serverPort);
            }
        }

        return domain.toString();
    }

    /**
     Obtains the full URL the client used to make the request.
     <p>
     Note that the server port will not be shown if it is the default server port for HTTP or HTTPS (80 and 443 respectively).
     @return the full URL, suitable for redirects (not decoded).
     */
    public static String buildFullRequestUrl(HttpServletRequest request) {

        StringBuilder url = new StringBuilder(buildFullDomainUrl(request));

        // Use the requestURI as it is encoded (RFC 3986) and hence suitable for redirects.
        url.append(request.getRequestURI());

        String queryString = request.getQueryString();
        if (queryString != null) {
            url.append("?").append(queryString);
        }

        return url.toString();
    }

    /**
     判断是不是微信请求
     @param request
     */
    public static boolean isWechatRequest(HttpServletRequest request) {
        return isWechatRequest(request.getHeader("user-agent"));
    }

    public static boolean isWechatRequest(String userAgent) {
        return StringUtils.contains(userAgent, "MicroMessenger");
    }

    /**
     Obtain current request through {@link org.springframework.web.context.request.RequestContextHolder}.
     */
    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Preconditions.checkNotNull(requestAttributes, "Could not find current request via RequestContextHolder");
        Preconditions.checkState(requestAttributes instanceof ServletRequestAttributes);
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        Preconditions.checkNotNull(servletRequest, "Could not find current HttpServletRequest");
        return servletRequest;
    }

    /**
     Obtain current response through {@link RequestContextHolder}.
     */
    public static HttpServletResponse getCurrentResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Preconditions.checkNotNull(requestAttributes, "Could not find current request via RequestContextHolder");
        Preconditions.checkState(requestAttributes instanceof ServletRequestAttributes);
        HttpServletResponse servletResponse = ((ServletRequestAttributes) requestAttributes).getResponse();
        Preconditions.checkNotNull(servletResponse, "Could not find current HttpServletResponse");
        return servletResponse;
    }

    public static String getRealRequestIp() {
        return getRealRequestIp(getCurrentRequest());
    }

    /**
     根据HttpServletRequest获取请求的真实IP地址
     @param request
     */
    public static String getRealRequestIp(HttpServletRequest request) {

        String ip = getRealRequestIpFromRequestHeader(request);
        if (StringUtils.isEmpty(ip)) {
            ip = request.getRemoteAddr();
        }

        // If the ip is still null, we just return 0.0.0.0 to avoid empty value
        return StringUtils.isEmpty(ip) ? "0.0.0.0" : ip;
    }

    private static String getRealRequestIpFromRequestHeader(HttpServletRequest request) {

        for (String ipHeaderName : IP_HEADER_NAMES) {

            String originalIp = request.getHeader(ipHeaderName);
            if (originalIp == null) {
                continue;
            }
            String[] ips = StringUtils.split(originalIp, ",");
            for (String ip : ips) {
                // filter out port from IP address
                ip = StringUtils.substringBeforeLast(ip, ":");
                if (!InetAddressValidator.getInstance().isValid(ip)) {
                    continue;
                }
                try {
                    // get client ip
                    InetAddress address = InetAddress.getByName(ip);
                    // Check whether the ipAddress is in private range
                    if (!address.isSiteLocalAddress()) {
                        return address.getHostAddress();
                    }
                } catch (UnknownHostException e) {
                    // indicate it's an illegal address
                    // we try to get ip from next request header
                }
            }
        }
        return null;
    }

    public static Cookie createCookie(String name, Object value) {
        return createCookie(name, value, null, -1);
    }

    public static Cookie createCookie(String name, Object value, String path, int maxAge) {
        Cookie cookie = new Cookie(name, value == null ? null : value.toString());
        cookie.setMaxAge(value == null ? 0 : maxAge);
        if (StringUtils.isNotBlank(path)) {
            cookie.setPath(path);
        }

        return cookie;
    }

}
