package DAO;

import io.github.cdimascio.dotenv.Dotenv;
import model.Incidencia;
import model.Movimiento;
import model.Usuario;

import service.ServiceUsuario;
import service.ServiceIncidencia;

import java.sql.*;
import java.util.ArrayList;

public class DAOMovimiento implements IDAO<Movimiento> {
    private ServiceUsuario serviceUsuario;
    private ServiceIncidencia serviceIncidencia;

    Dotenv dotenv = Dotenv.load();
    String DB_JDBC_DRIVER = dotenv.get("DB_JDBC_DRIVER");
    String DB_URL = dotenv.get("DB_URL");
    String DB_USER = dotenv.get("DB_USER");
    String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    @Override
    public void guardar(Movimiento movimiento) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Incidencia incidencia = new Incidencia();

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            preparedStatement = connection.prepareStatement("INSERT INTO MOVIMIENTO (ID_INCIDENCIA, ID_ESTADO_ANTERIOR, ID_ESTADO_NUEVO, FECHA_CAMBIO, USUARIO_RESPONSABLE) VALUES (?,?,?,?,?)");
            preparedStatement.setInt(1, movimiento.getIncidencia().getIdIncidencia());
            if (movimiento.getEstadoAnterior() == null || movimiento.getEstadoAnterior().isEmpty()) {
                preparedStatement.setInt(2, 1);
            } else {
                preparedStatement.setInt(2, incidencia.getIDEstado(movimiento.getEstadoAnterior()));
            }
            preparedStatement.setInt(3, incidencia.getIDEstado(movimiento.getEstadoNuevo()));
            preparedStatement.setDate(4, new java.sql.Date(movimiento.getFechaCambio().getTime()));
            preparedStatement.setInt(5, movimiento.getUsuario().getIdUsuario());

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
    public void modificar(Movimiento elemento) throws DAOException {

    }

    @Override
    public void eliminar(int id) throws DAOException {

    }

    @Override
    public Movimiento buscar(int id) throws DAOException {
        return null;
    }

    @Override
    public ArrayList<Movimiento> buscarTodos() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ArrayList<Movimiento> movimientos = new ArrayList<>();

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            preparedStatement = connection.prepareStatement(
                    "SELECT MOVIMIENTO.*, USUARIO.NOMBRE_USUARIO, INCIDENCIA.DESCRIPCION " +
                            "FROM MOVIMIENTO " +
                            "LEFT JOIN USUARIO ON MOVIMIENTO.USUARIO_RESPONSABLE = USUARIO.ID_USUARIO " +
                            "LEFT JOIN INCIDENCIA ON MOVIMIENTO.ID_INCIDENCIA = INCIDENCIA.ID_INCIDENCIA"
            );
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Movimiento movimiento = new Movimiento();
                Incidencia incidencia = new Incidencia();
                Usuario usuario = new Usuario();

                movimiento.setIdMovimiento(rs.getInt("ID_MOVIMIENTO"));
                movimiento.setEstadoAnterior(incidencia.getEstadoID(rs.getInt("ID_ESTADO_ANTERIOR")));
                movimiento.setEstadoNuevo(incidencia.getEstadoID(rs.getInt("ID_ESTADO_NUEVO")));
                movimiento.setFechaCambio(rs.getDate("FECHA_CAMBIO"));
                usuario.setIdUsuario(rs.getInt("USUARIO_RESPONSABLE"));
                usuario.setNombreUsuario(rs.getString("NOMBRE_USUARIO"));
                movimiento.setUsuario(usuario);
                incidencia.setIdIncidencia(rs.getInt("ID_INCIDENCIA"));
                incidencia.setDescripcion(rs.getString("DESCRIPCION"));
                movimiento.setIncidencia(incidencia);

                movimientos.add(movimiento);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new DAOException("Ocurrió un error en la base de datos");
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return movimientos;
    }

    public ArrayList<Movimiento> buscarPorIncidencia(int idIncidencia) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ArrayList<Movimiento> movimientos = new ArrayList<>();

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            preparedStatement = connection.prepareStatement("SELECT MOVIMIENTO.*, USUARIO.NOMBRE_USUARIO, INCIDENCIA.DESCRIPCION " +
                            "FROM MOVIMIENTO LEFT JOIN USUARIO ON MOVIMIENTO.USUARIO_RESPONSABLE = USUARIO.ID_USUARIO " +
                            "LEFT JOIN INCIDENCIA ON MOVIMIENTO.ID_INCIDENCIA = INCIDENCIA.ID_INCIDENCIA " +
                            "WHERE MOVIMIENTO.ID_INCIDENCIA = ?"
            );
            preparedStatement.setInt(1, idIncidencia);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Movimiento movimiento = new Movimiento();
                Usuario usuario = new Usuario();
                Incidencia incidencia = new Incidencia();

                movimiento.setIdMovimiento(rs.getInt("ID_MOVIMIENTO"));
                movimiento.setEstadoAnterior(incidencia.getEstadoID(rs.getInt("ID_ESTADO_ANTERIOR")));
                movimiento.setEstadoNuevo(incidencia.getEstadoID(rs.getInt("ID_ESTADO_NUEVO")));
                movimiento.setFechaCambio(rs.getDate("FECHA_CAMBIO"));

                usuario.setIdUsuario(rs.getInt("USUARIO_RESPONSABLE"));
                usuario.setNombreUsuario(rs.getString("NOMBRE_USUARIO"));
                movimiento.setUsuario(usuario);

                incidencia.setIdIncidencia(rs.getInt("ID_INCIDENCIA"));
                incidencia.setDescripcion(rs.getString("DESCRIPCION"));
                movimiento.setIncidencia(incidencia);

                movimientos.add(movimiento);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new DAOException("Ocurrió un error en la base de datos");
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return movimientos;
    }


    @Override
    public int obtenerUltimoID() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT MAX(ID_MOVIMIENTO) AS ID FROM MOVIMIENTO");
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next())
                return rs.getInt("ID");
            else
                return 1;
        } catch (ClassNotFoundException|SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error al acceder a la base de datos");
        } finally {
            try {
                preparedStatement.close();
            } catch(SQLException e) {
                e.printStackTrace();
                throw new DAOException("Error");
            }
        }
    }
}

