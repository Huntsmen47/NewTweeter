package edu.byu.cs.tweeter.server.dao.dto;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class AuthTokenDTO {



    private String userAlias;

    public String token;
    /**
     * Long representation of date/time at which the auth token was created.
     */
    public Long datetime;

    public AuthTokenDTO(){

    }
    @DynamoDbPartitionKey
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

    public AuthTokenDTO(String userAlias, String token, Long datetime) {
        this.userAlias = userAlias;
        this.token = token;
        this.datetime = datetime;
    }


    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }


}
