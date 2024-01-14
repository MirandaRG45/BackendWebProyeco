package com.proyecto.mx.modelo.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.mx.modelo.dao.CategoriaDAO;
import com.proyecto.mx.modelo.entidades.Categoria;

@Service
public class CategoriaServicioImpl implements CategoriaServicio{
	@Autowired
	CategoriaDAO dao;
	
	@Override
	public List<Categoria> findAll() {
		return (List<Categoria>) dao.findAll() ;
	}

	@Override
	public Categoria findById(Long id) {
		return dao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Categoria save(Categoria categoria) {
		return dao.save(categoria);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		dao.deleteById(id);
	}

}
