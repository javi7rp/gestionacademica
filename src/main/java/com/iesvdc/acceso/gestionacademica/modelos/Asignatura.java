package com.iesvdc.acceso.gestionacademica.modelos;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Asignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 25)
    private String nombre;
    @Column(length = 25)
    private int curso;
    @Column(length = 25)
    private String ciclo;

    @ManyToMany(mappedBy = "asignaturas")
    private List<Alumno> alumnos;

    @ManyToOne
    private Profesor profesor;
}
