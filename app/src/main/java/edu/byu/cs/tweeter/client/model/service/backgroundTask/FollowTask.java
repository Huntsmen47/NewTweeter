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
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticationResponse;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.util.Pair;

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
    protected Pair processTask() {
        try {
            FollowRequest followRequest = new FollowRequest(followee.getAlias(),
                    Cache.getInstance().getCurrUser().getAlias(),getAuthToken());
            FollowResponse response =  getServerFacade().follow(followRequest, UserService.FOLLOW_PATH);
            if (response.isSuccess()) {
                return new Pair<Boolean,String>(true,"");
            } else {
                return new Pair<Boolean,String>(false,response.getMessage());
            }
        } catch (Exception ex) {
            Log.e("FollowTask", ex.getMessage(), ex);
            return new Pair<Boolean,String>(false,ex.getMessage());
        }

    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {

    }
}
