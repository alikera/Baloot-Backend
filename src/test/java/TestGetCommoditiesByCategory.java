import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

        cmdHandler.addProvider("{\"id\": 1, \"name\": \"provider1\", \"registryDate\": \"2023-09-15\"}");
        cmdHandler.addCommodity("{\"id\": 1, \"name\": \"Headphone\", \"providerId\": 1, \"price\": 35000, " +
                "\"categories\": [\"Technology\", \"Phone\"], \"rating\": 8.8, \"inStock\": 50}");
        cmdHandler.addCommodity("{\"id\": 2, \"name\": \"Apple\", \"providerId\": 1, \"price\": 3000, " +
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
        String expectedOutput = "{\n\t\"data\" : {\n\t\t\"commoditiesListByCategory\" : [ {\n\t\t\t\"id\": 1, \n\t\t\t\"name\" : \n\t\t\t\"Headphone\", " +
                "\n\t\t\t\"providerId\" : 1, \n\t\t\t\"price\" : 35000, \n\t\t\t\"categories\" : [ \"Technology\", \"Phone\" ], \n\t\t\t\"rating\" : 8.8\n\t\t}, " +
                "{\n\t\t\t\"id\" : 2, \n\t\t\t\"name\" : \"Apple\", \n\t\t\t\"providerId\" : 1, \n\t\t\t\"price\" : 3000, \n\t\t\t\"categories\" : [ \"Technology\" ], " +
                "\n\t\t\t\"rating\" : 1.8\n\t\t}\n\t\t\t]\n}";
//        JsonNode node = mapper.readValue(expectedOutput);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testGetCommoditiesByNoExistedCategory() throws JsonProcessingException {
        String output = cmdHandler.getCommodityByCategory("Science");
        String expectedOutput = "\"data\": {\"commoditiesListByCategory\": []}";
        assertEquals(expectedOutput, output);
    }
}
