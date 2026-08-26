package com.wallet.app.user.repository;

import com.wallet.app.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
        SELECT u
        FROM User u
        JOIN FETCH u.wallet
        WHERE u.id = :userId
    """)
    Optional<User> findByIdWithWallet(@Param("userId") UUID userId);

    @Query("""
        SELECT u
        FROM User u
        JOIN FETCH u.wallet
        WHERE u.email = :email
    """)
    Optional<User> findByEmailWithWallet(@Param("email") String email);
}