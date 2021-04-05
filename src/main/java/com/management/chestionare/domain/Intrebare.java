package com.management.chestionare.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@Table(name = "intrebare")
@NoArgsConstructor
@AllArgsConstructor
public class Intrebare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intrebare_id")
    @Getter
    private Long intrebareId;

    @Column(name = "continut")
    @Getter
    @Setter
    private String continut;

    @Column(name = "numar_de_puncte")
    @Getter
    @Setter
    private Integer numarDePuncte;

    @Column(name = "finalizata")
    @Getter
    @Setter
    private Boolean finalizata;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chestionar_id")
    @Getter
    @Setter
    private Chestionar chestionar;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "intrebare",
            cascade = CascadeType.ALL
    )
    @Getter
    @Setter
    private Set<VariantaDeRaspuns> varianteDeRaspuns; //entitatea Intrebare

    @Override
    public String toString() {
        return "Intrebare{" +
                "intrebareId=" + intrebareId +
                ", continut='" + continut + '\'' +
                ", numarDePuncte=" + numarDePuncte +
                ", chestionar=" + chestionar +
                ", varianteDeRaspuns=" + varianteDeRaspuns +
                '}';
    }
}
