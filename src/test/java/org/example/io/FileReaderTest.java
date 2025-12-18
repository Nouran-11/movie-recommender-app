package org.example.io;

import org.example.model.User;
import org.example.model.Movie;
import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

 
public class FileReaderTest {

    private File createTempFile(String content) throws IOException {
        File temp = File.createTempFile("user_test", ".txt");
        temp.deleteOnExit();

        try (java.io.FileWriter writer = new java.io.FileWriter(temp)) {
            writer.write(content);
        }

        return temp;
    }

      // ------------------------- TEST readUsers() -------------------------

    @Test
    void testValidUserValidIdValidMovie() throws Exception {

        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                "Menna Tarik,12345678A\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        List<User> users = FileReader.readUsers(temp.getAbsolutePath(), movies);

        assertEquals(1, users.size());
    }

    // WRONG USERNAMES
    @Test
    void testWrongUserName_StartsWithSpace() throws Exception {
        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                "  Menna,12345678A\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileReader.readUsers(temp.getAbsolutePath(), movies));
    }

    @Test
    void testWrongUserName_HasDigits() throws Exception {
        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                "Menna3,12345678A\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileReader.readUsers(temp.getAbsolutePath(), movies));
    }

    @Test
    void testWrongUserName_HasSymbols() throws Exception {
        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                "Men@na,12345678A\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileReader.readUsers(temp.getAbsolutePath(), movies));
    }

    @Test
    void testWrongUserName_Empty() throws Exception {
        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                ",12345678A\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileReader.readUsers(temp.getAbsolutePath(), movies));
    }



// WRONG USER IDS

    @Test
    void testWrongUserId_TooShort() throws Exception {
        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                "Menna,12345\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileReader.readUsers(temp.getAbsolutePath(),movies));
    }

    @Test
    void testWrongUserId_TooLong() throws Exception {
        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                "Menna,1234567890\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileReader.readUsers(temp.getAbsolutePath(),movies));
    }

    @Test
    void testWrongUserId_NotStartingWithNumber() throws Exception {
        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                "Menna,A23456789\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileReader.readUsers(temp.getAbsolutePath(),movies));
    }

    @Test
    void testWrongUserId_EndsWithMoreThanOneLetter() throws Exception {
        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                "Menna,1234567AB\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileReader.readUsers(temp.getAbsolutePath(),movies));
    }

    @Test
    void testWrongUserId_HasSymbols() throws Exception {
        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                "Menna,12345@78A\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileReader.readUsers(temp.getAbsolutePath(),movies));
    }

    @Test
    void testWrongUserId_Duplicate() throws Exception {
        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                "Menna,12345678A\n" +
                        "TM123\n" +
                        "Bob,12345678A\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileReader.readUsers(temp.getAbsolutePath(),movies));
    }


// WRONG MOVIE IDS

    @Test
    void testWrongMovieId_MissingLetters() throws Exception {
        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                "Menna,12345678A\n" +
                        "M123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileReader.readUsers(temp.getAbsolutePath(),movies));
    }

    @Test
    void testWrongMovieId_ExtraLetters() throws Exception {
        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                "Menna,12345678A\n" +
                        "TMM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileReader.readUsers(temp.getAbsolutePath(),movies));
    }

    @Test
    void testWrongMovieId_WrongLetters() throws Exception {
        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                "Menna,12345678A\n" +
                        "TX123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileReader.readUsers(temp.getAbsolutePath(),movies));
    }

    @Test
    void testWrongMovieId_RepeatedDigits() throws Exception {
        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                "Menna,12345678A\n" +
                        "TM111\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileReader.readUsers(temp.getAbsolutePath(),movies));
    }

    @Test
    void testWrongMovieId_NotThreeDigits() throws Exception {
        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                "Menna,12345678A\n" +
                        "TM12\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileReader.readUsers(temp.getAbsolutePath(),movies));
    }

    @Test
    void testWrongMovieId_LetterInsideDigits() throws Exception {
        Movie matrix = new Movie("The Matrix", "TM123", "Action");
        List<Movie> movies = List.of(matrix);
        String file =
                "Menna,12345678A\n" +
                        "TM1A3\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileReader.readUsers(temp.getAbsolutePath(),movies));
    }

   
    // ------------------------- TEST readMovies() -------------------------

    private Path tempFile;

    @AfterEach
    void cleanup() throws IOException {
        if (tempFile != null) {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void testReadMovies_Success() throws Exception {
        tempFile = Files.createTempFile("movies", ".txt");

        String data =
                "The Matrix, TM263\n" +
                        "Action\n" +
                        "Avatar, A456\n" +
                        "Fantasy\n";

        Files.write(tempFile, data.getBytes());

        List<Movie> movies = FileReader.readMovies(tempFile.toString());

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

        List<Movie> movies = FileReader.readMovies(tempFile.toString());

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

        List<Movie> movies = FileReader.readMovies(tempFile.toString());

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
                FileReader.readMovies(tempFile.toString())
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
                FileReader.readMovies(tempFile.toString())
        );

        assertEquals("ERROR: Movie Title 123WrongTitle is wrong", ex.getMessage());
    }
    @Test
    void testReadMovies_EmptyFile_ThrowsException() throws IOException {
        java.nio.file.Path tempPath = java.nio.file.Files.createTempFile("movies", ".txt");

        Exception exception = assertThrows(Exception.class, () -> {
            FileReader.readMovies(tempPath.toString());
        });

        assertEquals("ERROR: Movie Title  is wrong", exception.getMessage());
        java.nio.file.Files.deleteIfExists(tempPath);
    }
    @Test
    void testReadMovies_OnlyOneLine_NoGenre_ThrowsException() throws IOException {
        java.nio.file.Path tempPath = java.nio.file.Files.createTempFile("movies", ".txt");
        String data = "The Matrix, TM263\n"; // No second line
        java.nio.file.Files.write(tempPath, data.getBytes());

        Exception exception = assertThrows(Exception.class, () -> {
            FileReader.readMovies(tempPath.toString());
        });
        assertEquals("ERROR: Movie Title  is wrong", exception.getMessage());
        java.nio.file.Files.deleteIfExists(tempPath);
    }
    @Test
    void testReadMovies_InvalidIdNumbers_ThrowsException() throws Exception {
        tempFile = Files.createTempFile("movies", ".txt");

        String data =
                "The Matrix, TM2A3\n" +  // Invalid number format
                        "Action\n";

        Files.write(tempFile, data.getBytes());

        Exception ex = assertThrows(Exception.class, () ->
                FileReader.readMovies(tempFile.toString())
        );

        assertEquals(
                "ERROR: Movie Id numbers TM2A3 aren't unique",
                ex.getMessage()
        );
    }


}
