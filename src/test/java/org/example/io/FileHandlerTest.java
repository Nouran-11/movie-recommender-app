package org.example.io;

import org.example.model.User;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileHandlerTest {
    private File createTempFile(String content) throws IOException {
        File temp = File.createTempFile("user_test", ".txt");
        temp.deleteOnExit();

        try (FileWriter writer = new FileWriter(temp)) {
            writer.write(content);
        }

        return temp;
    }

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
}
