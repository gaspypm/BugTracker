package DAO;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;

public class DAOAdministrador {
    Dotenv dotenv = Dotenv.load();
    String DB_JDBC_DRIVER = dotenv.get("DB_JDBC_DRIVER");
    String DB_URL = dotenv.get("DB_URL");
    String DB_USER = dotenv.get("DB_USER");
    String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    public boolean crearUsuario(String nombreUsuario, String contrasena, String tipo) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("INSERT INTO USUARIO (NOMBRE_USUARIO, CONTRASENIA, TIPO) VALUES (?,?,?)");
            preparedStatement.setString(1, nombreUsuario);
            preparedStatement.setString(2, contrasena);
            preparedStatement.setString(3, tipo);

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

    public ArrayList<Integer> obtenerPermisos(int idUsuario) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ArrayList<Integer> permisos = new ArrayList<>();
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT ID_PERMISO FROM USUARIO_PERMISO WHERE ID_USUARIO = ?");
            preparedStatement.setInt(1, idUsuario);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                permisos.add(rs.getInt("ID_PERMISO"));
            }
            return permisos;

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
                    preparedStatement = connection.prepareStatement("INSERT INTO USUARIO_PERMISO (ID_USUARIO, ID_PERMISO) VALES (?, ?)");
                    preparedStatement.setInt(1, idUsuario);
                    preparedStatement.setInt(2, idPermiso);

                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                }
            }

            for (int idPermiso : permisos) {
                if (!permisos.contains(idPermiso)) {
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
