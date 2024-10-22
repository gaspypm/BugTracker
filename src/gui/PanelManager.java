package gui;

import service.ServiceException;

import javax.swing.*;
import java.awt.*;

public class PanelManager {
    private FormularioIncidencia formularioIncidencia;
    private ReporteIncidencias reporteIncidencias;
    JFrame jFrame;

    public PanelManager(int tipo) throws ServiceException {
        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (tipo==1) {
            formularioIncidencia = new FormularioIncidencia(this);
            mostrar(formularioIncidencia);
        }
        else if (tipo==2) {
            reporteIncidencias = new ReporteIncidencias(this);
            mostrar(reporteIncidencias);
        }
    }
    public void mostrar(JPanel panel) {
        jFrame.getContentPane().removeAll();
        jFrame.getContentPane().add(BorderLayout.SOUTH, panel);
        jFrame.getContentPane().validate();
        jFrame.getContentPane().repaint();
        jFrame.show();
        jFrame.pack();
    }

    public ReporteIncidencias getReporteIncidencias() throws ServiceException {
        return reporteIncidencias=new ReporteIncidencias(this);
    }
}
