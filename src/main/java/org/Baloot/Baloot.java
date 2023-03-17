package org.Baloot;

import org.Baloot.Database.Database;
import org.Baloot.Entities.Comment;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;
import org.Baloot.Entities.User;
import org.Baloot.Exception.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Baloot {
    private Database db;
    public Baloot(Database _db) throws ProviderNotFoundException {
        db = _db;
        for (Commodity commodity : db.getCommodities()) {
            addToProviderCommodityList(commodity);
        }
    }

    public Commodity findByCommodityId(int commodityId) throws CommodityNotFoundException {
        for (Commodity commodity : db.getCommodities()) {
            if (commodity.getId() == commodityId) {
                return commodity;
            }
        }
        throw new CommodityNotFoundException("Couldn't find commodity with the given Id!");
    }

    public List<Commodity> getCommoditiesByPriceRange(String _startPrice, String _endPrice) throws NumberFormatException {
        double startPrice = Double.parseDouble(_startPrice);
        double endPrice = Double.parseDouble(_endPrice);

        List<Commodity> filteredCommodities = new ArrayList<>();
        for (Commodity commodity : db.getCommodities()) {
            if (commodity.getPrice() >= startPrice && commodity.getPrice() <= endPrice) {
                filteredCommodities.add(commodity);
            }
        }
        return filteredCommodities;
    }
    public List<Commodity> getCommoditiesByCategory(String category) {
        List<Commodity> filteredCommodities = new ArrayList<>();
        for (Commodity commodity : db.getCommodities()) {
            if (commodity.isInCategoryGiven(category)) {
                filteredCommodities.add(commodity);
            }
        }
        return filteredCommodities;
    }

    public List<Comment> getCommentsByCommodityId(int commodityId){
        List<Comment> filteredComments = new ArrayList<>();
        for (Comment comment : db.getComments()) {
            if (comment.getCommodityId() == commodityId) {
                filteredComments.add(comment);
            }
        }
        return filteredComments;
    }
    public User findByUsername(String username) throws UserNotFoundException {
        for (User user : db.getUsers()) {
            if (Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }
        throw new UserNotFoundException("Couldn't find user with the given Username!");
    }

    public String findUsernameByUserEmail(String email) throws UserNotFoundException {
        for (User user : db.getUsers()) {
            if (Objects.equals(user.getEmail(), email)) {
                return user.getUsername();
            }
        }
        throw new UserNotFoundException("Couldn't find user with the given Username!");
    }
    public Provider findByProviderId(int providerId) throws ProviderNotFoundException {
        for (Provider provider : db.getProviders()) {
            if (Objects.equals(provider.getId(), providerId)) {
                return provider;
            }
        }
        throw new ProviderNotFoundException("Couldn't find provider with the given Id!");
    }

    private void addToProviderCommodityList(Commodity commodity) {
        for(Provider provider : db.getProviders()){
            if(Objects.equals(provider.getId(), commodity.getProviderId())){
                provider.addToCommodities(commodity);
            }
        }
    }

//    private List<Commodity> findCommoditiesByCategory(String category){
//        List<Commodity> foundedCommodities = new ArrayList<>();
//        for (Commodity commodity : db.getCommodities()) {
//            Set<String> categories = commodity.getCategories();
//            if (categories.contains(category)) {
//                foundedCommodities.add(commodity);
//            }
//        }
//        return foundedCommodities;
//    }
//    public void addUser(User user) throws JsonProcessingException {
//        Pattern pattern = Pattern.compile("[0-9a-zA-Z]+");
//        Matcher matcher = pattern.matcher(user.getUsername());
//        try {
//            if (matcher.matches()) {
//                User foundUser = findByUsername(user.getUsername());
//                foundUser.modifyFields(user);
//                return makeJsonFromString(true, "User modified successfully");
//            } else {
//                throw new InvalidUsernameException("Invalid Username!");
//            }
//        }
//        catch (UserNotFoundException e){
//            db.insertUser(user);
//            return makeJsonFromString(true, "User added successfully");
//        }
//        catch (InvalidUsernameException e) {
//            return makeJsonFromString(false, e.getMessage());
//        }
//    }
//
//    public ObjectNode addProvider(Provider provider) throws JsonProcessingException {
//        db.insertProvider(provider);
//        return makeJsonFromString(true, "Provider added successfully");
//    }
//
//    public ObjectNode addCommodity(Commodity commodity) throws JsonProcessingException {
//        addToProviderCommodityList(commodity);
//        db.insertCommodity(commodity);
//        return makeJsonFromString(true, "Commodity added successfully");
//    }

//    public ObjectNode getCommoditiesList() {
//        ObjectMapper mapper = new ObjectMapper();
//        List<ObjectNode> commodityNodes = new ArrayList<>();
//        for(Commodity commodity : db.getCommodities()) {
//            commodityNodes.add(commodity.toJson());
//        }
//        ObjectNode mainNode = mapper.createObjectNode();
//        mainNode.putPOJO("commoditiesList", commodityNodes);
//
//        return makeJsonFromObjectNode(true, mainNode);
//    }
//
//    public ObjectNode rateCommodity(ObjectNode node) {
//        try {
//            Commodity commodityFound = findByCommodityId(node.get("commodityId").asInt());
//            User userFound = findByUsername(node.get("username").asText());
//            commodityFound.rateCommodity(node.get("username").asText(), node.get("score").asInt());
//            return makeJsonFromString(true, "User rated commodity successfully");
//        } catch (ExceptionHandler e) {
//            return makeJsonFromString(false, e.getMessage());
//        }
//    }

//    public ObjectNode addToUserBuyList(ObjectNode node) throws ExceptionHandler {
//        try {
//            Commodity commodityFound = findByCommodityId(node.get("commodityId").asInt());
//
//            if (commodityFound.getInStock() == 0) {
//                throw new OutOfStockException("Commodity out of stock!");
//            }
//            commodityFound.decreaseInStock();
//            User userFound = findByUsername(node.get("username").asText());
//            userFound.addToBuyList(node.get("commodityId").asInt());
//            return makeJsonFromString(true, "Commodity added to user buy list successfully");
//        }
//        catch (CommodityNotFoundException e) {
//            return makeJsonFromString(false, e.getMessage());
//        }
//        catch (CommodityExistenceException e) {
//            return makeJsonFromString(false, e.getMessage());
//        }
//        catch (UserNotFoundException e) {
//            return makeJsonFromString(false, e.getMessage());
//        }
//        catch (OutOfStockException e) {
//            return makeJsonFromString(false, e.getMessage());
//        }
//    }

//    public ObjectNode removeFromUserBuyList(ObjectNode node) throws ExceptionHandler {
//        try {
//            Commodity commodityFound = findByCommodityId(node.get("commodityId").asInt());
//            User userFound = findByUsername(node.get("username").asText());
//            userFound.removeFromBuyList(node.get("commodityId").asInt());
//            return makeJsonFromString(true, "Commodity removed from user buy list successfully");
//        }
//        catch (CommodityNotFoundException e) {
//            return makeJsonFromString(false, "Commodity does not exists in your BuyList!");
//        }
//        catch (CommodityExistenceException e) {
//            return makeJsonFromString(false, e.getMessage());
//        }
//        catch (UserNotFoundException e) {
//            return makeJsonFromString(false, e.getMessage());
//        }
//    }
//
//    public ObjectNode getCommodityById(int id) throws ExceptionHandler {
//        try {
//            Commodity commodity = findByCommodityId(id);
//            ObjectNode commodityNode = commodity.toJson();
//            commodityNode.remove("inStock");
//            return makeJsonFromObjectNode(true, commodityNode);
//        }
//        catch (CommodityNotFoundException e) {
//            return makeJsonFromString(false, e.getMessage());
//        }
//    }

//    public ObjectNode getCommodityByCategory(String category) {
//        List<Commodity> foundedCommodities = findCommoditiesByCategory(category);
//
//        ObjectMapper mapper = new ObjectMapper();
//        List<ObjectNode> commodityNodes = new ArrayList<>();
//        for(Commodity commodity : foundedCommodities) {
//            ObjectNode commodityNode = commodity.toJson();
//            commodityNode.remove("inStock");
//            commodityNodes.add(commodityNode);
//        }
//        ObjectNode mainNode = mapper.createObjectNode();
//        mainNode.putPOJO("commoditiesListByCategory", commodityNodes);
//
//        return makeJsonFromObjectNode(true, mainNode);
//    }
//
//    public ObjectNode getBuyList(String username) throws ExceptionHandler {
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            User user = findByUsername(username);
//            Set<Integer> buyListIds = user.getBuyList();
//            List<ObjectNode> commodityNodes = new ArrayList<>();
//            for (int commodityId : buyListIds) {
//                Commodity commodity = findByCommodityId(commodityId);
//                ObjectNode commodityNode = commodity.toJson();
//                commodityNode.remove("inStock");
//                commodityNodes.add(commodityNode);
//            }
//            ObjectNode mainNode = mapper.createObjectNode();
//            mainNode.putPOJO("buyList", commodityNodes);
//
//            return makeJsonFromObjectNode(true, mainNode);
//        }
//        catch (UserNotFoundException e) {
//            return makeJsonFromString(false, e.getMessage());
//        }
//    }
//   public ObjectNode makeJsonFromObjectNode(Boolean success, ObjectNode dataNode) {
//        ObjectMapper mapper = new ObjectMapper();
//        ObjectNode node = mapper.createObjectNode();
//        node.put("success", success);
//        node.put("data", dataNode);
//        return node;
//    }
//
//    public ObjectNode makeJsonFromString(Boolean success, String data) {
//        ObjectMapper mapper = new ObjectMapper();
//        ObjectNode node = mapper.createObjectNode();
//        node.put("success", success);
//        node.put("data", data);
//        return node;
//    }

    public void addCredit(String username, String credit) throws UserNotFoundException, NegativeAmountException {
        double amount = Double.parseDouble(credit);

        User user = findByUsername(username);
        if (amount <= 0) {
            throw new NegativeAmountException();
        }
        user.increaseCredit(amount);
    }
    public void finalizePayment(String username) throws UserNotFoundException, NotEnoughCreditException, CommodityNotFoundException {
        User user = findByUsername(username);
        Set<Integer> commoditiesId = user.getBuyList();
        double cost = 0;

        for (Integer id: commoditiesId) {
            Commodity commodity = findByCommodityId(id);
            cost += commodity.getPrice();
        }
        user.moveBuyToPurchased(cost);
    }

    public void voteComment(String userId, String commodityId, String vote) throws UserNotFoundException, CommodityNotFoundException, InvalidVoteException {
        if (!Objects.equals(vote, "0") && !Objects.equals(vote, "1") && !Objects.equals(vote, "-1")) {
            throw new InvalidVoteException("Invalid Vote");
        }
        User user = findByUsername(userId);
        Commodity commodity = findByCommodityId(Integer.parseInt(commodityId));
        for (Comment comment : db.getComments()){
            if(comment.getCommodityId() == commodity.getId()){
                comment.voteComment(userId, Integer.parseInt(vote));
            }
        }
    }

    public void addCommodityToUserBuyList(String userId, String commodityId) throws CommodityNotFoundException, OutOfStockException, UserNotFoundException, CommodityExistenceException {
        Commodity commodityFound = findByCommodityId(Integer.parseInt(commodityId));
        if (commodityFound.getInStock() == 0) {
            throw new OutOfStockException("Commodity out of stock!");
        }
        User userFound = findByUsername(userId);
        userFound.addToBuyList(Integer.parseInt(commodityId));
        commodityFound.decreaseInStock();
    }

    public void removeCommodityFromUserBuyList(String userId, String commodityId) throws CommodityNotFoundException, UserNotFoundException, CommodityExistenceException {
        Commodity commodityFound = findByCommodityId(Integer.parseInt(commodityId));
        User userFound = findByUsername(userId);
        userFound.removeFromBuyList(Integer.parseInt(commodityId));
        commodityFound.increaseInStock();
    }

    public void rateCommodity(String userId, String commodityId, String rate) throws CommodityNotFoundException, UserNotFoundException, InvalidRatingException, NumberFormatException {
        Commodity commodityFound = findByCommodityId(Integer.parseInt(commodityId));
        User userFound = findByUsername(userId);
        commodityFound.rateCommodity(userId, Integer.parseInt(rate));
    }
    public List<User> getUsers() {
        return db.getUsers();
    }
    public List<Provider> getProviders() {
        return db.getProviders();
    }
    public List<Commodity> getCommodities() {
        return db.getCommodities();
    }


}
