package io.github.arrudalabs.mizudo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Table(name = "membros")
public class Membro extends PanacheEntity {

    public static Membro novoMembro(String nome) {
        Membro membro = new Membro();
        membro.nome = nome;
        membro.persist();
        return membro;
    }

    public static Optional<Membro> buscarPorId(Long id) {
        return Membro.findByIdOptional(id);
    }

    /**
     * Método dedicado para testes
     * Não utilizar em produção
     */
    @Deprecated
    public static void removerTodosMembros() {
        List<Membro> membros = Membro.listAll();
        membros.stream().forEach(Membro::apagar);
    }

    public static Stream<Membro> listarMembros() {
        return Membro.streamAll();
    }


    @NotBlank
    public String nome;

    @Valid
    public DadosGerais dadosGerais;

    @Valid
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "registros",
            joinColumns = @JoinColumn(name = "membro_id"))
    public Set<@NotNull Registro> registros;

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

    private void apagar() {
        this.emails.clear();
        this.telefones.clear();
        this.examesMedicos.clear();
        Usuario.buscarUsuariosDoMembro(this)
                .forEach(Usuario::delete);
        this.persist();
        this.delete();
    }

    public void atualizarTelefones(List<Telefone> telefones) {
        this.telefones.clear();
        this.telefones.addAll(telefones);
    }

    public void atualizarEmails(List<String> emails) {
        this.emails.clear();
        this.emails.addAll(emails);
    }

    public void atualizarExamesMedicos(List<ExameMedico> exameMedicos) {
        this.examesMedicos.clear();
        this.examesMedicos.addAll(exameMedicos);
    }
}
