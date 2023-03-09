package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.CountTask;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;

public abstract class GetCountHandler<T extends CountTask> extends BackgroundTaskHandler<CountObserver> {




    public GetCountHandler(CountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, CountObserver observer) {
        setItemCount(data.getInt(T.COUNT_KEY),observer);
    }

    protected abstract void setItemCount(int count,CountObserver observer);

}
