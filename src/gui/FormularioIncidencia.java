package gui;
import DAO.DAOException;
import model.Incidencia;
import model.Usuario;
import service.ServiceIncidencia;
import service.ServiceException;
import service.ServiceUsuario;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class FormularioIncidencia extends JPanel {
    ServiceIncidencia serviceIncidencia;
    ServiceUsuario serviceUsuario;
    PanelManager panel;
    JPanel formularioIncidencia;
    JLabel JLabelVacio;
    JButton JButtonCerrarSesion;
    JLabel JLabelID;
    JTextField JTextFieldID;
    JLabel JLabelDescripcion;
    JTextField JTextFieldDescripcion;
    JLabel JLabelEstimacionHoras;
    JTextField JTextFieldEstimacionHoras;
    JLabel JLabelEstado;
    JTextField JTextFieldEstado;
    JLabel JLabelTiempoInvertido;
    JTextField JTextFieldTiempoInvertido;
    JButton JButtonReportar;
    JButton JButtonMostrarIncidencias;
    JButton JButtonModificar;
    JButton JButtonBuscar;
    JLabel JLabelMensaje;

    public FormularioIncidencia(PanelManager panel) {
        this.panel = panel;
        armarFormulario();
    }

    public void armarFormulario() {
        // Inicializo todos los elementos de la ventana
        serviceIncidencia = new ServiceIncidencia();
        serviceUsuario = new ServiceUsuario();
        formularioIncidencia = new JPanel();
        formularioIncidencia.setLayout(new GridLayout(9,2));
        JLabelVacio = new JLabel("Bienvenido, " + panel.getUsuarioActual().getNombreUsuario());
        JButtonCerrarSesion = new JButton(" Cerrar sesión");
        JLabelID = new JLabel("ID");
        JTextFieldID = new JTextField(30);
        JLabelDescripcion = new JLabel("Descripción");
        JTextFieldDescripcion = new JTextField(30);
        JLabelEstimacionHoras = new JLabel("Estimación de horas");
        JTextFieldEstimacionHoras = new JTextField(30);
        JLabelEstado = new JLabel("Estado");
        JTextFieldEstado = new JTextField(30);
        JLabelTiempoInvertido = new JLabel("Tiempo Invertido");
        JTextFieldTiempoInvertido = new JTextField();
        JButtonReportar = new JButton("Reportar incidencia");
        JButtonModificar = new JButton("Modificar incidencia");
        JButtonMostrarIncidencias = new JButton("Mostrar incidencias");
        JButtonBuscar = new JButton("Buscar incidencia");
        JLabelMensaje = new JLabel("");

        // Estilos
        formularioIncidencia.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabelMensaje.setForeground(Color.GREEN);
        try {
            Image iconoCerrarSesion = ImageIO.read(getClass().getResource("/iconos/cerrar_sesion.png"));
            JButtonCerrarSesion.setIcon(new ImageIcon(iconoCerrarSesion.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Agrego elementos al panel
        formularioIncidencia.add(JLabelVacio);
        formularioIncidencia.add(JButtonCerrarSesion);
        formularioIncidencia.add(JLabelID);
        formularioIncidencia.add(JTextFieldID);
        formularioIncidencia.add(JLabelDescripcion);
        formularioIncidencia.add(JTextFieldDescripcion);
        formularioIncidencia.add(JLabelEstimacionHoras);
        formularioIncidencia.add(JTextFieldEstimacionHoras);
        formularioIncidencia.add(JLabelEstado);
        formularioIncidencia.add(JTextFieldEstado);
        formularioIncidencia.add(JLabelTiempoInvertido);
        formularioIncidencia.add(JTextFieldTiempoInvertido);
        formularioIncidencia.add(JButtonReportar);
        formularioIncidencia.add(JButtonBuscar);
        formularioIncidencia.add(JButtonModificar);
        formularioIncidencia.add(JButtonMostrarIncidencias);
        formularioIncidencia.add(JLabelMensaje);

        JButtonReportar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Incidencia incidencia = new Incidencia();
                Usuario usuario = new Usuario();

                if (!JTextFieldID.getText().isEmpty()) {
                    incidencia.setIdIncidencia(Integer.parseInt(JTextFieldID.getText()));
                } else {
                    try {
                        incidencia.setIdIncidencia(serviceIncidencia.obtenerUltimoID() + 1);
                    } catch (DAOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if (!JTextFieldDescripcion.getText().isEmpty())
                    incidencia.setDescripcion(JTextFieldDescripcion.getText());
                if (!JTextFieldEstimacionHoras.getText().isEmpty())
                    incidencia.setEstimacionHoras(Double.parseDouble(JTextFieldEstimacionHoras.getText()));
                if (!JTextFieldEstado.getText().isEmpty())
                    incidencia.setEstado(JTextFieldEstado.getText());
                if (!JTextFieldTiempoInvertido.getText().isEmpty())
                    incidencia.setTiempoInvertido(Double.parseDouble(JTextFieldTiempoInvertido.getText()));


                try {
                    usuario.setIdUsuario(serviceUsuario.obtenerID(panel.getUsuarioActual().getNombreUsuario()));
                    usuario.setNombreUsuario(panel.getUsuarioActual().getNombreUsuario());
                    incidencia.setUsuario(usuario);
                } catch (ServiceException ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    serviceIncidencia.guardar(incidencia);
                    JLabelMensaje.setForeground(Color.GREEN);
                    JLabelMensaje.setText("Incidencia guardada con éxito");
                } catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo guardar");
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
                    Incidencia i = serviceIncidencia.buscar(Integer.parseInt(JTextFieldID.getText()));
                    JTextFieldDescripcion.setText(i.getDescripcion());
                    JTextFieldEstimacionHoras.setText(String.valueOf(i.getEstimacionHoras()));
                    JTextFieldEstado.setText(i.getEstado());
                } catch (DAOException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo guardar");
                }
            }
        });

        JButtonModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JTextFieldID.getText().isEmpty()) {
                    JLabelMensaje.setForeground(Color.RED);
                    JLabelMensaje.setText("Ingrese un ID");
                }
                try {
                    Incidencia i = new Incidencia();
                    i.setIdIncidencia(Integer.parseInt(JTextFieldID.getText()));
                    i.setDescripcion(JTextFieldDescripcion.getText());
                    i.setEstimacionHoras(Double.parseDouble(JTextFieldEstimacionHoras.getText()));
                    i.setEstado(JTextFieldEstado.getText());

                    serviceIncidencia.modificar(i);

                    JLabelMensaje.setForeground(Color.GREEN);
                    JLabelMensaje.setText("Incidencia modificada con éxito");
                } catch (DAOException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo modificar");
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
        add(formularioIncidencia, BorderLayout.CENTER);
    }
}
