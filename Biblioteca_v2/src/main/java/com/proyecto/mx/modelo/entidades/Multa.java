package com.proyecto.mx.modelo.entidades;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "Multa")
@Data
public class Multa implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idMulta;
	
	@NotNull(message = "No puede estar vacio el monto")
	private Long montoMulta;
	
	@NotNull(message = "No puede estar vacio el estado")
	private Long estadoDelPago;
	
	@ManyToOne
	@JoinColumn(name="idPrestamo")
	private Prestamo idPrestamo;

}

