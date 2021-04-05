package com.management.chestionare.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@Table(name = "utilizator",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "nume_de_utilizator")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class Utilizator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "utilizator_id")
    @Getter
    private Long utilizatorId;

    @Column(name = "nume_prenume")
    @Getter
    @Setter
    private String numePrenume;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    @Getter
    @Setter
    private Rol rol;

    @Column(name = "nume_de_utilizator")
    @Getter
    @Setter
    private String numeDeUtilizator;

    @Column(name = "parola")
    @Getter
    @Setter
    private String parola;

    @Override
    public String toString() {
        return "Utilizator{" +
                "utilizatorId=" + utilizatorId +
                ", numePrenume='" + numePrenume + '\'' +
                ", rol=" + rol +
                ", numeDeUtilizator='" + numeDeUtilizator + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }
}
