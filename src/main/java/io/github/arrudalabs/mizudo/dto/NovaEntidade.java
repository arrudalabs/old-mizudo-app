package io.github.arrudalabs.mizudo.dto;

import io.github.arrudalabs.mizudo.model.Entidade;
import io.github.arrudalabs.mizudo.validation.SuportaValidacao;
import io.github.arrudalabs.mizudo.validation.Validacao;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@SuportaValidacao(
        message = "Entidade já registrada",
        classeValidadora = NovaEntidade.Validador.class
)
public record NovaEntidade(@NotNull @NotBlank String descricao) {

    public Entidade criarEntidade() {
        return Entidade.novaEntidade(descricao);
    }

    public static class Validador implements Validacao {
        @Override
        public boolean estahValido(Object object) {
            if (object instanceof NovaEntidade novaEntidade) {
                return Entidade.buscarPorDescricao(novaEntidade.descricao).isEmpty();
            }
            return false;
        }
    }
}