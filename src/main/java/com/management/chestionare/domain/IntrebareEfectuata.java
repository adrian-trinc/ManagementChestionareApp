package com.management.chestionare.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@Table(name = "intrebare_efectuata")
@NoArgsConstructor
@AllArgsConstructor
public class IntrebareEfectuata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intrebare_efectuata_id")
    @Getter
    private Long intrebareEfectuataId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intrebare_id")
    @Getter
    @Setter
    private Intrebare intrebare;

    @Column(name = "aleasa_corect")
    @Getter
    @Setter
    private Boolean aleasaCorect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chestionar_efectuat_id")
    @Getter
    @Setter
    private ChestionarEfectuat chestionarEfectuat;

    @Override
    public String toString() {
        return "IntrebareEfectuata{" +
                "intrebareEfectuataId=" + intrebareEfectuataId +
                ", intrebare=" + intrebare +
                ", aleasaCorect=" + aleasaCorect +
                ", chestionarEfectuat=" + chestionarEfectuat +
                '}';
    }
}
