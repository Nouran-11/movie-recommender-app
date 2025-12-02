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
        assertFalse(UserValidator.isValidMovieId(" "));
    }

    // -----------------------------
    // UserName validation Tests
    // -----------------------------
    @Test
    void testValidName() {
        assertTrue(UserValidator.isValidName("Menna"));
        assertTrue(UserValidator.isValidName("John Doe"));
    }

    @Test
    void testInvalidName() {
        assertFalse(UserValidator.isValidName(null));
        assertFalse(UserValidator.isValidName(""));
        assertFalse(UserValidator.isValidName("  Menna"));
        assertFalse(UserValidator.isValidName("123Menna"));
        assertFalse(UserValidator.isValidName("Menna!"));
    }

    // -----------------------------
    // User ID Validation Tests
    // -----------------------------
    @Test
    void testValidUserId() {
        assertTrue(UserValidator.isValidUserId("1A2B3C4D5"));  // ends with digit
        assertTrue(UserValidator.isValidUserId("1A2B3C4D6"));  // ends with digit
        assertTrue(UserValidator.isValidUserId("1A2B3C43E"));  // ends with letter, 7th is not letter
    }

    @Test
    void testInvalidUserId_NullOrWrongLength() {
        assertFalse(UserValidator.isValidUserId(null));
        assertFalse(UserValidator.isValidUserId("12345"));       // too short
        assertFalse(UserValidator.isValidUserId("12345678910")); // too long
    }

    @Test
    void testInvalidUserId_NotAlphanumeric() {
        assertFalse(UserValidator.isValidUserId("1A$B3C4D5"));
        assertFalse(UserValidator.isValidUserId("1A2B3C4D!"));
    }

    @Test
    void testInvalidUserId_DoesNotStartWithDigit() {
        assertFalse(UserValidator.isValidUserId("A12345678"));
    }

    @Test
    void testInvalidUserId_LastTwoCharsBothLetters() {
        assertFalse(UserValidator.isValidUserId("1A2B3CDDE"));  // last two letters → invalid
        assertFalse(UserValidator.isValidUserId("1A2B323CE"));  // last two letters → invalid
    }
}