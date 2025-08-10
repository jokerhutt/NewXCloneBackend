package com.xclone.xclone.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.TestingAuthenticationToken;

public final class MvcTestHelper {

    private MvcTestHelper() {
    }

    public static TestingAuthenticationToken authenticationToken(int authenticatedUserId) {
        return new TestingAuthenticationToken(Integer.valueOf(authenticatedUserId), null);
    }

    public static String toJson(ObjectMapper objectMapper, Object objectToSerialize) {
        try {
            return objectMapper.writeValueAsString(objectToSerialize);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}