package io.github.arrudalabs.mizudo.model;

import io.github.arrudalabs.mizudo.resources.membros.ListagemDeMembrosResource;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Table(name = "membros")
public class Membro extends PanacheEntity {

    @NotBlank
    public String nome;

    @Valid
    public DadosGerais dadosGerais;

    @Valid
    public DadosFisicos dadosFisicos;

    @Valid
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "membros_emails",
            joinColumns = @JoinColumn(name = "membro_id"))
    public List<@NotBlank @Email String> emails;

    @Valid
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "membros_telefones",
            joinColumns = @JoinColumn(name = "responsavel_id"))
    public List<Telefone> telefones;

    @Valid
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "exames_medicos",
            joinColumns = @JoinColumn(name = "membro_id"))
    public Set<ExameMedico> examesMedicos = new LinkedHashSet<>();

    public Graduacao graduacao;

    public static Membro novoMembro(String nome) {
        Membro membro = new Membro();
        membro.nome = nome;
        membro.persist();
        return membro;
    }

    public static Membro buscarPorId(Long id) {
        return Membro.findById(id);
    }

    public static void removerTodosMembros() {
        List<Membro> membros = Membro.listAll();
        membros.stream().forEach(Membro::apagar);
    }

    public static Stream<Membro> listarMembros() {
        return Membro.streamAll();
    }

    private void apagar() {
        this.emails.clear();
        this.telefones.clear();
        this.persist();
        this.delete();
    }

}
