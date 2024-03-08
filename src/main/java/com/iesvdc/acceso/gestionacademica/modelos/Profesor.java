package com.iesvdc.acceso.gestionacademica.modelos;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 25)
    private String username; //usuario
    @Column(length = 100)
    private String password; //password
    @Column(length = 50)
    private String nombre; //nombre
    private String apellido; //apellido
    @Column(length = 100)
    private String email; //email

    @ManyToMany
    private List<Rol> roles;
    @ManyToMany
    private List<Asignatura> asignaturas;

}
