package com.management.chestionare.mapper;

import com.management.chestionare.domain.VariantaDeRaspuns;
import com.management.chestionare.dtodomain.VariantaDeRaspunsDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MapperVariantaDeRaspuns {
    public VariantaDeRaspunsDTO variantaDeRaspunsDTOToVariantaDeRaspunsDTO(VariantaDeRaspuns variantaDeRaspuns) {
        if (variantaDeRaspuns == null) {
            return null;
        }
        VariantaDeRaspunsDTO variantaDeRaspunsDTO = new VariantaDeRaspunsDTO();
        variantaDeRaspunsDTO.variraspunsId = variantaDeRaspuns.getVariraspunsId();
        variantaDeRaspunsDTO.continut = variantaDeRaspuns.getContinut();
        variantaDeRaspunsDTO.variantaCorecta = variantaDeRaspuns.getVariantaCorecta();
        return variantaDeRaspunsDTO;
    }

    public List<VariantaDeRaspunsDTO> varianteDeRaspunsToVarianteDeRaspunsDTO(Set<VariantaDeRaspuns> varianteDeRaspuns) {
        return varianteDeRaspuns
                .stream()
                .map(this::variantaDeRaspunsDTOToVariantaDeRaspunsDTO)
                .collect(Collectors.toList());
    }
}
