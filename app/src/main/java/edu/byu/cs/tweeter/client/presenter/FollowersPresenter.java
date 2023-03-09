package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.SendUserObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {





    public interface FollowerView extends PagedView {

        void addItems(List<User> followers);
    }

    public FollowersPresenter(FollowerView view){
        super(view);
        userService = new UserService();
        followService = new FollowService();
    }

    @Override
    public void serviceLoad(User user, int pageSize, User lastItem) {
        followService.loadMoreFollowers(user,pageSize,lastItem,new PageClassObserver());
    }

    @Override
    public void addMoreItems(List<User> items) {
        ((FollowerView)view).addItems(items);
    }


}
