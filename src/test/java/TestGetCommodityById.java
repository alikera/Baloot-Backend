import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.Baloot.Baloot;
import org.Baloot.CommandHandler;
import org.Baloot.Commodity;
import org.Baloot.Exception.ExceptionHandler;
import org.Baloot.Provider;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestGetCommodityById {
    private ObjectMapper mapper;
    private Baloot baloot;

    @Before
    public void setUp() throws JsonProcessingException {
        baloot = new Baloot();
        mapper = new ObjectMapper();

        Provider provider = new Provider(1, "A", "2023-09-15");
        baloot.addProvider(provider);

        List<String> categories1 = new ArrayList<>();
        categories1.add("Technology");
        Commodity commodity1 = new Commodity(1, "Headphone", 1, 35000, categories1, 0,
                50);
        baloot.addCommodity(commodity1);
    }

    @After
    public void tearDown() {
        baloot = null;
        mapper = null;
    }

    @Test
    public void testGetCommoditiesById() throws JsonProcessingException, ExceptionHandler {
        ObjectNode outputNode = baloot.getCommodityById(1);

        assertTrue(outputNode.get("success").asBoolean());
        assertEquals("{\"id\":1,\"name\":\"Headphone\",\"providerId\":1,\"price\":35000.0,\"categories\":[\"Technology\"],\"rating\":0.0}", outputNode.get("data").toString());
    }

    @Test
    public void testGetCommoditiesByNoExistedId() throws JsonProcessingException, ExceptionHandler {
        ObjectNode outputNode = baloot.getCommodityById(4);

        assertFalse(outputNode.get("success").asBoolean());
        assertEquals("Couldn't find commodity with the given Id!", outputNode.get("data").asText());
    }
}
