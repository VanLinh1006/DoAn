package com.prepify.be.utils;

import java.util.regex.Pattern;

public class StringUtils {
    public static final String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public static Boolean validEmailString(String email) {
        Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        if (emailPattern.matcher(email).matches()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
