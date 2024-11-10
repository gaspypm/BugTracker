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
        formularioUsuario.setLayout(new GridLayout(7,2));
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
        try {
            Image iconoVolverAtras = ImageIO.read(getClass().getResource("/iconos/volver_atras.png"));
            JButtonVolverAtras.setIcon(new ImageIcon(iconoVolverAtras.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Agrego elementos al panel
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
        formularioUsuario.add(JButtonVolverAtras);
        formularioUsuario.add(JLabelMensaje);

        // Botones
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
                    JLabelMensaje.setForeground(new Color(217, 9, 9));
                    JLabelMensaje.setText("Ingrese un ID");
                }
                try {
                    Usuario usuario = serviceUsuario.buscar(Integer.parseInt(JTextFieldID.getText()));
                    if(usuario != null)
                        JTextFieldNombreUsuario.setText(usuario.getNombreUsuario());
                    else {
                        JLabelMensaje.setForeground(new Color(217, 9, 9));
                        JLabelMensaje.setText("El usuario no fue encontrado");
                    }
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

        JButtonModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Esta función no está disponible");
            }
        });

        setLayout(new BorderLayout());
        add(formularioUsuario, BorderLayout.CENTER);
    }
}