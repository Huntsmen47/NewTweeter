package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedTask<Status> {




    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(messageHandler,authToken,targetUser,limit,lastStatus);
    }




    @Override
    protected Pair<List<Status>, Boolean> getItems() {
        String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
        Status lastStatus = getLastItem() == null ? null:getLastItem();

        FeedRequest request = new FeedRequest(targetUserAlias,getAuthToken(),getLimit(),lastStatus);
        try {
            FeedResponse response = getServerFacade().getFeed(request, StatusService.GET_FEED_PATH);
            if(response.isSuccess()){
                return new Pair<>(response.getFeed(),response.getHasMorePages());
            }else{
                sendFailedMessage(response.getMessage());
            }
        }catch (Exception ex){
            Log.e("Get Feed Task,","Get Feed Task Failed",ex);
            sendExceptionMessage(ex);
        }

        System.out.println("You are returning null here in GetFeedTask, something went wrong here");
        return null;
    }



}
