package org.example.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void testAddLikedMovie() {
        User user = new User("Menna", "1A2B3C4D5");

        user.addLikedMovie("M001");
        user.addLikedMovie("M002");

        List<String> movies = user.getLikedMovieIds();

        assertEquals(2, movies.size());
        assertTrue(movies.contains("M001"));
        assertTrue(movies.contains("M002"));
    }
    // -----------------------------
    // MovieId validation Tests
    // -----------------------------
    void testMovieId(){
        assertFalse(User.isValidMovieId(" "));
    }

    // -----------------------------
    // UserName validation Tests
    // -----------------------------
    @Test
    void testValidName() {
        assertTrue(User.isValidName("Menna"));
        assertTrue(User.isValidName("John Doe"));
    }

    @Test
    void testInvalidName() {
        assertFalse(User.isValidName(null));
        assertFalse(User.isValidName(""));
        assertFalse(User.isValidName("  Menna"));
        assertFalse(User.isValidName("123Menna"));
        assertFalse(User.isValidName("Menna!"));
    }

    // -----------------------------
    // User ID Validation Tests
    // -----------------------------
    @Test
    void testValidUserId() {
        assertTrue(User.isValidUserId("1A2B3C4D5"));  // ends with digit
        assertTrue(User.isValidUserId("1A2B3C4D6"));  // ends with digit
        assertTrue(User.isValidUserId("1A2B3C43E"));  // ends with letter, 7th is not letter
    }

    @Test
    void testInvalidUserId_NullOrWrongLength() {
        assertFalse(User.isValidUserId(null));
        assertFalse(User.isValidUserId("12345"));       // too short
        assertFalse(User.isValidUserId("12345678910")); // too long
    }

    @Test
    void testInvalidUserId_NotAlphanumeric() {
        assertFalse(User.isValidUserId("1A$B3C4D5"));
        assertFalse(User.isValidUserId("1A2B3C4D!"));
    }

    @Test
    void testInvalidUserId_DoesNotStartWithDigit() {
        assertFalse(User.isValidUserId("A12345678"));
    }

    @Test
    void testInvalidUserId_LastTwoCharsBothLetters() {
        assertFalse(User.isValidUserId("1A2B3CDDE"));  // last two letters → invalid
        assertFalse(User.isValidUserId("1A2B323CE"));  // last two letters → invalid
    }
}