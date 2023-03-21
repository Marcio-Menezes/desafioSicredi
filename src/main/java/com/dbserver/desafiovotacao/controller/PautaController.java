
package com.dbserver.desafiovotacao.controller;

import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.service.PautaService;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Pauta> criarPauta(@Valid @RequestBody Pauta pauta, BindingResult bindingResult) {

        Optional<Pauta> criarNovaPauta = Optional.of(pauta);
        if(bindingResult.hasErrors()|| criarNovaPauta.isEmpty()){
            return new ResponseEntity<>(pauta, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pautaService.salvarPauta(pauta), HttpStatus.CREATED);
    }

    @GetMapping("/numeroassociados/{id}")
    public ResponseEntity<Integer> totalVotantes(@PathVariable UUID id) {
        return new ResponseEntity<>(this.pautaService.totalVotantes(id), HttpStatus.OK);
    }
    // Modificar, pois só mostra uma unica pauta por hash
    @GetMapping("/pautas")
    public ResponseEntity<Iterable<Pauta>> mostrarPauta() {
        return new ResponseEntity<>(this.pautaService.mostraPautas(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pauta> encontrarPautaPorId(@PathVariable UUID id) {
        Optional<Pauta> pauta = pautaService.encontrarPautaPorID(id);
        if (pauta.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pauta.get(), HttpStatus.OK);
        
    }


}

