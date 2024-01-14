package com.proyecto.mx.modelo.servicios;

import java.util.List;

import com.proyecto.mx.modelo.entidades.Multa;

public interface MultaServicio {
	public List<Multa> findAll();
	public Multa findById(Long id);
	public Multa save(Multa multa);
	public void delete(Long id);
}