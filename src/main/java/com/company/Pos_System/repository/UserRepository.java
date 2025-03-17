package com.company.Pos_System.repository;

import com.company.Pos_System.enums.UserRole;
import com.company.Pos_System.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsernameAndDeletedAtIsNull(String username);

    Optional<Users> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByUsernameAndDeletedAtIsNull(String username);

    Optional<List<Users>> findByUsernameContainingAndDeletedAtIsNull(String username);

    Optional<Users> findByRoleAndDeletedAtIsNull(UserRole role);

}
