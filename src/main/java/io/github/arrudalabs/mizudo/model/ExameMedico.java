package io.github.arrudalabs.mizudo.model;

import io.github.arrudalabs.mizudo.validation.ValidationGroups;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class ExameMedico {

    public static ExameMedico novoExameMedico(LocalDate data, String obs) {
        var exameMedico = new ExameMedico();
        exameMedico.data = data;
        exameMedico.obs = obs;
        return exameMedico;
    }

    @JsonbDateFormat(value = "yyyy-MM-dd")
    @NotNull(groups = ValidationGroups.OnPut.class)
    public LocalDate data;

    public String obs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExameMedico that = (ExameMedico) o;
        return data.equals(that.data) && obs.equals(that.obs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, obs);
    }
}
