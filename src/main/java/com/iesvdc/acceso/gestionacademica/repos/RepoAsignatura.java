package com.iesvdc.acceso.gestionacademica.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iesvdc.acceso.gestionacademica.modelos.Asignatura;

public interface RepoAsignatura extends JpaRepository<Asignatura, Long>{
    
}
