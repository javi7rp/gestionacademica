package com.iesvdc.acceso.gestionacademica.repos;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;

import com.iesvdc.acceso.gestionacademica.modelos.Rol;
//import com.iesvdc.acceso.zapateria.modelos.Usuario;

public interface RepoRol extends JpaRepository<Rol, Long>{

    //@Query("SELECT usuario.* from usuario, usuario_roles, rol  where  usuario_roles.roles_id=rol.id AND rol.nombre = ?1")
    //List<Usuario> findUsersByRol(Rol rol);
    
}