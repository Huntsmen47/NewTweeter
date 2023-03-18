package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.response.CountResponse;

public abstract class CountTask extends AuthenticatedTask{


    public static final String COUNT_KEY = "count";

    private int count;

    /**
     * The user whose following count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private User targetUser;

    public CountTask(Handler messageHandler, AuthToken authToken, User targetUser) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
    }

    public abstract CountResponse getResponse() throws IOException, TweeterRemoteException;


    @Override
    protected void processTask() {
        try {
            CountResponse response = getResponse();
            if(response.isSuccess()){
                count = response.getCount();
            }else{
                sendFailedMessage(response.getMessage());
            }
        }catch (IOException|TweeterRemoteException ex){
            Log.e("CountTask",ex.getMessage(),ex);
            sendExceptionMessage(ex);
        }

    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, count);
    }
}
