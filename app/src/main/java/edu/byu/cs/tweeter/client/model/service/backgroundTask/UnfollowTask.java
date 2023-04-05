package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.util.Pair;

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
    protected Pair processTask() {
        try{
            UnfollowRequest request = new UnfollowRequest(followee.getAlias(),
                    Cache.getInstance().getCurrUser().getAlias(),getAuthToken());
            UnfollowResponse response = getServerFacade().unfollow(request, FollowService.UNFOLLOW_PATH);
            if(response.isSuccess()){
                return new Pair<Boolean,String>(true,"");
            }else{
                return new Pair<Boolean,String>(false,response.getMessage());
            }
        }catch (Exception ex){
            Log.e("UnfollowTask",ex.getMessage(),ex);
            return new Pair<Boolean,String>(false, ex.getMessage());
        }

    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {

    }
}
