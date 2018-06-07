package com.templateService.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateValue {
    private static final String translationKeyValue = "#([^#]*)#";

    private static Pattern hashTagEnclosedPattern = Pattern.compile(translationKeyValue);

    public static String isValue (String value) {

//        Matcher matcher = hashTagEnclosedPattern.matcher("#" + "787878787"  +  "#");
        Matcher matcher = hashTagEnclosedPattern.matcher(value);
        if (matcher.find()) {
//            System.out.println(matcher.group(1));
            return  matcher.group(1);
        }
        else return "";
    }
}