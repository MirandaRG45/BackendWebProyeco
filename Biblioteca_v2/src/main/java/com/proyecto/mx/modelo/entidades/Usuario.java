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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "Usuario")
@Data
public class Usuario implements Serializable {
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idUsuario;
	
	@NotEmpty(message = "No puede estar vacio")
	@Size(min = 5, max = 80, message = "el tamaño de nombre de usuario tiene que estar entre 5 y 80")
	@NotBlank(message = "El nombre del usuario es obligatorio")
	private String nombreUsuario;
	
	@NotEmpty(message = "No puede estar vacio")
	@Size(min = 6, max = 250, message = "el tamaño de direccion tiene que estar entre 6 y 250")
	@NotBlank(message = "La dirección es obligatorio")
	private String direccion;
	
	@NotEmpty(message = "No puede estar vacio")
	@Size(min = 10, max = 20, message = "el tamaño de telefono tiene que estar entre 10 y 20")
	@NotBlank(message = "El teléfono es obligatorio")
	private String telefono;
	
	@NotEmpty(message = "No puede estar vacio")
	@Size(min = 15, max = 200, message = "el tamaño tiene que estar entre 15 y 200")
	@NotBlank(message = "El correo es obligatorio")
	private String correo;
	
	@Column(name = "fechaRegistro")
	@Temporal(TemporalType.DATE)
	private Date fechaRegistro;

	@PrePersist
	public void prePersist() {
		this.fechaRegistro = new Date();
	}
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "idUsuario", cascade = CascadeType.ALL)
	private List<Prestamo> prestamos;
}
