// File: src/test/java/org/example/IntegrationTesting/bottomup/ModelsDriver.java
package org.example.IntegrationTesting.bottomup;

import org.example.model.Movie;
import org.example.model.User;

public class ModelsDriver {
    public static void main(String[] args) {
        System.out.println("=== Testing Models (Bottom-Up) ===");
        
        try {
            // Test 1: Movie creation
            Movie movie = new Movie("Test Movie", "TM123", "Action");
            System.out.println("✓ Movie created: " + movie.getTitle());
            
            // Test 2: User creation  
            User user = new User("Test User", "123456789");
            System.out.println("✓ User created: " + user.getName());
            
            // Test 3: Integration
            user.addLikedMovie(movie.getId());
            System.out.println("✓ User liked movie: " + movie.getId());
            
            System.out.println("\n✅ All model tests passed!");
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}