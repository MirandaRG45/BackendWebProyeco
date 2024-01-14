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

import com.proyecto.mx.modelo.entidades.Libro;
import com.proyecto.mx.modelo.entidades.Prestamo;
import com.proyecto.mx.modelo.entidades.Usuario;
import com.proyecto.mx.modelo.servicios.LibroServicio;
import com.proyecto.mx.modelo.servicios.PrestamoServicio;
import com.proyecto.mx.modelo.servicios.UsuarioServicio;

import jakarta.validation.Valid;


@CrossOrigin (origins = {"*"})
@RestController
@RequestMapping("/apiBiblioteca")
public class PrestamoController {
	@Autowired
	PrestamoServicio servicio;
	@Autowired
	LibroServicio libroServicio;
	@Autowired
	UsuarioServicio usuarioServicio;
	
	@GetMapping("/prestamos")
	public List<Prestamo> readAll(){
		return servicio.findAll();
	}
	
	@PostMapping("/prestamos")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> save(@RequestBody Prestamo prestamo) {
	    Map<String, Object> respuesta = new HashMap<>();
	    
	    try {
	    	// Verificar si el prestamo tiene un Usuario válido
	        if (prestamo.getIdUsuario() == null || prestamo.getIdUsuario().getIdUsuario() == null) {
	            respuesta.put("mensaje", "El prestamo debe tener un Usuario válido.");
	            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.BAD_REQUEST);
	        }
        	
	     // Verificar si el Usuario existe
	        Usuario UsuarioExistente = usuarioServicio.findById(prestamo.getIdUsuario().getIdUsuario());
	        if (UsuarioExistente == null) {
	            respuesta.put("mensaje", "El Usuario especificado no existe.");
	            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
	        }
	    	
	    	
	        // Verificar si el prestamo tiene una libro válida
	        if (prestamo.getIdLibro() == null || prestamo.getIdLibro().getIdLibro() == null) {
	            respuesta.put("mensaje", "El prestamo debe tener una libro válida.");
	            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.BAD_REQUEST);
	        }

	        // Verificar si el libro existe
	        Libro LibroExistente = libroServicio.findById(prestamo.getIdLibro().getIdLibro());
	        if (LibroExistente == null) {
	            respuesta.put("mensaje", "El libro especificado no existe.");
	            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
	        }

	        // Asociar el prestamo con la libro existente
	        prestamo.setIdLibro(LibroExistente);
	     // Asociar el prestamo con el Usuario existente
	        prestamo.setIdUsuario(UsuarioExistente);
	        
	        // Guardar el prestamo
	        Prestamo prestamoGuardado = servicio.save(prestamo);
	        
	        respuesta.put("mensaje", "prestamo creado satisfactoriamente.");
	        respuesta.put("prestamo", prestamoGuardado);
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.CREATED);
	    } catch (DataAccessException e) {
	        respuesta.put("mensaje", "Error al crear el prestamo.");
	        respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getMessage()));
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@GetMapping("/prestamos/{id}")
	public ResponseEntity<?> read(@PathVariable("id") Long id){
		Map<String, Object> respuesta = new HashMap<>();
		Prestamo prestamoNuevo = null;
			try {
				prestamoNuevo = servicio.findById(id);
			}catch(DataAccessException e) {
				respuesta.put("mensaje", "Error al realizar la consulta");
				respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if(prestamoNuevo == null) {
				respuesta.put("mensaje", "El prestamo ".concat(id.toString()).concat(" no existe en la base de datos"));
				return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
			}
		return new ResponseEntity<Prestamo>(prestamoNuevo, HttpStatus.OK);
	}
	
	@PutMapping("/prestamos/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Prestamo prestamo, BindingResult resultado, @PathVariable("id") Long id){
		Map<String, Object> respuesta = new HashMap<>();
		Prestamo prestamoActualizado = null;
		Prestamo prestamoBuscado = servicio.findById(id);
		if(prestamoBuscado == null) {
			respuesta.put("mensaje", "El prestamo con ID " + id + " no existe.");
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
		}
		if (resultado.hasErrors()) {
			List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo " + err.getField() + " " + err.getDefaultMessage()).
					collect(Collectors.toList());
			respuesta.put("errores", errores);
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.BAD_REQUEST);
		}
		try {
			prestamoBuscado.setEstadoPrestamo(prestamo.getEstadoPrestamo());
			prestamoBuscado.setFechaDevolucionPrevista(prestamo.getFechaDevolucionPrevista());
			prestamoBuscado.setFechaDevolucionReal(prestamo.getFechaDevolucionReal());
			
			// Obtener la libro de la base de datos
	        Libro Libro = libroServicio.findById(prestamo.getIdLibro().getIdLibro());
	        // Asignar la libro al prestamo
	        prestamoBuscado.setIdLibro(Libro);
	        
	        // Obtener el Usuario de la base de datos
	        Usuario Usuario = usuarioServicio.findById(prestamo.getIdUsuario().getIdUsuario());
	        // Asignar el Usuario al prestamo
	        prestamoBuscado.setIdUsuario(Usuario);

	        // Guardar el prestamo actualizado
			prestamoActualizado = servicio.save(prestamoBuscado);
		} catch (DataAccessException e) {
			respuesta.put("mensaje", "Error al actualizar el prestamo");
			respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		respuesta.put("mensaje", "El prestamo se actualizó satisfactoriamente");
		respuesta.put("prestamo", prestamoActualizado);
		return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/prestamos/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            servicio.delete(id);
        } catch (DataAccessException e) {
            respuesta.put("mensaje", "Error al eliminar el prestamo");
            respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getLocalizedMessage()));
            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        respuesta.put("mensaje", "El prestamo se eliminó satisfactoriamente");
        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.OK);
    }
}
