package io.github.arrudalabs.mizudo.resources.membros;

import io.github.arrudalabs.mizudo.model.Membro;

public record MembroRegistrado(Long id, String nome) {

    static MembroRegistrado of(Membro membro) {
        return new MembroRegistrado(membro.id, membro.nome);
    }

}
