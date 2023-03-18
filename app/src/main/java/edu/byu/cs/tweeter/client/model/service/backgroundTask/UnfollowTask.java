package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthenticatedTask {


    /**
     * The user that is being followed.
     */
    private User followee;

    public UnfollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(messageHandler,authToken);
        this.followee = followee;

    }

    @Override
    protected void processTask() {
        try{
            UnfollowRequest request = new UnfollowRequest(followee.getAlias(),
                    "currentUserAlias",getAuthToken());
            UnfollowResponse response = getServerFacade().unfollow(request, FollowService.UNFOLLOW_PATH);
            if(response.isSuccess()){

            }else{
                sendFailedMessage(response.getMessage());
            }
        }catch (IOException | TweeterRemoteException ex){
            Log.e("UnfollowTask",ex.getMessage(),ex);
            sendExceptionMessage(ex);
        }

    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {

    }
}
