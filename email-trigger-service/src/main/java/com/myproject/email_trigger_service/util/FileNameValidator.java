package com.myproject.email_trigger_service.util;

import com.myproject.email_trigger_service.constants.EmailTriggerConstants;

public class FileNameValidator {
    private static final String Regex= EmailTriggerConstants.CAID_REGEX;

    public static boolean isValid(String fileName){
        return fileName.matches(Regex);
    }
}
