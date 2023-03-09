package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthenticatedTask {

    public static final String USER_KEY = "user";



    /**
     * Alias (or handle) for user whose profile is being retrieved.
     */
    private String alias;

    private User user;


    public GetUserTask(AuthToken authToken, String alias, Handler messageHandler) {
        super(messageHandler,authToken);
        this.alias = alias;

    }

    @Override
    protected void processTask() {
         user = getUser();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, user);
    }

    private User getUser() {
        user = getFakeData().findUserByAlias(alias);
        return user;
    }


}
