package com.proyecto.mx.modelo.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.mx.modelo.dao.PrestamoDAO;
import com.proyecto.mx.modelo.entidades.Prestamo;

import jakarta.transaction.Transactional;

@Service
public class PrestamoServicioImpl implements PrestamoServicio{
	@Autowired
	PrestamoDAO dao;
	
	@Override
	public List<Prestamo> findAll() {
		return (List<Prestamo>) dao.findAll() ;
	}

	@Override
	public Prestamo findById(Long id) {
		return dao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Prestamo save(Prestamo prestamo) {
		return dao.save(prestamo);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		dao.deleteById(id);
	}
}

