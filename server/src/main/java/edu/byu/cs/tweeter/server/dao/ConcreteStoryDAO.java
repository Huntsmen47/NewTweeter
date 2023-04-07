package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.StoryDAO;
import edu.byu.cs.tweeter.server.dao.dto.FollowDTO;
import edu.byu.cs.tweeter.server.dao.dto.StoryDTO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
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

public class ConcreteStoryDAO implements StoryDAO {

    private static final String TableName = "Story";

    private static final String UserAliasAttr = "userAlias";
    private static final String TimeStampAttr = "timeStamp";

    private static DynamoDbClient dynamoDbClient;
    private static DynamoDbEnhancedClient enhancedClient;

    @Override
    public Pair<List<StoryDTO>,Boolean> getStory(int pageLimit,String targetUser,StoryDTO lastStatus) {
        assert pageLimit > 0;
        assert targetUser != null;

          DataPage<StoryDTO> storyPage = getPageOfStatus(targetUser,pageLimit,lastStatus);
          List<StoryDTO> story = storyPage.getValues();
          System.out.println("Size of StoryDTO data that gets returned:" + story.size());
          boolean hasMorePages = storyPage.isHasMorePages();

          return new Pair<>(story,hasMorePages);

    }

    @Override
    public void postStatus(StoryDTO status) {
        DynamoDbTable<StoryDTO> table = getEnhancedClient().table(TableName,
                TableSchema.fromBean(StoryDTO.class));
        Key key = Key.builder()
                .partitionValue(status.getUserAlias()).sortValue(status.getTimeStamp())
                .build();

        StoryDTO storyDTO = table.getItem(key);
        if(storyDTO != null){
            table.updateItem(status);
        }else{
            table.putItem(status);
        }
    }
    private DataPage<StoryDTO> getPageOfStatus(String targetUserAlias, int pageSize, StoryDTO lastStatus){
        System.out.println(targetUserAlias);
        System.out.println(pageSize);

        DynamoDbTable<StoryDTO> table = getEnhancedClient().table(TableName, TableSchema.fromBean(StoryDTO.class));
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize).scanIndexForward(false);

        System.out.println("paging size of Story:"+ pageSize);
        if(lastStatus != null) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(UserAliasAttr, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(TimeStampAttr, AttributeValue.builder().s(Long.toString(lastStatus.getTimeStamp())).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<StoryDTO> result = new DataPage<StoryDTO>();

        PageIterable<StoryDTO> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<StoryDTO> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(status -> result.getValues().add(status));
                });
        return result;
    }

    @Override
    public boolean update(StoryDTO status) {
        DynamoDbTable<StoryDTO> table = getEnhancedClient().table(TableName,TableSchema.fromBean(StoryDTO.class));
        Key key = Key.builder()
                .partitionValue(status.getUserAlias()).sortValue(status.getTimeStamp())
                .build();
        StoryDTO storyDTO = table.getItem(key);
        if(storyDTO != null){
            table.updateItem(status);
        }else{
            return false;
        }
        return true;
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





}
