package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.dao_interfaces.DAOFactory;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.UserDAO;
import edu.byu.cs.tweeter.server.dao.dto.FollowDTO;
import edu.byu.cs.tweeter.server.dao.dto.UserDTO;

public class Filler {

    // How many follower users to add
    // We recommend you test this with a smaller number first, to make sure it works for you
    private final static int NUM_USERS = 10000;

    // The alias of the user to be followed by each user created
    // This example code does not add the target user, that user must be added separately.
    private final static String FOLLOW_TARGET = "followed";

    public static void fillUserDatabase(DAOFactory daoFactory) {

        // Get instance of DAOs by way of the Abstract Factory Pattern
        UserDAO userDAO = daoFactory.makeUserDao();

        List<String> followers = new ArrayList<>();
        List<UserDTO> users = new ArrayList<>();

        // Iterate over the number of users you will create
        for (int i = 1; i <= NUM_USERS; i++) {

            String name = "Guy " + i;
            String alias = "guy" + i;

            // Note that in this example, a UserDTO only has a name and an alias.
            // The url for the profile image can be derived from the alias in this example
            UserDTO user = new UserDTO();
            user.setUserAlias(alias);
            user.setFirstName(name);
            users.add(user);

            // Note that in this example, to represent a follows relationship, only the aliases
            // of the two users are needed
            followers.add(alias);
        }

        // Call the DAOs for the database logic
        if (users.size() > 0) {
            userDAO.addUserBatch (users);
        }
        if (followers.size() > 0) {
            //followDAO.addFollowersBatch(followers, FOLLOW_TARGET);
        }
    }

    public static void fillFollowerDatabase(DAOFactory daoFactory) {

        // Get instance of DAOs by way of the Abstract Factory Pattern
        FollowDAO followDAO = daoFactory.makeFollowDAO();


        List<FollowDTO> follows = new ArrayList<>();

        // Iterate over the number of users you will create
        for (int i = 7301; i <= NUM_USERS; i++) {

            String followerAlias = "guy" + i;
            String followeeAlias = "@h";

            // Note that in this example, a UserDTO only has a name and an alias.
            // The url for the profile image can be derived from the alias in this example
            FollowDTO follow = new FollowDTO();
            follow.setFollower_handle(followerAlias);
            follow.setFollowee_handle(followeeAlias);
            follows.add(follow);
        }

        // Call the DAOs for the database logic
        if (follows.size() > 0) {
            followDAO.addFollowBatch (follows);
        }
    }

    public static void main(String[] args) {
        DAOFactory daoFactory = new ConcreteDaoFactory();

       // fillUserDatabase(daoFactory);
        fillFollowerDatabase(daoFactory);
    }

}

