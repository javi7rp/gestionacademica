package com.iesvdc.acceso.gestionacademica.modelos;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Gestor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 25)
    private String username;
    @Column(length = 100)
    private String password;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'GESTOR'")
    private String authority;
}