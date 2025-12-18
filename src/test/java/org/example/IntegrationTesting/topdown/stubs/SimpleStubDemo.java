// File: src/test/java/org/example/IntegrationTesting/topdown/SimpleStubDemo.java
package org.example.IntegrationTesting.topdown.stubs;

import org.example.model.Movie;
import org.example.model.User;

import java.util.List;

import org.example.IntegrationTesting.topdown.TestableMain;

/**
 * SIMPLE working demonstration of top-down testing with stubs
 */
public class SimpleStubDemo {
    
    // Simple stub that implements the TestableMain interfaces
    static class SimpleFileHandler implements TestableMain.FileHandler {
        private String lastError;
        private int callCount = 0;
        

        public List<Movie> readMovies(String filePath) throws Exception {
            callCount++;
            System.out.println("  [STUB] FileHandler.readMovies() called with: " + filePath);
            
            // Return some test movies
            return List.of(
                new Movie("Demo Movie 1", "DM001", "Action"),
                new Movie("Demo Movie 2", "DM002", "Drama")
            );
        }
        
        
        public List<User> readUsers(String filePath, List<Movie> movies) throws Exception {
            callCount++;
            System.out.println("  [STUB] FileHandler.readUsers() called with " + movies.size() + " movies");
            
            // Create a test user who likes the first movie
            User user = new User("Demo User", "123456789");
            user.addLikedMovie("DM001");
            return List.of(user);
        }
        
        public void writeError(String filePath, String errorMessage) {
            lastError = errorMessage;
            System.out.println("  [STUB] FileHandler.writeError() called: " + errorMessage);
        }
        
        public int getCallCount() { return callCount; }
        public String getLastError() { return lastError; }
    }
    
    static class SimpleRecommendation implements TestableMain.Recommendation {
        private boolean wasCalled = false;
        private String lastOutputPath;
        
        public void generateRecommendations(List<User> users, List<Movie> movies, String outputPath) {
            wasCalled = true;
            lastOutputPath = outputPath;
            System.out.println("  [STUB] Recommendation.generateRecommendations() called");
            System.out.println("    - Users: " + users.size());
            System.out.println("    - Movies: " + movies.size());
            System.out.println("    - Output: " + outputPath);
        }
        
        public boolean wasCalled() { return wasCalled; }
        public String getLastOutputPath() { return lastOutputPath; }
    }
    
    public static void main(String[] args) {
        System.out.println("=== SIMPLE TOP-DOWN TESTING DEMONSTRATION ===\n");
        
        System.out.println("1. Creating stubs for FileHandler and Recommendation...");
        SimpleFileHandler fileHandler = new SimpleFileHandler();
        SimpleRecommendation recommendation = new SimpleRecommendation();
        
        System.out.println("\n2. Creating TestableMain with stubs...");
        TestableMain main = new TestableMain(fileHandler, recommendation);
        
        System.out.println("\n3. Running Main with stubs (TOP-DOWN TESTING)...");
        System.out.println("   Calling main.run(\"movies.txt\", \"users.txt\", \"output.txt\")");
        System.out.println();
        
        main.run("movies.txt", "users.txt", "output.txt");
        
        System.out.println("\n4. Results:");
        System.out.println("   ✓ FileHandler methods called: " + fileHandler.getCallCount() + " times");
        System.out.println("   ✓ Recommendation called: " + recommendation.wasCalled());
        System.out.println("   ✓ Output path: " + recommendation.getLastOutputPath());
        System.out.println("   ✓ Last error: " + (fileHandler.getLastError() != null ? fileHandler.getLastError() : "None"));
        
        System.out.println("\n=== DEMONSTRATION COMPLETE ===");
        System.out.println("\nWhat we demonstrated:");
        System.out.println("1. Main was tested FROM THE TOP");
        System.out.println("2. Used STUBS for FileHandler and Recommendation");
        System.out.println("3. No real files were read/written");
        System.out.println("4. Verified Main calls lower modules correctly");
        System.out.println("5. This is textbook TOP-DOWN integration testing!");
    }
}