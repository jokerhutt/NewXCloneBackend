package com.xclone.xclone.util;

import java.util.Map;

public class MessageParser {

    public static Map<String, String> parseMessage(String message) {
        return Map.of("message", message);
    }

}
