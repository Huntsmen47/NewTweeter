package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginFragPresenter extends AuthenticatePresenter{

    public interface LoginView extends Presenter.View {

        void showLogin(User loggedInUser);
    }



    public LoginFragPresenter(LoginView view){
        super(view);
        userService = new UserService();
    }



    public void login(String alias, String password) {
        userService.login(alias,password,new LoginFragObserver());

    }

    public class LoginFragObserver extends Observer implements AuthenticateObserver {

        @Override
        public void authenticate(User user) {
            ((LoginView)view).showLogin(user);
        }
    }

    @Override
    public void validateRegister(String firstName, String lastName, String alias, String password, boolean uploadable) {

    }
}
