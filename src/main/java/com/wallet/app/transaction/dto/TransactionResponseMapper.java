package com.wallet.app.transaction.dto;

import com.wallet.app.transaction.entity.Transaction;

import java.util.List;
public class TransactionResponseMapper {

    public static TransactionListResponse mapTransaction(
            List<Transaction> list
    ) {

        List<TransactionResponse> transactions = list.stream()
                .map(TransactionResponseMapper::map)
                .toList();

        return new TransactionListResponse(transactions);
    }

    public static TransactionResponse map(Transaction transaction) {

        return new TransactionResponse(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getStatus(),
                transaction.getSenderWallet() != null
                        ? transaction.getSenderWallet().getId()
                        : null,
                transaction.getReceiverWallet() != null
                        ? transaction.getReceiverWallet().getId()
                        : null,
                transaction.getCreatedAt(),
                transaction.getCompletedAt()
        );
    }
}
