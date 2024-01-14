package com.proyecto.mx.modelo.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.mx.modelo.dao.AutorDAO;
import com.proyecto.mx.modelo.entidades.Autor;

@Service
public class AutorServicioImpl implements AutorServicio {
	
	@Autowired
	AutorDAO dao;
	
	@Override
	public List<Autor> findAll() {
		return (List<Autor>) dao.findAll() ;
	}
	
	@Override
	public Autor findById(Long id) {
		return dao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Autor save(Autor autor) {
		return dao.save(autor);
	}
	
	@Override
	@Transactional
	public void delete(Long id) {
		dao.deleteById(id);
	}
	


}
