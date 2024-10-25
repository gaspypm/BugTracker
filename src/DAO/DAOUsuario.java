package DAO;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class DAOUsuario {
    Dotenv dotenv = Dotenv.load();
    String DB_JDBC_DRIVER = dotenv.get("DB_JDBC_DRIVER");
    String DB_URL = dotenv.get("DB_URL");
    String DB_USER = dotenv.get("DB_USER");
    String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    public boolean validarUsuario(String nombreUsuario, String contrasena) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT * FROM USUARIO WHERE NOMBRE_USUARIO = ? AND CONTRASENA = ?");
            preparedStatement.setString(1, nombreUsuario);
            preparedStatement.setString(2, contrasena);

            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (ClassNotFoundException | SQLException e) {
            throw new DAOException("Error al acceder a la base de datos");
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException("Error al cerrar la conexión");
            }
        }
    }

    public String validarTipo(String nombreUsuario) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT TIPO FROM USUARIO WHERE NOMBRE_USUARIO = ?");
            preparedStatement.setString(1, nombreUsuario);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("TIPO");
            } else {
                return null;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error al acceder a la base de datos");
        } finally {
            try {
                preparedStatement.close();
            }
            catch(SQLException e) {
                e.printStackTrace();
                throw new DAOException("Error");
            }
        }
    }

    public int obtenerID(String nombreUsuario) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT ID_USUARIO FROM USUARIO WHERE NOMBRE_USUARIO = ?");
            preparedStatement.setString(1, nombreUsuario);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_USUARIO");
            } else {
                throw new DAOException("Usuario no encontrado: " + nombreUsuario);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error al acceder a la base de datos");
        } finally {
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
