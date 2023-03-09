package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public abstract class Presenter {

    protected static final String LOG_TAG = "Presenter";
    protected View view;
    protected UserService userService;
    protected StatusService statusService;
    protected FollowService followService;

    public Presenter(View view) {
        this.view = view;
    }

    public interface View{
        void displayMessage(String msg);
    }

    public class Observer implements ServiceObserver{

        @Override
        public void handleFailure(String message) {
            processFailure();
            view.displayMessage(message);
        }
    }

    public abstract void processFailure();

    protected UserService getUserService(){
        if (userService == null){
            userService = new UserService();
        }
        return userService;
    }

    protected StatusService getStatusService(){
        if(statusService == null){
            statusService = new StatusService();
        }
        return statusService;
    }

    protected FollowService getFollowService(){
        if (followService == null){
            followService = new FollowService();
        }
        return followService;
    }
}
