package com.xclone.xclone.domain.auth;

import com.xclone.xclone.domain.user.User;
import com.xclone.xclone.domain.user.UserDTO;
import com.xclone.xclone.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody Map<String, String> body) {
        String accessToken = body.get("token");
        User authenticatedUser = authService.authenticateGoogleUser(accessToken);
        UserDTO dtoToReturn = userService.generateUserDTOByUserId(authenticatedUser.getId());
        return ResponseEntity.ok(dtoToReturn);
    }

    @PostMapping("/tempSignup")
    public ResponseEntity<?> authenticateWithTempSignup() {
        User newTempuser = authService.registerTemporaryUser();
        UserDTO dtoToReturn = userService.generateUserDTOByUserId(newTempuser.getId());
        return ResponseEntity.ok(dtoToReturn);
    }

}