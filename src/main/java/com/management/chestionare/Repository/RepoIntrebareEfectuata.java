package com.management.chestionare.Repository;

import com.management.chestionare.domain.IntrebareEfectuata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoIntrebareEfectuata extends JpaRepository<IntrebareEfectuata, Long> {
    List<IntrebareEfectuata> findAllByChestionarEfectuat_ChestionarEfectuatId(Long chestionarEfectuatId);
    boolean existsIntrebareEfectuataByIntrebare_IntrebareId(Long intrebareId);
}
