package com.proyecto.mx.modelo.servicios;

import java.util.List;

import com.proyecto.mx.modelo.entidades.Libro;


public interface LibroServicio {
	public List<Libro> findAll();
	public Libro findById(Long id);
	public Libro save(Libro libro);
	public void delete(Long id);
}
