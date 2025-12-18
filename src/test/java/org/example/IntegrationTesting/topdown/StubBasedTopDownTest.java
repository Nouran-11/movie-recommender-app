// File: src/test/java/org/example/IntegrationTesting/topdown/StubBasedTopDownTest.java
package org.example.IntegrationTesting.topdown;

import java.util.List;

import org.example.IntegrationTesting.topdown.stubs.MainStubs;
import org.example.model.Movie;
import org.example.model.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * TOP-DOWN integration tests using STUBS
 * Tests Main from the top down with stubbed dependencies
 */
class StubBasedTopDownTest {
    
    private MainStubs.FileHandlerStub fileHandlerStub;
    private MainStubs.RecommendationStub recommendationStub;
    private TestableMain testableMain;
    
    @BeforeEach
    void setUp() {
        fileHandlerStub = new MainStubs.FileHandlerStub();
        recommendationStub = new MainStubs.RecommendationStub();
        testableMain = new TestableMain(fileHandlerStub, recommendationStub);
    }
    
    @Test
    @DisplayName("Top-Down with Stubs: Main completes successfully")
    void testMainSuccessWithStubs() {
        // Arrange: Configure stubs to return valid data
        List<Movie> testMovies = List.of(
            new Movie("The Matrix", "TM123", "Action,Sci-Fi"),
            new Movie("Inception", "I456", "Action,Thriller")
        );
        
        User testUser = new User("Alice Johnson", "12345678A");
        testUser.addLikedMovie("TM123");
        List<User> testUsers = List.of(testUser);
        
        fileHandlerStub.setMoviesToReturn(testMovies);
        fileHandlerStub.setUsersToReturn(testUsers);
        
        // Act: Run Main logic
        testableMain.run("movies.txt", "users.txt", "recommendations.txt");
        
        // Assert: Verify top-down flow
        assertEquals(1, fileHandlerStub.getReadMoviesCallCount(), 
            "FileHandler.readMovies() should be called once");
        assertEquals(1, fileHandlerStub.getReadUsersCallCount(),
            "FileHandler.readUsers() should be called once");
        
        assertTrue(recommendationStub.wasCalled(),
            "Recommendation.generateRecommendations() should be called");
        
        assertEquals("recommendations.txt", recommendationStub.getLastOutputPath(),
            "Correct output path should be passed to Recommendation");
        
        assertEquals(testUsers, recommendationStub.getLastUsers(),
            "Correct users should be passed to Recommendation");
        assertEquals(testMovies, recommendationStub.getLastMovies(),
            "Correct movies should be passed to Recommendation");
        
        assertNull(fileHandlerStub.getLastErrorWritten(),
            "No error should be written on success");
    }
    
    @Test
    @DisplayName("Top-Down with Stubs: Main handles FileHandler errors")
    void testMainFileHandlerErrorWithStubs() {
        // Arrange: Configure FileHandler stub to throw an exception
        Exception testException = new Exception("ERROR: Movie Title Test Movie is wrong");
        fileHandlerStub.setExceptionToThrow(testException);
        
        // Act: Run Main logic (should catch the error)
        testableMain.run("movies.txt", "users.txt", "recommendations.txt");
        
        // Assert: Verify error handling
        assertEquals(1, fileHandlerStub.getReadMoviesCallCount(),
            "FileHandler.readMovies() should be called (and fail)");
        assertEquals(0, fileHandlerStub.getReadUsersCallCount(),
            "FileHandler.readUsers() should NOT be called after error");
        
        assertFalse(recommendationStub.wasCalled(),
            "Recommendation should NOT be called when FileHandler fails");
        
        assertEquals("ERROR: Movie Title Test Movie is wrong", 
            fileHandlerStub.getLastErrorWritten(),
            "Error should be written to file");
    }
    
    @Test
    @DisplayName("Top-Down with Stubs: Main handles empty data")
    void testMainEmptyDataWithStubs() {
        // Arrange: Empty data
        fileHandlerStub.setMoviesToReturn(List.of());
        fileHandlerStub.setUsersToReturn(List.of());
        
        // Act
        testableMain.run("movies.txt", "users.txt", "recommendations.txt");
        
        // Assert: Should still complete successfully
        assertTrue(recommendationStub.wasCalled(),
            "Recommendation should be called even with empty data");
        
        assertEquals(0, recommendationStub.getLastUsers().size(),
            "Empty users list should be passed");
        assertEquals(0, recommendationStub.getLastMovies().size(),
            "Empty movies list should be passed");
    }
    
    @Test
    @DisplayName("Top-Down with Stubs: Main handles Recommendation errors")
    void testMainRecommendationErrorWithStubs() {
        // Arrange: Valid data but Recommendation fails
        List<Movie> testMovies = List.of(new Movie("Test", "T123", "Action"));
        List<User> testUsers = List.of(new User("Test", "123456789"));
        
        fileHandlerStub.setMoviesToReturn(testMovies);
        fileHandlerStub.setUsersToReturn(testUsers);
        recommendationStub.setExceptionToThrow(new RuntimeException("Recommendation failed"));
        
        // Act
        testableMain.run("movies.txt", "users.txt", "recommendations.txt");
        
        // Assert: Check that error contains the message
        String errorWritten = fileHandlerStub.getLastErrorWritten();
        assertTrue(errorWritten.contains("Recommendation failed"),
            "Recommendation error should be caught and written. Got: " + errorWritten);
    }}