package com.management.chestionare.service;

import com.management.chestionare.Repository.RepoVariantaDeRaspuns;
import com.management.chestionare.domain.Intrebare;
import com.management.chestionare.domain.VariantaDeRaspuns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class ServiceVariantaDeRaspuns {
    private final RepoVariantaDeRaspuns repoVariantaDeRaspuns;

    @Autowired
    public ServiceVariantaDeRaspuns(RepoVariantaDeRaspuns repoVariantaDeRaspuns) {
        this.repoVariantaDeRaspuns = repoVariantaDeRaspuns;
    }

    @Transactional(readOnly = true)
    public Optional<VariantaDeRaspuns> findById(Long id) {
        return repoVariantaDeRaspuns.findById(id);
    }

    @Transactional
    public VariantaDeRaspuns save(Intrebare intrebare, VariantaDeRaspuns variantaDeRaspuns) {
        if (variantaDeRaspuns.getVariantaCorecta()) {
            Optional<Boolean> areVariantaCorectaInOptional = intrebare.getVarianteDeRaspuns()
                    .stream()
                    .map(VariantaDeRaspuns::getVariantaCorecta)
                    .reduce(Boolean::logicalOr);
            if (areVariantaCorectaInOptional.isPresent()) {
                boolean areVariantaCorecta = areVariantaCorectaInOptional.get();
                if (!areVariantaCorecta) {
                    VariantaDeRaspuns variantaDeRaspunsReturnata = repoVariantaDeRaspuns.saveAndFlush(variantaDeRaspuns);
                    intrebare.getVarianteDeRaspuns().add(variantaDeRaspunsReturnata);
                    return variantaDeRaspunsReturnata;
                } else {
                    throw new ServiceException("Exista deja o varianta corecta.");
                }
            } else {
                VariantaDeRaspuns variantaDeRaspunsReturnata = repoVariantaDeRaspuns.saveAndFlush(variantaDeRaspuns);
                intrebare.getVarianteDeRaspuns().add(variantaDeRaspunsReturnata);
                return variantaDeRaspunsReturnata;
            }
        } else {
            VariantaDeRaspuns variantaDeRaspunsReturnata = repoVariantaDeRaspuns.saveAndFlush(variantaDeRaspuns);
            intrebare.getVarianteDeRaspuns().add(variantaDeRaspunsReturnata);
            return variantaDeRaspunsReturnata;
        }
    }

    @Transactional
    public void delete(Intrebare intrebare, VariantaDeRaspuns variantaDeRaspuns) {
        Set<VariantaDeRaspuns> varianteDeRaspuns = intrebare.getVarianteDeRaspuns();
        varianteDeRaspuns.removeIf(variantaDeRaspunsIter -> variantaDeRaspunsIter
                .getVariraspunsId()
                .equals(variantaDeRaspuns.getVariraspunsId())
        );
        repoVariantaDeRaspuns.delete(variantaDeRaspuns);
    }
}
