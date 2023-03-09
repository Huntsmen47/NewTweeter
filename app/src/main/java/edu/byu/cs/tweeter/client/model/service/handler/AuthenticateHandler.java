package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.AuthenticationTask;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticateHandler<T extends AuthenticationTask> extends BackgroundTaskHandler<AuthenticateObserver> {
    public AuthenticateHandler(AuthenticateObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, AuthenticateObserver observer) {
        User registeredUser = (User) data.getSerializable(T.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(T.AUTH_TOKEN_KEY);
        Cache.getInstance().setCurrUser(registeredUser);
        Cache.getInstance().setCurrUserAuthToken(authToken);
        authenticate(observer,registeredUser);
    }

    public abstract void authenticate(AuthenticateObserver observer, User user);

}
