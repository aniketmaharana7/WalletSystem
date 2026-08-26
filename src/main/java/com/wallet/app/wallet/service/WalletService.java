package com.wallet.app.wallet.service;

import com.wallet.app.ledger.entity.LedgerEntry;
import com.wallet.app.ledger.entity.LedgerEntryType;
import com.wallet.app.ledger.repository.LedgerEntryRepository;
import com.wallet.app.transaction.entity.Transaction;
import com.wallet.app.transaction.entity.TransactionType;
import com.wallet.app.transaction.repository.TransactionRepository;
import com.wallet.app.common.utility.SecurityContextUtil;
import com.wallet.app.wallet.dto.DepositRequest;
import com.wallet.app.wallet.dto.DepositResponse;
import com.wallet.app.wallet.dto.WalletResponse;
import com.wallet.app.wallet.entity.Wallet;
import com.wallet.app.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final LedgerEntryRepository ledgerEntryRepository;

    public WalletService(WalletRepository walletRepository,
                         TransactionRepository transactionRepository, LedgerEntryRepository ledgerEntryRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
    }

    public WalletResponse getWalletDetails(UUID id) {
        Wallet wallet = walletRepository.findByUserId(id).orElseThrow(() ->
                new IllegalArgumentException("Wallet not found! Please contact the administrator!"));

        return new WalletResponse(wallet.getId(),
                wallet.getBalance(), wallet.getCurrency(), wallet.getStatus().name());
    }

    @Transactional
    public DepositResponse toWalletDeposit(DepositRequest depositRequest) {
        UUID id = SecurityContextUtil.getCurrentUserId();

        // locked the object
        Wallet w = walletRepository.findByUserIdForUpdate(id).orElseThrow(()->
                new IllegalArgumentException("Wallet not found! Please contact the administrator!"));

        if (w.isblocked()) {
            throw new IllegalArgumentException("Wallet is blocked!");
        }

        Transaction transaction = new Transaction(
                TransactionType.ADD_MONEY,
                null,
                w,
                depositRequest.amount(),
                w.getCurrency()
        );

        transactionRepository.save(transaction);

        w.credit(depositRequest.amount());

        LedgerEntry ledgerEntry = new LedgerEntry(
                transaction,
                w,
                LedgerEntryType.CREDIT,
                depositRequest.amount(),
                w.getCurrency()
        );

        ledgerEntryRepository.save(ledgerEntry);

        transaction.markCompleted();

        return new DepositResponse(
                transaction.getId(),
                depositRequest.amount(),
                w.getBalance(),
                w.getCurrency(),
                transaction.getType().name(),
                transaction.getStatus().name()
        );
    }
}
