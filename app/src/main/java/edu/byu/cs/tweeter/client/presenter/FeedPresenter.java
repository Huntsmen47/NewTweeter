package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {




    public interface FeedView extends PagedView{

        void addItems(List<Status> statuses);

    }

    public FeedPresenter(FeedView view){
        super(view);
        userService = new UserService();
    }


    @Override
    public void serviceLoad(User user, int pageSize, Status lastItem) {
        userService.loadMoreItems(user,pageSize,lastItem,new PageClassObserver());
    }

    @Override
    public void addMoreItems(List<Status> items) {
        ((FeedView)view).addItems(items);
    }

}
