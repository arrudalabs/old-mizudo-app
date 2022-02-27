package io.github.arrudalabs.mizudo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.runtime.annotations.IgnoreProperty;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Embeddable
public class Graduacao {

    @ManyToOne
    @JoinColumn(name = "nivelGraduacao", updatable = false)
    public NivelGraduacao nivelGraduacao;

    public LocalDate dataGraduacao;

}
