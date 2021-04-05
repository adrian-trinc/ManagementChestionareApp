package com.management.chestionare.dtodomain;

import java.util.List;

public class IntrebareEfectuataDTO {
    public Long intrebareEfectuataId;
    public Long intrebareId;
    public String continut;
    public Integer numarDePuncte;
    public Boolean aleasaCorect;
    public List<VariantaDeRaspunsDTO> varianteDeRaspuns;
}
