import java.util.ArrayList;

public class Proyecto {
    private int idProyecto;
    private String nombreProyecto;
    private ArrayList<Incidencia> incidencias;

    public Proyecto() {
        this.idProyecto = 0;
        this.nombreProyecto = "";
        this.incidencias = new ArrayList<>();
    }

    public Proyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
        this.incidencias = new ArrayList<>();
    }

    public void agregarIncidencia(Incidencia incidencia) {
        this.incidencias.add(incidencia);
    }
}
