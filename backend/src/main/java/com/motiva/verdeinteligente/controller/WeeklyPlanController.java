package com.motiva.verdeinteligente.controller;

import com.motiva.verdeinteligente.dto.WeeklyPlanRequest;
import com.motiva.verdeinteligente.dto.WeeklyPlanResponse;
import com.motiva.verdeinteligente.service.WeeklyPlanService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weekly-plans")
public class WeeklyPlanController {

    private final WeeklyPlanService weeklyPlanService;

    public WeeklyPlanController(WeeklyPlanService weeklyPlanService) {
        this.weeklyPlanService = weeklyPlanService;
    }

    @PostMapping("/generate")
    public WeeklyPlanResponse generate(@Valid @RequestBody WeeklyPlanRequest request) {
        return weeklyPlanService.generate(request);
    }

    @GetMapping("/{id}")
    public WeeklyPlanResponse findById(@PathVariable Long id) {
        return weeklyPlanService.findById(id);
    }
}
