package edu.byu.cs.tweeter.server.dao.dao_interfaces;

import edu.byu.cs.tweeter.server.dao.DataAccessException;
import edu.byu.cs.tweeter.server.dao.dto.UserDTO;

public interface UserDAO extends StringPartitionDAO<UserDTO> {

    void updateUser (UserDTO user) throws DataAccessException;

    String getPassword(String userAlias) throws DataAccessException;


}
