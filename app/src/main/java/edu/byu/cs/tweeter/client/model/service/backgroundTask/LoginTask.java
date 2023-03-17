package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticationResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticationTask {


    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler,username,password);
    }


    @Override
    public AuthenticationResponse doAuthentication() throws Exception {
            LoginRequest request = new LoginRequest(getUsername(), getPassword());
            return getServerFacade().login(request, UserService.LOGIN_PATH);
    }
}
