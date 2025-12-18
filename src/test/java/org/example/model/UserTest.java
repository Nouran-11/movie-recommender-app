package org.example.model;

import org.example.constant.ValidationMessages;
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
        assertEquals(ValidationMessages.VALID, UserValidator.validateName("Menna"));
    }

    @Test
    void testValidName_WithSpace() {
        assertEquals(ValidationMessages.VALID, UserValidator.validateName("John Doe"));
    }

    @Test
    void testInvalidName_Null() {
        // null safe format in validator: name == null ? "" : name
        assertEquals(String.format(ValidationMessages.ERROR_USER_NAME, ""), UserValidator.validateName(null));
    }

    @Test
    void testInvalidName_Empty() {
        assertEquals(String.format(ValidationMessages.ERROR_USER_NAME, ""), UserValidator.validateName(""));
    }

    @Test
    void testInvalidName_LeadingSpace() {
        assertEquals(String.format(ValidationMessages.ERROR_USER_NAME, "  Menna"),
                UserValidator.validateName("  Menna"));
    }

    @Test
    void testInvalidName_StartsWithDigit() {
        assertEquals(String.format(ValidationMessages.ERROR_USER_NAME, "123Menna"),
                UserValidator.validateName("123Menna"));
    }

    @Test
    void testInvalidName_SpecialChars() {
        assertEquals(String.format(ValidationMessages.ERROR_USER_NAME, "Menna!"), UserValidator.validateName("Menna!"));
    }

    // User ID Validation Tests
    @Test
    void testValidUserId_EndsWithDigit() {
        assertEquals(ValidationMessages.VALID, UserValidator.validateUserId("1A2B3C4D5"));
    }

    @Test
    void testValidUserId_EndsWithDigit2() {
        assertEquals(ValidationMessages.VALID, UserValidator.validateUserId("1A2B3C4D6"));
    }

    @Test
    void testValidUserId_EndsWithLetter() {
        assertEquals(ValidationMessages.VALID, UserValidator.validateUserId("1A2B3C43E"));
    }

    @Test
    void testInvalidUserId_Null() {
        // null check in validator: returns format w/ "null" string
        assertEquals(String.format(ValidationMessages.ERROR_USER_ID, "null"), UserValidator.validateUserId(null));
    }

    @Test
    void testInvalidUserId_TooShort() {
        assertEquals(String.format(ValidationMessages.ERROR_USER_ID, "12345"), UserValidator.validateUserId("12345"));
    }

    @Test
    void testInvalidUserId_TooLong() {
        assertEquals(String.format(ValidationMessages.ERROR_USER_ID, "12345678910"),
                UserValidator.validateUserId("12345678910"));
    }

    @Test
    void testInvalidUserId_NotAlphanumeric_Dollar() {
        assertEquals(String.format(ValidationMessages.ERROR_USER_ID, "1A$B3C4D5"),
                UserValidator.validateUserId("1A$B3C4D5"));
    }

    @Test
    void testInvalidUserId_NotAlphanumeric_Exclamation() {
        assertEquals(String.format(ValidationMessages.ERROR_USER_ID, "1A2B3C4D!"),
                UserValidator.validateUserId("1A2B3C4D!"));
    }

    @Test
    void testInvalidUserId_DoesNotStartWithDigit() {
        assertEquals(String.format(ValidationMessages.ERROR_USER_ID, "A12345678"),
                UserValidator.validateUserId("A12345678"));
    }

    @Test
    void testInvalidUserId_LastTwoCharsBothLetters1() {
        assertEquals(String.format(ValidationMessages.ERROR_USER_ID, "1A2B3CDDE"),
                UserValidator.validateUserId("1A2B3CDDE"));
    }

    @Test
    void testInvalidUserId_LastTwoCharsBothLetters2() {
        assertEquals(String.format(ValidationMessages.ERROR_USER_ID, "1A2B323CE"),
                UserValidator.validateUserId("1A2B323CE"));
    }
}