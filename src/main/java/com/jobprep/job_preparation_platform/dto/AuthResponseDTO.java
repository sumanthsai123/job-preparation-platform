package com.jobprep.job_preparation_platform.dto;


public class AuthResponseDTO {

    private String token;
    private String tokenType;

    public AuthResponseDTO(String token, String tokenType) {
        this.token = token;
        this.tokenType = tokenType;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }
}
