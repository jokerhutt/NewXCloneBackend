package com.xclone.xclone.util;

import com.xclone.xclone.domain.user.User;

import java.util.Base64;

public class MediaParsingUtils {

    public static String encodeUserMediaToBase64 (User user, String type) {
        if (type.equals("profilePic")) {
            String profilePic = Base64.getEncoder().encodeToString(user.getProfilePicture());
            return profilePic;
        }
        if (type.equals("bannerImage")) {
            String bannerImage = Base64.getEncoder().encodeToString(user.getBannerImage());
            return bannerImage;
        }
    }


}


