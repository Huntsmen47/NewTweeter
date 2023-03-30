package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.dto.UserDTO;

public interface UserDAO {
    UserDTO getUser(String userAlias) throws DataAccessException;
    void addUser(UserDTO user) throws DataAccessException;
    void deleteUser(String userAlias) throws DataAccessException;
    void updateUser (UserDTO user) throws DataAccessException;

    String getPassword(String userAlias) throws DataAccessException;
}
