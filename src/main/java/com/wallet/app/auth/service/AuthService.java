package com.wallet.app.auth.service;

import com.wallet.app.auth.dto.LoginRequest;
import com.wallet.app.auth.dto.LoginResponse;
import com.wallet.app.auth.dto.RegisterRequest;
import com.wallet.app.config.JwtService;
import com.wallet.app.user.entity.User;
import com.wallet.app.user.entity.UserStatus;
import com.wallet.app.user.repository.UserRepository;
import com.wallet.app.wallet.entity.Wallet;
import com.wallet.app.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;;
    private final JwtService jwtService;;

    public AuthService(
            UserRepository userRepository,
            WalletRepository walletRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new IllegalArgumentException("Email already registered");
        }
        Wallet wallet = new Wallet();

        User user = new User(registerRequest.name(),
                registerRequest.email(),
                passwordEncoder.encode(registerRequest.password()));
        user.assignWallet(wallet);

        walletRepository.save(wallet);
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmailWithWallet(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found. Please register"));

        if (user.getStatus().equals(UserStatus.BLOCKED)) throw new IllegalArgumentException("User is blocked");
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid user or password");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail()
        );

        return new LoginResponse(user.getId(), user.getWallet().getId(), user.getName(), user.getEmail(), token);
    }
}
