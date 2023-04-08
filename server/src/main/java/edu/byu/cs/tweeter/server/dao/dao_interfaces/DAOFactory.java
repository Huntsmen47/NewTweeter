package edu.byu.cs.tweeter.server.dao.dao_interfaces;

public interface DAOFactory {

    UserDAO makeUserDao();

    ImageDAO makeImageDao();

    AuthTokenDAO makeAuthTokenDao();

    FollowDAO makeFollowDAO();

    StoryDAO makeStoryDAO();

    FeedDAO makeFeedDAO();
}
