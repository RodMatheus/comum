package br.com.exemplo.comum.api.v1.controller;

import br.com.exemplo.comum.api.v1.filter.FiltroClassificacaoOrcamentaria;
import br.com.exemplo.comum.api.v1.mapper.ClassificacaoOrcamentariaMapper;
import br.com.exemplo.comum.api.v1.model.dto.ClassificacaoOrcamentariaDTO;
import br.com.exemplo.comum.api.v1.model.input.ClassificacaoOrcamentariaParam;
import br.com.exemplo.comum.api.v1.openapi.ClassificacaoOrcamentariaControllerOpenApi;
import br.com.exemplo.comum.core.security.CheckSecurity;
import br.com.exemplo.comum.domain.model.entities.ClassificacaoOrcamentaria;
import br.com.exemplo.comum.domain.repository.ClassificacaoOrcamentariaRepository;
import br.com.exemplo.comum.domain.service.ClassificacaoOrcamentariaService;
import br.com.exemplo.comum.infrastructure.util.WebUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/classificacoes-orcamentarias",
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class ClassificacaoOrcamentariaController implements ClassificacaoOrcamentariaControllerOpenApi {

    private final ClassificacaoOrcamentariaService classificacaoOrcamentariaService;
    private final ClassificacaoOrcamentariaRepository classificacaoOrcamentariaRepository;
    private final ClassificacaoOrcamentariaMapper classificacaoOrcamentariaMapper;

    public ClassificacaoOrcamentariaController(ClassificacaoOrcamentariaService classificacaoOrcamentariaService,
                                               ClassificacaoOrcamentariaRepository classificacaoOrcamentariaRepository,
                                               ClassificacaoOrcamentariaMapper classificacaoOrcamentariaMapper) {
        this.classificacaoOrcamentariaService = classificacaoOrcamentariaService;
        this.classificacaoOrcamentariaRepository = classificacaoOrcamentariaRepository;
        this.classificacaoOrcamentariaMapper = classificacaoOrcamentariaMapper;
    }

    @GetMapping
    @CheckSecurity.comum.all
    public ResponseEntity<List<ClassificacaoOrcamentariaDTO>> get(FiltroClassificacaoOrcamentaria filtros) {
        log.info("LISTAGEM DE CLASSIFICAÇÕES ORÇAMENTÁRIAS");
        List<ClassificacaoOrcamentariaDTO> classificacoesOrcamentariasDTO = List.of();

        log.info("Contando classificações orçamentárias por filtros. FILTROS: {}.", filtros);
        final Long total = classificacaoOrcamentariaRepository.contaPorFiltros(filtros);

        if(total > 0) {
            log.info("Pesquisando classificações orçamentárias.");
            final List<ClassificacaoOrcamentaria> classificacaoOrcamentarias = classificacaoOrcamentariaRepository
                    .pesquisaPorFiltros(filtros);

            log.info("Convertendo entidades de Planos de contas em DTO.");
            classificacoesOrcamentariasDTO = classificacaoOrcamentariaMapper.toResourceList(classificacaoOrcamentarias);
        }

        log.info("Retornando dtos de resposta. DTOS: {}.", classificacoesOrcamentariasDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(WebUtil.X_TOTAL_COUNT_HEADER, String.valueOf(total))
                .body(classificacoesOrcamentariasDTO);
    }

    @GetMapping("/{id}")
    @CheckSecurity.comum.all
    public ResponseEntity<ClassificacaoOrcamentariaDTO> getById(@PathVariable Long id) {
        log.info("LISTAGEM DE CLASSIFICAÇÃO ORÇAMENTÁRIA POR ID");

        log.info("Iniciando processo de busca por ID.");
        final ClassificacaoOrcamentaria classificacaoOrcamentaria = classificacaoOrcamentariaService.pesquisaClassificacaoOrcamentariaPorId(id);

        log.info("Convertendo entidade de classificação orçamentária em DTO." + " CLASSIFICAÇÃO ORÇAMENTARIA: {}.", classificacaoOrcamentaria);
        final ClassificacaoOrcamentariaDTO classificacaoOrcamentariaDTO = classificacaoOrcamentariaMapper.toResource(classificacaoOrcamentaria);

        log.info("Retornando resposta da operação.");
        return ResponseEntity.ok(classificacaoOrcamentariaDTO);
    }

    @PostMapping
    @CheckSecurity.comum.maintain
    public ResponseEntity<Void> post(@Valid @RequestBody ClassificacaoOrcamentariaParam classificacaoOrcamentariaParam) {
        log.info("CADASTRO DE CLASSIFICAÇÕES ORÇAMENTÁRIAS");

        log.info("Iniciando processo de cadastramento da classificação orçamentária." +
                " CLASSIFICAÇÃO ORÇAMENTÁRIA: {}.", classificacaoOrcamentariaParam);
        classificacaoOrcamentariaService.cadastraClassificacaoOrcamentaria(classificacaoOrcamentariaParam);

       log.info("Retornando resposta da operação.");
       return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}")
    @CheckSecurity.comum.maintain
    public ResponseEntity<ClassificacaoOrcamentariaDTO> put(@PathVariable Long id,
                                                @Valid @RequestBody ClassificacaoOrcamentariaParam classificacaoOrcamentariaParam) {
        log.info("ATUALIZAÇÃO DE CLASSIFICAÇÕES ORÇAMENTÁRIAS");

        log.info("Iniciando processo de atualização da classificação orçamentária. CLASSIFICAÇÃO ORÇAMENTÁRIA: {}.", classificacaoOrcamentariaParam);
        final ClassificacaoOrcamentaria classificacaoOrcamentaria = classificacaoOrcamentariaService
                .atualizaClassificacaoOrcamentaria(classificacaoOrcamentariaParam, id);

        log.info("Convertendo entidade de classificação orçamentária em DTO.");
        final ClassificacaoOrcamentariaDTO classificacaoOrcamentariaDTO = classificacaoOrcamentariaMapper.toResource(classificacaoOrcamentaria);

        log.info("Retornando resposta da operação. DTO: {}.", classificacaoOrcamentariaDTO);
        return ResponseEntity.ok(classificacaoOrcamentariaDTO);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity.comum.maintain
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("EXCLUSÃO DE CLASSIFICAÇÕES ORÇAMENTÁRIAS");

        log.info("Iniciando processo de exclusão de classificação orçamentaria.");
        classificacaoOrcamentariaService.removeClassificacaoOrcamentaria(id);

        log.info("Retornando resposta da operação.");
        return ResponseEntity
                .noContent()
                .build();
    }
}