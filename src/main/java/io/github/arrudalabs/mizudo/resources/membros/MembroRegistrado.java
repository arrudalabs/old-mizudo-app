package io.github.arrudalabs.mizudo.resources.membros;

import io.github.arrudalabs.mizudo.model.Membro;

public class MembroRegistrado {

    public Long id;
    public String nome;

    static MembroRegistrado of(Membro membro) {
        var response = new MembroRegistrado();
        response.id = membro.id;
        response.nome = membro.nome;
        return response;
    }

}
