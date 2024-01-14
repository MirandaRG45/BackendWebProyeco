package com.proyecto.mx.modelo.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.mx.modelo.dao.LibroDAO;
import com.proyecto.mx.modelo.entidades.Libro;


@Service
public class LibroServicioImpl implements LibroServicio{
	@Autowired
	LibroDAO dao;
	
	@Override
	public List<Libro> findAll() {
		return (List<Libro>) dao.findAll() ;
	}

	@Override
	public Libro findById(Long id) {
		return dao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Libro save(Libro libro) {
		return dao.save(libro);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		dao.deleteById(id);
	}
}
