package com.example.allinworks.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Common {

    public static String getNumber() {
        Random random = new Random();
        int number = random.nextInt(1000000); // 0 ~ 999999
        return String.format("%06d", number);
    }

    public static int getNumberInt() {
        Random random = new Random();
        return random.nextInt(1000000);
    }


    public static String dateTimeFormat() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String dateTimeFormat2(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }



}
