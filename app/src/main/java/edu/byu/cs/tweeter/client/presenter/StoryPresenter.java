package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status>{

    public interface StoryView extends PagedView{

        void addItems(List<Status> statuses);
    }

    public StoryPresenter(StoryView view){
        super(view);
        userService = new UserService();
        statusService = new StatusService();
    }

    @Override
    public void serviceLoad(User user, int pageSize, Status lastItem) {
        statusService.loadMoreItems(user,pageSize,lastItem, new PageClassObserver());
    }

    @Override
    public void addMoreItems(List<Status> items) {
        ((StoryView)view).addItems(items);
    }


}
