package com.dbserver.desafiovotacao.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

import java.util.UUID;

public record PautaRequest(@NotBlank String titulo, UUID idAutor, @NotBlank String hash) {

}
