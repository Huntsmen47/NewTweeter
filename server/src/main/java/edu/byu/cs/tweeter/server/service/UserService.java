package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dto.UserDTO;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService {

    public LoginResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        User user = getDummyUser();
        AuthToken authToken = getDummyAuthToken();
        return new LoginResponse(user, authToken);
    }

    public RegisterResponse register(RegisterRequest request) {
        if (request.getUsername() == null) {
            throw new RuntimeException("[Bad Request] Missing username attribute");
        }
        if (request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing password attribute");
        }
        if (request.getFirstName() == null) {
            throw new RuntimeException("[Bad Request] Missing first name attribute");
        }
        if (request.getLastName() == null) {
            throw new RuntimeException("[Bad Request] Missing last name attribute");
        }
        if (request.getImage() == null) {
            throw new RuntimeException("[Bad Request] Missing image attribute");
        }

        // create UserDTO
        // put this in DataBase (in order to implement this part you need to apply abstract factory pattern)
        // Translate UserDTO to User model object
        // create authToken
        // create response that includes user and authToken and return it.

        UserDTO userDTO = new UserDTO(request.getFirstName(),request.getLastName(),
                request.getUsername(),request.getImage(),request.getPassword());
        User user = getDummyUser();
        AuthToken authToken = getDummyAuthToken();
        return new RegisterResponse(user, authToken);
    }

    public UserResponse getUser(UserRequest request){
        if(request.getTargetUserAlias() == null){
            throw new RuntimeException("[Bad Request] Missing user alias");
        }
        User user = getFakeData().findUserByAlias(request.getTargetUserAlias());
        return new UserResponse(user);
    }

    public LogoutResponse logout(LogoutRequest request){
        return new LogoutResponse();
    }

    /**
     * Returns the dummy user to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy user.
     *
     * @return a dummy user.
     */
    User getDummyUser() {
        return getFakeData().getFirstUser();
    }

    /**
     * Returns the dummy auth token to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy auth token.
     *
     * @return a dummy auth token.
     */
    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }

    boolean validatePassword(String userAlias,String password){
        return true;
    }
}
