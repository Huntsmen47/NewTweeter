package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import java.io.IOException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticationResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

public abstract class AuthenticationTask extends BackgroundTask {
    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";
    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    private String username;
    /**
     * The user's password.
     */
    private String password;

    private User authenticatedUser;

    private AuthToken authToken;

    public AuthenticationTask(Handler messageHandler,String username,String password) {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }

    public abstract AuthenticationResponse doAuthentication() throws IOException, TweeterRemoteException;

    /**
     User authenticatedUser = getFakeData().getFirstUser();
     AuthToken authToken = getFakeData().getAuthToken();
     return new Pair<>(authenticatedUser, authToken);
     */

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, authenticatedUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }

    @Override
    protected Pair processTask() {
        try {
            AuthenticationResponse response =  doAuthentication();
            if (response.isSuccess()) {
                setAuthenticatedUser(response.getUser());
                setAuthToken(response.getAuthToken());
                Cache.getInstance().setCurrUserAuthToken(response.getAuthToken());
                Cache.getInstance().setCurrUser(response.getUser());
                return new Pair<Boolean,String>(true,"");
            } else {
                return new Pair<Boolean,String>(false,response.getMessage());
            }
        } catch (Exception ex) {
            Log.e("AuthenticationTask", ex.getMessage(), ex);
            return new Pair<Boolean,String>(false,ex.getMessage());
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setAuthenticatedUser(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
