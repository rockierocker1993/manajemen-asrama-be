package id.manajemenasrama.service;

import id.manajemenasrama.entity.Role;
import id.manajemenasrama.entity.User;
import id.manajemenasrama.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username.toLowerCase().trim())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<GrantedAuthority> authorities = buildAuthorities(List.of(user.getRole()));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    private List<GrantedAuthority> buildAuthorities(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return Collections.emptyList();
        }

        return roles.stream()
                .map(p -> new SimpleGrantedAuthority(p.getName()))
                .collect(java.util.stream.Collectors.toList());
    }
}

