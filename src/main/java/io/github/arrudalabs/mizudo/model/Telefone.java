package io.github.arrudalabs.mizudo.model;

import io.github.arrudalabs.mizudo.validation.ValidationGroups;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Embeddable
public class Telefone {

    public static Telefone novoTelefone(String numero, String contato) {
        var telefone = new Telefone();
        telefone.numero = numero;
        telefone.contato = contato;
        return telefone;
    }

    @NotBlank(groups = ValidationGroups.OnPut.class)
    public String numero;

    public String contato;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Telefone telefone = (Telefone) o;
        return Objects.equals(numero, telefone.numero) && Objects.equals(contato, telefone.contato);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, contato);
    }

    @Override
    public String toString() {
        return "Telefone{" +
                "numero='" + numero + '\'' +
                ", contato='" + contato + '\'' +
                '}';
    }
}
