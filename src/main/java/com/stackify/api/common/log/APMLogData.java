package com.stackify.api.common.log;

import com.stackify.api.WebRequestDetail;

import java.util.Map;

/**
 * Provides means for APM to link and provide data elements from the current profiler trace.
 *
 * @author Darin Howard
 */
public class APMLogData {

    /**
     * Details if APM has established a link to this class. When false all methods return null.
     */
    public static boolean isLinked() {
        return false;
    }

    public static String getTransactionId() {
        // empty implementation - value will be provided by apm
        return null;
    }

    public static String getUser() {
        // empty implementation - value will be provided by apm
        return null;
    }

    public static WebRequestDetail getWebRequest() {
        if (isLinked()) {
            WebRequestDetail.Builder builder = WebRequestDetail.newBuilder();
            builder.userIpAddress(getUserIpAddress());
            builder.httpMethod(getHttpMethod());
            builder.requestProtocol(getRequestProtocol());
            builder.requestUrl(getRequestUrl());
            builder.requestUrlRoot(getRequestUrlRoot());
            builder.referralUrl(getReferralUrl());
            builder.headers(getHeaders());
            builder.cookies(getCookies());
            builder.queryString(getQueryString());
            builder.sessionData(getSessionData());
            return builder.build();
        }
        return null;
    }

    private static String getUserIpAddress() {
        // empty implementation - value will be provided by apm
        return null;
    }

    private static String getHttpMethod() {
        // empty implementation - value will be provided by apm
        return null;
    }

    private static String getRequestProtocol() {
        // empty implementation - value will be provided by apm
        return null;
    }

    private static String getRequestUrl() {
        // empty implementation - value will be provided by apm
        return null;
    }

    private static String getRequestUrlRoot() {
        // empty implementation - value will be provided by apm
        return null;
    }

    private static String getReferralUrl() {
        // empty implementation - value will be provided by apm
        return null;
    }

    private static Map<String, String> getHeaders() {
        // empty implementation - value will be provided by apm
        return null;
    }

    private static Map<String, String> getCookies() {
        // empty implementation - value will be provided by apm
        return null;
    }

    private static Map<String, String> getQueryString() {
        // empty implementation - value will be provided by apm
        return null;
    }

    private static Map<String, String> getSessionData() {
        // empty implementation - value will be provided by apm
        return null;
    }
}
