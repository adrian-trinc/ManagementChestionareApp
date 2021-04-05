package com.management.chestionare.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@Table(name = "chestionar")
@NoArgsConstructor
@AllArgsConstructor
public class Chestionar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chestionar_id")
    @Getter
    private Long chestionarId;

    @Column(name = "descriere")
    @Getter
    @Setter
    private String descriere;

    @Column(name = "numar_de_intrebari")
    @Getter
    @Setter
    private Integer numarDeIntrebari;

    @Column(name = "finalizat")
    @Getter
    @Setter
    private Boolean finalizat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilizator_creator_id")
    @Getter
    @Setter
    private Utilizator utilizatorCreator;

    @Override
    public String toString() {
        return "Chestionar{" +
                "chestionarId=" + chestionarId +
                ", descriere='" + descriere + '\'' +
                ", numarDeIntrebari=" + numarDeIntrebari +
                ", utilizatorCreator=" + utilizatorCreator +
                '}';
    }
}
