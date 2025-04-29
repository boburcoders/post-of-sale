package com.company.Pos_System.repository;

import com.company.Pos_System.enums.UserRole;
import com.company.Pos_System.models.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private Users user;

    @BeforeEach
    void setUp() {
        user = Users.builder()
                .fullName("Full Name")
                .password("<PASSWORD>")
                .username("activeUser")
                .role(UserRole.ADMIN)
                .build();

        userRepository.save(user);
    }

    @Test
    void findByUsernameAndDeletedAtIsNull() {
        Optional<Users> result = userRepository.findByUsernameAndDeletedAtIsNull(user.getUsername());
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void findByIdAndDeletedAtIsNull() {
        Optional<Users> result = userRepository.findByIdAndDeletedAtIsNull(user.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(user.getId());
    }

    @Test
    void existsByUsernameAndDeletedAtIsNull() {
        boolean result = userRepository.existsByUsernameAndDeletedAtIsNull(user.getUsername());
        assertThat(result).isTrue();
    }

    @Test
    void findByUsernameContainingAndDeletedAtIsNull() {
        Optional<List<Users>> resultList = userRepository.findByUsernameContainingAndDeletedAtIsNull(user.getUsername());
        assertThat(resultList).isPresent();
        assertThat(resultList.get().get(0).getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void findByRoleAndDeletedAtIsNull() {
        Optional<Users> result = userRepository.findByRoleAndDeletedAtIsNull(UserRole.ADMIN);
        assertThat(result).isPresent();
        assertThat(result.get().getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    void findAllByDeletedAtIsNull() {
        Optional<List<Users>> resultList = userRepository.findAllByDeletedAtIsNull();
        assertThat(resultList).isPresent();
        assertThat(resultList.get().get(0).getUsername()).isEqualTo(user.getUsername());
    }
}