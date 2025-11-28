package org.example.io;

import org.example.model.User;
import org.example.model.Movie;
import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

 
public class FileHandlerTest {
  
    private Path tempFile;
    
    @AfterEach
    void cleanup() throws IOException {
        if (tempFile != null) {
            Files.deleteIfExists(tempFile);
        }
    } 
 

      // ------------------------- TEST readUers() -------------------------

    @Test
    void testValidUserValidIdValidMovie() throws Exception {
        String file =
                "Menna Tarik,12345678A\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        List<User> users = FileHandler.readUsers(temp.getAbsolutePath());

        assertEquals(1, users.size());
    }

    // WRONG USERNAMES
    @Test
    void testWrongUserName_StartsWithSpace() throws Exception {
        String file =
                "  Menna,12345678A\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    @Test
    void testWrongUserName_HasDigits() throws Exception {
        String file =
                "Menna3,12345678A\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    @Test
    void testWrongUserName_HasSymbols() throws Exception {
        String file =
                "Men@na,12345678A\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    @Test
    void testWrongUserName_Empty() throws Exception {
        String file =
                ",12345678A\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }


// WRONG USER IDS

    @Test
    void testWrongUserId_TooShort() throws Exception {
        String file =
                "Menna,12345\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    @Test
    void testWrongUserId_TooLong() throws Exception {
        String file =
                "Menna,1234567890\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    @Test
    void testWrongUserId_NotStartingWithNumber() throws Exception {
        String file =
                "Menna,A23456789\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    @Test
    void testWrongUserId_EndsWithMoreThanOneLetter() throws Exception {
        String file =
                "Menna,1234567AB\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    @Test
    void testWrongUserId_HasSymbols() throws Exception {
        String file =
                "Menna,12345@78A\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    @Test
    void testWrongUserId_Duplicate() throws Exception {
        String file =
                "Menna,12345678A\n" +
                        "TM123\n" +
                        "Bob,12345678A\n" +
                        "TM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }


// WRONG MOVIE IDS

    @Test
    void testWrongMovieId_MissingLetters() throws Exception {
        String file =
                "Menna,12345678A\n" +
                        "M123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    @Test
    void testWrongMovieId_ExtraLetters() throws Exception {
        String file =
                "Menna,12345678A\n" +
                        "TMM123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    @Test
    void testWrongMovieId_WrongLetters() throws Exception {
        String file =
                "Menna,12345678A\n" +
                        "TX123\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    @Test
    void testWrongMovieId_RepeatedDigits() throws Exception {
        String file =
                "Menna,12345678A\n" +
                        "TM111\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    @Test
    void testWrongMovieId_NotThreeDigits() throws Exception {
        String file =
                "Menna,12345678A\n" +
                        "TM12\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    @Test
    void testWrongMovieId_LetterInsideDigits() throws Exception {
        String file =
                "Menna,12345678A\n" +
                        "TM1A3\n";

        File temp = createTempFile(file);
        assertThrows(Exception.class, () -> FileHandler.readUsers(temp.getAbsolutePath()));
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
