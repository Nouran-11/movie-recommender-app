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
        // Empty string -> title == "" ? "" : title -> ""
        // ERROR: Movie Title is wrong
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_TITLE, ""), MovieValidator.validateTitle(""),
                "Empty title should be invalid");
    }

    @Test
    public void testIsValidTitle_Null() {
        // Null -> title == null ? "" : title -> ""
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
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_ID, "SAP123"),
                MovieValidator.validateId("SAP123", title),
                "Wrong prefix should fail");
    }

    @Test
    public void testValidateId_MissingPrefix() {
        String title = "Pride And Prejudice";
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_ID, "AP123"),
                MovieValidator.validateId("AP123", title),
                "Missing prefix should fail");
    }

    @Test
    public void testValidateId_NullId() {
        String title = "Pride And Prejudice";
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_ID, "null"), MovieValidator.validateId(null, title),
                "Null ID should fail");
    }

    @Test
    public void testValidateId_TooShortSuffix() {
        String title = "Pride And Prejudice";
        // Logic check: "PAP12" suffix "12". length 2.
        // Code: suffix.length() != 3 -> returns ERROR_MOVIE_ID_UNIQUE ("isn't unique").
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_ID_UNIQUE, "PAP12"),
                MovieValidator.validateId("PAP12", title),
                "Too short suffix should fail");
    }

    @Test
    public void testValidateId_RepeatedDigitsAreValid() {
        String title = "Pride And Prejudice";
        assertEquals(ValidationMessages.VALID, MovieValidator.validateId("PAP112", title), "Repeated digits is valid ");
    }

    @Test
    public void testValidateId_NonDigitSuffix() {
        String title = "Pride And Prejudice";
        // PAP12A -> suffix 12A. Code matches failure?
        // suffix "12A". length 3. matches \d{3} -> false.
        // Returns ERROR_MOVIE_ID_UNIQUE ("isn't unique").
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_ID_UNIQUE, "PAP12A"),
                MovieValidator.validateId("PAP12A", title),
                "Non-digit suffix should fail");
    }

    @Test
    public void testValidateId_TooLongSuffix() {
        String title = "Pride And Prejudice";
        // PAP1234 -> suffix 1234. length 4.
        // Code: suffix.length() > 3 check kicks in.
        // Return ERROR_MOVIE_ID ("is wrong").
        assertEquals(String.format(ValidationMessages.ERROR_MOVIE_ID, "PAP1234"),
                MovieValidator.validateId("PAP1234", title),
                "More than 3 digits suffix should fail");
    }
}
