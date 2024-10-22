package gui;
import DAO.DAOException;
import model.Incidencia;
import service.ServiceIncidencia;
import service.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormularioIncidencia extends JPanel {
    ServiceIncidencia serviceIncidencia;
    PanelManager panel;
    JPanel formularioIncidencia;
    JLabel JLabelID;
    JTextField JTextFieldID;
    JLabel JLabeldescripcion;
    JTextField JTextFieldDescripcion;
    JLabel JLabelEstimacionHoras;
    JTextField JTextFieldEstimacionHoras;
    JLabel JLabelEstado;
    JTextField JTextFieldEstado;
    JButton JButtonGrabar;
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
        formularioIncidencia = new JPanel();
        formularioIncidencia.setLayout(new GridLayout(7,2));
        JLabelID = new JLabel("ID");
        JTextFieldID = new JTextField(40);
        JLabeldescripcion = new JLabel("Descripción");
        JTextFieldDescripcion = new JTextField(40);
        JLabelEstimacionHoras = new JLabel("Estimación de horas");
        JTextFieldEstimacionHoras = new JTextField(20);
        JLabelEstado = new JLabel("Estado");
        JTextFieldEstado = new JTextField(40);
        JButtonGrabar = new JButton("Grabar");
        JButtonModificar = new JButton("Modificar");
        JButtonMostrarIncidencias = new JButton("Mostrar incidencias");
        JButtonBuscar = new JButton("Buscar");
        JLabelMensaje = new JLabel("");

        // Estilos
        JLabelMensaje.setForeground(Color.GREEN);

        // Agrego elementos al panel
        formularioIncidencia.add(JLabelID);
        formularioIncidencia.add(JTextFieldID);
        formularioIncidencia.add(JLabeldescripcion);
        formularioIncidencia.add(JTextFieldDescripcion);
        formularioIncidencia.add(JLabelEstimacionHoras);
        formularioIncidencia.add(JTextFieldEstimacionHoras);
        formularioIncidencia.add(JLabelEstado);
        formularioIncidencia.add(JTextFieldEstado);
        formularioIncidencia.add(JButtonGrabar);
        formularioIncidencia.add(JButtonBuscar);
        formularioIncidencia.add(JButtonModificar);
        formularioIncidencia.add(JButtonMostrarIncidencias);
        formularioIncidencia.add(JLabelMensaje);

        JButtonGrabar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Incidencia incidencia = new Incidencia();
                if(!JTextFieldID.getText().isEmpty()) {
                    incidencia.setIdIncidencia(Integer.parseInt(JTextFieldID.getText()));
                }
                incidencia.setDescripcion(JTextFieldDescripcion.getText());
                incidencia.setEstimacionHoras(Integer.parseInt(JTextFieldEstimacionHoras.getText()));
                if(!JTextFieldEstado.getText().isEmpty()) {
                    incidencia.setEstado(JTextFieldEstado.getText());
                }

                try {
                    serviceIncidencia.guardar(incidencia);
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
                if(JTextFieldID.getText().isEmpty()) {
                    JLabelMensaje.setForeground(Color.RED);
                    JLabelMensaje.setText("Ingrese un ID");
                }
                try {
                    Incidencia i = serviceIncidencia.buscar(Integer.parseInt(JTextFieldID.getText()));
                    JTextFieldDescripcion.setText(i.getDescripcion());
                    JTextFieldEstimacionHoras.setText(String.valueOf(i.getEstimacionHoras()));
                    JTextFieldEstado.setText(i.getEstado());
                }
                catch (DAOException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo guardar");
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

        setLayout(new BorderLayout());
        add(formularioIncidencia, BorderLayout.CENTER);
    }
}
