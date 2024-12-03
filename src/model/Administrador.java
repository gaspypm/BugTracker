package model;

import java.util.ArrayList;

public class Administrador extends Usuario {
    private ArrayList<Proyecto> proyectos;

    public Administrador() {
        this.proyectos = new ArrayList<>();
    }

    public Administrador(String nombreUsuario) {
        super(nombreUsuario);
        this.proyectos = new ArrayList<>();
    }

    public void crearUsuario(String nombreUsuario, ArrayList<Integer> permisos) {
        Usuario usuario = new Usuario(nombreUsuario, permisos);
    }

    public void crearUsuario(String nombreUsuario) {
        crearUsuario(nombreUsuario);
    }

    public void crearProyecto(int idProyecto, String nombreProyecto) {
        Proyecto nuevoProyecto = new Proyecto(nombreProyecto);
        proyectos.add(nuevoProyecto);
    }
}
