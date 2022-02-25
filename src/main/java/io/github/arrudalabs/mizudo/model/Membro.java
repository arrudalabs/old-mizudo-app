package io.github.arrudalabs.mizudo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

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

}
