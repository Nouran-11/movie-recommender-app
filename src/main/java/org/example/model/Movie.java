package org.example.model;

public class Movie {
    private String title;
    private String id;
    private String genre;

    public Movie(String title, String id, String genre) {
        this.title = title;
        this.id = id;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

}