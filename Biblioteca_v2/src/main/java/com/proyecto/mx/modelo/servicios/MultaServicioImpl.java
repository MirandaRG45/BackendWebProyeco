package com.proyecto.mx.modelo.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.mx.modelo.dao.MultaDAO;
import com.proyecto.mx.modelo.entidades.Multa;

import jakarta.transaction.Transactional;

@Service
public class MultaServicioImpl implements MultaServicio{
	@Autowired
	MultaDAO dao;
	
	@Override
	public List<Multa> findAll() {
		return (List<Multa>) dao.findAll() ;
	}

	@Override
	public Multa findById(Long id) {
		return dao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Multa save(Multa usuario) {
		return dao.save(usuario);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		dao.deleteById(id);
	}
}
