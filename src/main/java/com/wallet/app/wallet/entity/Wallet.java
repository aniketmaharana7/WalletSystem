package com.wallet.app.wallet.entity;

import com.wallet.app.user.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WalletStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /*
     * Inverse side of the relationship.
     *
     * User owns the relationship because users.wallet_id
     * contains the foreign key.
     *
     * mappedBy = "wallet" refers to the field in User.
     */
    @OneToOne(mappedBy = "wallet", fetch = FetchType.LAZY)
    private User user;

    @Version
    @Column(nullable = false)
    private Long version;

    public Wallet() {
        this.balance = BigDecimal.ZERO;
        this.currency = "INR";
        this.status = WalletStatus.ACTIVE;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    public WalletStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        this.updatedAt = Instant.now();
    }

    public void debit(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
        this.updatedAt = Instant.now();
    }

    public void block() {
        this.status = WalletStatus.BLOCKED;
        this.updatedAt = Instant.now();
    }

    public void activate() {
        this.status = WalletStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public boolean isblocked() {
       return getStatus().equals(WalletStatus.BLOCKED);
    }
}