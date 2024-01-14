package com.proyecto.mx.modelo.dao;

import org.springframework.data.repository.CrudRepository;

import com.proyecto.mx.modelo.entidades.Libro;


public interface LibroDAO extends CrudRepository<Libro, Long>{

}
