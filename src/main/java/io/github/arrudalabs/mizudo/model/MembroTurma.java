package io.github.arrudalabs.mizudo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "membros_turmas")
public class MembroTurma extends PanacheEntityBase {

    @EmbeddedId
    public MembroTurmaPK id;

}
