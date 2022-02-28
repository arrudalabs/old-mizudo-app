package io.github.arrudalabs.mizudo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "membros")
public class Membro extends PanacheEntity {

    @NotBlank
    public String nome;

    public DadosGerais dadosGerais;

    public DadosFisicos dadosFisicos;

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

    public ExameMedico exameMedicoMaisAtual() {
        return this.examesMedicos.stream().sorted().findFirst().orElse(null);
    }



    public Graduacao graduacao;

    public static Membro novoMembro(String nome) {
        Membro membro = new Membro();
        membro.nome = nome;
        membro.persist();
        return membro;
    }

    public static Membro buscarPorId(Long id){
        return Membro.findById(id);
    }

    public static void removerTodosMembros(){
        List<Membro> membros = Membro.listAll();
        membros.stream().forEach(Membro::apagar);
    }

    private void apagar() {
        this.emails.clear();
        this.telefones.clear();
        this.persist();
        this.delete();
    }
}
