package com.xclone.xclone.utils;

import java.time.Instant;

public class TestConstants {

    // ----- Hashtags -----
    public static final String HASHTAG = "#JetBrains";

    // ----- User 1 -----
    public static final Integer USER_ID = 2;
    public static final String USERNAME = "iamthezuck";
    public static final String DISPLAY_NAME = "MrMarkZuckerBerg";
    public static final String USER_EMAIL = "zuck@facebook.com";
    public static final String ABOUT = "Hello world!";
    public static final String USER_CREATED_AT = "2025-08-01T23:34:32";
    public static final String PFP_SRC_1 = "https://storage.googleapis.com/xclone-media/deffour.png";
    public static final String BANNER_SRC = "https://storage.googleapis.com/xclone-media/defaultBanner.jpg";
    public static final Integer PINNED_TWEET_ID = 40;

    // ----- User 2 -----
    public static final Integer USER_ID_2 = 3;
    public static final String USERNAME_2 = "John_Doe";
    public static final String DISPLAY_NAME_2 = "Hello twitter!";
    public static final String USER_EMAIL_2 = "john@example.com";
    public static final String PFP_SRC_2 = "https://storage.googleapis.com/xclone-media/deffive.png";

    // ----- User 3 -----
    public static final Integer USER_ID_3 = 4;
    public static final String USERNAME_3 = "Jane_Smith";
    public static final String DISPLAY_NAME_3 = "Jane Smith";
    public static final String USER_EMAIL_3 = "jane@example.com";
    public static final String PFP_SRC_3 = "https://storage.googleapis.com/xclone-media/defsix.png";

    // ----- Auth -----
    public static final String AUTH_TOKEN = "eyJhbGciOiJIUzI1NiJ9....";

    // ----- Tweet IDs & Text -----
    public static final Integer TWEET_ID = 11;
    public static final String TWEET_CREATED_AT = "2024-10-03T20:34:15";
    public static final String TWEET_TEXT = "#JetBrains https://www.jetbrains.com/ ";

    public static final String TWEET_TEXT_1 = "first test tweet";
    public static final String TWEET_TEXT_2 = "second test tweet with hashtag " + HASHTAG;
    public static final String TWEET_TEXT_3 = "third test tweet plain text";

    // ----- Poll -----
    public static final Integer POLL_ID = 1;
    public static final Integer POLL_CHOICE_ID = 1;
    public static final String POLL_CHOICE_1 = "test choice 1";
    public static final String POLL_CHOICE_2 = "test choice 2";

    // ----- Time -----
    public static final Instant TIME_1 = Instant.parse("2025-01-01T10:00:00Z");
    public static final Instant TIME_2 = Instant.parse("2025-01-01T10:05:00Z");
    public static final Instant TIME_3 = Instant.parse("2025-01-01T10:10:00Z");
    public static final Instant CURSOR_TIME = TIME_3.plusSeconds(60);
}