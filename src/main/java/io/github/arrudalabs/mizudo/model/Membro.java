package io.github.arrudalabs.mizudo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "membros")
public class Membro extends PanacheEntity {

    @NotBlank
    public String nome;

    public DadosGerais dadosGerais;

    public DadosFisicos dadosFisicos;

    @OneToMany(mappedBy = "membro")
    public List<ExameMedico> examesMedicos;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "membros_emails",
            joinColumns = @JoinColumn(name = "membro_id"))
    @Column(name = "email")
    public List<String> emails;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "membros_telefones",
            joinColumns = @JoinColumn(name = "responsavel_id"))
    public List<Telefone> telefones;

    public Graduacao graduacao;

    public static Membro novoMembro(String nome) {
        Membro membro = new Membro();
        membro.nome = nome;
        return membro;
    }

    public static Optional<Membro> procurarPorNome(String value) {
        if (Objects.isNull(value)) return Optional.empty();
        return Membro.find("lower(nome) like lower(concat('%',?1,'%'))", value).firstResultOptional();
    }
}
