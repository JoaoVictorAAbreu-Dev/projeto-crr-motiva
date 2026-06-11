package com.motiva.verdeinteligente.dto;

public record AuthenticatedUserResponse(
    String username,
    String fullName,
    String role
) {
}
