package edu.byu.cs.tweeter.server.dao.dao_interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.dto.FeedDTO;
import edu.byu.cs.tweeter.util.Pair;

public interface FeedDAO {

    Pair<List<FeedDTO>, Boolean> getFeed(int pageLimit, String targetUser, FeedDTO lastStatus);

    void addStatus(FeedDTO feedDTO);



}
