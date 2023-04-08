package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowRequest {
    private String followeeAlias;

    private AuthToken authToken;

    private String followerAlias;

    private FollowRequest(){}

    public FollowRequest(String followeeAlias, String followerAlias, AuthToken authToken) {
        this.followeeAlias = followeeAlias;
        this.followerAlias = followerAlias;
        this.authToken = authToken;
    }

    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getFollowerAlias() {
        return followerAlias;
    }

    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }
}
