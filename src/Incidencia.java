import java.util.ArrayList;

public class Incidencia {
    private int idIncidencia;
    private static int numIncidencia = 1;
    private String descripcion;
    private Usuario usuario;
    private Proyecto proyecto;
    private String estado;
    private double estimacionHoras;
    private double tiempoInvertido;
    private ArrayList<Movimiento> movimientos;

    // Constructor por defecto sin Usuario ni Proyecto
    public Incidencia() {
        idIncidencia = numIncidencia;
        incrementarIdIncidencia();
        this.descripcion = "";
        this.estado = "Nuevo";
        this.estimacionHoras = 0;
        this.movimientos = new ArrayList<>();
    }

    // Constructor para crear incidencia desde Proyecto
    public Incidencia(String descripcion, Usuario usuario, double estimacionHoras) {
        this.idIncidencia = numIncidencia;
        incrementarIdIncidencia();
        this.descripcion = descripcion;
        this.usuario = usuario;
        this.estado = "Nuevo";
        this.estimacionHoras = estimacionHoras;
        this.movimientos = new ArrayList<>();
    }

    public Incidencia(String descripcion, double estimacionHoras, String estado) {
        idIncidencia = numIncidencia;
        incrementarIdIncidencia();
        this.descripcion = descripcion;
        this.estado = "Nuevo";
        this.estimacionHoras = estimacionHoras;
        this.movimientos = new ArrayList<>();
    }

    public void reportar(Usuario usuario, String descripcion, double estimacionHoras, Proyecto proyecto) {
        this.descripcion = descripcion;
        this.usuario = usuario;
        this.estimacionHoras = estimacionHoras;
        this.proyecto = proyecto;
        this.getMovimientos().add(new Movimiento(usuario));
    }

    public void actualizarEstado(Usuario usuario, String nuevoEstado) {
        this.usuario = usuario;
        this.getMovimientos().add(new Movimiento(this.estado, nuevoEstado, usuario));
    }

    public void agregarTiempoInvertido(double tiempoInvertido) {
        this.tiempoInvertido += tiempoInvertido;    // Botón con +/- para agregar horas
    }

    public void cerrar(){
        this.estado = "Cerrado";
    }

    public void incrementarIdIncidencia() {
        numIncidencia++;
    }

    public int getIdIncidencia() {
        return idIncidencia;
    }

    public ArrayList<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(ArrayList<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }
}
