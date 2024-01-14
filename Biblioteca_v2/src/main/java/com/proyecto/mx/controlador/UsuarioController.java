package com.proyecto.mx.controlador;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.proyecto.mx.modelo.entidades.Usuario;
import com.proyecto.mx.modelo.servicios.EmailService;
import com.proyecto.mx.modelo.servicios.UsuarioServicio;

import jakarta.validation.Valid;


@CrossOrigin (origins = {"*"})
@RestController
@RequestMapping("/apiBiblioteca")
public class UsuarioController {
	@Autowired
	UsuarioServicio servicio;
	
	@Autowired
	EmailService emailService;
	
	@GetMapping("/usuarios")
	public List<Usuario> readAll(){
		return servicio.findAll();
	}
	
	@PostMapping("/usuarios")
	@ResponseStatus(HttpStatus.CREATED)
	public Usuario save(@RequestBody Usuario usuario) {
		Usuario nuevoUsuario = servicio.save(usuario);
		
		// Envío de correo
		String to = usuario.getCorreo();
		String subject = "Usuario registrado con éxito";
		String text = "¡Gracias por registrarte como usuario, " + usuario.getNombreUsuario() + "!";

		emailService.sendSimpleMessage(to, subject, text);		
				
		return nuevoUsuario;
	}
	
			
	
	@GetMapping("/usuarios/{id}")
	public ResponseEntity<?> read(@PathVariable("id") Long id){
		Map<String, Object> respuesta = new HashMap<>();
		Usuario usuarioNuevo = null;
			try {
				usuarioNuevo = servicio.findById(id);
			}catch(DataAccessException e) {
				respuesta.put("mensaje", "Error al realizar la consulta");
				respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if(usuarioNuevo == null) {
				respuesta.put("mensaje", "El libro ".concat(id.toString()).concat(" no existe en la base de datos"));
				return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
			}
		return new ResponseEntity<Usuario>(usuarioNuevo, HttpStatus.OK);
	}
	
	@PutMapping("/usuarios/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Usuario usuario, BindingResult resultado, @PathVariable("id") Long id){
		Map<String, Object> respuesta = new HashMap<>();
		Usuario usuarioActualizado = null;
		Usuario usuarioBuscado = servicio.findById(id);
		if(usuarioBuscado == null) {
			respuesta.put("mensaje", "El usuario con ID " + id + " no existe.");
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
		}
		if (resultado.hasErrors()) {
			List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo " + err.getField() + " " + err.getDefaultMessage()).
					collect(Collectors.toList());
			respuesta.put("errores", errores);
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.BAD_REQUEST);
		}
		try {
			usuarioBuscado.setNombreUsuario(usuario.getNombreUsuario());
			usuarioBuscado.setCorreo(usuario.getCorreo());
			usuarioBuscado.setTelefono(usuario.getTelefono());
			usuarioBuscado.setDireccion(usuario.getDireccion());
			usuarioActualizado = servicio.save(usuarioBuscado);
		} catch (DataAccessException e) {
			respuesta.put("mensaje", "Error al actualizar el usuario");
			respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		respuesta.put("mensaje", "El usuario se actualizó satisfactoriamente");
		respuesta.put("usuario", usuarioActualizado);
		
		// Envío de correo
	    String to = usuario.getCorreo();
	    String subject = "Usuario actualizado con éxito";
	    String text = usuario.getNombreUsuario() + ", tus datos han sido actualizados correctamente!";

	    emailService.sendSimpleMessage(to, subject, text);

		
		return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/usuarios/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
	    Map<String, Object> respuesta = new HashMap<>();

	    try {
	        // Verificar si el usuario existe antes de intentar eliminarla
	        if (servicio.findById(id) != null) {
		         // Envío de correo
				String to = servicio.findById(id).getCorreo();
				String subject = "Usuario eliminado con éxito";
				String text = servicio.findById(id).getNombreUsuario() + ", tu usuario ha sido eliminado exitosamente";

				emailService.sendSimpleMessage(to, subject, text);
	        	
	            servicio.delete(id);
	            respuesta.put("mensaje", "El usuario se eliminó satisfactoriamente");


	            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.OK);
	        } else {
	            respuesta.put("mensaje", "El usuario con ID " + id + " no existe.");
	            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
	        }
	    } catch (DataAccessException e) {
	        respuesta.put("mensaje", "Error al eliminar el usuario");
	        respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getLocalizedMessage()));
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@GetMapping(value = "/usuarios/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> reporteAsistente() throws IOException {
		List<Usuario> listaDeUsuarios = (List<Usuario>) servicio.findAll();

		ByteArrayInputStream bis = servicio.reporteUsuarios(listaDeUsuarios);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=listaDeProductos.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}
}
