package gui;

import DAO.DAOException;
import model.Incidencia;
import model.Proyecto;
import model.Usuario;
import service.ServiceIncidencia;
import service.ServiceException;
import service.ServiceProyecto;
import service.ServiceUsuario;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class FormularioIncidencia extends JPanel {
    ServiceIncidencia serviceIncidencia;
    ServiceUsuario serviceUsuario;
    ServiceProyecto serviceProyecto;
    Usuario usuario;
    PanelManager panel;

    JPanel formularioIncidencia;
    JLabel JLabelBienvenida;
    JButton JButtonSalir;
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
    JLabel JLabelProyecto;
    JComboBox<String> JComboBoxProyectos;
    JButton JButtonReportar;
    JButton JButtonMostrarIncidencias;
    JButton JButtonModificar;
    JButton JButtonCerrar;
    JButton JButtonBuscar;
    JButton JButtonVaciar;
    JLabel JLabelMensaje;

    public FormularioIncidencia(PanelManager panel) throws ServiceException {
        this.panel = panel;
        armarFormulario();
    }

    public void armarFormulario() throws ServiceException {
        // Inicializo todos los elementos de la ventana
        serviceIncidencia = new ServiceIncidencia();
        serviceUsuario = new ServiceUsuario();
        serviceProyecto = new ServiceProyecto();
        usuario = new Usuario();
        formularioIncidencia = new JPanel();
        formularioIncidencia.setLayout(new GridLayout(11,2));
        JLabelBienvenida = new JLabel("Bienvenido, " + panel.getUsuarioActual().getNombreUsuario());
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
        JLabelProyecto = new JLabel("Proyecto");
        JComboBoxProyectos = new JComboBox<>();
        JButtonReportar = new JButton("Reportar incidencia");
        JButtonModificar = new JButton("Modificar incidencia");
        JButtonCerrar = new JButton("Cerrar incidencia");
        JButtonMostrarIncidencias = new JButton("Mostrar incidencias");
        JButtonBuscar = new JButton("Buscar incidencia");
        JButtonVaciar = new JButton("Vaciar");
        JLabelMensaje = new JLabel("");

        // Guardo el usuario actual
        try {
            usuario.setIdUsuario(serviceUsuario.obtenerID(panel.getUsuarioActual().getNombreUsuario()));
            usuario.setNombreUsuario(panel.getUsuarioActual().getNombreUsuario());
            usuario.setPermisos(serviceUsuario.obtenerPermisos(usuario.getIdUsuario()));
            usuario.setTipo(panel.getUsuarioActual().getTipo());
        } catch (ServiceException ex) {
            throw new RuntimeException(ex);
        }

        // Estilos
        formularioIncidencia.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if(usuario.getTipo().equals("ADMINISTRADOR")) {
            JButtonSalir = new JButton("Volver atrás");
            try {
                Image iconoVolverAtras = ImageIO.read(getClass().getResource("/iconos/volver_atras.png"));
                JButtonSalir.setIcon(new ImageIcon(iconoVolverAtras.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            JButtonSalir = new JButton("Cerrar sesión");
            try {
                Image iconoCerrarSesion = ImageIO.read(getClass().getResource("/iconos/cerrar_sesion.png"));
                JButtonSalir.setIcon(new ImageIcon(iconoCerrarSesion.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        cargarListaProyectos();

        // Agrego elementos al panel
        formularioIncidencia.add(JLabelBienvenida);
        formularioIncidencia.add(JButtonSalir);
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
        formularioIncidencia.add(JLabelProyecto);
        formularioIncidencia.add(JComboBoxProyectos);
        formularioIncidencia.add(JButtonReportar);
        formularioIncidencia.add(JButtonBuscar);
        formularioIncidencia.add(JButtonModificar);
        formularioIncidencia.add(JButtonCerrar);
        formularioIncidencia.add(JButtonMostrarIncidencias);
        formularioIncidencia.add(JButtonVaciar);
        formularioIncidencia.add(JLabelMensaje);

        // Agrego estados de incidencia a JComboBoxEstados
        JComboBoxEstados.addItem("Nuevo");
        JComboBoxEstados.addItem("Asignado");
        JComboBoxEstados.addItem("En progreso");
        JComboBoxEstados.addItem("Resuelto");
        JComboBoxEstados.addItem("Verificado");
        JComboBoxEstados.addItem("Pospuesto");
        JComboBoxEstados.addItem("Rechazado");

        // Botones
        JButtonReportar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Incidencia incidencia = new Incidencia();
                Proyecto proyecto = new Proyecto();
                String nombreProyecto = (String) JComboBoxProyectos.getSelectedItem();


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
                    } catch (DAOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if (!JTextFieldDescripcion.getText().isEmpty())
                    incidencia.setDescripcion(JTextFieldDescripcion.getText());
                if (!JTextFieldEstimacionHoras.getText().isEmpty())
                    incidencia.setEstimacionHoras(Double.parseDouble(JTextFieldEstimacionHoras.getText()));
                incidencia.setEstado(JComboBoxEstados.getSelectedItem().toString());
                if (!JTextFieldTiempoInvertido.getText().isEmpty())
                    incidencia.setTiempoInvertido(Double.parseDouble(JTextFieldTiempoInvertido.getText()));
                incidencia.setUsuarioResponsable(usuario);
                if (nombreProyecto != null) {
                    try {
                        int idProyecto = serviceProyecto.obtenerID(nombreProyecto);
                        proyecto.setIdProyecto(idProyecto);
                        incidencia.setProyecto(proyecto);
                    } catch (ServiceException ex) {
                        JOptionPane.showMessageDialog(null, "Error al obtener el proyecto seleccionado");
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un proyecto");
                    return;
                }

                try {
                    serviceIncidencia.guardar(incidencia);
                    JLabelMensaje.setForeground(new Color(50, 191, 64));
                    JLabelMensaje.setText("Incidencia reportada con éxito");
                } catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo guardar");
                }
            }
        });

        JButtonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JTextFieldID.getText().isEmpty()) {
                    JLabelMensaje.setForeground(new Color(217, 9, 9));
                    JLabelMensaje.setText("Ingrese un ID");
                    return;
                }
                try {
                    Incidencia incidencia = serviceIncidencia.buscar(Integer.parseInt(JTextFieldID.getText()));
                    if(incidencia != null) {
                        JTextFieldDescripcion.setText(incidencia.getDescripcion());
                        JTextFieldEstimacionHoras.setText(String.valueOf(incidencia.getEstimacionHoras()));
                        JComboBoxEstados.setSelectedItem(incidencia.getEstado());
                        JTextFieldTiempoInvertido.setText(String.valueOf(incidencia.getTiempoInvertido()));
                    }
                    else {
                        JLabelMensaje.setForeground(new Color(217, 9, 9));
                        JLabelMensaje.setText("La incidencia no fue encontrada");
                    }
                } catch (DAOException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo guardar");
                }
            }
        });

        JButtonModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Incidencia incidencia;
                Object[] opciones = {"Sí", "No"};

                if (panel.getUsuarioActual().getNombreUsuario().equals("Invitado")) {
                    JOptionPane.showMessageDialog(null, "No tienes permiso para realizar esta acción");
                    return;
                }

                if (!JTextFieldID.getText().isEmpty()) {
                    try {
                        incidencia = serviceIncidencia.buscar(Integer.parseInt(JTextFieldID.getText()));
                    } catch (DAOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JLabelMensaje.setForeground(new Color(217, 9, 9));
                    JLabelMensaje.setText("Ingrese un ID");
                    return;
                }

                int confirmacion = JOptionPane.showOptionDialog(
                        null,
                        "ID: " + incidencia.getIdIncidencia() +
                                "\nDescripción:" +
                                "\n  Anterior: " + incidencia.getDescripcion() +
                                "\n  Actual: " + (!JTextFieldDescripcion.getText().isEmpty() ? JTextFieldDescripcion.getText() : incidencia.getDescripcion()) +
                                "\nEstimación de horas:" +
                                "\n  Anterior: " + incidencia.getEstimacionHoras() +
                                "\n  Actual: " + (!JTextFieldEstimacionHoras.getText().isEmpty() ? JTextFieldEstimacionHoras.getText() : incidencia.getEstimacionHoras()) +
                                "\nEstado:" +
                                "\n  Anterior: " + incidencia.getEstado() +
                                "\n  Actual: " + (!JComboBoxEstados.getSelectedItem().toString().isEmpty() && !JComboBoxEstados.getSelectedItem().equals(incidencia.getEstado()) ? JComboBoxEstados.getSelectedItem() : incidencia.getEstado()) +
                                "\nTiempo invertido:" +
                                "\n  Anterior: " + incidencia.getTiempoInvertido() +
                                "\n  Actual: " + (!JTextFieldTiempoInvertido.getText().isEmpty() ? JTextFieldTiempoInvertido.getText() : incidencia.getTiempoInvertido()) +
                                "\n\n¿Está seguro que desea modificar la incidencia?",
                        "Confirmar cambios",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.CANCEL_OPTION,
                        null,
                        opciones,
                        opciones[0]);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    try {
                        if (!JTextFieldDescripcion.getText().isEmpty())
                            incidencia.setDescripcion(JTextFieldDescripcion.getText());

                        if (!JTextFieldEstimacionHoras.getText().isEmpty())
                            incidencia.setEstimacionHoras(Double.parseDouble(JTextFieldEstimacionHoras.getText()));

                        if (!JComboBoxEstados.getSelectedItem().equals(incidencia.getEstado())) {
                            if (usuario.getPermisos().contains(2)) {
                                incidencia.setEstado(JComboBoxEstados.getSelectedItem().toString());
                            } else {
                                JOptionPane.showMessageDialog(null, "No tienes permiso para modificar el estado");
                                return;
                            }
                        }

                        if (!JTextFieldTiempoInvertido.getText().isEmpty() && !JTextFieldTiempoInvertido.getText().equals(String.valueOf(incidencia.getTiempoInvertido()))) {
                            if (usuario.getPermisos().contains(3)) {
                                incidencia.setTiempoInvertido(Double.parseDouble(JTextFieldTiempoInvertido.getText()));
                            } else {
                                JOptionPane.showMessageDialog(null, "No tienes permiso para modificar el tiempo invertido");
                                return;
                            }
                        }

                        incidencia.setUsuarioResponsable(panel.getUsuarioActual());

                        serviceIncidencia.modificar(incidencia);

                        JLabelMensaje.setForeground(new Color(50, 191, 64));
                        JLabelMensaje.setText("Incidencia modificada con éxito");
                    } catch (DAOException s) {
                        JOptionPane.showMessageDialog(null, "No se pudo modificar");
                    }
                }
                else {
                    JLabelMensaje.setForeground(new Color(217, 9, 9));
                    JLabelMensaje.setText("Operación cancelada");
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

        JButtonCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Incidencia incidencia = new Incidencia();
                Object[] opciones = {"Sí", "No"};

                if (panel.getUsuarioActual().getNombreUsuario().equals("Invitado")) {
                    JOptionPane.showMessageDialog(null, "No tienes permiso para realizar esta acción");
                    return;
                }

                if (!JTextFieldID.getText().isEmpty()) {
                    try {
                        incidencia = serviceIncidencia.buscar(Integer.parseInt(JTextFieldID.getText()));
                    } catch (DAOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese un ID");
                    return;
                }

                int confirmacion = JOptionPane.showOptionDialog(
                        null,
                        "ID: " + incidencia.getIdIncidencia() +
                                "\nDescripción: " + incidencia.getDescripcion() +
                                "\nEstimación de horas: " + incidencia.getEstimacionHoras() +
                                "\nEstado: " + incidencia.getEstado() +
                                "\nTiempo invertido: " + incidencia.getTiempoInvertido() +
                                "\n\n¿Está seguro que desea cerrar la incidencia?",
                        "Confirmación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.CANCEL_OPTION,
                        null,
                        opciones,
                        opciones[0]);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    try {
                        if (usuario.getPermisos().contains(4)) {
                            serviceIncidencia.cerrar(incidencia);
                            JLabelMensaje.setForeground(new Color(50, 191, 64));
                            JLabelMensaje.setText("Incidencia cerrada con éxito");
                        } else {
                            JOptionPane.showMessageDialog(null, "No tienes permiso para realizar esta acción");
                        }
                    } catch (DAOException ex) {
                        JOptionPane.showMessageDialog(null, "No se pudo cerrar la incidencia");
                    }
                } else {
                    JLabelMensaje.setForeground(new Color(217, 9, 9));
                    JLabelMensaje.setText("Operación cancelada");
                }
            }
        });

        JButtonVaciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextFieldID.setText("");
                JTextFieldDescripcion.setText("");
                JTextFieldEstimacionHoras.setText("");
                JTextFieldTiempoInvertido.setText("");
                JLabelMensaje.setText("");
            }
        });

        JButtonSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] opciones = {"No", "Sí"};

                if(usuario.getTipo().equals("ADMINISTRADOR")) {
                    try {
                        panel.mostrar(panel.getMenuAdministrador());
                        return;
                    } catch (ServiceException s) {
                        JOptionPane.showMessageDialog(null,"No se pudo volver atrás");
                    }
                }

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
        add(formularioIncidencia, BorderLayout.CENTER);
    }

    private void cargarListaProyectos() {
        try {
            ArrayList<Proyecto> proyectos = serviceProyecto.buscarTodos();
            for (Proyecto proyecto : proyectos) {
                JComboBoxProyectos.addItem(proyecto.getNombreProyecto());
            }
        } catch (ServiceException e) {
            throw new RuntimeException("Error al cargar proyectos");
        }
    }
}
