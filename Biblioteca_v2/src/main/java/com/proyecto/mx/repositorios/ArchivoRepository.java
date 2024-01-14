package com.proyecto.mx.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.mx.modelo.entidades.Archivo;


public interface ArchivoRepository  extends JpaRepository<Archivo, Long> {

}
