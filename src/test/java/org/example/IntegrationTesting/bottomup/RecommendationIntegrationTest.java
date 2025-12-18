// File: src/test/java/org/example/IntegrationTesting/bottomup/RecommendationIntegrationTest.java
package org.example.IntegrationTesting.bottomup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.example.model.Movie;
import org.example.model.User;
import org.example.service.Recommendation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RecommendationIntegrationTest {
    
    @Test
    @DisplayName("Bottom-Up: Recommendation with real Movie and User data")
    void testRecommendationWithRealData() throws IOException {
        // Arrange: Create real Movie and User objects
        List<Movie> movies = List.of(
            new Movie("The Matrix", "TM123", "Action,Sci-Fi"),
            new Movie("Inception", "I456", "Action,Thriller"),
            new Movie("Parasite", "P789", "Drama,Fun"),
            new Movie("The Nun", "TN345", "Horror,Action")
        );
        
        User alice = new User("Alice Johnson", "12345678A");
        alice.addLikedMovie("TM123"); // Likes The Matrix
        alice.addLikedMovie("I456");  // Likes Inception
        
        User bob = new User("Bob Smith", "234567890");
        bob.addLikedMovie("P789");    // Likes Parasite
        
        List<User> users = List.of(alice, bob);
        
        // Act: Generate recommendations (using all lower-level modules)
        File outputFile = File.createTempFile("recommendations", ".txt");
        outputFile.deleteOnExit();
        
        Recommendation recommender = new Recommendation();
        recommender.generateRecommendations(users, movies, outputFile.getAbsolutePath());
        
        // Assert: Verify the output
        List<String> lines = Files.readAllLines(outputFile.toPath());
        
        assertEquals(4, lines.size(), "Should have 2 users × 2 lines each");
        
        // Check Alice's recommendations
        assertEquals("Alice Johnson, 12345678A", lines.get(0));
        String aliceRecs = lines.get(1);
        assertTrue(aliceRecs.contains("The Nun"), 
            "Alice should get 'The Nun' (shared Action genre with her liked movies)");
        assertFalse(aliceRecs.contains("The Matrix") || aliceRecs.contains("Inception"),
            "Should not recommend already liked movies");
        assertFalse(aliceRecs.contains("Parasite"),
            "Should not recommend movies from unrelated genres");
        
        // Check Bob's recommendations
        assertEquals("Bob Smith, 234567890", lines.get(2));
        String bobRecs = lines.get(3);
        // Bob likes Drama,Fun - should get movies with these genres
    }
    
    @Test
    @DisplayName("Bottom-Up: Recommendation with empty data")
    void testRecommendationWithEmptyData() throws IOException {
        // Arrange: Empty data
        List<Movie> movies = List.of();
        List<User> users = List.of();
        
        // Act
        File outputFile = File.createTempFile("recommendations", ".txt");
        outputFile.deleteOnExit();
        
        Recommendation recommender = new Recommendation();
        recommender.generateRecommendations(users, movies, outputFile.getAbsolutePath());
        
        // Assert
        List<String> lines = Files.readAllLines(outputFile.toPath());
        assertTrue(lines.isEmpty(), "Should produce empty output for empty input");
    }
}