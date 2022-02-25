package io.github.arrudalabs.mizudo.model;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Embeddable
public class Graduacao {

    @ManyToOne
    @JoinColumn(name = "nivelGraduacao")
    public NivelGraduacao nivelGraduacao;

    public LocalDate dataGraduacao;

}
