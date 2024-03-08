package com.iesvdc.acceso.gestionacademica.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iesvdc.acceso.gestionacademica.modelos.Profesor;

public interface RepoProfesor extends JpaRepository<Profesor, Long>{
    
    @Query(value = "SELECT * FROM profesor WHERE username = :username", nativeQuery = true)
    Profesor findByUsername(@Param("username") String username);
}