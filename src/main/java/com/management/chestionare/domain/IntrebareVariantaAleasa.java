package com.management.chestionare.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class IntrebareVariantaAleasa implements Comparable<IntrebareVariantaAleasa> {
    @Getter
    @Setter
    private Long intrebareId;

    @Getter
    @Setter
    private Long variraspunsId;

    @Override
    public String toString() {
        return "IntrebareVariantaAleasa{" +
                "intrebareId=" + intrebareId +
                ", variraspunsId=" + variraspunsId +
                '}';
    }

    @Override
    public int compareTo(IntrebareVariantaAleasa intrebareVariantaAleasa) {
        return intrebareId.compareTo(intrebareVariantaAleasa.intrebareId);
    }
}
