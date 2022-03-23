package io.github.arrudalabs.mizudo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "responsaveis")
public class Responsavel extends PanacheEntity {

    public String nome;

    public Endereco endereco;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "responsavel_emails",
            joinColumns = @JoinColumn(name = "responsavel_id"))
    @Column(name = "email")
    public Set<String> emails;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "responsavel_telefones",
            joinColumns = @JoinColumn(name = "responsavel_id"))
    public Set<Telefone> telefones;

    @ManyToMany
    public Set<Membro> membros;

}
