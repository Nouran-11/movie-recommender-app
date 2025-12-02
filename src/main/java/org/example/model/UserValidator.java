package org.example.model;

// ===================== UserValidate Class ==================== //

public class UserValidator{

    // ------ Validate MovieId ------ //
    public static boolean isValidMovieId(String movieId){
        if(movieId ==null || movieId.isEmpty()) return false;
        else return true;
    }

    // -------- Validate User name ------ //
    public static boolean isValidName(String name) {
        if (name == null || name.isEmpty()) return false;
        return name.matches("^[a-zA-Z][a-zA-Z\\s]*$");
    }

    // -------- Validate User ID ------ //
    public static boolean isValidUserId(String id) {

        if (id == null || id.length() != 9) return false;

        if (!id.matches("[a-zA-Z0-9]+")) return false;

        if (!Character.isDigit(id.charAt(0))) return false;

        char lastChar = id.charAt(8);
        boolean lastIsLetter = Character.isLetter(lastChar);

        if (lastIsLetter && Character.isLetter(id.charAt(7))) return false;

        return true;
    }
}
