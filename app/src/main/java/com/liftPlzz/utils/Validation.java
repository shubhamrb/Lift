package com.liftPlzz.utils;

import android.util.Patterns;

public class Validation {

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
