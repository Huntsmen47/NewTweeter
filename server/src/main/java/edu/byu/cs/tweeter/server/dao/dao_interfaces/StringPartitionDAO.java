package edu.byu.cs.tweeter.server.dao.dao_interfaces;

import edu.byu.cs.tweeter.server.dao.DataAccessException;

public interface StringPartitionDAO<T> {

    T getItem(String partitionID) throws DataAccessException;
    void addItem(T item,String userAlias) throws  DataAccessException;
    void deleteItem(String userAlias) throws  DataAccessException;

    boolean isInDatabase(String userAlias);
}
