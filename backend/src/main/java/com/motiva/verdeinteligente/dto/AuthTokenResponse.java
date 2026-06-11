package com.motiva.verdeinteligente.dto;

import java.time.LocalDateTime;

public record AuthTokenResponse(
    String accessToken,
    String tokenType,
    LocalDateTime expiresAt,
    String username,
    String fullName,
    String role
) {
}
