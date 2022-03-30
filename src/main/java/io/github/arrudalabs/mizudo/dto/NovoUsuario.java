package io.github.arrudalabs.mizudo.dto;

import io.github.arrudalabs.mizudo.validation.SuportaValidacao;
import io.github.arrudalabs.mizudo.validation.Validacao;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@SuportaValidacao(
        message = "senhas não conferem",
        classeValidadora = NovoUsuario.NovoUsuarioValidation.class
)
public record NovoUsuario(@NotNull
                          @NotBlank
                          String username,
                          @NotNull
                          @NotBlank
                          String senha,
                          @NotNull
                          @NotBlank
                          String confirmacaoSenha) {
    public static class NovoUsuarioValidation implements Validacao {
        @Override
        public boolean estahValido(Object object) {
            if (object instanceof NovoUsuario novoUsuario) {
                return Objects.nonNull(novoUsuario)
                        && Objects.nonNull(novoUsuario.senha)
                        && novoUsuario.senha.equals(novoUsuario.confirmacaoSenha);
            }
            return false;
        }
    }
}