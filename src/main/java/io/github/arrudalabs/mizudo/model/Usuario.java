package io.github.arrudalabs.mizudo.model;

import io.github.arrudalabs.mizudo.services.GeradorDeHash;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

@Entity
@Table(name = "usuarios")
public class Usuario extends PanacheEntityBase {

    public static Usuario definirUsuarioParaMembroInformado(Membro membro,
                                                            String username,
                                                            String senha,
                                                            GeradorDeHash geradorDeHash) {

        Optional<Usuario> usuarioRef = Usuario.buscarPorUsername(username);

        if (usuarioRef.isPresent()) {
            throw new IllegalArgumentException("Já existe usuário com o username informado");
        }
        Usuario novoUsuario = novoUsuario(username, Papeis.Papel.ALUNO);
        novoUsuario.membro = membro;
        novoUsuario.forcarAlteracaoDeSenha(senha, geradorDeHash);
        novoUsuario.persist();
        return novoUsuario;
    }

    public static Usuario novoUsuario(String username, Papeis.Papel... papeis) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.username = username;
        novoUsuario.definirPapeis(papeis);
        return novoUsuario;
    }

    public static Optional<Usuario> autentica(
            String username,
            String senha,
            GeradorDeHash geradorDeHash) {
        var usuarioLocalizado = Usuario.buscarPorUsername(username);
        if (usuarioLocalizado.isPresent()) {
            var hash = geradorDeHash
                    .gerarHash(new String(usuarioLocalizado.get().salt), senha);
            if (!hash.equals(new String(usuarioLocalizado.get().hash))) {
                return Optional.empty();
            }
        }
        return usuarioLocalizado;
    }

    public void definirPapeis(Papeis.Papel... papeis) {
        this.papeis.clear();
        if (papeis == null) {
            this.papeis.add(Papeis.Papel.ALUNO);
            return;
        }
        this.papeis.addAll(Arrays.asList(papeis));
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
    public Set<Papeis.Papel> papeis = new LinkedHashSet<>();

    public void forcarAlteracaoDeSenha(String senha, GeradorDeHash geradorDeHash) {
        var salt = geradorDeHash.novoSalt();
        this.salt = salt.getBytes(StandardCharsets.UTF_8);
        this.hash = geradorDeHash.gerarHash(salt, senha).getBytes(StandardCharsets.UTF_8);
    }
}
