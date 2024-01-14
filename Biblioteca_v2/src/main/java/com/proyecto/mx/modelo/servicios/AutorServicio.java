package com.proyecto.mx.modelo.servicios;

import java.util.List;

import com.proyecto.mx.modelo.entidades.Autor;

public interface AutorServicio {
	public List<Autor> findAll();
	public Autor findById(Long id);
	public Autor save(Autor autor);
	public void delete(Long id);
}
