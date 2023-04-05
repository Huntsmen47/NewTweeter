package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class IsFollowerResponse extends Response{

    private boolean isFollowFlag;

    private AuthToken authToken;

    IsFollowerResponse(String message) {
        super(false, message);
    }

    public IsFollowerResponse(boolean isFollowFlag,AuthToken authToken) {
        super(true, null);
        this.isFollowFlag = isFollowFlag;
        this.authToken = authToken;
    }

    public boolean isFollowFlag() {
        return isFollowFlag;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
