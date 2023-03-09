package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
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
        Pair<List<User>, Boolean> pageOfUsers = getFakeData().getPageOfUsers((User)getLastItem(),
                getLimit(), getTargetUser());
        return pageOfUsers;
    }
}
