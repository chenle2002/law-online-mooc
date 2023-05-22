package com.chenle.commonutils;

public class RedisUtil {
    public static final String SEPARATOR = "::";


    public static String buildKey(Object... args) {
        if (args.length < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(args[0]);
        for (int i = 1; i < args.length; i++) {
            sb.append(SEPARATOR).append(args[i]);
        }
        return sb.toString();
    }

}
