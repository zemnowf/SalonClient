package com.zemno.clientapplication.utils;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NumberValidationService {

    private final static String DOUBLE_REGEX = "^[0-9]{1,5}+.[0-9]{2}+$";

    private final Pattern doublePattern = Pattern.compile(DOUBLE_REGEX);

    public boolean isValidDouble(String number){
        if (number == "") {
            return false;
        }
        Matcher loginMatcher = doublePattern.matcher(number);

        return loginMatcher.matches();

    }
}
