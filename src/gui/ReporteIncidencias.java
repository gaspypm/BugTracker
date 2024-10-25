package gui;

import DAO.DAOException;
import service.ServiceIncidencia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import model.Incidencia;
import service.ServiceException;

public class ReporteIncidencias extends JPanel {
    private PanelManager panelManager;
    private JTable jTable;
    private DefaultTableModel contenido;
    private JScrollPane scrollPane;

    public ReporteIncidencias(PanelManager panelManager) throws ServiceException {
        this.panelManager = panelManager;
        armarTablaReporte();
    }
    public void armarTablaReporte() throws ServiceException {
        ServiceIncidencia service = new ServiceIncidencia();
        setLayout(new BorderLayout());
        contenido= new DefaultTableModel();
        jTable = new JTable(contenido);
        scrollPane=new JScrollPane();
        scrollPane.setViewportView(jTable);
        contenido.addColumn("ID");
        contenido.addColumn("Descripción");
        contenido.addColumn("Estimación Horas");
        contenido.addColumn("Estado");
        contenido.addColumn("Tiempo Invertido");
        contenido.addColumn("Usuario");

        try {
            ArrayList<Incidencia> incidencias = service.buscarTodos();
            for(Incidencia incidencia:incidencias) {
                Object [] fila= new Object[6];
                fila[0] = incidencia.getIdIncidencia();
                fila[1] = incidencia.getDescripcion();
                fila[2] = incidencia.getEstimacionHoras();
                fila[3] = incidencia.getEstado();
                fila[4] = incidencia.getTiempoInvertido();
                fila[5] = incidencia.getUsuario().getNombreUsuario();

                contenido.addRow(fila);
            }
        }
        catch ( DAOException e) {
            JOptionPane.showMessageDialog(null, "Error");
        }
        add(scrollPane, BorderLayout.CENTER);
    }
}
