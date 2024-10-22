package model;

import java.util.Date;

public class Movimiento {
    private int idMovimiento;
    private String estadoAnterior;
    private String estadoNuevo;
    private Date fechaCambio;
    private Usuario usuario;

    public Movimiento() {
        this.estadoAnterior = "";
        this.estadoNuevo = "";
        this.fechaCambio = new Date();
        this.usuario = null;
    }

    public Movimiento(Usuario usuario) {
        this.estadoNuevo = "Nuevo";
        this.fechaCambio = new Date();
        this.usuario = usuario;
    }

    public Movimiento(String estadoAnterior, String estadoNuevo, Usuario usuario) {
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = new Date();
        this.usuario = usuario;
    }
}
