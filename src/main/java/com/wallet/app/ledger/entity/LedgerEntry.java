package com.wallet.app.ledger.entity;

import com.wallet.app.transaction.entity.Transaction;
import com.wallet.app.wallet.entity.Wallet;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "ledger_entries",
        indexes = {
                @Index(
                        name = "idx_ledger_entries_wallet_id",
                        columnList = "wallet_id"
                ),
                @Index(
                        name = "idx_ledger_entries_transaction_id",
                        columnList = "transaction_id"
                ),
                @Index(
                        name = "idx_ledger_entries_created_at",
                        columnList = "created_at"
                )
        }
)
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /*
     * The transaction that caused this ledger entry.
     *
     * Many ledger entries can belong to one transaction.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "transaction_id",
            nullable = false
    )
    private Transaction transaction;

    /*
     * The wallet whose balance is affected by this entry.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "wallet_id",
            nullable = false
    )
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private LedgerEntryType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected LedgerEntry() {
        // Required by JPA
    }

    public LedgerEntry(
            Transaction transaction,
            Wallet wallet,
            LedgerEntryType type,
            BigDecimal amount,
            String currency
    ) {
        this.transaction = transaction;
        this.wallet = wallet;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public LedgerEntryType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}