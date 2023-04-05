package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedTask<User> {






    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastFollowee);
        this.messageHandler = messageHandler;
    }






    @Override
    protected Pair<List<User>, Boolean> getItems() {
        String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
        String lastFolloweeAlias = getLastItem() == null ? null : getLastItem().getAlias();

        FollowingRequest request = new FollowingRequest(getAuthToken(),targetUserAlias,
                getLimit(),lastFolloweeAlias);

        try{
            FollowingResponse response = getServerFacade().getFollowees(request, FollowService.GET_FOLLOWING_PATH);
            if(response.isSuccess()){
                setDateTime(response.getAuthToken().datetime);
                return new Pair<>(response.getFollowees(),response.getHasMorePages());
            }else{
                sendFailedMessage(response.getMessage());
            }
        }catch(Exception ex){
            Log.e("Get Following Task,","Get Following Task Failed",ex);
            sendExceptionMessage(ex);
        }

        System.out.println("You are returning null here in GetFollowingTask, something went wrong here");
        return null;
    }
}
