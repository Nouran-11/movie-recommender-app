// File: src/test/java/org/example/IntegrationTesting/bottomup/DriverBasedIntegrationTest.java
package org.example.IntegrationTesting.bottomup;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.example.io.FileHandler;
import org.example.model.Movie;
import org.example.model.User;
import org.example.service.Recommendation;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DriverBasedIntegrationTest {
    
    private static class TestDriver {
        public List<Movie> loadMovies(String path) throws Exception {
            return FileHandler.readMovies(path);
        }
        
        public List<User> loadUsers(String path, List<Movie> movies) throws Exception {
            return FileHandler.readUsers(path, movies);
        }
        
        public void generateRecommendations(List<User> users, List<Movie> movies, String outputPath) {
            Recommendation recommender = new Recommendation();
            recommender.generateRecommendations(users, movies, outputPath);
        }
    }
    
    private TestDriver driver;
    private File tempMoviesFile;
    private File tempUsersFile;
    
    @BeforeEach
    void setUp() throws IOException {
        driver = new TestDriver();
        
        tempMoviesFile = createTempFile(
            "The Matrix,TM123\n" +
            "Action,Sci-Fi\n" +
            "Inception,I456\n" +
            "Action,Thriller\n"
        );
        
        tempUsersFile = createTempFile(
            "Alice Johnson,12345678A\n" +
            "TM123,I456\n"
        );
    }
    
    @AfterEach
    void tearDown() {
        if (tempMoviesFile != null) tempMoviesFile.delete();
        if (tempUsersFile != null) tempUsersFile.delete();
    }
    
    private File createTempFile(String content) throws IOException {
        File file = File.createTempFile("test", ".txt");
        file.deleteOnExit();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
        return file;
    }
    
    @Test
    @DisplayName("Bottom-Up with Driver: Test module chain")
    void testModuleChainWithDriver() throws Exception {
        List<Movie> movies = driver.loadMovies(tempMoviesFile.getAbsolutePath());
        assertEquals(2, movies.size());
        
        List<User> users = driver.loadUsers(tempUsersFile.getAbsolutePath(), movies);
        assertEquals(1, users.size());
        
        File outputFile = File.createTempFile("output", ".txt");
        outputFile.deleteOnExit();
        
        driver.generateRecommendations(users, movies, outputFile.getAbsolutePath());
        
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
    }
    
    @Test
    @DisplayName("Bottom-Up with Driver: Test error propagation")
    void testErrorPropagationWithDriver() throws IOException {
        File invalidFile = createTempFile("invalid title,XXX\nGenre\n");
        
        Exception exception = assertThrows(Exception.class, () -> {
            driver.loadMovies(invalidFile.getAbsolutePath());
        });
        
        assertTrue(exception.getMessage().contains("Movie Title"),
            "Driver should propagate Movie validation error");
        
        invalidFile.delete();
    }
}