package service;

import DAO.DAOUsuario;
import DAO.DAOException;
import model.Usuario;

import java.util.ArrayList;

public class ServiceUsuario {
    private DAOUsuario daoUsuario;

    public ServiceUsuario() {
        daoUsuario = new DAOUsuario();
    }

    public boolean validarUsuario(String usuario, String contrasena) throws ServiceException {
        try {
            return daoUsuario.validarUsuario(usuario, contrasena);
        }
        catch(DAOException d) {
            throw new ServiceException("Error");
        }
    }

    public String validarTipo(int id) throws ServiceException {
        try {
            return daoUsuario.validarTipo(id);
        }
        catch(DAOException d) {
            throw new ServiceException("Error");
        }
    }

    public int obtenerID(String nombreUsuario) throws ServiceException {
        try {
            return daoUsuario.obtenerID(nombreUsuario);
        }
        catch (DAOException d) {
            throw new ServiceException("Error");
        }
    }

    public ArrayList<Integer> obtenerPermisos(int idUsuario) throws ServiceException {
        try {
            return daoUsuario.obtenerPermisos(idUsuario);
        }
        catch(DAOException d) {
            throw new ServiceException("Error");
        }
    }

    public ArrayList<Usuario> buscarTodos() throws ServiceException {
        try {
            return daoUsuario.buscarTodos();
        }
        catch (DAOException d) {
            throw new ServiceException("Error");
        }
    }

    public int obtenerUltimoID() throws ServiceException {
        try {
            return daoUsuario.obtenerUltimoID();
        }
        catch (DAOException d) {
            throw new ServiceException("Error");
        }
    }

    public Usuario buscar(int id) throws ServiceException {
        try {
            return daoUsuario.buscar(id);
        }
        catch (DAOException d) {
            throw new ServiceException("Error");
        }
    }
}
