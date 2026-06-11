package com.motiva.verdeinteligente.repository;

import com.motiva.verdeinteligente.model.VegetationClass;
import com.motiva.verdeinteligente.model.VegetationRuleProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VegetationRuleProfileRepository extends JpaRepository<VegetationRuleProfile, Long> {

    Optional<VegetationRuleProfile> findByVegetationClass(VegetationClass vegetationClass);
}
