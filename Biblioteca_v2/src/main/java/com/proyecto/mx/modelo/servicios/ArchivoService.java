package com.proyecto.mx.modelo.servicios;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.proyecto.mx.modelo.entidades.Archivo;

public interface ArchivoService {
	public Archivo guardar(MultipartFile archivo) throws IOException;
	
	public Optional<Archivo> descargar(Long id) throws FileNotFoundException;
	
	public List<Archivo> findAll();

}