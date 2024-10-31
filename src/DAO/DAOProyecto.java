package DAO;

import io.github.cdimascio.dotenv.Dotenv;
import model.Proyecto;
import java.sql.*;
import java.util.ArrayList;

public class DAOProyecto implements IDAO<Proyecto> {
    Dotenv dotenv = Dotenv.load();
    String DB_JDBC_DRIVER = dotenv.get("DB_JDBC_DRIVER");
    String DB_URL = dotenv.get("DB_URL");
    String DB_USER = dotenv.get("DB_USER");
    String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    public int obtenerID(String nombreProyecto) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT ID_PROYECTO FROM PROYECTO WHERE NOMBRE_PROYECTO = ?");
            preparedStatement.setString(1, nombreProyecto);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_PROYECTO");
            } else {
                throw new DAOException("Proyecto " + nombreProyecto + " no encontrado");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error al acceder a la base de datos");
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException("Error");
            }
        }
    }

    public ArrayList<Proyecto> buscarTodos() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Proyecto proyecto = null;
        ArrayList<Proyecto> proyectos = new ArrayList<>();

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            preparedStatement = connection.prepareStatement("SELECT *  FROM PROYECTO");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                proyecto = new Proyecto();
                proyecto.setIdProyecto(rs.getInt("ID_PROYECTO"));
                proyecto.setNombreProyecto(rs.getString("NOMBRE_PROYECTO"));
                proyectos.add(proyecto);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new DAOException("Ocurrió un error en la base de datos");
        }
        return proyectos;
    }

    @Override
    public void guardar(Proyecto proyecto) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int idProyecto;

        if (proyecto.getIdProyecto() != 0)
            idProyecto = proyecto.getIdProyecto();
        else {
            try {
                idProyecto = obtenerUltimoID() + 1;
            }
            catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            preparedStatement = connection.prepareStatement("INSERT INTO PROYECTO (ID_PROYECTO, NOMBRE_PROYECTO, ID_ADMINISTRADOR) VALUES (?,?,?)");

            preparedStatement.setInt(1, idProyecto);
            preparedStatement.setString(2, proyecto.getNombreProyecto());
            preparedStatement.setInt(3, proyecto.getAdministrador().getIdUsuario());

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
    public void modificar(Proyecto proyecto) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("UPDATE PROYECTO SET NOMBRE_PROYECTO = ? WHERE ID_PROYECTO = ?");
            preparedStatement.setString(1, proyecto.getNombreProyecto());
            preparedStatement.setInt(2, proyecto.getIdProyecto());

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
            preparedStatement = connection.prepareStatement("DELETE FROM PROYECTO WHERE ID_PROYECTO=?");
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
    public Proyecto buscar(int id) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Proyecto proyectoAux = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT * FROM PROYECTO WHERE ID_PROYECTO = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                proyectoAux = new Proyecto();
                proyectoAux.setIdProyecto(rs.getInt("ID_PROYECTO"));
                proyectoAux.setNombreProyecto(rs.getString("NOMBRE_PROYECTO"));
            }
            return proyectoAux;
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
    public int obtenerUltimoID() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT MAX(ID_PROYECTO) AS ID FROM PROYECTO");
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next())
                return rs.getInt("ID");
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
}
