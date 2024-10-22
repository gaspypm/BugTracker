package model;

import java.util.ArrayList;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private ArrayList<String> permisos;

    public Usuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Usuario(String nombreUsuario, ArrayList<String> permisos) {
        this.nombreUsuario = nombreUsuario;
        this.permisos = new ArrayList<>(permisos);
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
}
