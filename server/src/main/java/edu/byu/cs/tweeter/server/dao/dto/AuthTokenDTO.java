package edu.byu.cs.tweeter.server.dao.dto;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class AuthTokenDTO {

    private AuthToken authToken;

    private String userAlias;

    public AuthTokenDTO(AuthToken authToken, String userAlias) {
        this.authToken = authToken;
        this.userAlias = userAlias;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }
}
