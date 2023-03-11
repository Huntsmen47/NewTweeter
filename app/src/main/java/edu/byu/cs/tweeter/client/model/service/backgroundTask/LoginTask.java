package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
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
    public void doAuthentication() {
        try {
            LoginRequest request = new LoginRequest(getUsername(), getPassword());
            LoginResponse response = getServerFacade().login(request, UserService.URL_PATH);

            if (response.isSuccess()) {
                setAuthenticatedUser(response.getUser());
                setAuthToken(response.getAuthToken());
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (Exception ex) {
            Log.e("LoginTask", ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }
}
