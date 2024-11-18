package model;

import java.util.ArrayList;

public class Incidencia {
    private int idIncidencia;
    private String descripcion;
    private Usuario usuarioResponsable;
    private Proyecto proyecto;
    private String estado;
    private double estimacionHoras;
    private double tiempoInvertido;
    private ArrayList<Movimiento> movimientos;

    public Incidencia() {
        this.idIncidencia = 0;
        this.descripcion = "";
        this.estado = "Nuevo";
        this.estimacionHoras = 0;
        this.movimientos = new ArrayList<>();
    }

    public Incidencia(String descripcion, Usuario usuarioResponsable, double estimacionHoras) {
        this.idIncidencia = 0;
        this.descripcion = descripcion;
        this.usuarioResponsable = usuarioResponsable;
        this.estado = "Nuevo";
        this.estimacionHoras = estimacionHoras;
        this.movimientos = new ArrayList<>();
    }

    public Incidencia(String descripcion, double estimacionHoras, String estado, double tiempoInvertido) {
        this.idIncidencia = 0;
        this.descripcion = descripcion;
        this.estado = estado;
        this.estimacionHoras = estimacionHoras;
        this.tiempoInvertido = tiempoInvertido;
        this.movimientos = new ArrayList<>();
    }

    public void reportar(Usuario usuario, String descripcion, double estimacionHoras, Proyecto proyecto) {
        this.descripcion = descripcion;
        this.usuarioResponsable = usuario;
        this.estimacionHoras = estimacionHoras;
        this.proyecto = proyecto;
        this.getMovimientos().add(new Movimiento(usuario));
    }

    public void actualizarEstado(Usuario usuario, String nuevoEstado) {
        this.usuarioResponsable = usuario;
        this.getMovimientos().add(new Movimiento(this.estado, nuevoEstado, usuario));
    }

    public void cerrar(){
        this.estado = "Cerrado";
    }

    public int getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(int idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public ArrayList<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(ArrayList<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public double getEstimacionHoras() {
        return estimacionHoras;
    }

    public void setEstimacionHoras(double estimacionHoras) {
        this.estimacionHoras = estimacionHoras;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTiempoInvertido() {
        return tiempoInvertido;
    }

    public void setTiempoInvertido(double tiempoInvertido) {
        this.tiempoInvertido = tiempoInvertido;
    }

    public void setUsuarioResponsable(Usuario usuarioResponsable) {
        this.usuarioResponsable = usuarioResponsable;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
}
