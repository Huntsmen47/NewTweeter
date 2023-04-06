package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.DataAccessException;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.DAOFactory;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.FeedDAO;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.StoryDAO;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.UserDAO;
import edu.byu.cs.tweeter.server.dao.dto.StoryDTO;
import edu.byu.cs.tweeter.server.dao.dto.UserDTO;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService {

    public PostStatusResponse postStatus(PostStatusRequest request, DAOFactory daoFactory){
        if(request.getStatus() == null){
            throw new RuntimeException("[Bad Request] Missing status");
        }
        AuthToken authToken =  authenticate(request.getAuthToken());
        StoryDAO storyDAO = daoFactory.makeStoryDAO();
        StoryDTO storyDTO = convertStatus(request.getStatus());
        storyDAO.postStatus(storyDTO);

        return new PostStatusResponse(authToken);
    }
    
    public GetStoryResponse getStory(GetStoryRequest request, DAOFactory daoFactory){
        if(request.getTargetUserAlias() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a target user alias");
        }else if(request.getLimit() <= 0){
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        AuthToken authToken =  authenticate(request.getAuthToken());
        StoryDAO storyDAO = daoFactory.makeStoryDAO();
        Pair<List<StoryDTO>,Boolean> data = storyDAO.getStory(request.getLimit(),
                request.getTargetUserAlias(),convertStatus(request.getLastStatus()));
        List<Status> statuses = new ArrayList<>();

        for(StoryDTO storyDTO:data.getFirst()){
            statuses.add(convertStoryDTO(storyDTO,daoFactory));
        }

        return new GetStoryResponse(statuses,data.getSecond(),authToken);
    }

    public FeedResponse getFeed(FeedRequest request, DAOFactory daoFactory){
        if(request.getTargetUserAlias() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a target user alias");
        }else if(request.getLimit() <= 0){
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        AuthToken authToken =  authenticate(request.getAuthToken());
        FeedDAO feedDAO = daoFactory.makeFeedDAO();
        Pair<List<Status>,Boolean> data = feedDAO.getFeed(request.getLimit(),
                request.getTargetUserAlias(),request.getLastStatus());
        return new FeedResponse(data.getSecond(),data.getFirst(),authToken);
    }


    private AuthToken authenticate(AuthToken authToken){
        long difference = System.currentTimeMillis() - authToken.datetime;
        if(difference > 60000){
            throw new RuntimeException("[Bad Request] Please login");
        }
        authToken.setDatetime(System.currentTimeMillis());
        return authToken;
    }

    public Status convertStoryDTO(StoryDTO storyDTO,DAOFactory daoFactory) {
        UserDAO userDAO = daoFactory.makeUserDao();
        User user = null;
        try{
            UserDTO userDTO = userDAO.getItem(storyDTO.getUserAlias());
            user = convertUserDTO(userDTO);
        }catch (DataAccessException ex){
            System.out.println(ex.getMessage());
            throw new RuntimeException("[Bad Request] Cannot get story");
        }

        Status status = new Status(storyDTO.getPost(),user,
                storyDTO.getTimeStamp(),storyDTO.getUrls(),storyDTO.getMentions());
        return status;
    }

    public StoryDTO convertStatus(Status status){
        if(status == null){
            return null;
        }
        StoryDTO storyDTO = new StoryDTO(status.user.getAlias(),
                status.timestamp,status.urls,status.mentions,status.post);
        return  storyDTO;
    }

    public User convertUserDTO(UserDTO userDTO) {
        User user = new User(userDTO.getFirstName(),userDTO.getLastName(),
                userDTO.getUserAlias(),userDTO.getImageUrl());
        return user;
    }
}
