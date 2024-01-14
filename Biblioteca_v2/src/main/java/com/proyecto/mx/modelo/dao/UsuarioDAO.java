package com.proyecto.mx.modelo.dao;

import org.springframework.data.repository.CrudRepository;

import com.proyecto.mx.modelo.entidades.Usuario;

public interface UsuarioDAO extends CrudRepository<Usuario, Long> {

}