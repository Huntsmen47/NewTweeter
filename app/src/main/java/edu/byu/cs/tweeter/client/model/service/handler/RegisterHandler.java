package edu.byu.cs.tweeter.client.model.service.handler;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterHandler extends AuthenticateHandler<RegisterTask> {



    public RegisterHandler(AuthenticateObserver observer) {
        super(observer);
    }

    @Override
    protected String getErrorMessage() {
        return "Failed to register";
    }


    @Override
    public void authenticate(AuthenticateObserver observer, User registeredUser) {
        observer.authenticate(registeredUser);
    }
}
