package model;

import java.util.ArrayList;

public class Proyecto {
    private int idProyecto;
    private String nombreProyecto;
    private ArrayList<Incidencia> incidencias;

    public Proyecto() {
        this.nombreProyecto = "";
        this.incidencias = new ArrayList<>();
    }

    public Proyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
        this.incidencias = new ArrayList<>();
    }

    public void agregarIncidencia(int idIncidencia, String descripcionIncidencia, Usuario usuario, double estimacionHoras) {
        this.incidencias.add(new Incidencia(descripcionIncidencia, usuario, estimacionHoras));
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }
}
