package com.xclone.xclone.domain.auth;

import com.xclone.xclone.domain.feed.EdgeRank;
import com.xclone.xclone.domain.user.User;
import com.xclone.xclone.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final EdgeRank edgeRank;
    UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository, EdgeRank edgeRank) {
        this.userRepository = userRepository;
        this.edgeRank = edgeRank;
    }

    public User registerTemporaryUser () {

        //TODO make sure this never collides, maybe keep trying until a unique one??
        User user = new User();


    }

    public User authenticateGoogleUser (String accessToken) {

        Map userInfo = parseGoogleUserInfo(accessToken);

        if (userInfo == null || !userInfo.containsKey("sub")) {
            throw new IllegalStateException("Could not find user");
        }

        Optional<User> user = userRepository.findByGoogleId((String) userInfo.get("sub"));

        if (user.isPresent()) {
            User toReturn = user.get();
            edgeRank.generateFeed(toReturn.getId());
            return toReturn;
        } else {
            return createNewGoogleUser(userInfo);
        }
    }

    private User createNewGoogleUser (Map userInfo) {

        String googleId = (String) userInfo.get("sub");
        String email = (String) userInfo.get("email");
        String pictureUrl = (String) userInfo.get("picture");
        String firstName = (String) userInfo.get("given_name");
        String lastName =  (String) userInfo.get("family_name");

        User newUser = new User();
        newUser.setGoogleId(googleId);

        //TODO make sure this never collides, maybe keep trying until a unique one??
        int suffix = (int)(Math.random() * 90000) + 10000;
        newUser.setUsername(parseGoogleUserName(firstName, lastName, suffix));
        newUser.setDisplayName(parseGoogleDisplayName(firstName, lastName, suffix));

        newUser.setEmail(email);
        newUser.setCreatedAt(Timestamp.from(Instant.now()));

        newUser.setProfilePicture(parseGoogleImageToByte(pictureUrl));
        newUser.setBannerImage(parseDefaultBannerImage("static/assets/defaultBanner.jpg"));

        userRepository.save(newUser);
        edgeRank.generateFeed(newUser.getId());

        return newUser;

    }

    private String parseGoogleDisplayName (String firstName, String lastName, int suffix) {
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

    private String parseGoogleUserName (String firstName, String lastName, int suffix) {
        String baseName = (firstName != null && !firstName.isBlank()) ? firstName.toLowerCase() : "user";
        return baseName + suffix;
    }

    private Map parseGoogleUserInfo (String accessToken) {

        RestTemplate restTemplate = new RestTemplate();
        String googleUserInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(googleUserInfoUrl, HttpMethod.GET, request, Map.class);
        return response.getBody();

    }

    public byte[] parseGoogleImageToByte (String pictureUrl) {
        try (InputStream in = new URL(pictureUrl).openStream()) {
            byte[] profileBytes = in.readAllBytes();
            return profileBytes;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Google profile picture", e);
        }
    }

    public byte[] parseDefaultBannerImage (String bannerPath) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(bannerPath)) {
            if (in == null) throw new RuntimeException("default banner not found");
            return in.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load default banner image", e);
        }
    }




}
