package org.example.service;

import org.example.model.Movie;
import org.example.model.User;
import org.example.validator.RecommendationValidator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationValidatorTest {

    private Map<User, List<String>> generateSingleUserResult() {
        Movie ironMan = new Movie("Iron Man", "IM123", "Action");
        Movie inception = new Movie("Inception", "I123", "Sci-Fi");
        Movie interstellar = new Movie("Interstellar", "I789", "Sci-Fi, Thriller");

        List<Movie> movies = List.of(ironMan, inception, interstellar);

        User alice = new User("Alice", "12345678A");
        alice.addLikedMovie("I123");

        RecommendationValidator validator = new RecommendationValidator();
        return validator.generateRecommendations(List.of(alice), movies);
    }

    @Test
    void recommendsMovieWithCommonGenre() {
        Map<User, List<String>> result = generateSingleUserResult();
        List<String> recs = result.values().iterator().next();

        assertTrue(recs.contains("Interstellar"));
    }

    @Test
    void doesNotRecommendAlreadyLikedMovie() {
        Map<User, List<String>> result = generateSingleUserResult();
        List<String> recs = result.values().iterator().next();

        assertFalse(recs.contains("Inception"));
    }

    @Test
    void doesNotRecommendUnrelatedGenreMovie() {
        Map<User, List<String>> result = generateSingleUserResult();
        List<String> recs = result.values().iterator().next();

        assertFalse(recs.contains("Iron Man"));
    }

    @Test
    void noRecommendationsWhenUserLikesAllMovies() {
        Movie m1 = new Movie("Little Women","LM123","Romance, Drama");
        Movie m2 = new Movie("Good Will Hunting","GWH913","Drama");
        Movie m3 = new Movie("Interstellar","I789","Sci-Fi, Thriller");

        User alice = new User("Alice","12345678A");
        alice.addLikedMovie("LM123");
        alice.addLikedMovie("GWH913");
        alice.addLikedMovie("I789");

        RecommendationValidator validator = new RecommendationValidator();
        Map<User, List<String>> result =
                validator.generateRecommendations(List.of(alice), List.of(m1, m2, m3));

        assertTrue(result.get(alice).isEmpty());
    }

    @Test
    void noUsersReturnsEmptyResult() {
        RecommendationValidator validator = new RecommendationValidator();

        Map<User, List<String>> result =
                validator.generateRecommendations(List.of(), List.of());

        assertTrue(result.isEmpty());
    }
}
