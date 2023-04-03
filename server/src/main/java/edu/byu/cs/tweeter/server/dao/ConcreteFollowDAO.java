package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
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
public class ConcreteFollowDAO implements FollowDAO {


    private static final String TableName = "Follows";
    public static final String IndexName = "follows_index";

    private static final String FollowerHandleAttr = "follower_handle";
    private static final String FolloweeHandleAttr = "followee_handle";


    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_1)
            .build();
    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    private static boolean isNonEmptyString(String value){
        return (value != null && value.length()>0);
    }

    @Override
    public void putFollow(String follower_handle, String followerName,
                          String followee_handle, String followeeName){
        DynamoDbTable<FollowDTO> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowDTO.class));
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
        DynamoDbTable<FollowDTO> table = enhancedClient.table(TableName,TableSchema.fromBean(FollowDTO.class));
        Key key = Key.builder()
                .partitionValue(follower_handle).sortValue(followee_handle)
                .build();
        FollowDTO follow = table.getItem(key);
        return follow;
    }

    @Override
    public boolean update(FollowDTO follow){
        DynamoDbTable<FollowDTO> table = enhancedClient.table(TableName,TableSchema.fromBean(FollowDTO.class));
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
        DynamoDbTable<FollowDTO> table = enhancedClient.table(TableName,TableSchema.fromBean(FollowDTO.class));
        Key key = Key.builder()
                .partitionValue(follow.getFollower_handle()).sortValue(follow.getFollowee_handle())
                .build();
        table.deleteItem(key);
    }

    @Override
    public DataPage<FollowDTO> getPageOfFollowees(String targetUserAlias, int pageSize, String lastUserAlias){
        DynamoDbTable<FollowDTO> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowDTO.class));
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);


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
        DynamoDbIndex<FollowDTO> index = enhancedClient.table(TableName, TableSchema.fromBean(FollowDTO.class)).index(IndexName);
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(isNonEmptyString(lastUserAlias)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeHandleAttr, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(FollowerHandleAttr, AttributeValue.builder().s(lastUserAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<FollowDTO> result = new DataPage<FollowDTO>();

        SdkIterable<Page<FollowDTO>> sdkIterable = index.query(request);
        PageIterable<FollowDTO> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowDTO> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(follow -> result.getValues().add(follow));
                });

        return result;

    }





    /**
     * Gets the count of users from the database that the user specified is following. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param follower the User whose count of how many following is desired.
     * @return said count.
     */
    @Override
    public Integer getFolloweeCount(User follower) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert follower != null;
        return getDummyFollowees().size();
    }

    @Override
    public Integer getFollowerCount(User followee) {
        return null;
    }

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getFollowerAlias() != null;

        List<User> allFollowees = getDummyFollowees();
        List<User> responseFollowees = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowees != null) {
                int followeesIndex = getFolloweesStartingIndex(request.getLastFolloweeAlias(), allFollowees);

                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
        }

        return new FollowingResponse(responseFollowees, hasMorePages);
    }


    @Override
    public Pair<List<FollowDTO>,Boolean> getFollowers(String targetUserAlias, int pageSize, String lastUserAlias){
        assert pageSize>0;
        assert targetUserAlias != null;

        DataPage<FollowDTO> followPage = getPageOfFollowers(targetUserAlias,pageSize,lastUserAlias);
        List<FollowDTO> followers = followPage.getValues();

        boolean hasMorePages = followPage.isHasMorePages();

        return new Pair<>(followers,hasMorePages);
    }

    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFolloweeAlias the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allFollowees the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFolloweesStartingIndex(String lastFolloweeAlias, List<User> allFollowees) {

        int followeesIndex = 0;

        if(lastFolloweeAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFolloweeAlias.equals(allFollowees.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }

        return followeesIndex;
    }


    private int getFollowersStartingIndex(String lastFollowerAlias, List<User> allFollowers){
        int followersIndex = 0;
        if(lastFollowerAlias != null){
            for (int i = 0; i < allFollowers.size(); i++) {
                if(lastFollowerAlias.equals(allFollowers.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followersIndex = i + 1;
                    break;
                }
            }
        }
        return followersIndex;
    }

    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the followees.
     */
    List<User> getDummyFollowees() {
        return getFakeData().getFakeUsers();
    }

    List<User> getDummyFollowers(){return getFakeData().getFakeUsers();}

    /**
     * Returns the {@link FakeData} object used to generate dummy followees.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
