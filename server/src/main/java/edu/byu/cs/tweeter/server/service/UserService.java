package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.ConcreteDaoFactory;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.DAOFactory;
import edu.byu.cs.tweeter.server.dao.DataAccessException;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.ImageDAO;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.UserDAO;
import edu.byu.cs.tweeter.server.dao.dto.UserDTO;
import edu.byu.cs.tweeter.util.FakeData;


public class UserService {

    private DAOFactory daoFactory;

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
        // possible unwanted dependency with ConcreteDaoFactory talk with TA
        daoFactory = new ConcreteDaoFactory();
        UserDAO userDAO = daoFactory.makeUserDao();
        ImageDAO imageDAO = daoFactory.makeImageDao();


        UserDTO userDTO = new UserDTO(request.getFirstName(),request.getLastName(),
                request.getUsername(), imageDAO.uploadImage(request.getImage(),request.getUsername()),
                request.getPassword());
        try{
            userDAO.addItem(userDTO,userDTO.getUserAlias());
        } catch (DataAccessException ex){
            System.out.println(ex.getMessage());
        }

        User user = convertUserDTO(userDTO);
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

    /**
     * Pre-Conditions
     * - userDTO cannot be null
     * - userDTO must have non null firstName, lastName, userAlias, imageUrl
     * Post-Conditions
     * - user returned with non null firstName, lastName, userAlias, imageUrl
     * @param userDTO
     * @return
     */
    public User convertUserDTO(UserDTO userDTO) {
        User user = new User(userDTO.getFirstName(),userDTO.getLastName(),
                userDTO.getUserAlias(),userDTO.getImageUrl());
        return user;
    }
}
