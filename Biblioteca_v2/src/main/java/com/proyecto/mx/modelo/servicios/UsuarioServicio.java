package com.proyecto.mx.modelo.servicios;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.proyecto.mx.modelo.entidades.Usuario;

public interface UsuarioServicio {
	public List<Usuario> findAll();
	public Usuario findById(Long id);
	public Usuario save(Usuario usuario);
	public void delete(Long id);
	

	public ByteArrayInputStream reporteUsuarios(List<Usuario> usuarios);
}
