import java.util.ArrayList;

public class Administrador extends Usuario {
    public Administrador(String nombreUsuario) {
        super(nombreUsuario);
    }

    public void crearUsuario(String nombreUsuario, ArrayList<String> permisos) {
        Usuario usuario = new Usuario(nombreUsuario, permisos);
    }

    public void crearUsuario(String nombreUsuario) {
        crearUsuario(nombreUsuario);
    }

    public void crearProyecto(String nombreProyecto) {
        Proyecto proyecto = new Proyecto(nombreProyecto);
    }

    public void obtenerReporte(int idProyecto) {

    }
}
