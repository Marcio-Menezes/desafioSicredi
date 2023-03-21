
package com.dbserver.desafiovotacao.controller;

import com.dbserver.desafiovotacao.model.Assembleia;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.service.AssembleiaServiceImplementacao;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
	public ResponseEntity<Assembleia> procuraAssembleia(@PathVariable UUID id) {

		Optional<Assembleia> assembleiaAtual;
		assembleiaAtual = assembleiaService.encontrarAssembleiaPorID(id);

		if (assembleiaAtual.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(assembleiaAtual.get(), HttpStatus.OK);
	}
        
        @GetMapping("/numeropautas/{id}")
    public ResponseEntity<Integer> totalPautas(@PathVariable UUID id) {
        return new ResponseEntity<>(this.assembleiaService.totalPautas(id), HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<Assembleia> criarAssembleia(@Valid @RequestBody Assembleia assembleia, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(assembleia, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(assembleiaService.salvarAssembleia(assembleia), HttpStatus.CREATED);
    }
    
}
