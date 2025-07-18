package com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML;

import java.util.Date;

public class TransaccionDTO {
    private int idTransaccion;
    private Date fechaRegistro;
    private String nombreUsuario;
    private String claveContrato;
    private String claveNodoRecepcion;
    private String descNodoRecepcion;
    private String claveNodoEntrega;
    private String descNodoEntrega;
    private String zonaInyeccion;
    private String zonaExtraccion;
    private Double gasExceso;
    private Double cargoUso;
    private Double cargoGasExceso;
    private Double facturaTotal;
    private Double nominadaRecepcion;
    private Double asignadaRecepcion;
    private Double nominadaEntrega;
    private Double asignadaEntrega;
    private Double excesoFirme;
    private Double usoInterrumpible;

    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getClaveContrato() {
        return claveContrato;
    }

    public void setClaveContrato(String claveContrato) {
        this.claveContrato = claveContrato;
    }

    public String getClaveNodoRecepcion() {
        return claveNodoRecepcion;
    }

    public void setClaveNodoRecepcion(String claveNodoRecepcion) {
        this.claveNodoRecepcion = claveNodoRecepcion;
    }

    public String getDescNodoRecepcion() {
        return descNodoRecepcion;
    }

    public void setDescNodoRecepcion(String descNodoRecepcion) {
        this.descNodoRecepcion = descNodoRecepcion;
    }

    public String getClaveNodoEntrega() {
        return claveNodoEntrega;
    }

    public void setClaveNodoEntrega(String claveNodoEntrega) {
        this.claveNodoEntrega = claveNodoEntrega;
    }

    public String getDescNodoEntrega() {
        return descNodoEntrega;
    }

    public void setDescNodoEntrega(String descNodoEntrega) {
        this.descNodoEntrega = descNodoEntrega;
    }

    public String getZonaInyeccion() {
        return zonaInyeccion;
    }

    public void setZonaInyeccion(String zonaInyeccion) {
        this.zonaInyeccion = zonaInyeccion;
    }

    public String getZonaExtraccion() {
        return zonaExtraccion;
    }

    public void setZonaExtraccion(String zonaExtraccion) {
        this.zonaExtraccion = zonaExtraccion;
    }

    public Double getGasExceso() {
        return gasExceso;
    }

    public void setGasExceso(Double gasExceso) {
        this.gasExceso = gasExceso;
    }

    public Double getCargoUso() {
        return cargoUso;
    }

    public void setCargoUso(Double cargoUso) {
        this.cargoUso = cargoUso;
    }

    public Double getCargoGasExceso() {
        return cargoGasExceso;
    }

    public void setCargoGasExceso(Double cargoGasExceso) {
        this.cargoGasExceso = cargoGasExceso;
    }

    public Double getFacturaTotal() {
        return facturaTotal;
    }

    public void setFacturaTotal(Double facturaTotal) {
        this.facturaTotal = facturaTotal;
    }

    public Double getNominadaRecepcion() {
        return nominadaRecepcion;
    }

    public void setNominadaRecepcion(Double nominadaRecepcion) {
        this.nominadaRecepcion = nominadaRecepcion;
    }

    public Double getAsignadaRecepcion() {
        return asignadaRecepcion;
    }

    public void setAsignadaRecepcion(Double asignadaRecepcion) {
        this.asignadaRecepcion = asignadaRecepcion;
    }

    public Double getNominadaEntrega() {
        return nominadaEntrega;
    }

    public void setNominadaEntrega(Double nominadaEntrega) {
        this.nominadaEntrega = nominadaEntrega;
    }

    public Double getAsignadaEntrega() {
        return asignadaEntrega;
    }

    public void setAsignadaEntrega(Double asignadaEntrega) {
        this.asignadaEntrega = asignadaEntrega;
    }

    public Double getExcesoFirme() {
        return excesoFirme;
    }

    public void setExcesoFirme(Double excesoFirme) {
        this.excesoFirme = excesoFirme;
    }

    public Double getUsoInterrumpible() {
        return usoInterrumpible;
    }

    public void setUsoInterrumpible(Double usoInterrumpible) {
        this.usoInterrumpible = usoInterrumpible;
    }

    
}

