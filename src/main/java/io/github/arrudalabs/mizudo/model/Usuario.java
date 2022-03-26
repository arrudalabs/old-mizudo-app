package io.github.arrudalabs.mizudo.model;

import io.github.arrudalabs.mizudo.services.GeradorDeHash;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Stream;

@Entity
@Table(name = "usuarios")
public class Usuario extends PanacheEntityBase {

    public static Usuario definirUsuario(Membro membro,
                                         String username,
                                         String senha,
                                         GeradorDeHash geradorDeHash) {

        Optional<Usuario> usuarioRef = Usuario.buscarPorUsername(username);

        if (usuarioRef.isPresent()) {
            throw new IllegalArgumentException("Já existe usuário com o username informado");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.membro = membro;
        novoUsuario.username = username;
        String salt = geradorDeHash.novoSalt();
        novoUsuario.salt = salt.getBytes(StandardCharsets.UTF_8);
        novoUsuario.hash = geradorDeHash.gerarHash(salt, senha).getBytes(StandardCharsets.UTF_8);
        novoUsuario.persist();
        novoUsuario.definirPapeis(Set.of(Papeis.ALUNO));
        return novoUsuario;
    }

    public static Optional<Usuario> autentica(
            String username,
            String senha,
            GeradorDeHash geradorDeHash) {
        var usuarioLocalizado = Usuario.buscarPorUsername(username);
        if (!usuarioLocalizado.isPresent()) {
            var hash = geradorDeHash
                    .gerarHash(new String(usuarioLocalizado.get().salt), senha);
            if (!hash.equals(new String(usuarioLocalizado.get().hash))) {
                return Optional.empty();
            }
        }
        return usuarioLocalizado;
    }

    public void definirPapeis(Set<Papeis> papeis) {
        this.papeis.clear();
        this.papeis.addAll(papeis);
    }

    public static Optional<Usuario> buscarPorUsername(String username) {
        return Usuario.findByIdOptional(username);
    }

    public static Stream<Usuario> buscarUsuariosDoMembro(Membro membro) {
        return Usuario.stream("membro.id = :membroId", Map.of("membroId", membro.id));
    }

    /**
     * Método dedicado aos testes
     * Não utilizar esse método em produção
     */
    @Deprecated
    public static void apagarTodosOsUsuarios() {
        Usuario.streamAll().forEach(u -> u.delete());
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
    public Set<Papeis> papeis = new LinkedHashSet<>();


}
