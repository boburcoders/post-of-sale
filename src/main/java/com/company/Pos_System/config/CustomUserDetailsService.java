package com.company.Pos_System.config;

import com.company.Pos_System.models.Users;
import com.company.Pos_System.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> optionalUsers = this.userRepository.findByUsernameAndDeletedAtIsNull(username);
        if (optionalUsers.isPresent()) {
            return new UserPrincipal(optionalUsers.get());
        }
        throw new UsernameNotFoundException("User not found with username:");
    }
}
