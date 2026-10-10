package id.manajemenasrama.controller;

import id.manajemenasrama.dto.auth.LoginRequest;
import id.manajemenasrama.dto.auth.RegisterRequest;
import id.manajemenasrama.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return authService.registerUser(request);
    }

}

