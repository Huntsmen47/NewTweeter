package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.server.dao.ConcreteDaoFactory;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.DAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class FollowerCountHandler implements RequestHandler<FollowerCountRequest, CountResponse> {
    @Override
    public CountResponse handleRequest(FollowerCountRequest input, Context context) {
        FollowService followService = new FollowService();
        DAOFactory daoFactory = new ConcreteDaoFactory();
        return followService.getFollowerCount(input,daoFactory);
    }
}
