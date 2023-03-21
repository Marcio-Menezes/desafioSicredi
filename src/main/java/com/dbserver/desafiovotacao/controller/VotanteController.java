
package com.dbserver.desafiovotacao.controller;

import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.service.VotanteServiceImplementacao;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/votante")
public class VotanteController {
    private final VotanteServiceImplementacao votanteService;

	@Autowired
	public VotanteController(VotanteServiceImplementacao votanteService) {
		this.votanteService = votanteService;
	}
        
        @RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Votante> criarVotante(@RequestBody @Validated Votante votante, BindingResult bindingResult) {

		Optional<Votante> criarVotante = Optional.of(votante);

		if (bindingResult.hasErrors() || criarVotante.isEmpty()) {
			return new ResponseEntity<>(votante, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(votanteService.salvarVotante(votante), HttpStatus.CREATED);
	}
        
        @GetMapping("/{id}")
	public ResponseEntity<Votante> procuraVotante(@PathVariable UUID id) {

		Optional<Votante> associado;
		associado = votanteService.findById(id);

		if (associado.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(associado.get(), HttpStatus.OK);
	}
        
        @GetMapping("/totalvotantes")
	public Integer totalVotantes() {
		return this.votanteService.totalVotantes();
	}
}
