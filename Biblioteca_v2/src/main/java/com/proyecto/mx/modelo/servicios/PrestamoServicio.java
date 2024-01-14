package com.proyecto.mx.modelo.servicios;

import java.util.List;

import com.proyecto.mx.modelo.entidades.Prestamo;

public interface PrestamoServicio {
	public List<Prestamo> findAll();
	public Prestamo findById(Long id);
	public Prestamo save(Prestamo prestamo);
	public void delete(Long id);
}