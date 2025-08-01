/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML;

public class Rol {

    private int idRol;
    private String rol; // <-- ¡Asegúrate de que exista este campo y su getter!

    // Constructor vacío
    public Rol() {
    }

    // Getters y Setters
    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getRol() { // <-- ¡Getter para rol!
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

