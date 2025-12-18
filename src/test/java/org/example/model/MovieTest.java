package org.example.model;

import org.example.validator.MovieValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for Movie class
 */
public class MovieTest {

    private Movie movie;

    public MovieTest() {
    }

    @BeforeAll
    public static void setUpClass() {

    }

    @AfterAll
    public static void tearDownClass() {

    }

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
        String expected = "Pride And Prejudice";
        assertEquals(expected, movie.getTitle(), "getTitle should return the correct title");
    }

    @Test
    public void testGetId() {
        String expected = "PAP123";
        assertEquals(expected, movie.getId(), "getId should return the correct id");
    }

    @Test
    public void testGetGenre() {
        String expected = "Romance,Drama";
        assertEquals(expected, movie.getGenre(), "getGenre should return the correct genre");
    }

    @Test
    public void testIsValidTitle_positive() {
        assertTrue(MovieValidator.isValidTitle("Pride And Prejudice"),
                "Title starting with capital letters should be valid");
        assertTrue(MovieValidator.isValidTitle("A B C"),
                "Title with multiple words starting with uppercase should be valid");

    }

    @Test
    public void testIsValidTitle_negative() {
        assertFalse(MovieValidator.isValidTitle("pride And Prejudice"),
                "Title starting with lowercase should be invalid");
        assertFalse(MovieValidator.isValidTitle(""), "Empty title should be invalid");
        assertFalse(MovieValidator.isValidTitle(null), "Null title should be invalid");
        assertFalse(MovieValidator.isValidTitle("-Pride And Prejudice"),
                "Title starting with a special character should be invalid");
    }

    @Test
    public void testValidateId_valid() {
        String title = "Pride And Prejudice";
        assertEquals("Valid", MovieValidator.validateId("PAP123", title), "Correct ID should be valid");
        assertEquals("Valid", MovieValidator.validateId("PAP456", title), "Another correct ID should be valid");
    }

    @Test
    public void testValidateId_invalidLetters() {
        String title = "Pride And Prejudice";
        assertEquals("Movie Id letters {movie_id} are wrong", MovieValidator.validateId("SAP123", title),
                "Wrong prefix should fail");
        assertEquals("Movie Id letters {movie_id} are wrong", MovieValidator.validateId("AP123", title),
                "Missing prefix should fail");
        assertEquals("Movie Id letters {movie_id} are wrong", MovieValidator.validateId(null, title),
                "Null ID should fail");
    }

    @Test
    public void testValidateId_invalidNumbers() {
        String title = "Pride And Prejudice";
        assertEquals("Movie Id numbers {movie_id} aren't unique", MovieValidator.validateId("PAP12", title),
                "Too short suffix should fail");
        assertEquals("Valid", MovieValidator.validateId("PAP112", title), "Repeated digits should be valid now");
        assertEquals("Movie Id numbers {movie_id} aren't unique", MovieValidator.validateId("PAP12A", title),
                "Non-digit suffix should fail");
        assertEquals("Movie Id numbers {movie_id} aren't unique", MovieValidator.validateId("PAP1234", title),
                "More than 3 digits suffix should fail");
    }
}
