package edu.byu.cs.tweeter.server.dao.dto;
import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class UserDTO {
    private String firstName;
    private String lastName;
    private String userAlias;
    private String imageUrl;

    private String password;

    private int followerCount;

    private int followeeCount;

    public UserDTO(){

    }

    public UserDTO(User user){
        this.userAlias = user.getAlias();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.imageUrl = user.getImageUrl();
    }

    public UserDTO(String firstName, String lastName, String userAlias,
                   String imageUrl, String password, int followerCount,int followeeCount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userAlias = userAlias;
        this.imageUrl = imageUrl;
        this.password = password;
        this.followerCount = followerCount;
        this.followeeCount = followeeCount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @DynamoDbPartitionKey
    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getFolloweeCount() {
        return followeeCount;
    }

    public void setFolloweeCount(int followeeCount) {
        this.followeeCount = followeeCount;
    }
}
