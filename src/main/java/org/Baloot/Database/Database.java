package org.Baloot.Database;

import org.Baloot.Entities.*;
import org.Baloot.Entities.Date;
import org.Baloot.Exception.*;
import org.Baloot.Repository.*;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.lang.reflect.Field;

public class Database {
    static DiscountRepository<DiscountCode> discountRepository = new DiscountRepository<>();
    static ProviderRepository<Provider> providerRepository = new ProviderRepository<>();
    static UserRepository<User> userRepository = new UserRepository<>();
    static CommodityRepository<Commodity> commodityRepository = new CommodityRepository<>();
    static CommentRepository<Comment> commentRepository = new CommentRepository<>();

    public static void insertInitialData(User[] _users, Provider[] _providers, Commodity[] _commodities,
                                         Comment[] _comments, DiscountCode[] _discountCodes) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        Statement createTableStatement = con.createStatement();
        discountRepository.createTable(createTableStatement);
        discountRepository.createWeakTable(createTableStatement);

        providerRepository.createTable(createTableStatement);

        commodityRepository.createTable(createTableStatement);
        commodityRepository.createWeakTable(createTableStatement);
        commodityRepository.createRatingTable(createTableStatement);

        userRepository.createTable(createTableStatement);
        userRepository.createWeakTable(createTableStatement, "BuyList");
        userRepository.createPurchasedListTable(createTableStatement);
        commentRepository.createTable(createTableStatement);
        commentRepository.createWeakTable(createTableStatement);

        System.out.println(_users.length);
        createTableStatement.executeBatch();
        createTableStatement.close();
        con.close();

        for (DiscountCode discountCode: _discountCodes) {
            discountRepository.insert(discountRepository.insertStatement(discountCode.getAttributes()));
        }

        for (Provider provider: _providers){
            insertProvider(provider);
        }

        for (Commodity commodity: _commodities){
            insertCommodity(commodity);
        }

