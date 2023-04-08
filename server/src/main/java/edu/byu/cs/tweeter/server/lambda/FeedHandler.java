package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.server.dao.ConcreteDaoFactory;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.DAOFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class FeedHandler implements RequestHandler<FeedRequest, FeedResponse> {

    @Override
    public FeedResponse handleRequest(FeedRequest input, Context context) {
        StatusService service = new StatusService();
        DAOFactory daoFactory = new ConcreteDaoFactory();
        return service.getFeed(input,daoFactory);
    }
}
