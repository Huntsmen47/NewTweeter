package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dto.UserDTO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class ConcreteUserDAO implements UserDAO{

    private static final String TableName = "User";

    private static final String UserAliasAttr = "userAlias";

    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_1)
            .build();
    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();


    /**
     * Pre-conditions:
     * - userAlias must not be null
     * - userAlias is a corresponding partition key in the db
     *
     * Post-conditions:
     * User of corresponding alias is returned
     * @param userAlias partition key
     * @return User of corresponding userAlias
     */
    @Override
    public User getUser(String userAlias) throws DataAccessException {
        if(userAlias == null){
            throw new DataAccessException("User alias is null");
        }
        DynamoDbTable<UserDTO> table = enhancedClient.table(TableName, TableSchema.fromBean(UserDTO.class));
        Key key = Key.builder()
                .partitionValue(userAlias)
                .build();
        UserDTO user = table.getItem(key);
        if(user == null){
            throw new DataAccessException("Requested User Alias does not exist in Database");
        }
        return convertUserDTO(user);
    }

    /**
     * Pre-conditions:
     * - user is not null
     * - userAlias is not null
     * - user is not already in the database
     *
     * Post-conditions:
     * - user is in the database
     *
     * @param user
     */
    @Override
    public void addUser(UserDTO user) throws DataAccessException {
        if(user == null){
            throw new DataAccessException("Cannot add null user");
        } else if(user.getUserAlias() == null){
            throw new DataAccessException("userAlias is null, cannot add");
        }
        if(!isInDatabase(user.getUserAlias())){
            throw new DataAccessException("user is already in database");
        }
        DynamoDbTable<UserDTO> table = enhancedClient.table(TableName, TableSchema.fromBean(UserDTO.class));
        table.putItem(user);
    }

    private boolean isInDatabase(String userAlias){
        try {
            getUser(userAlias);
            return false;
        } catch (DataAccessException ex){
            return true;
        }
    }


    /**
     * Pre-conditions:
     * - user must already be in the database
     * Post-conditions:
     * - specified user is no longer in the database
     * @param userAlias
     */
    @Override
    public void deleteUser(String userAlias) throws DataAccessException {
        try {
            getUser(userAlias);
        }catch (DataAccessException ex){
            throw ex;
        }

        DynamoDbTable<UserDTO> table = enhancedClient.table(TableName, TableSchema.fromBean(UserDTO.class));
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
     * @param user
     */
    @Override
    public void updateUser(UserDTO user) {

    }

    @Override
    public DataPage<User> getPageOfUsers(int pageSize, String lastUserAlias) {
        return null;
    }

    @Override
    public String getPassword(String userAlias) {
        return null;
    }

    private User convertUserDTO(UserDTO userDTO){
        User user = new User(userDTO.getFirstName(),userDTO.getLastName(),
                userDTO.getUserAlias(),userDTO.getImageUrl());
        return user;
    }
}
