package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterResponse extends AuthenticationResponse {


    /**
     * Creates a response with message stating why register was unsuccessful
     * @param message error message
     */
    public RegisterResponse(String message) {
        super(message);
    }

    public RegisterResponse(User user, AuthToken authToken) {
        super(user,authToken);
    }
}
