package edu.byu.cs.tweeter.server.dao.dto;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FeedDTO {

    private String userAlias;

    private String followerAlias;

    private Long timeStamp;

    private List<String> urls;

    private List<String> mentions;

    private String post;

    public FeedDTO(){

    }

    public FeedDTO(String userAlias, String followerAlias, Long timeStamp, List<String> urls, List<String> mentions, String post) {
        this.userAlias = userAlias;
        this.followerAlias = followerAlias;
        this.timeStamp = timeStamp;
        this.urls = urls;
        this.mentions = mentions;
        this.post = post;
    }

    @DynamoDbPartitionKey
    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getFollowerAlias() {
        return followerAlias;
    }

    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }

    @DynamoDbSortKey()
    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}