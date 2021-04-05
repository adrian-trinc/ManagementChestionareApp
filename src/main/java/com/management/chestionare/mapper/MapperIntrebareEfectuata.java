package com.management.chestionare.mapper;

import com.management.chestionare.domain.Intrebare;
import com.management.chestionare.domain.IntrebareEfectuata;
import com.management.chestionare.dtodomain.IntrebareEfectuataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperIntrebareEfectuata {
    private final MapperVariantaDeRaspuns mapperVariantaDeRaspuns;

    @Autowired
    public MapperIntrebareEfectuata(MapperVariantaDeRaspuns mapperVariantaDeRaspuns) {
        this.mapperVariantaDeRaspuns = mapperVariantaDeRaspuns;
    }

    public IntrebareEfectuataDTO intrebareEfectuataToIntrebareEfectuataDTO(IntrebareEfectuata intrebareEfectuata) {
        if (intrebareEfectuata == null) {
            return null;
        }
        IntrebareEfectuataDTO intrebareEfectuataDTO = new IntrebareEfectuataDTO();
        intrebareEfectuataDTO.intrebareEfectuataId = intrebareEfectuata.getIntrebareEfectuataId();
        Intrebare intrebare = intrebareEfectuata.getIntrebare();
        intrebareEfectuataDTO.intrebareId = intrebare.getIntrebareId();
        intrebareEfectuataDTO.continut = intrebare.getContinut();
        intrebareEfectuataDTO.numarDePuncte = intrebare.getNumarDePuncte();
        intrebareEfectuataDTO.aleasaCorect = intrebareEfectuata.getAleasaCorect();
        intrebareEfectuataDTO.varianteDeRaspuns = mapperVariantaDeRaspuns.varianteDeRaspunsToVarianteDeRaspunsDTO(intrebare.getVarianteDeRaspuns());
        return intrebareEfectuataDTO;
    }

    public List<IntrebareEfectuataDTO> intrebariEfectuateToIntrebariEfectuateDTO(List<IntrebareEfectuata> intrebariEfectuate) {
        return intrebariEfectuate
                .stream()
                .map(this::intrebareEfectuataToIntrebareEfectuataDTO)
                .collect(Collectors.toList());
    }
}
