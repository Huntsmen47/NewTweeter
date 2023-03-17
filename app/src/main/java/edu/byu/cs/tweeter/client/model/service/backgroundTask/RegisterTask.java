package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticationResponse;

/**
 * Background task that creates a new user account and logs in the new user (i.e., starts a session).
 */
public class RegisterTask extends AuthenticationTask {

    /**
     * The user's first name.
     */
    private String firstName;
    /**
     * The user's last name.
     */
    private String lastName;
    /**
     * The base-64 encoded bytes of the user's profile image.
     */
    private String image;


    public

    RegisterTask(String firstName, String lastName, String username, String password,
                        String image, Handler messageHandler) {
        super(messageHandler,username,password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
    }

    @Override
    public AuthenticationResponse doAuthentication() throws Exception {
        RegisterRequest request = new RegisterRequest(getUsername(),getPassword(),
                firstName,lastName,image);
        return getServerFacade().register(request, UserService.REGISTER_PATH);
    }
}
