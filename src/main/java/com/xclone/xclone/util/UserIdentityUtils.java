package com.xclone.xclone.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public class UserIdentityUtils {

    public static String parseGoogleDisplayName (String firstName, String lastName, int suffix) {
        if (firstName != null && !firstName.isBlank()) {
            if (lastName != null && !lastName.isBlank()) {
                return (firstName + " " + lastName);
            } else {
                return firstName;
            }
        } else {
            return ("tempAccount" + suffix);
        }
    }

    public static String parseGoogleUserName (String firstName, String lastName, int suffix) {
        String baseName = (firstName != null && !firstName.isBlank()) ? firstName.toLowerCase() : "user";
        return baseName + suffix;
    }

    public static Map parseGoogleUserInfo (String accessToken) {

        RestTemplate restTemplate = new RestTemplate();
        String googleUserInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(googleUserInfoUrl, HttpMethod.GET, request, Map.class);
        return response.getBody();
    }

    public static byte[] parseGoogleImageToByte (String pictureUrl) {
        try (InputStream in = new URL(pictureUrl).openStream()) {
            byte[] profileBytes = in.readAllBytes();
            return profileBytes;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Google profile picture", e);
        }
    }

    public static byte[] parseDefaultImage (String bannerPath) {
        try (InputStream in = UserIdentityUtils.class.getClassLoader().getResourceAsStream(bannerPath)) {
            if (in == null) throw new RuntimeException("default banner not found");
            return in.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load default banner image", e);
        }
    }

}
