package io.github.arrudalabs.mizudo.resources.membros;

import io.github.arrudalabs.mizudo.model.Membro;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;
import java.util.stream.Collectors;

@Path("membros")
public class ListagemDeMembrosResource {

    @GET
    public List<MembroRegistrado> listagemDeMembros(){

        return Membro.listarMembros()
                .map(MembroRegistrado::of)
                .collect(Collectors.toList());
    }

}
