
package com.dbserver.desafiovotacao.controller;

import com.dbserver.desafiovotacao.dto.AssembleiaRequest;
import com.dbserver.desafiovotacao.dto.AssembleiaResponse;
import com.dbserver.desafiovotacao.dto.ClienteRequest;
import com.dbserver.desafiovotacao.model.Assembleia;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.service.implementacao.AssembleiaServiceImplementacao;
import jakarta.validation.Valid;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class AssembleiaController {
    private AssembleiaServiceImplementacao assembleiaService;

    @Autowired
    public AssembleiaController(AssembleiaServiceImplementacao assembleiaService) {
        this.assembleiaService = assembleiaService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssembleiaResponse> procuraAssembleia(@PathVariable UUID id) {
        Optional<Assembleia> assembleiaAtual = assembleiaService.encontrarAssembleiaPorID(id);
        return new ResponseEntity<>(new AssembleiaResponse(assembleiaAtual.get()), HttpStatus.OK);
    }

    @GetMapping("/numeropautas/{id}")
    public ResponseEntity<Integer> totalPautas(@PathVariable UUID id) {
        return new ResponseEntity<>(this.assembleiaService.totalPautas(id), HttpStatus.OK);
    }

    @GetMapping("/tudo")
    public ResponseEntity<Page<Assembleia>> mostrarTudo(@PageableDefault(size = 10) Pageable pageable) {
        Page<Assembleia> assembleiaPage = this.assembleiaService.mostraTudo(pageable);
        return new ResponseEntity<>(assembleiaPage, HttpStatus.OK);
    }

    @GetMapping("/mostrarassembleias")
    public ResponseEntity<Page<AssembleiaResponse>> mostrarAssembleias(@PageableDefault(size = 10) Pageable pageable) {
        return new ResponseEntity<>(this.assembleiaService.mostrarAssembleias(pageable), HttpStatus.OK);
    }

    @GetMapping("/mostrarpautas/{id}")
    public ResponseEntity<Iterable<Pauta>> mostrarPautas(@PathVariable UUID id) {
        return new ResponseEntity<>(this.assembleiaService.mostraPautas(id), HttpStatus.OK);
    }

    @GetMapping("/finalizarassembleia/{id}")
    public ResponseEntity<Assembleia> finalizaAssembleia(@PathVariable UUID id) {
        return new ResponseEntity<>(this.assembleiaService.finalizarAssembleia(id), HttpStatus.OK);
    }

    @PostMapping("/adicionapauta/{id}")
    public ResponseEntity<Assembleia> adicionaPautaNaAssembleia(@PathVariable UUID id, @RequestBody ClienteRequest clienteRequest) {
        return new ResponseEntity<>(this.assembleiaService.adicionarPauta(id, clienteRequest), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Assembleia> criarAssembleia(@Valid @RequestBody AssembleiaRequest assembleiaRequest) {
        return new ResponseEntity<>(assembleiaService.salvarAssembleia(assembleiaRequest), HttpStatus.CREATED);
    }

}
