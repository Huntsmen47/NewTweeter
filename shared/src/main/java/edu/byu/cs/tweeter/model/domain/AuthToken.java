package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;

/**
 * Represents an auth token in the system.
 */
public class AuthToken implements Serializable {
    /**
     * Value of the auth token.
     */
    public String token;
    /**
     * Long representation of date/time at which the auth token was created.
     */
    public Long datetime;

    public AuthToken() {
    }

    public AuthToken(String token) {
        this.token = token;
    }

    public AuthToken(String token, Long datetime) {
        this.token = token;
        this.datetime = datetime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getDatetime() {
        return datetime;
    }
}