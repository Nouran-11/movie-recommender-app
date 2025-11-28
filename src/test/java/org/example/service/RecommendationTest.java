package org.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.example.model.Movie;
import org.example.model.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class RecommendationTest {

    @Test  //single user test with one other common liked genre movie
    void testSingleUserRecommendation() throws IOException {
        Movie ironMan = new Movie("Iron Man", "IM123", "Action");
        Movie inception = new Movie("Inception", "I123", "Sci-Fi");
        Movie interstellar = new Movie("Interstellar", "I789", "Sci-Fi, Thriller");
        List<Movie> movieList = List.of(ironMan, inception, interstellar);
        User alice = new User("Alice", "12345678A");
        List<User> userList = List.of(alice);
        alice.addLikedMovie("I123");
        Recommendation recommendation = new Recommendation();
        Path outputPath = Files.createTempFile("recommendations", ".txt");
        recommendation.generateRecommendations(userList, movieList, outputPath.toString());
        List<String> lines = Files.readAllLines(outputPath);

        //start checking the test case
        assertEquals("Alice, 12345678A", lines.get(0));
        String aliceRecommendations = lines.get(1);
        assertTrue(aliceRecommendations.contains("Interstellar"));
        assertFalse(aliceRecommendations.contains("Inception"), "Should not recommend already liked movie");
        assertFalse(aliceRecommendations.contains("Iron Man"), "Should not recommend movies from unrelated genres");
    }

    @Test  //multi users test with one other common liked genre movie
    void testMultipleUsersRecommendation() throws IOException {
        Movie ironMan = new Movie("Iron Man", "IM123", "Action");
        Movie inception = new Movie("Inception", "I123", "Sci-Fi");
        Movie interstellar = new Movie("Interstellar", "I789", "Sci-Fi, Thriller");
        List<Movie> movieList = List.of(ironMan, inception, interstellar);
        User alice = new User("Alice","12345678A");
        User bob = new User("Bob","87654321B");
        User charlie = new User("Charlie","56781234C");
        List<User> userList = List.of(alice, bob, charlie);
        alice.addLikedMovie("I123");
        bob.addLikedMovie("IM123");
        bob.addLikedMovie("I123");
        charlie.addLikedMovie("IM123");
        Recommendation recommendation = new Recommendation();
        Path outputPath = Files.createTempFile("recommendations", ".txt");
        recommendation.generateRecommendations(userList, movieList, outputPath.toString());
        List<String> lines = Files.readAllLines(outputPath);

        //start checking the test case
        assertEquals("Alice, 12345678A", lines.get(0));
        String aliceRecommendations = lines.get(1);
        assertTrue(aliceRecommendations.contains("Interstellar"));
        assertFalse(aliceRecommendations.contains("Inception"), "Should not recommend already liked movie");
        assertFalse(aliceRecommendations.contains("Iron Man"), "Should not recommend movies from unrelated genres");
    }

    @Test   //user that likes every movie
    void testNoRecommendationsWhenUserLikedAllMovies() throws IOException {
        Movie littleWomen = new Movie("Little Women","LM123","Romance, Drama");
        Movie goodWillHunting = new Movie("Good Will Hunting","GWH913","Drama");
        Movie interstellar = new Movie("Interstellar","I789","Sci-Fi, Thriller");
        List<Movie> movieList = List.of(littleWomen, goodWillHunting, interstellar);
        User alice = new User("Alice","12345678A");
        User bob = new User("Bob","87654321B");
        User charlie = new User("Charlie","56781234C");
        List<User> userList = List.of(alice, bob, charlie);
        alice.addLikedMovie("LM123");
        alice.addLikedMovie("GWH913");
        alice.addLikedMovie("I789");
        bob.addLikedMovie("LM123");
        charlie.addLikedMovie("GWH913");
        Recommendation recommendation = new Recommendation();
        Path outputPath = Files.createTempFile("recommendations", ".txt");
        recommendation.generateRecommendations(userList, movieList, outputPath.toString());
        List<String> lines = Files.readAllLines(outputPath);

        //start checking the test case
        assertEquals("Alice, 12345678A", lines.get(0));

        String aliceRecommendations = lines.get(1);
        assertEquals("", aliceRecommendations, "Should not recommend any movies as user likes all");
    }

    @Test
    void testMoviesExistButNoUsers() throws IOException {
        Movie ironMan = new Movie("Iron Man", "IM123", "Action");
        Movie inception = new Movie("Inception", "I123", "Sci-Fi");
        Movie interstellar = new Movie("Interstellar", "I789", "Sci-Fi, Thriller");
        List<Movie> movieList = List.of(ironMan, inception, interstellar);

        List<User> userList = List.of();

        Recommendation recommendation = new Recommendation();
        Path outputPath = Files.createTempFile("recommendations", ".txt");

        recommendation.generateRecommendations(userList, movieList, outputPath.toString());

        List<String> lines = Files.readAllLines(outputPath);

        assertTrue(lines.isEmpty(), "Output file should be empty when there are no users");
    }

    @Test
    void testNoUsersAndNoMoviesExist() throws IOException {
        List<User> userList = List.of();
        List<Movie> movieList = List.of();

        Recommendation recommendation = new Recommendation();
        Path outputPath = Files.createTempFile("recommendations", ".txt");

        recommendation.generateRecommendations(userList, movieList, outputPath.toString());

        List<String> lines = Files.readAllLines(outputPath);

        assertTrue(lines.isEmpty(), "Output file should be empty when there are no users");
    }
}
