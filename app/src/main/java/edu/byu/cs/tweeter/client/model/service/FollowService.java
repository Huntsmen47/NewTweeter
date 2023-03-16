package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.handler.FollowHandler;
import edu.byu.cs.tweeter.client.model.service.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.model.service.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.model.service.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.handler.PageHandler;
import edu.byu.cs.tweeter.client.model.service.handler.UnfollowHandler;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.FollowButtonObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PageObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends BaseService {

    public static final String URL_PATH = "/getfollowing";

    public void changeToUnfollow(User selectedUser, FollowButtonObserver mainObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new UnfollowHandler(mainObserver));
        execute(unfollowTask);
    }

    public void changeToFollow(User selectedUser, FollowButtonObserver mainObserver) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowHandler(mainObserver));
        execute(followTask);
    }


    public void loadMoreFollowees(User user, int pageSize, User lastFollowee, PageObserver getFollowingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowee, new PageHandler<User>(getFollowingObserver));
        execute(getFollowingTask);
    }

    public void loadMoreFollowers(User user, int pageSize, User lastFollower,
                                  PageObserver pageObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollower, new PageHandler<User>(pageObserver));
        execute(getFollowersTask);
    }

    public void addFollowingButton(User selectedUser, IsFollowObserver mainObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(mainObserver));
        execute(isFollowerTask);
    }

    public void updateSelectedUserFollowAndFollower(User selectedUser, CountObserver mainObserver) {

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(mainObserver));


        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(mainObserver));

        doubleExecute(followersCountTask,followingCountTask);

    }

    public <T extends BackgroundTask> void doubleExecute(T task1, T task2){
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(task1);
        executor.execute(task2);
    }

}
