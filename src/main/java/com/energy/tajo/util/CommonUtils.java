package com.energy.tajo.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class CommonUtils {

    private CommonUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String getE164FormatPhoneNumber(String phoneNum) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(phoneNum, "KR");
            return phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            throw new IllegalArgumentException("전화번호 형식이 유효하지 않습니다.", e);
        }
    }
}

