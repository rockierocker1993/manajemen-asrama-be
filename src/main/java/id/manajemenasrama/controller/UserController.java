package id.manajemenasrama.controller;

import id.manajemenasrama.dto.BaseResponse;
import id.manajemenasrama.dto.user.ProfileResponseDto;
import id.manajemenasrama.dto.user.UpdateUserRequestDto;
import id.manajemenasrama.dto.user.UserResponseDto;
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

    @GetMapping(value = {"", "/"})
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<BaseResponse<List<UserResponseDto>>> getAll() {
        return userService.getAll();
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
