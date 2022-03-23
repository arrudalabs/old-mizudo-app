package io.github.arrudalabs.mizudo.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;


@Embeddable
public class MembroTurmaPK implements Serializable {
    @ManyToOne
    public Membro membro;
    @ManyToOne
    public Turma turma;
}
