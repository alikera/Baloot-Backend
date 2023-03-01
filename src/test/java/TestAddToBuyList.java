import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.Baloot.CommandHandler;
import org.Baloot.Commodity;
import org.Baloot.Exception.ExceptionHandler;
import org.Baloot.Exception.UserNotFoundException;
import org.Baloot.User;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestAddToBuyList {
    private CommandHandler cmdHandler;
    User user;

    @Before
    public void setUp() throws JsonProcessingException, UserNotFoundException {
        cmdHandler = new CommandHandler();
        cmdHandler.addProvider("{\"id\": 1, \"name\": \"provider1\", \"registryDate\": \"2023-09-15\"}");

        cmdHandler.addUser("{\"username\": \"user1\", \"password\": \"1234\", \"email\": \"user@gmail.com\", " +
                "\"birthDate\": \"1977-09-15\", \"address\": \"address1\", \"credit\": 1500}");

        cmdHandler.addCommodity("{\"id\": 1, \"name\": \"Headphone\", \"providerId\": 1, \"price\": 35000, " +
                "\"categories\": [\"Technology\", \"Phone\"], \"rating\": 8.8, \"inStock\": 10}");
        cmdHandler.addCommodity("{\"id\": 2, \"name\": \"Apple\", \"providerId\": 1, \"price\": 3000, " +
                "\"categories\": [\"Technology\"], \"rating\": 1.8, \"inStock\": 20}");
        cmdHandler.addCommodity("{\"id\": 3, \"name\": \"Apple\", \"providerId\": 1, \"price\": 300, " +
                "\"categories\": [\"Fruit\"], \"rating\": 9.8, \"inStock\": 0}");
        user = cmdHandler.findByUsername("user1");

    }

    @After
    public void tearDown() {
        cmdHandler = null;
    }

    @Test
    public void testAddToBuyListIfUserDoesNotExist() throws JsonProcessingException, ExceptionHandler {
        String output = cmdHandler.addToUserBuyList("{\"username\": \"user2\", \"commodityId\": 1}");
        String expectedOutput = "{\"success\" : false, \"data\" : \"Couldn't find user with the given Username!\"}";
        assertEquals(expectedOutput, output);
    }
    @Test
    public void testAddToBuyListIfCommodityDoesNotExist() throws JsonProcessingException, ExceptionHandler {
        String output = cmdHandler.addToUserBuyList("{\"username\": \"user1\", \"commodityId\": 4}");
        String expectedOutput = "{\"success\" : false, \"data\" : \"Couldn't find commodity with the given Id!\"}";
        assertEquals(expectedOutput, output);
    }
    @Test
    public void testAddToBuyListIfCommodityAlreadyExistedInBuyListUser() throws JsonProcessingException, ExceptionHandler {
        String dummy = cmdHandler.addToUserBuyList("{\"username\": \"user1\", \"commodityId\": 1}");
        String output = cmdHandler.addToUserBuyList("{\"username\": \"user1\", \"commodityId\": 1}");

        String expectedOutput = "{\"success\" : false, \"data\" :\"Commodity already exists in your BuyList!\"}";

        assertEquals(expectedOutput, output);
    }
    @Test
    public void testAddToBuyListOutOfStockCommodity() throws JsonProcessingException, ExceptionHandler {
        String output = cmdHandler.addToUserBuyList("{\"username\": \"user1\", \"commodityId\": 4}");
        String expectedOutput = "{\"success\" : false, \"data\" : \"Commodity out of stock!\"}";
        assertEquals(expectedOutput, output);
    }
    @Test
    public void testAddToBuyListSuccessful() throws JsonProcessingException, ExceptionHandler {
        String output = cmdHandler.addToUserBuyList("{\"username\": \"user1\", \"commodityId\": 1}");
        String expectedOutput = "{\"success\" : true, \"data\" : \"Commodity added to user buy list successfully\"}";
        assertEquals(expectedOutput, output);
    }
}
