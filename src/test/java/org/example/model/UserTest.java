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
        assertEquals("Valid", UserValidator.validateName("Menna"));
    }

    @Test
    void testValidName_WithSpace() {
        assertEquals("Valid", UserValidator.validateName("John Doe"));
    }

    @Test
    void testInvalidName_Null() {
        assertEquals("ERROR: User Name null is wrong", UserValidator.validateName(null));
    }

    @Test
    void testInvalidName_Empty() {
        assertEquals("ERROR: User Name  is wrong", UserValidator.validateName(""));
    }

    @Test
    void testInvalidName_LeadingSpace() {
        // " Menna" -> matches regex "^[a-zA-Z][a-zA-Z\\s]*$" ?
        // "^[a-zA-Z]" -> starts with letter. " Menna" starts with space. So no match.
        assertEquals("ERROR: User Name   Menna is wrong", UserValidator.validateName("  Menna"));
    }

    @Test
    void testInvalidName_StartsWithDigit() {
        assertEquals("ERROR: User Name 123Menna is wrong", UserValidator.validateName("123Menna"));
    }

    @Test
    void testInvalidName_SpecialChars() {
        assertEquals("ERROR: User Name Menna! is wrong", UserValidator.validateName("Menna!"));
    }

    // User ID Validation Tests
    @Test
    void testValidUserId_EndsWithDigit() {
        assertEquals("Valid", UserValidator.validateUserId("1A2B3C4D5"));
    }

    @Test
    void testValidUserId_EndsWithDigit2() {
        assertEquals("Valid", UserValidator.validateUserId("1A2B3C4D6"));
    }

    @Test
    void testValidUserId_EndsWithLetter() {
        assertEquals("Valid", UserValidator.validateUserId("1A2B3C43E"));
    }

    @Test
    void testInvalidUserId_Null() {
        assertEquals("ERROR: User Id null is wrong", UserValidator.validateUserId(null));
    }

    @Test
    void testInvalidUserId_TooShort() {
        assertEquals("ERROR: User Id 12345 is wrong", UserValidator.validateUserId("12345"));
    }

    @Test
    void testInvalidUserId_TooLong() {
        assertEquals("ERROR: User Id 12345678910 is wrong", UserValidator.validateUserId("12345678910"));
    }

    @Test
    void testInvalidUserId_NotAlphanumeric_Dollar() {
        assertEquals("ERROR: User Id 1A$B3C4D5 is wrong", UserValidator.validateUserId("1A$B3C4D5"));
    }

    @Test
    void testInvalidUserId_NotAlphanumeric_Exclamation() {
        assertEquals("ERROR: User Id 1A2B3C4D! is wrong", UserValidator.validateUserId("1A2B3C4D!"));
    }

    @Test
    void testInvalidUserId_DoesNotStartWithDigit() {
        assertEquals("ERROR: User Id A12345678 is wrong", UserValidator.validateUserId("A12345678"));
    }

    @Test
    void testInvalidUserId_LastTwoCharsBothLetters1() {
        assertEquals("ERROR: User Id 1A2B3CDDE is wrong", UserValidator.validateUserId("1A2B3CDDE"));
    }

    @Test
    void testInvalidUserId_LastTwoCharsBothLetters2() {
        assertEquals("ERROR: User Id 1A2B323CE is wrong", UserValidator.validateUserId("1A2B323CE"));
    }
}