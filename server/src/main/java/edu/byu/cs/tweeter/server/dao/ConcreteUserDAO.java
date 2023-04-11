package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.dao_interfaces.UserDAO;
import edu.byu.cs.tweeter.server.dao.dto.UserDTO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class ConcreteUserDAO extends StringPartitionBase<UserDTO> implements UserDAO {

    private static final String TableName = "User";


    @Override
    protected UserDTO getItemFromDB(String userAlias) {
        DynamoDbTable<UserDTO> table = getEnhancedClient().table(TableName, TableSchema.fromBean(UserDTO.class));
        Key key = Key.builder()
                .partitionValue(userAlias)
                .build();
        UserDTO user = table.getItem(key);
        return user;
    }

    @Override
    protected void putItemInDB(UserDTO item) {
        DynamoDbTable<UserDTO> table = getEnhancedClient().table(TableName, TableSchema.fromBean(UserDTO.class));
        table.putItem(item);
    }

    @Override
    protected void removeItemFromDB(String userAlias) {
        DynamoDbTable<UserDTO> table = getEnhancedClient().table(TableName, TableSchema.fromBean(UserDTO.class));
        Key key = Key.builder()
                .partitionValue(userAlias)
                .build();
        table.deleteItem(key);
    }

    /**
     * Pre-conditions:
     * - User must already be in the database
     * - passed in user must not be null
     * - userAlias is not null
     *
     * Post-Conditions:
     * - Update will reflect in the database
     *
     * @param user
     */
    @Override
    public void updateUser(UserDTO user) throws DataAccessException {
        if(user == null){
            throw new DataAccessException("User is null, cannot update");
        } else if (user.getUserAlias() == null) {
            throw new DataAccessException("User has nullAlias, cannot update");
        } else if (!isInDatabase(user.getUserAlias())) {
            throw new DataAccessException("User not in database, can't update");
        }

        DynamoDbTable<UserDTO> table = getEnhancedClient().table(TableName, TableSchema.fromBean(UserDTO.class));
        table.updateItem(user);

    }


    /**
     * Pre-Conditions
     * - userAlias is not null
     * - userAlias is in database
     * Post-Conditions
     * - password of corresponding user is returned
     * @param userAlias
     * @return
     */
    @Override
    public String getPassword(String userAlias) throws DataAccessException {
        if(userAlias == null){
            throw new DataAccessException("null alias, cannot get password");
        }else if(!isInDatabase(userAlias)){
            throw new DataAccessException("alias not in database, cannot get user");
        }
        UserDTO user = getItemFromDB(userAlias);
        return user.getPassword();
    }

    @Override
    public void addUserBatch(List<UserDTO> users) {
        List<UserDTO> batchToWrite = new ArrayList<>();
        for (UserDTO u : users) {
            batchToWrite.add(u);

            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunkOfUserDTOs(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (batchToWrite.size() > 0) {
            // package this batch up and send to DynamoDB.
            writeChunkOfUserDTOs(batchToWrite);
        }

    }

    private void writeChunkOfUserDTOs(List<UserDTO> userDTOs) {
        if(userDTOs.size() > 25)
            throw new RuntimeException("Too many users to write");

        DynamoDbTable<UserDTO> table = getEnhancedClient().table(TableName, TableSchema.fromBean(UserDTO.class));
        WriteBatch.Builder<UserDTO> writeBuilder = WriteBatch.builder(UserDTO.class).mappedTableResource(table);
        for (UserDTO item : userDTOs) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = getEnhancedClient().batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfUserDTOs(result.unprocessedPutItemsForTable(table));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


}
