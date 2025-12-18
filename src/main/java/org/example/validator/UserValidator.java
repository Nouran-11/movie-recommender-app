package org.example.validator;

public class UserValidator {

    public static boolean isValidName(String name) {
        if (name == null || name.isEmpty())
            return false;
        return name.matches("^[a-zA-Z][a-zA-Z\\s]*$");
    }

    public static boolean isValidUserId(String id) {
        if (id == null || id.length() != 9)
            return false;

        if (!id.matches("[a-zA-Z0-9]+"))
            return false;

        if (!Character.isDigit(id.charAt(0)))
            return false;

        char lastChar = id.charAt(8);
        boolean lastIsLetter = Character.isLetter(lastChar);

        if (lastIsLetter) {
            if (Character.isLetter(id.charAt(7)))
                return false;
        }

        return true;
    }
}
