package DAO;

import io.github.cdimascio.dotenv.Dotenv;
import model.Incidencia;
import model.Usuario;
import model.Proyecto;
import service.ServiceUsuario;

import java.sql.*;
import java.util.ArrayList;

public class DAOIncidencia implements IDAO<Incidencia> {
    private ServiceUsuario serviceUsuario;
    Dotenv dotenv = Dotenv.load();
    String DB_JDBC_DRIVER = dotenv.get("DB_JDBC_DRIVER");
    String DB_URL = dotenv.get("DB_URL");
    String DB_USER = dotenv.get("DB_USER");
    String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    @Override
    public void guardar(Incidencia incidencia) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int idIncidencia;
        serviceUsuario = new ServiceUsuario();

        if (incidencia.getIdIncidencia() != 0)
            idIncidencia = incidencia.getIdIncidencia();
        else {
            try {
                idIncidencia = obtenerUltimoID() + 1;
            }
            catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            preparedStatement = connection.prepareStatement("INSERT INTO INCIDENCIA (ID_INCIDENCIA, DESCRIPCION, ESTIMACION_HORAS, ESTADO, TIEMPO_INVERTIDO, USUARIO_RESPONSABLE) VALUES (?,?,?,?,?,?)");

            preparedStatement.setInt(1, idIncidencia);
            preparedStatement.setString(2, incidencia.getDescripcion());
            preparedStatement.setDouble(3, incidencia.getEstimacionHoras());
            preparedStatement.setString(4, incidencia.getEstado());
            preparedStatement.setDouble(5, incidencia.getTiempoInvertido());
            preparedStatement.setInt(6, incidencia.getUsuario().getIdUsuario());

            int i = preparedStatement.executeUpdate();
            System.out.println(i);
        }
        catch (ClassNotFoundException|SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error al acceder a la base de datos");
        }
        finally {
            try {
                preparedStatement.close();
            }
            catch(SQLException e) {
                e.printStackTrace();
                throw new DAOException("Error");
            }
        }
    }

    @Override
    public void modificar(Incidencia incidencia) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("UPDATE INCIDENCIA SET DESCRIPCION = ?, ESTIMACION_HORAS = ?, ESTADO = ? WHERE ID_INCIDENCIA = ?");
            preparedStatement.setString(1, incidencia.getDescripcion());
            preparedStatement.setDouble(2, incidencia.getEstimacionHoras());
            preparedStatement.setString(3, incidencia.getEstado());
            preparedStatement.setInt(4, incidencia.getIdIncidencia());

            int i = preparedStatement.executeUpdate();
            System.out.println(i);
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error al acceder a la base de datos");
        }
        finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException("Error al cerrar la conexión");
            }
        }
    }

    @Override
    public void eliminar(int id) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("DELETE FROM INCIDENCIA WHERE ID_INCIDENCIA=?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
        catch (ClassNotFoundException|SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error al acceder a la base de datos");
        }
        finally {
            try {
                preparedStatement.close();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
                throw new DAOException("Error");
            }
        }

    }

    @Override
    public Incidencia buscar(int id) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Incidencia incidenciaAux = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT * FROM INCIDENCIA WHERE ID_INCIDENCIA = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                incidenciaAux = new Incidencia();
                incidenciaAux.setIdIncidencia(rs.getInt("ID_INCIDENCIA"));
                incidenciaAux.setDescripcion(rs.getString("DESCRIPCION"));
                incidenciaAux.setEstimacionHoras(rs.getDouble("ESTIMACION_HORAS"));
                incidenciaAux.setEstado(rs.getString("ESTADO"));
            }
            return incidenciaAux;
        }
        catch (ClassNotFoundException|SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error al acceder a la base de datos");
        }
        finally {
            try {
                preparedStatement.close();
            }
            catch(SQLException e) {
                e.printStackTrace();
                throw new DAOException("Error");
            }
        }
    }

    @Override
    public ArrayList<Incidencia> buscarTodos() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Incidencia incidencia = null;
        Usuario usuario = null;
        Proyecto proyecto = null;
        ArrayList<Incidencia> incidencias = new ArrayList<>();

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            preparedStatement = connection.prepareStatement("SELECT INCIDENCIA.*, USUARIO.NOMBRE_USUARIO, PROYECTO.NOMBRE_PROYECTO  FROM INCIDENCIA LEFT JOIN USUARIO ON INCIDENCIA.USUARIO_RESPONSABLE = USUARIO.ID_USUARIO LEFT JOIN PROYECTO ON INCIDENCIA.PROYECTO = PROYECTO.ID_PROYECTO");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                incidencia = new Incidencia();
                usuario = new Usuario();
                proyecto = new Proyecto();
                incidencia.setIdIncidencia(rs.getInt("ID_INCIDENCIA"));
                incidencia.setDescripcion(rs.getString("DESCRIPCION"));
                incidencia.setEstimacionHoras(rs.getDouble("ESTIMACION_HORAS"));
                incidencia.setEstado(rs.getString("ESTADO"));
                incidencia.setEstado(String.valueOf(rs.getDouble("TIEMPO_INVERTIDO")));
                usuario.setIdUsuario(rs.getInt("USUARIO_RESPONSABLE"));
                usuario.setNombreUsuario(rs.getString("NOMBRE_USUARIO"));
                proyecto.setIdProyecto(rs.getInt("PROYECTO"));
                proyecto.setNombreProyecto(rs.getString("NOMBRE_PROYECTO"));

                incidencia.setUsuario(usuario);
                incidencia.setProyecto(proyecto);
                incidencias.add(incidencia);
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DAOException("Ocurrio un error en la base de datos");
        }
        return incidencias;
    }

    @Override
    public int obtenerUltimoID() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT MAX(ID_INCIDENCIA) AS id FROM INCIDENCIA");
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next())
                return rs.getInt("id");
            else
                return 1;
        }
        catch (ClassNotFoundException|SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error al acceder a la base de datos");
        }
        finally {
            try {
                preparedStatement.close();
            }
            catch(SQLException e) {
                e.printStackTrace();
                throw new DAOException("Error");
            }
        }
    }

    public void cerrar(Incidencia incidencia) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("UPDATE INCIDENCIA SET CERRADA = TRUE WHERE ID_INCIDENCIA = ?");
            preparedStatement.setInt(1, incidencia.getIdIncidencia());

            int i = preparedStatement.executeUpdate();
            System.out.println(i);
        }
        catch (ClassNotFoundException|SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error al acceder a la base de datos");
        }
        finally {
            try {
                preparedStatement.close();
            }
            catch(SQLException e) {
                e.printStackTrace();
                throw new DAOException("Error");
            }
        }
    }
}