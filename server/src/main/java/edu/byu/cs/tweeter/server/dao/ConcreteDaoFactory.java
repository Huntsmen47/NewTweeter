package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.dao_interfaces.DAOFactory;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.ImageDAO;
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
}
