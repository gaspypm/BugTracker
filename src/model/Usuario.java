package model;

import java.util.ArrayList;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private ArrayList<Integer> permisos;

    public Usuario() {
        this.permisos = new ArrayList<>();
    }

    public Usuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.permisos = new ArrayList<>();
    }

    public Usuario(String nombreUsuario, ArrayList<Integer> permisos) {
        this.nombreUsuario = nombreUsuario;
        this.permisos = new ArrayList<>(permisos);
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public ArrayList<Integer> getPermisos() {
        return permisos;
    }

    public void setPermisos(ArrayList<Integer> permisos) {
        this.permisos = permisos;
    }
}
