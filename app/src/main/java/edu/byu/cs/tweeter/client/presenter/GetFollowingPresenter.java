package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.SendUserObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingPresenter extends PagedPresenter<User> {



    public interface GetFollowingView extends PagedView {
        void addItems(List<User> followees);
    }



    public GetFollowingPresenter(GetFollowingView view){
        super(view);
        followService = new FollowService();
        userService = new UserService();
    }

    @Override
    public void serviceLoad(User user, int pageSize, User lastItem) {
        followService.loadMoreFollowees(user, pageSize, lastItem,new PageClassObserver());
    }

    @Override
    public void addMoreItems(List<User> items) {
        ((GetFollowingView)view).addItems(items);
    }

}
