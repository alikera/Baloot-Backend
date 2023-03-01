import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.Baloot.CommandHandler;
import org.Baloot.Commodity;
import org.Baloot.Exception.ExceptionHandler;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestAddToBuyList {
    private CommandHandler cmdHandler;

    @Before
    public void setUp() throws JsonProcessingException {
        cmdHandler = new CommandHandler();

        cmdHandler.addUser("{\"username\": \"user1\", \"password\": \"1234\", \"email\": \"user@gmail.com\", " +
                "\"birthDate\": \"1977-09-15\", \"address\": \"address1\", \"credit\": 1500}");

        cmdHandler.addCommodity("{\"id\": 1, \"name\": \"Headphone\", \"providerId\": 3, \"price\": 35000, " +
                "\"categories\": [\"Technology\", \"Phone\"], \"rating\": 8.8, \"inStock\": 0}");
        cmdHandler.addCommodity("{\"id\": 2, \"name\": \"Apple\", \"providerId\": 2, \"price\": 3000, " +
                "\"categories\": [\"Technology\"], \"rating\": 1.8, \"inStock\": 20}");
        cmdHandler.addCommodity("{\"id\": 3, \"name\": \"Apple\", \"providerId\": 1, \"price\": 300, " +
                "\"categories\": [\"Fruit\"], \"rating\": 9.8, \"inStock\": 220}");
    }

    @After
    public void tearDown() {
        cmdHandler = null;
    }

    @Test
    public void testAddToBuyListNotExistedUser() throws JsonProcessingException, ExceptionHandler {
        String output = cmdHandler.getCommodityById(1);
        String expectedOutput = "\"data\": {\"id\": 1, \"name\": \"Headphone\", \"providerId\": 3, \"price\": 35000, " +
                "\"categories\": [\"Technology\", \"Phone\"], \"rating\": 8.8}";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCommodityAlreadyExistedInBuyListUser() throws JsonProcessingException, ExceptionHandler {
        String output = cmdHandler.getCommodityById(1);
        String expectedOutput = "\"data\": {\"id\": 1, \"name\": \"Headphone\", \"providerId\": 3, \"price\": 35000, " +
                "\"categories\": [\"Technology\", \"Phone\"], \"rating\": 8.8}";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testAddToBuyListNotExistedCommodity() throws JsonProcessingException, ExceptionHandler {
        String output = cmdHandler.getCommodityById(1);
        String expectedOutput = "\"data\": {\"id\": 1, \"name\": \"Headphone\", \"providerId\": 3, \"price\": 35000, " +
                "\"categories\": [\"Technology\", \"Phone\"], \"rating\": 8.8}";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testAddToBuyListOutOfStockCommodity() throws JsonProcessingException, ExceptionHandler {
        String output = cmdHandler.getCommodityById(1);
        String expectedOutput = "\"data\": {\"id\": 1, \"name\": \"Headphone\", \"providerId\": 3, \"price\": 35000, " +
                "\"categories\": [\"Technology\", \"Phone\"], \"rating\": 8.8}";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testAddToBuyListSuccessful() throws JsonProcessingException, ExceptionHandler {
        String output = cmdHandler.getCommodityById(1);
        String expectedOutput = "\"data\": {\"id\": 1, \"name\": \"Headphone\", \"providerId\": 3, \"price\": 35000, " +
                "\"categories\": [\"Technology\", \"Phone\"], \"rating\": 8.8}";
        assertEquals(expectedOutput, output);
    }
}
