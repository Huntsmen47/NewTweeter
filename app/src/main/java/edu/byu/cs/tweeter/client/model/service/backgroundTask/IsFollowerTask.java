package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.Random;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that determines if one user is following another.
 */
public class IsFollowerTask extends AuthenticatedTask {

    public static final String IS_FOLLOWER_KEY = "is-follower";


    /**
     * The alleged follower.
     */
    private User follower;
    /**
     * The alleged followee.
     */
    private User followee;

    private boolean isFollower;


    public IsFollowerTask(AuthToken authToken, User follower, User followee, Handler messageHandler) {
        super(messageHandler,authToken);
        this.follower = follower;
        this.followee = followee;
    }


    @Override
    protected Pair processTask() {
        try {
            IsFollowerRequest request = new IsFollowerRequest(follower.getAlias(),followee.getAlias(),getAuthToken());
            IsFollowerResponse response =  getServerFacade().isFollower(request, FollowService.IS_FOLLOWER_PATH);
            if (response.isSuccess()) {
                setDateTime(response.getAuthToken().datetime);
                isFollower = response.getIsFollowFlag();
                return new Pair<Boolean,String>(true,"");
            } else {
                return new Pair<Boolean,String>(false,response.getMessage());
            }
        } catch (Exception ex) {
            Log.e("IsFollowerTask", ex.getMessage(), ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putBoolean(IS_FOLLOWER_KEY, isFollower);
    }


}
