package com.voterra.controllers;

import com.voterra.entities.User;
import com.voterra.services.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.* ;

import java.util.Map;

@RestController
@CrossOrigin(
        origins = {"*"}
)
@RequestMapping("/friends")
public class FriendController {
    @Autowired
    FriendService friendService ;

    @PostMapping("/addFriend")
    public ResponseEntity<?> addFriend(@RequestParam String friendEmail){
        try{
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName(); //get the user from the token
            friendService.addFriend(userEmail , friendEmail) ;
            return ResponseEntity.ok().body(Map.of("message" , "friend added")) ;
        }catch (Exception e){
            return ResponseEntity.status(400).body(Map.of("message" , "user not found")) ;
        }
    }
    @DeleteMapping("/removeFriend")
    public ResponseEntity<?> removeFriend(@RequestParam String friendEmail){
        try{
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();//get the user from the token
            friendService.removeFriend(userEmail , friendEmail) ;
            return ResponseEntity.ok().body(Map.of("message" , "friend removed")) ;
        }catch (Exception e){
            return ResponseEntity.status(400).body(Map.of("message" , "user not found")) ;
        }
    }
    @GetMapping
    public ResponseEntity<?> getFriends(@RequestParam int page) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();//get the user from the token
            List<User> friends = friendService.getFriends(userEmail, page);
            return ResponseEntity.ok().body(friends);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", "user not found"));
        }
    }
    @GetMapping("/suggest")
    public ResponseEntity<?> suggestFriends(@RequestParam int page) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName(); //get the user from the token
            List<User> suggestedFriends = friendService.suggestFriends(userEmail, page);
            return ResponseEntity.ok().body(suggestedFriends);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", "user not found"));
        }
    }
}
