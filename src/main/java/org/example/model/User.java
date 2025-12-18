package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String id;
    private List<String> likedMovieIds;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
        this.likedMovieIds = new ArrayList<>();
    }

    public void addLikedMovie(String movieId) {
        this.likedMovieIds.add(movieId);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<String> getLikedMovieIds() {
        return likedMovieIds;
    }
}