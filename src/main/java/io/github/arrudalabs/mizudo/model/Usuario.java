package io.github.arrudalabs.mizudo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Table(name = "usuarios")
public class Usuario extends PanacheEntityBase {

    public static Stream<Usuario> buscarUsuariosDoMembro(Membro membro) {
        return Usuario.stream("membro.id = :membroId", Map.of("membroId", membro.id));
    }

    /**
     * Método dedicado aos testes
     * Não utilizar esse método em produção
     */
    @Deprecated
    public static void apagarTodosOsUsuarios() {
        Usuario.deleteAll();
    }

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
