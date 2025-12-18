package org.example.io;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileWriterTest {

    private String readFileContent(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    @Test
    void writesErrorMessageToFile() throws IOException {
        // Arrange
        File tempFile = File.createTempFile("error_test", ".txt");
        tempFile.deleteOnExit();

        String message = "This is a test error message";

        // Act
        FileWriter.writeError(tempFile.getAbsolutePath(), message);

        // Assert
        String content = readFileContent(tempFile);
        assertEquals(message, content);
    }

    @Test
    void overwritesExistingFile() throws IOException {
        // Arrange
        File tempFile = File.createTempFile("error_test", ".txt");
        tempFile.deleteOnExit();

        // Write initial content
        FileWriter.writeError(tempFile.getAbsolutePath(), "Old message");

        // Act
        String newMessage = "New message";
        FileWriter.writeError(tempFile.getAbsolutePath(), newMessage);

        // Assert
        String content = readFileContent(tempFile);
        assertEquals(newMessage, content, "FileWriter should overwrite existing content");
    }

    @Test
    void writesEmptyMessage() throws IOException {
        // Arrange
        File tempFile = File.createTempFile("error_test", ".txt");
        tempFile.deleteOnExit();

        // Act
        FileWriter.writeError(tempFile.getAbsolutePath(), "");

        // Assert
        String content = readFileContent(tempFile);
        assertEquals("", content, "FileWriter should correctly write empty string");
    }

    @Test
    void fileIsCreatedIfNotExist() throws IOException {
        // Arrange
        File tempFile = new File(System.getProperty("java.io.tmpdir"), "non_existing_file.txt");
        if (tempFile.exists()) tempFile.delete();

        String message = "Hello world";

        // Act
        FileWriter.writeError(tempFile.getAbsolutePath(), message);

        // Assert
        assertTrue(tempFile.exists(), "FileWriter should create file if it does not exist");
        assertEquals(message, readFileContent(tempFile));
    }
}
