package id.manajemenasrama.controller;

import id.manajemenasrama.dto.BaseResponse;
import id.manajemenasrama.dto.auth.TokenResponse;
import id.manajemenasrama.dto.user.*;
import id.manajemenasrama.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<BaseResponse<ProfileResponseDto>> me() {
        return userService.me();
    }

    @PutMapping(value = {"/me"})
    public ResponseEntity<BaseResponse<TokenResponse>> meUpdate(@Valid @RequestBody UpdateProfileRequestDto request) {
        return userService.meUpdate(request);
    }

    @GetMapping(value = {"", "/"})
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<BaseResponse<List<UserResponseDto>>> getAll() {
        return userService.getAll();
    }

    @GetMapping(value = {"/ustadzah"})
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','PENGURUS')")
    public ResponseEntity<BaseResponse<List<UstdazahResponseDto>>> getAllUstadzah() {
        return userService.getAllUstadzah();
    }

    @PutMapping(value = {"", "/"})
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<BaseResponse<UserResponseDto>> update(@Valid @RequestBody UpdateUserRequestDto request) {
        return userService.update(request);
    }

    @DeleteMapping(value = {"/{username}"})
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable("username") String username) {
        return userService.delete(username);
    }
}
