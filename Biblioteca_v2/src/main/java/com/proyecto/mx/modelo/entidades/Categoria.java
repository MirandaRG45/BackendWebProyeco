package com.proyecto.mx.modelo.entidades;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "Categoria")
@Data
public class Categoria implements Serializable {
private static final long serialVersionUID = 1L;
	public Categoria() {
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCategoria;
	

	@NotEmpty(message = "no puede estar vacio")
	@Size(min = 5, max = 80, message = "el tamaño tiene que estar entre 5 y 80")
	@NotBlank(message = "El Nombre es obligatorio")
	private String nombreCategoria;
	

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "idCategoria", cascade = CascadeType.ALL)
	private List<Libro> libros;

}