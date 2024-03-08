package com.iesvdc.acceso.gestionacademica.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iesvdc.acceso.gestionacademica.modelos.Alumno;

public interface RepoAlumno extends JpaRepository<Alumno, Long>{
    
    @Query(value = "SELECT * FROM alumno WHERE username = :username", nativeQuery = true)
    Alumno findByUsername(@Param("username") String username);
}