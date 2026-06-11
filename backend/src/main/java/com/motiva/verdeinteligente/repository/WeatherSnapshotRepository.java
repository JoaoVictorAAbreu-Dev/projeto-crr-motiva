package com.motiva.verdeinteligente.repository;

import com.motiva.verdeinteligente.model.WeatherSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherSnapshotRepository extends JpaRepository<WeatherSnapshot, Long> {
}
