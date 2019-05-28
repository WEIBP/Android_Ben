package com.ben.library;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

public class RegexUtils {

    public static boolean checkPhone(@NonNull String phone){
        //校验身份证
        String regEx = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(phone.trim());
        return matcher.matches();

    }

    public static boolean checkCardId(@NonNull String cardId){
        //校验身份证
        String regEx = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(cardId.trim());
        return matcher.matches();
    }

}
