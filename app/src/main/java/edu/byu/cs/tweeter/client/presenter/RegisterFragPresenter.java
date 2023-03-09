package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterFragPresenter extends AuthenticatePresenter{




    public interface RegisterView extends Presenter.View{

        void registerLogin(User registeredUser);
    }

    public RegisterFragPresenter(RegisterView view){
        super(view);
        userService = new UserService();
    }

    public void register(String firstName, String lastName, String alias,
                         String password, String imageBytesBase64) {
        
        userService.register(firstName,lastName,alias,password,imageBytesBase64,
                new RegisterFragObserver());
    }

    @Override
    public void validateRegister(String firstName, String lastName,String password, String alias,  boolean uploadable) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }

        if (uploadable) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
        validateLogin(alias,password);
    }
    
    public class RegisterFragObserver extends Observer implements AuthenticateObserver {

        @Override
        public void authenticate(User registeredUser) {
            ((RegisterView)view).registerLogin(registeredUser);
        }
    }

}
