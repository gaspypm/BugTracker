package model;

import java.util.Date;

public class Movimiento {
    private int idMovimiento;
    private Incidencia incidencia;
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

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(String estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public String getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(String estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public Date getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Incidencia getIncidencia() {
        return incidencia;
    }

    public void setIncidencia(Incidencia incidencia) {
        this.incidencia = incidencia;
    }
}
