package com.management.chestionare.domain;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class CloneUtils {
    public static VariantaDeRaspuns clone(VariantaDeRaspuns altaVariantaDeRaspuns, Intrebare intrebare) {
        return new VariantaDeRaspuns(
                0L,
                altaVariantaDeRaspuns.getContinut(),
                altaVariantaDeRaspuns.getVariantaCorecta(),
                intrebare
        );
    }

    public static Intrebare clone(Intrebare altaIntrebare, Chestionar chestionarClonat) {
        Intrebare intrebare = new Intrebare(
                0L,
                altaIntrebare.getContinut(),
                altaIntrebare.getNumarDePuncte(),
                altaIntrebare.getFinalizata(),
                chestionarClonat,
                new HashSet<>()
        );
        altaIntrebare
                .getVarianteDeRaspuns()
                .forEach(variantaDeRaspuns -> intrebare
                        .getVarianteDeRaspuns()
                        .add(CloneUtils.clone(variantaDeRaspuns, intrebare))
                );
        return intrebare;
    }

    public static Intrebare cloneCuVariantaDeRaspunsStearsa(Intrebare altaIntrebare, Chestionar chestionarClonat, Long variraspunsStearsaId) {
        Intrebare intrebare = new Intrebare(
                0L,
                altaIntrebare.getContinut(),
                altaIntrebare.getNumarDePuncte(),
                altaIntrebare.getFinalizata(),
                chestionarClonat,
                new HashSet<>()
        );
        altaIntrebare
                .getVarianteDeRaspuns()
                .stream()
                .filter(variantaDeRaspuns -> !variantaDeRaspuns.getVariraspunsId().equals(variraspunsStearsaId))
                .forEach(variantaDeRaspuns -> intrebare
                        .getVarianteDeRaspuns()
                        .add(CloneUtils.clone(variantaDeRaspuns, intrebare))
                );
        return intrebare;
    }

    public static List<Intrebare> clone(List<Intrebare> intrebari, Chestionar chestionarClonat) {
        return intrebari
                .stream()
                .map(intrebareIter -> CloneUtils.clone(intrebareIter, chestionarClonat))
                .collect(Collectors.toList());
    }

    public static List<Intrebare> cloneCuIntrebareStearsa(List<Intrebare> intrebari, Chestionar chestionarClonat, Long intrebareStearsaId) {
        return intrebari
                .stream()
                .filter(intrebareIter -> !intrebareIter.getIntrebareId().equals(intrebareStearsaId))
                .map(intrebareIter -> CloneUtils.clone(intrebareIter, chestionarClonat))
                .collect(Collectors.toList());
    }

    public static List<Intrebare> cloneCuVariantaDeRaspunsStearsa(List<Intrebare> intrebari, Chestionar chestionarClonat, Long intrebareCuVariantaStearsaId, Long variraspunsStearsaId) {
        return intrebari
                .stream()
                .map(intrebareIter -> {
                    if (intrebareIter.getIntrebareId().equals(intrebareCuVariantaStearsaId)) {
                        return CloneUtils.cloneCuVariantaDeRaspunsStearsa(
                                intrebareIter,
                                chestionarClonat,
                                variraspunsStearsaId);
                    } else {
                        return CloneUtils.clone(intrebareIter, chestionarClonat);
                    }
                })
                .collect(Collectors.toList());
    }

    public static Chestionar clone(Chestionar altChestionar, Boolean chestionarFinalizat) {
        return new Chestionar(
                0L,
                altChestionar.getDescriere(),
                altChestionar.getNumarDeIntrebari(),
                chestionarFinalizat,
                altChestionar.getUtilizatorCreator()
        );
    }
}
