package com.management.chestionare.Repository;

import com.management.chestionare.domain.Intrebare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoIntrebare extends JpaRepository<Intrebare, Long> {
    List<Intrebare> findAllByChestionar_ChestionarId(Long chestionarId);
    List<Intrebare> findAllByChestionar_ChestionarIdOrderByIntrebareId(Long chestionarId);
    List<Intrebare> deleteAllByChestionar_ChestionarId(Long chestionarId);
    List<Intrebare> findAllByChestionar_UtilizatorCreator_NumeDeUtilizator(String numeDeUtilizator);
}
