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
        this.movimientos.add(new Movimiento(this.estado, nuevoEstado, usuario));
        this.estado = nuevoEstado;
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

    public int getIDEstado(String estado) {
        switch(estado) {
            case "Nuevo": return 1;
            case "Asignado": return 2;
            case "En Progreso": return 3;
            case "Resuelto": return 4;
            case "Verificado": return 5;
            case "Cerrado": return 6;
            case "Reabierto": return 7;
            case "Pospuesto": return 8;
            case "Rechazado": return 9;
            default:
                return 1;
        }
    }

    public String getEstadoID(int id_estado) {
        switch(id_estado) {
            case 1: return "Nuevo";
            case 2: return "Asignado";
            case 3: return "En Progreso";
            case 4: return "Resuelto";
            case 5: return "Verificado";
            case 6: return "Cerrado";
            case 7: return "Reabierto";
            case 8: return "Pospuesto";
            case 9: return "Rechazado";
            default:
                return "Nuevo";
        }
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
