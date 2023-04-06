package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class PostStatusResponse extends Response{

    private AuthToken authToken;
    public PostStatusResponse(String message) {
        super(false, message);
    }

    public PostStatusResponse(AuthToken authToken) {
        super(true,null);
        this.authToken = authToken;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
