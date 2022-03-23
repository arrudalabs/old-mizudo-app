package io.github.arrudalabs.mizudo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "exames_graduacao")
public class ExameGraduacao extends PanacheEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "nivelGraduacao")
    public NivelGraduacao nivelGraduacao;

    @NotNull
    public LocalDate dataExame;

    @ManyToOne
    public Membro examinado;

    @ManyToOne
    public Membro examinador;

    @ManyToOne
    public Membro conferente;

    @NotNull
    public LocalDate dataConferencia;

    public Double notaKihon;

    public Double notaKata;

    public Double notaKumite;

}
