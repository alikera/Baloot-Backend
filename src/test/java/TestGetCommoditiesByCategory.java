//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import org.Baloot.Baloot;
//import org.Baloot.CommandHandler;
//import org.Baloot.Commodity;
//import org.Baloot.Provider;
//import org.junit.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class TestGetCommoditiesByCategory {
//    private ObjectMapper mapper;
//    private Baloot baloot;
//
//    @Before
//    public void setUp() throws JsonProcessingException {
//        baloot = new Baloot();
//        mapper = new ObjectMapper();
//
//        Provider provider = new Provider(1, "A", "2023-09-15");
//        baloot.addProvider(provider);
//
//        List<String> categories1 = new ArrayList<>();
//        categories1.add("Technology");
//        Commodity commodity1 = new Commodity(1, "Headphone", 1, 35000, categories1, 0,
//                50);
//        baloot.addCommodity(commodity1);
//
//        List<String> categories2 = new ArrayList<>();
//        categories2.add("Technology");
//        categories2.add("Phone");
//        Commodity commodity2 = new Commodity(2, "Apple", 1, 3000, categories2, 0,
//                0);
//        baloot.addCommodity(commodity2);
//
//        List<String> categories3 = new ArrayList<>();
//        categories3.add("Fruit");
//        Commodity commodity3 = new Commodity(3, "Apple", 1, 300, categories3, 0,
//                220);
//        baloot.addCommodity(commodity3);
//    }
//
//    @After
//    public void tearDown() {
//        baloot = null;
//        mapper = null;
//    }
//
//    @Test
//    public void testGetCommoditiesByExistedCategory() throws JsonProcessingException {
//        ObjectNode outputNode = baloot.getCommodityByCategory("Technology");
//        assertTrue(outputNode.get("success").asBoolean());
//        assertEquals("{\"commoditiesListByCategory\":[{\"id\":1,\"name\":\"Headphone\",\"providerId\":1,\"price\":35000.0,\"categories\":[\"Technology\"],\"rating\":0.0},{\"id\":2,\"name\":\"Apple\",\"providerId\":1,\"price\":3000.0,\"categories\":[\"Technology\",\"Phone\"],\"rating\":0.0}]}", outputNode.get("data").toString());
//    }
//
//    @Test
//    public void testGetCommoditiesByNotExistedCategory() throws JsonProcessingException {
//        ObjectNode outputNode = baloot.getCommodityByCategory("Science");
//        assertTrue(outputNode.get("success").asBoolean());
//        assertEquals("{\"commoditiesListByCategory\":[]}", outputNode.get("data").toString());
//    }
//}
