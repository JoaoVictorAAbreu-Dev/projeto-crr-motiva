package com.motiva.verdeinteligente.dto;

import java.util.List;

public record DashboardAssumptionsResponse(
    String dataPolicy,
    String decisionPolicy,
    String safetyPolicy,
    String privacyPolicy,
    List<String> costReferences,
    List<String> operationalReferences
) {
}
