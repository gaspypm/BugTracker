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

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }
}
