package com.wallet.app.ledger.dto;

import java.util.List;

public record LedgerEntryListResponse(
        List<LedgerEntryResponse> entries
) {
}