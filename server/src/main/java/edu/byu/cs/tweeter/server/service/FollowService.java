package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.ConcreteFollowDAO;
import edu.byu.cs.tweeter.server.dao.DataAccessException;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.DAOFactory;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.UserDAO;
import edu.byu.cs.tweeter.server.dao.dto.FollowDTO;
import edu.byu.cs.tweeter.server.dao.dto.UserDTO;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link ConcreteFollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        return getFollowingDAO().getFollowees(request);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request){
        if(request.getAllegedFollowerAlias() == null){
            throw new RuntimeException("[Bad Request] Request needs to have an alleged Follower");
        }else if(request.getAllegedFolloweeAlias()==null){
            throw new RuntimeException("[Bad Request] Request needs to have an alleged followee");
        }

        return new IsFollowerResponse(true);
    }

    public FollowersResponse getFollowers(FollowersRequest request, DAOFactory daoFactory){
        if(request.getFolloweeAlias() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }else if(request.getLimit() <= 0){
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        FollowDAO followDAO = daoFactory.makeFollowDAO();
        UserDAO userDAO = daoFactory.makeUserDao();
        Pair<List<FollowDTO>,Boolean> data = followDAO.getFollowers(request.getFolloweeAlias(),
                request.getLimit(),request.getLastFollowerAlias());
        List<User> followers = new ArrayList<>();
        System.out.println(data.getFirst().size());
        for(FollowDTO follow:data.getFirst()){
            User user = null;
            try{
                user = convertUserDTO(userDAO.getItem(follow.getFollower_handle()));
            }catch (DataAccessException ex){
                System.out.println(ex.getMessage());
                throw new RuntimeException("[Bad Request]" + ex.getMessage());
            }

            System.out.println(user.getFirstName());
            followers.add(user);
        }
        return new FollowersResponse(followers,data.getSecond());
    }


    public FollowResponse follow(FollowRequest request) {
        String debugMessage = "Follow";
        String debug1 = String.format("FolloweeAlias: %s",request.getFolloweeAlias());
        String debug2 = String.format("FollowerAlias: %s",request.getFollowerAlias());
        System.out.println(debugMessage);
        System.out.println(debug1);
        System.out.println(debug2);
        if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Missing followee user alias attribute");
        }
        if (request.getFollowerAlias() == null) {
          //  throw new RuntimeException("[Bad Request] Missing follower user alias attribute");
        }

        return new FollowResponse();
    }

    public CountResponse getFollowerCount(FollowerCountRequest request){
        if(request.getTargetUserAlias() == null){
            throw new RuntimeException("[Bad Request] Missing target user alias attribute");
        }

        return new CountResponse(20);
    }

    public CountResponse getFollowingCount(FollowingCountRequest request){
        if(request.getTargetUserAlias() == null){
            throw new RuntimeException("[Bad Request] Missing target user alias attribute");
        }
        return new CountResponse(20);
    }

    public UnfollowResponse unfollow(UnfollowRequest request){
        String debugMessage = "Unfollow";
        String debug1 = String.format("FolloweeAlias: %s",request.getFolloweeAlias());
        String debug2 = String.format("FollowerAlias: %s",request.getFollowerAlias());
        System.out.println(debugMessage);
        System.out.println(debug1);
        System.out.println(debug2);
        if (request.getFolloweeAlias() == null){
            throw new RuntimeException("[Bad Request] Missing followee user alias");
        }
        if(request.getFollowerAlias() == null){
            throw new RuntimeException("[Bad Request] Missing follower user alias");
        }

        return new UnfollowResponse();
    }



    /**
     * Returns an instance of {@link ConcreteFollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    ConcreteFollowDAO getFollowingDAO() {
        return new ConcreteFollowDAO();
    }

    public User convertUserDTO(UserDTO userDTO) {
        User user = new User(userDTO.getFirstName(),userDTO.getLastName(),
                userDTO.getUserAlias(),userDTO.getImageUrl());
        return user;
    }
}
