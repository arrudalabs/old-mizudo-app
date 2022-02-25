package io.github.arrudalabs.mizudo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "exames_medicos")
public class ExameMedico extends PanacheEntity {

    @ManyToOne
    public Membro membro;

    public LocalDate data;

    @Lob
    public String obs;
}
