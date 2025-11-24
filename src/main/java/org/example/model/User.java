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

    public String getName() { return name; }
    public String getId() { return id; }
    public List<String> getLikedMovieIds() { return likedMovieIds; }


    public static boolean isValidName(String name) {
        if (name == null || name.isEmpty()) return false;
        return name.matches("^[a-zA-Z][a-zA-Z\\s]*$");
    }

    public static boolean isValidUserId(String id) {
        if (id == null || id.length() != 9) return false;

        if (!id.matches("[a-zA-Z0-9]+")) return false;

        if (!Character.isDigit(id.charAt(0))) return false;


        char lastChar = id.charAt(8);
        boolean lastIsLetter = Character.isLetter(lastChar);

        if (lastIsLetter) {
            if (Character.isLetter(id.charAt(7))) return false;
        }

        return true;
    }
}