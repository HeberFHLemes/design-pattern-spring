package com.dio.design_patterns_spring.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dio.design_patterns_spring.model.Cliente;
import com.dio.design_patterns_spring.model.Endereco;
import com.dio.design_patterns_spring.repository.ClienteRepository;
import com.dio.design_patterns_spring.repository.EnderecoRepository;
import com.dio.design_patterns_spring.service.ClienteService;
import com.dio.design_patterns_spring.service.ViaCepService;
import com.dio.design_patterns_spring.util.exception.CepInvalidoException;

@Service
public class ClienteServiceImpl implements ClienteService {

	// Singleton: Injetar os componentes do Spring com @Autowired.
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private ViaCepService viaCepService;
	
	// Padrão de Projeto Factory Method utilizado por org.slf4j.LoggerFactory
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Iterable<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		Optional<Cliente> clienteOp = clienteRepository.findById(id);
		if (clienteOp.isEmpty()) {
			logger.error("Não foi possível encontrar o cliente...");
		}
		return clienteOp.orElse(null);
	}

	@Override
	public void inserir(Cliente cliente) {
		if (cliente == null || cliente.getNome() == null || cliente.getEndereco().getCep() == null) {
			logger.error("Tentativa de inserir cliente com campos necessários nulos");
			return;
		}
		
		try {			
			salvarClienteComCep(cliente);
		} catch (CepInvalidoException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		Optional<Cliente> clienteBd = clienteRepository.findById(id);
		if (clienteBd.isEmpty()) {
			logger.error("Não foi possível encontrar o cliente...");
			return;
		}
		
		Cliente clienteExistente = clienteBd.get();
		if (cliente.getId() == null) cliente.setId(id);
		if (cliente.getNome() == null) cliente.setNome(clienteExistente.getNome());
		if (cliente.getEndereco() == null) cliente.setEndereco(clienteExistente.getEndereco());
		
		try {			
			salvarClienteComCep(cliente);
		} catch (CepInvalidoException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void deletar(Long id) {
		if (id == null) {
			logger.info("Tentativa de deletar id nulo");
			return;
		}
		if (clienteRepository.findById(id).isPresent()) {
			clienteRepository.deleteById(id);
			logger.info("Cliente com id: {} deletado.", id);
		} else {
			logger.info("Cliente com id: {} não encontrado...", id);
		}
	}
	
	/**
	 * Torna possível inserir ou atualizar um cliente
	 * com base apenas no nome e cep, tendo o 
	 * endereço preechido com a consulta no ViaCep.
	 * @param cliente {@code Cliente} a ser salvo.
	 */
	private void salvarClienteComCep(Cliente cliente) {
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(
					() -> {
						Endereco novoEndereco = viaCepService.consultarCep(cep);
						if (novoEndereco == null) {
							throw new CepInvalidoException("Tentativa de cadastrar endereço falhou... Verifique o CEP.");
						}
						enderecoRepository.save(novoEndereco);
						logger.info("Novo endereço cadastrado com sucesso.");
						return novoEndereco;
					}
				);
		cliente.setEndereco(endereco);
		clienteRepository.save(cliente);
		logger.info("Novo cliente cadastrado com sucesso.");
	}
}