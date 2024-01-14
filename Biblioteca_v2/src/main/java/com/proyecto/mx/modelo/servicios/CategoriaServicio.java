package com.proyecto.mx.modelo.servicios;

import java.util.List;

import com.proyecto.mx.modelo.entidades.Categoria;

public interface CategoriaServicio {
	public List<Categoria> findAll();
	public Categoria findById(Long id);
	public Categoria save(Categoria categoria);
	public void delete(Long id);
}
