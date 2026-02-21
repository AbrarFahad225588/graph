package com.example.graph.auth;

import java.util.regex.Pattern;

public class ValidatorUtils{
        // A common regex for email format validation (more flexible options exist)
        private static final String EMAIL_REGEX =
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        public static boolean isValidEmailFormat(String email) {
            if (email == null) {
                return false;
            }
            Pattern pattern = Pattern.compile(EMAIL_REGEX);
            return pattern.matcher(email).matches();
        }
    public static boolean isValidPassword(String password) {

//        String regex = "^(?=.[a-zA-Z])(?=.[0-9])(?=.[!@#$%^&()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
//        return password != null && password.matches(regex);
        return password != null && password.length()>=8;
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // A basic regex for a 10-digit US phone number (e.g., 1234567890)
        String regex = "\\d{10}";

        // For a more flexible international format, including optional country code and separators:
        // String regex = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$";

        if (phoneNumber != null && phoneNumber.matches(regex)) {
            return true;
        } else {
            return false;
        }
    }
}
