package com.voterra.services;

import com.voterra.entities.User;
import com.voterra.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
public class FriendService {

    @Autowired
    UserRepository userRepository ;
    private static final int PAGE_SIZE = 10;
    public List<User> getFriends(String email , int page){
        // get the user
        User user = userRepository.findById(email).orElseThrow(() -> new RuntimeException("User Not Found"));

        List<String> friendIds = user.getFriends();
        if (friendIds == null || friendIds.isEmpty()) {
            return new ArrayList<>();
        }

        // boundaries
        int fromIndex = page * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, friendIds.size());

        if (fromIndex >= friendIds.size()) {
            return new ArrayList<>();
        }

        // Get the sublist
        List<String> paginatedFriendIds = friendIds.subList(fromIndex, toIndex);

        // Fetch the friend User objects
        return userRepository.findAllById(paginatedFriendIds);
    }
    public void addFriend(String userEmail , String friendEmail){
        // get the user
        User user = userRepository.findById(userEmail).orElseThrow(() -> new RuntimeException("User Not Found"));
        //malicious user adding himself
        if(userEmail.equals(friendEmail)){
            throw new RuntimeException("User Not Found");
        }
        //initialize the list if it is empty
        if(user.getFriends() == null ){
            user.setFriends(new ArrayList<>());
        }
        //the friend does not exist
        if(!userRepository.existsById(friendEmail)){
            throw new RuntimeException("User Not Found");
        }
        //the friend is already a friend
        if(user.getFriends().contains(friendEmail)){
            throw new RuntimeException("User already a friend");
        }
        //if you add a friend it is like ig you just follow him and see his posts
        //not like facebook where u wait for him to accept
        //u also may follow someone without him following you back
        user.getFriends().add(friendEmail) ;
        userRepository.save(user) ;
    }
    public void removeFriend(String userEmail , String friendEmail){
        // get the user
        User user = userRepository.findById(userEmail).orElseThrow(() -> new RuntimeException("User Not Found"));
        //malicious user removing himself
        if(userEmail.equals(friendEmail)){
            throw new RuntimeException("User Not Found");
        }
        // the friend list is empty or the friendEmail is not in the friends list
        if(user.getFriends() == null
                || !(user.getFriends().contains(friendEmail))) {
            throw new RuntimeException("User Not Found");
        }
        user.getFriends().remove(friendEmail) ;
        userRepository.save(user) ;
    }
    public List<User> suggestFriends(String email, int page) {

        // Fetch the user and their direct friends
        User user = userRepository.findById(email).orElseThrow(() -> new RuntimeException("User Not Found"));
        List<String> visited = new ArrayList<>(); // To keep track of already visited users
        Queue<String> queue = new LinkedList<>();
        List<String> suggestions = new ArrayList<>();

        // Enqueue user's direct friends
        queue.addAll(user.getFriends());
        visited.add(email); // Mark the starting user as visited

        // BFS to explore friends of friends
        while (!queue.isEmpty() && suggestions.size() < (page + 1) * PAGE_SIZE) {
            String currentFriend = queue.poll();
            if (!visited.contains(currentFriend)) {
                visited.add(currentFriend);
                if(!user.getFriends().contains(currentFriend)){
                    suggestions.add(currentFriend);
                }
                // Fetch current friend's friends and enqueue them
                User friendUser = userRepository.findById(currentFriend).orElse(null);
                if (friendUser != null && friendUser.getFriends() != null) {
                    queue.addAll(friendUser.getFriends());
                }
            }
        }

        // Pagination
        int fromIndex = page * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, suggestions.size());
        if (fromIndex >= suggestions.size()) {
            return new ArrayList<>(); // Return empty list if the page is out of bounds
        }

        // Fetch User objects for the paginated suggestions
        List<String> paginatedSuggestions = suggestions.subList(fromIndex, toIndex);
        return userRepository.findAllById(paginatedSuggestions);
    }
}
