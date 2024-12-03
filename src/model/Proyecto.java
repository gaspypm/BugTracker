package model;

import java.util.ArrayList;

public class Proyecto {
    private int idProyecto;
    private String nombreProyecto;
    private Administrador administrador;
    private ArrayList<Incidencia> incidencias;

    public Proyecto() {
        this.nombreProyecto = "";
        this.incidencias = new ArrayList<>();
    }

    public Proyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
        this.incidencias = new ArrayList<>();
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public ArrayList<Incidencia> getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(ArrayList<Incidencia> incidencias) {
        this.incidencias = incidencias;
    }
}
