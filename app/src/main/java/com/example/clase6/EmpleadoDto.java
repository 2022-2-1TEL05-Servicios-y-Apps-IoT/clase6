package com.example.clase6;

public class EmpleadoDto {
    private Empleado[] lista;
    private String estado;

    public Empleado[] getLista() {
        return lista;
    }

    public void setLista(Empleado[] lista) {
        this.lista = lista;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
