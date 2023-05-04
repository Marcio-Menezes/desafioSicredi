
package com.dbserver.desafiovotacao.service.implementacao;

import com.dbserver.desafiovotacao.dto.VotanteRequest;
import com.dbserver.desafiovotacao.exception.AcaoInvalidaException;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.repository.VotanteRepositorio;
import java.util.Optional;
import java.util.UUID;

import com.dbserver.desafiovotacao.service.VotanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
@Service
public class VotanteServiceImplementacao implements VotanteService {
    private final VotanteRepositorio votanteRepositorio;
    
    @Autowired
	public VotanteServiceImplementacao(VotanteRepositorio votanteRepositorio) {
		this.votanteRepositorio = votanteRepositorio;

	}
        
    @Override
	public Optional<Votante> encontrarVotantePorID(UUID id) throws DataAccessException {
		return votanteRepositorio.findById(id);
	}
        
    @Override
	public long totalVotantes() {
		return votanteRepositorio.count();
	}
    @Override
	public Iterable<Votante> findAll() {
		return this.votanteRepositorio.findAll();
	}
        
    @Override
    public Votante salvarVotante(VotanteRequest votanteRequest) throws AcaoInvalidaException {
		if(!verificaCPF(votanteRequest.cpf())){
			throw new AcaoInvalidaException("CPF Invalido, verifique se está digitando apenas numeros e que eles tenham 11 digitos");
		}else{
			return votanteRepositorio.save(Votante.builder().cpf(votanteRequest.cpf())
					.voto(votanteRequest.votoEnum()).build());
		}
    }

	public boolean verificaCPF(String CPF){
		Integer tamanhoCPF = 11;
		if (CPF == null || CPF.length() != 11|| !CPF.matches("\\d{11}") || CPF.matches("(\\d)\\1{10}")) {
			return false;
		}
		Integer[] primeiraParte = {10, 9, 8, 7, 6, 5, 4, 3, 2};
		Integer[] segundaParte = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
		int[] digitos = new int[tamanhoCPF];
		for (int i = 0; i < tamanhoCPF; i++) {
			digitos[i] = CPF.charAt(i) - '0';
		}
		int primeiroVerificador = calculaDigito(digitos,primeiraParte);
		int segundoVerificador = calculaDigito(digitos,segundaParte);
		if (primeiroVerificador == 10 || primeiroVerificador == 11 ||
				segundoVerificador == 10 || segundoVerificador == 11 ||
				primeiroVerificador != digitos[9] || segundoVerificador != digitos[10]) {
			return false;
		}else{
			return true;
		}
	}
	public int calculaDigito(int[] digitos, Integer[] parte){
		int soma = 0;
		for (int i = 0; i < parte.length; i++) {
			soma += digitos[i] * parte[i];
		}
		int resultado = soma % 11;
		return resultado < 2 ? 0 : 11 - resultado;
	}
}
