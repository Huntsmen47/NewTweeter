package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.ConcreteUserDAO;
import edu.byu.cs.tweeter.server.dao.DataAccessException;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.dto.UserDTO;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DaoTests {

    private UserDAO userDAO;

    @BeforeAll
    public void setup(){
        userDAO = new ConcreteUserDAO();
        UserDTO user = new UserDTO("taco","bell","@taco","someUrl","somePassword");
        try {
            userDAO.addUser(user);
        }catch (Exception ex){
            System.out.println("Problem setting up tests");
        }

    }

    @AfterAll
    public void takeDown(){
        try {
            userDAO.deleteUser("@taco");
            userDAO.deleteUser("asdf");
        }catch (Exception ex){
            System.out.println("Problem taking down tests");
        }

    }


    @Test
    public void getUser_aliasNull_dataException(){
           DataAccessException exception = Assertions.assertThrows(DataAccessException.class,()->{
                userDAO.getUser(null);
            });

           Assertions.assertEquals("User alias is null",exception.getMessage());
    }

    @Test
    public void getUser_invalidAlias_dataException(){
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class,()->{
            userDAO.getUser("@loco");
        });

        Assertions.assertEquals("Requested User Alias does not exist in Database",exception.getMessage());

    }

    @Test
    public void getUser_validAlias_noExceptionMatchAlias(){
       try {
           User user = userDAO.getUser("@taco");
           Assertions.assertEquals("@taco",user.getAlias());
       } catch (DataAccessException ex){
           assert false;
       }

    }

    @Test
    public void addUser_NullUser_DataAccessException(){
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class,()->{
            userDAO.addUser(null);
        });

        Assertions.assertEquals("Cannot add null user",exception.getMessage());
    }

    @Test
    public void addUser_userAliasNull_DataAccessException(){
        UserDTO user = new UserDTO("hunter","larson",null,"someUrl","somePassword");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class,()->{
            userDAO.addUser(user);
        });

        Assertions.assertEquals("userAlias is null, cannot add",exception.getMessage());

    }

    @Test
    public void addUser_userAlreadyInDatabase_DataAccessException(){
        UserDTO user = new UserDTO("hunter","larson","asdf","someUrl","somePassword");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class,()->{
            userDAO.addUser(user);
        });

        Assertions.assertEquals("user is already in database",exception.getMessage());

    }

    @Test
    public void addValidUser_userIsNotInDatabase_successfulAdd(){
        UserDTO user = new UserDTO("hunter","larson","asdf","someUrl","somePassword");
        try{
            userDAO.addUser(user);
            User userFromDatabase = userDAO.getUser("asdf");
            Assertions.assertEquals(user.getUserAlias(),userFromDatabase.getAlias());
            Assertions.assertEquals(user.getFirstName(),userFromDatabase.getFirstName());
            Assertions.assertEquals(user.getLastName(),userFromDatabase.getLastName());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            assert false;
        }
    }

    @Test
    public void deleteUser_userIsNotInDatabase_DataAccessException(){
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class,()->{
            userDAO.deleteUser("someUser");
        });

        Assertions.assertEquals("Requested User Alias does not exist in Database",exception.getMessage());
    }

    @Test
    public void deleteUser_userIsInDatabase_deleteSuccessful(){
        try{
            UserDTO user = new UserDTO("billy","joe","@billy","someUrl","somePassword");
            userDAO.addUser(user);
            userDAO.deleteUser("@billy");
            userDAO.getUser("@billy");
            assert false;
        }catch (Exception ex){
            Assertions.assertEquals("Requested User Alias does not exist in Database",ex.getMessage());
        }

    }

}
