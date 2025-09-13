package org.unimanage.util;

public class Creator {

    public static String personalCode(String nationalCode, String phoneNumber) {
        String nationalCodePart = nationalCode.substring(0, 5);
        String phoneNumberPart = phoneNumber.substring(0, 5);

        return nationalCodePart + phoneNumberPart;
    }
}
