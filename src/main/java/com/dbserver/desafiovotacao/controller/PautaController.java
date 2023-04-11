
package com.dbserver.desafiovotacao.controller;

import com.dbserver.desafiovotacao.dto.ClienteRequest;
import com.dbserver.desafiovotacao.dto.PautaRequest;
import com.dbserver.desafiovotacao.dto.PautaResponse;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.service.PautaService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pauta")
public class PautaController {
    private final PautaService pautaService;

    @Autowired
    public PautaController(PautaService pautaService){
        this.pautaService = pautaService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Pauta> criarPauta(@Valid @RequestBody PautaRequest pautaRequest, BindingResult bindingResult) {

        return new ResponseEntity<>(pautaService.salvarPauta(pautaRequest), HttpStatus.CREATED);
    }

    @GetMapping("/numeroassociados/{id}")
    public ResponseEntity<Integer> totalVotantes(@PathVariable UUID id) {
        return new ResponseEntity<>(this.pautaService.totalVotantes(id), HttpStatus.OK);
    }
    @GetMapping("/mostrartodas")
    public ResponseEntity<Page<Pauta>> mostrarPauta(@PageableDefault(size = 10) Pageable pageable) {
        return new ResponseEntity<>(this.pautaService.mostraPautas(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PautaResponse> encontrarPautaPorId(@PathVariable UUID id) {
        Optional<Pauta> pauta = pautaService.encontrarPautaPorID(id);
        return new ResponseEntity<>(new PautaResponse(pauta.get()), HttpStatus.OK);
        
    }

    @GetMapping("/associados/{id}")
    public ResponseEntity<List<Votante>> encontrarVotantesNaPauta(@PathVariable UUID id) {
        Optional<Pauta> pauta = pautaService.encontrarPautaPorID(id);
        return new ResponseEntity<>(pauta.get().getAssociados(), HttpStatus.OK);

    }

    @PostMapping("/adicionavotante/{hash}")
    public ResponseEntity<Pauta> adicionaVotanteNaPauta(@PathVariable String hash, @RequestBody ClienteRequest clienteRequest) {
        return new ResponseEntity<>(pautaService.adicionarAssociado(hash, clienteRequest), HttpStatus.OK);
    }

    @GetMapping("/finaliza/{hash}")
    public ResponseEntity<Pauta> finalizaPauta(@PathVariable String hash) {
        return new ResponseEntity<>(pautaService.finalizarPauta(hash), HttpStatus.OK);

    }


}

