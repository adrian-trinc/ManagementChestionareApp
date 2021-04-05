package com.management.chestionare.service;

import com.management.chestionare.Repository.RepoIntrebare;
import com.management.chestionare.domain.Chestionar;
import com.management.chestionare.domain.Intrebare;
import com.management.chestionare.domain.VariantaDeRaspuns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ServiceIntrebare {
    private final RepoIntrebare repoIntrebare;

    @Autowired
    public ServiceIntrebare(RepoIntrebare repoIntrebare) {
        this.repoIntrebare = repoIntrebare;
    }

    @Transactional(readOnly = true)
    public List<Intrebare> findAll() {
        return repoIntrebare.findAll();
    }

    @Transactional(readOnly = true)
    public List<Intrebare> findAllByChestionar_ChestionarId(Long chestionarId) {
        return repoIntrebare.findAllByChestionar_ChestionarId(chestionarId);
    }

    @Transactional(readOnly = true)
    public List<Intrebare> findAllByChestionar_UtilizatorCreator_NumeDeUtilizator(String numeDeUtilizator) {
        return repoIntrebare.findAllByChestionar_UtilizatorCreator_NumeDeUtilizator(numeDeUtilizator);
    }

    @Transactional(readOnly = true)
    public Optional<Intrebare> findById(Long id) {
        return repoIntrebare.findById(id);
    }

    @Transactional
    public List<Intrebare> saveAll(List<Intrebare> intrebari) {
        List<Intrebare> intrebariReturnate = new ArrayList<>();
        for (Intrebare intrebare : intrebari) {
            Intrebare intrebareReturnata = repoIntrebare.saveAndFlush(intrebare);
            intrebariReturnate.add(intrebareReturnata);
        }
        return intrebariReturnate;
    }

    @Transactional
    public Intrebare save(Chestionar chestionar, Intrebare intrebare) {
        chestionar.setNumarDeIntrebari(chestionar.getNumarDeIntrebari() + 1);
        return repoIntrebare.saveAndFlush(intrebare);
    }

    @Transactional
    public void delete(Chestionar chestionar, Intrebare intrebare) {
        chestionar.setNumarDeIntrebari(chestionar.getNumarDeIntrebari() - 1);
        repoIntrebare.delete(intrebare);
    }

    @Transactional
    public void verificaFinalizare(Intrebare intrebare) {
        Set<VariantaDeRaspuns> varianteDeRaspunsIntrebare = intrebare.getVarianteDeRaspuns();
        Optional<Boolean> intrebareFinalizataInOptional = varianteDeRaspunsIntrebare
                .stream()
                .map(VariantaDeRaspuns::getVariantaCorecta)
                .reduce(Boolean::logicalOr);
        if (intrebareFinalizataInOptional.isPresent()) {
            boolean intrebareFinalizata = intrebareFinalizataInOptional.get();
            intrebare.setFinalizata(intrebareFinalizata);
        } else {
            intrebare.setFinalizata(false);
        }
    }
}
