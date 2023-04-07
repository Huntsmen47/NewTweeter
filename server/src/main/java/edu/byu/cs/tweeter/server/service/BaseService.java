package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dto.UserDTO;

public class BaseService {


    protected User convertUserDTO(UserDTO userDTO) {
        User user = new User(userDTO.getFirstName(),userDTO.getLastName(),
                userDTO.getUserAlias(),userDTO.getImageUrl());
        return user;
    }

    protected AuthToken authenticate(AuthToken authToken){
        long difference = System.currentTimeMillis() - authToken.datetime;
        if(difference > 60000){
            throw new RuntimeException("[Bad Request] Please login");
        }
        authToken.setDatetime(System.currentTimeMillis());
        return authToken;
    }
}
