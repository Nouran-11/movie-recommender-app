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

    @Test
    public void testIsValidTitle_SimpleTitle() {
        assertEquals("Valid", MovieValidator.validateTitle("Pride And Prejudice"),
                "Title starting with capital letters should be valid");
    }

    @Test
    public void testIsValidTitle_MultiWordTitle() {
        assertEquals("Valid", MovieValidator.validateTitle("A B C"),
                "Title with multiple words starting with uppercase should be valid");
    }

    // Negative Tests
    @Test
    public void testIsValidTitle_LowerCaseStart() {
        assertEquals("ERROR: Movie Title pride And Prejudice is wrong",
                MovieValidator.validateTitle("pride And Prejudice"),
                "Title starting with lowercase should be invalid");
    }

    @Test
    public void testIsValidTitle_Empty() {
        assertEquals("ERROR: Movie Title  is wrong", MovieValidator.validateTitle(""), "Empty title should be invalid");
    }

    @Test
    public void testIsValidTitle_Null() {
        assertEquals("ERROR: Movie Title null is wrong", MovieValidator.validateTitle(null),
                "Null title should be invalid");
    }

    @Test
    public void testIsValidTitle_SpecialCharStart() {
        // "-Pride And Prejudice" splits to "-Pride", "And", "Prejudice". "-Pride"
        // charAt(0) is '-'. isUpperCase('-') is false.
        assertEquals("ERROR: Movie Title -Pride And Prejudice is wrong",
                MovieValidator.validateTitle("-Pride And Prejudice"),
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
        assertEquals("ERROR: Movie Id SAP123 is wrong", MovieValidator.validateId("SAP123", title),
                "Wrong prefix should fail");
    }

    @Test
    public void testValidateId_MissingPrefix() {
        String title = "Pride And Prejudice";
        assertEquals("ERROR: Movie Id AP123 is wrong", MovieValidator.validateId("AP123", title),
                "Missing prefix should fail");
    }

    @Test
    public void testValidateId_NullId() {
        String title = "Pride And Prejudice";
        assertEquals("ERROR: Movie Id null is wrong", MovieValidator.validateId(null, title),
                "Null ID should fail");
    }

    @Test
    public void testValidateId_TooShortSuffix() {
        String title = "Pride And Prejudice";
        // PAP12 -> suffix 12. length 2.
        assertEquals("ERROR: Movie Id PAP12 isn't unique", MovieValidator.validateId("PAP12", title),
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
        // PAP12A -> suffix 12A
        assertEquals("ERROR: Movie Id PAP12A isn't unique", MovieValidator.validateId("PAP12A", title),
                "Non-digit suffix should fail");
    }

    @Test
    public void testValidateId_TooLongSuffix() {
        String title = "Pride And Prejudice";
        // PAP1234 -> suffix 1234. length 4.
        // suffix substring(0,1) = 1. matches digits.
        // length != 3 check.
        assertEquals("ERROR: Movie Id PAP1234 isn't unique", MovieValidator.validateId("PAP1234", title),
                "More than 3 digits suffix should fail");
    }
}
