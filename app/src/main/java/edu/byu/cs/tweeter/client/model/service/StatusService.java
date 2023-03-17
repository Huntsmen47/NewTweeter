package edu.byu.cs.tweeter.client.model.service;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.handler.PageHandler;
import edu.byu.cs.tweeter.client.model.service.handler.PostStatusHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PageObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleTaskObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends BaseService {

    public static final String POST_STATUS_PATH = "/post_status";

    public void makePost(String post, SimpleTaskObserver mainObserver,
                         List<String> parsedUrls,List<String> parsedMentions) {
        Status newStatus = new Status(post, Cache.getInstance().getCurrUser(),
                System.currentTimeMillis(), parsedUrls, parsedMentions);
        PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
                newStatus, new PostStatusHandler(mainObserver));
        execute(statusTask);
    }

    public void loadMoreItems(User user, int pageSize, Status lastStatus, PageObserver observer) {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new PageHandler<Status>(observer));
        execute(getStoryTask);
    }

}
