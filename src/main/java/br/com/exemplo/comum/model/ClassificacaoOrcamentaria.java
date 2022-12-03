package br.com.exemplo.comum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
public class ClassificacaoOrcamentaria implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    @NotNull
    private boolean sintetico;

    @NotNull
    private boolean receita;

    @NotNull
    private boolean permanente;

    @NotNull
    private boolean removido;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private ClassificacaoOrcamentaria pai;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pai")
    private List<ClassificacaoOrcamentaria> filhos;
}