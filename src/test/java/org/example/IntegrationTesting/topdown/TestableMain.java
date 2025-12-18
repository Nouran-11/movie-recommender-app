// File: src/test/java/org/example/IntegrationTesting/topdown/TestableMain.java
package org.example.IntegrationTesting.topdown;

import java.util.List;

import org.example.model.Movie;
import org.example.model.User;

/**
 * Testable version of Main that allows dependency injection
 */
public class TestableMain {
    
    private final FileHandler fileHandler;
    private final Recommendation recommendation;
    
    // Interface for FileHandler
    public interface FileHandler {
        List<Movie> readMovies(String filePath) throws Exception;
        List<User> readUsers(String filePath, List<Movie> movies) throws Exception;
        void writeError(String filePath, String errorMessage);
    }
    
    // Interface for Recommendation
    public interface Recommendation {
        void generateRecommendations(List<User> users, List<Movie> movies, String outputPath);
    }
    
    // Constructor for dependency injection
    public TestableMain(FileHandler fileHandler, Recommendation recommendation) {
        this.fileHandler = fileHandler;
        this.recommendation = recommendation;
    }
    
    // The main logic from Main.java, but testable
    public void run(String movieFile, String userFile, String outputFile) {
        try {
            List<Movie> movies = fileHandler.readMovies(movieFile);
            List<User> users = fileHandler.readUsers(userFile, movies);
            
            recommendation.generateRecommendations(users, movies, outputFile);
            
            System.out.println("Success! Recommendations generated.");
            
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            
            if (errorMsg != null) {
                fileHandler.writeError(outputFile, errorMsg);
                System.err.println("Validation Failed. detailed error written to " + outputFile);
            } else {
                e.printStackTrace();
            }
        }
    }
}