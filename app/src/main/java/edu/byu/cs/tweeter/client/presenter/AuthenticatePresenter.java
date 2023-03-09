package edu.byu.cs.tweeter.client.presenter;

public abstract class AuthenticatePresenter extends Presenter{


    public AuthenticatePresenter(View view){
        super(view);
    }
    public void validateLogin(String alias, String password) {
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public abstract void validateRegister(String firstName, String lastName,String password, String alias, boolean uploadable);

    @Override
    public void processFailure() {

    }
}
