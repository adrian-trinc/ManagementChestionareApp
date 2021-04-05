package com.management.chestionare.mapper;

import com.management.chestionare.domain.Intrebare;
import com.management.chestionare.dtodomain.IntrebareDTO;
import com.management.chestionare.dtodomain.IntrebareDTOV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperIntrebare {
    private final MapperVariantaDeRaspuns mapperVariantaDeRaspuns;

    @Autowired
    public MapperIntrebare(MapperVariantaDeRaspuns mapperVariantaDeRaspuns) {
        this.mapperVariantaDeRaspuns = mapperVariantaDeRaspuns;
    }

    public IntrebareDTO intrebareToIntrebareDTO(Intrebare intrebare) {
        if (intrebare == null) {
            return null;
        }
        IntrebareDTO intrebareDTO = new IntrebareDTO();
        intrebareDTO.intrebareId = intrebare.getIntrebareId();
        intrebareDTO.continut = intrebare.getContinut();
        intrebareDTO.numarDePuncte = intrebare.getNumarDePuncte();
        intrebareDTO.finalizata = intrebare.getFinalizata();
        intrebareDTO.varianteDeRaspuns = mapperVariantaDeRaspuns.varianteDeRaspunsToVarianteDeRaspunsDTO(intrebare.getVarianteDeRaspuns());
        return intrebareDTO;
    }

    public List<IntrebareDTO> intrebariToIntrebariDTO(List<Intrebare> intrebari) {
        return intrebari
                .stream()
                .map(this::intrebareToIntrebareDTO)
                .collect(Collectors.toList());
    }

    public IntrebareDTOV2 intrebareToIntrebareDTOV2(Intrebare intrebare) {
        if (intrebare == null) {
            return null;
        }
        IntrebareDTOV2 intrebareDTOV2 = new IntrebareDTOV2();
        intrebareDTOV2.intrebareId = intrebare.getIntrebareId();
        intrebareDTOV2.continut = intrebare.getContinut();
        intrebareDTOV2.numarDePuncte = intrebare.getNumarDePuncte();
        intrebareDTOV2.chestionarId = intrebare.getChestionar().getChestionarId();
        return intrebareDTOV2;
    }

    public List<IntrebareDTOV2> intrebariToIntrebariDTOV2(List<Intrebare> intrebari) {
        return intrebari
                .stream()
                .map(this::intrebareToIntrebareDTOV2)
                .collect(Collectors.toList());
    }
}
