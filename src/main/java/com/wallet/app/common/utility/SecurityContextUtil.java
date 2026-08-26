package com.wallet.app.common.utility;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class SecurityContextUtil {

    private SecurityContextUtil() {
    }

    public static UUID getCurrentUserId() {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof UUID userId)) {
            throw new IllegalStateException(
                    "Authenticated user not found"
            );
        }

        return userId;
    }
}