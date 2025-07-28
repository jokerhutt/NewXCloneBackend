package com.xclone.xclone.util;

import com.xclone.xclone.domain.post.poll.Poll;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class PollUtils {

    public static boolean checkPollExpiry (Poll poll) {
        return poll.getExpiresAt().before(new Timestamp(System.currentTimeMillis()));
    }

    public static Timestamp parsePollExpiryToTimeStamp(List<String> pollExpiry) {
        int days = Integer.parseInt(pollExpiry.get(0));
        int hours = Integer.parseInt(pollExpiry.get(1));
        int minutes = Integer.parseInt(pollExpiry.get(2));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusDays(days).plusHours(hours).plusMinutes(minutes);
        return Timestamp.valueOf(expiration);
    }

}
