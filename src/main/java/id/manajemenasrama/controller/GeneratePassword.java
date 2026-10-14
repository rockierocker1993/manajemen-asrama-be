package id.manajemenasrama.controller;

import id.manajemenasrama.dto.BaseResponse;
import id.manajemenasrama.dto.kegiatan.JadwalKegiatanByDayDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate-password")
@RequiredArgsConstructor
public class GeneratePassword {
    private final PasswordEncoder passwordEncoder;
    @GetMapping(value = {"/{password}"})
    public ResponseEntity<String> asramaByDay(@PathVariable("password") String password) {
        String encodedPassword = passwordEncoder.encode(password);
        return ResponseEntity.ok(encodedPassword);
    }
}
