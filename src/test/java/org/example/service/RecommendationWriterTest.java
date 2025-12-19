package org.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.example.model.Movie;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecommendationWriterTest {

    private List<String> writeAndRead(Map<User, List<String>> data) throws IOException {
        Path tempFile = Files.createTempFile("recommendations", ".txt");

        RecommendationWriter writer = new RecommendationWriter();
        writer.writeToFile(data, tempFile.toString());

        return Files.readAllLines(tempFile);
    }

    @Test
    void writesUserHeaderCorrectly() throws IOException {
        User alice = new User("Alice", "12345678A");

        List<String> lines = writeAndRead(
                Map.of(alice, List.of("Interstellar"))
        );

        assertEquals("Alice, 12345678A", lines.get(0));
    }

    @Test
    void writesSingleRecommendationCorrectly() throws IOException {
        User alice = new User("Alice", "12345678A");

        List<String> lines = writeAndRead(
                Map.of(alice, List.of("Interstellar"))
        );

        assertEquals("Interstellar", lines.get(1));
    }

    @Test
    void writesMultipleRecommendationsSeparatedByComma() throws IOException {
        User alice = new User("Alice", "12345678A");

        List<String> lines = writeAndRead(
                Map.of(alice, List.of("Interstellar", "Inception"))
        );

        assertEquals("Interstellar, Inception", lines.get(1));
    }

    @Test
    void writingEmptyMapProducesEmptyFile() throws IOException {
        List<String> lines = writeAndRead(Map.of());

        assertTrue(lines.isEmpty());
    }

    @Test
    void handlesIOExceptionGracefully() {
        RecommendationWriter writer = new RecommendationWriter();

        User alice = new User("Alice", "12345678A");

        // Invalid path to force IOException
        String invalidPath = "Z:\\this_path_does_not_exist\\file.txt";

        writer.writeToFile(
                Map.of(alice, List.of("Interstellar")),
                invalidPath
        );

        // No exception should be thrown (method handles it internally)
        assertTrue(true);
    }

}
