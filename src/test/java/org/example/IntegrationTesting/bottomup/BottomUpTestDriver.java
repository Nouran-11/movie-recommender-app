// File: src/test/java/org/example/IntegrationTesting/bottomup/BottomUpTestDriver.java
package org.example.IntegrationTesting.bottomup;

import org.example.io.FileHandler;
import org.example.model.Movie;
import org.example.model.User;
import org.example.service.Recommendation;
import java.io.*;
import java.util.List;

/**
 * Manual test driver to demonstrate bottom-up integration
 * Run this to see the integration flow from bottom to top
 */
public class BottomUpTestDriver {
    
    public static void main(String[] args) {
        System.out.println("=== MOVIE RECOMMENDER - BOTTOM-UP INTEGRATION TESTING ===\n");
        
        System.out.println("Starting from the BOTTOM (Models) and moving UP...\n");
        
        try {
            // STEP 1: Test the foundation (Models)
            System.out.println("STEP 1: Testing Models (Movie + User)...");
            testModels();
            System.out.println("✓ Models are working correctly\n");
            
            // STEP 2: Test FileHandler with Models
            System.out.println("STEP 2: Testing FileHandler with Models...");
            testFileHandlerWithModels();
            System.out.println("✓ FileHandler integrates with Models correctly\n");
            
            // STEP 3: Test Recommendation with everything
            System.out.println("STEP 3: Testing Recommendation with all lower layers...");
            testRecommendationIntegration();
            System.out.println("✓ Recommendation integrates with all lower layers\n");
            
            System.out.println("======================================================");
            System.out.println("✅ BOTTOM-UP INTEGRATION SUCCESSFUL!");
            System.out.println("All modules work together from bottom to top.");
            System.out.println("Ready for top-down testing!");
            
        } catch (Exception e) {
            System.err.println("\n❌ BOTTOM-UP INTEGRATION FAILED!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void testModels() {
        System.out.println("  Creating Movie and User objects...");
        
        // Test Movie creation and validation
        Movie movie = new Movie("The Matrix", "TM123", "Action,Sci-Fi");
        if (!Movie.isValidTitle(movie.getTitle())) {
            throw new RuntimeException("Movie title validation failed");
        }
        if (!Movie.isValidMovieId(movie.getId(), movie.getTitle())) {
            throw new RuntimeException("Movie ID validation failed");
        }
        
        // Test User creation and validation
        User user = new User("Alice Johnson", "12345678A");
        if (!User.isValidName(user.getName())) {
            throw new RuntimeException("User name validation failed");
        }
        if (!User.isValidUserId(user.getId())) {
            throw new RuntimeException("User ID validation failed");
        }
        
        // Test integration: User likes a Movie
        user.addLikedMovie(movie.getId());
        if (!user.getLikedMovieIds().contains(movie.getId())) {
            throw new RuntimeException("User-Movie integration failed");
        }
        
        System.out.println("  ✓ Movie created: " + movie.getTitle());
        System.out.println("  ✓ User created: " + user.getName());
        System.out.println("  ✓ User liked movie: " + movie.getId());
    }
    
    private static void testFileHandlerWithModels() throws Exception {
        System.out.println("  Testing FileHandler.readMovies()...");
        
        // Create a test movies file
        File moviesFile = createTestMoviesFile();
        List<Movie> movies = FileHandler.readMovies(moviesFile.getAbsolutePath());
        
        if (movies.isEmpty()) {
            throw new RuntimeException("FileHandler.readMovies() failed - no movies read");
        }
        System.out.println("  ✓ Read " + movies.size() + " movies from file");
        
        System.out.println("  Testing FileHandler.readUsers() with movie dependency...");
        
        // Create a test users file
        File usersFile = createTestUsersFile();
        List<User> users = FileHandler.readUsers(usersFile.getAbsolutePath(), movies);
        
        if (users.isEmpty()) {
            throw new RuntimeException("FileHandler.readUsers() failed - no users read");
        }
        System.out.println("  ✓ Read " + users.size() + " users from file");
        
        // Verify data integrity
        User user = users.get(0);
        if (user.getLikedMovieIds().isEmpty()) {
            throw new RuntimeException("User should have liked movies");
        }
        
        System.out.println("  ✓ Data integrity verified");
        
        // Cleanup
        moviesFile.delete();
        usersFile.delete();
    }
    
    private static void testRecommendationIntegration() throws IOException {
        System.out.println("  Creating test data for recommendation...");
        
        // Create test movies
        List<Movie> movies = List.of(
            new Movie("Action Movie", "AM123", "Action"),
            new Movie("Drama Movie", "DM456", "Drama"),
            new Movie("Action Drama", "AD789", "Action,Drama")
        );
        
        // Create test user who likes action movies
        User user = new User("Test User", "111111111");
        user.addLikedMovie("AM123"); // Likes Action Movie
        
        List<User> users = List.of(user);
        
        // Generate recommendations
        File outputFile = File.createTempFile("recommendations", ".txt");
        outputFile.deleteOnExit();
        
        Recommendation recommender = new Recommendation();
        recommender.generateRecommendations(users, movies, outputFile.getAbsolutePath());
        
        // Verify output
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String userLine = reader.readLine();
            String recommendations = reader.readLine();
            
            if (!userLine.contains("Test User")) {
                throw new RuntimeException("Recommendation output missing user info");
            }
            
            if (!recommendations.contains("Action Drama")) {
                throw new RuntimeException("Should recommend Action Drama (shared genre)");
            }
            
            if (recommendations.contains("Action Movie")) {
                throw new RuntimeException("Should not recommend already liked movie");
            }
        }
        
        System.out.println("  ✓ Recommendations generated successfully");
        System.out.println("  ✓ Output file created: " + outputFile.getAbsolutePath());
    }
    
    private static File createTestMoviesFile() throws IOException {
        File file = File.createTempFile("test_movies", ".txt");
        file.deleteOnExit();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("The Matrix,TM123\n");
            writer.write("Action,Sci-Fi\n");
        }
        return file;
    }
    
    private static File createTestUsersFile() throws IOException {
        File file = File.createTempFile("test_users", ".txt");
        file.deleteOnExit();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Test User,111111111\n");
            writer.write("TM123\n");
        }
        return file;
    }
}