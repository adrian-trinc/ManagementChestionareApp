package com.management.chestionare.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@Table(name = "chestionar_efectuat")
@NoArgsConstructor
@AllArgsConstructor
public class ChestionarEfectuat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chestionar_efectuat_id")
    @Getter
    private Long chestionarEfectuatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chestionar_id")
    @Getter
    @Setter
    private Chestionar chestionar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilizator_id")
    @Getter
    @Setter
    private Utilizator utilizator;

    @Column(name = "punctaj_obtinut")
    @Getter
    @Setter
    private Integer punctajObtinut;

    @Override
    public String toString() {
        return "ChestionarEfectuat{" +
                "chestionarEfectuatId=" + chestionarEfectuatId +
                ", chestionar=" + chestionar +
                ", utilizator=" + utilizator +
                ", punctajObtinut=" + punctajObtinut +
                '}';
    }
}
