package org.example.validator;

public class UserValidator {

    public static String validateName(String name) {
        if (name == null || name.isEmpty())
            return "ERROR: User Name " + name + " is wrong";
        if (!name.matches("^[a-zA-Z][a-zA-Z\\s]*$")) {
            return "ERROR: User Name " + name + " is wrong";
        }
        return "Valid";
    }

    public static String validateUserId(String id) {
        if (id == null || id.length() != 9)
            return "ERROR: User Id " + id + " is wrong";

        if (!id.matches("[a-zA-Z0-9]+"))
            return "ERROR: User Id " + id + " is wrong";

        if (!Character.isDigit(id.charAt(0)))
            return "ERROR: User Id " + id + " is wrong";

        char lastChar = id.charAt(8);
        boolean lastIsLetter = Character.isLetter(lastChar);

        if (lastIsLetter) {
            if (Character.isLetter(id.charAt(7)))
                return "ERROR: User Id " + id + " is wrong";
        }

        return "Valid";
    }
}
