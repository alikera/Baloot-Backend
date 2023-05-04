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
    public CommentManager(){

    }
    public List<Comment> getCommentsByCommodityId(int commodityId){
        List<Comment> filteredComments = new ArrayList<>();
        for (Comment comment : Database.getComments()) {
            if (comment.getCommodityId() == commodityId) {
                filteredComments.add(comment);
            }
        }
        return filteredComments;
    }
    public int voteComment(String userId, String commodityId, String commentId, String vote) throws UserNotFoundException, CommodityNotFoundException, InvalidVoteException {
        if (!Objects.equals(vote, "0") && !Objects.equals(vote, "1") && !Objects.equals(vote, "-1")) {
            throw new InvalidVoteException("Invalid Vote");
        }
        User user = Database.findByUsername(userId);
        Commodity commodity = Database.findByCommodityId(Integer.parseInt(commodityId));
        System.out.println(userId);
        for (Comment comment : Database.getComments()){
            if(comment.getId() == Integer.parseInt(commentId)){
                return comment.voteComment(userId, Integer.parseInt(vote));
            }
        }
        throw new CommodityNotFoundException("");
    }
}
