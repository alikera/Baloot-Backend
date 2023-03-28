package org.Baloot.Managers;

import org.Baloot.Database.Database;
import org.Baloot.Entities.Comment;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.User;
import org.Baloot.Exception.CommodityNotFoundException;
import org.Baloot.Exception.InvalidVoteException;
import org.Baloot.Exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentManager {
    private Database db;
    public CommentManager(Database _db){
        db = _db;
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
    public void voteComment(String userId, String commodityId, String vote) throws UserNotFoundException, CommodityNotFoundException, InvalidVoteException {
        if (!Objects.equals(vote, "0") && !Objects.equals(vote, "1") && !Objects.equals(vote, "-1")) {
            throw new InvalidVoteException("Invalid Vote");
        }
        User user = db.findByUsername(userId);
        Commodity commodity = db.findByCommodityId(Integer.parseInt(commodityId));
        for (Comment comment : db.getComments()){
            if(comment.getCommodityId() == commodity.getId()){
                comment.voteComment(userId, Integer.parseInt(vote));
            }
        }
    }
}
