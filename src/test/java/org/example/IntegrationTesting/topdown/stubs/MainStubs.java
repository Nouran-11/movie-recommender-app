// File: src/test/java/org/example/IntegrationTesting/topdown/stubs/MainStubs.java
package org.example.IntegrationTesting.topdown.stubs;

import java.util.ArrayList;
import java.util.List;

import org.example.IntegrationTesting.topdown.TestableMain;
import org.example.model.Movie;
import org.example.model.User;

public class MainStubs {
    
    public static class FileHandlerStub implements TestableMain.FileHandler {
        private List<Movie> moviesToReturn = new ArrayList<>();
        private List<User> usersToReturn = new ArrayList<>();
        private Exception exceptionToThrow;
        private String lastErrorWritten;
        private int readMoviesCallCount = 0;
        private int readUsersCallCount = 0;
        
        public void setMoviesToReturn(List<Movie> movies) {
            this.moviesToReturn = movies;
        }
        
        public void setUsersToReturn(List<User> users) {
            this.usersToReturn = users;
        }
        
        public void setExceptionToThrow(Exception e) {
            this.exceptionToThrow = e;
        }
        
        public String getLastErrorWritten() {
            return lastErrorWritten;
        }
        
        public int getReadMoviesCallCount() {
            return readMoviesCallCount;
        }
        
        public int getReadUsersCallCount() {
            return readUsersCallCount;
        }
        
        @Override
        public List<Movie> readMovies(String filePath) throws Exception {
            readMoviesCallCount++;
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            return new ArrayList<>(moviesToReturn);
        }
        
        @Override
        public List<User> readUsers(String filePath, List<Movie> movies) throws Exception {
            readUsersCallCount++;
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            return new ArrayList<>(usersToReturn);
        }
        
        @Override
        public void writeError(String filePath, String errorMessage) {
            lastErrorWritten = errorMessage;
        }
    }
    
    public static class RecommendationStub implements TestableMain.Recommendation {
        private Exception exceptionToThrow;
        private boolean wasCalled = false;
        private String lastOutputPath;
        private List<User> lastUsers;
        private List<Movie> lastMovies;
        
        public void setExceptionToThrow(Exception e) {
            this.exceptionToThrow = e;
        }
        
        public boolean wasCalled() {
            return wasCalled;
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
            wasCalled = true;
            lastOutputPath = outputPath;
            lastUsers = users;
            lastMovies = movies;
            
            if (exceptionToThrow != null) {
                throw new RuntimeException(exceptionToThrow);
            }
        }
    }
}