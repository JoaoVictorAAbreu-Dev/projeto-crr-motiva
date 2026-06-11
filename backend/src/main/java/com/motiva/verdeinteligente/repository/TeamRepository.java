package com.motiva.verdeinteligente.repository;

import com.motiva.verdeinteligente.model.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findAllByOrderByNameAsc();
}
