package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.dao_interfaces.StringPartitionDAO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public abstract class StringPartitionBase<T> implements StringPartitionDAO<T> {

    private static DynamoDbClient dynamoDbClient;
    private static DynamoDbEnhancedClient enhancedClient;

    @Override
    public T getItem(String userAlias) throws DataAccessException{
        if(userAlias == null){
            throw new DataAccessException("Item is null");
        }

        T item = getItemFromDB(userAlias);

        if(item == null){
            throw new DataAccessException("Requested Item does not exist in Database");
        }
        return item;
    }

    protected abstract T getItemFromDB(String userAlias);

    @Override
    public void addItem(T item,String userAlias) throws DataAccessException {
        if(item == null){
            throw new DataAccessException("Cannot add null item");
        } else if(userAlias == null){
            throw new DataAccessException("Partition key is null on item, cannot add");
        }
        if(isInDatabase(userAlias)){
            throw new DataAccessException("partition key for item already in use");
        }
        putItemInDB(item);


    }

    protected abstract void putItemInDB(T item);

    protected abstract void removeItemFromDB(String userAlias);

    @Override
    public void deleteItem(String userAlias) throws  DataAccessException{
        try {
            getItem(userAlias);
        }catch (DataAccessException ex){
            throw ex;
        }


        removeItemFromDB(userAlias);

    }

    protected DynamoDbClient getDynamoDbClient(){
        if(dynamoDbClient == null){
            dynamoDbClient = DynamoDbClient.builder()
                    .region(Region.US_EAST_1)
                    .build();
        }

        return dynamoDbClient;
    }

    protected DynamoDbEnhancedClient getEnhancedClient(){
        if(enhancedClient == null){
            enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(getDynamoDbClient())
                    .build();
        }
        return enhancedClient;
    }

    @Override
    public boolean isInDatabase(String userAlias){
        try {
            getItem(userAlias);
            return true;
        } catch (DataAccessException ex){
            return false;
        }
    }
}
