package org.example.validator;

import org.example.constant.ValidationMessages;

public class MovieValidator {

    public static String validateTitle(String title) {
        if (title == null || title.isEmpty())
            return String.format(ValidationMessages.ERROR_MOVIE_TITLE, title == null ? "" : title);

        String[] words = title.split(" ");
        for (String word : words) {
            String w = word.trim();
            if (w.isEmpty())
                continue;
            if (!Character.isUpperCase(w.charAt(0))) {
                return String.format(ValidationMessages.ERROR_MOVIE_TITLE, title);
            }
        }
        return ValidationMessages.VALID;
    }

    public static String validateId(String id, String title) {
        if (id == null || id.length() < 4)
            // Falls under "letters ... wrong" if prefix missing or malformed?
            // "Movie Id letters {id} are wrong"
            return String.format(ValidationMessages.ERROR_MOVIE_ID, id);

        StringBuilder capsInTitle = new StringBuilder();
        for (char c : title.toCharArray()) {
            if (Character.isUpperCase(c)) {
                capsInTitle.append(c);
            }
        }
        String expectedPrefix = capsInTitle.toString();

        if (!id.startsWith(expectedPrefix)) {
            // Prefix wrong -> letters wrong
            return String.format(ValidationMessages.ERROR_MOVIE_ID, id);
        }

        String suffix = id.substring(expectedPrefix.length());

        // Requirements: "3 unique numbers".
        // Suffix must be 3 digits.
        if (suffix.length() != 3 || !suffix.matches("\\d{3}")) {
            return String.format(ValidationMessages.ERROR_MOVIE_ID_UNIQUE, id);
        }

        return ValidationMessages.VALID;
    }

    public static String validateMovieId(String id, String title) {
        return validateId(id, title);
    }
}
