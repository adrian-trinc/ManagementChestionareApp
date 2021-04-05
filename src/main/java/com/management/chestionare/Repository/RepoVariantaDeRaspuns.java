package com.management.chestionare.Repository;

import com.management.chestionare.domain.VariantaDeRaspuns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoVariantaDeRaspuns extends JpaRepository<VariantaDeRaspuns, Long> {
}
