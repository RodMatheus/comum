package br.com.exemplo.comum.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DepartamentoDTO(Long id, String nome, Boolean removido) {
}
