package com.management.chestionare.mapper;

import com.management.chestionare.domain.ChestionarEfectuat;
import com.management.chestionare.dtodomain.ChestionarEfectuatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperChestionarEfectuat {
    private final MapperChestionar mapperChestionar;

    @Autowired
    public MapperChestionarEfectuat(MapperChestionar mapperChestionar) {
        this.mapperChestionar = mapperChestionar;
    }

    public ChestionarEfectuatDTO chestionarEfectuatToChestionarEfectuatDTO(ChestionarEfectuat chestionarEfectuat) {
        if (chestionarEfectuat == null) {
            return null;
        }
        ChestionarEfectuatDTO chestionarEfectuatDTO = new ChestionarEfectuatDTO();
        chestionarEfectuatDTO.chestionarEfectuatId = chestionarEfectuat.getChestionarEfectuatId();
        chestionarEfectuatDTO.chestionar = mapperChestionar.chestionarToChestionarDTO(chestionarEfectuat.getChestionar());
        chestionarEfectuatDTO.punctajObtinut = chestionarEfectuat.getPunctajObtinut();
        return chestionarEfectuatDTO;
    }

    public List<ChestionarEfectuatDTO> chestionareEfectuateToChestionareEfectuateDTO(List<ChestionarEfectuat> chestionareEfectuate) {
        return chestionareEfectuate
                .stream()
                .map(this::chestionarEfectuatToChestionarEfectuatDTO)
                .collect(Collectors.toList());
    }
}
