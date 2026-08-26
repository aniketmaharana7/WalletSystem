package com.wallet.app.auth.dto;

import java.util.UUID;

public record LoginResponse(
        UUID userId,
        UUID walletId,
        String name,
        String email,
        String token
) {
}