package io.github.arrudalabs.mizudo.services;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.enterprise.context.ApplicationScoped;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@ApplicationScoped
public class GeradorDeHash {

    @ConfigProperty(name = "gerador.senha.algoritmo",defaultValue = "PBKDF2WithHmacSHA512")
    private String algoritmo;
    @ConfigProperty(name = "gerador.senha.iteracoes",defaultValue = "150000")
    private Integer iteracoes;
    @ConfigProperty(name = "gerador.senha.tamanho.chave",defaultValue = "32")
    private Integer tamanhoChave;

    public String gerarHash(String salt, String senha){
        try {
            var hash = SecretKeyFactory.getInstance(this.algoritmo)
                    .generateSecret(
                            new PBEKeySpec(
                                    senha.toCharArray(),
                                    salt.getBytes(StandardCharsets.UTF_8),
                                    this.iteracoes,
                                    this.tamanhoChave
                            )
                    ).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
