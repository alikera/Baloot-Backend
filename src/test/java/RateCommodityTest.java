import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.Baloot.Baloot;
import org.Baloot.Commodity;
import org.Baloot.Exception.CommodityNotFoundException;
import org.Baloot.Exception.ExceptionHandler;
import org.Baloot.Exception.InvalidRatingException;
import org.Baloot.Exception.UserNotFoundException;
import org.Baloot.Provider;
import org.Baloot.User;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RateCommodityTest {
//    private Baloot baloot;
//    User user;
//    @Before
//    public void setUp() throws JsonProcessingException, UserNotFoundException {
//        baloot = new Baloot();
//        baloot.addProvider("{\"id\": 1, \"name\": \"provider1\", \"registryDate\": \"2023-09-15\"}");
//        baloot.addUser("{\"username\": \"user1\", \"password\": \"1234\", \"email\": \"user@gmail.com\", " +
//                "\"birthDate\": \"1977-09-15\", \"address\": \"address1\", \"credit\": 1500}");
//        baloot.addCommodity("{\"id\": 1, \"name\": \"Headphone\", \"providerId\": 1, \"price\": 35000, " +
//                "\"categories\": [\"Technology\", \"Phone\"], \"rating\": 0.0, \"inStock\": 50}");
//        baloot.addCommodity("{\"id\": 2, \"name\": \"Apple\", \"providerId\": 1, \"price\": 3000, " +
//                "\"categories\": [\"Technology\"], \"rating\": 0.0, \"inStock\": 20}");
//        baloot.addCommodity("{\"id\": 3, \"name\": \"Apple\", \"providerId\": 1, \"price\": 300, " +
//                "\"categories\": [\"Fruit\"], \"rating\": 0.0, \"inStock\": 220}");
//
//        user = baloot.findByUsername("user1");
//    }
//    @After
//    public void tearDown() {
//        baloot = null;
//    }
private Baloot baloot;
    User user;
    Provider provider;
    Commodity commodity1, commodity2, commodity3;
    ObjectMapper objectMapper;
    @Before
    public void setUp() throws JsonProcessingException, UserNotFoundException {
        baloot = new Baloot();
        objectMapper = new ObjectMapper();
        provider = new Provider(1, "provider1", "2023-09-15");
        baloot.addProvider(provider);

        user = new User("user1", "1234", "user@gmail.com","1977-09-15",
                "address1",1500);
        baloot.addUser(user);

        List<String> categories1 = new ArrayList<>();
        categories1.add("Technology");
        commodity1 = new Commodity(1, "Headphone", 1, 35000, categories1, 0.0,
                50);
        baloot.addCommodity(commodity1);

        List<String> categories2 = new ArrayList<>();
        categories2.add("Technology");
        categories2.add("Phone");
        commodity2 = new Commodity(2, "Apple", 1, 3000, categories2, 0.0,
                0);
        baloot.addCommodity(commodity2);

        List<String> categories3 = new ArrayList<>();
        categories3.add("Fruit");
        commodity3 = new Commodity(3, "Apple", 1, 300, categories3, 0.0,
                220);
        baloot.addCommodity(commodity3);
    }

    @After
    public void tearDown() {
        baloot = null;
        objectMapper = null;
        user = null;
        provider = null;
        commodity1 = null;
        commodity2 = null;
        commodity3 = null;

    }
    @Test
    public void testIncreaseRatingsCountIfUserHasNotRatedCurrentCommodity() throws ExceptionHandler {
        int userGivenRating = 4;
        double ratingsCount = commodity1.getCountOfRatings();
        commodity1.rateCommodity(user.getUsername(), userGivenRating);

        assertEquals(ratingsCount + 1, commodity1.getCountOfRatings(), 0.01);
//        assertEquals((rating + userGivenRating)/(ratingsCount + 1), commodity.getRating());
    }
    @Test
    public void testCalculateAverageRatingIfUserHasNotRatedCurrentCommodity() throws ExceptionHandler {
        int userGivenRating = 4;
        double rating  = commodity1.getRating();
        commodity1.rateCommodity(user.getUsername(), userGivenRating);

//        assertEquals(ratingsCount + 1, commodity.getCountOfRatings());
        assertEquals((rating + userGivenRating)/commodity1.getCountOfRatings(), commodity1.getRating(), 0.01);
    }
    @Test
    public void testDontIncreaseRatingsCountIfUserHasAlreadyRatedCurrentCommodity() throws ExceptionHandler {
        int userGivenRating_1 = 4;
        commodity1.rateCommodity(user.getUsername(), userGivenRating_1);
        double ratingsCount = commodity1.getCountOfRatings();
        int userGivenRating_2 = 2;
        commodity1.rateCommodity(user.getUsername(), userGivenRating_2);

        assertEquals(ratingsCount, commodity1.getCountOfRatings(), 0.01);
//        assertEquals((rating + userGivenRating)/(ratingsCount + 1), commodity.getRating());
    }
    @Test
    public void testCalculateAverageRatingIfUserHasAlreadyRatedCurrentCommodity() throws ExceptionHandler {
        int userGivenRating_1 = 4;
        commodity1.rateCommodity(user.getUsername(), userGivenRating_1);

        double rating = commodity1.getRating();
        double ratingsCount = commodity1.getCountOfRatings();

        int userGivenRating_2 = 2;
        commodity1.rateCommodity(user.getUsername(), userGivenRating_2);

//        assertEquals(ratingsCount, commodity.getCountOfRatings());
        assertEquals(((rating*ratingsCount - userGivenRating_1) + userGivenRating_2) / commodity1.getCountOfRatings(), commodity1.getRating(),0.01);
    }
    @Test
    public void TestErrorIfUserRatingIsNotBetween1To10() {
        int userGivenRating = -1;

//        assertEquals(ratingsCount + 1, commodity.getCountOfRatings());
        assertThrows(InvalidRatingException.class, () -> commodity1.rateCommodity(user.getUsername(), userGivenRating));
    }
    @Test
    public void testRateCommodityIfUserDoesNotExist() throws JsonProcessingException {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("username", "user2");
        node.put("commodityId", 1);
        node.put("score", 2);
        ObjectNode outputNode = baloot.rateCommodity(node);
        assertFalse(outputNode.get("success").asBoolean());
        assertEquals("Couldn't find user with the given Username!", outputNode.get("data").asText());
    }

    @Test
    public void testRateCommodityIfCommodityDoesNotExist() throws JsonProcessingException {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("username", "user1");
        node.put("commodityId", 4);
        node.put("score", 2);
        ObjectNode outputNode = baloot.rateCommodity(node);
        assertFalse(outputNode.get("success").asBoolean());
        assertEquals("Couldn't find commodity with the given Id!", outputNode.get("data").asText());
    }
}
