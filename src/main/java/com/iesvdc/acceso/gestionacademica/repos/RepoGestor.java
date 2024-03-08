package com.iesvdc.acceso.gestionacademica.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iesvdc.acceso.gestionacademica.modelos.Gestor;

public interface RepoGestor extends JpaRepository<Gestor, Long>{
    
}
