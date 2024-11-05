package gui;

import model.Administrador;
import model.Usuario;
import service.ServiceAdministrador;
import service.ServiceException;
import service.ServiceUsuario;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class FormularioUsuario extends JPanel {
    ServiceAdministrador serviceAdministrador;
    ServiceUsuario serviceUsuario;
    Usuario usuario;
    PanelManager panel;
    JPanel formularioUsuario;
    JLabel JLabelVacio;
    JLabel JLabelVacio2;
    JButton JButtonVolverAtras;
    JLabel JLabelID;
    JTextField JTextFieldID;
    JLabel JLabelNombreUsuario;
    JTextField JTextFieldNombreUsuario;
    JLabel JLabelContrasena;
    JPasswordField JPasswordFieldContrasena;
    JLabel JLabelTipo;
    JComboBox<String> JComboBoxTipos;
    JButton JButtonCrear;
    JButton JButtonMostrar;
    JButton JButtonModificar;
    JButton JButtonEliminar;
    JButton JButtonBuscar;
    JLabel JLabelMensaje;

    public FormularioUsuario(PanelManager panel) {
        this.panel = panel;
        armarFormulario();
    }

    public void armarFormulario() {
        // Inicializo todos los elementos de la ventana
        serviceAdministrador = new ServiceAdministrador();
        serviceUsuario = new ServiceUsuario();
        usuario = new Usuario();
        formularioUsuario = new JPanel();
        formularioUsuario.setLayout(new GridLayout(8,2));
        JLabelVacio = new JLabel();
        JLabelVacio2 = new JLabel();
        JButtonVolverAtras = new JButton(" Volver atrás");
        JLabelID = new JLabel("ID");
        JTextFieldID = new JTextField(30);
        JLabelNombreUsuario = new JLabel("Nombre de usuario");
        JTextFieldNombreUsuario = new JTextField(30);
        JLabelContrasena = new JLabel("Contraseña");
        JPasswordFieldContrasena = new JPasswordField(30);
        JLabelTipo = new JLabel("Tipo");
        JComboBoxTipos = new JComboBox<>();
        JButtonCrear = new JButton("Crear usuario");
        JButtonModificar = new JButton("Modificar usuario");
        JButtonEliminar = new JButton("Eliminar usuario");
        JButtonMostrar = new JButton("Mostrar usuarios");
        JButtonBuscar = new JButton("Buscar usuario");
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

        // Agrego tipos de usuarios a JComboBoxTipos
        JComboBoxTipos.addItem("Regular");
        JComboBoxTipos.addItem("Administrador");

        // Estilos
        formularioUsuario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabelMensaje.setForeground(new Color(50, 191, 64));
        try {
            Image iconoVolverAtras = ImageIO.read(getClass().getResource("/iconos/volver_atras.png"));
            JButtonVolverAtras.setIcon(new ImageIcon(iconoVolverAtras.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Agrego elementos al panel
        formularioUsuario.add(JLabelVacio);
        formularioUsuario.add(JButtonVolverAtras);
        formularioUsuario.add(JLabelID);
        formularioUsuario.add(JTextFieldID);
        formularioUsuario.add(JLabelNombreUsuario);
        formularioUsuario.add(JTextFieldNombreUsuario);
        formularioUsuario.add(JLabelContrasena);
        formularioUsuario.add(JPasswordFieldContrasena);
        formularioUsuario.add(JLabelTipo);
        formularioUsuario.add(JComboBoxTipos);
        formularioUsuario.add(JButtonCrear);
        formularioUsuario.add(JButtonBuscar);
        formularioUsuario.add(JButtonModificar);
        formularioUsuario.add(JLabelVacio2);
        formularioUsuario.add(JLabelMensaje);

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
                String tipo = null;
                if (JComboBoxTipos.getSelectedItem().equals("Administrador")) {
                    Administrador usuario = new Administrador();
                    tipo = "ADMINISTRADOR";
                }
                else if (JComboBoxTipos.getSelectedItem().equals("Regular")) {
                    Usuario usuario = new Usuario();
                    tipo = "REGULAR";
                }
                else {
                    JOptionPane.showMessageDialog(null,"Ingrese el tipo de usuario");
                    return;
                }

                if (!JTextFieldID.getText().isEmpty()) {
                    usuario.setIdUsuario(Integer.parseInt(JTextFieldID.getText()));
                }
                else {
                    try {
                        usuario.setIdUsuario(serviceUsuario.obtenerUltimoID() + 1);
                    }
                    catch (ServiceException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                if (!JTextFieldNombreUsuario.getText().isEmpty())
                    usuario.setNombreUsuario(JTextFieldNombreUsuario.getText());

                try {
                    serviceAdministrador.crearUsuario(usuario.getIdUsuario(), usuario.getNombreUsuario(), JPasswordFieldContrasena.getPassword(), tipo);

                    JLabelMensaje.setForeground(new Color(50, 191, 64));
                    JLabelMensaje.setText("Usuario creado con éxito");
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
                    Usuario usuario = serviceUsuario.buscar(Integer.parseInt(JTextFieldID.getText()));
                    JTextFieldNombreUsuario.setText(usuario.getNombreUsuario());
                    if (serviceUsuario.validarTipo(usuario.getIdUsuario()).equals("ADMINISTRADOR")) {
                        JComboBoxTipos.setSelectedItem("Administrador");
                    }
                    else if (serviceUsuario.validarTipo(usuario.getIdUsuario()).equals("REGULAR")) {
                        JComboBoxTipos.setSelectedItem("Regular");
                    }
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

                    JLabelMensaje.setForeground(new Color(217, 9, 9));
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
        */
        setLayout(new BorderLayout());
        add(formularioUsuario, BorderLayout.CENTER);
    }
}
