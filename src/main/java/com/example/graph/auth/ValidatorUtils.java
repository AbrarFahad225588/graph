package com.example.graph.auth;

import java.util.regex.Pattern;

public class ValidatorUtils{
        private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        public static boolean isValidEmailFormat(String email) {
            if (email == null) {
                return false;
            }
            Pattern pattern = Pattern.compile(EMAIL_REGEX);
            return pattern.matcher(email).matches();
        }
    public static boolean isValidPassword(String password) {

        return password != null && password.length()>=4;
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "\\d{10}";
        if (phoneNumber != null && phoneNumber.matches(regex)) {
            return true;
        } else {
            return false;
        }
    }
}
