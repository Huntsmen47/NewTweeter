package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.server.dao.ConcreteDaoFactory;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.DAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class FollowingCountHandler implements RequestHandler<FollowingCountRequest, CountResponse> {
    @Override
    public CountResponse handleRequest(FollowingCountRequest input, Context context) {
        FollowService service = new FollowService();
        DAOFactory daoFactory = new ConcreteDaoFactory();
        return service.getFollowingCount(input,daoFactory);
    }
}
