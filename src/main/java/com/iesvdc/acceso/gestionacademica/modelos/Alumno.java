package com.iesvdc.acceso.gestionacademica.modelos;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 25)
    private String username;
    @Column(length = 100)
    private String password; 
    @Column(length = 50)
    private String nombre; 
    private String apellido; 
    @Column(length = 100)
    private String email; 

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'ALUMNO'")
    private String authority;

    @OneToMany
    private List<Asignatura> asignaturas;
}