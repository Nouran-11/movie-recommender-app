package org.example.model;

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

    // Happy Path Tests
    @Test
    public void testIsValidTitle_SimpleTitle() {
        assertTrue(MovieValidator.isValidTitle("Pride And Prejudice"),
                "Title starting with capital letters should be valid");
    }

    @Test
    public void testIsValidTitle_MultiWordTitle() {
        assertTrue(MovieValidator.isValidTitle("A B C"),
                "Title with multiple words starting with uppercase should be valid");
    }

    // Negative Tests
    @Test
    public void testIsValidTitle_LowerCaseStart() {
        assertFalse(MovieValidator.isValidTitle("pride And Prejudice"),
                "Title starting with lowercase should be invalid");
    }

    @Test
    public void testIsValidTitle_Empty() {
        assertFalse(MovieValidator.isValidTitle(""), "Empty title should be invalid");
    }

    @Test
    public void testIsValidTitle_Null() {
        assertFalse(MovieValidator.isValidTitle(null), "Null title should be invalid");
    }

    @Test
    public void testIsValidTitle_SpecialCharStart() {
        assertFalse(MovieValidator.isValidTitle("-Pride And Prejudice"),
                "Title starting with a special character should be invalid");
    }

    // ID Validation Tests
    @Test
    public void testValidateId_CorrectId1() {
        String title = "Pride And Prejudice";
        assertEquals("Valid", MovieValidator.validateId("PAP123", title), "Correct ID should be valid");
    }

    @Test
    public void testValidateId_CorrectId2() {
        String title = "Pride And Prejudice";
        assertEquals("Valid", MovieValidator.validateId("PAP456", title), "Another correct ID should be valid");
    }

    @Test
    public void testValidateId_WrongPrefix() {
        String title = "Pride And Prejudice";
        assertEquals("Movie Id letters {movie_id} are wrong", MovieValidator.validateId("SAP123", title),
                "Wrong prefix should fail");
    }

    @Test
    public void testValidateId_MissingPrefix() {
        String title = "Pride And Prejudice";
        assertEquals("Movie Id letters {movie_id} are wrong", MovieValidator.validateId("AP123", title),
                "Missing prefix should fail");
    }

    @Test
    public void testValidateId_NullId() {
        String title = "Pride And Prejudice";
        assertEquals("Movie Id letters {movie_id} are wrong", MovieValidator.validateId(null, title),
                "Null ID should fail");
    }

    @Test
    public void testValidateId_TooShortSuffix() {
        String title = "Pride And Prejudice";
        assertEquals("Movie Id numbers {movie_id} aren't unique", MovieValidator.validateId("PAP12", title),
                "Too short suffix should fail");
    }

    @Test
    public void testValidateId_RepeatedDigitsAreValid() {
        String title = "Pride And Prejudice";
        assertEquals("Valid", MovieValidator.validateId("PAP112", title), "Repeated digits is valid ");
    }

    @Test
    public void testValidateId_NonDigitSuffix() {
        String title = "Pride And Prejudice";
        assertEquals("Movie Id numbers {movie_id} aren't unique", MovieValidator.validateId("PAP12A", title),
                "Non-digit suffix should fail");
    }

    @Test
    public void testValidateId_TooLongSuffix() {
        String title = "Pride And Prejudice";
        assertEquals("Movie Id numbers {movie_id} aren't unique", MovieValidator.validateId("PAP1234", title),
                "More than 3 digits suffix should fail");
    }
}
