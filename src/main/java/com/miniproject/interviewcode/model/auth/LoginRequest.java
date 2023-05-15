package com.miniproject.interviewcode.model.auth;

import javax.validation.constraints.NotBlank;


public class LoginRequest {
    @NotBlank
    private String noTelp;

    @NotBlank
    private String password;

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
