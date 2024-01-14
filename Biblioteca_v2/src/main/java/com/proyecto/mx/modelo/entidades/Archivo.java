package com.proyecto.mx.modelo.entidades;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Archivo")
public class Archivo {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long idArchivo;
		private String nombreArchivo;
		private String tipoArchivo;
		
		@Lob
		@Column(length=429496729)
		private byte[] datosArchivo;
}

