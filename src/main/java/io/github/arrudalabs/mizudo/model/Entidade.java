package io.github.arrudalabs.mizudo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;
import java.util.stream.Stream;

@Entity
@Table(name = "entidades")
public class Entidade extends PanacheEntity {
    @NotEmpty
    public String descricao;

    public static Optional<Entidade> buscarPorId(Long id) {
        return Entidade.findByIdOptional(id);
    }

    public static Stream<Entidade> listarEntidades() {
        return Entidade.streamAll();
    }

    public static Entidade novaEntidade(String descricao) {
        var entidade = new Entidade();
        entidade.descricao = descricao;
        return entidade;
    }

    public void apagarEntidade() {
        this.delete();
    }

    public static void apagarEntidades() {
        Entidade.deleteAll();
    }

}
