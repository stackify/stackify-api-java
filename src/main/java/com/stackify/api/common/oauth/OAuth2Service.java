package com.stackify.api.common.oauth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.stackify.api.EnvironmentDetail;
import com.stackify.api.common.ApiConfiguration;
import com.stackify.api.common.http.HttpClient;
import com.stackify.api.common.http.HttpResponse;
import lombok.NonNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Darin Howard
 */
public class OAuth2Service {

    private final static Map<EnvironmentDetail, OAuth2Token> TOKENS = new ConcurrentHashMap<EnvironmentDetail, OAuth2Token>();

    private final ApiConfiguration apiConfiguration;

    public OAuth2Service(@NonNull final ApiConfiguration apiConfiguration) {
        this.apiConfiguration = apiConfiguration;
    }

    public synchronized OAuth2Token getAccessToken(@NonNull final EnvironmentDetail environmentDetail) throws IOException {

        // use cached token if available and not expired
        if (TOKENS.containsKey(environmentDetail)) {
            OAuth2Token oAuth2Token = TOKENS.get(environmentDetail);
            if (oAuth2Token != null && !oAuth2Token.isExpired()) {
                return oAuth2Token;
            }
        }

        OAuth2Token oAuth2Token = requestOAuthToken(environmentDetail);
        if (oAuth2Token != null) {
            TOKENS.put(environmentDetail, oAuth2Token);
        }

        return oAuth2Token;
    }

    /**
     * Requests oauth token from server for given EnvironmentalDetail
     */
    private OAuth2Token requestOAuthToken(@NonNull final EnvironmentDetail environmentDetail) throws IOException {

        Map<String, String> authParametersMap = new HashMap<String, String>();

        authParametersMap.put("client_id", apiConfiguration.getApiKey());
        authParametersMap.put("scope", "api");
        authParametersMap.put("grant_type", "client_credentials");
        authParametersMap.put("plat", "java");

        if (environmentDetail.getDeviceName() != null) {
            authParametersMap.put("device_name", environmentDetail.getDeviceName());
        }
        if (environmentDetail.getConfiguredEnvironmentName() != null) {
            authParametersMap.put("env_name", environmentDetail.getConfiguredEnvironmentName());
        }
        if (environmentDetail.getConfiguredAppName() != null) {
            authParametersMap.put("app_name", environmentDetail.getConfiguredAppName());
        }
        if (environmentDetail.getAppLocation() != null) {
            authParametersMap.put("file_path", environmentDetail.getAppLocation());
        }

        StringBuilder stringBuffer = new StringBuilder();
        Iterator it = authParametersMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            stringBuffer.append(entry.getKey()).append("=").append(entry.getValue());
            if (it.hasNext()) {
                stringBuffer.append("&");
            }
        }

        byte[] body = stringBuffer.toString().getBytes("UTF-8");
        Map<String, String> requestProperties = new HashMap<String, String>();
        requestProperties.put("content-type", "application/x-www-form-urlencoded");

        long createdMillis = System.currentTimeMillis(); // set prior to request to ensure we error in expiring too soon
        HttpClient httpClient = new HttpClient();
        HttpResponse response = httpClient.executePost(apiConfiguration.getAuthUrl(), "/oauth2/token", body, false, requestProperties);

        if (response.getStatusCode() != HttpURLConnection.HTTP_OK) {
            return null;
        }

        ObjectReader jsonReader = new ObjectMapper().reader(new TypeReference<OAuth2Token>() {
        });
        OAuth2Token oAuth2Token = jsonReader.readValue(response.getResponseBody());
        oAuth2Token.setCreatedMillis(createdMillis);
        return oAuth2Token;
    }

}
