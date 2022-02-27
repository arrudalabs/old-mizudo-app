package io.github.arrudalabs.mizudo.model;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class ExameMedico implements Comparable<ExameMedico> {
    public LocalDate data;
    public String obs;

    @Override
    public int compareTo(ExameMedico o) {
        if (this.equals(o))
            return 0;
        return Objects.compare(this.data, o.data, LocalDate::compareTo);
    }

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
