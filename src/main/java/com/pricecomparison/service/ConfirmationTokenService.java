package com.pricecomparison.service;

import com.pricecomparison.exception.ResourceNotFoundException;
import com.pricecomparison.model.ConfirmationToken;
import com.pricecomparison.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public ConfirmationToken getConfirmationToken(String token) {
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Confirmation token with token [%s] not found".formatted(token)
                ));
    }
}
