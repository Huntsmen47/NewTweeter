package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedTask<User> {


    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(messageHandler,authToken,targetUser,limit,lastFollower);

    }


    @Override
    protected Pair<List<User>, Boolean> getItems() {
        String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
        String lastFollowerAlias = getLastItem() == null ? null : getLastItem().getAlias();

        FollowersRequest request = new FollowersRequest(getAuthToken(),targetUserAlias,
                getLimit(),lastFollowerAlias);

        try{
            FollowersResponse response = getServerFacade().getFollowers(request, FollowService.GET_FOLLOWERS_PATH);
            if(response.isSuccess()){
                setAuthToken(response.getAuthToken());
                return new Pair<>(response.getFollowers(),response.getHasMorePages());
            }else{
                sendFailedMessage(response.getMessage());
            }
        }catch(IOException | TweeterRemoteException ex){
            Log.e("Get Followers Task,","Get Followers Task Failed",ex);
            sendExceptionMessage(ex);
        }

        System.out.println("You are returning null here in GetFollowersTask, something went wrong here");
        return null;
    }
}
