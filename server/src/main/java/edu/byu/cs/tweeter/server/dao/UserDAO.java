package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dto.UserDTO;

public interface UserDAO {
    User getUser(String userAlias) throws DataAccessException;
    void addUser(UserDTO user) throws DataAccessException;
    void deleteUser(String userAlias) throws DataAccessException;
    void updateUser (UserDTO user);
    DataPage<User> getPageOfUsers(int pageSize,String lastUserAlias);

    String getPassword(String userAlias);
}
