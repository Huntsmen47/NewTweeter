package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.StatusDao;

public class StatusService {

    public PostStatusResponse postStatus(PostStatusRequest request){
        if(request.getStatus() == null){
            throw new RuntimeException("[Bad Request] Missing status");
        }
        return new PostStatusResponse();
    }
    
    public GetStoryResponse getStory(GetStoryRequest request){
        if(request.getTargetUserAlias() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a target user alias");
        }else if(request.getLimit() <= 0){
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        return getStatusDAO().getStory(request);
    }

    StatusDao getStatusDAO() {
        return new StatusDao();
    }

}
