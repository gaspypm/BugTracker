package gui;

import service.ServiceException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MenuAdministrador extends JPanel {
    PanelManager panel;
    JPanel menuAdministrador;
    JButton JButtonCrearUsuario;
    JButton JButtonCrearProyecto;
    JButton JButtonCrearIncidencia;
    JButton JButtonMostrarIncidencias;
    JButton JButtonEditarPermisosUsuario;
    JButton JButtonReporteIncidenciasPorProyecto;
    JButton JButtonHistorialMovimientos;
    JButton JButtonReporteProyectos;
    JButton JButtonCerrarSesion;

    public MenuAdministrador(PanelManager panel) {
        this.panel = panel;
        armarPanelAdministrador();
    }

    public void armarPanelAdministrador() {
        // Inicializo todos los elementos de la ventana
        menuAdministrador = new JPanel();
        menuAdministrador.setLayout(new GridLayout(5,2));
        JButtonCrearUsuario = new JButton("Crear usuario");
        JButtonCrearProyecto = new JButton("Crear proyecto");
        JButtonCrearIncidencia = new JButton("Crear incidencia");
        JButtonMostrarIncidencias = new JButton("Mostrar incidencias");
        JButtonEditarPermisosUsuario = new JButton("Editar permisos usuario");
        JButtonReporteIncidenciasPorProyecto = new JButton("Mostrar incidencias por proyecto");
        JButtonHistorialMovimientos = new JButton("Historial movimientos");
        JButtonReporteProyectos = new JButton("Reporte proyectos");
        JButtonCerrarSesion = new JButton("Cerrar sesión");

        // Estilos
        menuAdministrador.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        try {
            Image iconoCerrarSesion = ImageIO.read(getClass().getResource("/iconos/cerrar_sesion.png"));
            JButtonCerrarSesion.setIcon(new ImageIcon(iconoCerrarSesion.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Agrego elementos al panel
        menuAdministrador.add(JButtonCrearUsuario);
        menuAdministrador.add(JButtonEditarPermisosUsuario);
        menuAdministrador.add(JButtonCrearIncidencia);
        menuAdministrador.add(JButtonMostrarIncidencias);
        menuAdministrador.add(JButtonCrearProyecto);
        menuAdministrador.add(JButtonReporteIncidenciasPorProyecto);
        menuAdministrador.add(JButtonHistorialMovimientos);
        menuAdministrador.add(JButtonReporteProyectos);
        menuAdministrador.add(JButtonCerrarSesion);

        // Botones
        JButtonCrearUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.mostrar(panel.getFormularioUsuario());
                } catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo abrir la pantalla");
                }
            }
        });

        JButtonEditarPermisosUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.mostrar(panel.getEditorPermisos());
                } catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo abrir la pantalla");
                }
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
                try {
                    panel.mostrar(panel.getFormularioProyecto());
                } catch (ServiceException s) {
                    s.printStackTrace();
                    JOptionPane.showMessageDialog(null,"No se pudo abrir la pantalla");
                }
            }
        });

        JButtonReporteIncidenciasPorProyecto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.mostrar(panel.getReporteIncidenciasPorProyecto());
                } catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo abrir la pantalla");
                }
            }
        });

        JButtonHistorialMovimientos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.mostrar(panel.getReporteMovimientos());
                } catch (ServiceException s) {
                    s.printStackTrace();
                    JOptionPane.showMessageDialog(null,"No se pudo abrir la pantalla");
                }
            }
        });

        JButtonReporteProyectos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.mostrar(panel.getReporteProyectos());
                } catch (ServiceException s) {
                    s.printStackTrace();
                    JOptionPane.showMessageDialog(null,"No se pudo abrir la pantalla");
                }
            }
        });

        JButtonCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] opciones = {"No", "Sí"};

                int confirmacion = JOptionPane.showOptionDialog(
                        null,
                        "¿Está seguro que desea cerrar sesión?",
                        "Confirmación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.CANCEL_OPTION,
                        null,
                        opciones,
                        opciones[0]);
                if (confirmacion != JOptionPane.YES_OPTION) {
                    try {
                        panel.mostrar(panel.getInicioSesion());
                    } catch (ServiceException s) {
                        JOptionPane.showMessageDialog(null,"No se pudo cerrar sesión");
                    }
                }
            }
        });

        setLayout(new BorderLayout());
        add(menuAdministrador, BorderLayout.CENTER);
    }
}
