package edu.byu.cs.tweeter.server.dao;

public class ConcreteDaoFactory implements DAOFactory{
    @Override
    public UserDAO makeUserDao() {
        return new ConcreteUserDAO();
    }
}
