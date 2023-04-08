package edu.byu.cs.tweeter.server.dao.dto;

import edu.byu.cs.tweeter.server.dao.ConcreteFollowDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
@DynamoDbBean
public class FollowDTO {
    private String follower_handle;
    private String followerName;
    private String followee_handle;
    private String followeeName;


    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = ConcreteFollowDAO.IndexName)
    public String getFollower_handle(){
        return follower_handle;
    }

    public void setFollower_handle(String followerHandle){
        this.follower_handle = followerHandle;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = ConcreteFollowDAO.IndexName)
    public String getFollowee_handle(){
        return followee_handle;
    }

    public void setFollowee_handle(String followee_handle){
        this.followee_handle = followee_handle;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    public String getFolloweeName() {
        return followeeName;
    }

    public void setFolloweeName(String followeeName) {
        this.followeeName = followeeName;
    }
}
