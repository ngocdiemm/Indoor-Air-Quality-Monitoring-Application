package com.uit.myairquality.Model;


import com.google.gson.annotations.SerializedName;

public class TokenResponse {
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("expires_in")
    private int expiresIn;

    @SerializedName("refresh_expires_in")
    private int refreshExpiresIn;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("not-before-policy")
    private long notBeforePolicy;

    @SerializedName("session_state")
    private String sessionState;

    @SerializedName("scope")
    private String scope;

    // Ví dụ:
    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
