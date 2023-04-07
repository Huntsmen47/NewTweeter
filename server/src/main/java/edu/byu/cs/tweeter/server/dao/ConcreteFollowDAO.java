package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.dto.FollowDTO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class ConcreteFollowDAO extends BaseDAO implements FollowDAO {


    private static final String TableName = "Follows";
    public static final String IndexName = "follows_index";

    private static final String FollowerHandleAttr = "follower_handle";
    private static final String FolloweeHandleAttr = "followee_handle";

    private static boolean isNonEmptyString(String value){
        return (value != null && value.length()>0);
    }

    @Override
    public void putFollow(String follower_handle, String followerName,
                          String followee_handle, String followeeName){
        DynamoDbTable<FollowDTO> table = getEnhancedClient().table(TableName, TableSchema.fromBean(FollowDTO.class));
        Key key = Key.builder()
                .partitionValue(follower_handle).sortValue(followee_handle)
                .build();
        FollowDTO dbFollow = table.getItem(key);
        if(dbFollow != null){
            dbFollow.setFollowerName(followerName);
            dbFollow.setFolloweeName(followeeName);
            table.updateItem(dbFollow);
        }else{
            FollowDTO newFollow = new FollowDTO();
            newFollow.setFolloweeName(followeeName);
            newFollow.setFollowerName(followerName);
            newFollow.setFollowee_handle(followee_handle);
            newFollow.setFollower_handle(follower_handle);
            table.putItem(newFollow);
        }

    }

    @Override
    public FollowDTO getFollow(String follower_handle, String followee_handle){
        DynamoDbTable<FollowDTO> table = getEnhancedClient().table(TableName,TableSchema.fromBean(FollowDTO.class));
        Key key = Key.builder()
                .partitionValue(follower_handle).sortValue(followee_handle)
                .build();
        FollowDTO follow = table.getItem(key);
        return follow;
    }

    @Override
    public boolean update(FollowDTO follow){
        DynamoDbTable<FollowDTO> table = getEnhancedClient().table(TableName,TableSchema.fromBean(FollowDTO.class));
        Key key = Key.builder()
                .partitionValue(follow.getFollower_handle()).sortValue(follow.getFollowee_handle())
                .build();
        FollowDTO dbFollow = table.getItem(key);
        if(dbFollow != null){
            dbFollow.setFollowerName(follow.getFollowerName());
            dbFollow.setFolloweeName(follow.getFolloweeName());
            table.updateItem(dbFollow);
        }else{
            return false;
        }
        return true;
    }

    @Override
    public void delete(FollowDTO follow){
        DynamoDbTable<FollowDTO> table = getEnhancedClient().table(TableName,TableSchema.fromBean(FollowDTO.class));
        Key key = Key.builder()
                .partitionValue(follow.getFollower_handle()).sortValue(follow.getFollowee_handle())
                .build();
        table.deleteItem(key);
    }

    private DataPage<FollowDTO> getPageOfFollowees(String targetUserAlias, int pageSize, String lastUserAlias){
        DynamoDbTable<FollowDTO> table = getEnhancedClient().table(TableName, TableSchema.fromBean(FollowDTO.class));
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        System.out.println("paging size of following:"+ pageSize);
        if(isNonEmptyString(lastUserAlias)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeHandleAttr, AttributeValue.builder().s(lastUserAlias).build());
            startKey.put(FollowerHandleAttr, AttributeValue.builder().s(targetUserAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<FollowDTO> result = new DataPage<FollowDTO>();

        PageIterable<FollowDTO> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowDTO> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(follow -> result.getValues().add(follow));
                });

        return result;

    }




    public DataPage<FollowDTO> getPageOfFollowers(String targetUserAlias, int pageSize, String lastUserAlias){
        DynamoDbIndex<FollowDTO> index = getEnhancedClient().table(TableName, TableSchema.fromBean(FollowDTO.class)).index(IndexName);
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        System.out.println("Made request builder and key");
        if(isNonEmptyString(lastUserAlias)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeHandleAttr, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(FollowerHandleAttr, AttributeValue.builder().s(lastUserAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }


        QueryEnhancedRequest request = requestBuilder.build();
        System.out.println("built request");
        DataPage<FollowDTO> result = new DataPage<FollowDTO>();

        SdkIterable<Page<FollowDTO>> sdkIterable = index.query(request);
        PageIterable<FollowDTO> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowDTO> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(follow -> result.getValues().add(follow));
                });

        System.out.println("about to return result");
        return result;

    }


    @Override
    public Pair<List<FollowDTO>,Boolean> getFollowees(String targetUserAlias, int pageSize, String lastUserAlias) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert pageSize>0;
        assert targetUserAlias != null;

        DataPage<FollowDTO> followPage = getPageOfFollowees(targetUserAlias,pageSize,lastUserAlias);
        List<FollowDTO> followers = followPage.getValues();

        boolean hasMorePages = followPage.isHasMorePages();

        return new Pair<>(followers,hasMorePages);
    }


    @Override
    public Pair<List<FollowDTO>,Boolean> getFollowers(String targetUserAlias, int pageSize, String lastUserAlias){
        assert pageSize>0;
        assert targetUserAlias != null;

        System.out.println("About to get follow page");
        DataPage<FollowDTO> followPage = getPageOfFollowers(targetUserAlias,pageSize,lastUserAlias);
        System.out.println("got follow page");
        List<FollowDTO> followers = followPage.getValues();

        boolean hasMorePages = followPage.isHasMorePages();

        return new Pair<>(followers,hasMorePages);
    }


}
