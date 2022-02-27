package io.github.arrudalabs.mizudo.model;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class DadosFisicos {

    @Enumerated(EnumType.ORDINAL)
    public Sexo sexo = Sexo.INDEFINIDO;

    public Double peso;

    public Double altura;

}
