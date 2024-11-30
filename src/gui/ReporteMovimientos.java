package gui;

import DAO.DAOException;
import model.Movimiento;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import service.ServiceException;
import service.ServiceMovimiento;
import service.ServiceIncidencia;

public class ReporteMovimientos extends JPanel {
    private ServiceIncidencia serviceIncidencia;
    private ServiceMovimiento serviceMovimiento;
    private PanelManager panelManager;
    private JPanel reporteMovimientos;
    private JTable JTable;
    private DefaultTableModel contenido;
    private JScrollPane scrollPane;
    private JButton JButtonVolverAtras;
    private JLabel JLabelDescripcion;
    private boolean mostrarTodos;

    public ReporteMovimientos(PanelManager panelManager) {
        this.panelManager = panelManager;
        this.serviceIncidencia = new ServiceIncidencia();
        this.serviceMovimiento = new ServiceMovimiento();
        armarTablaReporte();
        mostrarOpciones();
    }

    private void armarTablaReporte() {
        setLayout(new BorderLayout());
        reporteMovimientos = new JPanel(new BorderLayout());
        contenido = new DefaultTableModel();
        JTable = new JTable(contenido);
        scrollPane = new JScrollPane(JTable);

        JButtonVolverAtras = new JButton("Volver atrás");
        JLabelDescripcion = new JLabel("");

        contenido.addColumn("ID Movimiento");
        contenido.addColumn("Estado Anterior");
        contenido.addColumn("Estado Nuevo");
        contenido.addColumn("Usuario");
        contenido.addColumn("Fecha");

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(JButtonVolverAtras);
        headerPanel.add(JLabelDescripcion);

        reporteMovimientos.add(headerPanel, BorderLayout.NORTH);
        add(reporteMovimientos, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Estilo del botón "Volver atrás"
        try {
            Image iconoVolverAtras = ImageIO.read(getClass().getResource("/iconos/volver_atras.png"));
            JButtonVolverAtras.setIcon(new ImageIcon(iconoVolverAtras.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Acción del botón "Volver atrás"
        JButtonVolverAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panelManager.mostrar(panelManager.getMenuAdministrador());
                } catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo abrir el menú de administrador");
                }
            }
        });
    }

    private void mostrarOpciones() {
        Object[] opciones = {"Mostrar todos", "Buscar", "Cancelar"};

        JPanel panel = new JPanel();
        panel.add(new JLabel("Ingrese el ID de incidencia"));
        JTextField textField = new JTextField(10);
        panel.add(textField);

        int result = JOptionPane.showOptionDialog(
                null, panel, "Buscar movimientos",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opciones, null
        );

        try {
            if (result == 0) { // Mostrar todos
                ArrayList<Movimiento> movimientos = serviceMovimiento.buscarTodos();
                mostrarMovimientos(movimientos);
            } else if (result == 1) { // Buscar por ID
                String idTexto = textField.getText().trim();
                if (!idTexto.isEmpty()) {
                    int idIncidencia = Integer.parseInt(idTexto);
                    ArrayList<Movimiento> movimientos = serviceMovimiento.buscarPorIncidencia(idIncidencia);
                    if (movimientos.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No se encontraron movimientos para el ID: " + idIncidencia);
                    } else {
                        mostrarMovimientos(movimientos);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID válido.");
                }
            } else if (result == 2 || result == JOptionPane.CLOSED_OPTION) { // Cancelar
                JOptionPane.showMessageDialog(null, "Operación cancelada.");
            }
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    private void mostrarMovimientos(ArrayList<Movimiento> movimientos) {
        contenido.setRowCount(0);
        for (Movimiento movimiento : movimientos) {
            Object[] fila = new Object[5];
            fila[0] = movimiento.getIdMovimiento();
            fila[1] = movimiento.getEstadoAnterior();
            fila[2] = movimiento.getEstadoNuevo();
            fila[3] = movimiento.getUsuario().getNombreUsuario();
            fila[4] = movimiento.getFechaCambio();
            contenido.addRow(fila);
        }
    }

    private void volverAlMenu() {
        try {
            panelManager.mostrar(panelManager.getMenuAdministrador());
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo volver al menú: " + ex.getMessage());
        }
    }
}
