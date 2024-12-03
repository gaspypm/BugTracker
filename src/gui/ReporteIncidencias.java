package gui;

import DAO.DAOException;
import service.ServiceIncidencia;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import model.Incidencia;
import service.ServiceException;

public class ReporteIncidencias extends JPanel {
    private PanelManager panel;
    private JPanel reporteIncidencias;
    private JTable JTable;
    private DefaultTableModel contenido;
    private JScrollPane scrollPane;
    private JButton JButtonVolverAtras;

    public ReporteIncidencias(PanelManager panel) throws ServiceException {
        this.panel = panel;
        armarTablaReporte();
    }

    public void armarTablaReporte() throws ServiceException {
        ServiceIncidencia service = new ServiceIncidencia();
        setLayout(new BorderLayout());
        reporteIncidencias = new JPanel();
        reporteIncidencias.setLayout(new BorderLayout());
        contenido = new DefaultTableModel();
        JTable = new JTable(contenido);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(JTable);
        JButtonVolverAtras = new JButton("Volver atrás");
        contenido.addColumn("ID");
        contenido.addColumn("Descripción");
        contenido.addColumn("Est. Horas");
        contenido.addColumn("Estado");
        contenido.addColumn("Tiempo Invertido");
        contenido.addColumn("Usuario");
        contenido.addColumn("Proyecto");
        reporteIncidencias.add(JButtonVolverAtras);

        // Estilos
        try {
            Image iconoVolverAtras = ImageIO.read(getClass().getResource("/iconos/volver_atras.png"));
            JButtonVolverAtras.setIcon(new ImageIcon(iconoVolverAtras.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            ArrayList<Incidencia> incidencias = service.buscarTodos();
            for(Incidencia incidencia:incidencias) {
                Object [] fila = new Object[7];
                fila[0] = incidencia.getIdIncidencia();
                fila[1] = incidencia.getDescripcion();
                fila[2] = incidencia.getEstimacionHoras();
                fila[3] = incidencia.getEstado();
                fila[4] = incidencia.getTiempoInvertido();
                fila[5] = incidencia.getUsuarioResponsable().getNombreUsuario();
                fila[6] = incidencia.getProyecto().getNombreProyecto();

                contenido.addRow(fila);
            }
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(null, "Error");
        }
        add(reporteIncidencias, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.SOUTH);

        // Botones
        JButtonVolverAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.mostrar(panel.getFormularioIncidencias());
                } catch (ServiceException s) {
                    JOptionPane.showMessageDialog(null,"No se pudo abrir el formulario de incidencias");
                }
            }
        });
    }
}
