package gui;

import model.Usuario;
import service.ServiceUsuario;
import service.ServiceException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InicioSesion extends JPanel {
    ServiceUsuario serviceUsuario;
    PanelManager panel;
    JPanel inicioSesion;
    JLabel JLabelUsuario;
    JTextField JTextFieldUsuario;
    JLabel JLabelContrasena;
    JPasswordField JPasswordFieldContrasena;
    JButton JButtonIniciarSesion;
    JButton JButtonInvitado;

    public InicioSesion(PanelManager panel) {
        this.panel = panel;
        iniciarSesion();
    }

    public void iniciarSesion() {
        // Inicializo todos los elementos de la ventana
        serviceUsuario = new ServiceUsuario();
        inicioSesion = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JLabelUsuario = new JLabel("Usuario");
        JTextFieldUsuario = new JTextField(20);
        JLabelContrasena = new JLabel("Contraseña");
        JPasswordFieldContrasena = new JPasswordField(20);
        JButtonIniciarSesion = new JButton("Iniciar sesión");
        JButtonInvitado = new JButton("Invitado");

        // Primera columna (Usuario y Contraseña)
        inicioSesion.add(JLabelUsuario, gbc);
        gbc.gridy = 1;
        inicioSesion.add(JTextFieldUsuario, gbc);
        gbc.gridy = 2;
        inicioSesion.add(JLabelContrasena, gbc);
        gbc.gridy = 3;
        inicioSesion.add(JPasswordFieldContrasena, gbc);
        gbc.gridy = 4;
        inicioSesion.add(JButtonIniciarSesion, gbc);

        // Estilos
        inicioSesion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Separador
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 10;
        gbc.fill = GridBagConstraints.VERTICAL;
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        inicioSesion.add(separator, gbc);

        // Segunda columna (Invitado)
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 5;
        gbc.fill = GridBagConstraints.NONE;
        inicioSesion.add(JButtonInvitado, gbc);

        // Botones
        JButtonInvitado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.getUsuarioActual().setNombreUsuario("Invitado");
                    panel.getUsuarioActual().setIdUsuario(0);
                    panel.getUsuarioActual().setTipo("REGULAR");
                    panel.mostrar(panel.getFormularioIncidencias());
                } catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo abrir la pantalla");
                }
            }
        });

        JButtonIniciarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Usuario usuario = new Usuario();
                usuario.setNombreUsuario(JTextFieldUsuario.getText());
                String contrasena = new String(JPasswordFieldContrasena.getPassword());

                try {
                    if (serviceUsuario.validarUsuario(usuario.getNombreUsuario(), contrasena)) {
                        panel.getUsuarioActual().setNombreUsuario(usuario.getNombreUsuario());
                        panel.getUsuarioActual().setIdUsuario(serviceUsuario.obtenerID(usuario.getNombreUsuario()));
                        usuario.setIdUsuario(serviceUsuario.obtenerID(usuario.getNombreUsuario()));

                        if (serviceUsuario.validarTipo(panel.getUsuarioActual().getIdUsuario()).equals("ADMINISTRADOR")) {
                            panel.getUsuarioActual().setTipo("ADMINISTRADOR");
                            panel.mostrar(panel.getMenuAdministrador());
                        }
                        else {
                            panel.getUsuarioActual().setTipo("REGULAR");
                            panel.mostrar(panel.getFormularioIncidencias());
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
                    }
                } catch (ServiceException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al acceder a la base de datos");
                }
            }
        });

        setLayout(new BorderLayout());
        add(inicioSesion, BorderLayout.CENTER);
    }
}
