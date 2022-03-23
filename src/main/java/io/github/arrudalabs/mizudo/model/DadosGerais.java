package io.github.arrudalabs.mizudo.model;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class DadosGerais {

    @JsonbDateFormat(value = "yyyy-MM-dd")
    public LocalDate dataNascimento;

    public Endereco endereco;

}
