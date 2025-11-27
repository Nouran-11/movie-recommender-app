package org.example.io;

import org.example.model.Movie;
import org.example.model.User;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {

    private Path tempFile;

    @AfterEach
    void cleanup() throws IOException {
        if (tempFile != null) {
            Files.deleteIfExists(tempFile);
        }
    }
    // ------------------------- TEST readMovies() -------------------------

    @Test
    void testReadMovies_Success() throws Exception {
        tempFile = Files.createTempFile("movies", ".txt");

        String data =
                "The Matrix, TM263\n" +
                        "Action\n" +
                        "Avatar, A456\n" +
                        "Fantasy\n";

        Files.write(tempFile, data.getBytes());

        List<Movie> movies = FileHandler.readMovies(tempFile.toString());

        assertEquals(2, movies.size());
        assertEquals("The Matrix", movies.get(0).getTitle());
        assertEquals("TM263", movies.get(0).getId());
        assertEquals("Action", movies.get(0).getGenre());

    }
    @Test
    void testReadMovies_TitleIsCaseSensitive() throws Exception {
        tempFile = Files.createTempFile("movies", ".txt");

        String data =
                "The Matrix, TM263\n" +
                        "Action\n" +
                        "Avatar, A456\n" +
                        "Fantasy\n";

        Files.write(tempFile, data.getBytes());

        List<Movie> movies = FileHandler.readMovies(tempFile.toString());

        assertEquals(2, movies.size());
        assertNotEquals("avatar", movies.get(1).getTitle());
        assertEquals("A456", movies.get(1).getId());
        assertEquals("Fantasy", movies.get(1).getGenre());

    }
    @Test
    void testReadMovies_IdIsCaseSensitive() throws Exception {
        tempFile = Files.createTempFile("movies", ".txt");

        String data =
                "The Matrix, TM263\n" +
                        "Action\n" +
                        "Avatar, A456\n" +
                        "Fantasy\n";

        Files.write(tempFile, data.getBytes());

        List<Movie> movies = FileHandler.readMovies(tempFile.toString());

        assertEquals(2, movies.size());
        assertEquals("Avatar", movies.get(1).getTitle());
        assertNotEquals("a456", movies.get(1).getId());
        assertEquals("Fantasy", movies.get(1).getGenre());

    }
    @Test
    void testReadMovies_InvalidIdLetters_ThrowsException() throws Exception {
        tempFile = Files.createTempFile("movies", ".txt");

        String data =
                "The Matrix, XX123\n" +
                        "Action\n";

        Files.write(tempFile, data.getBytes());

        Exception ex = assertThrows(Exception.class, () ->
                FileHandler.readMovies(tempFile.toString())
        );

        assertEquals("ERROR: Movie Id letters XX123 are wrong", ex.getMessage());

    }
    @Test
    void testReadMovies_InvalidTitle_ThrowsException() throws Exception {
        tempFile = Files.createTempFile("movies", ".txt");

        String data =
                "123WrongTitle, 111\n" +
                        "Action\n";

        Files.write(tempFile, data.getBytes());

        Exception ex = assertThrows(Exception.class, () ->
                FileHandler.readMovies(tempFile.toString())
        );

        assertEquals("ERROR: Movie Title 123WrongTitle is wrong", ex.getMessage());
    }
    @Test
    void testReadMovies_EmptyFile_ReturnsEmptyList() throws Exception {
        tempFile = Files.createTempFile("movies", ".txt");
        Files.write(tempFile, new byte[0]);

        List<Movie> movies = FileHandler.readMovies(tempFile.toString());

        assertTrue(movies.isEmpty());
    }
    @Test
    void testReadMovies_OnlyOneLine_NoGenre_ReturnsEmptyList() throws Exception {
        tempFile = Files.createTempFile("movies", ".txt");

        String data = "The Matrix, TM263\n";  // No second line

        Files.write(tempFile, data.getBytes());

        List<Movie> movies = FileHandler.readMovies(tempFile.toString());

        assertEquals(0, movies.size());
    }
    @Test
    void testReadMovies_InvalidIdNumbers_ThrowsException() throws Exception {
        tempFile = Files.createTempFile("movies", ".txt");

        String data =
                "The Matrix, TM2A3\n" +  // Invalid number format
                        "Action\n";

        Files.write(tempFile, data.getBytes());

        Exception ex = assertThrows(Exception.class, () ->
                FileHandler.readMovies(tempFile.toString())
        );

        assertEquals(
                "ERROR: Movie Id numbers TM2A3 aren't unique",
                ex.getMessage()
        );
    }


}
