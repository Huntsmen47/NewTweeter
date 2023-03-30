package edu.byu.cs.tweeter.server.dao.dao_interfaces;

import edu.byu.cs.tweeter.server.dao.DataAccessException;

public interface AliasPartitionDAO<T> {

    T getItem(String userAlias) throws DataAccessException;
    void addItem(T item,String userAlias) throws  DataAccessException;
    void deleteItem(String userAlias) throws  DataAccessException;

    boolean isInDatabase(String userAlias);
}
