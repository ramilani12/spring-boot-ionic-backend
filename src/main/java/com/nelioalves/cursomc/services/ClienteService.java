package com.nelioalves.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cidade;
import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.Endereco;
import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.dto.ClienteNewDTO;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.repositories.EnderecoRepository;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository endRepo;

	public Cliente find(final Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	
	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	
	
	public Cliente  insert(Cliente  obj) {
		
		obj.setId(null);
		
		obj = repo.save(obj);
		
		endRepo.saveAll(obj.getEnderecos());
		
		return obj;
	}
	
	public Cliente update (Cliente obj) {
		
		Cliente newObj = find(obj.getId());
		
		updateData(newObj , obj);
		
		return repo.save(newObj);
				
	}
	
	private void updateData(Cliente newObj , Cliente obj) {
		
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());;
		
	}
	
	public Page<Cliente> findPage(Integer page , Integer linerPerPage, String orderBy , String direction){
		
		PageRequest pageRequest = PageRequest.of(page, linerPerPage, Direction.valueOf(direction) , orderBy);
		
		return repo.findAll(pageRequest);
		
	}
	
	public void delete (Integer id) {
		
		find(id);
		
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir prq ha entidades relacionadas");
		}
	}
	
	public Cliente fromDTO(ClienteDTO dto) {
		return new Cliente(dto.getId(), dto.getNome(), dto.getEmail(), null , null);
	}
	
	
	public Cliente fromDTO(ClienteNewDTO cli) {
		
		Cliente cliente = new Cliente(null, cli.getNome(), cli.getEmail(), cli.getCpfOuCnpj(), TipoCliente.toEnum(cli.getTipo()));
		
		Cidade cidade = new Cidade(cli.getCidadeId(), null , null);
		
		Endereco end = new Endereco(null, cli.getLogradouro(), cli.getNumero(), cli.getComplemento(), cli.getBairro(), cli.getCep(), 
				cliente, cidade);
		
		cliente.getEnderecos().add(end);
		
		cliente.getTelefones().add(cli.getTelefone1());
		
		if (cli.getTelefone2() != null) {
			cliente.getTelefones().add(cli.getTelefone2());
		}
		
		if (cli.getTelefone3() != null) {
			cliente.getTelefones().add(cli.getTelefone3());
		}
		
		
		return cliente;
	
	}
	
}
