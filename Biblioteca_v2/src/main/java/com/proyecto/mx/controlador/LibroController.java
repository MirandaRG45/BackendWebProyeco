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
import com.proyecto.mx.modelo.entidades.Autor;
import com.proyecto.mx.modelo.entidades.Categoria;
import com.proyecto.mx.modelo.servicios.AutorServicio;
import com.proyecto.mx.modelo.servicios.CategoriaServicio;
import com.proyecto.mx.modelo.servicios.LibroServicio;

import jakarta.validation.Valid;

@CrossOrigin (origins = {"*"})
@RestController
@RequestMapping("/apiBiblioteca")
public class LibroController {
	@Autowired
	LibroServicio servicio;
	@Autowired
	CategoriaServicio categoriaServicio;
	@Autowired
	AutorServicio autorServicio;
	
	@GetMapping("/libros")
	public List<Libro> readAll(){
		return servicio.findAll();
	}
	
	@PostMapping("/libros")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> save(@RequestBody Libro libro) {
	    Map<String, Object> respuesta = new HashMap<>();
	    
	    try {
	    	// Verificar si el libro tiene un autor válido
	        if (libro.getIdAutor() == null || libro.getIdAutor().getIdAutor() == null) {
	            respuesta.put("mensaje", "El libro debe tener un autor válido.");
	            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.BAD_REQUEST);
	        }
	        
	     // Verificar si el autor existe
	        Autor autorExistente = autorServicio.findById(libro.getIdAutor().getIdAutor());
	        if (autorExistente == null) {
	            respuesta.put("mensaje", "El autor especificado no existe.");
	            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
	        }
	    	
	    	
	        // Verificar si el libro tiene una categoría válida
	        if (libro.getIdCategoria() == null || libro.getIdCategoria().getIdCategoria() == null) {
	            respuesta.put("mensaje", "El libro debe tener una categoría válida.");
	            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.BAD_REQUEST);
	        }

	        // Verificar si la categoría existe
	        Categoria categoriaExistente = categoriaServicio.findById(libro.getIdCategoria().getIdCategoria());
	        if (categoriaExistente == null) {
	            respuesta.put("mensaje", "La categoría especificada no existe.");
	            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
	        }

	        // Asociar el libro con la categoría existente
	        libro.setIdCategoria(categoriaExistente);
	     // Asociar el libro con el autor existente
	        libro.setIdAutor(autorExistente);
	        
	        // Guardar el libro
	        Libro libroGuardado = servicio.save(libro);
	        respuesta.put("mensaje", "Libro creado satisfactoriamente.");
	        respuesta.put("libro", libroGuardado);
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.CREATED);
	    } catch (DataAccessException e) {
	        respuesta.put("mensaje", "Error al crear el libro.");
	        respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getMessage()));
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@GetMapping("/libros/{id}")
	public ResponseEntity<?> read(@PathVariable("id") Long id){
		Map<String, Object> respuesta = new HashMap<>();
		Libro libroNuevo = null;
			try {
				libroNuevo = servicio.findById(id);
			}catch(DataAccessException e) {
				respuesta.put("mensaje", "Error al realizar la consulta");
				respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if(libroNuevo == null) {
				respuesta.put("mensaje", "El libro ".concat(id.toString()).concat(" no existe en la base de datos"));
				return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
			}
		return new ResponseEntity<Libro>(libroNuevo, HttpStatus.OK);
	}
	
	@PutMapping("/libros/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Libro libro, BindingResult resultado, @PathVariable("id") Long id){
		Map<String, Object> respuesta = new HashMap<>();
		Libro libroActualizado = null;
		Libro libroBuscado = servicio.findById(id);
		if(libroBuscado == null) {
			respuesta.put("mensaje", "El libro con ID " + id + " no existe.");
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
		}
		if (resultado.hasErrors()) {
			List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo " + err.getField() + " " + err.getDefaultMessage()).
					collect(Collectors.toList());
			respuesta.put("errores", errores);
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.BAD_REQUEST);
		}
		try {
			libroBuscado.setAnoPublicacion(libro.getAnoPublicacion());
			libroBuscado.setEditorial(libro.getEditorial());
			libroBuscado.setEjemplaresDisponibles(libro.getEjemplaresDisponibles());
			libroBuscado.setTituloLibro(libro.getTituloLibro());
			
			// Obtener la categoría de la base de datos
	        Categoria categoria = categoriaServicio.findById(libro.getIdCategoria().getIdCategoria());
	        // Asignar la categoría al libro
	        libroBuscado.setIdCategoria(categoria);
	        
	        // Obtener el autor de la base de datos
	        Autor autor = autorServicio.findById(libro.getIdAutor().getIdAutor());
	        // Asignar el autor al libro
	        libroBuscado.setIdAutor(autor);

	        // Guardar el libro actualizado
			libroActualizado = servicio.save(libroBuscado);
		} catch (DataAccessException e) {
			respuesta.put("mensaje", "Error al actualizar el libro");
			respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		respuesta.put("mensaje", "El libro se actualizó satisfactoriamente");
		respuesta.put("libro", libroActualizado);
		return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/libros/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            servicio.delete(id);
        } catch (DataAccessException e) {
            respuesta.put("mensaje", "Error al eliminar el libro");
            respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getLocalizedMessage()));
            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        respuesta.put("mensaje", "El libro se eliminó satisfactoriamente");
        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.OK);
    }
}
