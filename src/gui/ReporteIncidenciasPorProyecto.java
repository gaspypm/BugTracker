package gui;

import DAO.DAOException;
import model.Incidencia;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import model.Proyecto;
import service.ServiceProyecto;
import service.ServiceException;
import service.ServiceIncidencia;

public class ReporteIncidenciasPorProyecto extends JPanel {
    private ServiceProyecto serviceProyecto;
    private ServiceIncidencia serviceIncidencia;
    private Incidencia incidencia;
    private PanelManager panelManager;
    private JPanel reporteIncidenciasPorProyecto;
    private JTable JTable;
    private DefaultTableModel contenido;
    private JScrollPane scrollPane;
    private JButton JButtonVolverAtras;
    private JLabel JLabelDescripcion;
    private JComboBox<String> JComboBoxProyectos;

    public ReporteIncidenciasPorProyecto(PanelManager panelManager) {
        this.panelManager = panelManager;
        serviceIncidencia = new ServiceIncidencia();
        serviceProyecto = new ServiceProyecto();
        armarTablaReporte();
    }

    public void armarTablaReporte() {
        ServiceProyecto service = new ServiceProyecto();
        setLayout(new BorderLayout());
        reporteIncidenciasPorProyecto = new JPanel();
        reporteIncidenciasPorProyecto.setLayout(new BorderLayout());
        contenido = new DefaultTableModel();
        JTable = new JTable(contenido);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(JTable);
        JButtonVolverAtras = new JButton("Volver atrás");
        JLabelDescripcion = new JLabel("");
        JComboBoxProyectos = new JComboBox<>();
        contenido.addColumn("ID Incidencia");
        contenido.addColumn("Descripción");
        contenido.addColumn("Estado");
        contenido.addColumn("Est. horas");
        contenido.addColumn("Tiempo invertido");
        contenido.addColumn("Usuario");

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(JComboBoxProyectos);
        headerPanel.add(JButtonVolverAtras);
        reporteIncidenciasPorProyecto.add(headerPanel, BorderLayout.NORTH);

        add(reporteIncidenciasPorProyecto, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        cargarListaProyectos();

        // Estilos
        try {
            Image iconoVolverAtras = ImageIO.read(getClass().getResource("/iconos/volver_atras.png"));
            JButtonVolverAtras.setIcon(new ImageIcon(iconoVolverAtras.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        add(reporteIncidenciasPorProyecto, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.SOUTH);

        JComboBoxProyectos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombreProyecto = (String) JComboBoxProyectos.getSelectedItem();
                    if (nombreProyecto != null) {
                        cargarIncidencias(nombreProyecto);
                    }
                } catch (ServiceException ex) {
                    JOptionPane.showMessageDialog(null, "Error al cargar incidencias del proyecto.");
                }
            }
        });

        // Botones
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

    private void cargarListaProyectos() {
        try {
            ArrayList<Proyecto> proyectos = serviceProyecto.buscarTodos();
            for (Proyecto proyecto : proyectos) {
                JComboBoxProyectos.addItem(proyecto.getNombreProyecto());
            }

            if (!proyectos.isEmpty()) {
                JComboBoxProyectos.setSelectedIndex(0);
                cargarIncidencias(proyectos.get(0).getNombreProyecto());
            }
        } catch (ServiceException e) {
            throw new RuntimeException("Error al cargar proyectos");
        }
    }


    private void cargarIncidencias(String nombreProyecto) throws ServiceException {
        contenido.setRowCount(0);

        try {
            int idProyecto = serviceProyecto.obtenerID(nombreProyecto);
            ArrayList<Incidencia> incidencias = serviceIncidencia.buscarPorProyecto(idProyecto);

            for (Incidencia incidencia : incidencias) {
                Object[] fila = new Object[6];
                fila[0] = incidencia.getIdIncidencia();
                fila[1] = incidencia.getDescripcion();
                fila[2] = incidencia.getEstado();
                fila[3] = incidencia.getEstimacionHoras();
                fila[4] = incidencia.getTiempoInvertido();
                fila[5] = incidencia.getUsuarioResponsable().getNombreUsuario();
                contenido.addRow(fila);
            }
        } catch (DAOException e) {
            throw new ServiceException("Error al cargar incidencias");
        }
    }
}
