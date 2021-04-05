package com.management.chestionare.Repository;

import com.management.chestionare.domain.Chestionar;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoChestionar extends JpaRepository<Chestionar, Long> {
    List<Chestionar> findAllByUtilizatorCreator_NumeDeUtilizator(String numeDeUtilizator);
    List<Chestionar> findAllByUtilizatorCreator_NumeDeUtilizator(String numeDeUtilizator, Sort sort);
}
