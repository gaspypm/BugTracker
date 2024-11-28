package gui;

import model.Usuario;
import service.ServiceException;

import javax.swing.*;
import java.awt.*;

public class PanelManager {
    private InicioSesion inicioSesion;
    private MenuAdministrador menuAdministrador;
    private FormularioIncidencia formularioIncidencia;
    private ReporteIncidencias reporteIncidencias;
    private EditorPermisos editorPermisos;
    private FormularioUsuario formularioUsuario;
    private FormularioProyecto formularioProyecto;
    private ReporteMovimientos reporteMovimientos;
    private ReporteIncidenciasPorProyecto reporteIncidenciasPorProyecto;
    private Usuario usuarioActual = new Usuario();

    JFrame jFrame;

    public PanelManager(int tipo) throws ServiceException {
        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        switch(tipo) {
            case 1:
                inicioSesion = new InicioSesion(this);
                mostrar(inicioSesion);
                break;
            case 2:
                menuAdministrador = new MenuAdministrador(this);
                mostrar(menuAdministrador);
                break;
            case 3:
                formularioIncidencia = new FormularioIncidencia(this);
                mostrar(formularioIncidencia);
                break;
            case 4:
                reporteIncidencias = new ReporteIncidencias(this);
                mostrar(reporteIncidencias);
                break;
            case 5:
                editorPermisos = new EditorPermisos(this);
                mostrar(editorPermisos);
                break;
            case 6:
                formularioUsuario = new FormularioUsuario(this);
                mostrar(formularioUsuario);
                break;
            case 7:
                formularioProyecto = new FormularioProyecto(this);
                mostrar(formularioProyecto);
                break;
            case 8:
                reporteMovimientos = new ReporteMovimientos(this);
                mostrar(reporteMovimientos);
                break;
            case 9:
                reporteIncidenciasPorProyecto = new ReporteIncidenciasPorProyecto(this);
                mostrar(reporteIncidenciasPorProyecto);
                break;
            default:
                inicioSesion = new InicioSesion(this);
                mostrar(inicioSesion);
                break;
        }
    }

    public void mostrar(JPanel panel) {
        jFrame.getContentPane().removeAll();
        jFrame.getContentPane().add(BorderLayout.SOUTH, panel);
        jFrame.getContentPane().validate();
        jFrame.getContentPane().repaint();
        jFrame.show();
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
    }

    public InicioSesion getInicioSesion() throws  ServiceException {
        return inicioSesion = new InicioSesion(this);
    }

    public MenuAdministrador getMenuAdministrador() throws ServiceException {
        return menuAdministrador = new MenuAdministrador(this);
    }

    public ReporteIncidencias getReporteIncidencias() throws ServiceException {
        return reporteIncidencias = new ReporteIncidencias(this);
    }

    public FormularioIncidencia getFormularioIncidencias() throws ServiceException {
        return formularioIncidencia = new FormularioIncidencia(this);
    }

    public EditorPermisos getEditorPermisos() throws ServiceException {
        return editorPermisos = new EditorPermisos(this);
    }

    public FormularioUsuario getFormularioUsuario() throws ServiceException {
        return formularioUsuario = new FormularioUsuario(this);
    }

    public FormularioProyecto getFormularioProyecto() throws ServiceException {
        return formularioProyecto = new FormularioProyecto(this);
    }

    public ReporteMovimientos getReporteMovimientos() throws ServiceException {
        return reporteMovimientos = new ReporteMovimientos(this);
    }

    public ReporteIncidenciasPorProyecto getReporteProyectos() throws ServiceException {
        return reporteIncidenciasPorProyecto = new ReporteIncidenciasPorProyecto(this);
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }
}
