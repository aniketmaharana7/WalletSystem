package com.wallet.app.transaction.service;

import com.wallet.app.common.exception.WalletNotFoundException;
import com.wallet.app.common.utility.SecurityContextUtil;
import com.wallet.app.transaction.dto.TransactionListResponse;
import com.wallet.app.transaction.dto.TransactionResponse;
import com.wallet.app.transaction.dto.TransactionResponseMapper;
import com.wallet.app.transaction.entity.Transaction;
import com.wallet.app.transaction.entity.TransactionStatus;
import com.wallet.app.transaction.entity.TransactionType;
import com.wallet.app.transaction.repository.TransactionRepository;
import com.wallet.app.wallet.entity.Wallet;
import com.wallet.app.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    public TransactionListResponse getRecentTransactions() {
        Wallet w = getWallet();

        List<Transaction> list = transactionRepository.findRecentTransactions(w.getId(),
                Limit.of(3));

        return TransactionResponseMapper.mapTransaction(list);
    }

    private @NonNull Wallet getWallet() {
        UUID userId = SecurityContextUtil.getCurrentUserId();

        Wallet w = walletRepository.findByUserId(userId).orElseThrow(
                () -> new WalletNotFoundException("Wallet not found")
        );
        return w;
    }

    public TransactionListResponse getCurrentMonthTransactions() {
        Wallet w = getWallet();

        LocalDate today = LocalDate.now();

        Instant start = today
                .withDayOfMonth(1)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();

        Instant end = today
                .withDayOfMonth(1)
                .plusMonths(1)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();

        List<Transaction> transactions =
                transactionRepository.findTransactionsBetween(
                        w.getId(),
                        start,
                        end
                );

        return TransactionResponseMapper.mapTransaction(transactions);
    }

    public List<TransactionResponse> getTransactions(
            LocalDate from,
            LocalDate to,
            TransactionType type,
            TransactionStatus status
    ) {

        Wallet wallet = getWallet();

        Instant start = from != null
                ? from.atStartOfDay(ZoneOffset.UTC).toInstant()
                : null;

        Instant end = to != null
                ? to.plusDays(1)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                : null;

        List<Transaction> transactions =
                transactionRepository.findTransactions(
                        wallet.getId(),
                        start,
                        end,
                        type,
                        status
                );

        return transactions.stream()
                .map(TransactionResponseMapper::map)
                .toList();
    }
}
