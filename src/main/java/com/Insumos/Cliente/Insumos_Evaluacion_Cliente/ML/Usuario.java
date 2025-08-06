package com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String username;
    private Rol rol;        
    private int status;

    // Constructor vacío (necesario para Spring/Jackson)
    public Usuario() {
    }

    // Getters y Setters para todas las propiedades
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Rol getRol() { // <-- ¡Getter para Rol!
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
