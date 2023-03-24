package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;

public interface DataAccessObject<T,K,L> {
    T getItem(K partitionKey,L sortKey);
    void addItem(T item);
    void deleteItem(K partitionKey,L sortKey);
    void updateItem (T item);
                                          // need to figure out what last item datatype should be?
    DataPage<T> getPageOfItem(int pageSize,String lastUserAlias);
}
