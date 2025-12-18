// File: src/test/java/org/example/IntegrationTesting/bottomup/FileHandlerIntegrationTest.java
package org.example.IntegrationTesting.bottomup;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.example.io.FileHandler;
import org.example.model.Movie;
import org.example.model.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileHandlerIntegrationTest {
                                           
    
    private File createTempFile(String content) throws IOException {
        File temp = File.createTempFile("test", ".txt");
        temp.deleteOnExit(); // Clean up after test
        try (FileWriter writer = new FileWriter(temp)) {
            writer.write(content);
        }
        return temp;
    }
    
    @Test
    @DisplayName("Bottom-Up: FileHandler reads Movies with Movie validation")
    void testReadMoviesWithMovieValidation() throws Exception {
        // Arrange: Create a movies file
        String content = "The Matrix,TM123\n" +
                        "Action,Sci-Fi\n" +
                        "Inception,I456\n" +
                        "Action,Thriller\n";
        
        File moviesFile = createTempFile(content);
        


        // Act: FileHandler uses Movie validation internally
        List<Movie> movies = FileHandler.readMovies(moviesFile.getAbsolutePath());
        
        // Assert: Verify integration worked
        assertEquals(2, movies.size(), "Should read 2 movies");
        
        // Test Movie 1 - integration of FileHandler + Movie validation
        Movie movie1 = movies.get(0);
        assertEquals("The Matrix", movie1.getTitle());
        assertEquals("TM123", movie1.getId());
        assertTrue(Movie.isValidTitle(movie1.getTitle()), "Title should be valid");
        assertTrue(Movie.isValidMovieId(movie1.getId(), movie1.getTitle()), "ID should be valid");
        
        // Test Movie 2
        Movie movie2 = movies.get(1);
        assertEquals("Inception", movie2.getTitle());
        assertEquals("I456", movie2.getId());
    }
    
    @Test
    @DisplayName("Bottom-Up: FileHandler reads Users with User validation and Movie dependency")
    void testReadUsersWithValidations() throws Exception {
        // Arrange: First create some movies (lower level)
        List<Movie> movies = List.of(
            new Movie("The Matrix", "TM123", "Action,Sci-Fi"),
            new Movie("Inception", "I456", "Action,Thriller")
        );
        
        // Create users file that references those movies
        String usersContent = "Alice Johnson,12345678A\n" +
                             "TM123,I456\n" +
                             "Bob Smith,234567890\n" +
                             "TM123\n";
        
        File usersFile = createTempFile(usersContent);
        
        // Act: FileHandler uses User validation and checks Movie existence
        List<User> users = FileHandler.readUsers(usersFile.getAbsolutePath(), movies);
        
        // Assert: Verify all integrations worked
        assertEquals(2, users.size(), "Should read 2 users");
        
        // Test User 1
        User user1 = users.get(0);
        assertEquals("Alice Johnson", user1.getName());
        assertEquals("12345678A", user1.getId());
        assertTrue(User.isValidName(user1.getName()), "Name should be valid");
        assertTrue(User.isValidUserId(user1.getId()), "User ID should be valid");
        assertEquals(2, user1.getLikedMovieIds().size(), "Should have 2 liked movies");
        
        // Test User 2
        User user2 = users.get(1);
        assertEquals("Bob Smith", user2.getName());
        assertEquals("234567890", user2.getId());
        assertEquals(1, user2.getLikedMovieIds().size(), "Should have 1 liked movie");
    }
    
    @Test
    @DisplayName("Bottom-Up: FileHandler detects invalid movie reference")
    void testInvalidMovieReferenceDetection() throws Exception {
        // Arrange: Create movies
        List<Movie> movies = List.of(
            new Movie("The Matrix", "TM123", "Action,Sci-Fi")
        );
        
        // User tries to like a non-existent movie
        String usersContent = "Charlie Brown,345678901\n" +
                             "INVALID999\n"; // This movie doesn't exist
        
        File usersFile = createTempFile(usersContent);
        
        // Act & Assert: FileHandler should throw exception when movie doesn't exist
        Exception exception = assertThrows(Exception.class, () -> {
            FileHandler.readUsers(usersFile.getAbsolutePath(), movies);
        });
        
        assertTrue(exception.getMessage().contains("does not exist"), 
            "Should detect non-existent movie reference");
    }
}