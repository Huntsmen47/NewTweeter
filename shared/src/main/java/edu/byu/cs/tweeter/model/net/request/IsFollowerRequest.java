package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class IsFollowerRequest {

    private String allegedFollowerAlias;

    private String allegedFolloweeAlias;

    private AuthToken authToken;

    private IsFollowerRequest(){}

    public IsFollowerRequest(String allegedFollowerAlias, String allegedFolloweeAlias, AuthToken authToken) {
        this.allegedFollowerAlias = allegedFollowerAlias;
        this.allegedFolloweeAlias = allegedFolloweeAlias;
        this.authToken = authToken;
    }

    public String getAllegedFollowerAlias() {
        return allegedFollowerAlias;
    }

    public void setAllegedFollowerAlias(String allegedFollowerAlias) {
        this.allegedFollowerAlias = allegedFollowerAlias;
    }

    public String getAllegedFolloweeAlias() {
        return allegedFolloweeAlias;
    }

    public void setAllegedFolloweeAlias(String allegedFolloweeAlias) {
        this.allegedFolloweeAlias = allegedFolloweeAlias;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
