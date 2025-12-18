package org.example.model;

import org.example.constant.ValidationMessages;
import org.example.validator.MovieValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for Movie class
 */
public class MovieTest {

    private Movie movie;

    @BeforeEach
    public void setUp() {
        movie = new Movie("Pride And Prejudice", "PAP123", "Romance,Drama");
    }

    @AfterEach
    public void tearDown() {
        movie = null;
    }

    @Test
    public void testGetTitle() {
        assertEquals("Pride And Prejudice", movie.getTitle(), "getTitle should return the correct title");
    }

    @Test
    public void testGetId() {
        assertEquals("PAP123", movie.getId(), "getId should return the correct id");
    }

    @Test
    public void testGetGenre() {
        assertEquals("Romance,Drama", movie.getGenre(), "getGenre should return the correct genre");
    }

    @Test
    public void testIsValidTitle_SimpleTitle() {
        assertEquals(ValidationMessages.VALID, MovieValidator.validateTitle("Pride And Prejudice"),
                "Title starting with capital letters should be valid");
    }

    @Test
    public void testIsValidTitle_MultiWordTitle() {
        assertEquals(ValidationMessages.VALID, MovieValidator.validateTitle("A B C"),
                "Title with multiple words starting with uppercase should be valid");
    }

    // Negative Tests
    @Test
    public void testIsValidTitle_LowerCaseStart() {
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_TITLE, "pride And Prejudice"),
                MovieValidator.validateTitle("pride And Prejudice"),
                "Title starting with lowercase should be invalid");
    }

    @Test
    public void testIsValidTitle_Empty() {
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_TITLE, ""), MovieValidator.validateTitle(""),
                "Empty title should be invalid");
    }

    @Test
    public void testIsValidTitle_Null() {
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_TITLE, ""), MovieValidator.validateTitle(null),
                "Null title should be invalid");
    }

    @Test
    public void testIsValidTitle_SpecialCharStart() {
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_TITLE, "-Pride And Prejudice"),
                MovieValidator.validateTitle("-Pride And Prejudice"),
                "Title starting with a special character should be invalid");
    }

    // ID Validation Tests
    @Test
    public void testValidateId_CorrectId1() {
        String title = "Pride And Prejudice";
        assertEquals(ValidationMessages.VALID, MovieValidator.validateId("PAP123", title),
                "Correct ID should be valid");
    }

    @Test
    public void testValidateId_CorrectId2() {
        String title = "Pride And Prejudice";
        assertEquals(ValidationMessages.VALID, MovieValidator.validateId("PAP456", title),
                "Another correct ID should be valid");
    }

    @Test
    public void testValidateId_WrongPrefix() {
        String title = "Pride And Prejudice";
        // Logic: prefix wrong -> "letters ... wrong" -> ERROR_MOVIE_ID
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_ID, "SAP123"),
                MovieValidator.validateId("SAP123", title),
                "Wrong prefix should fail with letters error");
    }

    @Test
    public void testValidateId_MissingPrefix() {
        String title = "Pride And Prejudice";
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_ID, "AP123"),
                MovieValidator.validateId("AP123", title),
                "Missing prefix should fail with letters error");
    }

    @Test
    public void testValidateId_NullId() {
        String title = "Pride And Prejudice";
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_ID, "null"), MovieValidator.validateId(null, title),
                "Null ID should fail with letters error");
    }

    @Test
    public void testValidateId_TooShortSuffix() {
        String title = "Pride And Prejudice";
        // Suffix "12". length 2. Requirement: "3 unique numbers".
        // Code: returns ERROR_MOVIE_ID_UNIQUE ("numbers ... aren't unique").
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_ID_UNIQUE, "PAP12"),
                MovieValidator.validateId("PAP12", title),
                "Too short suffix should fail with numbers error");
    }

    @Test
    public void testValidateId_RepeatedDigitsAreValidInValidator() {
        String title = "Pride And Prejudice";
        // PAP112. Digits 1,1,2. Code: checks format (length 3, digits).
        // Validator returns Valid. Global uniqueness is checked in FileHandler.
        // User clarified: "112 is valid".
        assertEquals(ValidationMessages.VALID, MovieValidator.validateId("PAP112", title),
                "Repeated digits (112) should be format-valid");
    }

    @Test
    public void testValidateId_NonDigitSuffix() {
        String title = "Pride And Prejudice";
        // PAP12A. Suffix "12A". Not numeric.
        // Code: returns ERROR_MOVIE_ID_UNIQUE ("numbers ... aren't unique").
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_ID_UNIQUE, "PAP12A"),
                MovieValidator.validateId("PAP12A", title),
                "Non-digit suffix should fail with numbers error");
    }

    @Test
    public void testValidateId_TooLongSuffix() {
        String title = "Pride And Prejudice";
        // PAP1234. Suffix 1234. Length 4.
        // Falls under "numbers ... aren't unique" category?
        // Wait, current code logic for suffix.length() > 3:
        // returns ERROR_MOVIE_ID_UNIQUE now.
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_ID_UNIQUE, "PAP1234"),
                MovieValidator.validateId("PAP1234", title),
                "More than 3 digits suffix should fail with numbers error");
    }
}
