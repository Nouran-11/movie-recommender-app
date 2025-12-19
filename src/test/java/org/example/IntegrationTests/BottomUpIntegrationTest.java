package org.example.IntegrationTests;

import org.example.io.FileReader;
import org.example.model.Movie;
import org.example.model.User;
import org.example.service.RecommendationWriter;
import org.example.validator.RecommendationValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BottomUpIntegrationTest {

    @TempDir
    Path tempDir;

    @Test
    void testBottomUpFlow() throws Exception {
        // --- Layer 1: IO (Reading) ---
        Path moviesFile = tempDir.resolve("movies.txt");
        Path usersFile = tempDir.resolve("users.txt");

        Files.write(moviesFile, List.of(
                "Adventure Movie,AM123",
                "Adventure",
                "Action Movie,AM124",
                "Action",
                "Another Action,AA125",
                "Action"));
        Files.write(usersFile, List.of(
                "Bob,98765432B",
                "AM124"));

        // Step 1 Check: FileReader reads correctly
        List<Movie> movies = FileReader.readMovies(moviesFile.toAbsolutePath().toString());
        assertEquals(3, movies.size(), "Should have read 3 movies");

        List<User> users = FileReader.readUsers(usersFile.toAbsolutePath().toString(), movies);
        assertEquals(1, users.size(), "Should have read 1 user");
        User bob = users.get(0);
        assertEquals("Bob", bob.getName());

        // --- Layer 2: Business Logic (Validation over Real IO Data) ---
        RecommendationValidator validator = new RecommendationValidator();
        Map<User, List<String>> recommendations = validator.generateRecommendations(users, movies);

        // Step 2 Check: Logic produced expected results
        assertNotNull(recommendations);
        assertTrue(recommendations.containsKey(bob));
        List<String> bobRecs = recommendations.get(bob);

        // Bob likes 'Action Movie' (ID 2). 'Another Action' (ID 3) is also Action.
        // He should NOT get 'Action Movie' (already watched) but SHOULD get 'Another
        // Action'.
        // 'Adventure Movie' is Adventure, not recommended.
        assertTrue(bobRecs.contains("Another Action"), "Bob should be recommended 'Another Action'");
        assertFalse(bobRecs.contains("Action Movie"), "Bob shouldn't be recommended what he already watched");
        assertFalse(bobRecs.contains("Adventure Movie"), "Bob shouldn't be recommended unrelated genre");

        // --- Layer 3: IO (Writing Results) ---
        Path outputPath = tempDir.resolve("bottom_up_recs.txt");
        RecommendationWriter writer = new RecommendationWriter();
        writer.writeToFile(recommendations, outputPath.toAbsolutePath().toString());

        // Step 3 Check: File exists and has content
        assertTrue(Files.exists(outputPath));
        List<String> lines = Files.readAllLines(outputPath);

        // Verify file structure
        // Bob, 98765432B
        // Another Action
        boolean userFound = lines.stream().anyMatch(l -> l.contains("Bob, 98765432B"));
        boolean recFound = lines.stream().anyMatch(l -> l.contains("Another Action"));

        assertTrue(userFound, "Output file must contain user header");
        assertTrue(recFound, "Output file must contain recommendation");
    }
}
