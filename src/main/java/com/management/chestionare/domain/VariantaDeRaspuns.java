package com.management.chestionare.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class VariantaDeRaspuns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variraspuns_id")
    @Getter
    private Long variraspunsId;

    @Column(name = "continut")
    @Getter
    @Setter
    private String continut;

    @Column(name = "varianta_corecta")
    @Getter
    @Setter
    private Boolean variantaCorecta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intrebare_id")
    @Getter
    @Setter
    private Intrebare intrebare; //entitatea VariantaDeRaspuns

    @Override
    public String toString() {
        return "VariantaDeRaspuns{" +
                "variraspunsId=" + variraspunsId +
                ", continut='" + continut + '\'' +
                ", variantaCorecta=" + variantaCorecta +
                '}';
    }
}
