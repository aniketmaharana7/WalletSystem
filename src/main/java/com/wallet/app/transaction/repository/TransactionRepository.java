package com.wallet.app.transaction.repository;

import com.wallet.app.transaction.entity.Transaction;
import com.wallet.app.transaction.entity.TransactionStatus;
import com.wallet.app.transaction.entity.TransactionType;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository
        extends JpaRepository<Transaction, UUID> {

    Page<Transaction> findBySenderWalletId(
            UUID walletId,
            Pageable pageable
    );

    Page<Transaction> findByReceiverWalletId(
            UUID walletId,
            Pageable pageable
    );

    @Query("""
    SELECT t
    FROM Transaction t
    WHERE t.senderWallet.id = :walletId
       OR t.receiverWallet.id = :walletId
    ORDER BY t.createdAt DESC
    """)
    List<Transaction> findRecentTransactions(
            @Param("walletId") UUID walletId,
            Limit limit
    );

    @Query("""
        SELECT t
        FROM Transaction t
        WHERE (t.senderWallet.id = :walletId
            OR t.receiverWallet.id = :walletId)
          AND t.createdAt >= :start
          AND t.createdAt < :end
        ORDER BY t.createdAt DESC
        """)
    List<Transaction> findTransactionsBetween(
            @Param("walletId") UUID walletId,
            @Param("start") Instant start,
            @Param("end") Instant end
            );

    @Query("""
        SELECT t
        FROM Transaction t
        WHERE (t.senderWallet.id = :walletId
            OR t.receiverWallet.id = :walletId)
          AND t.createdAt >= :start
          AND t.createdAt < :end
          AND t.type =: type
          AND t.status =:status
        ORDER BY t.createdAt DESC
        """)
    List<Transaction> findTransactions(
            @Param("walletId") UUID walletId,
            @Param("start") Instant start,
            @Param("end") Instant end,
            @Param("type")TransactionType type,
            @Param("status")TransactionStatus status
    );
}