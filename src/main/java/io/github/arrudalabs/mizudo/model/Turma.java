package io.github.arrudalabs.mizudo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "turmas")
public class Turma extends PanacheEntity {
    public String descricao;
}
