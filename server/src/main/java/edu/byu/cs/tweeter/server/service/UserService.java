package edu.byu.cs.tweeter.server.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.ConcreteDaoFactory;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.DAOFactory;
import edu.byu.cs.tweeter.server.dao.DataAccessException;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.ImageDAO;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.UserDAO;
import edu.byu.cs.tweeter.server.dao.dto.AuthTokenDTO;
import edu.byu.cs.tweeter.server.dao.dto.UserDTO;
import edu.byu.cs.tweeter.util.FakeData;



public class UserService {

    private DAOFactory daoFactory;


    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    private static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }


    public LoginResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        // check if user name exists
            // if not state that password is incorrect
        // if so match given password with actual password
            // if match log userIn with authtoken
        // if not declare that password is incorrect

        daoFactory = createDAOFactory();
        UserDAO userDAO = daoFactory.makeUserDao();
        if(userDAO.isInDatabase(request.getUsername()));

        // TODO: Generates dummy data. Replace with a real implementation.
        User user = getDummyUser();
        AuthToken authToken = new AuthToken(generateNewToken(),System.currentTimeMillis());
        // put authToken in database when login is ready
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


        daoFactory = createDAOFactory();
        UserDAO userDAO = daoFactory.makeUserDao();
        ImageDAO imageDAO = daoFactory.makeImageDao();
        AuthTokenDAO authTokenDAO = daoFactory.makeAuthTokenDao();

        String hashedPassword = hashPassword(request.getPassword());

        UserDTO userDTO = new UserDTO(request.getFirstName(),request.getLastName(),
                request.getUsername(), imageDAO.uploadImage(request.getImage(),request.getUsername()),
                hashedPassword);
        User user = convertUserDTO(userDTO);
        AuthToken authToken = new AuthToken(generateNewToken(),System.currentTimeMillis());
        try{
            userDAO.addItem(userDTO,userDTO.getUserAlias());
            AuthTokenDTO authTokenDTO = new AuthTokenDTO(request.getUsername(),authToken.token
                    ,authToken.datetime);
            authTokenDAO.addItem(authTokenDTO,authTokenDTO.getUserAlias());
        } catch (DataAccessException ex){
            throw new RuntimeException(ex.getMessage());
        }

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

    private static String hashPassword(String passwordToHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "FAILED TO HASH";
    }

    private DAOFactory createDAOFactory(){
        if(daoFactory == null){
            // is this a bad dependency vvv
            daoFactory = new ConcreteDaoFactory();
        }
        return daoFactory;
    }
}
