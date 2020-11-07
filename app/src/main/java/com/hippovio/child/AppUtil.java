package com.hippovio.child;

public class AppUtil {
    public static String getPlainPhoneNumber(String formattedNumber){
        return formattedNumber.replaceFirst("\\+", "00").replaceAll("[()\\s-]+", "");
    }
}
