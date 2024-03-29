package com.example.hirememicroserviceUser.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@ToString
public class LoginBody {

    @JsonProperty("idToken")
    private String idToken;

    @JsonCreator
    public LoginBody(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    @Override
    public String toString() {
        return "LoginBody{" +
                "idToken='" + idToken + '\'' +
                '}';
    }
}
