import org.Baloot.Baloot;
import org.Baloot.Entities.Commodity;
import org.Baloot.Database.Database;
import org.Baloot.Exception.*;
import org.Baloot.Entities.User;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RateCommodityTest {
    private Baloot baloot;
    private Database db;
    private User user;
    private Commodity commodity1, commodity2, commodity3;
    @Before
    public void setUp() throws ProviderNotFoundException {
        db = new Database();
        baloot = Baloot.getBaloot(db);

        user = new User("user1", "1234", "user@gmail.com","1977-09-15",
                "address1",1500);
        db.insertUser(user);

        List<String> categories1 = new ArrayList<>();
        categories1.add("Technology");
        commodity1 = new Commodity(1, "Headphone", 1, 35000, categories1, 0.0,
                50);
        db.insertCommodity(commodity1);

        List<String> categories2 = new ArrayList<>();
        categories2.add("Technology");
        categories2.add("Phone");
        commodity2 = new Commodity(2, "Apple", 1, 3000, categories2, 0.0,
                0);
        db.insertCommodity(commodity2);

        List<String> categories3 = new ArrayList<>();
        categories3.add("Fruit");
        commodity3 = new Commodity(3, "Apple", 1, 300, categories3, 0.0,
                220);
        db.insertCommodity(commodity3);
    }

    @After
    public void tearDown() {
        baloot = null;
        user = null;
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
    }
    @Test
    public void testCalculateAverageRatingIfUserHasNotRatedCurrentCommodity() throws ExceptionHandler {
        int userGivenRating = 4;
        double rating  = commodity1.getRating();
        commodity1.rateCommodity(user.getUsername(), userGivenRating);

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
    }
    @Test
    public void testCalculateAverageRatingIfUserHasAlreadyRatedCurrentCommodity() throws ExceptionHandler {
        int userGivenRating_1 = 4;
        commodity1.rateCommodity(user.getUsername(), userGivenRating_1);

        double rating = commodity1.getRating();
        double ratingsCount = commodity1.getCountOfRatings();

        int userGivenRating_2 = 2;
        commodity1.rateCommodity(user.getUsername(), userGivenRating_2);

        assertEquals(((rating*ratingsCount - userGivenRating_1) + userGivenRating_2) / commodity1.getCountOfRatings(), commodity1.getRating(),0.01);
    }
    @Test
    public void TestErrorInRateCommodityIfUserRatingIsNotBetween1To10() {
        int userGivenRating = -1;

        assertThrows(InvalidRatingException.class, () -> commodity1.rateCommodity(user.getUsername(), userGivenRating));
    }
    @Test
    public void testErrorInRateCommodityIfUserDoesNotExist() {
        assertThrows(UserNotFoundException.class, () -> baloot.commodityManager.rateCommodity("user2", "1", "2"));
    }

    @Test
    public void testErrorInRateCommodityIfCommodityDoesNotExist() {
        assertThrows(CommodityNotFoundException.class, () -> baloot.commodityManager.rateCommodity("user1", "4", "2"));
    }
    @Test
    public void testErrorInRateCommodityWhenRateIsNotNumerical() {
        String rate = "a";
        assertThrows(NumberFormatException.class, () -> baloot.commodityManager.rateCommodity("user1", "1", rate));
    }
}
