package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.observer.PageObserver;

public class PageHandler<T> extends BackgroundTaskHandler<PageObserver> {

    public PageHandler(PageObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, PageObserver observer) {
        List<T> items = (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
        Boolean hasMorePages = data.getBoolean(GetFollowingTask.MORE_PAGES_KEY);
        observer.getPaging(items, hasMorePages);
    }

    @Override
    protected String getErrorMessage() {
        return "Failed to load data";
    }
}
