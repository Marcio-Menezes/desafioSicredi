
package com.dbserver.desafiovotacao.controller;


import com.dbserver.desafiovotacao.dto.VotanteRequest;
import com.dbserver.desafiovotacao.dto.VotanteResponse;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.service.implementacao.PautaServiceImplementacao;
import com.dbserver.desafiovotacao.service.implementacao.VotanteServiceImplementacao;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voto")
public class VotanteController {
    private final VotanteServiceImplementacao votanteService;
	private final PautaServiceImplementacao pautaService;

	@Autowired
	public VotanteController(VotanteServiceImplementacao votanteService, PautaServiceImplementacao pautaService) {
		this.votanteService = votanteService;
		this.pautaService = pautaService;
	}
        
        @RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Votante> criarVotante(@RequestBody @Validated VotanteRequest votanteRequest) {
		return new ResponseEntity<>(votanteService.salvarVotante(votanteRequest), HttpStatus.CREATED);
	}
        
        @GetMapping("/{id}")
	public ResponseEntity<VotanteResponse> procuraVoto(@PathVariable UUID id) {

		Optional<Votante> associado = votanteService.encontrarVotantePorID(id);
		return new ResponseEntity<>(new VotanteResponse(associado.get()), HttpStatus.OK);
	}
        
        @GetMapping("/total")
	public long totalVotantes() {
		return this.votanteService.totalVotantes();
	}
}
