import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.Baloot.CommandHandler;
import org.Baloot.Commodity;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestGetCommoditiesByCategory {
    private CommandHandler cmdHandler;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws JsonProcessingException {
        cmdHandler = new CommandHandler();

        cmdHandler.addCommodity("{\"id\": 1, \"name\": \"Headphone\", \"providerId\": 3, \"price\": 35000, " +
                "\"categories\": [\"Technology\", \"Phone\"], \"rating\": 8.8, \"inStock\": 50}");
        cmdHandler.addCommodity("{\"id\": 2, \"name\": \"Apple\", \"providerId\": 2, \"price\": 3000, " +
                "\"categories\": [\"Technology\"], \"rating\": 1.8, \"inStock\": 20}");
        cmdHandler.addCommodity("{\"id\": 3, \"name\": \"Apple\", \"providerId\": 1, \"price\": 300, " +
                "\"categories\": [\"Fruit\"], \"rating\": 9.8, \"inStock\": 220}");

        mapper = new ObjectMapper();
    }

    @After
    public void tearDown() {
        cmdHandler = null;
        mapper = null;
    }

    @Test
    public void testGetCommoditiesByExistedCategory() throws JsonProcessingException {
        String output = cmdHandler.getCommodityByCategory("Technology");
        String expectedOutput = "\"data\": {\"commoditiesListByCategory\": [{\"id\": 1, \"name\": \"Headphone\", " +
                "\"providerId\": 3, \"price\": 35000, \"categories\": [\"Technology\", \"Phone\"], \"rating\": 8.8}" +
                "{\"id\": 2, \"name\": \"Apple\", \"providerId\": 2, \"price\": 3000, \"categories\": [\"Technology\"], " +
                "\"rating\": 1.8}]}";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testGetCommoditiesByNoExistedCategory() throws JsonProcessingException {
        String output = cmdHandler.getCommodityByCategory("Science");
        String expectedOutput = "\"data\": {\"commoditiesListByCategory\": []}";
        assertEquals(expectedOutput, output);
    }
}