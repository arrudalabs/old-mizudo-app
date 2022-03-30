package io.github.arrudalabs.mizudo.model;

import java.util.Arrays;
import java.util.Set;

public interface Papeis {
    public static final String ALUNO = "ALUNO";
    public static final String INSTRUTOR = "INSTRUTOR";
    public static final String EXAMINADOR = "EXAMINADOR";
    public static final String COORDENADOR = "COORDENADOR";
    public static final String ADMINISTRADOR = "ADMINISTRADOR";

    public static enum Papel {
        ALUNO,
        INSTRUTOR,
        EXAMINADOR,
        COORDENADOR,
        ADMINISTRADOR;
    }

    static Set<String> todosOsPapeis() {
        return Arrays.stream(Papel.values()).map(Papel::name).collect(java.util.stream.Collectors.toSet());
    }
}
