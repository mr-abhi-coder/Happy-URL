package url.shortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import url.shortener.dtos.LoginRequest;
import url.shortener.dtos.RegisterRequest;
import url.shortener.models.User;
import url.shortener.service.UserService;

@RestController
@RequestMapping("api/auth/")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.authenticationUser(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setUsername(registerRequest.getUsername());

        String rolesAsString = String.join(",", registerRequest.getRoles());
        user.setRoles(rolesAsString);

        userService.registerUser(user);
        return ResponseEntity.ok("User registered Successfully");
    }
}
