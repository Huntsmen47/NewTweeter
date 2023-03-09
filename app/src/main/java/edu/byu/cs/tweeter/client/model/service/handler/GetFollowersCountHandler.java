package edu.byu.cs.tweeter.client.model.service.handler;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;

public class GetFollowersCountHandler extends GetCountHandler<GetFollowersCountTask> {

    public GetFollowersCountHandler(CountObserver observer) {
        super(observer);
    }

    @Override
    protected String getErrorMessage() {
        return "Failed to get Followers Count";
    }

    @Override
    protected void setItemCount(int count,CountObserver observer) {
        observer.setFollowerCount(count);
    }
}
