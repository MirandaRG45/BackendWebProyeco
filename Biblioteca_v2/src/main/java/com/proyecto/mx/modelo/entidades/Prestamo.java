package com.proyecto.mx.modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name="Prestamo")
@Data
public class Prestamo implements Serializable{
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPrestamo;
	
	@Column(name = "fechaPrestamo")
	@Temporal(TemporalType.DATE)
	private Date fechaPrestamo;

	@PrePersist
	public void prePersist() {
		this.fechaPrestamo = new Date();
	}
	
	@NotNull(message = "No puede estar vacio la fecha devolucion prevista")
	private Date fechaDevolucionPrevista;
	
	@NotNull(message = "No puede estar vacio la fecha devolucion real")
	private Date fechaDevolucionReal;
	
	@NotEmpty(message = "No puede estar vacio")
	@Size(min = 5, max = 15, message = "el tamaño tiene que estar entre 5 y 15")
	@NotBlank(message = "La fecha de devolucion prevista obligatoria")
	private String estadoPrestamo;
	
	@ManyToOne
	@JoinColumn(name="idLibro")
	private Libro idLibro;
	
	@ManyToOne
	@JoinColumn(name="idUsuario")
	private Usuario idUsuario;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "idPrestamo", cascade = CascadeType.ALL)
	private List<Multa> multas;

}
