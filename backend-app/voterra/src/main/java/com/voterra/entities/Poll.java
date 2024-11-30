package com.voterra.entities;

import java.util.List;

public class Poll {
    private String pollContent;
    private List<String> voters;

    public Poll(String pollContent, List<String> voters) {
        this.pollContent = pollContent;
        this.voters = voters;
    }
    public String getPollContent() {
        return pollContent;
    }
    public List<String> getVoters() {
        return voters;
    }
}
