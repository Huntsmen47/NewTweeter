package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.FeedDAO;
import edu.byu.cs.tweeter.server.dao.dto.FeedDTO;
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

public class ConcreteFeedDAO implements FeedDAO {

    private static final String TableName = "Feed";

    private static final String UserAliasAttr = "userAlias";
    private static final String TimeStampAttr = "timeStamp";

    private static DynamoDbClient dynamoDbClient;
    private static DynamoDbEnhancedClient enhancedClient;
@Override
    public Pair<List<FeedDTO>, Boolean> getFeed(int pageLimit, String targetUser, FeedDTO lastStatus) {
        assert pageLimit > 0;
        assert targetUser != null;

        DataPage<FeedDTO> feedPage = getPageOfStatus(targetUser,pageLimit,lastStatus);
        List<FeedDTO> story = feedPage.getValues();
        System.out.println("Size of feedDTO data that gets returned:" + story.size());
        boolean hasMorePages = feedPage.isHasMorePages();

        return new Pair<>(story,hasMorePages);

    }

    @Override
    public void addStatus(FeedDTO feedDTO) {
        DynamoDbTable<FeedDTO> table = getEnhancedClient().table(TableName,
                TableSchema.fromBean(FeedDTO.class));
        Key key = Key.builder()
                .partitionValue(feedDTO.getUserAlias()).sortValue(feedDTO.getTimeStamp())
                .build();

        FeedDTO status = table.getItem(key);
        if(status != null){
            table.updateItem(feedDTO);
        }else{
            table.putItem(feedDTO);
            System.out.println("Status was added to feed");
        }
    }



    private DataPage<FeedDTO> getPageOfStatus(String targetUserAlias, int pageSize, FeedDTO lastStatus){
        System.out.println(targetUserAlias);
        System.out.println(pageSize);

        DynamoDbTable<FeedDTO> table = getEnhancedClient().table(TableName, TableSchema.fromBean(FeedDTO.class));
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize).scanIndexForward(false);

        System.out.println("paging size of Feed:"+ pageSize);
        if(lastStatus != null) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(UserAliasAttr, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(TimeStampAttr, AttributeValue.builder().s(Long.toString(lastStatus.getTimeStamp())).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<FeedDTO> result = new DataPage<FeedDTO>();

        PageIterable<FeedDTO> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<FeedDTO> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(status -> result.getValues().add(status));
                });
        return result;
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
