package com.management.chestionare.Repository;

import com.management.chestionare.domain.ChestionarEfectuat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoChestionarEfectuat extends JpaRepository<ChestionarEfectuat, Long> {
    List<ChestionarEfectuat> findAllByUtilizator_NumeDeUtilizator(String numeDeUtilizator);
    boolean existsChestionarEfectuatByChestionar_ChestionarId(Long chestionarId);
}
