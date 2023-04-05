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
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedTask<Status> {



    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(messageHandler,authToken,targetUser,limit,lastStatus);

    }


    @Override
    protected Pair<List<Status>, Boolean> getItems() {
        String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
        Status lastStatus = getLastItem() == null ? null:getLastItem();

        GetStoryRequest request = new GetStoryRequest(targetUserAlias,getAuthToken(),getLimit(),lastStatus);
        try {
            GetStoryResponse response = getServerFacade().getStory(request, StatusService.GET_STORY_PATH);
            if(response.isSuccess()){
                setAuthToken(response.getAuthToken());
                return new Pair<>(response.getStory(),response.getHasMorePages());
            }else{
                sendFailedMessage(response.getMessage());
            }
        }catch (Exception ex){
            Log.e("Get Story Task,","Get Story Task Failed",ex);
            sendExceptionMessage(ex);
        }

        System.out.println("You are returning null here in GetStoryTask, something went wrong here");
        return null;
    }
}