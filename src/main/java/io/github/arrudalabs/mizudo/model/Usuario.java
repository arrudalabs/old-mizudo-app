package io.github.arrudalabs.mizudo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario extends PanacheEntityBase {

    @Id
    public String username;

    @Lob
    @Basic(optional = true)
    public byte[] salt;

    @Lob
    @Basic(optional = true)
    public byte[] hash;

    @ManyToOne
    public Membro membro;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "usuarios_papeis",
            joinColumns = @JoinColumn(name = "username"))
    @Column(name = "papel")
    @Enumerated(EnumType.STRING)
    public Set<Papeis> papeis;

}
