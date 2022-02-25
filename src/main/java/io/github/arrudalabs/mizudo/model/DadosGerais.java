package io.github.arrudalabs.mizudo.model;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class DadosGerais{

    public LocalDate dataNascimento;

    public Endereco endereco;

}
