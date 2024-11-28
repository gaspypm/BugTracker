package gui;

import model.Usuario;
import service.ServiceAdministrador;
import service.ServiceException;
import service.ServiceUsuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EditorPermisos extends JPanel {
    ServiceUsuario serviceUsuario;
    ServiceAdministrador serviceAdministrador;
    PanelManager panel;
    Usuario usuarioSeleccionado;
    JPanel editorPermisos;
    JLabel JLabelUsuario;
    JComboBox<String> JComboBoxUsuarios;
    JLabel JLabelReportar;
    JCheckBox JCheckBoxReportar;
    JLabel JLabelCambiarEstado;
    JCheckBox JCheckBoxCambiarEstado;
    JLabel JLabelIndicarTiempoInvertido;
    JCheckBox JCheckBoxIndicarTiempoInvertido;
    JLabel JLabelCerrar;
    JCheckBox JCheckBoxCerrar;
    JButton JButtonMostrarPermisos;
    JButton JButtonActualizarPermisos;
    JButton JButtonVolverAtras;

    public EditorPermisos(PanelManager panel) {
        this.panel = panel;
        armarPanelPermisos();
        cargarListaUsuarios();
    }

    public void armarPanelPermisos() {
        // Inicializo todos los elementos de la ventana
        serviceUsuario = new ServiceUsuario();
        serviceAdministrador = new ServiceAdministrador();
        editorPermisos = new JPanel();
        editorPermisos.setLayout(new GridLayout(7,2));
        JLabelUsuario = new JLabel("Usuario");
        JComboBoxUsuarios = new JComboBox<>();
        JLabelReportar = new JLabel("Reportar incidencia");
        JCheckBoxReportar = new JCheckBox();
        JLabelCambiarEstado = new JLabel("Cambiar estado incidencia");
        JCheckBoxCambiarEstado = new JCheckBox();
        JLabelIndicarTiempoInvertido = new JLabel("Indicar tiempo invertido");
        JCheckBoxIndicarTiempoInvertido = new JCheckBox();
        JLabelCerrar = new JLabel("Cerrar incidencia");
        JCheckBoxCerrar = new JCheckBox();
        JButtonMostrarPermisos = new JButton("Mostrar permisos");
        JButtonActualizarPermisos = new JButton("Actualizar permisos");
        JButtonVolverAtras = new JButton("Volver atrás");

        // Estilos
        editorPermisos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Agrego elementos al panel
        editorPermisos.add(JLabelUsuario);
        editorPermisos.add(JComboBoxUsuarios);
        editorPermisos.add(JLabelReportar);
        editorPermisos.add(JCheckBoxReportar);
        editorPermisos.add(JLabelCambiarEstado);
        editorPermisos.add(JCheckBoxCambiarEstado);
        editorPermisos.add(JLabelIndicarTiempoInvertido);
        editorPermisos.add(JCheckBoxIndicarTiempoInvertido);
        editorPermisos.add(JLabelCerrar);
        editorPermisos.add(JCheckBoxCerrar);
        editorPermisos.add(JButtonMostrarPermisos);
        editorPermisos.add(JButtonActualizarPermisos);
        editorPermisos.add(JButtonVolverAtras);

        // Botones
        JButtonMostrarPermisos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreUsuario = (String)JComboBoxUsuarios.getSelectedItem();

                try {
                    int idUsuario = serviceUsuario.obtenerID(nombreUsuario);
                    ArrayList<Integer> permisos = serviceUsuario.obtenerPermisos(idUsuario);

                    JCheckBoxReportar.setSelected(permisos.contains(1));
                    JCheckBoxCambiarEstado.setSelected(permisos.contains(2));
                    JCheckBoxIndicarTiempoInvertido.setSelected(permisos.contains(3));
                    JCheckBoxCerrar.setSelected(permisos.contains(4));
                }
                catch (ServiceException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JButtonActualizarPermisos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreUsuario = (String)JComboBoxUsuarios.getSelectedItem();

                try {
                    int idUsuario = serviceUsuario.obtenerID(nombreUsuario);
                    ArrayList<Integer> permisosSeleccionados = new ArrayList<>();

                    if (JCheckBoxReportar.isSelected())
                        permisosSeleccionados.add(1);
                    if (JCheckBoxCambiarEstado.isSelected())
                        permisosSeleccionados.add(2);
                    if (JCheckBoxIndicarTiempoInvertido.isSelected())
                        permisosSeleccionados.add(3);
                    if (JCheckBoxCerrar.isSelected())
                        permisosSeleccionados.add(4);

                    serviceAdministrador.editarPermisos(idUsuario, permisosSeleccionados);

                }
                catch (ServiceException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });

        JButtonVolverAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.mostrar(panel.getMenuAdministrador());
                }
                catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo abrir la pantalla");
                }
            }
        });

        setLayout(new BorderLayout());
        add(editorPermisos, BorderLayout.CENTER);
    }

    public void cargarListaUsuarios() {
        try {
            ArrayList<Usuario> usuarios = serviceUsuario.buscarTodos();
            for (Usuario usuario : usuarios) {
                JComboBoxUsuarios.addItem(usuario.getNombreUsuario());
            }
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }
}
