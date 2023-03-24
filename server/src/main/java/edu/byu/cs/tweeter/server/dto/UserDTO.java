package edu.byu.cs.tweeter.server.dto;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class UserDTO {
    private String firstName;
    private String lastName;
    private String alias;
    private String imageUrl;

    private String password;

    public UserDTO(String firstName, String lastName, String alias, String imageUrl,String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.imageUrl = imageUrl;
        this.password = password;
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
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
}
