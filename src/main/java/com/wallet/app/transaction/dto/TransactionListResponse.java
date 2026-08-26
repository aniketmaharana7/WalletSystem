package com.wallet.app.transaction.dto;

import java.util.List;

public record TransactionListResponse(
        List<TransactionResponse> transactions
) {
}