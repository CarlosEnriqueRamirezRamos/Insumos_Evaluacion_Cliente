package com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String userName; // <-- ¡Asegúrate de que exista este campo!
    private Rol rol;        // <-- ¡Asegúrate de que exista este campo y su getter!
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

    public String getUserName() { // <-- ¡Getter para userName!
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
