package com.proyecto.mx.controlador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto.mx.dto.RespuestaDTO;
import com.proyecto.mx.modelo.entidades.Archivo;
import com.proyecto.mx.modelo.servicios.ArchivoService;


@SpringBootApplication
@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/apiDocumentos")
public class ArchivoController {
	
	@Autowired
	ArchivoService service;
	
	@GetMapping("/archivos")
	public List<Archivo> readAll() {
		return service.findAll();
	}
	
	@PostMapping("/archivos/guardar")
	public ResponseEntity<RespuestaDTO> guardarArchivo(@RequestParam ("archivo") MultipartFile archivo) throws  IOException{
		service.guardar(archivo);
		return ResponseEntity.status(HttpStatus.OK).body(new RespuestaDTO("El archivo se almaceno satisfactoriamente"));
	}
	
	@GetMapping("archivos/descargar/{id}")
	public ResponseEntity<byte[]> descargarArchivo(@PathVariable Long id) throws  FileNotFoundException{
		Archivo archivo = service.descargar(id).get();
		return ResponseEntity.status(HttpStatus.OK).
				header(HttpHeaders.CONTENT_TYPE, archivo.getTipoArchivo()).
				header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename\"" + archivo.getNombreArchivo() + "\"").
				body(archivo.getDatosArchivo());
	}
}


