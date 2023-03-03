package com.example.Employee_Management_System.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccessTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Long expiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("scope")
    private String scope;
}
