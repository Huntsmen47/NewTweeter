package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.server.dao.ConcreteDaoFactory;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.DAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;
import edu.byu.cs.tweeter.server.service.UserService;

public class FollowHandler implements RequestHandler<FollowRequest, FollowResponse> {

    @Override
    public FollowResponse handleRequest(FollowRequest input, Context context) {
        FollowService service = new FollowService();
        DAOFactory daoFactory = new ConcreteDaoFactory();
        return service.follow(input,daoFactory);
    }
}
