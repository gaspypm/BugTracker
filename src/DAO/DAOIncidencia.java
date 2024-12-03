package DAO;

import io.github.cdimascio.dotenv.Dotenv;
import model.Incidencia;
import model.Usuario;
import model.Proyecto;
import model.Movimiento;
import service.ServiceException;
import service.ServiceUsuario;
import service.ServiceMovimiento;

import java.sql.*;
import java.util.ArrayList;

public class DAOIncidencia implements IDAO<Incidencia> {
    private ServiceUsuario serviceUsuario;
    private ServiceMovimiento serviceMovimiento;

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
        serviceMovimiento = new ServiceMovimiento();

        if (incidencia.getIdIncidencia() != 0)
            idIncidencia = incidencia.getIdIncidencia();
        else {
            try {
                idIncidencia = obtenerUltimoID() + 1;
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO INCIDENCIA (ID_INCIDENCIA, DESCRIPCION, ESTIMACION_HORAS, ID_ESTADO, TIEMPO_INVERTIDO, USUARIO_RESPONSABLE, PROYECTO) " +
                            "VALUES (?,?,?,?,?,?,?)");

            preparedStatement.setInt(1, idIncidencia);
            preparedStatement.setString(2, incidencia.getDescripcion());
            preparedStatement.setDouble(3, incidencia.getEstimacionHoras());
            preparedStatement.setInt(4, incidencia.getIDEstado(incidencia.getEstado()));
            preparedStatement.setDouble(5, incidencia.getTiempoInvertido());
            preparedStatement.setInt(6, incidencia.getUsuarioResponsable().getIdUsuario());
            preparedStatement.setInt(7, incidencia.getProyecto().getIdProyecto());

            int i = preparedStatement.executeUpdate();

            Movimiento movimiento = new Movimiento("", incidencia.getEstado(), incidencia.getUsuarioResponsable());
            movimiento.setIncidencia(incidencia);
            serviceMovimiento.guardar(movimiento);

            System.out.println(i);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error al acceder a la base de datos");
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException("Error al cerrar la conexión");
            }
        }
    }

    @Override
    public void modificar(Incidencia incidencia) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        serviceMovimiento = new ServiceMovimiento();

        try {
            String estadoAnterior = this.buscar(incidencia.getIdIncidencia()).getEstado();

            if (!estadoAnterior.equals(incidencia.getEstado())) {
                Movimiento movimiento = new Movimiento(estadoAnterior, incidencia.getEstado(), incidencia.getUsuarioResponsable());
                movimiento.setIncidencia(incidencia);
                serviceMovimiento.guardar(movimiento);
            }

            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("UPDATE INCIDENCIA SET DESCRIPCION = ?, ESTIMACION_HORAS = ?, ID_ESTADO = ?, TIEMPO_INVERTIDO = ?, PROYECTO = ? WHERE ID_INCIDENCIA = ?");

            preparedStatement.setString(1, incidencia.getDescripcion());
            preparedStatement.setDouble(2, incidencia.getEstimacionHoras());
            preparedStatement.setInt(3, incidencia.getIDEstado(incidencia.getEstado()));
            preparedStatement.setDouble(4, incidencia.getTiempoInvertido());
            preparedStatement.setInt(5, incidencia.getProyecto().getIdProyecto());
            preparedStatement.setInt(6, incidencia.getIdIncidencia());

            int i = preparedStatement.executeUpdate();

            System.out.println(i);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error al acceder a la base de datos");
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
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
            preparedStatement = connection.prepareStatement(
                    "SELECT INCIDENCIA.*, PROYECTO.ID_PROYECTO, PROYECTO.NOMBRE_PROYECTO, " +
                            "USUARIO.ID_USUARIO, USUARIO.NOMBRE_USUARIO " +
                            "FROM INCIDENCIA " +
                            "LEFT JOIN PROYECTO ON INCIDENCIA.PROYECTO = PROYECTO.ID_PROYECTO " +
                            "LEFT JOIN USUARIO ON INCIDENCIA.USUARIO_RESPONSABLE = USUARIO.ID_USUARIO " +
                            "WHERE ID_INCIDENCIA = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                incidenciaAux = new Incidencia();
                Proyecto proyecto = new Proyecto();
                Usuario usuario = new Usuario();

                incidenciaAux.setIdIncidencia(rs.getInt("ID_INCIDENCIA"));
                incidenciaAux.setDescripcion(rs.getString("DESCRIPCION"));
                incidenciaAux.setEstimacionHoras(rs.getDouble("ESTIMACION_HORAS"));
                incidenciaAux.setEstado(incidenciaAux.getEstadoID(rs.getInt("ID_ESTADO")));
                incidenciaAux.setTiempoInvertido(rs.getDouble("TIEMPO_INVERTIDO"));

                proyecto.setIdProyecto(rs.getInt("ID_PROYECTO"));
                proyecto.setNombreProyecto(rs.getString("NOMBRE_PROYECTO"));
                incidenciaAux.setProyecto(proyecto);

                usuario.setIdUsuario(rs.getInt("ID_USUARIO"));
                usuario.setNombreUsuario(rs.getString("NOMBRE_USUARIO"));
                incidenciaAux.setUsuarioResponsable(usuario);
            }
            return incidenciaAux;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error al acceder a la base de datos");
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException("Error al cerrar la conexión");
            }
        }
    }

    public ArrayList<Incidencia> buscarPorProyecto(int idProyecto) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ArrayList<Incidencia> incidencias = new ArrayList<>();

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            preparedStatement = connection.prepareStatement(
                    "SELECT INCIDENCIA.*, USUARIO.NOMBRE_USUARIO, PROYECTO.NOMBRE_PROYECTO " +
                            "FROM INCIDENCIA " +
                            "LEFT JOIN USUARIO ON INCIDENCIA.USUARIO_RESPONSABLE = USUARIO.ID_USUARIO " +
                            "LEFT JOIN PROYECTO ON INCIDENCIA.PROYECTO = PROYECTO.ID_PROYECTO " +
                            "WHERE INCIDENCIA.PROYECTO = ?");
            preparedStatement.setInt(1, idProyecto);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Incidencia incidencia = new Incidencia();
                Usuario usuario = new Usuario();
                Proyecto proyecto = new Proyecto();

                incidencia.setIdIncidencia(rs.getInt("ID_INCIDENCIA"));
                incidencia.setDescripcion(rs.getString("DESCRIPCION"));
                incidencia.setEstado(incidencia.getEstadoID(rs.getInt("ID_ESTADO")));
                incidencia.setEstimacionHoras(rs.getDouble("ESTIMACION_HORAS"));
                incidencia.setTiempoInvertido(rs.getDouble("TIEMPO_INVERTIDO"));
                usuario.setIdUsuario(rs.getInt("USUARIO_RESPONSABLE"));
                usuario.setNombreUsuario(rs.getString("NOMBRE_USUARIO"));
                proyecto.setIdProyecto(rs.getInt("PROYECTO"));
                proyecto.setNombreProyecto(rs.getString("NOMBRE_PROYECTO"));

                incidencia.setUsuarioResponsable(usuario);
                incidencia.setProyecto(proyecto);

                incidencias.add(incidencia);
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
        return incidencias;
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

            preparedStatement = connection.prepareStatement("SELECT INCIDENCIA.*, USUARIO.NOMBRE_USUARIO, PROYECTO.NOMBRE_PROYECTO FROM INCIDENCIA " +
                    "LEFT JOIN USUARIO ON INCIDENCIA.USUARIO_RESPONSABLE = USUARIO.ID_USUARIO " +
                    "LEFT JOIN PROYECTO ON INCIDENCIA.PROYECTO = PROYECTO.ID_PROYECTO " +
                    "WHERE INCIDENCIA.ID_ESTADO != 6;");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                incidencia = new Incidencia();
                usuario = new Usuario();
                proyecto = new Proyecto();
                incidencia.setIdIncidencia(rs.getInt("ID_INCIDENCIA"));
                incidencia.setDescripcion(rs.getString("DESCRIPCION"));
                incidencia.setEstimacionHoras(rs.getDouble("ESTIMACION_HORAS"));
                incidencia.setEstado(incidencia.getEstadoID(rs.getInt("ID_ESTADO")));
                incidencia.setTiempoInvertido(rs.getDouble("TIEMPO_INVERTIDO"));
                usuario.setIdUsuario(rs.getInt("USUARIO_RESPONSABLE"));
                usuario.setNombreUsuario(rs.getString("NOMBRE_USUARIO"));
                proyecto.setIdProyecto(rs.getInt("PROYECTO"));
                proyecto.setNombreProyecto(rs.getString("NOMBRE_PROYECTO"));
                incidencia.setUsuarioResponsable(usuario);
                incidencia.setProyecto(proyecto);
                incidencias.add(incidencia);
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DAOException("Ocurrió un error en la base de datos");
        }
        return incidencias;
    }

    public Incidencia buscarConMovimientos(int id) throws DAOException {
        Incidencia incidencia = this.buscar(id);
        ArrayList<Movimiento> movimientos = serviceMovimiento.buscarPorIncidencia(id);
        incidencia.setMovimientos(movimientos);
        return incidencia;
    }

    @Override
    public int obtenerUltimoID() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT MAX(ID_INCIDENCIA) AS ID FROM INCIDENCIA");
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

    public void cerrar(Incidencia incidencia) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("UPDATE INCIDENCIA SET ID_ESTADO = 6 WHERE ID_INCIDENCIA = ?");
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