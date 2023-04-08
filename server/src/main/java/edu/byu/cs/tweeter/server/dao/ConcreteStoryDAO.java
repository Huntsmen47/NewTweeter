package edu.byu.cs.tweeter.server.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.server.dao.dao_interfaces.StoryDAO;
import edu.byu.cs.tweeter.server.dao.dto.StoryDTO;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class ConcreteStoryDAO extends PagingDAO<StoryDTO> implements StoryDAO {

    private static final String TableName = "Story";

    private static final String UserAliasAttr = "userAlias";
    private static final String TimeStampAttr = "timeStamp";


    @Override
    public Pair<List<StoryDTO>,Boolean> getStory(int pageLimit,String targetUser,StoryDTO lastStatus) {
        assert pageLimit > 0;
        assert targetUser != null;

          DataPage<StoryDTO> storyPage = getPageOfItem(targetUser,pageLimit,lastStatus);
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


    @Override
    protected DynamoDbTable<StoryDTO> getTable() {
        return getEnhancedClient().table(TableName, TableSchema.fromBean(StoryDTO.class));
    }

    @Override
    protected void checkForPaging(StoryDTO lastItem, String targetUserAlias, QueryEnhancedRequest.Builder requestBuilder) {
        if(lastItem != null) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(UserAliasAttr, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(TimeStampAttr, AttributeValue.builder().s(Long.toString(lastItem.getTimeStamp())).build());

            requestBuilder.exclusiveStartKey(startKey);
        }
    }
}
