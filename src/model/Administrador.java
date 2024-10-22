package model;

import java.util.ArrayList;

public class Administrador extends Usuario {
    private ArrayList<Proyecto> proyectos;

    public Administrador(String nombreUsuario) {
        super(nombreUsuario);
    }

    public void crearUsuario(String nombreUsuario, ArrayList<String> permisos) {
        Usuario usuario = new Usuario(nombreUsuario, permisos);
    }

    public void crearUsuario(String nombreUsuario) {
        crearUsuario(nombreUsuario);
    }

    public void crearProyecto(int idProyecto, String nombreProyecto) {
        Proyecto nuevoProyecto = new Proyecto(nombreProyecto);
        proyectos.add(nuevoProyecto);
    }

    public void obtenerReporte() {

    }
}
