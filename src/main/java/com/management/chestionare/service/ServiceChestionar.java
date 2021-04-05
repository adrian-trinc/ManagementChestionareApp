package com.management.chestionare.service;

import com.management.chestionare.Repository.RepoChestionar;
import com.management.chestionare.Repository.RepoIntrebare;
import com.management.chestionare.domain.Chestionar;
import com.management.chestionare.domain.Intrebare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceChestionar {
    private final RepoChestionar repoChestionar;
    private final RepoIntrebare repoIntrebare;

    @Autowired
    public ServiceChestionar(RepoChestionar repoChestionar, RepoIntrebare repoIntrebare) {
        this.repoChestionar = repoChestionar;
        this.repoIntrebare = repoIntrebare;
    }

    @Transactional(readOnly = true)
    public List<Chestionar> findAll() {
        return repoChestionar.findAll();
    }

    @Transactional(readOnly = true)
    public List<Chestionar> findAllByUtilizatorCreator_NumeDeUtilizator(String numeDeUtilizator) {
        return repoChestionar.findAllByUtilizatorCreator_NumeDeUtilizator(numeDeUtilizator);
    }

    @Transactional(readOnly = true)
    public List<Chestionar> findAllAscByIdByUtilizatorCreator_NumeDeUtilizator(String numeDeUtilizator) {
        Sort sortAscById = Sort.by("chestionarId").ascending();
        return repoChestionar.findAllByUtilizatorCreator_NumeDeUtilizator(numeDeUtilizator, sortAscById);
    }

    @Transactional(readOnly = true)
    public Optional<Chestionar> findById(Long id) {
        return repoChestionar.findById(id);
    }

    @Transactional
    public Chestionar save(Chestionar chestionar) {
        return repoChestionar.saveAndFlush(chestionar);
    }

    @Transactional
    public void update(Chestionar chestionar, String descriereNoua) {
        chestionar.setDescriere(descriereNoua);
    }

    @Transactional
    public List<Intrebare> delete(Chestionar chestionar) {
        List<Intrebare> intrebari = repoIntrebare.deleteAllByChestionar_ChestionarId(chestionar.getChestionarId());
        repoChestionar.delete(chestionar);
        return intrebari;
    }

    @Transactional
    public void verificaFinalizare(Chestionar chestionar) {
        List<Intrebare> intrebariChestionar = repoIntrebare
                .findAllByChestionar_ChestionarId(chestionar.getChestionarId());
        Optional<Boolean> chestionarFinalizatInOptional = intrebariChestionar
                .stream()
                .map(Intrebare::getFinalizata)
                .reduce(Boolean::logicalAnd);
        if (chestionarFinalizatInOptional.isPresent()) {
            boolean chestionarFinalizat = chestionarFinalizatInOptional.get();
            chestionar.setFinalizat(chestionarFinalizat);
        } else {
            chestionar.setFinalizat(false);
        }
    }

    @Transactional
    public void setFinalizare(Chestionar chestionar, Boolean finalizare) {
        chestionar.setFinalizat(finalizare);
    }
}
