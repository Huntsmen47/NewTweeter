package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;

public interface UserDAO {
    User getUser(String userAlias);
    void addUser(User user);
    void deleteUser(String userAlias);
    void updateUser (User user);
    DataPage<User> getPageOfUser(int pageSize,String lastUserAlias);
    boolean validatePassword(String userAlias,String password);
}
