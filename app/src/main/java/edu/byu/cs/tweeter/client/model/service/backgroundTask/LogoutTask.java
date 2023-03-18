package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {


    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(messageHandler,authToken);
    }

    @Override
    protected void processTask() {
        try{
            LogoutRequest request = new LogoutRequest(getAuthToken());
            LogoutResponse response = getServerFacade().logout(request, UserService.LOGOUT_PATH);
            if(response.isSuccess()){

            }else{
                sendFailedMessage(response.getMessage());
            }
        }catch (IOException | TweeterRemoteException ex){
            Log.e("LogoutTask",ex.getMessage(),ex);
            sendExceptionMessage(ex);
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {

    }
}
