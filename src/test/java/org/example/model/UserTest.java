package org.example.model;

import org.example.validator.UserValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testAddLikedMovie_Count() {
        User user = new User("Menna", "1A2B3C4D5");
        user.addLikedMovie("M001");
        user.addLikedMovie("M002");
        List<String> movies = user.getLikedMovieIds();
        assertEquals(2, movies.size());
    }

    @Test
    void testAddLikedMovie_ContainsM001() {
        User user = new User("Menna", "1A2B3C4D5");
        user.addLikedMovie("M001");
        user.addLikedMovie("M002");
        List<String> movies = user.getLikedMovieIds();
        assertTrue(movies.contains("M001"));
    }

    @Test
    void testAddLikedMovie_ContainsM002() {
        User user = new User("Menna", "1A2B3C4D5");
        user.addLikedMovie("M001");
        user.addLikedMovie("M002");
        List<String> movies = user.getLikedMovieIds();
        assertTrue(movies.contains("M002"));
    }

    // UserName validation Tests
    @Test
    void testValidName_Simple() {
        assertTrue(UserValidator.isValidName("Menna"));
    }

    @Test
    void testValidName_WithSpace() {
        assertTrue(UserValidator.isValidName("John Doe"));
    }

    @Test
    void testInvalidName_Null() {
        assertFalse(UserValidator.isValidName(null));
    }

    @Test
    void testInvalidName_Empty() {
        assertFalse(UserValidator.isValidName(""));
    }

    @Test
    void testInvalidName_LeadingSpace() {
        assertFalse(UserValidator.isValidName("  Menna"));
    }

    @Test
    void testInvalidName_StartsWithDigit() {
        assertFalse(UserValidator.isValidName("123Menna"));
    }

    @Test
    void testInvalidName_SpecialChars() {
        assertFalse(UserValidator.isValidName("Menna!"));
    }

    // User ID Validation Tests
    @Test
    void testValidUserId_EndsWithDigit() {
        assertTrue(UserValidator.isValidUserId("1A2B3C4D5"));
    }

    @Test
    void testValidUserId_EndsWithDigit2() {
        assertTrue(UserValidator.isValidUserId("1A2B3C4D6"));
    }

    @Test
    void testValidUserId_EndsWithLetter() {
        // ends with letter, 7th (index 8-1? no logic says last digit...)
        // Logic: lastIsLetter -> check charAt(7) (2nd to last) is NOT letter (must be
        // digit?)
        assertTrue(UserValidator.isValidUserId("1A2B3C43E"));
    }

    @Test
    void testInvalidUserId_Null() {
        assertFalse(UserValidator.isValidUserId(null));
    }

    @Test
    void testInvalidUserId_TooShort() {
        assertFalse(UserValidator.isValidUserId("12345"));
    }

    @Test
    void testInvalidUserId_TooLong() {
        assertFalse(UserValidator.isValidUserId("12345678910"));
    }

    @Test
    void testInvalidUserId_NotAlphanumeric_Dollar() {
        assertFalse(UserValidator.isValidUserId("1A$B3C4D5"));
    }

    @Test
    void testInvalidUserId_NotAlphanumeric_Exclamation() {
        assertFalse(UserValidator.isValidUserId("1A2B3C4D!"));
    }

    @Test
    void testInvalidUserId_DoesNotStartWithDigit() {
        assertFalse(UserValidator.isValidUserId("A12345678"));
    }

    @Test
    void testInvalidUserId_LastTwoCharsBothLetters1() {
        assertFalse(UserValidator.isValidUserId("1A2B3CDDE"));
    }

    @Test
    void testInvalidUserId_LastTwoCharsBothLetters2() {
        assertFalse(UserValidator.isValidUserId("1A2B323CE"));
    }
}