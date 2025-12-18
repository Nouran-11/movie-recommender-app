package org.example.validator;

import org.example.constant.ValidationMessages;

public class UserValidator {

    public static String validateName(String name) {
        if (name == null || name.isEmpty())
            return String.format(ValidationMessages.ERROR_USER_NAME, name == null ? "" : name);
        if (!name.matches("^[a-zA-Z][a-zA-Z\\s]*$")) {
            return String.format(ValidationMessages.ERROR_USER_NAME, name);
        }
        return ValidationMessages.VALID;
    }

    public static String validateUserId(String id) {
        if (id == null) // Special case for formatting null
            return String.format(ValidationMessages.ERROR_USER_ID, "null");

        if (id.length() != 9)
            return String.format(ValidationMessages.ERROR_USER_ID, id);

        if (!id.matches("[a-zA-Z0-9]+"))
            return String.format(ValidationMessages.ERROR_USER_ID, id);

        if (!Character.isDigit(id.charAt(0)))
            return String.format(ValidationMessages.ERROR_USER_ID, id);

        char lastChar = id.charAt(8);
        boolean lastIsLetter = Character.isLetter(lastChar);

        if (lastIsLetter) {
            if (Character.isLetter(id.charAt(7)))
                return String.format(ValidationMessages.ERROR_USER_ID, id);
        }

        return ValidationMessages.VALID;
    }
}
