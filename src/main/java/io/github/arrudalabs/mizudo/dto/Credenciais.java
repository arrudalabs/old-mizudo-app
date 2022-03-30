package io.github.arrudalabs.mizudo.dto;

import javax.validation.constraints.NotBlank;

public record Credenciais(
        @NotBlank
        String username,
        @NotBlank
        String senha) {
}