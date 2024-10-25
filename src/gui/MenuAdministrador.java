package gui;
import DAO.DAOException;
import model.Incidencia;
import model.Usuario;
import service.ServiceAdministrador;
import service.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuAdministrador extends JPanel {
    PanelManager panel;
    JPanel menuAdministrador;
    JButton JButtonCrearUsuario;
    JButton JButtonCrearProyecto;
    JButton JButtonCrearIncidencia;
    JButton JButtonMostrarIncidencias;
    JButton JButtonEditarPermisosUsuario;
    JButton JButtonCerrarSesion;

    public MenuAdministrador(PanelManager panel) {
        this.panel = panel;
        armarPanelAdministrador();
    }

    public void armarPanelAdministrador() {
        // Inicializo todos los elementos de la ventana
        menuAdministrador = new JPanel();
        menuAdministrador.setLayout(new GridLayout(3,2));
        JButtonCrearUsuario = new JButton("Crear usuario");
        JButtonCrearProyecto = new JButton("Crear proyecto");
        JButtonCrearIncidencia = new JButton("Crear incidencia");
        JButtonMostrarIncidencias = new JButton("Mostrar incidencias");
        JButtonEditarPermisosUsuario = new JButton("Editar permisos usuario");
        JButtonCerrarSesion = new JButton("Cerrar sesión");

        // Estilos
        menuAdministrador.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Agrego elementos al panel
        menuAdministrador.add(JButtonCrearUsuario);
        menuAdministrador.add(JButtonEditarPermisosUsuario);
        menuAdministrador.add(JButtonCrearIncidencia);
        menuAdministrador.add(JButtonMostrarIncidencias);
        menuAdministrador.add(JButtonCrearProyecto);
        menuAdministrador.add(JButtonCerrarSesion);


        JButtonCrearUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Usuario usuario = new Usuario();
            }
        });

        JButtonEditarPermisosUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        JButtonCrearIncidencia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.mostrar(panel.getFormularioIncidencias());
                } catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo abrir la pantalla");
                }
            }
        });

        JButtonMostrarIncidencias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.mostrar(panel.getReporteIncidencias());
                } catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo abrir la pantalla");
                }
            }
        });

        JButtonCrearProyecto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        JButtonCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.mostrar(panel.getInicioSesion());
                } catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo cerrar sesión");
                }
            }
        });

        setLayout(new BorderLayout());
        add(menuAdministrador, BorderLayout.CENTER);
    }
}
