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
    Usuario usuario;
    PanelManager panel;
    JPanel formularioIncidencia;
    JLabel JLabelVacio;
    JLabel JLabelBienvenida;
    JButton JButtonCerrarSesion;
    JLabel JLabelID;
    JTextField JTextFieldID;
    JLabel JLabelDescripcion;
    JTextField JTextFieldDescripcion;
    JLabel JLabelEstimacionHoras;
    JTextField JTextFieldEstimacionHoras;
    JLabel JLabelEstado;
    JComboBox<String> JComboBoxEstados;
    JLabel JLabelTiempoInvertido;
    JTextField JTextFieldTiempoInvertido;
    JButton JButtonReportar;
    JButton JButtonMostrarIncidencias;
    JButton JButtonModificar;
    JButton JButtonCerrar;
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
        usuario = new Usuario();
        formularioIncidencia = new JPanel();
        formularioIncidencia.setLayout(new GridLayout(10,2));
        JLabelBienvenida = new JLabel("Bienvenido, " + panel.getUsuarioActual().getNombreUsuario());
        JButtonCerrarSesion = new JButton(" Cerrar sesión");
        JLabelID = new JLabel("ID");
        JTextFieldID = new JTextField(30);
        JLabelDescripcion = new JLabel("Descripción");
        JTextFieldDescripcion = new JTextField(30);
        JLabelEstimacionHoras = new JLabel("Estimación de horas");
        JTextFieldEstimacionHoras = new JTextField(30);
        JLabelEstado = new JLabel("Estado");
        JComboBoxEstados = new JComboBox<>();
        JLabelTiempoInvertido = new JLabel("Tiempo Invertido");
        JTextFieldTiempoInvertido = new JTextField();
        JButtonReportar = new JButton("Reportar incidencia");
        JButtonModificar = new JButton("Modificar incidencia");
        JButtonCerrar = new JButton("Cerrar incidencia");
        JButtonMostrarIncidencias = new JButton("Mostrar incidencias");
        JButtonBuscar = new JButton("Buscar incidencia");
        JLabelVacio = new JLabel();
        JLabelMensaje = new JLabel("");

        // Guardo el usuario actual
        try {
            usuario.setIdUsuario(serviceUsuario.obtenerID(panel.getUsuarioActual().getNombreUsuario()));
            usuario.setNombreUsuario(panel.getUsuarioActual().getNombreUsuario());
            usuario.setPermisos(serviceUsuario.obtenerPermisos(usuario.getIdUsuario()));
        }
        catch (ServiceException ex) {
            throw new RuntimeException(ex);
        }

        // Estilos
        formularioIncidencia.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabelMensaje.setForeground(new Color(50, 191, 64));
        try {
            Image iconoCerrarSesion = ImageIO.read(getClass().getResource("/iconos/cerrar_sesion.png"));
            JButtonCerrarSesion.setIcon(new ImageIcon(iconoCerrarSesion.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Agrego elementos al panel
        formularioIncidencia.add(JLabelBienvenida);
        formularioIncidencia.add(JButtonCerrarSesion);
        formularioIncidencia.add(JLabelID);
        formularioIncidencia.add(JTextFieldID);
        formularioIncidencia.add(JLabelDescripcion);
        formularioIncidencia.add(JTextFieldDescripcion);
        formularioIncidencia.add(JLabelEstimacionHoras);
        formularioIncidencia.add(JTextFieldEstimacionHoras);
        formularioIncidencia.add(JLabelEstado);
        formularioIncidencia.add(JComboBoxEstados);
        formularioIncidencia.add(JLabelTiempoInvertido);
        formularioIncidencia.add(JTextFieldTiempoInvertido);
        formularioIncidencia.add(JButtonReportar);
        formularioIncidencia.add(JButtonBuscar);
        formularioIncidencia.add(JButtonModificar);
        formularioIncidencia.add(JButtonCerrar);
        formularioIncidencia.add(JButtonMostrarIncidencias);
        formularioIncidencia.add(JLabelVacio);
        formularioIncidencia.add(JLabelMensaje);

        // Agrego estados de incidencia a JComboBoxEstados
        JComboBoxEstados.addItem("");
        JComboBoxEstados.addItem("Nuevo");
        JComboBoxEstados.addItem("Asignado");
        JComboBoxEstados.addItem("En progreso");
        JComboBoxEstados.addItem("Resuelto");
        JComboBoxEstados.addItem("Verificado");
        JComboBoxEstados.addItem("Cerrado");
        JComboBoxEstados.addItem("Pospuesto");
        JComboBoxEstados.addItem("Rechazado");

        JButtonReportar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Incidencia incidencia = new Incidencia();

                if (!usuario.getPermisos().contains(1)) {
                    JOptionPane.showMessageDialog(null, "No tienes permiso para realizar esta acción");
                    return;
                }

                if (!JTextFieldID.getText().isEmpty()) {
                    incidencia.setIdIncidencia(Integer.parseInt(JTextFieldID.getText()));
                }
                else {
                    try {
                        incidencia.setIdIncidencia(serviceIncidencia.obtenerUltimoID() + 1);
                    }
                    catch (DAOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if (!JTextFieldDescripcion.getText().isEmpty())
                    incidencia.setDescripcion(JTextFieldDescripcion.getText());
                if (!JTextFieldEstimacionHoras.getText().isEmpty())
                    incidencia.setEstimacionHoras(Double.parseDouble(JTextFieldEstimacionHoras.getText()));
                if (!JComboBoxEstados.getSelectedItem().equals(""))
                    incidencia.setEstado(JComboBoxEstados.getSelectedItem().toString());
                if (!JTextFieldTiempoInvertido.getText().isEmpty())
                    incidencia.setTiempoInvertido(Double.parseDouble(JTextFieldTiempoInvertido.getText()));

                incidencia.setUsuario(usuario);
                try {
                    serviceIncidencia.guardar(incidencia);
                    JLabelMensaje.setForeground(new Color(50, 191, 64));
                    JLabelMensaje.setText("Incidencia guardada con éxito");
                }
                catch (ServiceException s) {
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
                    JComboBoxEstados.setSelectedItem(i.getEstado());
                    JTextFieldTiempoInvertido.setText(String.valueOf(i.getTiempoInvertido()));
                }
                catch (DAOException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo guardar");
                }
            }
        });

        JButtonModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JTextFieldID.getText().isEmpty()) {
                    JLabelMensaje.setForeground(new Color(217, 9, 9));
                    JLabelMensaje.setText("Ingrese un ID");
                    return;
                }
                try {
                    Incidencia i = new Incidencia();
                    i.setIdIncidencia(Integer.parseInt(JTextFieldID.getText()));
                    i.setDescripcion(JTextFieldDescripcion.getText());

                    if (!JTextFieldEstimacionHoras.getText().isEmpty())
                        i.setEstimacionHoras(Double.parseDouble(JTextFieldEstimacionHoras.getText()));

                    if (!JComboBoxEstados.getSelectedItem().equals("")) {
                        if (usuario.getPermisos().contains(2)) {
                            i.setEstado(JComboBoxEstados.getSelectedItem().toString());
                        } else if (!usuario.getPermisos().contains(2)) {
                            JOptionPane.showMessageDialog(null, "No tienes permiso para modificar el estado");
                            return;
                        }
                    }

                    if (usuario.getPermisos().contains(3)) {
                        if (!JTextFieldTiempoInvertido.getText().isEmpty())
                            i.setTiempoInvertido(Double.parseDouble(JTextFieldTiempoInvertido.getText()));
                    }
                    else if (!JTextFieldTiempoInvertido.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No tienes permiso para modificar el tiempo invertido");
                        return;
                    }

                    serviceIncidencia.modificar(i);

                    JLabelMensaje.setForeground(new Color(50, 191, 64));
                    JLabelMensaje.setText("Incidencia modificada con éxito");
                }
                catch (DAOException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo modificar");
                }
            }
        });

        JButtonMostrarIncidencias.addActionListener(new ActionListener() {
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

        JButtonCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Incidencia incidencia = new Incidencia();
                if (!JTextFieldID.getText().isEmpty())
                    incidencia.setIdIncidencia(Integer.parseInt(JTextFieldID.getText()));
                else
                    JOptionPane.showMessageDialog(null,"Ingrese un ID");

                try {
                    if (usuario.getPermisos().contains(4)) {
                        serviceIncidencia.cerrar(incidencia);
                        JLabelMensaje.setForeground(new Color(50, 191, 64));
                        JLabelMensaje.setText("Incidencia cerrada con éxito");
                    }
                    else
                        JOptionPane.showMessageDialog(null, "No tienes permiso para realizar esta acción");
                }
                catch (DAOException ex) {
                    JOptionPane.showMessageDialog(null,"No se pudo cerrar sesión");
                }
            }
        });

        setLayout(new BorderLayout());
        add(formularioIncidencia, BorderLayout.CENTER);
    }
}
