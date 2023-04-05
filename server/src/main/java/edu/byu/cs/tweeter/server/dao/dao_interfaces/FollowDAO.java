package edu.byu.cs.tweeter.server.dao.dao_interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.DataPage;
import edu.byu.cs.tweeter.server.dao.dto.FollowDTO;
import edu.byu.cs.tweeter.util.Pair;

public interface FollowDAO {

    // this will be called when making someone a follower
    // for the first time.
    void putFollow(String follower_handle, String followerName,
                   String followee_handle, String followeeName);

    // this will be called when you look at a user's page
    // to determine what state the follow button should be in
    FollowDTO getFollow(String follower_handle, String followee_handle);

    // this updates who a follower is following
    // this will be called when you follow someone
    boolean update(FollowDTO follow);

    // this will help implement unfollow
    public void delete(FollowDTO follow);

    Pair<List<FollowDTO>,Boolean> getFollowers(String targetUserAlias, int pageSize, String lastUserAlias);

    Pair<List<FollowDTO>,Boolean> getFollowees(String targetUserAlias, int pageSize, String lastUserAlias);

    Integer getFolloweeCount(User follower);

    Integer getFollowerCount(User followee);

}
