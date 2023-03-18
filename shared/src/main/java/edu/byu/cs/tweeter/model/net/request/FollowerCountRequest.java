package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowerCountRequest {
    private AuthToken authToken;
    private String targetUserAlias;


    private FollowerCountRequest(){}

    public FollowerCountRequest(AuthToken authToken, String targetUserAlias) {
        this.authToken = authToken;
        this.targetUserAlias = targetUserAlias;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getTargetUserAlias() {
        return targetUserAlias;
    }

    public void setTargetUserAlias(String targetUserAlias) {
        this.targetUserAlias = targetUserAlias;
    }
}
