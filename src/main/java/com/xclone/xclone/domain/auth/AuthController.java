package com.xclone.xclone.domain.auth;

import com.xclone.xclone.domain.user.User;
import com.xclone.xclone.domain.user.UserDTO;
import com.xclone.xclone.domain.user.UserService;
import com.xclone.xclone.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, UserService userService, JwtService jwtService) {
        this.authService = authService;
        this.userService = userService;
        this.jwtService = jwtService;
    }


    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody Map<String, String> body) {
        String accessToken = body.get("token");

        User authenticatedUser = authService.authenticateGoogleUser(accessToken);
        UserDTO dtoToReturn = userService.generateUserDTOByUserId(authenticatedUser.getId());

        String token = jwtService.createToken(dtoToReturn.id);
        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", dtoToReturn
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
        }

        String token = authHeader.replace("Bearer ", "");
        if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        int userId = jwtService.extractUserId(token);
        UserDTO dto = userService.generateUserDTOByUserId(userId);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/tempSignup")
    public ResponseEntity<?> authenticateWithTempSignup() {
        User newTempuser = authService.registerTemporaryUser();
        UserDTO dtoToReturn = userService.generateUserDTOByUserId(newTempuser.getId());
        String token = jwtService.createToken(newTempuser.getId());
        return ResponseEntity.ok(Map.of("token", token, "user", dtoToReturn));
    }

}