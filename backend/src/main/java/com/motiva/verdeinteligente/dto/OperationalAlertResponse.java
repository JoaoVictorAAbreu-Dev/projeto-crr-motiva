package com.motiva.verdeinteligente.dto;

public record OperationalAlertResponse(
    String title,
    String severity,
    String summary
) {
}
