package com.proyecto.mx.modelo.servicios;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto.mx.modelo.entidades.Archivo;
import com.proyecto.mx.repositorios.ArchivoRepository;

@Service
public class ArchivoServiceImpl implements ArchivoService{
	@Autowired
	ArchivoRepository archivoRespository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Archivo> findAll() {
		return (List<Archivo>) archivoRespository.findAll();
	}

	
	@Override
	public Archivo guardar(MultipartFile archivo) throws IOException {
		String nombreDelArchivo = StringUtils.cleanPath(archivo.getOriginalFilename());
		Archivo a = Archivo.builder().nombreArchivo(nombreDelArchivo).tipoArchivo(archivo.getContentType()).datosArchivo(archivo.getBytes()).build();
		
		return archivoRespository.save(a);
	}

	@Override
	public Optional<Archivo> descargar(Long id) throws FileNotFoundException {
		Optional<Archivo> archivo = archivoRespository.findById(id);
		if(archivo.isPresent()) {
			return archivo;
		}
		throw new FileNotFoundException("El archivo buscado no existe");
	}

}
