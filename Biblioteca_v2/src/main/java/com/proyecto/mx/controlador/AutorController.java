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

import com.proyecto.mx.modelo.entidades.Autor;
import com.proyecto.mx.modelo.servicios.AutorServicio;

import jakarta.validation.Valid;


@CrossOrigin (origins = {"*"})
@RestController
@RequestMapping("/apiBiblioteca")
public class AutorController {
	@Autowired
	AutorServicio servicio;
	
	@GetMapping("/autores")
	public List<Autor> readAll(){
		return servicio.findAll();
	}
	
	@PostMapping("/autores")
	@ResponseStatus(HttpStatus.CREATED)
	public Autor save(@RequestBody Autor autor) {
		return servicio.save(autor);
	}
	
	@GetMapping("/autores/{id}")
	public ResponseEntity<?> read(@PathVariable("id") Long id){
		Map<String, Object> respuesta = new HashMap<>();
		Autor autorNuevo = null;
			try {
				autorNuevo = servicio.findById(id);
			}catch(DataAccessException e) {
				respuesta.put("mensaje", "Error al realizar la consulta");
				respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if(autorNuevo == null) {
				respuesta.put("mensaje", "El libro ".concat(id.toString()).concat(" no existe en la base de datos"));
				return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
			}
		return new ResponseEntity<Autor>(autorNuevo, HttpStatus.OK);
	}
	
	@PutMapping("/autores/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Autor autor, BindingResult resultado, @PathVariable("id") Long id){
		Map<String, Object> respuesta = new HashMap<>();
		Autor autorActualizado = null;
		Autor autorBuscado = servicio.findById(id);
		if(autorBuscado == null) {
			respuesta.put("mensaje", "El autor con ID " + id + " no existe.");
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
		}
		if (resultado.hasErrors()) {
			List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo " + err.getField() + " " + err.getDefaultMessage()).
					collect(Collectors.toList());
			respuesta.put("errores", errores);
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.BAD_REQUEST);
		}
		try {
			autorBuscado.setNombreAutor(autor.getNombreAutor());
			autorBuscado.setLibros(autor.getLibros());
			autorActualizado = servicio.save(autorBuscado);
		} catch (DataAccessException e) {
			respuesta.put("mensaje", "Error al actualizar el autor");
			respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		respuesta.put("mensaje", "El autor se actualizó satisfactoriamente");
		respuesta.put("autor", autorActualizado);
		return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/autores/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
	    Map<String, Object> respuesta = new HashMap<>();

	    try {
	        // Verificar si la categoría existe antes de intentar eliminarla
	        if (servicio.findById(id) != null) {
	            servicio.delete(id);
	            respuesta.put("mensaje", "El autor se eliminó satisfactoriamente");
	            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.OK);
	        } else {
	            respuesta.put("mensaje", "El autor con ID " + id + " no existe.");
	            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
	        }
	    } catch (DataAccessException e) {
	        respuesta.put("mensaje", "Error al eliminar el autor");
	        respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getLocalizedMessage()));
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

}
