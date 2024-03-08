package com.iesvdc.acceso.gestionacademica.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iesvdc.acceso.gestionacademica.modelos.Alumno;

public interface RepoAlumno extends JpaRepository<Alumno, Long>{
    
}