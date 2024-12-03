package gui;

import DAO.DAOException;
import model.Proyecto;
import model.Incidencia;
import model.Usuario;
import service.ServiceProyecto;
import service.ServiceIncidencia;
import service.ServiceUsuario;
import service.ServiceException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class ReporteProyectos extends JPanel {
    private ServiceProyecto serviceProyecto;
    private ServiceIncidencia serviceIncidencia;
    private ServiceUsuario serviceUsuario;
    private Usuario usuario;
    private PanelManager panel;

    private JPanel reporteProyectos;
    private JComboBox<String> JComboBoxProyectos;
    private JButton JButtonGenerarReporte;
    private JLabel JLabelSeleccionProyecto;
    private JLabel JLabelProyectosAdelantados;
    private JLabel JLabelProyectosAdelantadosLista;
    private JLabel JLabelProyectosAtrasados;
    private JLabel JLabelProyectosAtrasadosLista;
    private JButton JButtonVolverAtras;

    private ArrayList<Proyecto> proyectos = new ArrayList<>();
    private ArrayList<Incidencia> incidencias = new ArrayList<>();
    int incidenciasAtrasadas = 0;
    int incidenciasAdelantadas = 0;
    double horasInvertidasTotales = 0;
    int incidenciasNuevas = 0;
    int incidenciasAsignadas = 0;
    int incidenciasEnProgreso = 0;
    int incidenciasResueltas = 0;
    int incidenciasVerificadas = 0;
    int incidenciasPospuestas = 0;
    int incidenciasRechazadas = 0;
    int incidenciasCerradas = 0;
    int incidenciasReabiertas = 0;

    public ReporteProyectos(PanelManager panel) throws ServiceException {
        this.panel = panel;

        armarFormulario();
    }

    private void armarFormulario() throws ServiceException {
        // Inicializo todos los elementos de la ventana
        serviceProyecto = new ServiceProyecto();
        serviceIncidencia = new ServiceIncidencia();
        serviceUsuario = new ServiceUsuario();
        reporteProyectos = new JPanel();
        reporteProyectos.setLayout(new GridLayout(5, 2, 10, 10));
        reporteProyectos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButtonVolverAtras = new JButton("Volver atrás");
        JLabelSeleccionProyecto = new JLabel("Seleccione un proyecto:");
        JComboBoxProyectos = new JComboBox<>();
        JButtonGenerarReporte = new JButton("Generar reporte");
        JLabelProyectosAdelantados = new JLabel("Proyectos adelantados:");
        JLabelProyectosAdelantadosLista = new JLabel("");
        JLabelProyectosAtrasados = new JLabel("Proyectos atrasados:");
        JLabelProyectosAtrasadosLista = new JLabel("");

        usuario = panel.getUsuarioActual();

        // Verifico permisos de administrador
        if (!usuario.getTipo().equals("ADMINISTRADOR")) {
            JOptionPane.showMessageDialog(null, "No posee los permisos necesarios");
            panel.mostrar(panel.getMenuAdministrador());
            return;
        }

        // Estilos
        try {
            Image iconoVolverAtras = ImageIO.read(getClass().getResource("/iconos/volver_atras.png"));
            JButtonVolverAtras.setIcon(new ImageIcon(iconoVolverAtras.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No se pudo cargar el ícono del botón.");
        }

        // Cargo datos de incidencias y de proyectos
        cargarDatos();

        // Cargo proyectos en el JComboBox
        cargarProyectos();

        // Agrego elementos al panel
        reporteProyectos.add(new JLabel()); // Espacio vacío
        reporteProyectos.add(JButtonVolverAtras);
        reporteProyectos.add(JLabelSeleccionProyecto);
        reporteProyectos.add(JComboBoxProyectos);
        reporteProyectos.add(new JLabel()); // Espacio vacío
        reporteProyectos.add(JButtonGenerarReporte);
        reporteProyectos.add(JLabelProyectosAdelantados);
        reporteProyectos.add(JLabelProyectosAdelantadosLista);
        reporteProyectos.add(JLabelProyectosAtrasados);
        reporteProyectos.add(JLabelProyectosAtrasadosLista);

        // Botones
        JButtonGenerarReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarReporte();
            }
        });

        JButtonVolverAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.mostrar(panel.getMenuAdministrador());
                } catch (ServiceException d) {
                    JOptionPane.showMessageDialog(null, "Error al volver al menú administrador");
                }
            }
        });

        setLayout(new BorderLayout());
        add(reporteProyectos, BorderLayout.CENTER);
    }

    private void cargarDatos() throws ServiceException {
        try {
            proyectos = serviceProyecto.buscarTodos();

            // Limpiar las etiquetas de proyectos adelantados y atrasados
            JLabelProyectosAdelantadosLista.setText("");
            JLabelProyectosAtrasadosLista.setText("");

            String proyectosAdelantados = "";
            String proyectosAtrasados = "";

            for (Proyecto proyecto : proyectos) {
                ArrayList<Incidencia> incidenciasProyecto = serviceIncidencia.buscarPorProyecto(proyecto.getIdProyecto());

                // Almacenar todas las incidencias globalmente
                for (Incidencia incidencia : incidenciasProyecto) {
                    incidencia.setProyecto(proyecto);
                    incidencias.add(incidencia);
                }

                double totalHorasInvertidas = 0;
                double totalHorasEstimadas = 0;

                // Calcular las horas totales del proyecto
                for (Incidencia incidencia : incidenciasProyecto) {
                    totalHorasInvertidas += incidencia.getTiempoInvertido();
                    totalHorasEstimadas += incidencia.getEstimacionHoras();
                }

                // Clasificar proyecto como adelantado o atrasado
                if (totalHorasInvertidas > totalHorasEstimadas) {
                    if (!proyectosAtrasados.isEmpty()) {
                        proyectosAtrasados += ", ";
                    }
                    proyectosAtrasados += proyecto.getNombreProyecto();
                } else {
                    if (!proyectosAdelantados.isEmpty()) {
                        proyectosAdelantados += ", ";
                    }
                    proyectosAdelantados += proyecto.getNombreProyecto();
                }
            }

            // Actualizar las etiquetas
            JLabelProyectosAdelantadosLista.setText(proyectosAdelantados);
            JLabelProyectosAtrasadosLista.setText(proyectosAtrasados);

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos: " + e.getMessage());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    private void mostrarReporte() {
        String proyectoSeleccionado = (String) JComboBoxProyectos.getSelectedItem();
        if (proyectoSeleccionado == null) {
            JOptionPane.showMessageDialog(null, "Seleccione un proyecto");
            return;
        }

        incidenciasAtrasadas = 0;
        incidenciasAdelantadas = 0;
        horasInvertidasTotales = 0;
        incidenciasNuevas = 0;
        incidenciasAsignadas = 0;
        incidenciasEnProgreso = 0;
        incidenciasResueltas = 0;
        incidenciasVerificadas = 0;
        incidenciasPospuestas = 0;
        incidenciasRechazadas = 0;
        incidenciasCerradas = 0;
        incidenciasReabiertas = 0;

        int totalIncidencias = 0;

        for (Incidencia incidencia : incidencias) {
            if (incidencia.getProyecto().getNombreProyecto().equals(proyectoSeleccionado)) {
                int estadoID = incidencia.getIDEstado(incidencia.getEstado());
                switch (estadoID) {
                    case 1 -> incidenciasNuevas++;
                    case 2 -> incidenciasAsignadas++;
                    case 3 -> incidenciasEnProgreso++;
                    case 4 -> incidenciasResueltas++;
                    case 5 -> incidenciasVerificadas++;
                    case 6 -> incidenciasCerradas++;
                    case 7 -> incidenciasReabiertas++;
                    case 8 -> incidenciasPospuestas++;
                    case 9 -> incidenciasRechazadas++;
                }

                horasInvertidasTotales += incidencia.getTiempoInvertido();
                totalIncidencias++;

                if (incidencia.getTiempoInvertido() > incidencia.getEstimacionHoras()) {
                    incidenciasAtrasadas++;
                } else {
                    incidenciasAdelantadas++;
                }
            }
        }

        // Mostrar reporte
        JFrame reporteProyectoSeleccionado = new JFrame("Reporte del Proyecto");
        reporteProyectoSeleccionado.setLayout(new GridLayout(7, 1, 10, 10));
        reporteProyectoSeleccionado.setSize(430, 300);

        reporteProyectoSeleccionado.add(new JLabel(" Proyecto: " + proyectoSeleccionado));
        reporteProyectoSeleccionado.add(new JLabel(" Total de incidencias: " + totalIncidencias));
        reporteProyectoSeleccionado.add(new JLabel(" Incidencias Atrasadas: " + incidenciasAtrasadas));
        reporteProyectoSeleccionado.add(new JLabel(" Incidencias Adelantadas: " + incidenciasAdelantadas));
        reporteProyectoSeleccionado.add(new JLabel(" Horas invertidas totales: " + String.format("%.2f", horasInvertidasTotales)));
        reporteProyectoSeleccionado.add(new JLabel(" Incidencias Nuevas: " + incidenciasNuevas));
        reporteProyectoSeleccionado.add(new JLabel(" Incidencias Asignadas: " + incidenciasAsignadas));
        reporteProyectoSeleccionado.add(new JLabel(" Incidencias En Progreso: " + incidenciasEnProgreso));
        reporteProyectoSeleccionado.add(new JLabel(" Incidencias Resueltas: " + incidenciasResueltas));
        reporteProyectoSeleccionado.add(new JLabel(" Incidencias Verificadas: " + incidenciasVerificadas));
        reporteProyectoSeleccionado.add(new JLabel(" Incidencias Pospuestas: " + incidenciasPospuestas));
        reporteProyectoSeleccionado.add(new JLabel(" Incidencias Rechazadas: " + incidenciasRechazadas));
        reporteProyectoSeleccionado.add(new JLabel(" Incidencias Reabiertas: " + incidenciasReabiertas));
        reporteProyectoSeleccionado.add(new JLabel(" Incidencias Cerradas: " + incidenciasCerradas));

        reporteProyectoSeleccionado.setVisible(true);
        reporteProyectoSeleccionado.setLocationRelativeTo(null);
    }

    private void cargarProyectos() {
        for (Proyecto proyecto : proyectos) {
            JComboBoxProyectos.addItem(proyecto.getNombreProyecto());
        }
    }
}