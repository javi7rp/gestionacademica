package com.iesvdc.acceso.gestionacademica.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iesvdc.acceso.gestionacademica.modelos.Usuario;

public interface RepoUsuario extends JpaRepository<Usuario, Long>{

    @Query(value = "SELECT * FROM usuario WHERE alumno_id = :alumnoId", nativeQuery = true)
    Usuario findByAlumnoId(@Param("alumnoId") Long alumnoId);

    void deleteByAlumnoId(Long AlumnoId);

    @Query(value = "SELECT * FROM usuario WHERE profesor_id = :profesorId", nativeQuery = true)
    Usuario findByProfesorId(@Param("profesorId") Long profesorId);

    void deleteByProfesorId(Long ProfesorId);
    
}
