package com.voterra.exceptions;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String id) {
        super("Post with ID " + id + " not found");
    }
}
