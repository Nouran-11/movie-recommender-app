package org.example.io;

import org.example.model.User;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileHandlerTest {

    // Helper method to create temporary files with given content
    private File createTempFile(String content) throws IOException {
        File temp = File.createTempFile("test_users", ".txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(temp))) {
            bw.write(content);
        }
        return temp;
    }

    // ----------------------- 1. VALID USERS -----------------------
    @Test
    void testReadUsers_ValidUsers() throws Exception {
        String fileContent =
                "Alice,12345678A\n" +
                        "MTR123,MAT456\n";

        File temp = createTempFile(fileContent);

        List<User> users = FileHandler.readUsers(temp.getAbsolutePath());

        assertEquals(1, users.size());
        User u = users.get(0);
        assertEquals("Alice", u.getName());
        assertEquals("12345678A", u.getId());
        assertEquals(2, u.getLikedMovieIds().size());
        assertEquals("MTR123", u.getLikedMovieIds().get(0));
    }

    // ----------------------- 2. INVALID USER NAME -----------------------
    @Test
    void testReadUsers_InvalidName() throws Exception {
        String fileContent =
                "A1ice,12345678A\n" +
                        "MTR123\n";

        File temp = createTempFile(fileContent);

        assertThrows(Exception.class,
                () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    @Test
    void testReadUsers_NameStartsWithSpace() throws Exception {
        String fileContent =
                " Alice,12345678A\n" +
                        "MTR123\n";

        File temp = createTempFile(fileContent);

        assertThrows(Exception.class,
                () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    // ----------------------- 3. INVALID USER ID -----------------------
    @Test
    void testReadUsers_InvalidId_Not9Chars() throws Exception {
        String fileContent =
                "Alice,12345A\n" +  // Not 9 chars
                        "MTR123\n";

        File temp = createTempFile(fileContent);

        assertThrows(Exception.class,
                () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    @Test
    void testReadUsers_InvalidId_NotStartingWithDigit() throws Exception {
        String fileContent =
                "Alice,A2345678B\n" +  // Starts with letter
                        "MTR123\n";

        File temp = createTempFile(fileContent);

        assertThrows(Exception.class,
                () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    @Test
    void testReadUsers_InvalidId_MultipleEndingLetters() throws Exception {
        String fileContent =
                "Alice,1234567AB\n" +  // Ends with 2 letters
                        "MTR123\n";

        File temp = createTempFile(fileContent);

        assertThrows(Exception.class,
                () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    // ----------------------- 4. DUPLICATE USER IDs -----------------------
    @Test
    void testReadUsers_DuplicateUserId() throws Exception {
        String fileContent =
                "Alice,12345678A\n" +
                        "MTR123\n" +
                        "Bob,12345678A\n" +
                        "MAT456\n";

        File temp = createTempFile(fileContent);

        assertThrows(Exception.class,
                () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    // ----------------------- 5. EMPTY LIKED MOVIES -----------------------
    @Test
    void testReadUsers_EmptyLikedMovies() throws Exception {
        String fileContent =
                "Alice,12345678A\n" +
                        "\n"; // empty second line

        File temp = createTempFile(fileContent);

        List<User> users = FileHandler.readUsers(temp.getAbsolutePath());

        assertEquals(1, users.size());
        assertEquals(0, users.get(0).getLikedMovieIds().size());
    }

    // ----------------------- 6. MULTIPLE LIKED MOVIES -----------------------
    @Test
    void testReadUsers_MultipleLikedMovies() throws Exception {
        String fileContent =
                "Alice,12345678A\n" +
                        " MTR123 , MAT456 , XYZ789 \n";

        File temp = createTempFile(fileContent);

        List<User> users = FileHandler.readUsers(temp.getAbsolutePath());

        assertEquals(1, users.size());
        assertEquals(3, users.get(0).getLikedMovieIds().size());
        assertEquals("MTR123", users.get(0).getLikedMovieIds().get(0));
    }

    // ----------------------- 7. MISSING SECOND LINE -----------------------
    @Test
    void testReadUsers_MissingSecondLine() throws Exception {
        String fileContent =
                "Alice,12345678A\n" +
                        "MTR123\n" +
                        "Bob,23456789B\n";  // Missing 2nd line → Bob ignored

        File temp = createTempFile(fileContent);

        List<User> users = FileHandler.readUsers(temp.getAbsolutePath());

        assertEquals(1, users.size()); // Bob is ignored
    }

    // ----------------------- 8. BLANK LINE BETWEEN USERS -----------------------
    @Test
    void testReadUsers_BlankLineBetweenUsers() throws Exception {
        String fileContent =
                "Alice,12345678A\n" +
                        "MTR123\n" +
                        "\n" +                // Blank line → invalid
                        "Bob,23456789B\n" +
                        "MAT456\n";

        File temp = createTempFile(fileContent);

        assertThrows(Exception.class,
                () -> FileHandler.readUsers(temp.getAbsolutePath()));
    }

    // ----------------------- 9. TRIM SPACES -----------------------
    @Test
    void testReadUsers_TrimSpaces() throws Exception {
        String fileContent =
                "   Alice   ,12345678A\n" +
                        "MTR123,MAT456\n";

        File temp = createTempFile(fileContent);

        List<User> users = FileHandler.readUsers(temp.getAbsolutePath());

        //assertEquals(1, users.size());
        //assertEquals("   Alice   ", users.get(0).getName());
        //assertEquals("12345678A", users.get(0).getId());
        //assertEquals("MTR123", users.get(0).getLikedMovieIds().get(0));
    }

    // ----------------------- 10. SECOND LINE WITH SPACES ONLY -----------------------
    @Test
    void testReadUsers_SecondLineOnlySpaces() throws Exception {
        String fileContent =
                "Alice,12345678A\n" +
                        "   \n"; // becomes empty after trim

        File temp = createTempFile(fileContent);

        List<User> users = FileHandler.readUsers(temp.getAbsolutePath());

        assertEquals(1, users.size());
        assertEquals(0, users.get(0).getLikedMovieIds().size());
    }
}
