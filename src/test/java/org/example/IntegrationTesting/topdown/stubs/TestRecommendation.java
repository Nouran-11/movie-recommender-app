// File: src/test/java/org/example/IntegrationTesting/topdown/stubs/TestRecommendation.java
package org.example.IntegrationTesting.topdown.stubs;

import java.util.List;

import org.example.model.Movie;
import org.example.model.User;
import org.example.service.Recommendation;

public class TestRecommendation extends Recommendation {
    
    private static TestRecommendation instance;
    private Exception exceptionToThrow;
    private boolean generateCalled = false;
    private String lastOutputPath;
    private List<User> lastUsers;
    private List<Movie> lastMovies;
    
    private TestRecommendation() {}
    
    public static TestRecommendation getInstance() {
        if (instance == null) {
            instance = new TestRecommendation();
        }
        return instance;
    }
    
    public static void reset() {
        instance = null;
    }
    
    public void setExceptionToThrow(Exception e) {
        this.exceptionToThrow = e;
    }
    
    public boolean wasGenerateCalled() {
        return generateCalled;
    }
    
    public String getLastOutputPath() {
        return lastOutputPath;
    }
    
    public List<User> getLastUsers() {
        return lastUsers;
    }
    
    public List<Movie> getLastMovies() {
        return lastMovies;
    }
    
    @Override
    public void generateRecommendations(List<User> users, List<Movie> movies, String outputPath) {
        generateCalled = true;
        lastOutputPath = outputPath;
        lastUsers = users;
        lastMovies = movies;
        
        if (exceptionToThrow != null) {
            throw new RuntimeException(exceptionToThrow);
        }
    }
}