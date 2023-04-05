package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UnfollowResponse extends Response{


    private AuthToken authToken;

    UnfollowResponse(String message) {
        super(false, message);
    }

    public UnfollowResponse(AuthToken authToken) {
        super(true, null);
        this.authToken = authToken;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
