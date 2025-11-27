package org.example.model;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Movie {
    private String title;
    private String id;
    private String genre;

    public Movie(String title, String id, String genre) {
        this.title = title;
        this.id = id;
        this.genre = genre;
    }

    public String getTitle() { return title; }
    public String getId() { return id; }
    public String getGenre() { return genre; }


    public static boolean isValidTitle(String title) {
        if (title == null || title.isEmpty()) return false;

        String[] words = title.split(" ");
        for (String word : words) {
            if (word.isEmpty()) continue;
            if (!Character.isUpperCase(word.charAt(0))) {
                return false;
            }
        }
        return true;
    }

    public static String validateId(String id, String title) {
        if (id == null || id.length() < 4) return "Error in movie id letters";

        StringBuilder capsInTitle = new StringBuilder();
        for (char c : title.toCharArray()) {
            if (Character.isUpperCase(c)) {
                capsInTitle.append(c);
            }
        }
        String expectedPrefix = capsInTitle.toString();

        if (!id.startsWith(expectedPrefix)) {
            return "Error in movie id letters";
        }

        String suffix = id.substring(expectedPrefix.length());

        if (suffix.length() != 3 || !suffix.matches("\\d{3}")) {
            return "Error in movie id unique numbers";
        }

        Set<Character> uniqueDigits = new HashSet<>();
        for (char c : suffix.toCharArray()) {
            uniqueDigits.add(c);
        }
        if (uniqueDigits.size() < 3) {
            return "Error in movie id unique numbers";
        }

        return "Valid";
    }
    //Handles the true/False isValidMovieID "BOOLEAN wrapper"
    public static boolean isValidMovieId(String id, String title) {
        return validateId(id, title).equals("Valid");
    }
}