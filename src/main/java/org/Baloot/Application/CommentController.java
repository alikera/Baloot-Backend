//package org.Baloot.Application;
//
//import org.Baloot.Database.Database;
//import org.Baloot.Entities.Comment;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.Map;
//
//@RestController
//@CrossOrigin(origins = "http://localhost:3000")
//@RequestMapping("/api/comment")
//public class CommentController {
//    @RequestMapping(value = "/post/{id}", method = RequestMethod.POST)
//    public ResponseEntity<?> postComment(@PathVariable(value = "id") String id,
//                                         @RequestBody Map<String, String> body) {
//
//        String username = body.get("username");
//        String text = body.get("text");
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String currDate = LocalDate.now().format(formatter);
//        Comment comment = new Comment(username, Integer.parseInt(id), text, currDate);
//        Database.insertComment(comment);
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
//
////    @RequestMapping(value = "/vote/{id}", method = RequestMethod.PUT)
////    public ResponseEntity<?> likeOrDislike(@PathVariable (value = "id") String id,
////                                           @RequestBody Map<String, String> body) {
////
////        String username = body.get("username");
////        String text = body.get("text");
////        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
////        String currDate = LocalDate.now().format(formatter);
////        Comment comment = new Comment(username, Integer.parseInt(id), text, currDate);
////        Database.insertComment(comment);
////        return ResponseEntity.status(HttpStatus.OK).build();
////    }
//}
