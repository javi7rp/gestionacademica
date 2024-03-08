package com.iesvdc.acceso.gestionacademica.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


import com.iesvdc.acceso.gestionacademica.modelos.Asignatura;
import com.iesvdc.acceso.gestionacademica.modelos.Profesor;

public interface RepoAsignatura extends JpaRepository<Asignatura, Long>{
    @Transactional
    void deleteByProfesor(Profesor profesor);
}
