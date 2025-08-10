package com.xclone.xclone.helpers;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExceptionAssertHelper {
    public static void assertIllegalState(Runnable executable) {
        assertThrows(IllegalStateException.class, executable::run);
    }
}
