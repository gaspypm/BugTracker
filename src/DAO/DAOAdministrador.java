package DAO;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;

public class DAOAdministrador extends DAOUsuario {
    Dotenv dotenv = Dotenv.load();
    String DB_JDBC_DRIVER = dotenv.get("DB_JDBC_DRIVER");
    String DB_URL = dotenv.get("DB_URL");
    String DB_USER = dotenv.get("DB_USER");
    String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    public void crearUsuario(int id, String nombreUsuario, char[] contrasena, String tipo) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("INSERT INTO USUARIO (ID_USUARIO, NOMBRE_USUARIO, CONTRASENA, TIPO) VALUES (?,?,?,?)");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, nombreUsuario);
            preparedStatement.setString(3, String.valueOf(contrasena));
            preparedStatement.setString(4, tipo);

            int i = preparedStatement.executeUpdate();
            System.out.println(i);
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

    public void editarPermisos(int idUsuario, ArrayList<Integer> permisosSeleccionados) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ArrayList<Integer> permisos = obtenerPermisos(idUsuario);

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            for (int idPermiso : permisosSeleccionados) {
                if (!permisos.contains(idPermiso)) {
                    preparedStatement = connection.prepareStatement("INSERT INTO USUARIO_PERMISO (ID_USUARIO, ID_PERMISO) VALUES (?, ?)");
                    preparedStatement.setInt(1, idUsuario);
                    preparedStatement.setInt(2, idPermiso);

                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                }
            }

            for (int idPermiso : permisos) {
                if (!permisosSeleccionados.contains(idPermiso)) {
                    preparedStatement = connection.prepareStatement("DELETE FROM USUARIO_PERMISO WHERE ID_USUARIO = ? AND ID_PERMISO = ?");
                    preparedStatement.setInt(1, idUsuario);
                    preparedStatement.setInt(2, idPermiso);

                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                }
            }
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

    public boolean crearProyecto(String nombreUsuario) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT TIPO FROM USUARIO WHERE NOMBRE_USUARIO = ?");
            preparedStatement.setString(1, nombreUsuario);

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
}
