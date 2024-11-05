package gui;

import model.Administrador;
import model.Proyecto;
import service.ServiceProyecto;
import service.ServiceException;
import service.ServiceUsuario;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class FormularioProyecto extends JPanel {
    ServiceProyecto serviceProyecto;
    ServiceUsuario serviceUsuario;
    Administrador administrador;
    PanelManager panel;
    JPanel formularioProyecto;
    JLabel JLabelVacio;
    JButton JButtonVolverAtras;
    JLabel JLabelID;
    JTextField JTextFieldID;
    JLabel JLabelNombreProyecto;
    JTextField JTextFieldNombreProyecto;
    JButton JButtonCrear;
    JButton JButtonMostrar;
    JButton JButtonModificar;
    JButton JButtonEliminar;
    JButton JButtonBuscar;
    JLabel JLabelMensaje;

    public FormularioProyecto(PanelManager panel) {
        this.panel = panel;
        armarFormulario();
    }

    public void armarFormulario() {
        // Inicializo todos los elementos de la ventana
        serviceProyecto = new ServiceProyecto();
        serviceUsuario = new ServiceUsuario();
        administrador = new Administrador();
        formularioProyecto = new JPanel();
        formularioProyecto.setLayout(new GridLayout(5,2));
        JLabelVacio = new JLabel();
        JButtonVolverAtras = new JButton(" Volver atrás");
        JLabelID = new JLabel("ID");
        JTextFieldID = new JTextField(30);
        JLabelNombreProyecto = new JLabel("Nombre del proyecto");
        JTextFieldNombreProyecto = new JTextField(30);
        JButtonCrear = new JButton("Crear proyecto");
        JButtonModificar = new JButton("Modificar proyecto");
        JButtonEliminar = new JButton("Eliminar proyecto");
        JButtonMostrar = new JButton("Mostrar proyectos");
        JButtonBuscar = new JButton("Buscar proyecto");
        JLabelMensaje = new JLabel("");

        // Guardo el usuario actual
        try {
            administrador.setIdUsuario(serviceUsuario.obtenerID(panel.getUsuarioActual().getNombreUsuario()));
            administrador.setNombreUsuario(panel.getUsuarioActual().getNombreUsuario());
            administrador.setPermisos(serviceUsuario.obtenerPermisos(administrador.getIdUsuario()));
        }
        catch (ServiceException ex) {
            throw new RuntimeException(ex);
        }

        // Estilos
        formularioProyecto.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabelMensaje.setForeground(new Color(50, 191, 64));
        try {
            Image iconoVolverAtras = ImageIO.read(getClass().getResource("/iconos/volver_atras.png"));
            JButtonVolverAtras.setIcon(new ImageIcon(iconoVolverAtras.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Agrego elementos al panel
        formularioProyecto.add(JLabelVacio);
        formularioProyecto.add(JButtonVolverAtras);
        formularioProyecto.add(JLabelID);
        formularioProyecto.add(JTextFieldID);
        formularioProyecto.add(JLabelNombreProyecto);
        formularioProyecto.add(JTextFieldNombreProyecto);
        formularioProyecto.add(JButtonCrear);
        formularioProyecto.add(JButtonBuscar);
        formularioProyecto.add(JButtonModificar);
        formularioProyecto.add(JButtonEliminar);

        JButtonVolverAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.mostrar(panel.getMenuAdministrador());
                }
                catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo abrir el menú de administrador");
                }
            }
        });

        JButtonCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Proyecto proyecto = new Proyecto();
                proyecto.setAdministrador(administrador);

                if (!JTextFieldID.getText().isEmpty()) {
                    proyecto.setIdProyecto(Integer.parseInt(JTextFieldID.getText()));
                }
                else {
                    try {
                        proyecto.setIdProyecto(serviceProyecto.obtenerUltimoID() + 1);
                    }
                    catch (ServiceException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                if (!JTextFieldNombreProyecto.getText().isEmpty())
                    proyecto.setNombreProyecto(JTextFieldNombreProyecto.getText());

                try {
                    serviceProyecto.guardar(proyecto);
                    JLabelMensaje.setForeground(new Color(50, 191, 64));
                    JLabelMensaje.setText("Proyecto creado con éxito");
                }
                catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo crear");
                    s.printStackTrace();
                }
            }
        });

        JButtonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JTextFieldID.getText().isEmpty()) {
                    JLabelMensaje.setForeground(Color.RED);
                    JLabelMensaje.setText("Ingrese un ID");
                }
                try {
                    Proyecto proyecto = serviceProyecto.buscar(Integer.parseInt(JTextFieldID.getText()));
                    JTextFieldNombreProyecto.setText(proyecto.getNombreProyecto());
                }
                catch (ServiceException ex) {
                    JOptionPane.showMessageDialog(null,"No se pudo buscar");
                }
            }
        });
    /*
        JButtonModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JTextFieldID.getText().isEmpty()) {
                    JLabelMensaje.setForeground(Color.RED);
                    JLabelMensaje.setText("Ingrese un ID");
                    return;
                }
                try {
                    Incidencia i = new Incidencia();
                    i.setIdIncidencia(Integer.parseInt(JTextFieldID.getText()));
                    i.setDescripcion(JTextFieldNombreUsuario.getText());

                    if (!JTextFieldEstimacionHoras.getText().isEmpty())
                        i.setEstimacionHoras(Double.parseDouble(JTextFieldEstimacionHoras.getText()));

                    if (usuario.getPermisos().contains(2)) {
                        if (!JTextFieldEstado.getText().isEmpty())
                            i.setEstado(JTextFieldEstado.getText());
                    }
                    else if (!JTextFieldEstado.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No tienes permiso para realizar esta acción");
                        return;
                    }

                    if (usuario.getPermisos().contains(3)) {
                        if (!JTextFieldTiempoInvertido.getText().isEmpty())
                            i.setTiempoInvertido(Double.parseDouble(JTextFieldTiempoInvertido.getText()));
                    }
                    else if (!JTextFieldTiempoInvertido.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No tienes permiso para realizar esta acción");
                        return;
                    }

                    serviceAdministrador.modificar(i);

                    JLabelMensaje.setForeground(Color.GREEN);
                    JLabelMensaje.setText("Incidencia modificada con éxito");
                }
                catch (DAOException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo modificar");
                }
            }
        });

        JButtonMostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.mostrar(panel.getReporteIncidencias());
                }
                catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo abrir la pantalla");
                }
            }
        });

        JButtonCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.mostrar(panel.getInicioSesion());
                }
                catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo cerrar sesión");
                }
            }
        });

        JButtonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Incidencia incidencia = new Incidencia();
                if (!JTextFieldID.getText().isEmpty())
                    incidencia.setIdIncidencia(Integer.parseInt(JTextFieldID.getText()));
                else
                    JOptionPane.showMessageDialog(null,"Ingrese un ID");

                try {
                    if (usuario.getPermisos().contains(4)) {
                        serviceAdministrador.cerrar(incidencia);
                        JLabelMensaje.setForeground(Color.GREEN);
                        JLabelMensaje.setText("Incidencia cerrada con éxito");
                    }
                    else
                        JOptionPane.showMessageDialog(null, "No tienes permiso para realizar esta acción");
                }
                catch (DAOException ex) {
                    JOptionPane.showMessageDialog(null,"No se pudo cerrar sesión");
                }
            }
        });*/

        setLayout(new BorderLayout());
        add(formularioProyecto, BorderLayout.CENTER);
    }
}
