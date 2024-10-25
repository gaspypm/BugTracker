package gui;

import DAO.DAOException;
import model.Usuario;
import service.ServiceUsuario;
import service.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class InicioSesion extends JPanel {
    ServiceUsuario serviceUsuario;
    PanelManager panel;
    JPanel inicioSesion;
    JLabel JLabelUsuario;
    JTextField JTextFieldUsuario;
    JLabel JLabelContrasena;
    JPasswordField JTextFieldContrasena;
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
        JTextFieldContrasena = new JPasswordField(20);
        JButtonIniciarSesion = new JButton("Iniciar sesión");
        JButtonInvitado = new JButton("Invitado");

        // Primera columna (Usuario y Contraseña)
        inicioSesion.add(JLabelUsuario, gbc);
        gbc.gridy = 1;
        inicioSesion.add(JTextFieldUsuario, gbc);
        gbc.gridy = 2;
        inicioSesion.add(JLabelContrasena, gbc);
        gbc.gridy = 3;
        inicioSesion.add(JTextFieldContrasena, gbc);
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

        JButtonInvitado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.getUsuarioActual().setNombreUsuario("Invitado");
                    panel.mostrar(panel.getFormularioIncidencias());
                } catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo abrir la pantalla");
                }
            }
        });

        JButtonIniciarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = JTextFieldUsuario.getText();
                String contrasena = new String(JTextFieldContrasena.getPassword());

                try {
                    if (serviceUsuario.validarUsuario(usuario, contrasena)) {
                        panel.getUsuarioActual().setNombreUsuario(usuario);
                        panel.getUsuarioActual().setIdUsuario(serviceUsuario.obtenerID(usuario));

                        if (Objects.equals(serviceUsuario.validarTipo(usuario), "ADMINISTRADOR")) {
                            panel.mostrar(panel.getMenuAdministrador());
                        }
                        else {
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
