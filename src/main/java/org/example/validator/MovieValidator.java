package org.example.validator;

public class MovieValidator {

    public static boolean isValidTitle(String title) {
        if (title == null || title.isEmpty())
            return false;

        String[] words = title.split(" ");
        for (String word : words) {
            if (word.isEmpty())
                continue;
            if (!Character.isUpperCase(word.charAt(0))) {
                return false;
            }
        }
        return true;
    }

    public static String validateId(String id, String title) {
        if (id == null || id.length() < 4)
            return "Movie Id letters {movie_id} are wrong";

        StringBuilder capsInTitle = new StringBuilder();
        for (char c : title.toCharArray()) {
            if (Character.isUpperCase(c)) {
                capsInTitle.append(c);
            }
        }
        String expectedPrefix = capsInTitle.toString();

        if (!id.startsWith(expectedPrefix)) {
            return "Movie Id letters {movie_id} are wrong";
        }

        String suffix = id.substring(expectedPrefix.length());

        if (suffix.length() > 3 && !suffix.substring(0, suffix.length() - 3).matches("\\d*")) {
            return "Movie Id letters {movie_id} are wrong";
        }

        if (suffix.length() != 3 || !suffix.matches("\\d{3}")) {
            return "Movie Id numbers {movie_id} aren't unique";
        }

        return "Valid";
    }

    // Handles the true/False isValidMovieID "BOOLEAN wrapper"
    public static boolean isValidMovieId(String id, String title) {
        return validateId(id, title).equals("Valid");
    }
}
