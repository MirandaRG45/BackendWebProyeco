package com.proyecto.mx.controlador;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

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

import com.proyecto.mx.modelo.entidades.Multa;
import com.proyecto.mx.modelo.servicios.MultaServicio;

import jakarta.validation.Valid;

@CrossOrigin (origins = {"*"})
@RestController
@RequestMapping("/apiBiblioteca")
public class MultaController {
	@Autowired
	MultaServicio servicio;
	
	@GetMapping("/multas")
	public List<Multa> readAll(){
		return servicio.findAll();
	}
	
	@PostMapping("/multas")
	@ResponseStatus(HttpStatus.CREATED)
	public Multa save(@RequestBody Multa multa) {
		return servicio.save(multa);
	}
	
	@GetMapping("/multas/{id}")
	public ResponseEntity<?> read(@PathVariable("id") Long id){
		Map<String, Object> respuesta = new HashMap<>();
		Multa multaNuevo = null;
			try {
				multaNuevo = servicio.findById(id);
			}catch(DataAccessException e) {
				respuesta.put("mensaje", "Error al realizar la consulta");
				respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if(multaNuevo == null) {
				respuesta.put("mensaje", "El libro ".concat(id.toString()).concat(" no existe en la base de datos"));
				return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
			}
		return new ResponseEntity<Multa>(multaNuevo, HttpStatus.OK);
	}
	
	@PutMapping("/multas/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Multa multa, BindingResult resultado, @PathVariable("id") Long id){
		Map<String, Object> respuesta = new HashMap<>();
		Multa multaActualizado = null;
		Multa multaBuscado = servicio.findById(id);
		if(multaBuscado == null) {
			respuesta.put("mensaje", "La multa con ID " + id + " no existe.");
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
		}
		if (resultado.hasErrors()) {
			List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo " + err.getField() + " " + err.getDefaultMessage()).
					collect(Collectors.toList());
			respuesta.put("errores", errores);
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.BAD_REQUEST);
		}
		try {
			multaBuscado.setMontoMulta(multa.getMontoMulta());
			multaBuscado.setEstadoDelPago(multa.getEstadoDelPago());
			multaActualizado = servicio.save(multaBuscado);
		} catch (DataAccessException e) {
			respuesta.put("mensaje", "Error al actualizar la multa");
			respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		respuesta.put("mensaje", "El multa se actualizó satisfactoriamente");
		respuesta.put("multa", multaActualizado);
		return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/multas/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
	    Map<String, Object> respuesta = new HashMap<>();

	    try {
	        // Verificar si el multa existe antes de intentar eliminarla
	        if (servicio.findById(id) != null) {
	            servicio.delete(id);
	            respuesta.put("mensaje", "El multa se eliminó satisfactoriamente");
	            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.OK);
	        } else {
	            respuesta.put("mensaje", "El multa con ID " + id + " no existe.");
	            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
	        }
	    } catch (DataAccessException e) {
	        respuesta.put("mensaje", "Error al eliminar el multa");
	        respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getLocalizedMessage()));
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}
