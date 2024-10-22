package model;

import java.util.ArrayList;

public class Incidencia {
    private int idIncidencia;
    private String descripcion;
    private Usuario usuario;
    private Proyecto proyecto;
    private String estado;
    private double estimacionHoras;
    private double tiempoInvertido;
    private ArrayList<Movimiento> movimientos;

    // Constructor por defecto sin model.Usuario ni model.Proyecto
    public Incidencia() {
        this.descripcion = "";
        this.estado = "Nuevo";
        this.estimacionHoras = 0;
        this.movimientos = new ArrayList<>();
    }

    // Constructor para crear incidencia desde model.Proyecto
    public Incidencia(String descripcion, Usuario usuario, double estimacionHoras) {
        this.descripcion = descripcion;
        this.usuario = usuario;
        this.estado = "Nuevo";
        this.estimacionHoras = estimacionHoras;
        this.movimientos = new ArrayList<>();
    }

    // Constructor con estado de incidencia diferente a "Nuevo"
    public Incidencia(String descripcion, double estimacionHoras, String estado) {
        this.descripcion = descripcion;
        this.estado = estado;
        this.estimacionHoras = estimacionHoras;
        this.movimientos = new ArrayList<>();
    }

    // Constructor con tiempo ya invertido
    public Incidencia(String descripcion, double estimacionHoras, String estado, double tiempoInvertido) {
        this.descripcion = descripcion;
        this.estado = estado;
        this.estimacionHoras = estimacionHoras;
        this.tiempoInvertido = tiempoInvertido;
        this.movimientos = new ArrayList<>();
    }

    public void reportar(Usuario usuario, String descripcion, double estimacionHoras, Proyecto proyecto) {
        this.descripcion = descripcion;
        this.usuario = usuario;
        this.estimacionHoras = estimacionHoras;
        this.proyecto = proyecto;
        this.getMovimientos().add(new Movimiento(usuario));
    }

    public void actualizarEstado(Usuario usuario, String nuevoEstado) {
        this.usuario = usuario;
        this.getMovimientos().add(new Movimiento(this.estado, nuevoEstado, usuario));
    }

    public void agregarTiempoInvertido(double tiempoInvertido) {
        this.tiempoInvertido += tiempoInvertido;    // Botón con +/- para agregar horas
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

    public Usuario getUsuario() {
        return usuario;
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
}
