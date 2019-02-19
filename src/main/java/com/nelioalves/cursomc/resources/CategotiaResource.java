package com.nelioalves.cursomc.resources;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nelioalves.cursomc.domain.Categoria;

@RestController
@RequestMapping(value="/categorias")
public class CategotiaResource {
	
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Categoria> listar() {		
		
		Categoria c = new Categoria(1,"Informática");
		
		Categoria c1 = new Categoria(2,"Escritório");
		
		List<Categoria> lista = new ArrayList<Categoria>();
		
		lista.add(c);
		lista.add(c1);
		
		return lista;
	}
	
}
