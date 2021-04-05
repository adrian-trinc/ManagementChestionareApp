package com.management.chestionare.service;

import com.management.chestionare.Repository.RepoChestionar;
import com.management.chestionare.Repository.RepoChestionarEfectuat;
import com.management.chestionare.Repository.RepoIntrebare;
import com.management.chestionare.Repository.RepoIntrebareEfectuata;
import com.management.chestionare.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.*;

@Service
public class ServiceChestionarEfectSiIntrebareEfect {
    private final RepoChestionarEfectuat repoChestionarEfectuat;
    private final RepoChestionar repoChestionar;
    private final RepoIntrebare repoIntrebare;
    private final RepoIntrebareEfectuata repoIntrebareEfectuata;

    @Autowired
    public ServiceChestionarEfectSiIntrebareEfect(RepoChestionarEfectuat repoChestionarEfectuat, RepoChestionar repoChestionar, RepoIntrebare repoIntrebare, RepoIntrebareEfectuata repoIntrebareEfectuata) {
        this.repoChestionarEfectuat = repoChestionarEfectuat;
        this.repoChestionar = repoChestionar;
        this.repoIntrebare = repoIntrebare;
        this.repoIntrebareEfectuata = repoIntrebareEfectuata;
    }

    @Transactional(readOnly = true)
    public Optional<ChestionarEfectuat> findById(Long id) {
        return repoChestionarEfectuat.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ChestionarEfectuat> findAllByUtilizator_NumeDeUtilizator(String numeDeUtilizator) {
        return repoChestionarEfectuat.findAllByUtilizator_NumeDeUtilizator(numeDeUtilizator);
    }

    @Transactional(readOnly = true)
    public boolean existsChestionarEfectuatByChestionar_ChestionarId(Long chestionarId) {
        return repoChestionarEfectuat.existsChestionarEfectuatByChestionar_ChestionarId(chestionarId);
    }

    @Transactional(readOnly = true)
    public List<IntrebareEfectuata> findAllByChestionarEfectuat_ChestionarEfectuatId(Long chestionarEfectuatId) {
        return repoIntrebareEfectuata.findAllByChestionarEfectuat_ChestionarEfectuatId(chestionarEfectuatId);
    }

    @Transactional(readOnly = true)
    public boolean existsIntrebareEfectuataByIntrebare_IntrebareId(Long intrebareId) {
        return repoIntrebareEfectuata.existsIntrebareEfectuataByIntrebare_IntrebareId(intrebareId);
    }

    @Transactional
    public ChestionarEfectuat save(Long chestionarId, String sablon, MultiValueMap<String, String> paramMap, Utilizator utilizator) {
        Optional<Chestionar> chestionarRezolvatOptional = repoChestionar.findById(chestionarId);
        if (chestionarRezolvatOptional.isPresent()) {
            Chestionar chestionarRezolvat = chestionarRezolvatOptional.get();
            paramMap.remove("_csrf");
            List<IntrebareVariantaAleasa> intrebareVariantaAleasaList = new ArrayList<>();
            for (String intrebareIdSirDeCaractere : paramMap.keySet()) {
                String variraspunsIdSirDeCaractere = paramMap.getFirst(intrebareIdSirDeCaractere);
                if (Objects.nonNull(variraspunsIdSirDeCaractere)) {
                    Long intrebareId = Long.parseLong(intrebareIdSirDeCaractere.substring(sablon.length()));
                    Long variraspunsId = Long.parseLong(variraspunsIdSirDeCaractere);
                    IntrebareVariantaAleasa intrebareVariantaAleasa = new IntrebareVariantaAleasa(intrebareId, variraspunsId);
                    intrebareVariantaAleasaList.add(intrebareVariantaAleasa);
                }
            }
            Collections.sort(intrebareVariantaAleasaList);
            List<Intrebare> intrebariChestionar = repoIntrebare.findAllByChestionar_ChestionarIdOrderByIntrebareId(chestionarId);
            ChestionarEfectuat chestionarEfectuat = new ChestionarEfectuat(
                    0L,
                    chestionarRezolvat,
                    utilizator,
                    0);
            List<IntrebareEfectuata> intrebariEfectuate = new ArrayList<>();
            for (int i = 0; i < intrebareVariantaAleasaList.size(); i++) {
                IntrebareVariantaAleasa intrebareVariantaAleasa = intrebareVariantaAleasaList.get(i);
                Intrebare intrebare = intrebariChestionar.get(i);
                if (intrebareVariantaAleasa.getIntrebareId().equals(intrebare.getIntrebareId())) {
                    IntrebareEfectuata intrebareEfectuata = new IntrebareEfectuata(
                            0L,
                            intrebare,
                            false,
                            chestionarEfectuat);
                    Optional<VariantaDeRaspuns> variantaDeRaspunsCorectaOptional = intrebare
                            .getVarianteDeRaspuns()
                            .stream()
                            .filter(VariantaDeRaspuns::getVariantaCorecta)
                            .findFirst();
                    if (variantaDeRaspunsCorectaOptional.isPresent()) {
                        VariantaDeRaspuns variantaDeRaspunsCorecta = variantaDeRaspunsCorectaOptional.get();
                        if (intrebareVariantaAleasa.getVariraspunsId().equals(variantaDeRaspunsCorecta.getVariraspunsId())) {
                            intrebareEfectuata.setAleasaCorect(true);
                            chestionarEfectuat.setPunctajObtinut(chestionarEfectuat.getPunctajObtinut() + intrebare.getNumarDePuncte());
                        }
                    }
                    intrebariEfectuate.add(intrebareEfectuata);
                }
            }
            ChestionarEfectuat chestionarEfectuatReturnat = repoChestionarEfectuat.saveAndFlush(chestionarEfectuat);
            for (IntrebareEfectuata intrebareEfectuata : intrebariEfectuate) {
                intrebareEfectuata.setChestionarEfectuat(chestionarEfectuatReturnat);
                repoIntrebareEfectuata.saveAndFlush(intrebareEfectuata);
            }
            return chestionarEfectuatReturnat;
        } else {
            throw new ServiceException("Chestionarul cu ID-ul " + chestionarId.toString() + " nu exista.");
        }
    }
}
