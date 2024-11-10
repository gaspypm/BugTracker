package DAO;

import io.github.cdimascio.dotenv.Dotenv;
import model.Administrador;
import model.Usuario;

import java.sql.*;
import java.util.ArrayList;

public class DAOUsuario implements IDAO<Usuario> {
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

    public String validarTipo(int id) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT TIPO FROM USUARIO WHERE ID_USUARIO = ?");
            preparedStatement.setInt(1, id);

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
            } catch (SQLException e) {
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
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException("Error");
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

            while (rs.next()) {
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

    public ArrayList<Usuario> buscarTodos() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Usuario usuario = null;
        Administrador administrador = null;
        ArrayList<Usuario> usuarios = new ArrayList<>();

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            preparedStatement = connection.prepareStatement("SELECT *  FROM USUARIO");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                if ("ADMINISTRADOR".equals(rs.getString("TIPO"))) {
                    administrador = new Administrador();
                    administrador.setIdUsuario(rs.getInt("ID_USUARIO"));
                    administrador.setNombreUsuario(rs.getString("NOMBRE_USUARIO"));
                    usuarios.add(administrador);
                } else {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("ID_USUARIO"));
                    usuario.setNombreUsuario(rs.getString("NOMBRE_USUARIO"));
                    usuarios.add(usuario);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new DAOException("Ocurrió un error en la base de datos");
        }
        return usuarios;
    }

    @Override
    public void guardar(Usuario usuario) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int idUsuario;

        if (usuario.getIdUsuario() != 0)
            idUsuario = usuario.getIdUsuario();
        else {
            try {
                idUsuario = obtenerUltimoID() + 1;
            }
            catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            preparedStatement = connection.prepareStatement("INSERT INTO USUARIO (ID_USUARIO, NOMBRE_USUARIO) VALUES (?,?)");

            preparedStatement.setInt(1, idUsuario);
            preparedStatement.setString(2, usuario.getNombreUsuario());

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
    public void modificar(Usuario usuario) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("UPDATE USUARIO SET NOMBRE_USUARIO = ? WHERE ID_USUARIO = ?");
            preparedStatement.setString(1, usuario.getNombreUsuario());
            preparedStatement.setInt(2, usuario.getIdUsuario());

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
            preparedStatement = connection.prepareStatement("DELETE FROM USUARIO WHERE ID_USUARIO=?");
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
    public Usuario buscar(int id) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Usuario usuarioAux = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT * FROM USUARIO WHERE ID_USUARIO = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                usuarioAux = new Usuario();
                usuarioAux.setIdUsuario(rs.getInt("ID_USUARIO"));
                usuarioAux.setNombreUsuario(rs.getString("NOMBRE_USUARIO"));
            }
            return usuarioAux;
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
            preparedStatement = connection.prepareStatement("SELECT MAX(ID_USUARIO) AS id FROM USUARIO");
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
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
