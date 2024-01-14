package com.proyecto.mx.controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.mx.modelo.entidades.Categoria;
import com.proyecto.mx.modelo.servicios.CategoriaServicio;

import jakarta.validation.Valid;


@CrossOrigin (origins = {"*"})
@RestController
@RequestMapping("/apiBiblioteca")
public class CategoriaController {
	@Autowired
	CategoriaServicio servicio;
	@GetMapping("/categorias")
	public List<Categoria> readAll(){
		return servicio.findAll();
	}
	
	
	@PostMapping("/categorias")
	@ResponseStatus(HttpStatus.CREATED)
	public Categoria save(@RequestBody Categoria categoria) {
		return servicio.save(categoria);
	}
	
	@GetMapping("/categorias/{id}")
	public ResponseEntity<?> read(@PathVariable("id") Long id){
		Map<String, Object> respuesta = new HashMap<>();
		Categoria categoriaNueva = null;
			try {
				categoriaNueva = servicio.findById(id);
			}catch(DataAccessException e) {
				respuesta.put("mensaje", "Error al realizar la consulta");
				respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if(categoriaNueva == null) {
				respuesta.put("mensaje", "El libro ".concat(id.toString()).concat(" no existe en la base de datos"));
				return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
			}
		return new ResponseEntity<Categoria>(categoriaNueva, HttpStatus.OK);
	}
	
	@PutMapping("/categorias/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Categoria categoria, BindingResult resultado, @PathVariable("id") Long id){
		Map<String, Object> respuesta = new HashMap<>();
		Categoria categoriaActualizada = null;
		Categoria categoriaBuscada = servicio.findById(id);
		if(categoriaBuscada == null) {
			respuesta.put("mensaje", "La categoria con ID " + id + " no existe.");
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
		}
		if (resultado.hasErrors()) {
			List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo " + err.getField() + " " + err.getDefaultMessage()).
					collect(Collectors.toList());
			respuesta.put("errores", errores);
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.BAD_REQUEST);
		}
		try {
			categoriaBuscada.setNombreCategoria(categoria.getNombreCategoria());
			categoriaBuscada.setLibros(categoria.getLibros());
			categoriaActualizada = servicio.save(categoriaBuscada);
		} catch (DataAccessException e) {
			respuesta.put("mensaje", "Error al actualizar la categoria");
			respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		respuesta.put("mensaje", "La categoria se actualizó satisfactoriamente");
		respuesta.put("categoria", categoriaActualizada);
		return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/categorias/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
	    Map<String, Object> respuesta = new HashMap<>();

	    try {
	        // Verificar si la categoría existe antes de intentar eliminarla
	        if (servicio.findById(id) != null) {
	            servicio.delete(id);
	            respuesta.put("mensaje", "La categoría se eliminó satisfactoriamente");
	            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.OK);
	        } else {
	            respuesta.put("mensaje", "La categoría con ID " + id + " no existe.");
	            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
	        }
	    } catch (DataAccessException e) {
	        respuesta.put("mensaje", "Error al eliminar la categoría");
	        respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getLocalizedMessage()));
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

}
