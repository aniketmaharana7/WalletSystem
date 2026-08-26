package com.wallet.app.wallet.repository;

import com.wallet.app.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    Optional<Wallet> findById(UUID walletId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT w from Wallet w JOIN User u on w.id=u.wallet.id WHERE u.id=:userId""")
    Optional<Wallet> findByUserIdForUpdate(@Param("userId") UUID userId);

    @Query("""
        SELECT w from Wallet w JOIN User u on w.id=u.wallet.id WHERE u.id=:userId""")
    Optional<Wallet> findByUserId(@Param("userId") UUID userId);

}