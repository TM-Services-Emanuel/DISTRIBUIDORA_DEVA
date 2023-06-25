package com.example.latinodistribuidora.Modelos;

import java.io.Serializable;

public class Productos implements Serializable {
    public int idproducto;
    public String cod_interno;
    public String cod_barra;
    public String descripcion;
    public String precioCosto;
    public String precio;
    public int idunidad;
    public String unidad;
    public int iddivision;
    public String division;
    public int idiva;
    public String iva;
    public int impuesto;
    public String mayorista;
    public int cant_mayorista;
    public int precio_minorista;
    public int precio_supermercado;

    /*public Productos(int idproducto, String cod_interno, String cod_barra, String descripcion, String precioCosto, String precio, int idunidad, String unidad, int iddivision, String division, int idiva, String iva) {
        this.idproducto = idproducto;
        this.cod_interno = cod_interno;
        this.cod_barra = cod_barra;
        this.descripcion = descripcion;
        this.precioCosto = precioCosto;
        this.precio = precio;
        this.idunidad = idunidad;
        this.unidad = unidad;
        this.iddivision = iddivision;
        this.division = division;
        this.idiva = idiva;
        this.iva = iva;
    }*/

    public Productos(int idproducto, String cod_interno, String cod_barra,String descripcion, String precioCosto,String precio, String unidad, String division,String iva, int impuesto) {
        this.idproducto = idproducto;
        this.cod_interno = cod_interno;
        this.cod_barra = cod_barra;
        this.descripcion = descripcion;
        this.precioCosto = precioCosto;
        this.precio = precio;
        this.unidad = unidad;
        this.division = division;
        this.iva = iva;
        this.impuesto = impuesto;

    }

    public Productos(int idproducto, String cod_interno, String cod_barra,String descripcion, String precioCosto,String precio, String unidad, String division,String iva, int impuesto, String mayorista, int cant_mayorista, int precio_minorista, int precio_supermercado) {
        this.idproducto = idproducto;
        this.cod_interno = cod_interno;
        this.cod_barra = cod_barra;
        this.descripcion = descripcion;
        this.precioCosto = precioCosto;
        this.precio = precio;
        this.unidad = unidad;
        this.division = division;
        this.iva = iva;
        this.impuesto = impuesto;
        this.mayorista = mayorista;
        this.cant_mayorista = cant_mayorista;
        this.precio_minorista = precio_minorista;
        this.precio_supermercado = precio_supermercado;
    }

    public Productos(){}

    public int getIdproducto() {return idproducto;}

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public String getCod_interno() {
        return cod_interno;
    }

    public void setCod_interno(String cod_interno) {
        this.cod_interno = cod_interno;
    }

    public String getCod_barra() {
        return cod_barra;
    }

    public void setCod_barra(String cod_barra) {
        this.cod_barra = cod_barra;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getIdunidad() {
        return idunidad;
    }

    public void setIdunidad(int idunidad) {
        this.idunidad = idunidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public int getIddivision() {
        return iddivision;
    }

    public void setIddivision(int iddivision) {
        this.iddivision = iddivision;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getIdiva() {
        return idiva;
    }

    public void setIdiva(int idiva) {
        this.idiva = idiva;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public int getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(int impuesto) {
        this.impuesto = impuesto;
    }

    public String getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(String precioCosto) {
        this.precioCosto = precioCosto;
    }

    public String getMayorista() {
        return mayorista;
    }

    public void setMayorista(String mayorista) {
        this.mayorista = mayorista;
    }

    public int getCant_mayorista() {
        return cant_mayorista;
    }

    public void setCant_mayorista(int cant_mayorista) {
        this.cant_mayorista = cant_mayorista;
    }

    public int getPrecio_minorista() {
        return precio_minorista;
    }

    public void setPrecio_minorista(int precio_minorista) {
        this.precio_minorista = precio_minorista;
    }

    public int getPrecio_supermercado() {
        return precio_supermercado;
    }

    public void setPrecio_supermercado(int precio_supermercado) {
        this.precio_supermercado = precio_supermercado;
    }
}
