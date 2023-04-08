package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.dao_interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.DAOFactory;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.FeedDAO;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.ImageDAO;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.StoryDAO;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.UserDAO;

public class ConcreteDaoFactory implements DAOFactory {
    @Override
    public UserDAO makeUserDao() {
        return new ConcreteUserDAO();
    }

    @Override
    public ImageDAO makeImageDao() {
        return new ConcreteImageDao();
    }

    @Override
    public AuthTokenDAO makeAuthTokenDao() {
        return new ConcreteAuthTokenDAO();
    }

    @Override
    public FollowDAO makeFollowDAO() {
        return new ConcreteFollowDAO();
    }

    @Override
    public StoryDAO makeStoryDAO() {
        return new ConcreteStoryDAO();
    }

    @Override
    public FeedDAO makeFeedDAO() {
        return new ConcreteFeedDAO();
    }
}
