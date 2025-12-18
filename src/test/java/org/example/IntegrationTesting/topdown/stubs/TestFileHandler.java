// File: src/test/java/org/example/IntegrationTesting/topdown/stubs/TestFileHandler.java
package org.example.IntegrationTesting.topdown.stubs;

import java.util.ArrayList;
import java.util.List;

import org.example.io.FileHandler;
import org.example.model.Movie;
import org.example.model.User;

public class TestFileHandler extends FileHandler {
    
    private static TestFileHandler instance;
    
    private List<Movie> moviesToReturn = new ArrayList<>();
    private List<User> usersToReturn = new ArrayList<>();
    private Exception exceptionToThrow;
    private String lastErrorWritten;
    private boolean readMoviesCalled = false;
    private boolean readUsersCalled = false;
    private boolean writeErrorCalled = false;
    
    private TestFileHandler() {}
    
    public static TestFileHandler getInstance() {
        if (instance == null) {
            instance = new TestFileHandler();
        }
        return instance;
    }
    
    public static void reset() {
        instance = null;
    }
    
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
    
    public boolean wasReadMoviesCalled() {
        return readMoviesCalled;
    }
    
    public boolean wasReadUsersCalled() {
        return readUsersCalled;
    }
    
    public boolean wasWriteErrorCalled() {
        return writeErrorCalled;
    }
    
    public static List<Movie> readMovies(String filePath) throws Exception {
        TestFileHandler handler = getInstance();
        handler.readMoviesCalled = true;
        
        if (handler.exceptionToThrow != null) {
            throw handler.exceptionToThrow;
        }
        
        return new ArrayList<>(handler.moviesToReturn);
    }
    
    public static List<User> readUsers(String filePath, List<Movie> movies) throws Exception {
        TestFileHandler handler = getInstance();
        handler.readUsersCalled = true;
        
        if (handler.exceptionToThrow != null) {
            throw handler.exceptionToThrow;
        }
        
        return new ArrayList<>(handler.usersToReturn);
    }
    
    public static void writeError(String filePath, String errorMessage) {
        TestFileHandler handler = getInstance();
        handler.writeErrorCalled = true;
        handler.lastErrorWritten = errorMessage;
    }
}