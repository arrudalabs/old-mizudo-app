package io.github.arrudalabs.mizudo.model;


import io.github.arrudalabs.mizudo.validation.ValidationGroups;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Embeddable
public class Registro {

    @NotBlank(groups = ValidationGroups.OnPut.class)
    public String entidade;

    @JsonbDateFormat(value = "yyyy-MM-dd")
    @NotNull(groups = ValidationGroups.OnPut.class)
    public LocalDate data;

    @NotEmpty(groups = ValidationGroups.OnPut.class)
    public String numero;
}
