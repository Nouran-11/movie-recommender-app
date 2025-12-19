package org.example.IntegrationTests;

import org.example.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TopDownIntegrationTest {

    @TempDir
    Path tempDir;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final InputStream originalIn = System.in;

    private Path moviesFile;
    private Path usersFile;
    private Path outputFile;

    @BeforeEach
    void setUp() throws IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        // Create temporary files
        moviesFile = tempDir.resolve("movies.txt");
        usersFile = tempDir.resolve("users.txt");
        outputFile = Paths.get("recommendations.txt"); // Main uses relative path "recommendations.txt"

        Files.write(moviesFile, List.of(
                "The Matrix,TM123",
                "Sci-Fi",
                "The Godfather,TG124",
                "Crime",
                "Inception,I125",
                "Sci-Fi"));

        Files.write(usersFile, List.of(
                "Alice,12345678A",
                "TM123,TG124"));
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalIn);

        Files.deleteIfExists(outputFile);
    }

    @Test
    void testMainFlow() throws IOException {
        // Prepare input for Scanner: movie path + newline + user path + newline
        String simulatedInput = moviesFile.toAbsolutePath().toString() + System.lineSeparator() +
                usersFile.toAbsolutePath().toString() + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Run Main
        Main.main(new String[] {});

        // Verify Console Output
        String output = outContent.toString();
        assertTrue(output.contains("Welcome to the Movie Recommender System!"), "Welcome message missing");
        assertTrue(output.contains("Success! Recommendations written to recommendations.txt"),
                "Success message missing");

        // Verify Output File Existence
        assertTrue(Files.exists(outputFile), "Output file should have been created");

        // Verify Output Content
        List<String> lines = Files.readAllLines(outputFile);
        assertFalse(lines.isEmpty(), "Output file shouldn't be empty");
        // user line
        assertTrue(lines.stream().anyMatch(line -> line.contains("Alice, 12345678A")));
        // rec line (Alice likes 1 & 2 (Sci-Fi, Crime). Inception is 3 (Sci-Fi). Should
        // recommend Inception)
        assertTrue(lines.stream().anyMatch(line -> line.contains("Inception")));
    }
}
