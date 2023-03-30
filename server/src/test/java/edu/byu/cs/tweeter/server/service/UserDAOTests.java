package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import edu.byu.cs.tweeter.server.dao.ConcreteUserDAO;
import edu.byu.cs.tweeter.server.dao.DataAccessException;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.UserDAO;
import edu.byu.cs.tweeter.server.dao.dto.UserDTO;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDAOTests {

    private UserDAO userDAO;

    @BeforeAll
    public void setup(){
        userDAO = new ConcreteUserDAO();
        UserDTO user = new UserDTO("tacoJoe","bell","taco","someUrl","somePassword");
        try {
            userDAO.addUser(user);
        }catch (Exception ex){
            System.out.println("Problem setting up tests");
        }

    }

    @AfterAll
    public void takeDown(){
        try {
            userDAO.deleteUser("taco");
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
            userDAO.getUser("loco");
        });

        Assertions.assertEquals("Requested User Alias does not exist in Database",exception.getMessage());

    }

    @Test
    public void getUser_validAlias_noExceptionMatchAlias(){
       try {
           UserDTO user = userDAO.getUser("taco");
           Assertions.assertEquals("taco",user.getUserAlias());
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
        UserDTO user = new UserDTO("hunter","larson","taco","someUrl","somePassword");
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
            UserDTO userFromDatabase = userDAO.getUser("asdf");
            Assertions.assertEquals(user.getUserAlias(),userFromDatabase.getUserAlias());
            Assertions.assertEquals(user.getFirstName(),userFromDatabase.getFirstName());
            Assertions.assertEquals(user.getLastName(),userFromDatabase.getLastName());
            userDAO.deleteUser("asdf");
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
            UserDTO user = new UserDTO("billy","joe","billy","someUrl","somePassword");
            userDAO.addUser(user);
            userDAO.deleteUser("billy");
            userDAO.getUser("billy");
            assert false;
        }catch (DataAccessException ex){
            Assertions.assertEquals("Requested User Alias does not exist in Database",ex.getMessage());
        }

    }

    @Test
    public void updateUser_userNotInDatabase_dataException(){
        UserDTO user = new UserDTO("hunter","larson","billyBoo","someUrl","somePassword");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class,()->{
            userDAO.updateUser(user);
        });

        Assertions.assertEquals("User not in database, can't update",exception.getMessage());

    }

    @Test
    public void updateUser_userNullAliasNull_dataException(){
        UserDTO user = new UserDTO("hunter","larson",null,"someUrl","somePassword");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class,()->{
            userDAO.updateUser(user);
        });

        DataAccessException exception2 = Assertions.assertThrows(DataAccessException.class,()->{
            userDAO.updateUser(null);
        });

        Assertions.assertEquals("User has nullAlias, cannot update",exception.getMessage());
        Assertions.assertEquals("User is null, cannot update",exception2.getMessage());

    }

    @Test
    public void updateUser_userInDataBase_updatedUser(){
        try{
            UserDTO user = userDAO.getUser("taco");
            user.setFirstName("Joey");
            userDAO.updateUser(user);
            UserDTO updatedUser = userDAO.getUser("taco");
            Assertions.assertEquals(user.getFirstName(),updatedUser.getFirstName());
        }catch (Exception ex){
            System.out.println("Exception thrown");
            System.out.println(ex.getMessage());
            assert false;
        }
    }

    @Test
    public void getPassword_userAliasIsNull_DataAccessException(){
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class,()->{
            userDAO.getPassword(null);
        });

        Assertions.assertEquals("null alias, cannot get password",exception.getMessage());
    }

    @Test
    public void getPassword_userAliasNotInDatabase_DataAccessException(){
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class,()->{
            userDAO.getPassword("nicky");
        });

        Assertions.assertEquals("alias not in database, cannot get user",exception.getMessage());
    }

    @Test
    public void getPassword_userAliasInDatabase_returnPassword(){
        try {
            String actualPassword = userDAO.getPassword("taco");
            Assertions.assertEquals("somePassword",actualPassword);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            assert false;
        }
    }



}
