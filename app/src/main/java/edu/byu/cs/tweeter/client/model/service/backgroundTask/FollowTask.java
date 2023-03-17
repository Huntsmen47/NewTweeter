package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticationResponse;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthenticatedTask {



    /**
     * The user that is being followed.
     */
    private User followee;

    public FollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(messageHandler,authToken);
        this.followee = followee;
    }


    @Override
    protected void processTask() {
        try {
            FollowRequest followRequest = new FollowRequest(followee.getAlias(),"currentUser",getAuthToken());
            FollowResponse response =  getServerFacade().follow(followRequest, UserService.FOLLOW_PATH);
            if (response.isSuccess()) {

                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e("FollowTask", ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }

    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {

    }
}
