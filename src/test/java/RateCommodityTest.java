import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.Baloot.CommandHandler;
import org.Baloot.Commodity;
import org.Baloot.Exception.CommodityNotFoundException;
import org.Baloot.Exception.ExceptionHandler;
import org.Baloot.Exception.InvalidRatingException;
import org.Baloot.User;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class RateCommodityTest {
    private CommandHandler cmdHandler;
    User user;
    @Before
    public void setUp() throws JsonProcessingException {
        cmdHandler = new CommandHandler();

        cmdHandler.addUser("{\"username\": \"user1\", \"password\": \"1234\", \"email\": \"user@gmail.com\", " +
                "\"birthDate\": \"1977-09-15\", \"address\": \"address1\", \"credit\": 1500}");
        cmdHandler.addCommodity("{\"id\": 1, \"name\": \"Headphone\", \"providerId\": 3, \"price\": 35000, " +
                "\"categories\": [\"Technology\", \"Phone\"], \"rating\": 8.8, \"inStock\": 50}");
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
    public void testIncreaseRatingsCountIfUserHasNotRatedCurrentCommodity() throws ExceptionHandler {
        int userGivenRating = 4;
        Commodity commodity =  cmdHandler.findByCommodityId(1);
        double ratingsCount = commodity.getCountOfRatings();
        commodity.rateCommodity(user.getUsername(), userGivenRating);

        assertEquals(ratingsCount + 1, commodity.getCountOfRatings());
//        assertEquals((rating + userGivenRating)/(ratingsCount + 1), commodity.getRating());
    }
    @Test
    public void testCalculateAverageRatingIfUserHasNotRatedCurrentCommodity() throws ExceptionHandler {
        int userGivenRating = 4;
        Commodity commodity =  cmdHandler.findByCommodityId(1);
        double rating  = commodity.getRating();
        commodity.rateCommodity(user.getUsername(), userGivenRating);

//        assertEquals(ratingsCount + 1, commodity.getCountOfRatings());
        assertEquals((rating + userGivenRating)/commodity.getCountOfRatings(), commodity.getRating());
    }
    @Test
    public void testDontIncreaseRatingsCountIfUserHasAlreadyRatedCurrentCommodity() throws ExceptionHandler {
        int userGivenRating_1 = 4;
        Commodity commodity =  cmdHandler.findByCommodityId(1);
        commodity.rateCommodity(user.getUsername(), userGivenRating_1);
        double ratingsCount = commodity.getCountOfRatings();
        int userGivenRating_2 = 2;
        commodity.rateCommodity(user.getUsername(), userGivenRating_2);

        assertEquals(ratingsCount, commodity.getCountOfRatings());
//        assertEquals((rating + userGivenRating)/(ratingsCount + 1), commodity.getRating());
    }

    @Test
    public void testCalculateAverageRatingIfUserHasAlreadyRatedCurrentCommodity() throws ExceptionHandler {
        int userGivenRating_1 = 4;
        Commodity commodity =  cmdHandler.findByCommodityId(1);
        commodity.rateCommodity(user.getUsername(), userGivenRating_1);

        double rating  = commodity.getRating();
        int userGivenRating_2 = 2;
        commodity.rateCommodity(user.getUsername(), userGivenRating_2);

//        assertEquals(ratingsCount, commodity.getCountOfRatings());
        assertEquals((rating + userGivenRating_2)/commodity.getCountOfRatings(), commodity.getRating());
    }
    @Test
    public void TestErrorIfUserRatingIsNotBetween1To10() throws ExceptionHandler{
        int userGivenRating = -1;
        Commodity commodity =  cmdHandler.findByCommodityId(1);
        commodity.rateCommodity(user.getUsername(), userGivenRating);

//        assertEquals(ratingsCount + 1, commodity.getCountOfRatings());
        assertThrows(InvalidRatingException.class, () -> commodity.rateCommodity(user.getUsername(), userGivenRating));
    }

}
