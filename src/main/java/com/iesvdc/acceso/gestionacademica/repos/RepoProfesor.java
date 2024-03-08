package com.iesvdc.acceso.gestionacademica.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iesvdc.acceso.gestionacademica.modelos.Profesor;

public interface RepoProfesor extends JpaRepository<Profesor, Long>{
    
}