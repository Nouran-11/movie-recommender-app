package org.example.validator;

public class MovieValidator {

    public static String validateTitle(String title) {
        if (title == null || title.isEmpty())
            return "ERROR: Movie Title " + title + " is wrong";

        String[] words = title.split(" ");
        for (String word : words) {
            if (word.isEmpty())
                continue;
            if (!Character.isUpperCase(word.charAt(0))) {
                return "ERROR: Movie Title " + title + " is wrong";
            }
        }
        return "Valid";
    }

    public static String validateId(String id, String title) {
        if (id == null || id.length() < 4)
            return "ERROR: Movie Id " + id + " is wrong";

        StringBuilder capsInTitle = new StringBuilder();
        for (char c : title.toCharArray()) {
            if (Character.isUpperCase(c)) {
                capsInTitle.append(c);
            }
        }
        String expectedPrefix = capsInTitle.toString();

        if (!id.startsWith(expectedPrefix)) {
            return "ERROR: Movie Id " + id + " is wrong";
        }

        String suffix = id.substring(expectedPrefix.length());

        if (suffix.length() > 3 && !suffix.substring(0, suffix.length() - 3).matches("\\d*")) {
            return "ERROR: Movie Id " + id + " is wrong";
        }

        if (suffix.length() != 3 || !suffix.matches("\\d{3}")) {
            return "ERROR: Movie Id " + id + " isn't unique";
        }

        return "Valid";
    }

    public static String validateMovieId(String id, String title) {
        return validateId(id, title);
    }
}
