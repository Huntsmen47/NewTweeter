package edu.byu.cs.tweeter.client.model.service.handler;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;

public class GetFollowingCountHandler extends GetCountHandler<GetFollowingCountTask> {

    public GetFollowingCountHandler(CountObserver observer) {
        super(observer);
    }

    @Override
    protected String getErrorMessage() {
        return "Failed to get Following Count";
    }

    @Override
    protected void setItemCount(int count, CountObserver observer) {
        observer.setFollowingCount(count);
    }
}
