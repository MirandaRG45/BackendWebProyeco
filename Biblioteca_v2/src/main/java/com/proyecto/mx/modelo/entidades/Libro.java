package com.proyecto.mx.modelo.entidades;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "Libro")
@Data
public class Libro implements Serializable {
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idLibro;
	

	@NotEmpty(message = "No puede estar vacio")
	@Size(min = 5, max = 100, message = "el tamaño tiene que estar entre 5 y 100")
	@NotBlank(message = "El titulo es obligatorio")
	private String tituloLibro;
	

	@NotNull(message = "El año de publicación es obligatorio")
	private Integer anoPublicacion;
	
	
	@NotEmpty(message = "No puede estar vacio")
	@Size(min = 5, max = 50, message = "El tamaño tiene que estar entre 5 y 50")
	@NotBlank(message = "El nombre de la editorial es obligatorioo")
	private String editorial;

	@NotNull(message = "Los ejemplares son obligatorio")
	private Integer ejemplaresDisponibles;
	
	@NotNull(message = "El año de publicación es obligatorio")
	private Integer totalEjemplares;
	
	@ManyToOne
	@JoinColumn(name="idCategoria")
	private Categoria idCategoria;
	
	@ManyToOne
	@JoinColumn(name="idAutor")
	private Autor idAutor;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "idLibro", cascade = CascadeType.ALL)
	private List<Prestamo> prestamos;
	
	public Libro() {
		
	}

}
