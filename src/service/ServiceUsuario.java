package service;

import DAO.DAOUsuario;
import DAO.DAOException;

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

    public String validarTipo(String tipo) throws ServiceException {
        try {
            return daoUsuario.validarTipo(tipo);
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
}
