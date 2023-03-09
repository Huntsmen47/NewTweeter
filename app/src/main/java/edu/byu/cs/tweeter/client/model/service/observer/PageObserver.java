package edu.byu.cs.tweeter.client.model.service.observer;

import java.util.List;

public interface PageObserver<T> extends ServiceObserver{
    void getPaging(List<T> items,boolean hasMorePages);
}
