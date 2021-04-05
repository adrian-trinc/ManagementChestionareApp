package com.management.chestionare.dtodomain;

import java.util.List;

public class IntrebareDTO {
    public Long intrebareId;
    public String continut;
    public Integer numarDePuncte;
    public Boolean finalizata;
    public List<VariantaDeRaspunsDTO> varianteDeRaspuns;
}
