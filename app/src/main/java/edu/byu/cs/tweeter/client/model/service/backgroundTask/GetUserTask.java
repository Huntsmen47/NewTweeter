package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthenticatedTask {

    public static final String USER_KEY = "user";



    /**
     * Alias (or handle) for user whose profile is being retrieved.
     */
    private String alias;

    private User user;


    public GetUserTask(AuthToken authToken, String alias, Handler messageHandler) {
        super(messageHandler,authToken);
        this.alias = alias;

    }

    @Override
    protected Pair processTask() {
         try {
             UserRequest request = new UserRequest(alias,getAuthToken());
             UserResponse response = getServerFacade().getUser(request, UserService.USER_PATH);
             if(response.isSuccess()){
                 user = response.getUser();
                 setDateTime(response.getAuthToken().datetime);
                 return new Pair<Boolean,String>(true,"");
             }else{
                 return new Pair<Boolean,String>(false,response.getMessage());
             }
         }catch (Exception ex){
             Log.e("UserTask",ex.getMessage(),ex);
            throw new RuntimeException(ex.getMessage());
         }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, user);
    }




}
