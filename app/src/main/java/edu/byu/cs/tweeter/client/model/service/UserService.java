package edu.byu.cs.tweeter.client.model.service;


import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.handler.LoginHandler;
import edu.byu.cs.tweeter.client.model.service.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.model.service.handler.PageHandler;
import edu.byu.cs.tweeter.client.model.service.handler.RegisterHandler;
import edu.byu.cs.tweeter.client.model.service.handler.SendUserHandler;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SendUserObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleTaskObserver;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService extends BaseService {

    public static final String LOGIN_PATH = "/login";
    public static final String REGISTER_PATH = "/register";

    public static final String FOLLOW_PATH = "/follow";

    public static final String USER_PATH = "/user";

    public void getUser(String userAlias, SendUserObserver followingObserver) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new SendUserHandler(followingObserver));
        execute(getUserTask);
    }

    public void logout(SimpleTaskObserver mainObserver) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(),
                new LogoutHandler(mainObserver));
        execute(logoutTask);

    }

    public void login(String alias, String password, AuthenticateObserver loginObserver) {
        LoginTask loginTask = new LoginTask(alias,
                password,
                new LoginHandler(loginObserver));
        execute(loginTask);
    }

    public void register(String firstName, String lastName, String alias,
                         String password, String imageBytesBase64, AuthenticateObserver authenticateObserver) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new RegisterHandler(authenticateObserver));
        execute(registerTask);
    }



    public void loadMoreItems(User user, int pageSize, Status lastStatus, PagedPresenter.
            PageClassObserver feedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new PageHandler<Status>(feedObserver));
        execute(getFeedTask);
    }

}
