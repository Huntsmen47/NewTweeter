package edu.byu.cs.tweeter.client.model.service;

import android.util.Log;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientServerTests {

    private RegisterRequest registerRequest;
    private FollowersRequest followersRequest;
    private FollowerCountRequest followerCountRequest;

    private ServerFacade serverFacade;


    @BeforeAll
    public void setup(){
        registerRequest = new RegisterRequest("@allen","homesDog45","Allen","Anderson","someImage");
        followersRequest = new FollowersRequest(null,"@allen",5,"@daisy");
        followerCountRequest = new FollowerCountRequest(null,"@allen");
        serverFacade = new ServerFacade();
    }

    @Test
    public void register_valid_userMatch(){
        try{
            RegisterResponse response = serverFacade.register(registerRequest,"/register");
           assert response.isSuccess();
           User user = response.getUser();
           assert registerRequest.getUsername().equals(user.getAlias());
           assert registerRequest.getFirstName().equals(user.getFirstName());
           assert registerRequest.getLastName().equals(user.getLastName());
        }catch (Exception ex){
           Log.e("RegisterTest","Exception thrown during test");
           Log.e("RegisterTest",ex.getMessage());
           assert false;
        }

    }


    @Test
    public void getFollowers_valid_sizeMatch(){
        try{
            FollowersResponse response = serverFacade.getFollowers(followersRequest,"/getfollowers");
            assert response.isSuccess();
            assert followersRequest.getLimit() == response.getFollowers().size();

        }catch (Exception ex){
            Log.e("GetFollowersTest","Exception thrown during test");
            Log.e("GetFollowersTest",ex.getMessage());
            assert false;
        }
    }


    @Test
    public void followerCount_valid_shouldBeTwenty(){
        try {
            CountResponse response = serverFacade.getFollowerCount(followerCountRequest,"/get_follower_count");
            assert response.isSuccess();
            assert response.getCount() == 20;

        }catch (Exception ex){
            Log.e("GetFollowerCountTest","Exception thrown during test");
            Log.e("GetFollowerCountTest",ex.getMessage());
            assert false;
        }
    }




}
