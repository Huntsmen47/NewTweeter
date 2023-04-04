package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends CountTask {




    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(messageHandler,authToken,targetUser);
    }

    @Override
    public CountResponse getResponse(String targetUser) throws IOException, TweeterRemoteException {
        FollowerCountRequest request = new FollowerCountRequest(getAuthToken(),targetUser);
        return getServerFacade().getFollowerCount(request, FollowService.FOLLOWER_COUNT_PATH);
    }
}
