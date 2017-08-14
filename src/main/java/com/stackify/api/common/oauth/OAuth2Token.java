package com.stackify.api.common.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Darin Howard
 */
@Data
public class OAuth2Token {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private int expiresInSeconds;
    private long createdMillis;

    public boolean isExpired() {
        return (System.currentTimeMillis() > (createdMillis + (expiresInSeconds * 1000)));
    }

}
