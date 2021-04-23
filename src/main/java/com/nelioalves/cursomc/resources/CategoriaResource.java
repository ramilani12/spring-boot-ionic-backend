package com.nelioalves.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.dto.CategoriaDTO;
import com.nelioalves.cursomc.services.CategoriaService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	
	@ApiOperation(value="Busca por id")
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable final Integer id) {		
		
		Categoria categoria = service.find(id);
	
		return ResponseEntity.ok().body(categoria);
	}
	
	@ApiOperation(value="Busca todas as categorias")
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> findAll() {		
		
		List<Categoria> categorias = service.findAll();
		List<CategoriaDTO> categoriasDTO = categorias.stream().map(obj -> new CategoriaDTO(obj))
				.collect(Collectors.toList());
	
		return ResponseEntity.ok().body(categoriasDTO);
	}
	
	@RequestMapping(value="/page" , method=RequestMethod.GET)
	@ApiOperation(value="Busca Categorias por Paginação")
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page , 
			@RequestParam(value="linesPerPage", defaultValue="24")  Integer linerPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy , 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {		
		
		Page<Categoria> categorias = service.findPage(page, linerPerPage, orderBy, direction);
		
		Page<CategoriaDTO> categoriasDTO = categorias.map(obj -> new CategoriaDTO(obj));
	
		return ResponseEntity.ok().body(categoriasDTO);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value="Insere uma categoria")
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDto) {
		
		Categoria obj = service.fromDTO(objDto);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
											 .buildAndExpand(obj.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	@ApiOperation(value="Atualiza uma Categoria")
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDto,@PathVariable final Integer id) {
	
		Categoria obj = service.fromDTO(objDto);
		
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
				
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value="Remove uma Categoria por ID")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Não é possível excluir uma categoria que possui produtos"), @ApiResponse(code = 404, message = "Código inexistente") })
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<Categoria> delete(@PathVariable final Integer id) {		
		
		service.delete(id);
	
		return ResponseEntity.noContent().build();
	}
	
}
