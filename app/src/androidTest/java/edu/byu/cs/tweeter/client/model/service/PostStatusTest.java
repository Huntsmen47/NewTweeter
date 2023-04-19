package edu.byu.cs.tweeter.client.model.service;


import android.os.Looper;
import android.util.Log;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostStatusTest {
    private CountDownLatch countDownLatch;

    private MainPresenter mainPresenterSpy;

    private MockView myMockView;

    private ServerFacade serverFacade;

    private LoginRequest loginRequest;

    private LoginResponse loginResponse;



    private class MockView implements MainPresenter.MainView{

        @Override
        public void setVisibilityFollowButton(boolean value) {

        }

        @Override
        public void formatFollowButton(boolean isFollower) {

        }

        @Override
        public void setFollowerCount(int count) {

        }

        @Override
        public void setFollowingCount(int count) {

        }

        @Override
        public void setTheFollowButton(boolean b) {

        }

        @Override
        public void setEnabledFollowButton(boolean b) {

        }

        @Override
        public void logout() {

        }

        @Override
        public void logoutToast() {

        }

        @Override
        public void cancelPostToast() {

        }

        @Override
        public void postStatusToast() {

        }

        @Override
        public void displayMessage(String msg) {
            System.out.println(msg);
            countDownLatch.countDown();
        }
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    @BeforeAll
    public void setup() {
        serverFacade = new ServerFacade();
        loginRequest = new LoginRequest("@j","1");
        myMockView = Mockito.spy(new MockView());
        mainPresenterSpy = Mockito.spy(new MainPresenter(myMockView));
        try{
            loginResponse = serverFacade.login(loginRequest,"/login");
            Cache.getInstance().setCurrUser(loginResponse.getUser());
            Cache.getInstance().setCurrUserAuthToken(loginResponse.getAuthToken());
        }catch (IOException | TweeterRemoteException ex){
            Log.e("Exception",ex.getMessage());
            throw new RuntimeException("problem with logging in");
        }

        resetCountDownLatch();
    }

    @Test
    public void postStatus_valid_correctResponse() throws InterruptedException, TweeterRemoteException,IOException{
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                mainPresenterSpy.onStatusPosted("taco");
                Looper.loop();
            }
        }).start();

        awaitCountDownLatch();

        verify(myMockView,times(1)).displayMessage("Successfully Posted!");

        GetStoryRequest getStoryRequest = new GetStoryRequest("@j",Cache.getInstance().getCurrUserAuthToken(),1,null);
        GetStoryResponse response = serverFacade.getStory(getStoryRequest,"/get_story");
        Status post = response.getStory().get(0);
        Log.i("Testing",post.toString());
        assert post.post.equals("taco");
        assert post.user.equals(Cache.getInstance().getCurrUser());

    }

}
