import org.Baloot.Baloot;
import org.Baloot.Entities.Commodity;
import org.Baloot.Database.Database;
import org.Baloot.Exception.*;
import org.Baloot.Entities.User;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;
public class TestShowingBuyList {
    private Baloot baloot;
    private Database db;
    private User user, user2, user3;
    private Commodity commodity1, commodity2, commodity3;
    @Before
    public void setUp() throws ProviderNotFoundException, CommodityExistenceException, NotEnoughCreditException {
        db = new Database();
        baloot = Baloot.getBaloot(db);

        user = new User("user1", "1234", "user@gmail.com","1977-09-15",
                "address1",1500);
        db.insertUser(user);
        user2 = new User("user2", "1234", "user2@gmail.com","1977-09-15",
                "address2",5500);
        db.insertUser(user2);
        user3 = new User("user3", "1234", "user3@gmail.com","1977-09-15",
                "address2",5500);
        db.insertUser(user3);

        List<String> categories1 = new ArrayList<>();
        categories1.add("Technology");
        commodity1 = new Commodity(1, "Headphone", 1, 35000, categories1, 0.0,
                50);
        db.insertCommodity(commodity1);
        user2.addToBuyList(commodity1.getId());
        user3.addToBuyList(commodity1.getId());
        user3.moveBuyToPurchased(0);

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
    public void testShowingBuyListIfUserBuyListIsEmpty() {
        Set<Integer> expected = Collections.emptySet();
        Set<Integer> actual = user.getBuyList();
        assertEquals(expected, actual);
    }
    @Test
    public void testShowingBuyListIfUserBuyListIsNotEmpty() {
        Set<Integer> expected = new HashSet<>();
        expected.add(1);
        Set<Integer> actual = user2.getBuyList();
        assertEquals(expected, actual);
    }

    @Test
    public void testErrorInShowingBuyListIfThisCommodityAlreadyExistsInUserBuyList() {
        assertThrows(CommodityExistenceException.class, () -> user2.addToBuyList(commodity1.getId()));
    }

    @Test
    public void testShowingBuyListIfUserHasPurchasedHisBuyListSuccessfully() throws NotEnoughCreditException {
        Set<Integer> actual = user3.getBuyList();
        Set<Integer> expected = Collections.emptySet();
        assertEquals(expected, actual);
    }
    @Test
    public void testShowingBuyListIfUserHasFinalizedHisBuyListPaymentSuccessfully() throws NotEnoughCreditException, UserNotFoundException, CommodityNotFoundException {
        baloot.userManager.finalizePayment("user1");
        Set<Integer> actual = user.getBuyList();
        Set<Integer> expected = Collections.emptySet();
        assertEquals(expected, actual);
    }

    @Test
    public void testErrorInShowingBuyListIfUserCreditIsNotEnough() {
        assertThrows(NotEnoughCreditException.class, () -> user2.moveBuyToPurchased(10000));
    }
    @Test
    public void testErrorInShowingBuyListIfUserCreditIsNotEnoughWhenFinalizingPayment() {
        assertThrows(NotEnoughCreditException.class, () -> baloot.userManager.finalizePayment("user2"));
    }

}
