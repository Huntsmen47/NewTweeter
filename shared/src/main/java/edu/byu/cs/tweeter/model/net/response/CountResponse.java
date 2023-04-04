package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class CountResponse extends Response {
    private int count;

    private AuthToken authToken;


   public CountResponse(String message) {
        super(false, message);
    }

    public CountResponse(int count,AuthToken authToken) {
        super(true, null);
        this.count = count;
        this.authToken = authToken;
    }

    public int getCount() {
        return count;
    }

    public AuthToken getAuthToken() {
       return authToken;
    }
}
