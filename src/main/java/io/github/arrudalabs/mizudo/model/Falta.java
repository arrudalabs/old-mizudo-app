package io.github.arrudalabs.mizudo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "membros_falta")
public class Falta extends PanacheEntity {
    @ManyToOne
    public Membro membro;
    @ManyToOne
    public Turma turma;
    @NotNull
    public LocalDate data;
}
