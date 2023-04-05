package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends AuthenticatedTask {



    /**
     * The new status being sent. Contains all properties of the status,
     * including the identity of the user sending the status.
     */
    private Status status;


    public PostStatusTask(AuthToken authToken, Status status, Handler messageHandler) {
        super(messageHandler,authToken);
        this.status = status;
    }


    @Override
    protected Pair processTask() {
        try {
        PostStatusRequest request = new PostStatusRequest(getAuthToken(),status);
        PostStatusResponse response =  getServerFacade().postStatus(request, StatusService.POST_STATUS_PATH);
        if (response.isSuccess()) {
            return new Pair<Boolean,String>(true,"");
        } else {
            return new Pair<Boolean,String>(false,response.getMessage());
        }
    } catch (Exception ex) {
        Log.e("PostStatusTask", ex.getMessage(), ex);
            return new Pair<Boolean,String>(false, ex.getMessage());
    }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {

    }
}