        for (User user: _users){
            insertUser(user);
        }
        for (Comment comment: _comments){
            insertComment(comment);
        }
    }
    public static void insertToBuyList(String username, String commodityId) throws SQLException {
        HashMap<String, String> values = new HashMap<>();
        values.put("cid", commodityId);
        values.put("username", username);
        userRepository.insert(userRepository.insertBuyListStatement(values));
    }
    public static void removeFromBuyList(String username, String commodityId) throws SQLException {
        HashMap<String, String> values = new HashMap<>();
        values.put("cid", commodityId);
        values.put("username", username);
        userRepository.insert(userRepository.removeFromBuyListStatement(values));
    }
    public static void insertToPurchasedList(String username, int commodityId, int quantity, Timestamp timestamp) throws SQLException {
        HashMap<String, String> values = new HashMap<>();
        values.put("cid", String.valueOf(commodityId));
        values.put("username", username);
        values.put("quantity", String.valueOf(quantity));
        values.put("buyTime", timestamp.toString());
        userRepository.insert(userRepository.insertPurchasedListStatement(values));
    }
    public static void insertUser(User user) throws SQLException {
        user.hashPassword();
        userRepository.insert(userRepository.insertStatement(user.getAttributes()));
    }

    public static void insertProvider(Provider provider) throws SQLException {
        providerRepository.insert(providerRepository.insertStatement(provider.getAttributes()));
    }

    public static void insertCategories(Commodity commodity) throws SQLException {
        for(String category: commodity.getCategories()){
            HashMap<String, String> values = new HashMap<>();
            values.put("cid", String.valueOf(commodity.getId()));
            values.put("name", category);
            commodityRepository.insert(commodityRepository.insertCategory(values));
        }
    }
    public static void insertCommodity(Commodity commodity) throws SQLException {
        commodityRepository.insert(commodityRepository.insertStatement(commodity.getAttributes()));
        insertCategories(commodity);
    }

    public static void insertComment(Comment comment) throws SQLException {
        commentRepository.insert(commentRepository.insertStatement(comment.getAttributes()));
    }

    public static User findByUsername(String username) throws UserNotFoundException, SQLException {
        List<HashMap<String, String>> userRow = userRepository.select(
                new ArrayList<Object>() {{ add(username); }},
                userRepository.getColNames(),
                userRepository.selectOneStatement("username")
        );
        if (userRow.isEmpty()) {
            throw new UserNotFoundException("User not found!");
        }

        return new User(userRow.get(0).get("username"),
                userRow.get(0).get("password"),
                userRow.get(0).get("salt"),
                userRow.get(0).get("email"),
                userRow.get(0).get("birth_date"), userRow.get(0).get("address"),
                Double.parseDouble(userRow.get(0).get("credit")));
    }

    public static boolean isEmailUsed(String email) throws SQLException {
        List<HashMap<String, String>> userRow = userRepository.select(
                new ArrayList<Object>() {{ add(email); }},
                userRepository.getColNames(),
                userRepository.selectOneStatement("email")
        );
        if (userRow.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    public static HashMap<Commodity, Integer> castHashMap(List<HashMap<String, String>> originalMap) throws SQLException {
        HashMap<Commodity, Integer> castedMap = new HashMap<>();
        for(HashMap<String, String> hashMap: originalMap) {
            Commodity commodity = new Commodity(Integer.parseInt(hashMap.get("cid")),
                    hashMap.get("name"),
                    Integer.parseInt(hashMap.get("pid")),
                    Double.parseDouble(hashMap.get("price")),
                    commodityRepository.getCategories(Integer.parseInt(hashMap.get("cid"))),
                    Double.parseDouble(hashMap.get("rating")),
                    Integer.parseInt(hashMap.get("inStock")),
                    hashMap.get("image"));
            castedMap.put(commodity,Integer.parseInt(hashMap.get("quantity")));
        }

        return castedMap;
    }

    public static HashMap<Commodity, Integer> getUserList(String username, String type) throws SQLException{
        List<String> colNames = commodityRepository.getColNames();
        colNames.add("quantity");
        String selectStatement = userRepository.selectListStatement(type);
        List<HashMap<String, String>> listRow = userRepository.select(new ArrayList<Object>() {{ add(username); }},
                colNames,
                selectStatement);

        return castHashMap(listRow);
    }

    public static List<Commodity> getProviderCommodities(String providerId) throws Exception {
        List<String> colNames = commodityRepository.getColNames();
        String selectStatement = providerRepository.selectCommodities();
        List<HashMap<String, String>> rows = userRepository.select(new ArrayList<Object>() {{ add(Integer.parseInt(providerId)); }},
                colNames,
                selectStatement);

        return castToList(rows, Commodity.class);
    }

    public static Commodity findByCommodityId(int commodityId) throws CommodityNotFoundException, SQLException {
        List<HashMap<String, String>> commodityRow = commodityRepository.select(
                new ArrayList<Object>() {{ add(commodityId); }},
                commodityRepository.getColNames(),
                commodityRepository.selectOneStatement("cid")
        );
        if (commodityRow.isEmpty()) {
            throw new CommodityNotFoundException("Commodity not found!");
        }

        return new Commodity(Integer.parseInt(commodityRow.get(0).get("cid")),
                commodityRow.get(0).get("name"),
                Integer.parseInt(commodityRow.get(0).get("pid")),
                Double.parseDouble(commodityRow.get(0).get("price")),
                commodityRepository.getCategories(commodityId),
                Double.parseDouble(commodityRow.get(0).get("rating")),
                Integer.parseInt(commodityRow.get(0).get("inStock")),
                commodityRow.get(0).get("image"));
    }

    public static Provider findByProviderId(int providerId) throws ProviderNotFoundException, SQLException {
        List<HashMap<String, String>> providerRow = providerRepository.select(
                new ArrayList<Object>() {{ add(providerId); }},
                providerRepository.getColNames(),
                providerRepository.selectOneStatement("pid")
        );
        // TODO: Maybe Error in throwing(providerRow[0].isempty()
        if (providerRow.isEmpty()) {
            throw new ProviderNotFoundException("Provider not found!");
        }

        return new Provider(Integer.parseInt(providerRow.get(0).get("pid")),
                providerRow.get(0).get("name"),
                providerRow.get(0).get("date"),
                providerRow.get(0).get("image"));
    }

    public static <T> List<T> castToList(List<HashMap<String, String>> originalMap, Class<T> targetType) throws Exception {
        List<T> castedList = new ArrayList<>();

        for (HashMap<String, String> hashMap : originalMap) {
            T instance = targetType.getDeclaredConstructor().newInstance();

            for (Field field : targetType.getDeclaredFields()) {
                String fieldName = field.getName();
                String fieldValue = hashMap.get(fieldName);

                if (fieldValue != null) {
                    field.setAccessible(true);
                    Object parsedValue = parseValue(field.getType(), fieldValue);
                    field.set(instance, parsedValue);
                }
            }

            castedList.add(instance);
        }

        return castedList;
    }

    private static Object parseValue(Class<?> fieldType, String fieldValue) {
        if (fieldType == int.class || fieldType == Integer.class) {
            return Integer.parseInt(fieldValue);
        } else if (fieldType == double.class || fieldType == Double.class) {
            return Double.parseDouble(fieldValue);
        } else if (fieldType == long.class || fieldType == Long.class) {
            return Long.parseLong(fieldValue);
        } else if(fieldType == org.Baloot.Entities.Date.class){
            Date date = new Date(fieldValue);
            return date;
        }
        else {
            // For other types, you can handle additional conversions or leave as-is
            return fieldValue;
        }
    }

    public static List<Commodity> getCommodities(String name, int available, String sortBy, String tableName, String entity) throws Exception {
        String finalName = name + '%';
        List<HashMap<String, String>> commodityRows = commodityRepository.select(
                new ArrayList<Object>() {{ add(finalName); add(available);}},
                commodityRepository.getColNames(),
                commodityRepository.selectCommodities(tableName, entity, sortBy)
        );

        return castToList(commodityRows, Commodity.class);
    }

    public static List<Comment> getComments(int commodityId) throws Exception {
        List<HashMap<String, String>> commentRows = commentRepository.select(
                new ArrayList<Object>() {{ add(commodityId); }},
                commentRepository.getColNames(),
                commentRepository.selectOneStatement("cid")
        );

        List<Comment> allComments = castToList(commentRows, Comment.class);
        setLikesAndDislikes(allComments);
//        setVotesOfComment(allComments);
        return allComments;
    }

    public static void setLikesAndDislikes(List<Comment> allComments) throws SQLException {
        for(Comment comment: allComments) {
            List<HashMap<String, String>> rows = commentRepository.select(new ArrayList<Object>() {{
                                                                              add(comment.getId());
                                                                          }},
                    new ArrayList<String>() {{
                        add("userEmail"); add("status");
                    }},
                    commentRepository.selectVoteStatement());
            comment.setLikesAndDislikes(rows);
        }
    }
    public static int insertVoteToComment(String userEmail, String tid, String status) throws SQLException, InvalidVoteException {
        if (!Objects.equals(status, "0") && !Objects.equals(status, "1") && !Objects.equals(status, "-1")) {
            throw new InvalidVoteException("Invalid Vote");
        }
        List<HashMap<String, String>> rows = commentRepository.select(new ArrayList<Object>() {{
                                                                          add(Integer.parseInt(tid)); add(userEmail);
                                                                      }},
                new ArrayList<String>() {{
                    add("status");
                }},
                commentRepository.selectPrevVoteStatement());
        int prevStatus = 1;
        if(rows.size() != 0){
            prevStatus = Integer.parseInt(rows.get(0).get("status"));
        }
        commentRepository.insert(commentRepository.insertVoteComment(
                new HashMap<String, String>() {{ put("userEmail", userEmail); put("tid", tid); put("status", status);}}
        ));
        if(prevStatus == Integer.parseInt(status)){
            return 0;
        } else if (rows.size() == 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public static double getDiscountFromCode(String code) throws DiscountCodeNotFoundException, SQLException {
        List<HashMap<String, String>> discount = discountRepository.select(
                new ArrayList<Object>() {{ add(code); }},
                discountRepository.getColNames(),
                discountRepository.selectOneStatement("code")
        );
        if (discount.isEmpty()) {
            throw new DiscountCodeNotFoundException("Discount code " + code + " is not exist");
        }
        return Double.parseDouble(discount.get(0).get("value")) / 100;
    }
    public static void updateUserCredit(String username, double amount) throws SQLException {
        String statement = userRepository.updateCreditStatement();
        List<Object> values = new ArrayList<Object>() {{add(amount); add(username);}};
        userRepository.update(statement, values);
    }

    public static boolean findDiscount(String username, String code) throws SQLException {
        List<HashMap<String, String>> rows = discountRepository.select(new ArrayList<Object>() {{ add(username); add(code);}},
                new ArrayList<String>() {{ add("uid"); add("code");}},
                discountRepository.findDiscountCodeStatement(username, code));
        return !rows.isEmpty();
    }

    public static void insertToUsedCode(String username, String code) throws SQLException {

        discountRepository.insert(discountRepository.insertUsedCodeStatement(
                new HashMap<String, String>() {{ put("uid", username); put("code", code);}}
        ));
    }

    public static void insertRating(String commodityId, String username, String score) throws SQLException {
        HashMap<String, String> values = new HashMap<>() {{
            put("cid", commodityId);
            put("username", username);
            put("rate", score);}};
        commodityRepository.insert(commodityRepository.insertRatingStatement(values));
    }

    public static List<String> getRatings(String commodityId) throws SQLException {
        List<Object> values = new ArrayList<Object>() {{add(commodityId);}};
        List<String> colNames = new ArrayList<String>() {{add("rate");}};
        List<HashMap<String, String>> ratings = commodityRepository.select(values, colNames, commodityRepository.selectRatingStatement());
        return ratings.stream()
                .flatMap(map -> map.values().stream())
                .collect(Collectors.toList());
    }

    public static void updateRating(String commodityId, double rating) throws SQLException {
        String statement = commodityRepository.updateRatingStatement();
        List<Object> values = new ArrayList<Object>() {{add(rating); add(commodityId);}};
        commodityRepository.update(statement, values);
    }

    public static void modifyInStock(String commodityId, int count) throws SQLException {
        String statement = commodityRepository.modifyInStockStatement();
        List<Object> values = new ArrayList<Object>() {{add(count); add(Integer.parseInt(commodityId));}};
        commodityRepository.update(statement, values);
    }

    public static List<Commodity> getSuggestedCommodities(int id) throws Exception {
        String statement = commodityRepository.getSuggestedCommoditiesStatement();
        List<String> colNames = commodityRepository.getColNames();
        List<HashMap<String, String>> commodities = commodityRepository.select(new ArrayList<>() {{add(id); add(id);}},
                colNames, statement);
        return castToList(commodities, Commodity.class);
    }
}
