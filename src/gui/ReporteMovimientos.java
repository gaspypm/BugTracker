package gui;

import DAO.DAOException;
import model.Incidencia;
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
    private Incidencia incidencia;
    private PanelManager panelManager;
    private JPanel reporteMovimientos;
    private JTable JTable;
    private DefaultTableModel contenido;
    private JScrollPane scrollPane;
    private JButton JButtonVolverAtras;
    private JLabel JLabelDescripcion;

    public ReporteMovimientos(PanelManager panelManager) throws ServiceException {
        this.panelManager = panelManager;
        serviceIncidencia = new ServiceIncidencia();
        int IDIncidencia = 0;

        String idStr = JOptionPane.showInputDialog(
                this,
                "Ingrese el ID de la incidencia:",
                "Buscar Movimientos",
                JOptionPane.QUESTION_MESSAGE
        );

        try {
            if (idStr != null && !idStr.isEmpty()) {
                IDIncidencia = Integer.parseInt(idStr);
                this.incidencia = serviceIncidencia.buscar(IDIncidencia);
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        try {
            armarTablaReporte(IDIncidencia);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    public void armarTablaReporte(int IDIncidencia) throws ServiceException, DAOException {
        ServiceMovimiento service = new ServiceMovimiento();
        setLayout(new BorderLayout());
        reporteMovimientos = new JPanel();
        reporteMovimientos.setLayout(new BorderLayout());
        contenido = new DefaultTableModel();
        JTable = new JTable(contenido);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(JTable);
        JButtonVolverAtras = new JButton("Volver atrás");
        JLabelDescripcion = new JLabel("");
        contenido.addColumn("ID Movimiento");
        contenido.addColumn("Estado Anterior");
        contenido.addColumn("Estado Nuevo");
        contenido.addColumn("Usuario");
        contenido.addColumn("Fecha");

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(JButtonVolverAtras);
        headerPanel.add(JLabelDescripcion);
        reporteMovimientos.add(headerPanel, BorderLayout.NORTH);

        // Estilos
        try {
            Image iconoVolverAtras = ImageIO.read(getClass().getResource("/iconos/volver_atras.png"));
            JButtonVolverAtras.setIcon(new ImageIcon(iconoVolverAtras.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Movimiento> movimientos;

        movimientos = service.buscarPorIncidencia(IDIncidencia);
        JLabelDescripcion.setText("ID: " + incidencia.getIdIncidencia() + " | Descripción: " + incidencia.getDescripcion());

        for (Movimiento movimiento : movimientos) {
            Object[] fila = new Object[5];
            fila[0] = movimiento.getIdMovimiento();
            fila[1] = movimiento.getEstadoAnterior();
            fila[2] = movimiento.getEstadoNuevo();
            fila[3] = movimiento.getUsuario().getNombreUsuario();
            fila[4] = movimiento.getFechaCambio();
            contenido.addRow(fila);
        }

        add(reporteMovimientos, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.SOUTH);

        // Botones
        JButtonVolverAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panelManager.mostrar(panelManager.getMenuAdministrador());
                } catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo abrir el formulario de incidencias");
                }
            }
        });
    }
}
