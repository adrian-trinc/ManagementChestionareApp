package com.management.chestionare.mapper;

import com.management.chestionare.domain.Chestionar;
import com.management.chestionare.dtodomain.ChestionarDTO;
import org.springframework.stereotype.Component;

@Component
public class MapperChestionar {
    public ChestionarDTO chestionarToChestionarDTO(Chestionar chestionar) {
        if (chestionar == null) {
            return null;
        }
        ChestionarDTO chestionarDTO = new ChestionarDTO();
        chestionarDTO.chestionarId = chestionar.getChestionarId();
        chestionarDTO.descriere = chestionar.getDescriere();
        chestionarDTO.numarDeIntrebari = chestionar.getNumarDeIntrebari();
        chestionarDTO.numePrenumeUtilizatorCreator = chestionar.getUtilizatorCreator().getNumePrenume();
        chestionarDTO.rolUtilizatorCreator = chestionar.getUtilizatorCreator().getRol();
        chestionarDTO.numeDeUtilizatorUtilizatorCreator = chestionar.getUtilizatorCreator().getNumeDeUtilizator();
        return chestionarDTO;
    }
}
