package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.dao_interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dto.AuthTokenDTO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class ConcreteAuthTokenDAO extends StringPartitionBase<AuthTokenDTO> implements AuthTokenDAO {

    private static final String TableName = "AuthToken";


    @Override
    protected AuthTokenDTO getItemFromDB(String userAlias) {
        DynamoDbTable<AuthTokenDTO> table = getEnhancedClient().table(TableName, TableSchema.fromBean(AuthTokenDTO.class));
        Key key = Key.builder()
                .partitionValue(userAlias)
                .build();
        AuthTokenDTO authToken = table.getItem(key);
        return authToken;
    }

    @Override
    protected void putItemInDB(AuthTokenDTO item) {
        DynamoDbTable<AuthTokenDTO> table = getEnhancedClient().table(TableName, TableSchema.fromBean(AuthTokenDTO.class));
        table.putItem(item);
    }

    @Override
    protected void removeItemFromDB(String userAlias) {
        DynamoDbTable<AuthTokenDTO> table = getEnhancedClient().table(TableName, TableSchema.fromBean(AuthTokenDTO.class));
        Key key = Key.builder()
                .partitionValue(userAlias)
                .build();
        table.deleteItem(key);
    }
}
